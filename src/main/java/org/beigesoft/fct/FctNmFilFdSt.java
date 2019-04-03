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
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.IConv;
import org.beigesoft.cnv.IFilFld;
import org.beigesoft.cnv.FilFldEnmStr;
import org.beigesoft.cnv.FilFldHsIdStr;
import org.beigesoft.cnv.FilFldSmpStr;

/**
 * <p>Factory of fields fillers from string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmFilFdSt implements IFctNm<IFilFld<String>> {

  /**
   * <p>DB-Copy filler owned entity from string name.</p>
   **/
  public static final String FILHSIDSTDBCPNM = "flHsIdStDbCp";

  /**
   * <p>UVD filler owned entity from string name.</p>
   **/
  public static final String FILHSIDSTUVDNM = "flHsIdStUvd";

  /**
   * <p>DB-Copy filler simple from string name.</p>
   **/
  public static final String FILSMPSTDBCPNM = "flSmpStDbCp";

  /**
   * <p>UVD filler owned entity from string name.</p>
   **/
  public static final String FILSMPSTUVDNM = "flSmpStUvd";

  //services:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //parts:
  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Fields converters names holder DBCP.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCnDbCp;

  /**
   * <p>Fields converters names holder UVD.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCnUvd;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctNm<IConv<String, ?>> fctCnvFld;

  /**
   * <p>Holder of fillers fields names UVD.</p>
   **/
  private IHldNm<Class<?>, String> hldFilFdNmsUvd;

  /**
   * <p>Holder of fillers fields names DBCP.</p>
   **/
  private IHldNm<Class<?>, String> hldFilFdNmsDbCp;

  /**
   * <p>Settings service UVD.</p>
   **/
  private ISetng stgUvd;

  /**
   * <p>Settings service DBCP.</p>
   **/
  private ISetng stgDbCp;

  //requested data:
  /**
   * <p>Fillers map.</p>
   **/
  private final Map<String, IFilFld<String>> fillers
    = new HashMap<String, IFilFld<String>>();

  /**
   * <p>Get filler in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pFiNm - filler name
   * @return requested filler
   * @throws Exception - an exception
   */
  public final IFilFld<String> laz(final Map<String, Object> pRqVs,
    final String pFiNm) throws Exception {
    IFilFld<String> rz = this.fillers.get(pFiNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.fillers.get(pFiNm);
        if (rz == null) {
          if (FilFldEnmStr.class.getSimpleName().equals(pFiNm)) {
            rz = crPuFilFldEnmStr();
          } else if (FILHSIDSTDBCPNM.equals(pFiNm)) {
            rz = crPuFilFldHsIdStrDbCp();
          } else if (FILHSIDSTUVDNM.equals(pFiNm)) {
            rz = crPuFilFldHsIdStrUvd();
          } else if (FILSMPSTDBCPNM.equals(pFiNm)) {
            rz = crPuFilFldSmpStrDbCp();
          } else if (FILSMPSTUVDNM.equals(pFiNm)) {
            rz = crPuFilFldSmpStrUvd();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no FIL FR STR: " + pFiNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map FilFldEnmStr.</p>
   * @return FilFldEnmStr
   */
  private FilFldEnmStr crPuFilFldEnmStr() {
    FilFldEnmStr rz = new FilFldEnmStr();
    rz.setHldSets(getHldSets());
    rz.setHldFdCls(getHldFdCls());
    this.fillers.put(FilFldEnmStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilFldEnmStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map UVD FilFldHsIdStr.</p>
   * @return UVD FilFldHsIdStr
   */
  private FilFldHsIdStr crPuFilFldHsIdStrUvd() {
    FilFldHsIdStr rz = new FilFldHsIdStr();
    rz.setHldSets(getHldSets());
    rz.setHldFilFdNms(getHldFilFdNmsUvd());
    rz.setSetng(getStgUvd());
    rz.setHldFdCls(getHldFdCls());
    rz.setFctFilFld(this);
    this.fillers.put(FILHSIDSTUVDNM, rz);
    getLogStd().info(null, getClass(), FILHSIDSTUVDNM + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DBCP FilFldHsIdStr.</p>
   * @return DBCP FilFldHsIdStr
   */
  private FilFldHsIdStr crPuFilFldHsIdStrDbCp() {
    FilFldHsIdStr rz = new FilFldHsIdStr();
    rz.setHldSets(getHldSets());
    rz.setHldFilFdNms(getHldFilFdNmsUvd());
    rz.setSetng(getStgDbCp());
    rz.setHldFdCls(getHldFdCls());
    rz.setFctFilFld(this);
    this.fillers.put(FILHSIDSTDBCPNM, rz);
    getLogStd().info(null, getClass(), FILHSIDSTDBCPNM + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DBCP FilFldSmpStr.</p>
   * @return DBCP FilFldSmpStr
   */
  private FilFldSmpStr crPuFilFldSmpStrDbCp() {
    FilFldSmpStr rz = new FilFldSmpStr();
    rz.setHldSets(getHldSets());
    rz.setHldNmFdCn(getHldNmFdCnDbCp());
    rz.setFctCnvFld(getFctCnvFld());
    this.fillers.put(FILSMPSTDBCPNM, rz);
    getLogStd().info(null, getClass(), FILSMPSTDBCPNM + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map UVD FilFldSmpStr.</p>
   * @return UVD FilFldSmpStr
   */
  private FilFldSmpStr crPuFilFldSmpStrUvd() {
    FilFldSmpStr rz = new FilFldSmpStr();
    rz.setHldSets(getHldSets());
    rz.setHldNmFdCn(getHldNmFdCnUvd());
    rz.setFctCnvFld(getFctCnvFld());
    this.fillers.put(FILSMPSTUVDNM, rz);
    getLogStd().info(null, getClass(), FILSMPSTUVDNM + " has been created.");
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
   * <p>Getter for hldSets.</p>
   * @return IHldNm<Class<?>, Method>
   **/
  public final IHldNm<Class<?>, Method> getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHldNm<Class<?>, Method> pHldSets) {
    this.hldSets = pHldSets;
  }


  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctCls<IConv<String, Object>
   **/
  public final IFctNm<IConv<String, ?>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IConv<String, ?>> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }

  /**
   * <p>Getter for DBCP hldNmFdCn.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldNmFdCnDbCp() {
    return this.hldNmFdCnDbCp;
  }

  /**
   * <p>Setter for DBCP hldNmFdCn.</p>
   * @param pHlNmFdCn DBCP reference
   **/
  public final void setHldNmFdCnDbCp(final IHldNm<Class<?>, String> pHlNmFdCn) {
    this.hldNmFdCnDbCp = pHlNmFdCn;
  }

  /**
   * <p>Getter for UVD hldNmFdCn.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldNmFdCnUvd() {
    return this.hldNmFdCnUvd;
  }

  /**
   * <p>Setter for UVD hldNmFdCn.</p>
   * @param pHldNmFdCn UVD reference
   **/
  public final void setHldNmFdCnUvd(final IHldNm<Class<?>, String> pHldNmFdCn) {
    this.hldNmFdCnUvd = pHldNmFdCn;
  }

  /**
   * <p>Getter for hldFdCls.</p>
   * @return IHldNm<Class<?>, Class<?>>
   **/
  public final IHldNm<Class<?>, Class<?>> getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHldNm<Class<?>, Class<?>> pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  /**
   * <p>Getter for UVD hldFilFdNms.</p>
   * @return UVD IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldFilFdNmsUvd() {
    return this.hldFilFdNmsUvd;
  }

  /**
   * <p>Setter for UVD hldFilFdNms.</p>
   * @param pHldFilFdNmsUvd reference
   **/
  public final void setHldFilFdNmsUvd(
    final IHldNm<Class<?>, String> pHldFilFdNmsUvd) {
    this.hldFilFdNmsUvd = pHldFilFdNmsUvd;
  }

  /**
   * <p>Getter for DBCP hldFilFdNms.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldFilFdNmsDbCp() {
    return this.hldFilFdNmsDbCp;
  }

  /**
   * <p>Setter for DBCP hldFilFdNms.</p>
   * @param pHldFilFdNmsDbCp reference
   **/
  public final void setHldFilFdNmsDbCp(
    final IHldNm<Class<?>, String> pHldFilFdNmsDbCp) {
    this.hldFilFdNmsDbCp = pHldFilFdNmsDbCp;
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
}
