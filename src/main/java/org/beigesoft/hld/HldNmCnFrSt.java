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

package org.beigesoft.hld;

import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

import org.beigesoft.cnv.CnvStrBln;
import org.beigesoft.cnv.CnvStrBgd;
import org.beigesoft.cnv.CnvStrDbl;
import org.beigesoft.cnv.CnvStrFlt;
import org.beigesoft.cnv.CnvStrInt;
import org.beigesoft.cnv.CnvStrLng;
import org.beigesoft.cnv.CnvStrStr;
import org.beigesoft.prp.ISetng;
/**
 * <p>Holder of names of converters of fields values from string.
 * Floats, Ints are represented as toString values without formatting.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmCnFrSt implements IHldNm<Class<?>, String> {

  /**
   * <p>Setting name of converter from string.</p>
   **/

  public static final String CNVFRSTRNM = "cnFrSt";

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Holder of custom field's converters. It's a settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Map of names of standard converters of fields values from string.
   * It's hard coded map Fields standard type - standard converter name.
   * Fields like Date requires manual format.</p>
   **/
  private final Map<Class<?>, String> stdCnvNms;

  /**
   * <p>Only constructor.</p>
   **/
  public HldNmCnFrSt() {
    this.stdCnvNms = new HashMap<Class<?>, String>();
    this.stdCnvNms.put(Integer.class, CnvStrInt.class.getSimpleName());
    this.stdCnvNms.put(Long.class, CnvStrLng.class.getSimpleName());
    this.stdCnvNms.put(String.class, CnvStrStr.class.getSimpleName());
    this.stdCnvNms.put(Float.class, CnvStrFlt.class.getSimpleName());
    this.stdCnvNms.put(Double.class, CnvStrDbl.class.getSimpleName());
    this.stdCnvNms.put(Boolean.class, CnvStrBln.class.getSimpleName());
    this.stdCnvNms.put(BigDecimal.class, CnvStrBgd.class.getSimpleName());
  }

  /**
   * <p>Get converter name for given class and field name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return converter from string name
   **/
  @Override
  public final String get(final Class<?> pCls, final String pFlNm) {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFlNm);
    String rez = this.stdCnvNms.get(fdCls);
    if (rez == null) {
      if (this.setng == null) {
    throw new RuntimeException("Not set holder CNV FLD FR STR! enCl/flNm/fdCl: "
  + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
      }
      try {
        rez = this.setng.lazFldStg(pCls, pFlNm, CNVFRSTRNM);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      if (rez == null) {
        throw new RuntimeException(
          "Custom holder has no CNV FLD FR STR enCl/fdNm/fdCl: "
            + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
      }
    }
    return rez;
  }

  //Simple getters and setters:
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