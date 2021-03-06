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
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.IFilFldStr;
import org.beigesoft.cnv.FilFldEnmStr;
import org.beigesoft.cnv.FilFldHsIdStr;
import org.beigesoft.cnv.FilFldSmpStr;

/**
 * <p>Factory of fields fillers from string for XML and UVD.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmFilFdSt implements IFcFlFdSt {

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
  private IHlNmClMt hldSets;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Fields converters names holder DBCP.</p>
   **/
  private IHlNmClSt hldNmFdCnDbCp;

  /**
   * <p>Fields converters names holder UVD.</p>
   **/
  private IHlNmClSt hldNmFdCnUvd;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctCnFrSt fctCnvFld;

  /**
   * <p>Holder of fillers fields names UVD.</p>
   **/
  private IHlNmClSt hldFilFdNmsUvd;

  /**
   * <p>Holder of fillers fields names DBCP.</p>
   **/
  private IHlNmClSt hldFilFdNmsDbCp;

  /**
   * <p>Settings service UVD.</p>
   **/
  private ISetng stgUvd;

  /**
   * <p>Settings service DBCP.</p>
   **/
  private ISetng stgDbCp;

  /**
   * <p>Outside factories.</p>
   **/
  private Set<IFcFlFdSt> fcts;

  //requested data:
  /**
   * <p>Fillers map.</p>
   **/
  private final Map<String, IFilFldStr> fillers
    = new HashMap<String, IFilFldStr>();

  /**
   * <p>Get filler in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pFiNm - filler name
   * @return requested filler
   * @throws Exception - an exception
   */
  public final IFilFldStr laz(final Map<String, Object> pRvs,
    final String pFiNm) throws Exception {
    IFilFldStr rz = this.fillers.get(pFiNm);
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
            if (this.fcts != null) {
              for (IFcFlFdSt fc : this.fcts) {
                rz = fc.laz(pRvs, pFiNm);
                if (rz != null) {
                  break;
                }
              }
            }
            if (rz == null) {
              throw new ExcCode(ExcCode.WRCN, "There is no FILFRSTR: " + pFiNm);
            }
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
    rz.setLog(getLogStd());
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
    rz.setLog(getLogStd());
    rz.setHldSets(getHldSets());
    rz.setHldFilFdNms(getHldFilFdNmsDbCp());
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
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHlNmClMt pHldSets) {
    this.hldSets = pHldSets;
  }


  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctCnFrSt
   **/
  public final IFctCnFrSt getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(final IFctCnFrSt pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }

  /**
   * <p>Getter for DBCP hldNmFdCn.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldNmFdCnDbCp() {
    return this.hldNmFdCnDbCp;
  }

  /**
   * <p>Setter for DBCP hldNmFdCn.</p>
   * @param pHlNmFdCn DBCP reference
   **/
  public final void setHldNmFdCnDbCp(final IHlNmClSt pHlNmFdCn) {
    this.hldNmFdCnDbCp = pHlNmFdCn;
  }

  /**
   * <p>Getter for UVD hldNmFdCn.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldNmFdCnUvd() {
    return this.hldNmFdCnUvd;
  }

  /**
   * <p>Setter for UVD hldNmFdCn.</p>
   * @param pHldNmFdCn UVD reference
   **/
  public final void setHldNmFdCnUvd(final IHlNmClSt pHldNmFdCn) {
    this.hldNmFdCnUvd = pHldNmFdCn;
  }

  /**
   * <p>Getter for hldFdCls.</p>
   * @return IHlNmClCl
   **/
  public final IHlNmClCl getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHlNmClCl pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  /**
   * <p>Getter for UVD hldFilFdNms.</p>
   * @return UVD IHlNmClSt
   **/
  public final IHlNmClSt getHldFilFdNmsUvd() {
    return this.hldFilFdNmsUvd;
  }

  /**
   * <p>Setter for UVD hldFilFdNms.</p>
   * @param pHldFilFdNmsUvd reference
   **/
  public final void setHldFilFdNmsUvd(final IHlNmClSt pHldFilFdNmsUvd) {
    this.hldFilFdNmsUvd = pHldFilFdNmsUvd;
  }

  /**
   * <p>Getter for DBCP hldFilFdNms.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldFilFdNmsDbCp() {
    return this.hldFilFdNmsDbCp;
  }

  /**
   * <p>Setter for DBCP hldFilFdNms.</p>
   * @param pHldFilFdNmsDbCp reference
   **/
  public final void setHldFilFdNmsDbCp(final IHlNmClSt pHldFilFdNmsDbCp) {
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

  /**
   * <p>Getter for fcts.</p>
   * @return Set<IFcFlFdSt>
   **/
  public final synchronized Set<IFcFlFdSt> getFcts() {
    return this.fcts;
  }

  /**
   * <p>Setter for fcts.</p>
   * @param pFcts reference
   **/
  public final synchronized void setFcts(final Set<IFcFlFdSt> pFcts) {
    this.fcts = pFcts;
  }
}
