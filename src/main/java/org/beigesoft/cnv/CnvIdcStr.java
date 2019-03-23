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
import java.util.Set;
import java.lang.reflect.Method;

import org.beigesoft.fct.IFctCls;
import org.beigesoft.hld.IHld;
import org.beigesoft.hld.IHldNm;

/**
 * <p>Converter of an entity composite ID to string representation.
 * String value is comma separated field name=value set,
 * e.g. ID user-role tomcat UsRlTmcId:
 * <pre>
 *  usr=admin,rol=admin
 * </pre>
 * Complex ID can't has entity with complex ID, in example above "usr" is
 * UsTmc, "rol" is RlTmc.</p>
 *
 * @param <T> Composite ID type
 * @author Yury Demidenko
 */
public class CnvIdcStr<T> implements IConv<T, String> {

  /**
   * <p>Holder of composite ID's fields names.</p>
   **/
  private IHld<Class<?>, Set<String>> hldFlNms;

  /**
   * <p>Converters fields factory.</p>
   */
  private IFctCls<IConv<?, String>> fctCnvFld;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldGets;

  /**
   * <p>Convert to string composite ID.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pCmId composite ID
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String conv(final Map<String, Object> pRqVs,
    final T pCmId) throws Exception {
    StringBuffer sb = new StringBuffer("");
    boolean isFirst = true;
    for (String fldNm : this.hldFlNms.get(pCmId.getClass())) {
      @SuppressWarnings("unchecked")
      IConv<Object, String> flCn = (IConv<Object, String>) this.fctCnvFld
        .laz(pRqVs, pCmId.getClass());
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      Method getter = this.hldGets.get(pCmId.getClass(), fldNm);
      Object fldVal = getter.invoke(pCmId);
      sb.append(fldNm + "=" + flCn.conv(pRqVs, fldVal));
    }
    return sb.toString();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for hldFlNms.</p>
   * @return IHld<Class<?>, Set<String>>
   **/
  public final IHld<Class<?>, Set<String>> getHldFlNms() {
    return this.hldFlNms;
  }

  /**
   * <p>Setter for hldFlNms.</p>
   * @param pHldFlNms reference
   **/
  public final void setHldFlNms(final IHld<Class<?>, Set<String>> pHldFlNms) {
    this.hldFlNms = pHldFlNms;
  }

  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctCls<IConv<?, String>>
   **/
  public final IFctCls<IConv<?, String>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctCls<IConv<?, String>> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
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
}
