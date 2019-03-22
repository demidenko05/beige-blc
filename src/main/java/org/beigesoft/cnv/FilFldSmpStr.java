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

import org.beigesoft.fct.IFctCls;
import org.beigesoft.hld.IHldNm;

/**
 * <p>Standard service that fills/converts object's field of simple type from
 * given string value (HTML parameter). Simple types - Integer, Long,
 * BigDecimal, etc.</p>
 *
 * @author Yury Demidenko
 */
public class FilFldSmpStr implements IFilFld<String> {

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFlCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctCls<IConv<String, Object>> fctSmpCnv;

  /**
   * <p>Fills object's field.</p>
   * @param <T> object type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pObject Object to fill, not null
   * @param pStVl Source field string value
   * @param pFlNm Field name
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pRqVs, final T pObj,
    final String pStVl, final String pFlNm) throws Exception {
    Object val = null;
    if (pStVl != null && !"".equals(pStVl)) {
      Class<?> flCls = hldFlCls.get(pObj.getClass(), pFlNm);
      IConv<String, Object> flCnv = this.fctSmpCnv.laz(pRqVs, flCls);
      val = flCnv.conv(pRqVs, pStVl);
    }
    Method setr = this.hldSets.get(pObj.getClass(), pFlNm);
    setr.invoke(pObj, val);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for hldFlCls.</p>
   * @return IHldNm<Class<?>, Class<?>>
   **/
  public final IHldNm<Class<?>, Class<?>> getHldFlCls() {
    return this.hldFlCls;
  }

  /**
   * <p>Setter for hldFlCls.</p>
   * @param pHldFlCls reference
   **/
  public final void setHldFlCls(final IHldNm<Class<?>, Class<?>> pHldFlCls) {
    this.hldFlCls = pHldFlCls;
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
   * <p>Getter for fctSmpCnv.</p>
   * @return IFctCls<IConv<String, Object>
   **/
  public final IFctCls<IConv<String, Object>> getFctSmpCnv() {
    return this.fctSmpCnv;
  }

  /**
   * <p>Setter for fctSmpCnv.</p>
   * @param pFctSmpCnv reference
   **/
  public final void setFctSmpCnv(
    final IFctCls<IConv<String, Object>> pFctSmpCnv) {
    this.fctSmpCnv = pFctSmpCnv;
  }
}
