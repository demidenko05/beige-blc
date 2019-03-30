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

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHld;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.cnv.IConv;
import org.beigesoft.cnv.IFilFld;
import org.beigesoft.cnv.FilFldEnmStr;
import org.beigesoft.cnv.FilFldIdcStr;
import org.beigesoft.cnv.FilFldHsIdStr;
import org.beigesoft.cnv.FilFldSmpStr;

/**
 * <p>Factory of fields fillers from string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmFilFdSt implements IFctNm<IFilFld<String>> {

  //services:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //parts:
  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Fields converters names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCn;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctNm<IConv<String, ?>> fctCnvFld;

  /**
   * <p>Holder of fillers fields names.</p>
   **/
  private IHldNm<Class<?>, String> hldFilFdNms;

  /**
   * <p>Holder of composite ID's fields names.</p>
   **/
  private IHld<Class<?>, Set<String>> hldFdNms;

  /**
   * <p>ID Fields names holder.</p>
   **/
  private IHld<Class<?>, String> hldIdFdNm;

  //requested data:
  /**
   * <p>Fillers map.</p>
   **/
  private final Map<String, IFilFld<String>> fillers
    = new HashMap<String, IFilFld<String>>();

  /**
   * <p>Get filler in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pFiNm - filler name
   * @return requested filler
   * @throws Exception - an exception
   */
  public final IFilFld<String> laz(final Map<String, Object> pRqVs,
    final String pFiNm) throws Exception {
    IFilFld<String> rz = this.fillers.get(pFiNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.fillers.get(pFiNm);
        if (rz == null) {
          if (FilFldEnmStr.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldEnmStr();
          } else if (FilFldIdcStr.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldIdcStr();
          } else if (FilFldHsIdStr.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldHsIdStr();
          } else if (FilFldSmpStr.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldSmpStr();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no FIL FR STR: " + pFiNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldEnmStr.</p>
   * @return FilFldEnmStr
   */
  private FilFldEnmStr crPuFilFldEnmStr() {
    FilFldEnmStr rz = new FilFldEnmStr();
    rz.setHldSets(getHldSets());
    rz.setHldFdCls(getHldFdCls());
    this.fillers.put(FilFldEnmStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldEnmStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldIdcStr.</p>
   * @return FilFldIdcStr
   */
  private FilFldIdcStr crPuFilFldIdcStr() {
    FilFldIdcStr rz = new FilFldIdcStr();
    rz.setHldFdNms(getHldFdNms());
    rz.setHldSets(getHldSets());
    rz.setHldFilFdNms(getHldFilFdNms());
    rz.setHldFdCls(getHldFdCls());
    rz.setFctFilFld(this);
    this.fillers.put(FilFldIdcStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldIdcStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldHsIdStr.</p>
   * @return FilFldHsIdStr
   */
  private FilFldHsIdStr crPuFilFldHsIdStr() {
    FilFldHsIdStr rz = new FilFldHsIdStr();
    rz.setHldSets(getHldSets());
    rz.setHldFilFdNms(getHldFilFdNms());
    rz.setHldIdFdNm(getHldIdFdNm());
    rz.setHldFdCls(getHldFdCls());
    rz.setFctFilFld(this);
    this.fillers.put(FilFldHsIdStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldHsIdStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldSmpStr.</p>
   * @return FilFldSmpStr
   */
  private FilFldSmpStr crPuFilFldSmpStr() {
    FilFldSmpStr rz = new FilFldSmpStr();
    rz.setHldSets(getHldSets());
    rz.setHldNmFdCn(getHldNmFdCn());
    rz.setFctCnvFld(getFctCnvFld());
    this.fillers.put(FilFldSmpStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldSmpStr.class.getSimpleName()
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
   * <p>Getter for hldSets.</p>
   * @return IHldNm<Class<?>, Method>
   **/
  public final IHldNm<Class<?>, Method> getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHldNm<Class<?>, Method> pHldSets) {
    this.hldSets = pHldSets;
  }


  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctCls<IConv<String, Object>
   **/
  public final IFctNm<IConv<String, ?>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IConv<String, ?>> pFctCnvFld) {
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
   * @param pHldNmFdCn reference
   **/
  public final void setHldNmFdCn(final IHldNm<Class<?>, String> pHldNmFdCn) {
    this.hldNmFdCn = pHldNmFdCn;
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
   * @param pHldFilFdNms reference
   **/
  public final void setHldFilFdNms(
    final IHldNm<Class<?>, String> pHldFilFdNms) {
    this.hldFilFdNms = pHldFilFdNms;
  }

  /**
   * <p>Getter for hldFdNms.</p>
   * @return IHld<Class<?>, Set<String>>
   **/
  public final IHld<Class<?>, Set<String>> getHldFdNms() {
    return this.hldFdNms;
  }

  /**
   * <p>Setter for hldFdNms.</p>
   * @param pHldFdNms reference
   **/
  public final void setHldFdNms(final IHld<Class<?>, Set<String>> pHldFdNms) {
    this.hldFdNms = pHldFdNms;
  }

  /**
   * <p>Getter for hldIdFdNm.</p>
   * @return IHld<Class<?>, String>
   **/
  public final IHld<Class<?>, String> getHldIdFdNm() {
    return this.hldIdFdNm;
  }

  /**
   * <p>Setter for hldIdFdNm.</p>
   * @param pHldIdFdNm reference
   **/
  public final void setHldIdFdNm(final IHld<Class<?>, String> pHldIdFdNm) {
    this.hldIdFdNm = pHldIdFdNm;
  }
}
