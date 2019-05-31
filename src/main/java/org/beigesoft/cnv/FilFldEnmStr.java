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

import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;

/**
 * <p>Standard service that fills/converts object's field of type Enum from
 * given string value (HTML parameter).</p>
 *
 * @param <E> Enum type
 * @author Yury Demidenko
 */
public class FilFldEnmStr<E extends Enum<E>> implements IFilFld<String> {

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHlNmClMt hldSets;

  /**
   * <p>Fills object's field.</p>
   * @param <T> object type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pObj Object to fill, not null
   * @param pStVl Source field string value
   * @param pFlNm Field name
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pObj,
      final String pStVl, final String pFlNm) throws Exception {
    Enum<?> val = null;
    if (pStVl != null && !"".equals(pStVl)) {
      @SuppressWarnings("unchecked")
      Class<E> flCls = (Class<E>) this.hldFdCls.get(pObj.getClass(), pFlNm);
      val = Enum.valueOf(flCls, pStVl);
    }
    Method setr = this.hldSets.get(pObj.getClass(), pFlNm);
    setr.invoke(pObj, val);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for hldFdCls.</p>
   * @return IHlNmClCl
   **/
  public final IHlNmClCl getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHlNmClCl pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  /**
   * <p>Getter for hldSets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHlNmClMt pHldSets) {
    this.hldSets = pHldSets;
  }
}
