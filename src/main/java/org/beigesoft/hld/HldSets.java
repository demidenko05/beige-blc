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
import java.lang.reflect.Method;

import org.beigesoft.srv.IReflect;

/**
 * <p>Holder of RAPI setters for any requested class.</p>
 *
 * @author Yury Demidenko
 */
public class HldSets implements IHlNmClMt {

  /**
   * <p>Reflection service.</p>
   **/
  private IReflect reflect;

  /**
   * <p>Map of classes and their fields names - RAPI setters.</p>
   **/
  private final Map<Class<?>, Map<String, Method>> setsMap =
    new HashMap<Class<?>, Map<String, Method>>();

  /**
   * <p>Get thing for given class and thing name.</p>
   * @param pCls a Class
   * @param pFlNm Thing Name
   * @return setter or exception if not found
   * @throws Exception an Exception
   **/
  @Override
  public final Method get(final Class<?> pCls,
    final String pFlNm) throws Exception {
    if (pCls == null || pFlNm == null) {
      throw new Exception("NULL parameter cls/fld: " + pCls
        + "/" + pFlNm);
    }
    Map<String, Method> stsMp = this.setsMap.get(pCls);
    if (stsMp == null) {
      // There is no way to get from Map partially initialized bean
      // in this double-checked locking implementation
      // cause putting to the Map fully initialized bean
      synchronized (this.setsMap) {
        stsMp = this.setsMap.get(pCls);
        if (stsMp == null) {
          stsMp = new HashMap<String, Method>();
          Method[] methods = getReflect().retMths(pCls);
          for (Method method : methods) {
            if (method.getName().startsWith("set")) {
              String fldNm = method.getName().substring(3, 4).toLowerCase()
                + method.getName().substring(4);
              stsMp.put(fldNm, method);
            }
          }
          //assigning fully initialized object:
          this.setsMap.put(pCls, stsMp);
        }
      }
    }
    Method rz = stsMp.get(pFlNm);
    if (rz == null) {
      throw new RuntimeException("Can't get setter for cls/fld: " + pCls
        + "/" + pFlNm);
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for reflect.</p>
   * @return IReflect
   **/
  public final IReflect getReflect() {
    return this.reflect;
  }

  /**
   * <p>Setter for reflect.</p>
   * @param pReflect reference
   **/
  public final void setReflect(final IReflect pReflect) {
    this.reflect = pReflect;
  }
}
