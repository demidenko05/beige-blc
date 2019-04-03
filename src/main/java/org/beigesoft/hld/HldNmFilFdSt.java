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
import org.beigesoft.cnv.FilFldEnmStr;
import org.beigesoft.prp.ISetng;
/**
 * <p>Holder of names of fillers of fields values from string.
 * Floats, Ints are represented as toString values without formatting.</p>
 *
 * @author Yury Demidenko
 */
public class HldNmFilFdSt implements IHldNm<Class<?>, String> {

  /**
   * <p>Setting name of filler from string.</p>
   **/

  public static final String FILFDSTRNM = "filFdSt";

  //Setting:
  /**
   * <p>Filler owned entity name.</p>
   **/
  private String filHasIdNm;

  /**
   * <p>Filler simple name.</p>
   **/
  private String filSmpNm;

  //Services:
  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Holder of custom field's fillers. It's a settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Map of names of standard fillers of fields values from string.
   * It's hard coded map Fields standard type - standard filler name.
   * Fields like Entity, Enum, Composite ID requires manual format.</p>
   **/
  private Map<Class<?>, String> stdFilNms;

  /**
   * <p>Get filler name for given class and field name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return filler from string name
   **/
  @Override
  public final String get(final Class<?> pCls, final String pFlNm) {
    if (this.stdFilNms == null) {
      synchronized (this) {
        if (this.stdFilNms == null) {
          init();
        }
      }
    }
    Class<?> fdCls = this.hldFdCls.get(pCls, pFlNm);
    if (fdCls.isEnum()) {
      return FilFldEnmStr.class.getSimpleName();
    }
    if (IHasId.class.isAssignableFrom(fdCls)) {
      return this.filHasIdNm;
    }
    String rez = this.stdFilNms.get(fdCls);
    if (rez == null) {
      if (this.setng == null) {
    throw new RuntimeException("Not set holder FIL FLD FR STR! enCl/flNm/fdCl: "
  + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
      }
      try {
        rez = this.setng.lazFldStg(pCls, pFlNm, FILFDSTRNM);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      if (rez == null) {
        throw new RuntimeException(
          "Custom holder has no FIL FLD FR STR enCl/fdNm/fdCl: "
            + pCls.getSimpleName() + "/" + pFlNm + "/" + fdCls.getSimpleName());
      }
    }
    return rez;
  }

  /**
   * <p>Initializes standard fillers map, unsynchronized.</p>
   **/
  private void init() {
    if (this.filSmpNm == null || this.filHasIdNm == null) {
      throw new RuntimeException("Non-configured fillers names: FILHSID/FILSMP"
        + this.filHasIdNm + "/" + this.filSmpNm);
    }
    Map<Class<?>, String> sfm = new HashMap<Class<?>, String>();
    sfm.put(Integer.class, this.filSmpNm);
    sfm.put(Long.class, this.filSmpNm);
    sfm.put(String.class, this.filSmpNm);
    sfm.put(Float.class, this.filSmpNm);
    sfm.put(Double.class, this.filSmpNm);
    sfm.put(Boolean.class, this.filSmpNm);
    sfm.put(BigDecimal.class, this.filSmpNm);
    sfm.put(Date.class, this.filSmpNm);
    //assign fully initialized:
    this.stdFilNms = sfm;
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

  /**
   * <p>Getter for filHasIdNm.</p>
   * @return String
   **/
  public final String getFilHasIdNm() {
    return this.filHasIdNm;
  }

  /**
   * <p>Setter for filHasIdNm.</p>
   * @param pFilHasIdNm reference
   **/
  public final void setFilHasIdNm(final String pFilHasIdNm) {
    this.filHasIdNm = pFilHasIdNm;
  }

  /**
   * <p>Getter for filSmpNm.</p>
   * @return String
   **/
  public final String getFilSmpNm() {
    return this.filSmpNm;
  }

  /**
   * <p>Setter for filSmpNm.</p>
   * @param pFilSmpNm reference
   **/
  public final void setFilSmpNm(final String pFilSmpNm) {
    this.filSmpNm = pFilSmpNm;
  }
}
