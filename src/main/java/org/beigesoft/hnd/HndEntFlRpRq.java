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
import java.io.OutputStream;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.fct.IFcClFcRq;
import org.beigesoft.cnv.IFilEntRq;
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
   * <p>Standard logger.</p>
   **/
  private ILog logStd;

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
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<? extends IHasId<?>>> entMap;

  /**
   * <p>Entities file-reporter factory.</p>
   **/
  private IFcEnFlRp fctEntFlRp;

  /**
   * <p>Transaction isolation for handling.</p>
   **/
  private Integer trIsl = IRdb.TRRUC;

  /**
   * <p>Handle request.</p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final OutputStream pSous) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    try {
      String ent = pRqDt.getParam("ent");
      @SuppressWarnings("unchecked")
      Class<IHasId<?>> cls = (Class<IHasId<?>>) this.entMap.get(ent);
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      IHasId<?> entity = null;
      IFctRq<IHasId<?>> entFac = this.fctFctEnt.laz(pRvs, cls);
      entity = entFac.create(pRvs);
      this.filEntRq.fill(pRvs, vs, entity, pRqDt);
      String nmRep = pRqDt.getParam("nmRep");
      @SuppressWarnings("unchecked")
      IEntFlRp<IHasId<?>, ?> efr = (IEntFlRp<IHasId<?>, ?>)
        this.fctEntFlRp.laz(pRvs, nmRep);
      efr.report(pRvs, entity, pRqDt, pSous);
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
   * <p>Getter for fctEntFlRp.</p>
   * @return IFcEnFlRp
   **/
  public final IFcEnFlRp getFctEntFlRp() {
    return this.fctEntFlRp;
  }

  /**
   * <p>Setter for fctEntFlRp.</p>
   * @param pFctEntFlRp reference
   **/
  public final void setFctEntFlRp(final IFcEnFlRp pFctEntFlRp) {
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
