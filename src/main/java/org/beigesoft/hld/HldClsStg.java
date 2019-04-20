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
import java.util.HashSet;

import org.beigesoft.prp.ISetng;
/**
 * <p>Holds a string setting for given class. Setting is either standard
 * hard-coded or from ISetng.</p>
 *
 * @author Yury Demidenko
 */
public class HldClsStg {

  /**
   * <p>Setting name.</p>
   **/
  private final String stgNm;

  /**
   * <p>Setting for standard classes.</p>
   **/
  private final String stdVal;

  /**
   * <p>Holder of custom field's converters. It's a settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Classes with custom setting from ISetng.</p>
   **/
  private final Set<Class<?>> custClss = new HashSet<Class<?>>();

  /**
   * <p>Only constructor.</p>
   * @param pStgNm setting name
   * @param pStdVal setting standard value
   **/
  public HldClsStg(final String pStgNm, final String pStdVal) {
    this.stgNm = pStgNm;
    this.stdVal = pStdVal;
  }

  /**
   * <p>Get setting for given class.</p>
   * @param pCls a Class
   * @return setting
   **/
  public final String get(final Class<?> pCls) {
    if (this.custClss.contains(pCls)) {
      String rez = null;
      try {
        rez = this.setng.lazClsStg(pCls, this.stgNm);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      if (rez == null) {
        throw new RuntimeException(
          "Custom setting not found cls/stgNm: "
            + pCls.getSimpleName() + "/" + this.stgNm);
      }
      return rez;
    }
    return this.stdVal;
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
   * <p>Getter for custClss.</p>
   * @return Set<Class<?>>
   **/
  public final Set<Class<?>> getCustClss() {
    return this.custClss;
  }
}
