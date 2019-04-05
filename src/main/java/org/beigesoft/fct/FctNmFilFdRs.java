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
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.cnv.IConvNm;
import org.beigesoft.cnv.IFilObj;
import org.beigesoft.cnv.IFilFld;
import org.beigesoft.cnv.FilFldEnmRs;
import org.beigesoft.cnv.FilFldHsIdRs;
import org.beigesoft.cnv.FilFldSmpRs;

/**
 * <p>Factory of fields fillers from DB result-set.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctNmFilFdRs<RS> implements IFctNm<IFilFld<IRecSet<RS>>> {

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
   * <p>Fields converters names.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCn;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctNm<IConvNm<IRecSet<RS>, ?>> fctCnvFld;

  /**
   * <p>Filler entity factory.</p>
   */
  private IFilObj<IRecSet<RS>> filEnt;

  //requested data:
  /**
   * <p>Fillers map.</p>
   **/
  private final Map<String, IFilFld<IRecSet<RS>>> fillers
    = new HashMap<String, IFilFld<IRecSet<RS>>>();

  /**
   * <p>Get filler in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pFiNm - filler name
   * @return requested filler
   * @throws Exception - an exception
   */
  public final IFilFld<IRecSet<RS>> laz(final Map<String, Object> pRqVs,
    final String pFiNm) throws Exception {
    IFilFld<IRecSet<RS>> rz = this.fillers.get(pFiNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.fillers.get(pFiNm);
        if (rz == null) {
          if (FilFldEnmRs.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldEnmRs();
          } else if (FilFldHsIdRs.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldHsIdRs();
          } else if (FilFldSmpRs.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldSmpRs();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no FIL FR STR: " + pFiNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldEnmRs.</p>
   * @return FilFldEnmRs
   */
  private FilFldEnmRs crPuFilFldEnmRs() {
    FilFldEnmRs rz = new FilFldEnmRs();
    rz.setLog(getLogStd());
    rz.setHldSets(getHldSets());
    rz.setHldFdCls(getHldFdCls());
    this.fillers.put(FilFldEnmRs.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldEnmRs.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldHsIdRs.</p>
   * @return FilFldHsIdRs
   */
  private FilFldHsIdRs crPuFilFldHsIdRs() {
    FilFldHsIdRs rz = new FilFldHsIdRs();
    rz.setLog(getLogStd());
    rz.setHldSets(getHldSets());
    rz.setHldFdCls(getHldFdCls());
    rz.setFilEnt(getFilEnt());
    this.fillers.put(FilFldHsIdRs.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldHsIdRs.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldSmpRs.</p>
   * @return FilFldSmpRs
   */
  private FilFldSmpRs crPuFilFldSmpRs() {
    FilFldSmpRs rz = new FilFldSmpRs();
    rz.setLog(getLogStd());
    rz.setHldSets(getHldSets());
    rz.setHldNmFdCn(getHldNmFdCn());
    rz.setFctCnvFld(getFctCnvFld());
    this.fillers.put(FilFldSmpRs.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldSmpRs.class.getSimpleName()
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
  public final IFctNm<IConvNm<IRecSet<RS>, ?>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IConvNm<IRecSet<RS>, ?>> pFctCnvFld) {
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
   * <p>Getter for filEnt.</p>
   * @return IFilObj<IRecSet<RS>>
   **/
  public final IFilObj<IRecSet<RS>> getFilEnt() {
    return this.filEnt;
  }

  /**
   * <p>Setter for filEnt.</p>
   * @param pFilEnt reference
   **/
  public final void setFilEnt(final IFilObj<IRecSet<RS>> pFilEnt) {
    this.filEnt = pFilEnt;
  }
}
