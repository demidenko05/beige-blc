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
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHld;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.cnv.IConv;
import org.beigesoft.cnv.CnvDtStr;
import org.beigesoft.cnv.CnvDtTmStr;
import org.beigesoft.cnv.CnvEnmStr;
import org.beigesoft.cnv.CnvSmpStr;
import org.beigesoft.cnv.CnvBlnStr;
import org.beigesoft.cnv.CnvHsIdStr;
import org.beigesoft.cnv.CnvIdcStr;
import org.beigesoft.cnv.CnvMaxStr;
import org.beigesoft.cnv.CnvPriStr;
import org.beigesoft.cnv.CnvQuanStr;
import org.beigesoft.cnv.CnvCostStr;
import org.beigesoft.srv.INumStr;

/**
 * <p>Factory of fields converters to string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmCnToSt implements IFctNm<IConv<?, String>> {

  //services:
  /**
   * <p>Outside app-beans factory.</p>
   **/
  private ILog logStd;

  //parts:
  /**
   * <p>Number to string service.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Fields converters names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCn;

  /**
   * <p>Holder of class fields names.</p>
   **/
  private IHld<Class<?>, Set<String>> hldFdNms;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldGets;

  /**
   * <p>ID Fields names holder.</p>
   **/
  private IHld<Class<?>, String> hldIdFdNm;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, IConv<?, String>> convrts
    = new HashMap<String, IConv<?, String>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final IConv<?, String> laz(final Map<String, Object> pRqVs,
    final String pCnNm) throws Exception {
    IConv<?, String> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CnvSmpStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvSmpStr();
          } else if (CnvDtStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtStr();
          } else if (CnvDtTmStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtTmStr();
          } else if (CnvEnmStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvEnmStr();
          } else if (CnvIdcStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIdcStr();
          } else if (CnvHsIdStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvHsIdStr();
          } else if (CnvCostStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvCostStr();
          } else if (CnvQuanStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvQuanStr();
          } else if (CnvMaxStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvMaxStr();
          } else if (CnvPriStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvPriStr();
          } else if (CnvBlnStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBlnStr();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no CNV TO STR: " + pCnNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtStr.</p>
   * @return CnvDtStr
   */
  private CnvDtStr crPuCnvDtStr() {
    CnvDtStr rz = new CnvDtStr();
    this.convrts.put(CnvDtStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtTmStr.</p>
   * @return CnvDtTmStr
   */
  private CnvDtTmStr crPuCnvDtTmStr() {
    CnvDtTmStr rz = new CnvDtTmStr();
    this.convrts.put(CnvDtTmStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtTmStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvEnmStr.</p>
   * @return CnvEnmStr
   */
  private CnvEnmStr<?> crPuCnvEnmStr() {
    CnvEnmStr rz = new CnvEnmStr();
    this.convrts.put(CnvEnmStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvEnmStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIdcStr.</p>
   * @return CnvIdcStr
   */
  private CnvIdcStr<?> crPuCnvIdcStr() {
    CnvIdcStr rz = new CnvIdcStr();
    rz.setFctCnvFld(this);
    rz.setHldNmFdCn(getHldNmFdCn());
    rz.setHldFdNms(getHldFdNms());
    rz.setHldGets(getHldGets());
    this.convrts.put(CnvIdcStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIdcStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvHsIdStr.</p>
   * @return CnvHsIdStr
   */
  private CnvHsIdStr<IHasId<?>> crPuCnvHsIdStr() {
    CnvHsIdStr<IHasId<?>> rz = new CnvHsIdStr<IHasId<?>>();
    rz.setFctCnvFld(this);
    rz.setHldNmFdCn(getHldNmFdCn());
    rz.setHldIdFdNm(getHldIdFdNm());
    this.convrts.put(CnvHsIdStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvHsIdStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvCostStr.</p>
   * @return CnvCostStr
   */
  private CnvCostStr crPuCnvCostStr() {
    CnvCostStr rz = new CnvCostStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvCostStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvCostStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvQuanStr.</p>
   * @return CnvQuanStr
   */
  private CnvQuanStr crPuCnvQuanStr() {
    CnvQuanStr rz = new CnvQuanStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvQuanStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvQuanStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvMaxStr.</p>
   * @return CnvMaxStr
   */
  private CnvMaxStr crPuCnvMaxStr() {
    CnvMaxStr rz = new CnvMaxStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvMaxStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvMaxStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvPriStr.</p>
   * @return CnvPriStr
   */
  private CnvPriStr crPuCnvPriStr() {
    CnvPriStr rz = new CnvPriStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvPriStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvPriStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBlnStr.</p>
   * @return CnvBlnStr
   */
  private CnvBlnStr crPuCnvBlnStr() {
    CnvBlnStr rz = new CnvBlnStr();
    this.convrts.put(CnvBlnStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBlnStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvSmpStr.</p>
   * @return CnvSmpStr
   */
  private CnvSmpStr<?> crPuCnvSmpStr() {
    CnvSmpStr rz = new CnvSmpStr();
    this.convrts.put(CnvSmpStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvSmpStr.class.getSimpleName()
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
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
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
