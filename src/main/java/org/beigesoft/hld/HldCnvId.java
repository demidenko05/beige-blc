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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IIdLn;
import org.beigesoft.mdl.IIdStr;
import org.beigesoft.cnv.CnvIdCst;
import org.beigesoft.cnv.CnvIdLn;
import org.beigesoft.cnv.CnvIdStr;

/**
 * <p>Holder of names of converters of entity to ID representation.</p>
 *
 * @author Yury Demidenko
 */
public class HldCnvId implements IHlClSt {

  /**
   * <p>Set of classes with custom ID (composite, ID is foreign entity or custom
   * ID name).</p>
   **/
  private Set<Class<? extends IHasId<?>>> custIdClss;

  /**
   * <p>Get converter name for given class and field name.</p>
   * @param pCls a Class
   * @param pFlNm Field Name
   * @return converter from string name
   * @throws Exception an Exception
   **/
  @Override
  public final <T extends IHasId<?>> String get(final Class<T> pCls) throws Exception {
    if (this.custIdClss != null && this.custIdClss.contains(pCls)) {
      return CnvIdCst.class.getSimpleName();
    }
    if (IIdLn.class.isAssignableFrom(pCls)) {
      return CnvIdLn.class.getSimpleName();
    }
    if (IIdStr.class.isAssignableFrom(pCls)) {
      return CnvIdStr.class.getSimpleName();
    }
    throw new Exception("There is no ICnvId for class: "
      + pCls.getSimpleName());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for custIdClss.</p>
   * @return Set<Class<? extends IHasId<?>>>
   **/
  public final Set<Class<? extends IHasId<?>>> getCustIdClss() {
    return this.custIdClss;
  }

  /**
   * <p>Setter for custIdClss.</p>
   * @param pCustIdClss reference
   **/
  public final void setCustIdClss(
    final Set<Class<? extends IHasId<?>>> pCustIdClss) {
    this.custIdClss = pCustIdClss;
  }
}
