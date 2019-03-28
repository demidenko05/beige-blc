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

package org.beigesoft.fct;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.cnv.IConv;
import org.beigesoft.cnv.CnvStrLng;
import org.beigesoft.cnv.CnvStrInt;
import org.beigesoft.cnv.CnvStrFlt;
import org.beigesoft.cnv.CnvStrDbl;
import org.beigesoft.cnv.CnvStrBln;
import org.beigesoft.cnv.CnvStrBgd;

/**
 * <p>Factory of fields converters to string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmCnFrSt implements IFctNm<IConv<String, ?>> {

  //services:
  /**
   * <p>Outside app-beans factory.</p>
   **/
  private ILog logStd;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, IConv<String, ?>> convrts
    = new HashMap<String, IConv<String, ?>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final IConv<String, ?> laz(final Map<String, Object> pRqVs,
    final String pCnNm) throws Exception {
    IConv<String, ?> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CnvStrLng.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrLng();
          } else if (CnvStrInt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrInt();
          } else if (CnvStrFlt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrFlt();
          } else if (CnvStrDbl.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDbl();
          } else if (CnvStrBln.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrBln();
          } else if (CnvStrBgd.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrBgd();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no CNV FR STR: " + pCnNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrLng.</p>
   * @return CnvStrLng
   */
  private CnvStrLng crPuCnvStrLng() {
    CnvStrLng rz = new CnvStrLng();
    this.convrts.put(CnvStrLng.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrLng.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrInt.</p>
   * @return CnvStrInt
   */
  private CnvStrInt crPuCnvStrInt() {
    CnvStrInt rz = new CnvStrInt();
    this.convrts.put(CnvStrInt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrInt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrFlt.</p>
   * @return CnvStrFlt
   */
  private CnvStrFlt crPuCnvStrFlt() {
    CnvStrFlt rz = new CnvStrFlt();
    this.convrts.put(CnvStrFlt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrFlt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrDbl.</p>
   * @return CnvStrDbl
   */
  private CnvStrDbl crPuCnvStrDbl() {
    CnvStrDbl rz = new CnvStrDbl();
    this.convrts.put(CnvStrDbl.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDbl.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrBln.</p>
   * @return CnvStrBln
   */
  private CnvStrBln crPuCnvStrBln() {
    CnvStrBln rz = new CnvStrBln();
    this.convrts.put(CnvStrBln.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrBln.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrBgd.</p>
   * @return CnvStrBgd
   */
  private CnvStrBgd crPuCnvStrBgd() {
    CnvStrBgd rz = new CnvStrBgd();
    this.convrts.put(CnvStrBgd.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrBgd.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for logStd.</p>
   * @return ILog
   **/
  public final ILog getLogStd() {
    return this.logStd;
  }

  /**
   * <p>Setter for logStd.</p>
   * @param pLogStd reference
   **/
  public final void setLogStd(final ILog pLogStd) {
    this.logStd = pLogStd;
  }
}
