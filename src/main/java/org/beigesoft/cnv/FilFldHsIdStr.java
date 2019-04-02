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

import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;

/**
 * <p>Standard service that fills/converts object's field of type IHasId from
 * given string value (HTML parameter). It's an owned entity converter.</p>
 *
 * @param <E> owned entity type
 * @param <ID> owned entity ID type
 * @author Yury Demidenko
 */
public class FilFldHsIdStr<E extends IHasId<ID>, ID>
  implements IFilFld<String> {

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Holder of fillers fields names.</p>
   **/
  private IHldNm<Class<?>, String> hldFilFdNms;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFctNm<IFilFld<String>> fctFilFld;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

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
    E val = null;
    if (pStVl != null && !"".equals(pStVl)) {
      @SuppressWarnings("unchecked")
      Class<E> flCls = (Class<E>) this.hldFdCls.get(pObj.getClass(), pFlNm);
      val = flCls.newInstance();
      List<String> fdIdNms = this.setng.lazIdFldNms(flCls);
      if (fdIdNms.size() > 1) {
        throw new ExcCode(ExcCode.NYI, "NYI");
      }
      String filFdNm = this.hldFilFdNms.get(flCls, fdIdNms.get(0));
      IFilFld<String> filFl = this.fctFilFld.laz(pRqVs, filFdNm);
      filFl.fill(pRqVs, val, pStVl, fdIdNms.get(0));
    }
    Method setr = this.hldSets.get(pObj.getClass(), pFlNm);
    setr.invoke(pObj, val);
  }

  //Simple getters and setters:
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
   * <p>Getter for fctFilFld.</p>
   * @return IFctNm<IFilFld<String>>
   **/
  public final IFctNm<IFilFld<String>> getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(final IFctNm<IFilFld<String>> pFctFilFld) {
    this.fctFilFld = pFctFilFld;
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
}
