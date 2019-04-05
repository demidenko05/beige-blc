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
import java.io.OutputStream;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.fct.IFctCls;
import org.beigesoft.cnv.IFilObj;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.IEntFlRp;

/**
 * <p>Handler that makes file-report entity, e.g. PDF.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndEntFlRpRq<RS> implements IHndFlRpRq {

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
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<?>> entMap;

  /**
   * <p>Entities file-reporter factory.</p>
   **/
  private IFctNm<IEntFlRp> fctEntFlRp;

  /**
   * <p>Transaction isolation for handling.</p>
   **/
  private Integer trIsl = IRdb.TRRUC;

  /**
   * <p>Handle request.</p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt,
      final OutputStream pSous) throws Exception {
    try {
      String nmEnt = pRqDt.getParam("nmEnt");
      Class entityClass = this.entMap.get(nmEnt);
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      IHasId<?> entity = null;
      @SuppressWarnings("unchecked")
      IFctRq<IHasId<?>> entFac = (IFctRq<IHasId<?>>)
        this.fctFctEnt.laz(pRqVs, entityClass);
      entity = entFac.create(pRqVs);
      this.filEntRq.fill(pRqVs, null, entity, pRqDt);
      String nmRep = pRqDt.getParam("nmRep");
      @SuppressWarnings("unchecked")
      IEntFlRp<IHasId<?>, ?> efr = (IEntFlRp<IHasId<?>, ?>)
        this.fctEntFlRp.laz(pRqVs, nmRep);
      efr.report(pRqVs, entity, pRqDt, pSous);
      this.rdb.commit();
    } catch (Exception ex) {
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
   * <p>Getter for fctEntFlRp.</p>
   * @return IFctNm<IEntFlRp>
   **/
  public final IFctNm<IEntFlRp> getFctEntFlRp() {
    return this.fctEntFlRp;
  }

  /**
   * <p>Setter for fctEntFlRp.</p>
   * @param pFctEntFlRp reference
   **/
  public final void setFctEntFlRp(final IFctNm<IEntFlRp> pFctEntFlRp) {
    this.fctEntFlRp = pFctEntFlRp;
  }

  /**
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }
}
