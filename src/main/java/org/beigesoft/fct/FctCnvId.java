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
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;
import org.beigesoft.cnv.ICnvId;
import org.beigesoft.cnv.CnvIdLn;
import org.beigesoft.cnv.CnvIdStr;
import org.beigesoft.cnv.CnvIdCst;
import org.beigesoft.prp.ISetng;

/**
 * <p>Factory of ID converters.</p>
 *
 * @author Yury Demidenko
 */
public class FctCnvId implements IFctCnvId {

  //services:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //parts:
  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, ICnvId<?, ?>> converters
    = new HashMap<String, ICnvId<?, ?>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pFiNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final ICnvId<?, ?> laz(final Map<String, Object> pRvs,
    final String pFiNm) throws Exception {
    ICnvId<?, ?> rz = this.converters.get(pFiNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.converters.get(pFiNm);
        if (rz == null) {
          if (CnvIdLn.class.getSimpleName().equals(pFiNm)) {
            rz = crPuCnvIdLn();
          } else if (CnvIdStr.class.getSimpleName().equals(pFiNm)) {
            rz = crPuCnvIdStr();
          } else if (CnvIdCst.class.getSimpleName().equals(pFiNm)) {
            rz = crPuCnvIdCst();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no ICnvId: " + pFiNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIdLn.</p>
   * @return CnvIdLn
   */
  private CnvIdLn crPuCnvIdLn() {
    CnvIdLn rz = new CnvIdLn();
    this.converters.put(CnvIdLn.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIdLn.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIdCst.</p>
   * @return CnvIdCst
   */
  private CnvIdCst crPuCnvIdCst() {
    CnvIdCst rz = new CnvIdCst();
    rz.setHldGets(getHldGets());
    rz.setHldFdCls(getHldFdCls());
    rz.setSetng(getSetng());
    this.converters.put(CnvIdCst.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIdCst.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIdStr.</p>
   * @return CnvIdStr
   */
  private CnvIdStr crPuCnvIdStr() {
    CnvIdStr rz = new CnvIdStr();
    this.converters.put(CnvIdStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIdStr.class.getSimpleName()
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
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
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
}
