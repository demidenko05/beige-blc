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

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.ICnToSt;
import org.beigesoft.cnv.CnvMnthStr;
import org.beigesoft.cnv.CnvDtStr;
import org.beigesoft.cnv.CnvDtScStr;
import org.beigesoft.cnv.CnvDtMsStr;
import org.beigesoft.cnv.CnvDtTmStr;
import org.beigesoft.cnv.CnvEnmStr;
import org.beigesoft.cnv.CnvDtStrMs;
import org.beigesoft.cnv.CnvStrToStrXml;
import org.beigesoft.cnv.CnvSmpStr;
import org.beigesoft.cnv.CnvBlnStr;
import org.beigesoft.cnv.CnvHsIdStr;
import org.beigesoft.cnv.CnvMaxStr;
import org.beigesoft.cnv.CnvTxrStr;
import org.beigesoft.cnv.CnvPriStr;
import org.beigesoft.cnv.CnvQuanStr;
import org.beigesoft.cnv.CnvCostStr;
import org.beigesoft.cnv.CnvDblStrFm;
import org.beigesoft.cnv.CnvFltStrFm;
import org.beigesoft.cnv.CnvIntStrFm;
import org.beigesoft.cnv.CnvLngStrFm;
import org.beigesoft.srv.INumStr;
import org.beigesoft.srv.IUtlXml;
import org.beigesoft.srv.ISrvDt;

