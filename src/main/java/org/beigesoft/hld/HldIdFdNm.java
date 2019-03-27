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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.UsTmc;

/**
 * <p>Holder of classes ID field names.</p>
 *
 * @author Yury Demidenko
 */
public class HldIdFdNm implements IHld<Class<?>, String> {

  /**
   * <p>Map of names of custom ID names.</p>
   **/
  private final Map<Class<?>, String> cstIdNms;

  /**
   * <p>Only constructor.</p>
   **/
  public HldIdFdNm() {
    this.cstIdNms = new HashMap<Class<?>, String>();
    this.cstIdNms.put(UsTmc.class, "usr");
  }

  /**
   * <p>Get ID field name.</p>
   * @param pCls class
   * @return ID field name
   **/
  @Override
  public final String get(final Class<?> pCls) {
    if (this.cstIdNms.keySet().contains(pCls)) {
      return this.cstIdNms.get(pCls);
    }
    return IHasId.IDNM;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cstIdNms.</p>
   * @return Map<Class<?>, String>
   **/
  public final Map<Class<?>, String> getCstIdNms() {
    return this.cstIdNms;
  }
}
