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

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.fct.IFctPrcEnt;
import org.beigesoft.fct.IFctPrc;
import org.beigesoft.fct.IFcClFcRq;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.cnv.IFilEntRq;
import org.beigesoft.prc.IPrc;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Handler for entity requests like "create, update, delete, list".
 * According "Beigesoft business logic programming".
 * All requests requires transaction.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndEntRq<RS> implements IHndRq {

  /**
   * <p>Standard logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>Security logger.</p>
   **/
  private ILog logSec;

  /**
   * <p>Database service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>Service that fill entity from request.</p>
   **/
  private IFilEntRq filEntRq;

  /**
   * <p>Entities factories factory.</p>
   **/
  private IFcClFcRq fctFctEnt;

  /**
   * <p>Entities processors names holder.</p>
   **/
  private IHlNmClSt hldEntPrcNm;

  /**
   * <p>Entities processors factory.</p>
   **/
  private IFctPrcEnt fctEntPrc;

  /**
   * <p>Processors for entities names holder.</p>
   **/
  private IHlNmClSt hldPrcFenNm;

  /**
   * <p>Processors for entities factory.</p>
   **/
  private IFctPrc fctPrcFen;

  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<? extends IHasId<?>>> entMap;

  /**
   * <p>Transaction isolation for changing DB phase.</p>
   **/
  private Integer writeTi = IRdb.TRRUC;

  /**
   * <p>Transaction isolation for reading DB phase.</p>
   **/
  private Integer readTi = IRdb.TRRC;

  /**
   * <p>Transaction isolation for writing and reading DB phase.</p>
   **/
  private Integer writeReTi = IRdb.TRRUC;

  /**
   * <p>Writing and reading phases in separated transactions.</p>
   **/
  private Boolean wrReSpTr = Boolean.FALSE;

  /**
   * <p>Handle request.
   * actions that change database use read_commited TI
   * this usually required only for complex transactions like loading
   * or withdrawal warehouse (receipt or issue).
   * WHandlerAndJsp requires handle NULL request, so if parameter
   * "ent" is null then do nothing.
   * </p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String entNm = pRqDt.getParam("ent");
    if (entNm == null) { //dsk/mbl.jsp
      return;
    }
    Class<? extends IHasId<?>> cls = this.entMap.get(entNm);
    if (cls == null) {
      this.logSec.error(pRvs, HndEntRq.class,
    "Trying to work with forbidden ent/host/addr/port/user: " + entNm + "/"
  + pRqDt.getRemHost() + "/" + pRqDt.getRemAddr() + "/"
+ pRqDt.getRemPort() + "/" + pRqDt.getUsrNm());
      throw new ExcCode(ExcCode.FORB, "FORB");
    }
    UvdVar uvs = new UvdVar();
    pRvs.put("uvs", uvs);
    boolean dbgSh = getLogStd().getDbgSh(this.getClass(), 5550);
    String[] actArr = pRqDt.getParam("act").split(",");
    for (String actNm : actArr) {
      if (dbgSh) {
        this.logStd.debug(pRvs, HndEntRq.class,
          "Action: " + actNm);
      }
    }
    if (this.wrReSpTr && ("entSv".equals(actArr[0])
  || "entOwSv".equals(actArr[0]) || "entDl".equals(actArr[0])
|| "entOwDl".equals(actArr[0]))) {
      hndChngIsl(pRvs, pRqDt, cls, actArr, dbgSh, entNm);
    } else {
      hndNoChngIsl(pRvs, pRqDt, cls, actArr, dbgSh, entNm);
    }
  }

  /**
   * <p>Handle request without changing transaction isolation.</p>
   * @param <T> entity type
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @param pCls Entity Class
   * @param pActArr Actions Array
   * @param pIsDbgSh Is Show Debug Messages
   * @param pNmEnt entity name
   * @throws Exception - an exception
   */
  public final <T extends IHasId<?>> void hndChngIsl(
    final Map<String, Object> pRvs, final IReqDt pRqDt, final Class<T> pCls,
      final String[] pActArr, final boolean pIsDbgSh,
        final String pNmEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    Class<T> cls = pCls;
    IHasId<?> ent = null;
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.writeTi);
      this.rdb.begin();
      IFctRq<T> entFac = this.fctFctEnt.laz(pRvs, cls);
      ent = entFac.create(pRvs);
      this.filEntRq.fill(pRvs, vs, ent, pRqDt);
      String entProcNm = this.hldEntPrcNm.get(cls, pActArr[0]);
      if (entProcNm == null) {
        this.logSec.error(null, HndEntRq.class,
          "Trying to work with forbidden ent/action/user: " + ent + "/"
            + pActArr[0] + "/" + pRqDt.getUsrNm());
        throw new ExcCode(ExcCode.FORB,
          "Forbidden!");
      }
      @SuppressWarnings("unchecked")
      IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
        this.fctEntPrc.laz(pRvs, entProcNm);
      if (pIsDbgSh) {
        this.logStd.debug(pRvs, HndEntRq.class,
          "CHANGING transaction use entProcNm/IPrcEnt: " + entProcNm
            + "/" + ep.getClass());
      }
      ent = ep.process(pRvs, ent, pRqDt);
      this.rdb.commit();
    } catch (Exception ex) {
      @SuppressWarnings("unchecked")
      Set<IHnTrRlBk> hnsTrRlBk = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
      if (hnsTrRlBk != null) {
        pRvs.remove(IHnTrRlBk.HNSTRRLBK);
        for (IHnTrRlBk hnTrRlBk : hnsTrRlBk) {
          try {
            hnTrRlBk.hndRlBk(pRvs);
          } catch (Exception ex1) {
            this.logStd.error(pRvs, getClass(), "Handler roll back: ", ex1);
          }
        }
      }
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
    if (pActArr.length > 1) { //reading phase:
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.readTi);
        this.rdb.begin();
        for (int i = 1; i < pActArr.length; i++) {
          String actNm = pActArr[i];
          if (actNm.startsWith("ent")) {
            if (ent == null) { // it's may be change ent to owner:
              ent = uvs.getOwnr();
              if (ent == null) {
                throw new ExcCode(ExcCode.WR,
                  "wrong_request_entity_not_filled");
              }
             cls = (Class<T>) ent.getClass();
            }
            String entProcNm = this.hldEntPrcNm.get(cls, actNm);
            if (entProcNm == null) {
              this.logSec.error(null, HndEntRq.class,
                "Trying to work with forbidden ent/action/user: " + ent
                  + "/" + actNm + "/" + pRqDt.getUsrNm());
              throw new ExcCode(ExcCode.FORB,
                "Forbidden!");
            }
            @SuppressWarnings("unchecked")
            IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
                this.fctEntPrc.laz(pRvs, entProcNm);
            if (pIsDbgSh) {
              this.logStd.debug(pRvs, HndEntRq.class,
                "It's used entProcNm/IPrcEnt: " + entProcNm
                  + "/" + ep.getClass());
            }
            ent = ep.process(pRvs, ent, pRqDt);
          } else { // else actions like "list" (page)
            String procNm = this.hldPrcFenNm.get(cls, actNm);
            if (procNm == null) {
              this.logSec.error(pRvs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + ent
                + "/" + actNm + "/" + pRqDt.getUsrNm());
              throw new ExcCode(ExcCode.FORB,
                "Forbidden!");
            }
            IPrc proc = this.fctPrcFen.laz(pRvs, procNm);
            if (pIsDbgSh) {
              this.logStd.debug(pRvs, HndEntRq.class,
                "It's used procNm/IPrc: " + procNm + "/"
                  + proc.getClass());
            }
            proc.process(pRvs, pRqDt);
          }
        }
        this.rdb.commit();
      } catch (Exception ex) {
        @SuppressWarnings("unchecked")
        Set<IHnTrRlBk> hnsTrRl = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
        if (hnsTrRl != null) {
          pRvs.remove(IHnTrRlBk.HNSTRRLBK);
          for (IHnTrRlBk hnTrRlBk : hnsTrRl) {
            try {
              hnTrRlBk.hndRlBk(pRvs);
            } catch (Exception ex1) {
              this.logStd.error(pRvs, getClass(), "Handler roll back: ", ex1);
            }
          }
        }
        if (!this.rdb.getAcmt()) {
          this.rdb.rollBack();
        }
        throw ex;
      } finally {
        this.rdb.release();
      }
    }
  }

  /**
   * <p>Handle request without changing transaction isolation.</p>
   * @param <T> entity type
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @param pCls Entity Class
   * @param pActArr Actions Array
   * @param pIsDbgSh Is Show Debug Messages
   * @param pNmEnt entity name
   * @throws Exception - an exception
   */
  public final <T extends IHasId<?>> void hndNoChngIsl(
    final Map<String, Object> pRvs, final IReqDt pRqDt, final Class<T> pCls,
      final String[] pActArr, final boolean pIsDbgSh,
        final String pNmEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    Class<T> cls = pCls;
    IHasId<?> ent = null;
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.writeReTi);
      this.rdb.begin();
      if (pActArr[0].startsWith("ent")) {
        // actions like "save", "delete"
        IFctRq<T> entFac = this.fctFctEnt.laz(pRvs, cls);
        ent = entFac.create(pRvs);
        this.filEntRq.fill(pRvs, vs, ent, pRqDt);
      }
      for (String actNm : pActArr) {
        if (actNm.startsWith("ent")) {
          if (ent == null) { // it's may be change ent to owner:
           ent = uvs.getOwnr();
           if (ent == null) {
              throw new ExcCode(ExcCode.WR,
                "wrong_request_entity_not_filled");
            }
            cls = (Class<T>) ent.getClass();
          }
          String entProcNm = this.hldEntPrcNm.get(cls, actNm);
          if (entProcNm == null) {
            this.logSec.error(pRvs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + pNmEnt
                + "/" + actNm + "/" + pRqDt.getUsrNm());
            throw new ExcCode(ExcCode.FORB, "Forbidden!");
          }
          @SuppressWarnings("unchecked")
          IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
            this.fctEntPrc.laz(pRvs, entProcNm);
          if (pIsDbgSh) {
            this.logStd.debug(pRvs, HndEntRq.class,
             "It's used entProcNm/IPrcEnt: " + entProcNm + "/" + ep.getClass());
          }
          ent = ep.process(pRvs, ent, pRqDt);
        } else { // else actions like "list" (page)
          String procNm = this.hldPrcFenNm.get(cls, actNm);
          if (procNm == null) {
            this.logSec.error(pRvs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + pNmEnt
                + "/" + actNm + "/" + pRqDt.getUsrNm());
            throw new ExcCode(ExcCode.FORB, "Forbidden!");
          }
          IPrc proc = this.fctPrcFen.laz(pRvs, procNm);
          if (pIsDbgSh) {
            this.logStd.debug(pRvs, HndEntRq.class,
            "It's used procNm/IPrc: " + procNm + "/" + proc.getClass());
          }
          proc.process(pRvs, pRqDt);
        }
      }
      this.rdb.commit();
    } catch (Exception ex) {
      @SuppressWarnings("unchecked")
      Set<IHnTrRlBk> hnsTrRlBk = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
      if (hnsTrRlBk != null) {
        pRvs.remove(IHnTrRlBk.HNSTRRLBK);
        for (IHnTrRlBk hnTrRlBk : hnsTrRlBk) {
          try {
            hnTrRlBk.hndRlBk(pRvs);
          } catch (Exception ex1) {
            this.logStd.error(pRvs, getClass(), "Handler roll back: ", ex1);
          }
        }
      }
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for logStd.</p>
   * @return ILog
   **/
  public final ILog getLogStd() {
    return this.logStd;
  }

  /**
   * <p>Setter for logStd.</p>
   * @param pLogStd reference
   **/
  public final void setLogStd(final ILog pLogStd) {
    this.logStd = pLogStd;
  }

  /**
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for filEntRq.</p>
   * @return IFilEntRq
   **/
  public final IFilEntRq getFilEntRq() {
    return this.filEntRq;
  }

  /**
   * <p>Setter for filEntRq.</p>
   * @param pFilEntRq reference
   **/
  public final void setFilEntRq(final IFilEntRq pFilEntRq) {
    this.filEntRq = pFilEntRq;
  }

  /**
   * <p>Getter for fctFctEnt.</p>
   * @return IFcClFcRq
   **/
  public final IFcClFcRq getFctFctEnt() {
    return this.fctFctEnt;
  }

  /**
   * <p>Setter for fctFctEnt.</p>
   * @param pFctFctEnt reference
   **/
  public final void setFctFctEnt(final IFcClFcRq pFctFctEnt) {
    this.fctFctEnt = pFctFctEnt;
  }

  /**
   * <p>Getter for fctEntPrc.</p>
   * @return IFctPrcEnt
   **/
  public final IFctPrcEnt getFctEntPrc() {
    return this.fctEntPrc;
  }

  /**
   * <p>Setter for fctEntPrc.</p>
   * @param pFctEntPrc reference
   **/
  public final void setFctEntPrc(final IFctPrcEnt pFctEntPrc) {
    this.fctEntPrc = pFctEntPrc;
  }

  /**
   * <p>Getter for hldEntPrcNm.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldEntPrcNm() {
    return this.hldEntPrcNm;
  }

  /**
   * <p>Setter for hldEntPrcNm.</p>
   * @param pHldEntPrcNm reference
   **/
  public final void setHldEntPrcNm(
    final IHlNmClSt pHldEntPrcNm) {
    this.hldEntPrcNm = pHldEntPrcNm;
  }

  /**
   * <p>Getter for fctPrcFen.</p>
   * @return IFctPrc
   **/
  public final IFctPrc getFctPrcFen() {
    return this.fctPrcFen;
  }

  /**
   * <p>Setter for fctPrcFen.</p>
   * @param pFctPrcFen reference
   **/
  public final void setFctPrcFen(final IFctPrc pFctPrcFen) {
    this.fctPrcFen = pFctPrcFen;
  }

  /**
   * <p>Getter for hldPrcFenNm.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldPrcFenNm() {
    return this.hldPrcFenNm;
  }

  /**
   * <p>Setter for hldPrcFenNm.</p>
   * @param pHdPrcFenNm reference
   **/
  public final void setHldPrcFenNm(final IHlNmClSt pHdPrcFenNm) {
    this.hldPrcFenNm = pHdPrcFenNm;
  }

  /**
   * <p>Getter for entMap.</p>
   * @return Map<String, Class<? extends IHasId<?>>>
   **/
  public final Map<String, Class<? extends IHasId<?>>> getEntMap() {
    return this.entMap;
  }

  /**
   * <p>Setter for entMap.</p>
   * @param pEntMap reference
   **/
  public final void setEntMap(
    final Map<String, Class<? extends IHasId<?>>> pEntMap) {
    this.entMap = pEntMap;
  }

  /**
   * <p>Getter for writeTi.</p>
   * @return Integer
   **/
  public final Integer getWriteTi() {
    return this.writeTi;
  }

  /**
   * <p>Setter for writeTi.</p>
   * @param pWriteTi reference
   **/
  public final void setWriteTi(final Integer pWriteTi) {
    this.writeTi = pWriteTi;
  }

  /**
   * <p>Getter for logSec.</p>
   * @return ILog
   **/
  public final ILog getLogSec() {
    return this.logSec;
  }

  /**
   * <p>Setter for logSec.</p>
   * @param pLogSec reference
   **/
  public final void setLogSec(final ILog pLogSec) {
    this.logSec = pLogSec;
  }

  /**
   * <p>Getter for readTi.</p>
   * @return Integer
   **/
  public final Integer getReadTi() {
    return this.readTi;
  }

  /**
   * <p>Setter for readTi.</p>
   * @param pReadTi reference
   **/
  public final void setReadTi(final Integer pReadTi) {
    this.readTi = pReadTi;
  }

  /**
   * <p>Getter for writeReTi.</p>
   * @return Integer
   **/
  public final Integer getWriteReTi() {
    return this.writeReTi;
  }

  /**
   * <p>Setter for writeReTi.</p>
   * @param pWriteReTi reference
   **/
  public final void setWriteReTi(final Integer pWriteReTi) {
    this.writeReTi = pWriteReTi;
  }

  /**
   * <p>Getter for wrReSpTr.</p>
   * @return Boolean
   **/
  public final Boolean getWrReSpTr() {
    return this.wrReSpTr;
  }

  /**
   * <p>Setter for wrReSpTr.</p>
   * @param pWrReSpTr reference
   **/
  public final void setWrReSpTr(final Boolean pWrReSpTr) {
    this.wrReSpTr = pWrReSpTr;
  }
}