/**
 * <p>Factory of fields converters to string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmCnToSt implements IFctCnToSt {

  /**
   * <p>DB-Copy converter owned entity to string name.</p>
   **/
  public static final String CNHSIDSTDBCPNM = "cnHsIdStDbCp";

  /**
   * <p>UVD converter owned entity to string name.</p>
   **/
  public static final String CNHSIDSTUVDNM = "cnHsIdStUvd";

  //services:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //parts:
  /**
   * <p>Number to string service.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Fields converters names holder UVD.</p>
   **/
  private IHlNmClSt hldNmFdCnUvd;

  /**
   * <p>Fields converters names holder DBCP.</p>
   **/
  private IHlNmClSt hldNmFdCnDbcp;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Settings service UVD.</p>
   **/
  private ISetng stgUvd;

  /**
   * <p>Settings service DBCP.</p>
   **/
  private ISetng stgDbCp;

  /**
   * <p>XML utility.</p>
   **/
  private IUtlXml utlXml;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Outside factories.</p>
   **/
  private Set<IFctCnToSt> fcts;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, ICnToSt<?>> convrts
    = new HashMap<String, ICnToSt<?>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  @Override
  public final ICnToSt<?> laz(final Map<String, Object> pRvs,
    final String pCnNm) throws Exception {
    ICnToSt<?> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CnvSmpStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvSmpStr();
          } else if (CnvDtStrMs.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtStrMs();
          } else if (CnvStrToStrXml.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvStrToStrXml();
          } else if (CnvDtStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtStr();
          } else if (CnvMnthStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvMnthStr();
          } else if (CnvDtScStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtScStr();
          } else if (CnvDtMsStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtMsStr();
          } else if (CnvDtTmStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDtTmStr();
          } else if (CnvEnmStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvEnmStr();
          } else if (CNHSIDSTUVDNM.equals(pCnNm)) {
            rz = crPuCnvHsIdStrUvd();
          } else if (CNHSIDSTDBCPNM.equals(pCnNm)) {
            rz = crPuCnvHsIdStrDbCp();
          } else if (CnvCostStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvCostStr();
          } else if (CnvQuanStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvQuanStr();
          } else if (CnvMaxStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvMaxStr();
          } else if (CnvDblStrFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvDblStrFm();
          } else if (CnvFltStrFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvFltStrFm();
          } else if (CnvIntStrFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIntStrFm();
          } else if (CnvLngStrFm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvLngStrFm();
          } else if (CnvTxrStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvTxrStr();
          } else if (CnvPriStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvPriStr();
          } else if (CnvBlnStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBlnStr();
          } else {
            if (this.fcts != null) {
              for (IFctCnToSt fc : this.fcts) {
                rz = fc.laz(pRvs, pCnNm);
                if (rz != null) {
                  break;
                }
              }
            }
            if (rz == null) {
              throw new ExcCode(ExcCode.WRCN, "There is no CNVTOSTR: " + pCnNm);
            }
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvMnthStr.</p>
   * @return CnvMnthStr
   */
  private CnvMnthStr crPuCnvMnthStr() {
    CnvMnthStr rz = new CnvMnthStr();
    rz.setSrvDt(getSrvDt());
    this.convrts.put(CnvMnthStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvMnthStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtStr.</p>
   * @return CnvDtStr
   */
  private CnvDtStr crPuCnvDtStr() {
    CnvDtStr rz = new CnvDtStr();
    rz.setSrvDt(getSrvDt());
    this.convrts.put(CnvDtStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtScStr.</p>
   * @return CnvDtScStr
   */
  private CnvDtScStr crPuCnvDtScStr() {
    CnvDtScStr rz = new CnvDtScStr();
    rz.setSrvDt(getSrvDt());
    this.convrts.put(CnvDtScStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtScStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtMsStr.</p>
   * @return CnvDtMsStr
   */
  private CnvDtMsStr crPuCnvDtMsStr() {
    CnvDtMsStr rz = new CnvDtMsStr();
    rz.setSrvDt(getSrvDt());
    this.convrts.put(CnvDtMsStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtMsStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtTmStr.</p>
   * @return CnvDtTmStr
   */
  private CnvDtTmStr crPuCnvDtTmStr() {
    CnvDtTmStr rz = new CnvDtTmStr();
    rz.setSrvDt(getSrvDt());
    this.convrts.put(CnvDtTmStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtTmStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvEnmStr.</p>
   * @return CnvEnmStr
   */
  private CnvEnmStr<?> crPuCnvEnmStr() {
    CnvEnmStr rz = new CnvEnmStr();
    this.convrts.put(CnvEnmStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvEnmStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map UVD CnvHsIdStr.</p>
   * @return UVD CnvHsIdStr
   */
  private CnvHsIdStr<IHasId<?>> crPuCnvHsIdStrUvd() {
    CnvHsIdStr<IHasId<?>> rz = new CnvHsIdStr<IHasId<?>>();
    rz.setHldGets(getHldGets());
    rz.setSetng(getStgUvd());
    this.convrts.put(CNHSIDSTUVDNM, rz);
    getLogStd().info(null, getClass(), CNHSIDSTUVDNM + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DBCP CnvHsIdStr.</p>
   * @return DBCP CnvHsIdStr
   */
  private CnvHsIdStr<IHasId<?>> crPuCnvHsIdStrDbCp() {
    CnvHsIdStr<IHasId<?>> rz = new CnvHsIdStr<IHasId<?>>();
    rz.setHldGets(getHldGets());
    rz.setSetng(getStgDbCp());
    this.convrts.put(CNHSIDSTDBCPNM, rz);
    getLogStd().info(null, getClass(), CNHSIDSTDBCPNM + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvCostStr.</p>
   * @return CnvCostStr
   */
  private CnvCostStr crPuCnvCostStr() {
    CnvCostStr rz = new CnvCostStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvCostStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvCostStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvQuanStr.</p>
   * @return CnvQuanStr
   */
  private CnvQuanStr crPuCnvQuanStr() {
    CnvQuanStr rz = new CnvQuanStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvQuanStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvQuanStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvMaxStr.</p>
   * @return CnvMaxStr
   */
  private CnvMaxStr crPuCnvMaxStr() {
    CnvMaxStr rz = new CnvMaxStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvMaxStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvMaxStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDblStrFm.</p>
   * @return CnvDblStrFm
   */
  private CnvDblStrFm crPuCnvDblStrFm() {
    CnvDblStrFm rz = new CnvDblStrFm();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvDblStrFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDblStrFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvFltStrFm.</p>
   * @return CnvFltStrFm
   */
  private CnvFltStrFm crPuCnvFltStrFm() {
    CnvFltStrFm rz = new CnvFltStrFm();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvFltStrFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvFltStrFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIntStrFm.</p>
   * @return CnvIntStrFm
   */
  private CnvIntStrFm crPuCnvIntStrFm() {
    CnvIntStrFm rz = new CnvIntStrFm();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvIntStrFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIntStrFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvLngStrFm.</p>
   * @return CnvLngStrFm
   */
  private CnvLngStrFm crPuCnvLngStrFm() {
    CnvLngStrFm rz = new CnvLngStrFm();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvLngStrFm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvLngStrFm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvTxrStr.</p>
   * @return CnvTxrStr
   */
  private CnvTxrStr crPuCnvTxrStr() {
    CnvTxrStr rz = new CnvTxrStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvTxrStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvTxrStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvPriStr.</p>
   * @return CnvPriStr
   */
  private CnvPriStr crPuCnvPriStr() {
    CnvPriStr rz = new CnvPriStr();
    rz.setNumStr(getNumStr());
    this.convrts.put(CnvPriStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvPriStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBlnStr.</p>
   * @return CnvBlnStr
   */
  private CnvBlnStr crPuCnvBlnStr() {
    CnvBlnStr rz = new CnvBlnStr();
    this.convrts.put(CnvBlnStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvBlnStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvDtStrMs.</p>
   * @return CnvDtStrMs
   */
  private CnvDtStrMs crPuCnvDtStrMs() {
    CnvDtStrMs rz = new CnvDtStrMs();
    this.convrts.put(CnvDtStrMs.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvDtStrMs.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvStrToStrXml.</p>
   * @return CnvStrToStrXml
   */
  private CnvStrToStrXml crPuCnvStrToStrXml() {
    CnvStrToStrXml rz = new CnvStrToStrXml();
    rz.setUtlXml(getUtlXml());
    this.convrts.put(CnvStrToStrXml.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvStrToStrXml.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvSmpStr.</p>
   * @return CnvSmpStr
   */
  private CnvSmpStr<?> crPuCnvSmpStr() {
    CnvSmpStr<Object> rz = new CnvSmpStr<Object>();
    this.convrts.put(CnvSmpStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvSmpStr.class.getSimpleName()
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
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }

  /**
   * <p>Getter for hldNmFdCnDbcp.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldNmFdCnDbcp() {
    return this.hldNmFdCnDbcp;
  }

  /**
   * <p>Setter for hldNmFdCnDbcp.</p>
   * @param pHldNmFdCnDbcp reference
   **/
  public final void setHldNmFdCnDbcp(final IHlNmClSt pHldNmFdCnDbcp) {
    this.hldNmFdCnDbcp = pHldNmFdCnDbcp;
  }

  /**
   * <p>Getter for hldNmFdCnUvd.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldNmFdCnUvd() {
    return this.hldNmFdCnUvd;
  }

  /**
   * <p>Setter for hldNmFdCnUvd.</p>
   * @param pHldNmFdCnUvd reference
   **/
  public final void setHldNmFdCnUvd(final IHlNmClSt pHldNmFdCnUvd) {
    this.hldNmFdCnUvd = pHldNmFdCnUvd;
  }

  /**
   * <p>Getter for hldGets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHlNmClMt pHldGets) {
    this.hldGets = pHldGets;
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
   * <p>Getter for stgUvd.</p>
   * @return ISetng
   **/
  public final ISetng getStgUvd() {
    return this.stgUvd;
  }

  /**
   * <p>Setter for stgUvd.</p>
   * @param pStgUvd reference
   **/
  public final void setStgUvd(final ISetng pStgUvd) {
    this.stgUvd = pStgUvd;
  }

  /**
   * <p>Getter for stgDbCp.</p>
   * @return ISetng
   **/
  public final ISetng getStgDbCp() {
    return this.stgDbCp;
  }

  /**
   * <p>Setter for stgDbCp.</p>
   * @param pStgDbCp reference
   **/
  public final void setStgDbCp(final ISetng pStgDbCp) {
    this.stgDbCp = pStgDbCp;
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

  /**
   * <p>Getter for fcts.</p>
   * @return Set<IFctCnToSt>
   **/
  public final synchronized Set<IFctCnToSt> getFcts() {
    return this.fcts;
  }

  /**
   * <p>Setter for fcts.</p>
   * @param pFcts reference
   **/
  public final synchronized void setFcts(final Set<IFctCnToSt> pFcts) {
    this.fcts = pFcts;
  }
}
