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
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;

/**
 * <p>Fills given column values with given entity's field of entity type.</p>
 *
 * @author Yury Demidenko
 */
public class FilNmCvHsId implements IFilNm<IHasId<?>, ColVals> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldGets;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Holder of converters fields names.</p>
  **/
  private IHldNm<Class<?>, String> hldCnvFdNms;

  /**
   * <p>Converters fields factory.</p>
   */
  private IFctNm<IConvNmInto<?, ColVals>> fctCnvFld;

  /**
   * <p>Fills given column values with given entity's field of entity type.</p>
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars
   * @param pEnt source entity
   * @param pClVl column values
   * @param pFdNm field name
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final IHasId<?> pEnt, final ColVals pClVl,
    final String pFdNm) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7023 && this.log.getDbgCl() > 7021;
    Method getter = this.hldGets.get(pEnt.getClass(), pFdNm);
    @SuppressWarnings("unchecked")
    IHasId<?> subEnt = (IHasId<?>) getter.invoke(pEnt);
    @SuppressWarnings("unchecked")
    Class<?> ifdCls = (Class<IHasId<?>>)
      this.hldFdCls.get(pEnt.getClass(), pFdNm);
    for (String fdNm : this.setng.lazIdFldNms(ifdCls)) {
      Object val;
      if (subEnt != null) {
        getter = this.hldGets.get(subEnt.getClass(), fdNm);
        val = getter.invoke(subEnt);
      } else {
        val = null;
      }
      String cnvFdNm = this.hldCnvFdNms.get(ifdCls, fdNm);
      IConvNmInto<Object, ColVals> cnvFl = (IConvNmInto<Object, ColVals>)
        this.fctCnvFld.laz(pRqVs, cnvFdNm);
      if (isDbgSh) {
        this.log.debug(pRqVs, FilNmCvSmp.class,
      "Converts fdNm/cls/val/converter: " + pFdNm + "/" + ifdCls
    .getSimpleName() + "/" + val + "/" + cnvFl.getClass().getSimpleName());
      }
      cnvFl.conv(pRqVs, pVs, val, pClVl, pFdNm);
    }
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
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
  }

  /**
   * <p>Getter for hldGets.</p>
   * @return IHldNm<Class<?>, Method>
   **/
  public final IHldNm<Class<?>, Method> getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHldNm<Class<?>, Method> pHldGets) {
    this.hldGets = pHldGets;
  }

  /**
   * <p>Getter for hldFdCls.</p>
   * @return IHldNm<Class<?>, Class<?>>
   **/
  public final IHldNm<Class<?>, Class<?>> getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHldNm<Class<?>, Class<?>> pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  /**
   * <p>Getter for hldCnvFdNms.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldCnvFdNms() {
    return this.hldCnvFdNms;
  }

  /**
   * <p>Setter for hldCnvFdNms.</p>
   * @param pHdCnFdNms reference
   **/
  public final void setHldCnvFdNms(final IHldNm<Class<?>, String> pHdCnFdNms) {
    this.hldCnvFdNms = pHdCnFdNms;
  }

  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctNm<IConvNmInto<?, ColVals>>
   **/
  public final IFctNm<IConvNmInto<?, ColVals>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IConvNmInto<?, ColVals>> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }
}
