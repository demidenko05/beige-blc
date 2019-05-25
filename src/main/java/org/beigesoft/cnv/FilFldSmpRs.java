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

package org.beigesoft.cnv;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.hld.IHlNmClSt;

/**
 * <p>Standard service that fills/converts object's field of simple type from
 * given DB result-set. Simple types - Integer, Long,
 * BigDecimal, etc.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FilFldSmpRs<RS> implements IFilFld<IRecSet<RS>> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Fields converters names holder.</p>
   **/
  private IHlNmClSt hldNmFdCn;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFctNm<IConvNm<IRecSet<RS>, ?>> fctCnvFld;

  /**
   * <p>Fills object's field.</p>
   * @param <T> object type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pObj Object to fill, not null
   * @param pRs Source record-set with field value
   * @param pFdNm Field name
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pObj,
    final IRecSet<RS> pRs, final String pFdNm) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7003 && this.log.getDbgCl() > 7001;
    Object val = null;
    String cnNm = this.hldNmFdCn.get(pObj.getClass(), pFdNm);
    @SuppressWarnings("unchecked")
    IConvNm<IRecSet<RS>, Object> flCnv =
      (IConvNm<IRecSet<RS>, Object>) this.fctCnvFld.laz(pRqVs, cnNm);
    String clNm;
    @SuppressWarnings("unchecked")
    List<String> tbAls = (List<String>) pVs.get("tbAls");
    if (tbAls.size() > 0) {
      clNm = tbAls.get(tbAls.size() - 1) + pFdNm.toUpperCase();
    } else {
      clNm = pFdNm.toUpperCase();
    }
    if (isDbgSh) {
      this.log.debug(pRqVs, FilFldSmpRs.class, "Column alias/cls: "
        + clNm + "/" + pObj.getClass());
    }
    val = flCnv.conv(pRqVs, pVs, pRs, clNm);
    Method setr = this.hldSets.get(pObj.getClass(), pFdNm);
    setr.invoke(pObj, val);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
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
   * @return IFctCls<IConvNm<IRecSet<RS>, Object>
   **/
  public final IFctNm<IConvNm<IRecSet<RS>, ?>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IConvNm<IRecSet<RS>, ?>> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }

  /**
   * <p>Getter for hldNmFdCn.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldNmFdCn() {
    return this.hldNmFdCn;
  }

  /**
   * <p>Setter for hldNmFdCn.</p>
   * @param pHldNmFdCn reference
   **/
  public final void setHldNmFdCn(final IHlNmClSt pHldNmFdCn) {
    this.hldNmFdCn = pHldNmFdCn;
  }
}
