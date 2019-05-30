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
import org.beigesoft.cnv.CnvStrBgdNf;
import org.beigesoft.cnv.CnvStrDt;
import org.beigesoft.cnv.CnvStrDtSc;
import org.beigesoft.cnv.CnvStrDtMsFm;
import org.beigesoft.cnv.CnvStrDtTm;
import org.beigesoft.cnv.CnvStrStr;
import org.beigesoft.cnv.CnvStrDtMs;
import org.beigesoft.cnv.CnvStrFrStrXml;
import org.beigesoft.cnv.CnvStrDblFm;
import org.beigesoft.cnv.CnvStrFltFm;
import org.beigesoft.cnv.CnvStrIntFm;
import org.beigesoft.cnv.CnvStrLngFm;
import org.beigesoft.cnv.CnvStrMnth;
import org.beigesoft.srv.IUtlXml;
import org.beigesoft.srv.ISrvDt;

/**
 * <p>Factory of fields converters from string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmCnFrSt implements IFctNm<IConv<String, ?>> {

  //services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>XML utility.</p>
   **/
  private IUtlXml utlXml;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

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
          } else if (CnvStrDt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDt();
          } else if (CnvStrDtSc.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDtSc();
          } else if (CnvStrDtMsFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDtMsFm();
          } else if (CnvStrMnth.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrMnth();
          } else if (CnvStrDtTm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDtTm();
          } else if (CnvStrStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrStr();
          } else if (CnvStrDtMs.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDtMs();
          } else if (CnvStrFrStrXml.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrFrStrXml();
          } else if (CnvStrInt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrInt();
          } else if (CnvStrFlt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrFlt();
          } else if (CnvStrDblFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDblFm();
          } else if (CnvStrFltFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrFltFm();
          } else if (CnvStrIntFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrIntFm();
          } else if (CnvStrLngFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrLngFm();
          } else if (CnvStrDbl.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrDbl();
          } else if (CnvStrBln.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrBln();
          } else if (CnvStrBgdNf.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrBgdNf();
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
   * <p>Create and put into the Map CnvStrDt.</p>
   * @return CnvStrDt
   */
  private CnvStrDt crPuCnvStrDt() {
    CnvStrDt rz = new CnvStrDt();
    this.convrts.put(CnvStrDt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrDtSc.</p>
   * @return CnvStrDtSc
   */
  private CnvStrDtSc crPuCnvStrDtSc() {
    CnvStrDtSc rz = new CnvStrDtSc();
    this.convrts.put(CnvStrDtSc.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDtSc.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrDtMsFm.</p>
   * @return CnvStrDtMsFm
   */
  private CnvStrDtMsFm crPuCnvStrDtMsFm() {
    CnvStrDtMsFm rz = new CnvStrDtMsFm();
    this.convrts.put(CnvStrDtMsFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDtMsFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrMnth.</p>
   * @return CnvStrMnth
   */
  private CnvStrMnth crPuCnvStrMnth() {
    CnvStrMnth rz = new CnvStrMnth();
    rz.setSrvDt(getSrvDt());
    this.convrts.put(CnvStrMnth.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrMnth.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrDtTm.</p>
   * @return CnvStrDtTm
   */
  private CnvStrDtTm crPuCnvStrDtTm() {
    CnvStrDtTm rz = new CnvStrDtTm();
    this.convrts.put(CnvStrDtTm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDtTm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrDtMs.</p>
   * @return CnvStrDtMs
   */
  private CnvStrDtMs crPuCnvStrDtMs() {
    CnvStrDtMs rz = new CnvStrDtMs();
    this.convrts.put(CnvStrDtMs.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDtMs.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrFrStrXml.</p>
   * @return CnvStrFrStrXml
   */
  private CnvStrFrStrXml crPuCnvStrFrStrXml() {
    CnvStrFrStrXml rz = new CnvStrFrStrXml();
    rz.setUtlXml(getUtlXml());
    this.convrts.put(CnvStrFrStrXml.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrFrStrXml.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrStr.</p>
   * @return CnvStrStr
   */
  private CnvStrStr crPuCnvStrStr() {
    CnvStrStr rz = new CnvStrStr();
    this.convrts.put(CnvStrStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrStr.class.getSimpleName()
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
   * <p>Create and put into the Map CnvStrDblFm.</p>
   * @return CnvStrDblFm
   */
  private CnvStrDblFm crPuCnvStrDblFm() {
    CnvStrDblFm rz = new CnvStrDblFm();
    this.convrts.put(CnvStrDblFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrDblFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrFltFm.</p>
   * @return CnvStrFltFm
   */
  private CnvStrFltFm crPuCnvStrFltFm() {
    CnvStrFltFm rz = new CnvStrFltFm();
    this.convrts.put(CnvStrFltFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrFltFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrIntFm.</p>
   * @return CnvStrIntFm
   */
  private CnvStrIntFm crPuCnvStrIntFm() {
    CnvStrIntFm rz = new CnvStrIntFm();
    this.convrts.put(CnvStrIntFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrIntFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrLngFm.</p>
   * @return CnvStrLngFm
   */
  private CnvStrLngFm crPuCnvStrLngFm() {
    CnvStrLngFm rz = new CnvStrLngFm();
    this.convrts.put(CnvStrLngFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrLngFm.class.getSimpleName()
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
   * <p>Create and put into the Map CnvStrBgdNf.</p>
   * @return CnvStrBgdNf
   */
  private CnvStrBgdNf crPuCnvStrBgdNf() {
    CnvStrBgdNf rz = new CnvStrBgdNf();
    this.convrts.put(CnvStrBgdNf.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrBgdNf.class.getSimpleName()
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

  /**
   * <p>Getter for utlXml.</p>
   * @return IUtlXml
   **/
  public final IUtlXml getUtlXml() {
    return this.utlXml;
  }

  /**
   * <p>Setter for utlXml.</p>
   * @param pUtlXml reference
   **/
  public final void setUtlXml(final IUtlXml pUtlXml) {
    this.utlXml = pUtlXml;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }
}
