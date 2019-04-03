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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.fct.IFctCls;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.cnv.IFilEnt;
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
  private IFilEnt<IReqDt> filEntRq;

  /**
   * <p>Entities factories factory.</p>
   **/
  private IFctCls<IFctRq<?>> fctFctEnt;

  /**
   * <p>Entities processors factory.</p>
   **/
  private IFctNm<IPrcEnt> fctPrcEnt;

  /**
   * <p>Processors names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldPrcNm;

  /**
   * <p>Entities processors names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldPrcEntNm;

  /**
   * <p>Processors factory.</p>
   **/
  private IFctNm<IPrc> fctPrc;

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
   * <p>Handle request.
   * actions that change database use read_commited TI
   * this usually required only for complex transactions like loading
   * or withdrawal warehouse (receipt or issue).
   * WHandlerAndJsp requires handle NULL request, so if parameter
   * "nmEnt" is null then do nothing.
   * </p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt) throws Exception {
    String nmEnt = pRqDt.getParam("nmEnt");
    Class<?> cls = this.entMap.get(nmEnt);
    if (cls == null) {
      this.logSec.error(pRqVs, HndEntRq.class,
    "Trying to work with forbidden ent/host/addr/port/user: " + nmEnt + "/"
  + pRqDt.getRemHost() + "/" + pRqDt.getRemAddr() + "/"
+ pRqDt.getRemPort() + "/" + pRqDt.getUsrNm());
      throw new ExcCode(ExcCode.FORB);
    }
    boolean isDbgSh = this.logStd.getDbgSh(this.getClass())
      && this.logStd.getDbgFl() < 5001 && this.logStd.getDbgCl() > 4999;
    String[] actArr = pRqDt.getParam("nmsAct").split(",");
    for (String actNm : actArr) {
      if (isDbgSh) {
        this.logStd.debug(pRqVs, HndEntRq.class,
          "Action: " + actNm);
      }
    }
    if (this.wrReSpTr && ("entityFolSave".equals(actArr[0])
  || "entitySave".equals(actArr[0]) || "entityDelete".equals(actArr[0])
|| "entityFolDelete".equals(actArr[0]))) {
      IHasId<?> ent = null;
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.writeTi);
        this.rdb.begin();
        @SuppressWarnings("unchecked")
        IFctRq<IHasId<?>> entFac = (IFctRq<IHasId<?>>)
          this.fctFctEnt.laz(pRqVs, cls);
        ent = entFac.create(pRqVs);
        this.filEntRq.fill(pRqVs, ent, pRqDt);
        String entProcNm = this.hldPrcEntNm
          .get(cls, actArr[0]);
        if (entProcNm == null) {
          this.logSec.error(null, HndEntRq.class,
            "Trying to work with forbidden ent/action/user: " + nmEnt + "/"
              + actArr[0] + "/" + pRqDt.getUsrNm());
          throw new ExcCode(ExcCode.FORB,
            "Forbidden!");
        }
        @SuppressWarnings("unchecked")
        IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
          this.fctPrcEnt.laz(pRqVs, entProcNm);
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
                ent = (IHasId<?>) pRqVs.get("nextEntity");
                if (ent == null) {
                  throw new ExcCode(ExcCode.WRPR,
                    "wrong_request_entity_not_filled");
                }
               cls = ent.getClass();
              }
              String entProcNm = this.hldPrcEntNm.get(cls, actNm);
              if (entProcNm == null) {
                this.logSec.error(null, HndEntRq.class,
                  "Trying to work with forbidden ent/action/user: " + nmEnt
                    + "/" + actNm + "/" + pRqDt.getUsrNm());
                throw new ExcCode(ExcCode.FORB,
                  "Forbidden!");
              }
              @SuppressWarnings("unchecked")
              IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
                  this.fctPrcEnt.laz(pRqVs, entProcNm);
              if (isDbgSh) {
                this.logStd.debug(pRqVs, HndEntRq.class,
                  "It's used entProcNm/IPrcEnt: " + entProcNm
                    + "/" + ep.getClass());
              }
              ent = ep.process(pRqVs, ent, pRqDt);
            } else { // else actions like "list" (page)
              String procNm = this.hldPrcNm.get(cls, actNm);
              if (procNm == null) {
                this.logSec.error(pRqVs, HndEntRq.class,
                "Trying to work with forbidden ent/action/user: " + nmEnt
                  + "/" + actNm + "/" + pRqDt.getUsrNm());
                throw new ExcCode(ExcCode.FORB,
                  "Forbidden!");
              }
              IPrc proc = this.fctPrc.laz(pRqVs, procNm);
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
      hndNoChngIsl(pRqVs, pRqDt, cls, actArr, isDbgSh, nmEnt);
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
        this.filEntRq.fill(pRqVs, ent, pRqDt);
      }
      for (String actNm : pActArr) {
        if (actNm.startsWith("ent")) {
          if (ent == null) { // it's may be change ent to owner:
           ent = (IHasId<?>) pRqVs.get("nextEntity");
           if (ent == null) {
              throw new ExcCode(ExcCode.WRPR,
                "wrong_request_entity_not_filled");
            }
           cls = ent.getClass();
          }
          String entProcNm = this.hldPrcEntNm.get(cls, actNm);
          if (entProcNm == null) {
            this.logSec.error(pRqVs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + pNmEnt
                + "/" + actNm + "/" + pRqDt.getUsrNm());
            throw new ExcCode(ExcCode.FORB, "Forbidden!");
          }
          @SuppressWarnings("unchecked")
          IPrcEnt<IHasId<?>, ?> ep = (IPrcEnt<IHasId<?>, ?>)
            this.fctPrcEnt.laz(pRqVs, entProcNm);
          if (pIsDbgSh) {
            this.logStd.debug(pRqVs, HndEntRq.class,
              "It's used entProcNm/IPrcEnt: " + entProcNm + "/"
                + ep.getClass());
          }
          ent = ep.process(pRqVs, ent, pRqDt);
        } else { // else actions like "list" (page)
          String procNm = this.hldPrcNm
            .get(cls, actNm);
          if (procNm == null) {
            this.logSec.error(pRqVs, HndEntRq.class,
              "Trying to work with forbidden ent/action/user: " + pNmEnt
                + "/" + actNm + "/" + pRqDt.getUsrNm());
            throw new ExcCode(ExcCode.FORB,
              "Forbidden!");
          }
          IPrc proc = this.fctPrc.laz(pRqVs, procNm);
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
   * @return IFilEnt<IReqDt>
   **/
  public final IFilEnt<IReqDt> getFilEntRq() {
    return this.filEntRq;
  }

  /**
   * <p>Setter for filEntRq.</p>
   * @param pFilEntRq reference
   **/
  public final void setFilEntRq(final IFilEnt<IReqDt> pFilEntRq) {
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
   * <p>Getter for fctPrcEnt.</p>
   * @return IFctNm<IPrcEnt>
   **/
  public final IFctNm<IPrcEnt> getFctPrcEnt() {
    return this.fctPrcEnt;
  }

  /**
   * <p>Setter for fctPrcEnt.</p>
   * @param pFctPrcEnt reference
   **/
  public final void setFctPrcEnt(final IFctNm<IPrcEnt> pFctPrcEnt) {
    this.fctPrcEnt = pFctPrcEnt;
  }

  /**
   * <p>Getter for hldPrcEntNm.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldPrcEntNm() {
    return this.hldPrcEntNm;
  }

  /**
   * <p>Setter for hldPrcEntNm.</p>
   * @param pHldPrcEntNm reference
   **/
  public final void setHldPrcEntNm(
    final IHldNm<Class<?>, String> pHldPrcEntNm) {
    this.hldPrcEntNm = pHldPrcEntNm;
  }

  /**
   * <p>Getter for fctPrc.</p>
   * @return IFctNm<IPrc>
   **/
  public final IFctNm<IPrc> getFctPrc() {
    return this.fctPrc;
  }

  /**
   * <p>Setter for fctPrc.</p>
   * @param pFctPrc reference
   **/
  public final void setFctPrc(final IFctNm<IPrc> pFctPrc) {
    this.fctPrc = pFctPrc;
  }

  /**
   * <p>Getter for hldPrcNm.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldPrcNm() {
    return this.hldPrcNm;
  }

  /**
   * <p>Setter for hldPrcNm.</p>
   * @param pHldPrcNm reference
   **/
  public final void setHldPrcNm(final IHldNm<Class<?>, String> pHldPrcNm) {
    this.hldPrcNm = pHldPrcNm;
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
}
