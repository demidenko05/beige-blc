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
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.prp.ISetng;

/**
 * <p>Converter of an owned entity to string.
 * It's entity ID field converted to string. ID either simple - String/Long
 * or complex - composite or a foreign entity.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class CnvHsIdStr<T extends IHasId<?>> implements ICnToSt<T> {

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Converts any entity to string (ID).</p>
   * @param pRvs request scoped vars, e.g. user preference decimal separator
   * @param pHsId Entity that ID
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String conv(final Map<String, Object> pRvs,
    final T pHsId) throws Exception {
    if (pHsId == null) {
      return "";
    }
    Object id = revId(pHsId);
    if (id.getClass().isEnum()) {
      return ((Enum) id).name();
    } else if (id.getClass() == Long.class || id.getClass() == Integer.class
      || id.getClass() == String.class) {
      return id.toString();
    } else {
      throw new ExcCode(ExcCode.WRCN, "Subentity with wrong ID!"
        + " ent/idCls" + pHsId + "/" + id.getClass());
    }
  }

  //Utils:
  /**
   * <p>Reveals last ID value.</p>
   * @param pEnt entity
   * @return ID value Integer/String/Long/Enum
   * @throws Exception - an exception
   **/
  public final Object revId(final IHasId<?> pEnt) throws Exception {
    Object rz;
    List<String> fdIdNms = this.setng.lazIdFldNms(pEnt.getClass());
    if (fdIdNms.size() > 1) {
      throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID - "  + pEnt);
    }
    String idNm = fdIdNms.get(0);
    Method getter = this.hldGets.get(pEnt.getClass(), idNm);
    rz = getter.invoke(pEnt);
    if (IHasId.class.isAssignableFrom(rz.getClass())) {
      @SuppressWarnings("unchecked")
      IHasId<?> sse = (IHasId<?>) rz;
      return revId(sse);
    }
    return rz;
  }

  //Simple getters and setters:
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
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHlNmClMt pHldGets) {
    this.hldGets = pHldGets;
  }
}
