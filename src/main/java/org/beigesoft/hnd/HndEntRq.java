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

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.fct.IFctCls;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.cnv.IFilObj;
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
  private IFilObj<IReqDt> filEntRq;

  /**
   * <p>Entities factories factory.</p>
   **/
  private IFctCls<IFctRq<?>> fctFctEnt;

  /**
   * <p>Entities processors names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldEntPrcNm;

  /**
   * <p>Entities processors factory.</p>
   **/
  private IFctNm<IPrcEnt> fctEntPrc;

  /**
   * <p>Processors for entities names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldPrcFenNm;

  /**
   * <p>Processors for entities factory.</p>
   **/
  private IFctNm<IPrc> fctPrcFen;

  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entMap;

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
   * <p>Holder transformed UVD settings, other holders and vars.</p>
   */
  private HldUvd<IHasId<?>> hldUvd;

  /**
   * <p>Handle request.
   * actions that change database use read_commited TI
   * this usually required only for complex transactions like loading
   * or withdrawal warehouse (receipt or issue).
   * WHandlerAndJsp requires handle NULL request, so if parameter
   * "ent" is null then do nothing.
   * </p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt) throws Exception {
    String entNm = pRqDt.getParam("ent");
    if (entNm == null) { //dsk/mbl.jsp
      return;
    }
    Class<?> cls = this.entMap.get(entNm);
    if (cls == null) {
      this.logSec.error(pRqVs, HndEntRq.class,
    "Trying to work with forbidden ent/host/addr/port/user: " + entNm + "/"
  + pRqDt.getRemHost() + "/" + pRqDt.getRemAddr() + "/"
+ pRqDt.getRemPort() + "/" + pRqDt.getUsrNm());
      throw new ExcCode(ExcCode.FORB);
    }
    pRqDt.setAttr("hldUvd", this.hldUvd);
    boolean isDbgSh = this.logStd.getDbgSh(this.getClass())
      && this.logStd.getDbgFl() < 5001 && this.logStd.getDbgCl() > 4999;
    String[] actArr = pRqDt.getParam("act").split(",");
    for (String actNm : actArr) {
      if (isDbgSh) {
        this.logStd.debug(pRqVs, HndEntRq.class,
          "Action: " + actNm);
      }
    }
    if (this.wrReSpTr && ("entSv".equals(actArr[0])
  || "entOwSv".equals(actArr[0]) || "entDl".equals(actArr[0])
|| "entOwDl".equals(actArr[0]))) {
      Map<String, Object> vs = new HashMap<String, Object>();
      IHasId<?> ent = null;
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.writeTi);
        this.rdb.begin();
        @SuppressWarnings("unchecked")
        IFctRq<IHasId<?>> entFac = (IFctRq<IHasId<?>>)
          this.fctFctEnt.laz(pRqVs, cls);
        ent = entFac.create(pRqVs);
        this.filEntRq.fill(pRqVs, vs, ent, pRqDt);
        String entProcNm = this.hldEntPrcNm.get(cls, actArr[0]);
        if (entProcNm == null) {
          this.logSec.error(null, HndEntRq.class,
            "Trying to work with forbidden ent/action/user: " + ent + "/"
              + actArr[0] + "/" + pRqDt.getUsrNm());
          throw new ExcCode(ExcCode.FORB,
            "Forbidden!");
        }
        @SuppressWarnings("unchecked")
        IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
          this.fctEntPrc.laz(pRqVs, entProcNm);
        if (isDbgSh) {
          this.logStd.debug(pRqVs, HndEntRq.class,
            "CHANGING transaction use entProcNm/IPrcEnt: " + entProcNm
              + "/" + ep.getClass());
        }
        ent = ep.process(pRqVs, ent, pRqDt);
        this.rdb.commit();
      } catch (Exception ex) {
        this.rdb.rollBack();
        throw ex;
      } finally {
        this.rdb.release();
      }
      if (actArr.length > 1) { //reading phase:
        try {
          this.rdb.setAcmt(false);
          this.rdb.setTrIsl(this.readTi);
          this.rdb.begin();
          for (int i = 1; i < actArr.length; i++) {
            String actNm = actArr[i];
            if (actNm.startsWith("ent")) {
              if (ent == null) { // it's may be change ent to owner:
                ent = (IHasId<?>) pRqVs.get("entNx");
                if (ent == null) {
                  throw new ExcCode(ExcCode.WRPR,
                    "wrong_request_entity_not_filled");
                }
               cls = ent.getClass();
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
                  this.fctEntPrc.laz(pRqVs, entProcNm);
              if (isDbgSh) {
                this.logStd.debug(pRqVs, HndEntRq.class,
                  "It's used entProcNm/IPrcEnt: " + entProcNm
                    + "/" + ep.getClass());
              }
              ent = ep.process(pRqVs, ent, pRqDt);
            } else { // else actions like "list" (page)
              String procNm = this.hldPrcFenNm.get(cls, actNm);
              if (procNm == null) {
                this.logSec.error(pRqVs, HndEntRq.class,
                "Trying to work with forbidden ent/action/user: " + ent
                  + "/" + actNm + "/" + pRqDt.getUsrNm());
                throw new ExcCode(ExcCode.FORB,
                  "Forbidden!");
              }
              IPrc proc = this.fctPrcFen.laz(pRqVs, procNm);
              if (isDbgSh) {
                this.logStd.debug(pRqVs, HndEntRq.class,
                  "It's used procNm/IPrc: " + procNm + "/"
                    + proc.getClass());
              }
              proc.process(pRqVs, pRqDt);
            }
          }
          this.rdb.commit();
        } catch (Exception ex) {
          this.rdb.rollBack();
          throw ex;
        } finally {
          this.rdb.release();
        }
      }
    } else {
      hndNoChngIsl(pRqVs, pRqDt, cls, actArr, isDbgSh, entNm);
    }
  }

  /**
   * <p>Handle request without changing transaction isolation.</p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @param pCls Entity Class
   * @param pActArr Actions Array
   * @param pIsDbgSh Is Show Debug Messages
   * @param pNmEnt entity name
   * @throws Exception - an exception
   */
  public final void hndNoChngIsl(final Map<String, Object> pRqVs,
    final IReqDt pRqDt, final Class<?> pCls,
      final String[] pActArr, final boolean pIsDbgSh,
        final String pNmEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    Class<?> cls = pCls;
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.writeReTi);
      this.rdb.begin();
      IHasId<?> ent = null;
      if (pActArr[0].startsWith("ent")) {
        // actions like "save", "delete"
        @SuppressWarnings("unchecked")
        IFctRq<IHasId<?>> entFac = (IFctRq<IHasId<?>>)
          this.fctFctEnt.laz(pRqVs, cls);
        ent = entFac.create(pRqVs);
        this.filEntRq.fill(pRqVs, vs, ent, pRqDt);
      }
      for (String actNm : pActArr) {
        if (actNm.startsWith("ent")) {
          if (ent == null) { // it's may be change ent to owner:
           ent = (IHasId<?>) pRqVs.get("entNx");
           if (ent == null) {
              throw new ExcCode(ExcCode.WRPR,
                "wrong_request_entity_not_filled");
            }
           cls = ent.getClass();
          }
          String entProcNm = this.hldEntPrcNm.get(cls, actNm);
          if (entProcNm == null) {
            this.logSec.error(pRqVs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + pNmEnt
                + "/" + actNm + "/" + pRqDt.getUsrNm());
            throw new ExcCode(ExcCode.FORB, "Forbidden!");
          }
          @SuppressWarnings("unchecked")
          IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
            this.fctEntPrc.laz(pRqVs, entProcNm);
          if (pIsDbgSh) {
            this.logStd.debug(pRqVs, HndEntRq.class,
              "It's used entProcNm/IPrcEnt: " + entProcNm + "/"
                + ep.getClass());
          }
          ent = ep.process(pRqVs, ent, pRqDt);
        } else { // else actions like "list" (page)
          String procNm = this.hldPrcFenNm.get(cls, actNm);
          if (procNm == null) {
            this.logSec.error(pRqVs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + pNmEnt
                + "/" + actNm + "/" + pRqDt.getUsrNm());
            throw new ExcCode(ExcCode.FORB,
              "Forbidden!");
          }
          IPrc proc = this.fctPrcFen.laz(pRqVs, procNm);
          if (pIsDbgSh) {
            this.logStd.debug(pRqVs, HndEntRq.class,
            "It's used procNm/IPrc: " + procNm + "/" + proc.getClass());
          }
          proc.process(pRqVs, pRqDt);
        }
      }
      this.rdb.commit();
    } catch (Exception ex) {
      this.rdb.rollBack();
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
   * @return IFilObj<IReqDt>
   **/
  public final IFilObj<IReqDt> getFilEntRq() {
    return this.filEntRq;
  }

  /**
   * <p>Setter for filEntRq.</p>
   * @param pFilEntRq reference
   **/
  public final void setFilEntRq(final IFilObj<IReqDt> pFilEntRq) {
    this.filEntRq = pFilEntRq;
  }

  /**
   * <p>Getter for fctFctEnt.</p>
   * @return IFctCls<IFctRq<?>>
   **/
  public final IFctCls<IFctRq<?>> getFctFctEnt() {
    return this.fctFctEnt;
  }

  /**
   * <p>Setter for fctFctEnt.</p>
   * @param pFctFctEnt reference
   **/
  public final void setFctFctEnt(final IFctCls<IFctRq<?>> pFctFctEnt) {
    this.fctFctEnt = pFctFctEnt;
  }

  /**
   * <p>Getter for fctEntPrc.</p>
   * @return IFctNm<IPrcEnt>
   **/
  public final IFctNm<IPrcEnt> getFctEntPrc() {
    return this.fctEntPrc;
  }

  /**
   * <p>Setter for fctEntPrc.</p>
   * @param pFctEntPrc reference
   **/
  public final void setFctEntPrc(final IFctNm<IPrcEnt> pFctEntPrc) {
    this.fctEntPrc = pFctEntPrc;
  }

  /**
   * <p>Getter for hldEntPrcNm.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldEntPrcNm() {
    return this.hldEntPrcNm;
  }

  /**
   * <p>Setter for hldEntPrcNm.</p>
   * @param pHldEntPrcNm reference
   **/
  public final void setHldEntPrcNm(
    final IHldNm<Class<?>, String> pHldEntPrcNm) {
    this.hldEntPrcNm = pHldEntPrcNm;
  }

  /**
   * <p>Getter for fctPrcFen.</p>
   * @return IFctNm<IPrc>
   **/
  public final IFctNm<IPrc> getFctPrcFen() {
    return this.fctPrcFen;
  }

  /**
   * <p>Setter for fctPrcFen.</p>
   * @param pFctPrcFen reference
   **/
  public final void setFctPrcFen(final IFctNm<IPrc> pFctPrcFen) {
    this.fctPrcFen = pFctPrcFen;
  }

  /**
   * <p>Getter for hldPrcFenNm.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldPrcFenNm() {
    return this.hldPrcFenNm;
  }

  /**
   * <p>Setter for hldPrcFenNm.</p>
   * @param pHdPrcFenNm reference
   **/
  public final void setHldPrcFenNm(final IHldNm<Class<?>, String> pHdPrcFenNm) {
    this.hldPrcFenNm = pHdPrcFenNm;
  }

  /**
   * <p>Getter for entMap.</p>
   * @return Map<String, Class<?>>
   **/
  public final Map<String, Class<?>> getEntMap() {
    return this.entMap;
  }

  /**
   * <p>Setter for entMap.</p>
   * @param pEntMap reference
   **/
  public final void setEntMap(final Map<String, Class<?>> pEntMap) {
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

  /**
   * <p>Getter for hldUvd.</p>
   * @return HldUvd<IHasId<?>>
   **/
  public final HldUvd<IHasId<?>> getHldUvd() {
    return this.hldUvd;
  }

  /**
   * <p>Setter for hldUvd.</p>
   * @param pHldUvd reference
   **/
  public final void setHldUvd(final HldUvd<IHasId<?>> pHldUvd) {
    this.hldUvd = pHldUvd;
  }
}
