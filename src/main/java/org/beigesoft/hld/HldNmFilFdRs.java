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
import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.cnv.FilFldEnmRs;
import org.beigesoft.cnv.FilFldHsIdRs;
import org.beigesoft.cnv.FilFldSmpRs;

/**
 * <p>Holder of names of fillers of fields values from DB record-set.
 * Floats, Ints are represented as toString values without formatting.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmFilFdRs implements IHlNmClSt {

  //Services:
  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Map of names of standard fillers of fields values from string.
   * It's hard coded map Fields standard type - standard filler name.
   * Fields like Entity, Enum, Composite ID requires manual format.</p>
   **/
  private final Map<Class<?>, String> stdFilNms;

  /**
   * <p>Only constructor.</p>
   **/
  public HldNmFilFdRs() {
    this.stdFilNms = new HashMap<Class<?>, String>();
    this.stdFilNms.put(Integer.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(Long.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(String.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(Float.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(Double.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(Boolean.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(BigDecimal.class, FilFldSmpRs.class.getSimpleName());
    this.stdFilNms.put(Date.class, FilFldSmpRs.class.getSimpleName());
  }

  /**
   * <p>Get filler name for given class and field name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return filler from string name
   * @throws Exception an Exception
   **/
  @Override
  public final String get(final Class<?> pCls,
    final String pFlNm) throws Exception {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFlNm);
    if (fdCls.isEnum()) {
      return FilFldEnmRs.class.getSimpleName();
    }
    if (IHasId.class.isAssignableFrom(fdCls)) {
      return FilFldHsIdRs.class.getSimpleName();
    }
    String rez = this.stdFilNms.get(fdCls);
    if (rez == null) {
      throw new Exception("There is no FIL FLD FR RS! enCl/flNm/fdCl: "
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
