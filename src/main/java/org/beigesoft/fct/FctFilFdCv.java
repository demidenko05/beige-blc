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

package org.beigesoft.fct;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.cnv.IConvNmInto;
import org.beigesoft.cnv.IFilNm;
import org.beigesoft.cnv.FilNmCvHsId;
import org.beigesoft.cnv.FilNmCvSmp;
import org.beigesoft.prp.ISetng;

/**
 * <p>Factory of column values fillers from fields.</p>
 *
 * @author Yury Demidenko
 */
public class FctFilFdCv implements IFctNm<IFilNm<IHasId<?>, ColVals>> {

  //services:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //parts:
  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldGets;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Fields converters names.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCn;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctNm<IConvNmInto<?, ColVals>> fctCnvFld;

  /**
   * <p>Holder of fillers fields names.</p>
  **/
  private IHldNm<Class<?>, String> hldFilFdNms;

  //requested data:
  /**
   * <p>Fillers map.</p>
   **/
  private final Map<String, IFilNm<IHasId<?>, ColVals>> fillers
    = new HashMap<String, IFilNm<IHasId<?>, ColVals>>();

  /**
   * <p>Get filler in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pFiNm - filler name
   * @return requested filler
   * @throws Exception - an exception
   */
  public final IFilNm<IHasId<?>, ColVals> laz(final Map<String, Object> pRqVs,
    final String pFiNm) throws Exception {
    IFilNm<IHasId<?>, ColVals> rz = this.fillers.get(pFiNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.fillers.get(pFiNm);
        if (rz == null) {
          if (FilNmCvHsId.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilNmCvHsId();
          } else if (FilNmCvSmp.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilNmCvSmp();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no FIL CV: " + pFiNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map FilNmCvSmp.</p>
   * @return FilNmCvSmp
   */
  private FilNmCvSmp crPuFilNmCvSmp() {
    FilNmCvSmp rz = new FilNmCvSmp();
    rz.setLog(getLogStd());
    rz.setHldGets(getHldGets());
    rz.setHldCnvFdNms(getHldNmFdCn());
    rz.setFctCnvFld(getFctCnvFld());
    this.fillers.put(FilNmCvSmp.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilNmCvSmp.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilNmCvHsId.</p>
   * @return FilNmCvHsId
   */
  private FilNmCvHsId crPuFilNmCvHsId() {
    FilNmCvHsId rz = new FilNmCvHsId();
    rz.setLog(getLogStd());
    rz.setHldGets(getHldGets());
    rz.setHldFdCls(getHldFdCls());
    rz.setSetng(getSetng());
    rz.setHldCnvFdNms(getHldNmFdCn());
    rz.setFctCnvFld(getFctCnvFld());
    this.fillers.put(FilNmCvHsId.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilNmCvHsId.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for logStd.</p>
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
   * <p>Getter for fctCnvFld.</p>
   * @return IFctCls<IConv<String, Object>
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

  /**
   * <p>Getter for hldNmFdCn.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldNmFdCn() {
    return this.hldNmFdCn;
  }

  /**
   * <p>Setter for hldNmFdCn.</p>
   * @param pHlNmFdCn reference
   **/
  public final void setHldNmFdCn(final IHldNm<Class<?>, String> pHlNmFdCn) {
    this.hldNmFdCn = pHlNmFdCn;
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
   * <p>Getter for hldFilFdNms.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldFilFdNms() {
    return this.hldFilFdNms;
  }

  /**
   * <p>Setter for hldFilFdNms.</p>
   * @param pHdCnFdNms reference
   **/
  public final void setHldFilFdNms(final IHldNm<Class<?>, String> pHdCnFdNms) {
    this.hldFilFdNms = pHdCnFdNms;
  }
}
