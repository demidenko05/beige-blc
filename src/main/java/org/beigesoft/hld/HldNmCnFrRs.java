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
import org.beigesoft.cnv.CvRsFvBgd;
import org.beigesoft.cnv.CvRsFvBln;
import org.beigesoft.cnv.CvRsFvDbl;
import org.beigesoft.cnv.CvRsFvDt;
import org.beigesoft.cnv.CvRsFvFlt;
import org.beigesoft.cnv.CvRsFvInt;
import org.beigesoft.cnv.CvRsFvLng;
import org.beigesoft.cnv.CvRsFvStr;

/**
 * <p>Holder of names of converters of fields values from DB result-set.
 * Floats, Ints are represented as toString values without formatting.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmCnFrRs implements IHlNmClSt {

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Map of names of standard converters of fields values from string.
   * It's hard coded map Fields standard type - standard converter name.
   * Fields like Date requires manual format.</p>
   **/
  private final Map<Class<?>, String> stdCnvNms;

  /**
   * <p>Only constructor.</p>
   **/
  public HldNmCnFrRs() {
    this.stdCnvNms = new HashMap<Class<?>, String>();
    this.stdCnvNms.put(Date.class, CvRsFvDt.class.getSimpleName());
    this.stdCnvNms.put(Integer.class, CvRsFvInt.class.getSimpleName());
    this.stdCnvNms.put(Long.class, CvRsFvLng.class.getSimpleName());
    this.stdCnvNms.put(String.class, CvRsFvStr.class.getSimpleName());
    this.stdCnvNms.put(Float.class, CvRsFvFlt.class.getSimpleName());
    this.stdCnvNms.put(Double.class, CvRsFvDbl.class.getSimpleName());
    this.stdCnvNms.put(Boolean.class, CvRsFvBln.class.getSimpleName());
    this.stdCnvNms.put(BigDecimal.class, CvRsFvBgd.class.getSimpleName());
  }

  /**
   * <p>Get converter name for given class and field name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return converter from string name
   * @throws Exception an Exception
   **/
  @Override
  public final <T extends IHasId<?>> String get(final Class<T> pCls,
    final String pFlNm) throws Exception {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFlNm);
    String rez = this.stdCnvNms.get(fdCls);
    if (rez == null) {
      throw new Exception("There is no CNV FLD FR STR! enCl/flNm/fdCl: "
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
}
