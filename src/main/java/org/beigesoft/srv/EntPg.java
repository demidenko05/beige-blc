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

package org.beigesoft.srv;

import java.util.Map;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.dlg.IEvalFr;
import org.beigesoft.log.ILog;

/**
 * <p>Service that retrieve entities page or filter data according request.
 * This is assembly dedicated to target set of entities and may include
 * dedicated filter,  e.g. S.E.Seller filter product.sel="1" for
 * products list.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class EntPg<RS> implements IEntPg {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>State-less delegate.</p>
   **/
  private HlpEntPg<RS> hlpEntPg;

  //state:
  /**
   * <p>Entities map "EntitySimpleName"-"Class".</p>
   **/
  private Map<String, Class<? extends IHasId<?>>> entMp;

  //service with business-logic:
  /**
   * <p>Delegate that makes first filter (from start of WHERE).
   * E.g. hard-coded S.E.Seller from request's user principal.
   * It must return either filter string or NULL.
   * It may throw exception, e.g. "Forbidden" if user has no permissions.</p>
   **/
  private IEvalFr<IReqDt, String> mkrFlt;

  /**
   * <p>Retrieve entities page - entities list, pages, filter map etc.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void retPg(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 5000);
    this.hlpEntPg.retPg(pRvs, pRqDt, this.entMp, dbgSh, this.mkrFlt);
  }

  /**
   * <p>Retrieve page filter data like SQL where and filter map.
   * It's used in other services e.g. PrcAssignItemsToCatalog.</p>
   * @param pRvs request scoped vars
   * @param pRqDt - Request Data
   * @param pCls Entity Class
   * @return StringBuffer with empty string "" or one like "ITSID=12"
   * @throws Exception - an Exception
   **/
  @Override
  public final StringBuffer revPgFltDt(final Map<String, Object> pRvs,
    final IReqDt pRqDt,
      final Class<? extends IHasId<?>> pCls) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 5001);
    return this.hlpEntPg.revPgFltDt(pRvs, pRqDt, pCls, dbgSh);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for hlpEntPg.</p>
   * @return HlpEntPg<RS>
   **/
  public final HlpEntPg<RS> getHlpEntPg() {
    return this.hlpEntPg;
  }

  /**
   * <p>Setter for hlpEntPg.</p>
   * @param pHlpEntPg reference
   **/
  public final void setHlpEntPg(final HlpEntPg<RS> pHlpEntPg) {
    this.hlpEntPg = pHlpEntPg;
  }

  /**
   * <p>Getter for entMp.</p>
   * @return Map<String, Class<? extends IHasId<?>>>
   **/
  public final Map<String, Class<? extends IHasId<?>>> getEntMp() {
    return this.entMp;
  }

  /**
   * <p>Setter for entMp.</p>
   * @param pEntMp reference
   **/
  public final void setEntMp(
    final Map<String, Class<? extends IHasId<?>>> pEntMp) {
    this.entMp = pEntMp;
  }

  /**
   * <p>Getter for mkrFlt.</p>
   * @return IEvalFr<IReqDt, String>
   **/
  public final IEvalFr<IReqDt, String> getMkrFlt() {
    return this.mkrFlt;
  }

  /**
   * <p>Setter for mkrFlt.</p>
   * @param pMkrFlt reference
   **/
  public final void setMkrFlt(
    final IEvalFr<IReqDt, String> pMkrFlt) {
    this.mkrFlt = pMkrFlt;
  }
}
