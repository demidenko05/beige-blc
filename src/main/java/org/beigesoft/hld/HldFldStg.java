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

import java.util.Set;
import java.util.Map;

import org.beigesoft.prp.ISetng;

/**
 * <p>Generic holder of string field setting.</p>
 *
 * @author Yury Demidenko
 */
public class HldFldStg implements IHldNm<Class<?>, String> {

  /**
   * <p>Setting name.</p>
   **/
  private final String stgNm;

  /**
   * <p>Setting for standard field.</p>
   **/
  private final String stdVal;

  //Services:
  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Holder of custom field's converters. It's a settings service.</p>
   **/
  private ISetng setng;

  //configuration:
  /**
   * <p>Map of fields settings for field's class.</p>
   **/
  private Map<Class<?>, String> stgClss;

  /**
   * <p>Map of fields settings for field's super class.</p>
   **/
  private Map<Class<?>, String> stgSclss;

  /**
   * <p>Field's classes with custom setting from ISetng.</p>
   **/
  private Set<Class<?>> custClss;

  /**
   * <p>Setting for any enum field.</p>
   **/
  private String enumVal;

  /**
   * <p>Only constructor.</p>
   * @param pStgNm setting name
   * @param pStdVal setting standard value
   **/
  public HldFldStg(final String pStgNm, final String pStdVal) {
    this.stgNm = pStgNm;
    this.stdVal = pStdVal;
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
    if (this.setng != null && this.custClss.contains(fdCls)) {
      try {
        return this.setng.lazFldStg(pCls, pFlNm, this.stgNm);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    if (this.stgClss != null && this.stgClss.keySet().contains(fdCls)) {
      return this.stgClss.get(fdCls);
    }
    if (fdCls.isEnum() && this.enumVal != null) {
      return this.enumVal;
    }
    if (this.stgSclss != null) {
      for (Map.Entry<Class<?>, String> enr : this.stgSclss.entrySet()) {
        if (enr.getKey().isAssignableFrom(fdCls)) {
          return enr.getValue();
        }
      }
    }
    return this.stdVal;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for stgNm.</p>
   * @return String
   **/
  public final String getStgNm() {
    return this.stgNm;
  }

  /**
   * <p>Getter for stdVal.</p>
   * @return String
   **/
  public final String getStdVal() {
    return this.stdVal;
  }

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
   * <p>Getter for stgClss.</p>
   * @return Map<Class<?>, String>
   **/
  public final Map<Class<?>, String> getStgClss() {
    return this.stgClss;
  }

  /**
   * <p>Setter for stgClss.</p>
   * @param pStgClss reference
   **/
  public final void setStgClss(final Map<Class<?>, String> pStgClss) {
    this.stgClss = pStgClss;
  }

  /**
   * <p>Getter for stgSclss.</p>
   * @return Map<Class<?>, String>
   **/
  public final Map<Class<?>, String> getStgSclss() {
    return this.stgSclss;
  }

  /**
   * <p>Setter for stgSclss.</p>
   * @param pStgSclss reference
   **/
  public final void setStgSclss(final Map<Class<?>, String> pStgSclss) {
    this.stgSclss = pStgSclss;
  }

  /**
   * <p>Getter for custClss.</p>
   * @return Set<Class<?>>
   **/
  public final Set<Class<?>> getCustClss() {
    return this.custClss;
  }

  /**
   * <p>Setter for custClss.</p>
   * @param pCustClss reference
   **/
  public final void setCustClss(final Set<Class<?>> pCustClss) {
    this.custClss = pCustClss;
  }

  /**
   * <p>Getter for enumVal.</p>
   * @return String
   **/
  public final String getEnumVal() {
    return this.enumVal;
  }

  /**
   * <p>Setter for enumVal.</p>
   * @param pEnumVal reference
   **/
  public final void setEnumVal(final String pEnumVal) {
    this.enumVal = pEnumVal;
  }
}
