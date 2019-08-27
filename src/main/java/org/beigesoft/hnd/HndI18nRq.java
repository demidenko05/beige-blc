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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.Lng;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.HlMaFrCl;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.UtlJsp;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.srv.INumStr;

/**
 * <p>It handles request internationalization and other preferences.
 * It adds base services and variables to request attributes.
 * It's the first handler for any request.</p>
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
  private IOrm orm;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>JSP utility.</p>
   */
  private UtlJsp utJsp;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Print number service.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Holder of classes to match.</p>
   **/
  private HlMaFrCl hlMaFrCl;

  /**
   * <p>Holder transformed UVD settings, other holders and vars.</p>
   */
  private HldUvd hldUvd;

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
   * @param pRvs Request scoped variables
   * @param pRqd Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqd) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 5500);
    if (dbgSh) {
      this.log.debug(pRvs, HndI18nRq.class,
    "Request user/URL/rem.user/addr/host/port/locale: " + pRqd.getUsrNm()
  + "/" + pRqd.getReqUrl() + "/" + pRqd.getRemUsr() + "/" + pRqd.getRemAddr()
+ "/" + pRqd.getRemHost() + "/" + pRqd.getRemPort() + "/" + pRqd.getLocale());
      StringBuffer prs = new StringBuffer();
      for (Map.Entry<String, String[]> enr : pRqd.getParamMap().entrySet()) {
        prs.append(enr.getKey() + "-" + Arrays.toString(enr.getValue()) + "; ");
      }
      this.log.debug(pRvs, HndI18nRq.class, "Request parameters: " + prs);
      this.log.debug(pRvs, HndI18nRq.class, "Request cookies: "
        + Arrays.toString(pRqd.getCookies()));
    }
    //unshared references of the latest versions of shared data:
    Map<String, Object> vs = new HashMap<String, Object>();
    List<UsPrf> upfsTmp = null;
    List<Lng> lgsTmp = null;
    List<DcSp> dssTmp = null;
    List<DcGrSp> dgssTmp = null;
    boolean tmpReady = false;
    if (this.usPrfs == null) {
      synchronized (this) {
        if (this.usPrfs == null) {
          try {
            this.log.info(pRvs, HndI18nRq.class, "Refreshing preferences...");
            this.rdb.setAcmt(false);
            this.rdb.setTrIsl(IRdb.TRRUC);
            this.rdb.begin();
            upfsTmp = this.orm.retLst(pRvs, vs, UsPrf.class);
            lgsTmp = this.orm.retLst(pRvs, vs, Lng.class);
            dssTmp = this.orm.retLst(pRvs, vs, DcSp.class);
            dgssTmp = this.orm.retLst(pRvs, vs, DcGrSp.class);
            this.rdb.commit();
            //assigning fully initialized data:
            this.dcGrSps = dgssTmp;
            this.dcSps = dssTmp;
            this.lngs = lgsTmp;
            this.usPrfs = upfsTmp;
            tmpReady = true;
          } catch (Exception ex) {
            if (!this.rdb.getAcmt()) {
              this.rdb.rollBack();
            }
            throw ex;
          } finally {
            this.rdb.release();
          }
        }
      }
    }
    if (!tmpReady) {
      upfsTmp = this.usPrfs;
      lgsTmp = this.lngs;
      dssTmp = this.dcSps;
      dgssTmp = this.dcGrSps;
      //it will be error in case when other thread is clearing these unlocked
      //data for refreshing
    }
    UsPrf upf = revUsPrf(pRqd, lgsTmp, dssTmp, dgssTmp, upfsTmp);
    CmnPrf cpf = revCmnPrf(pRqd, upf);
    for (UsPrf upft : upfsTmp) {
      if (upft.getDef()) {
        cpf.setLngDef(upft.getLng());
        break;
      }
    }
    if (upfsTmp.size() == 0 || cpf.getLngDef() == null) {
      this.log.error(pRvs, HndI18nRq.class,
        "There is no default user preferences!");
      cpf.setLngDef(upf.getLng());
    }
    Locale locCurr;
    if (pRqd.getLocale().getLanguage().equals(upf.getLng().getIid())) {
      locCurr = pRqd.getLocale();
    } else {
      locCurr = new Locale(upf.getLng().getIid());
    }
    cpf.setUsLoc(locCurr);
    pRvs.put("upf", upf);
    pRvs.put("cpf", cpf);
    pRvs.put("lngs", this.lngs);
    pRvs.put("dcSps", this.dcSps);
    pRvs.put("dcGrSps", this.dcGrSps);
    pRvs.put("usPrfs", this.usPrfs);
    pRqd.setAttr("hldUvd", this.hldUvd);
    pRqd.setAttr("utJsp", this.utJsp);
    pRqd.setAttr("i18n", this.i18n);
    pRqd.setAttr("srvDt", this.srvDt);
    pRqd.setAttr("numStr", this.numStr);
    pRqd.setAttr("hlMaFrCl", this.hlMaFrCl);
  }

  /**
   * <p>Reveals common preferences from user preferences.</p>
   * @param pRqd Request Data
   * @param pUpf UsPrf
   * @return common preferences
   */
  public final CmnPrf revCmnPrf(final IReqDt pRqd, final UsPrf pUpf) {
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
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 5501);
    boolean ndStCk = false;
    //check user request changing preferences:
    String pgSz = pRqd.getParam("pgSz");
    if (dbgSh) {
      this.log.debug(null, HndI18nRq.class, "Request pgSz: " + pgSz);
    }
    if (pgSz != null) {
      ndStCk = true;
    } else {
      //use from cookie:
      pgSz = pRqd.getCookVl("pgSz");
      if (dbgSh) {
        this.log.debug(null, HndI18nRq.class, "Cookie pgSz: " + pgSz);
      }
    }
    if (pgSz != null) {
      // from request or cookie:
      cpf.setPgSz(Integer.valueOf(pgSz));
      if (cpf.getPgSz() > 100) {
        cpf.setPgSz(100);
      } else if (cpf.getPgSz() < 5) {
        cpf.setPgSz(5);
      }
    }
    if (ndStCk) {
      pRqd.setCookVl("pgSz", cpf.getPgSz().toString());
      if (dbgSh) {
        this.log.debug(null, HndI18nRq.class, "Set cookie to pgSz: " + pgSz);
      }
    }
    return cpf;
  }

  /**
   * <p>Reveals user preferences from request/cookie/stored/system and adds
   * them into cookie if need.</p>
   * @param pRqd Request Data
   * @param pLngs Lngs
   * @param pDcSps DcSps
   * @param pDcGrSps DcGrSps
   * @param pUsPrfs UsPrfs
   * @return user preferences
   * @throws Exception - an exception
   */
  public final UsPrf revUsPrf(final IReqDt pRqd, final List<Lng> pLngs,
    final List<DcSp> pDcSps, final List<DcGrSp> pDcGrSps,
      final List<UsPrf> pUsPrfs) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 5502);
    UsPrf upf = null;
    boolean ndStCk = false;
    //check user request changing preferences:
    String lng = pRqd.getParam("lng");
    String dcSp = pRqd.getParam("dcSp");
    String dcGrSp = pRqd.getParam("dcGrSp");
    String dgInGr = pRqd.getParam("dgInGr");
    if (dbgSh) {
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
      lng = pRqd.getCookVl("lng");
      dcSp = pRqd.getCookVl("dcSp");
      dcGrSp = pRqd.getCookVl("dcGrSp");
      dgInGr = pRqd.getCookVl("dgInGr");
      if (dbgSh) {
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
      upf = revUsPrfDb(pRqd, pLngs,  pDcSps, pDcGrSps, pUsPrfs);
      ndStCk = true;
    }
    if (upf == null) {
      // reveal from system settings:
      upf = revUsPrfSys(pRqd, pLngs,  pDcSps, pDcGrSps, pUsPrfs);
      if (dbgSh) {
        this.log.debug(null, HndI18nRq.class, "Use system lng/dcSp/dcGrSp: "
          + upf.getLng() .getIid() + "/" + upf.getDcSp().getIid() + "/"
            + upf.getDcGrSp().getIid());
      }
      ndStCk = true;
    }
    if (ndStCk) {
      pRqd.setCookVl("dgInGr", upf.getDgInGr().toString());
      pRqd.setCookVl("lng", upf.getLng().getIid());
      pRqd.setCookVl("dcSp", upf.getDcSp().getIid());
      pRqd.setCookVl("dcGrSp", upf.getDcGrSp().getIid());
      if (dbgSh) {
        this.log.debug(null, HndI18nRq.class, "Set cookie to lng/dcSp/dcGrSp: "
          + upf.getLng() .getIid() + "/" + upf.getDcSp().getIid() + "/"
            + upf.getDcGrSp().getIid());
      }
    }
    return upf;
  }

  /**
   * <p>Reveals user preferences from system settings.</p>
   * @param pRqd Request Data
   * @param pLngs Lngs
   * @param pDcSps DcSps
   * @param pDcGrSps DcGrSps
   * @param pUsPrfs UsPrfs
   * @return user preferences
   * @throws Exception - an exception
   */
  public final UsPrf revUsPrfSys(final IReqDt pRqd, final List<Lng> pLngs,
    final List<DcSp> pDcSps, final List<DcGrSp> pDcGrSps,
      final List<UsPrf> pUsPrfs) throws Exception {
    // reveal from system settings:
    UsPrf upf = new UsPrf();
    Locale lc = pRqd.getLocale();
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
   * @param pRqd Request Data
   * @param pLngs Lngs
   * @param pDcSps DcSps
   * @param pDcGrSps DcGrSps
   * @param pUsPrfs UsPrfs
   * @return user preferences if stored or null
   * @throws Exception - an exception
   */
  public final UsPrf revUsPrfDb(final IReqDt pRqd, final List<Lng> pLngs,
    final List<DcSp> pDcSps, final List<DcGrSp> pDcGrSps,
      final List<UsPrf> pUsPrfs) throws Exception {
    UsPrf upf = null;
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 5503);
    // reveal from stored preferences:
    // try match client's locale, if not - default or the first:
    String ccountry = null;
    String clang = null;
    if (pRqd.getLocale() != null) {
      ccountry = pRqd.getLocale().getCountry();
      clang = pRqd.getLocale().getLanguage();
      if (dbgSh) {
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
      if (dbgSh) {
    this.log.debug(null, HndI18nRq.class, "Full match lng/dcSp/dcGrSp/dgInGr: "
  + upf.getLng().getIid() + "/" + upf.getDcSp().getIid() + "/"
+ upf.getDcGrSp().getIid() + "/" + upf.getDgInGr());
      }
    } else if (upfMl != null) {
      upf = upfMl;
      if (dbgSh) {
    this.log.debug(null, HndI18nRq.class, "Lang match lng/dcSp/dcGrSp/dgInGr: "
  + upf.getLng().getIid() + "/" + upf.getDcSp().getIid() + "/"
+ upf.getDcGrSp().getIid() + "/" + upf.getDgInGr());
      }
    } else {
      upf = upfDef;
      if (dbgSh) {
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
   * @return IOrm
   **/
  public final synchronized IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final synchronized void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for i18n.</p>
   * @return II18n
   **/
  public final II18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final II18n pI18n) {
    this.i18n = pI18n;
  }

  /**
   * <p>Getter for utJsp.</p>
   * @return UtlJsp
   **/
  public final UtlJsp getUtJsp() {
    return this.utJsp;
  }

  /**
   * <p>Setter for utJsp.</p>
   * @param pUtJsp reference
   **/
  public final void setUtJsp(final UtlJsp pUtJsp) {
    this.utJsp = pUtJsp;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }

  /**
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }

  /**
   * <p>Getter for hlMaFrCl.</p>
   * @return HlMaFrCl
   **/
  public final HlMaFrCl getHlMaFrCl() {
    return this.hlMaFrCl;
  }

  /**
   * <p>Setter for hlMaFrCl.</p>
   * @param pHlMaFrCl reference
   **/
  public final void setHlMaFrCl(final HlMaFrCl pHlMaFrCl) {
    this.hlMaFrCl = pHlMaFrCl;
  }

  /**
   * <p>Getter for hldUvd.</p>
   * @return HldUvd
   **/
  public final HldUvd getHldUvd() {
    return this.hldUvd;
  }

  /**
   * <p>Setter for hldUvd.</p>
   * @param pHldUvd reference
   **/
  public final void setHldUvd(final HldUvd pHldUvd) {
    this.hldUvd = pHldUvd;
  }
}
