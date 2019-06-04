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
import org.beigesoft.cnv.ICnvRsFdv;
import org.beigesoft.cnv.CvRsFvBgd;
import org.beigesoft.cnv.CvRsFvBln;
import org.beigesoft.cnv.CvRsFvDbl;
import org.beigesoft.cnv.CvRsFvDt;
import org.beigesoft.cnv.CvRsFvFlt;
import org.beigesoft.cnv.CvRsFvInt;
import org.beigesoft.cnv.CvRsFvLng;
import org.beigesoft.cnv.CvRsFvStr;

/**
 * <p>Factory of fields converters from DB result-set.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctNmCnFrRs<RS> implements IFcCnRsFdv<RS> {

  //services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, ICnvRsFdv<?, RS>> convrts
    = new HashMap<String, ICnvRsFdv<?, RS>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final ICnvRsFdv<?, RS> laz(final Map<String, Object> pRvs,
    final String pCnNm) throws Exception {
    ICnvRsFdv<?, RS> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CvRsFvBgd.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvBgd();
          } else if (CvRsFvBln.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvBln();
          } else if (CvRsFvDbl.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvDbl();
          } else if (CvRsFvDt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvDt();
          } else if (CvRsFvFlt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvFlt();
          } else if (CvRsFvInt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvInt();
          } else if (CvRsFvLng.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvLng();
          } else if (CvRsFvStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCvRsFvStr();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no CNV FR RS: " + pCnNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvBgd.</p>
   * @return CvRsFvBgd
   */
  private CvRsFvBgd crPuCvRsFvBgd() {
    CvRsFvBgd rz = new CvRsFvBgd();
    this.convrts.put(CvRsFvBgd.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvBgd.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvBln.</p>
   * @return CvRsFvBln
   */
  private CvRsFvBln crPuCvRsFvBln() {
    CvRsFvBln rz = new CvRsFvBln();
    this.convrts.put(CvRsFvBln.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvBln.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvDbl.</p>
   * @return CvRsFvDbl
   */
  private CvRsFvDbl crPuCvRsFvDbl() {
    CvRsFvDbl rz = new CvRsFvDbl();
    this.convrts.put(CvRsFvDbl.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvDbl.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvDt.</p>
   * @return CvRsFvDt
   */
  private CvRsFvDt crPuCvRsFvDt() {
    CvRsFvDt rz = new CvRsFvDt();
    this.convrts.put(CvRsFvDt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvDt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvFlt.</p>
   * @return CvRsFvFlt
   */
  private CvRsFvFlt crPuCvRsFvFlt() {
    CvRsFvFlt rz = new CvRsFvFlt();
    this.convrts.put(CvRsFvFlt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvFlt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvInt.</p>
   * @return CvRsFvInt
   */
  private CvRsFvInt crPuCvRsFvInt() {
    CvRsFvInt rz = new CvRsFvInt();
    this.convrts.put(CvRsFvInt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvInt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvLng.</p>
   * @return CvRsFvLng
   */
  private CvRsFvLng crPuCvRsFvLng() {
    CvRsFvLng rz = new CvRsFvLng();
    this.convrts.put(CvRsFvLng.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvLng.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CvRsFvStr.</p>
   * @return CvRsFvStr
   */
  private CvRsFvStr crPuCvRsFvStr() {
    CvRsFvStr rz = new CvRsFvStr();
    this.convrts.put(CvRsFvStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CvRsFvStr.class.getSimpleName()
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
