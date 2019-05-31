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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.cnv.CnvSmpStr;
import org.beigesoft.cnv.CnvBlnStr;
import org.beigesoft.cnv.CnvEnmStr;
import org.beigesoft.cnv.CnvDtStrMs;
import org.beigesoft.cnv.CnvStrToStrXml;

/**
 * <p>Holder of names of converters of fields values to XML string.
 * Floats, Ints, BigDecimal are represented as toString values without
 * formatting. Date is in milliseconds.
 * Composite ID is made from key fields.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmCnToStXml implements IHlNmClSt {

  /**
   * <p>Setting name of converter to string.</p>
   **/

  public static final String CNVTOSTRNM = "cnToSt";

  //Setting:
  /**
   * <p>Converter owned entity name.</p>
   **/
  private String cnHsIdToStNm;

  //Service:
  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  //Data:
  /**
   * <p>Map of names of standard converters of fields values to string.
   * It's hard coded map Fields standard type - standard converter name.
   * Fields like Date requires manual format.</p>
   **/
  private final Map<Class<?>, String> stdCnvNms;

  /**
   * <p>Only constructor.</p>
   **/
  public HldNmCnToStXml() {
    this.stdCnvNms = new HashMap<Class<?>, String>();
    this.stdCnvNms.put(Short.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Integer.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Long.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(String.class, CnvStrToStrXml.class.getSimpleName());
    this.stdCnvNms.put(Date.class, CnvDtStrMs.class.getSimpleName());
    this.stdCnvNms.put(Float.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Double.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Boolean.class, CnvBlnStr.class.getSimpleName());
    this.stdCnvNms.put(BigDecimal.class, CnvSmpStr.class.getSimpleName());
  }

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return converter to string name
   * @throws Exception an Exception
   **/
  @Override
  public final String get(final Class<?> pCls,
    final String pFlNm) throws Exception {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFlNm);
    if (fdCls.isEnum()) {
      return CnvEnmStr.class.getSimpleName();
    }
    if (IHasId.class.isAssignableFrom(fdCls)) {
      if (this.cnHsIdToStNm == null) {
        throw new Exception("Non-configured cnHsIdToStNm!");
      }
      return this.cnHsIdToStNm;
    }
    String rez = this.stdCnvNms.get(fdCls);
    if (rez == null) {
      throw new Exception(
        "There is no CNV FLD TO STR XML enCl/fdNm/fdCl: "
          + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
    }
    return rez;
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
   * <p>Getter for cnHsIdToStNm.</p>
   * @return String
   **/
  public final String getCnHsIdToStNm() {
    return this.cnHsIdToStNm;
  }

  /**
   * <p>Setter for cnHsIdToStNm.</p>
   * @param pCnHsIdToStNm reference
   **/
  public final void setCnHsIdToStNm(final String pCnHsIdToStNm) {
    this.cnHsIdToStNm = pCnHsIdToStNm;
  }
}
