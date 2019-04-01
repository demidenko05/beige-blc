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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

import org.beigesoft.cnv.CnvStrBln;
import org.beigesoft.cnv.CnvStrBgdNf;
import org.beigesoft.cnv.CnvStrDbl;
import org.beigesoft.cnv.CnvStrFlt;
import org.beigesoft.cnv.CnvStrInt;
import org.beigesoft.cnv.CnvStrLng;
import org.beigesoft.cnv.CnvStrFrStrXml;
import org.beigesoft.cnv.CnvStrDtMs;

/**
 * <p>Holder of names of converters of fields values from XML string.
 * Floats, Ints, BigDecimal are represented as toString values without
 * formatting. Date is in milliseconds.
 * Composite ID is made from key fields.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmCnFrStXml implements IHldNm<Class<?>, String> {

  /**
   * <p>Setting name of converter from string.</p>
   **/

  public static final String CNVFRSTRNM = "cnFrSt";

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Map of names of standard converters of fields values from string.
   * It's hard coded map Fields standard type - standard converter name.
   * Fields like Date requires manual format.</p>
   **/
  private final Map<Class<?>, String> stdCnvNms;

  /**
   * <p>Only constructor.</p>
   **/
  public HldNmCnFrStXml() {
    this.stdCnvNms = new HashMap<Class<?>, String>();
    this.stdCnvNms.put(Integer.class, CnvStrInt.class.getSimpleName());
    this.stdCnvNms.put(Long.class, CnvStrLng.class.getSimpleName());
    this.stdCnvNms.put(String.class, CnvStrFrStrXml.class.getSimpleName());
    this.stdCnvNms.put(Float.class, CnvStrFlt.class.getSimpleName());
    this.stdCnvNms.put(Double.class, CnvStrDbl.class.getSimpleName());
    this.stdCnvNms.put(Boolean.class, CnvStrBln.class.getSimpleName());
    this.stdCnvNms.put(BigDecimal.class, CnvStrBgdNf.class.getSimpleName());
    this.stdCnvNms.put(Date.class, CnvStrDtMs.class.getSimpleName());
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
      throw new RuntimeException(
        "There is no CNV FLD FR STR XML enCl/fdNm/fdCl: "
          + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
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
}
