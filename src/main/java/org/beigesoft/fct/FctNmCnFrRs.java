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
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.log.ILog;
import org.beigesoft.cnv.IConvNm;
import org.beigesoft.cnv.CnvBnRsBgd;
import org.beigesoft.cnv.CnvBnRsBln;
import org.beigesoft.cnv.CnvBnRsDbl;
import org.beigesoft.cnv.CnvBnRsDt;
import org.beigesoft.cnv.CnvBnRsFlt;
import org.beigesoft.cnv.CnvBnRsInt;
import org.beigesoft.cnv.CnvBnRsLng;
import org.beigesoft.cnv.CnvBnRsStr;

/**
 * <p>Factory of fields converters from DB result-set.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctNmCnFrRs<RS> implements IFctNm<IConvNm<IRecSet<RS>, ?>> {

  //services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, IConvNm<IRecSet<RS>, ?>> convrts
    = new HashMap<String, IConvNm<IRecSet<RS>, ?>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final IConvNm<IRecSet<RS>, ?> laz(final Map<String, Object> pRqVs,
    final String pCnNm) throws Exception {
    IConvNm<IRecSet<RS>, ?> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CnvBnRsBgd.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsBgd();
          } else if (CnvBnRsBln.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsBln();
          } else if (CnvBnRsDbl.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsDbl();
          } else if (CnvBnRsDt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsDt();
          } else if (CnvBnRsFlt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsFlt();
          } else if (CnvBnRsInt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsInt();
          } else if (CnvBnRsLng.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsLng();
          } else if (CnvBnRsStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBnRsStr();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no CNV FR RS: " + pCnNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsBgd.</p>
   * @return CnvBnRsBgd
   */
  private CnvBnRsBgd crPuCnvBnRsBgd() {
    CnvBnRsBgd rz = new CnvBnRsBgd();
    this.convrts.put(CnvBnRsBgd.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsBgd.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsBln.</p>
   * @return CnvBnRsBln
   */
  private CnvBnRsBln crPuCnvBnRsBln() {
    CnvBnRsBln rz = new CnvBnRsBln();
    this.convrts.put(CnvBnRsBln.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsBln.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsDbl.</p>
   * @return CnvBnRsDbl
   */
  private CnvBnRsDbl crPuCnvBnRsDbl() {
    CnvBnRsDbl rz = new CnvBnRsDbl();
    this.convrts.put(CnvBnRsDbl.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsDbl.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsDt.</p>
   * @return CnvBnRsDt
   */
  private CnvBnRsDt crPuCnvBnRsDt() {
    CnvBnRsDt rz = new CnvBnRsDt();
    this.convrts.put(CnvBnRsDt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsDt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsFlt.</p>
   * @return CnvBnRsFlt
   */
  private CnvBnRsFlt crPuCnvBnRsFlt() {
    CnvBnRsFlt rz = new CnvBnRsFlt();
    this.convrts.put(CnvBnRsFlt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsFlt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsInt.</p>
   * @return CnvBnRsInt
   */
  private CnvBnRsInt crPuCnvBnRsInt() {
    CnvBnRsInt rz = new CnvBnRsInt();
    this.convrts.put(CnvBnRsInt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsInt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsLng.</p>
   * @return CnvBnRsLng
   */
  private CnvBnRsLng crPuCnvBnRsLng() {
    CnvBnRsLng rz = new CnvBnRsLng();
    this.convrts.put(CnvBnRsLng.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsLng.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBnRsStr.</p>
   * @return CnvBnRsStr
   */
  private CnvBnRsStr crPuCnvBnRsStr() {
    CnvBnRsStr rz = new CnvBnRsStr();
    this.convrts.put(CnvBnRsStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBnRsStr.class.getSimpleName()
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
