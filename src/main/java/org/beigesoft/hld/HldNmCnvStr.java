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

import org.beigesoft.cnv.CnvSmpStr;
import org.beigesoft.cnv.CnvBlnStr;
import org.beigesoft.cnv.CnvEnmStr;

/**
 * <p>Holder of names of converters of fields values to string.
 * Floats, Ints are represented as toString values without formatting.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmCnvStr implements IHldNm<Class<?>, String> {

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Map of names of standard converters of fields values to string.
   * It's hard coded map Fields standard type - standard converter name.
   * Fields like Date requires manual format.</p>
   **/
  private final Map<Class<?>, String> stdCnvNms;

  /**
   * <p>Holder of custom field's converters. It's a settings service.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCn;

  /**
   * <p>Only constructor.</p>
   **/
  public HldNmCnvStr() {
    this.stdCnvNms = new HashMap<Class<?>, String>();
    this.stdCnvNms.put(Short.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Integer.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Long.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(String.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Float.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Double.class, CnvSmpStr.class.getSimpleName());
    this.stdCnvNms.put(Boolean.class, CnvBlnStr.class.getSimpleName());
    //this.stdCnvNms.put(.class, CnvStr.class.getSimpleName());
  }

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return converter to string name
   **/
  @Override
  public final String get(final Class<?> pCls, final String pFlNm) {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFlNm);
    if (fdCls.isEnum()) {
      return CnvEnmStr.class.getSimpleName();
    }
    String rez = this.stdCnvNms.get(fdCls);
    if (rez == null) {
      if (this.hldNmFdCn == null) {
    throw new RuntimeException("Not set holder CNV FLD TO STR! enCl/flNm/fdCl: "
  + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
      }
      rez = this.hldNmFdCn.get(pCls, pFlNm);
      if (rez == null) {
        throw new RuntimeException(
          "Custom holder has no CNV FLD TO STR enCl/fdNm/fdCl: "
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
}
