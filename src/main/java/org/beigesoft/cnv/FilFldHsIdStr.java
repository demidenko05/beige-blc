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
import org.beigesoft.mdl.IHasId;

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
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFlCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFctCls<IFilFld<String>> fctFilFld;

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
      Class<E> flCls = (Class<E>) hldFlCls.get(pObj.getClass(), pFlNm);
      IFilFld<String> idFlr =  this.fctFilFld.laz(pRqVs, flCls);
      val = flCls.newInstance();
      idFlr.fill(pRqVs, val, pStVl, IHasId.IDNM);
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
   * <p>Getter for fctFilFld.</p>
   * @return IFctCls<IFilFld<String>>
   **/
  public final IFctCls<IFilFld<String>> getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(final IFctCls<IFilFld<String>> pFctFilFld) {
    this.fctFilFld = pFctFilFld;
  }
}
