/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.hnd;

import java.util.List;
import java.util.Map;
import java.util.Locale;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.Lng;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.srv.IRdb;
import org.beigesoft.srv.IOrm;

/**
 * <p>It handles request internationalization and other preferences.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndI18nRq<RS> implements IHndRq, IHndCh {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Database service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   */
  private IOrm<RS> orm;

  //Cached data:
  /**
   * <p>Cached user preferences.</p>
   */
  private List<UsPrf> usPrfs;

  /**
   * <p>Cached languages.</p>
   */
  private List<Lng> lngs;

  /**
   * <p>Cached decimal separators.</p>
   */
  private List<DcSp> dcSps;

  /**
   * <p>Cached decimal group separators.</p>
   */
  private List<DcGrSp> dcGrSps;

  /**
   * <p>Handle request. Synchronization whole code may hit performance,
   * so this service is locked only during shared data changing.
   * Shared data references is copied into temporary references
   * for shared usage.
   * Any way, it's hardly ever happens changing preferences in DB.</p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt) throws Exception {
    //unlocked references of the latest versions of shared data:
    List<UsPrf> upfsTmp = null;
    List<Lng> lgsTmp = null;
    List<DcSp> dssTmp = null;
    List<DcGrSp> dgssTmp = null;
    if (this.usPrfs == null) {
      synchronized (this) {
        if (this.usPrfs == null) {
          try {
            this.log.info(pRqVs, HndI18nRq.class, "Refreshing preferences...");
            this.rdb.setAcmt(false);
            this.rdb.setTrIsl(IRdb.TRRUC);
            this.rdb.begin();
            List<UsPrf> upfs = this.orm.retLst(pRqVs, null, UsPrf.class);
            List<Lng> lgs = this.orm.retLst(pRqVs, null, Lng.class);
            List<DcSp> dss = this.orm.retLst(pRqVs, null, DcSp.class);
            List<DcGrSp> dgss = this.orm.retLst(pRqVs, null, DcGrSp.class);
            this.rdb.commit();
            //assigning fully initialized data:
            this.dcGrSps = dgss;
            this.dcSps = dss;
            this.lngs = lgs;
            this.usPrfs = upfs;
          } catch (Exception ex) {
            if (!this.rdb.getAcmt()) {
              this.rdb.rollBack();
            }
            throw ex;
          } finally {
            this.rdb.release();
          }
        }
        upfsTmp = this.usPrfs;
        lgsTmp = this.lngs;
        dssTmp = this.dcSps;
        dgssTmp = this.dcGrSps;
      }
    }
    upfsTmp = this.usPrfs;
    lgsTmp = this.lngs;
    dssTmp = this.dcSps;
    dgssTmp = this.dcGrSps;
    if (upfsTmp == null || lgsTmp == null || dssTmp == null
      || dgssTmp == null) {
      synchronized (this) { //waiting for another thread refresh data
        upfsTmp = this.usPrfs;
        lgsTmp = this.lngs;
        dssTmp = this.dcSps;
        dgssTmp = this.dcGrSps;
      }
    }
    UsPrf upf = revUsPrf(pRqDt, lgsTmp, dssTmp, dgssTmp, upfsTmp);
    CmnPrf cpf = revCmnPrf(upf);
    for (UsPrf upft : upfsTmp) {
      if (upft.getDef()) {
        cpf.setLngDef(upft.getLng());
        break;
      }
    }
    if (upfsTmp.size() == 0 || cpf.getLngDef() == null) {
      this.log.error(pRqVs, HndI18nRq.class,
        "There is no default user preferences!");
      cpf.setLngDef(upf.getLng());
    }
    Locale locCurr;
    if (pRqDt.getLocale().getLanguage().equals(upf.getLng().getIid())) {
      locCurr = pRqDt.getLocale();
    } else {
      locCurr = new Locale(upf.getLng().getIid());
    }
    cpf.setUsLoc(locCurr);
    pRqVs.put("upf", upf);
    pRqVs.put("cpf", cpf);
  }

  /**
   * <p>Reveals common preferences from user preferences.</p>
   * @param pUpf UsPrf
   * @return common preferences
   */
  public final CmnPrf revCmnPrf(final UsPrf pUpf) {
    CmnPrf cpf = new CmnPrf();
    if (pUpf.getDcSp().getIid().equals(DcSp.SPACEID)) {
      cpf.setDcSpv(DcSp.SPACEVL);
    } else if (pUpf.getDcSp().getIid().equals(DcSp.EMPTYID)) {
      cpf.setDcSpv(DcSp.EMPTYVL);
    } else {
      cpf.setDcSpv(pUpf.getDcSp().getIid());
    }
    if (pUpf.getDcGrSp().getIid().equals(DcSp.SPACEID)) {
      cpf.setDcGrSpv(DcSp.SPACEVL);
    } else if (pUpf.getDcGrSp().getIid().equals(DcSp.EMPTYID)) {
      cpf.setDcGrSpv(DcSp.EMPTYVL);
    } else {
      cpf.setDcGrSpv(pUpf.getDcGrSp().getIid());
    }
    return cpf;
  }

  /**
   * <p>Reveals user preferences from request/cookie/stored/system and adds
   * them into cookie if need.</p>
   * @param pRqDt Request Data
   * @param pLngs Lngs
   * @param pDcSps DcSps
   * @param pDcGrSps DcGrSps
   * @param pUsPrfs UsPrfs
   * @return user preferences
   * @throws Exception - an exception
   */
  public final UsPrf revUsPrf(final IReqDt pRqDt, final List<Lng> pLngs,
    final List<DcSp> pDcSps, final List<DcGrSp> pDcGrSps,
      final List<UsPrf> pUsPrfs) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 5201 && this.log.getDbgCl() > 5199;
    UsPrf upf = null;
    boolean ndStCk = false;
    //check user request changing preferences:
    String lng = pRqDt.getParam("lng");
    String dcSp = pRqDt.getParam("dcSp");
    String dcGrSp = pRqDt.getParam("dcGrSp");
    String dgInGr = pRqDt.getParam("dgInGr");
    if (isDbgSh) {
      this.log.debug(null, HndI18nRq.class, "Request lng/dcSp/dcGrSp/dgInGr: "
        + lng + "/" + dcSp + "/" + dcGrSp + "/" + dgInGr);
    }
    if (lng == null || lng.length() == 0
      || dcSp == null || dcSp.length() == 0
        || dcGrSp == null || dcGrSp.length() == 0
          || dgInGr == null || dgInGr.length() == 0) {
      lng = null;
      dcSp = null;
      dcGrSp = null;
      dgInGr = null;
    } else {
      ndStCk = true;
    }
    if (dcGrSp == null && dcSp == null && lng == null) {
      //use from cookie:
      lng = pRqDt.getCookVl("lng");
      dcSp = pRqDt.getCookVl("dcSp");
      dcGrSp = pRqDt.getCookVl("dcGrSp");
      dgInGr = pRqDt.getCookVl("dgInGr");
      if (isDbgSh) {
        this.log.debug(null, HndI18nRq.class, "Cookie lng/dcSp/dcGrSp/dgInGr: "
          + lng + "/" + dcSp + "/" + dcGrSp + "/" + dgInGr);
      }
    }
    if (dcGrSp != null && dcSp != null && lng != null && dgInGr != null) {
      // from request or cookie:
      if (dcGrSp.equals(dcSp)) {
        this.log.error(null, HndI18nRq.class,
           "Separators are same!! dcSp/dcGrSp: " + dcSp);
      } else {
        //try to match from cookies or changed by user:
        upf = new UsPrf();
        upf.setDgInGr(Integer.parseInt(dgInGr));
        for (Lng ln : pLngs) {
          if (ln.getIid().equals(lng)) {
            upf.setLng(ln);
            break;
          }
        }
        if (upf.getLng() == null) {
          upf = null;
        } else {
          for (DcSp ds : pDcSps) {
            if (ds.getIid().equals(dcSp)) {
              upf.setDcSp(ds);
              break;
            }
          }
          if (upf.getDcSp() == null) {
            upf = null;
          } else {
            for (DcGrSp dgs : pDcGrSps) {
              if (dgs.getIid().equals(dcGrSp)) {
                upf.setDcGrSp(dgs);
                break;
              }
            }
            if (upf.getDcGrSp() == null) {
              upf = null;
            }
          }
        }
      }
    }
    if (upf == null && pUsPrfs.size() > 0) {
      // reveal from stored preferences:
      // try match client's locale, if not - default or the first:
      upf = revUsPrfDb(pRqDt, pLngs,  pDcSps, pDcGrSps, pUsPrfs);
      ndStCk = true;
    }
    if (upf == null) {
      // reveal from system settings:
      upf = revUsPrfSys(pRqDt, pLngs,  pDcSps, pDcGrSps, pUsPrfs);
      if (isDbgSh) {
        this.log.debug(null, HndI18nRq.class, "Use system lng/dcSp/dcGrSp: "
          + upf.getLng() .getIid() + "/" + upf.getDcSp().getIid() + "/"
            + upf.getDcGrSp().getIid());
      }
      ndStCk = true;
    }
    if (ndStCk) {
      pRqDt.setCookVl("dgInGr", upf.getDgInGr().toString());
      pRqDt.setCookVl("lng", upf.getLng().getIid());
      pRqDt.setCookVl("dcSp", upf.getDcSp().getIid());
      pRqDt.setCookVl("dcGrSp", upf.getDcGrSp().getIid());
      if (isDbgSh) {
        this.log.debug(null, HndI18nRq.class, "Set cookie to lng/dcSp/dcGrSp: "
          + upf.getLng() .getIid() + "/" + upf.getDcSp().getIid() + "/"
            + upf.getDcGrSp().getIid());
      }
    }
    return upf;
  }

  /**
   * <p>Reveals user preferences from system settings.</p>
   * @param pRqDt Request Data
   * @param pLngs Lngs
   * @param pDcSps DcSps
   * @param pDcGrSps DcGrSps
   * @param pUsPrfs UsPrfs
   * @return user preferences
   * @throws Exception - an exception
   */
  public final UsPrf revUsPrfSys(final IReqDt pRqDt, final List<Lng> pLngs,
    final List<DcSp> pDcSps, final List<DcGrSp> pDcGrSps,
      final List<UsPrf> pUsPrfs) throws Exception {
    // reveal from system settings:
    UsPrf upf = new UsPrf();
    Locale lc = pRqDt.getLocale();
    if (lc == null) {
      lc = Locale.getDefault();
    }
    if (pLngs.size() > 0) {
      for (Lng ln : pLngs) {
        if (ln.getIid().equals(lc.getLanguage())) {
          upf.setLng(ln);
          break;
        }
      }
    }
    if (upf.getLng() == null) {
      Lng ln = new Lng();
      ln.setIid(lc.getLanguage());
      ln.setNme(lc.getLanguage());
      upf.setLng(ln);
    }
    if (pDcSps.size() > 0) {
     upf.setDcSp(pDcSps.get(0));
    }
    if (upf.getDcSp() == null) {
      DcSp sp = new DcSp();
      sp.setIid(".");
      sp.setNme("Dot");
      upf.setDcSp(sp);
    }
    if (pDcGrSps.size() > 0) {
     upf.setDcGrSp(pDcGrSps.get(0));
    }
    if (upf.getDcGrSp() == null) {
      DcGrSp sp = new DcGrSp();
      sp.setIid(",");
      sp.setNme("Comma");
      upf.setDcGrSp(sp);
    }
    return upf;
  }

  /**
   * <p>Reveals user preferences from stored ones.</p>
   * @param pRqDt Request Data
   * @param pLngs Lngs
   * @param pDcSps DcSps
   * @param pDcGrSps DcGrSps
   * @param pUsPrfs UsPrfs
   * @return user preferences if stored or null
   * @throws Exception - an exception
   */
  public final UsPrf revUsPrfDb(final IReqDt pRqDt, final List<Lng> pLngs,
    final List<DcSp> pDcSps, final List<DcGrSp> pDcGrSps,
      final List<UsPrf> pUsPrfs) throws Exception {
    UsPrf upf = null;
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 5202 && this.log.getDbgCl() > 5200;
    // reveal from stored preferences:
    // try match client's locale, if not - default or the first:
    String ccountry = null;
    String clang = null;
    if (pRqDt.getLocale() != null) {
      ccountry = pRqDt.getLocale().getCountry();
      clang = pRqDt.getLocale().getLanguage();
      if (isDbgSh) {
        this.log.debug(null, HndI18nRq.class,
          "Client prefers lng/country: " + clang + "/" + ccountry);
      }
    }
    UsPrf upfMf = null; //full match
    UsPrf upfMl = null; //language match
    UsPrf upfDef = null; //default
    for (UsPrf upft : pUsPrfs) {
      if (upft.getCntr().getIid().equals(ccountry)
        && upft.getLng().getIid().equals(clang)) {
        upfMf = upft;
        break;
      }
      if (upft.getLng().getIid().equals(clang)) {
        upfMl = upft;
      }
      if (upft.getDef()) {
        upfDef = upft;
      } else if (upfDef == null) {
        upfDef = upft;
      }
    }
    if (upfMf != null) {
      upf = upfMf;
      if (isDbgSh) {
    this.log.debug(null, HndI18nRq.class, "Full match lng/dcSp/dcGrSp/dgInGr: "
  + upf.getLng().getIid() + "/" + upf.getDcSp().getIid() + "/"
+ upf.getDcGrSp().getIid() + "/" + upf.getDgInGr());
      }
    } else if (upfMl != null) {
      upf = upfMl;
      if (isDbgSh) {
    this.log.debug(null, HndI18nRq.class, "Lang match lng/dcSp/dcGrSp/dgInGr: "
  + upf.getLng().getIid() + "/" + upf.getDcSp().getIid() + "/"
+ upf.getDcGrSp().getIid() + "/" + upf.getDgInGr());
      }
    } else {
      upf = upfDef;
      if (isDbgSh) {
    this.log.debug(null, HndI18nRq.class, "Default lng/dcSp/dcGrSp/dgInGr: "
  + upf.getLng().getIid() + "/" + upf.getDcSp().getIid() + "/"
+ upf.getDcGrSp().getIid() + "/" + upf.getDgInGr());
      }
    }
    return upf;
  }


  /**
   * <p>Handle data changed event.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void hndChange() throws Exception {
    this.usPrfs = null;
    this.lngs = null;
    this.dcSps = null;
    this.dcGrSps = null;
    this.log.info(null, HndI18nRq.class, "User preferences changes handled.");
  }

  //Simple getters and setters (their synchronization never hit performance):
  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final synchronized ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final synchronized void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final synchronized IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final synchronized void setRdb(
    final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for orm.</p>
   * @return IOrm<RS>
   **/
  public final synchronized IOrm<RS> getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final synchronized void setOrm(final IOrm<RS> pOrm) {
    this.orm = pOrm;
  }
}
