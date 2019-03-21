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

package org.beigesoft.srv;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * <p>Abstraction reflection service.</p>
 *
 * @author Yury Demidenko
 */
public interface IReflect {

  /**
   * <p>Retrieve all non-static private members from given class.</p>
   * @param pCls - class
   * @return Field[] fields.
   **/
  Field[] retFlds(Class<?> pCls);

  /**
   * <p>Retrieve all non-static non-private methods from given class.</p>
   * @param pCls - class
   * @return Method[] fields.
   **/
  Method[] retMths(Class<?> pCls);

  /**
   * <p>Retrieve member from given class.</p>
   * @param pCls - class
   * @param pFlNm - field name
   * @return field
   * @throws Exception if not exist
   **/
  Field retFld(Class<?> pCls, String pFlNm);

  /**
   * <p>Retrieve method from given class by name.</p>
   * @param pCls - class
   * @param pMthNm - method name
   * @return method
   * @throws Exception if not exist
   **/
  Method retMth(Class<?> pCls, String pMthNm);

  /**
   * <p>Retrieve getter from given class by field name.</p>
   * @param pCls - class
   * @param pFlNm - field name
   * @return getter or NULL.
   **/
  Method retGet(Class<?> pCls, String pFlNm);

  /**
   * <p>Retrieve setter from given class by field name.</p>
   * @param pCls - class
   * @param pFlNm - field name
   * @return setter or NULL.
   **/
  Method retSet(Class<?> pCls, String pFlNm);
}
