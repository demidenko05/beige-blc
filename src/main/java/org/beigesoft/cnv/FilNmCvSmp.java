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

package org.beigesoft.cnv;

import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClSt;

/**
 * <p>Fills given column values with given entity's field of simple type
 * that handled by simple type converter.</p>
 *
 * @author Yury Demidenko
 */
public class FilNmCvSmp implements IFilCvFld {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Holder of converters fields names.</p>
  **/
  private IHlNmClSt hldCnvFdNms;

  /**
   * <p>Converters fields factory.</p>
   */
  private IFctNm<IFilCvFdv<?>> fctCnvFld;

  /**
   * <p>Fills given column values with given entity's field of simple type
   * that handled by simple type converter.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields {id, ver, nme}.
   * @param pEnt source entity
   * @param pClVl column values
   * @param pFdNm field name
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> void fill(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pEnt, final String pFdNm,
      final ColVals pClVl) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7022 && this.log.getDbgCl() > 7020;
    String cnvFdNm = this.hldCnvFdNms.get(pEnt.getClass(), pFdNm);
    @SuppressWarnings("unchecked")
    IFilCvFdv<Object> flCvFdv = (IFilCvFdv<Object>)
      this.fctCnvFld.laz(pRvs, cnvFdNm);
    Method getter = this.hldGets.get(pEnt.getClass(), pFdNm);
    Object val = getter.invoke(pEnt);
    if (isDbgSh) {
      this.log.debug(pRvs, FilNmCvSmp.class,
    "Converts fdNm/cls/val/converter: " + pFdNm + "/" + pEnt.getClass()
  .getSimpleName() + "/" + val + "/" + flCvFdv.getClass().getSimpleName());
    }
    flCvFdv.fill(pRvs, pVs, pFdNm, val, pClVl);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
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
   * <p>Getter for hldGets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHlNmClMt pHldGets) {
    this.hldGets = pHldGets;
  }

  /**
   * <p>Getter for hldCnvFdNms.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldCnvFdNms() {
    return this.hldCnvFdNms;
  }

  /**
   * <p>Setter for hldCnvFdNms.</p>
   * @param pHdCnFdNms reference
   **/
  public final void setHldCnvFdNms(final IHlNmClSt pHdCnFdNms) {
    this.hldCnvFdNms = pHdCnFdNms;
  }

  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctNm<IFilCvFdv<?>>
   **/
  public final IFctNm<IFilCvFdv<?>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IFilCvFdv<?>> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }
}
