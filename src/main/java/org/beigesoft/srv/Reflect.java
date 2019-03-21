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
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.HashSet;

/**
 * <p>Simple reflection service.</p>
 *
 * @author Yury Demidenko
 */
public class Reflect implements IReflect {

  /**
   * <p>Retrieve all nonstatic private members from given class.</p>
   * @param clazz - class
   * @return Field[] fields.
   **/
  @Override
  public final Field[] retFlds(final Class<?> clazz) {
    Set<Field> fldSet = new HashSet<Field>();
    for (Field fld : clazz.getDeclaredFields()) {
      int mdfMsk = fld.getModifiers();
      if ((mdfMsk & Modifier.PRIVATE) > 0
            && (mdfMsk & Modifier.STATIC) == 0) {
        fldSet.add(fld);
      }
    }
    final Class<?> supCls = clazz.getSuperclass();
    if (supCls != null && supCls != java.lang.Object.class) {
      for (Field fld : retFlds(supCls)) {
        fldSet.add(fld);
      }
    }
    return fldSet.toArray(new Field[fldSet.size()]);
  }

  /**
   * <p>Retrieve all nonstatic non-private methods from given class.</p>
   * @param clazz - class
   * @return Method[] fields.
   **/
  @Override
  public final Method[] retMths(final Class<?> clazz) {
    Set<Method> fldSet = new HashSet<Method>();
    for (Method mfd : clazz.getDeclaredMethods()) {
      int mdfMsk = mfd.getModifiers();
      if ((mdfMsk & Modifier.PRIVATE) == 0
            && (mdfMsk & Modifier.STATIC) == 0) {
        fldSet.add(mfd);
      }
    }
    final Class<?> supCls = clazz.getSuperclass();
    if (supCls != null && supCls != java.lang.Object.class) {
      for (Method mfd : retMths(supCls)) {
        fldSet.add(mfd);
      }
    }
    return fldSet.toArray(new Method[fldSet.size()]);
  }

  /**
   * <p>Retrieve member from given class.</p>
   * @param pCls - class
   * @param pFlNm - field name
   * @return field or NULL.
   **/
  @Override
  public final Field retFld(final Class<?> pCls, final String pFlNm) {
    for (Field fld : pCls.getDeclaredFields()) {
      if (fld.getName().equals(pFlNm)) {
        return fld;
      }
    }
    final Class<?> supCls = pCls.getSuperclass();
    Field field = null;
    if (supCls != null && supCls != java.lang.Object.class) {
      field = retFld(supCls, pFlNm);
    }
    return field;
  }

  /**
   * <p>Retrieve method from given class by name.</p>
   * @param pCls - class
   * @param pMthNm - method name
   * @return method or NULL.
   **/
  @Override
  public final Method retMth(final Class<?> pCls, final String pMthNm) {
    for (Method mfd : pCls.getDeclaredMethods()) {
      if (mfd.getName().equals(pMthNm)) {
        return mfd;
      }
    }
    final Class<?> supCls = pCls.getSuperclass();
    Method mth = null;
    if (supCls != null && supCls != java.lang.Object.class) {
      mth = retMth(supCls, pMthNm);
    }
    return mth;
  }

  /**
   * <p>Retrieve getter from given class by field name.</p>
   * @param pCls - class
   * @param pFlNm - field name
   * @return Method getter.
   **/
  @Override
  public final Method retGet(final Class<?> pCls, final String pFlNm) {
    String gtNm = "get" + pFlNm.substring(0, 1).toUpperCase()
      + pFlNm.substring(1);
    return retMth(pCls, gtNm);
  }

  /**
   * <p>Retrieve setter from given class by field name.</p>
   * @param pCls - class
   * @param pFlNm - field name
   * @return setter or NULL.
   **/
  @Override
  public final Method retSet(final Class<?> pCls, final String pFlNm) {
    String stNm = "set" + pFlNm.substring(0, 1).toUpperCase()
      + pFlNm.substring(1);
    return retMth(pCls, stNm);
  }
}
