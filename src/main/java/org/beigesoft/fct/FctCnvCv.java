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
import org.beigesoft.mdl.ColVals;
import org.beigesoft.log.ILog;
import org.beigesoft.cnv.IConvNmInto;
import org.beigesoft.cnv.CnvIbnBgdCv;
import org.beigesoft.cnv.CnvIbnBlnCv;
import org.beigesoft.cnv.CnvIbnDblCv;
import org.beigesoft.cnv.CnvIbnDtCv;
import org.beigesoft.cnv.CnvIbnEnmCv;
import org.beigesoft.cnv.CnvIbnFltCv;
import org.beigesoft.cnv.CnvIbnIntCv;
import org.beigesoft.cnv.CnvIbnLngCv;
import org.beigesoft.cnv.CnvIbnStrCv;
import org.beigesoft.srv.ISqlEsc;
/**
 * <p>Factory of converters fields to column values.</p>
 *
 * @author Yury Demidenko
 */
public class FctCnvCv implements IFctNm<IConvNmInto<?, ColVals>> {

  //services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>SQL Escape srv.</p>
   **/
  private ISqlEsc sqlEsc;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, IConvNmInto<?, ColVals>> convrts
    = new HashMap<String, IConvNmInto<?, ColVals>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final IConvNmInto<?, ColVals> laz(final Map<String, Object> pRqVs,
    final String pCnNm) throws Exception {
    IConvNmInto<?, ColVals> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CnvIbnBgdCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnBgdCv();
          } else if (CnvIbnBlnCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnBlnCv();
          } else if (CnvIbnDblCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnDblCv();
          } else if (CnvIbnDtCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnDtCv();
          } else if (CnvIbnEnmCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnEnmCv();
          } else if (CnvIbnFltCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnFltCv();
          } else if (CnvIbnIntCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnIntCv();
          } else if (CnvIbnLngCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnLngCv();
          } else if (CnvIbnStrCv.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnStrCv();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no CNV IN CV: " + pCnNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnStrCv.</p>
   * @return CnvIbnStrCv
   */
  private CnvIbnStrCv crPuCnvIbnStrCv() {
    CnvIbnStrCv rz = new CnvIbnStrCv();
    rz.setSqlEsc(getSqlEsc());
    this.convrts.put(CnvIbnStrCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnStrCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnLngCv.</p>
   * @return CnvIbnLngCv
   */
  private CnvIbnLngCv crPuCnvIbnLngCv() {
    CnvIbnLngCv rz = new CnvIbnLngCv();
    this.convrts.put(CnvIbnLngCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnLngCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnIntCv.</p>
   * @return CnvIbnIntCv
   */
  private CnvIbnIntCv crPuCnvIbnIntCv() {
    CnvIbnIntCv rz = new CnvIbnIntCv();
    this.convrts.put(CnvIbnIntCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnIntCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnFltCv.</p>
   * @return CnvIbnFltCv
   */
  private CnvIbnFltCv crPuCnvIbnFltCv() {
    CnvIbnFltCv rz = new CnvIbnFltCv();
    this.convrts.put(CnvIbnFltCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnFltCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnEnmCv.</p>
   * @return CnvIbnEnmCv
   */
  private CnvIbnEnmCv crPuCnvIbnEnmCv() {
    CnvIbnEnmCv rz = new CnvIbnEnmCv();
    this.convrts.put(CnvIbnEnmCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnEnmCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnDtCv.</p>
   * @return CnvIbnDtCv
   */
  private CnvIbnDtCv crPuCnvIbnDtCv() {
    CnvIbnDtCv rz = new CnvIbnDtCv();
    this.convrts.put(CnvIbnDtCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnDtCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnDblCv.</p>
   * @return CnvIbnDblCv
   */
  private CnvIbnDblCv crPuCnvIbnDblCv() {
    CnvIbnDblCv rz = new CnvIbnDblCv();
    this.convrts.put(CnvIbnDblCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnDblCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnBlnCv.</p>
   * @return CnvIbnBlnCv
   */
  private CnvIbnBlnCv crPuCnvIbnBlnCv() {
    CnvIbnBlnCv rz = new CnvIbnBlnCv();
    this.convrts.put(CnvIbnBlnCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnBlnCv.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvIbnBgdCv.</p>
   * @return CnvIbnBgdCv
   */
  private CnvIbnBgdCv crPuCnvIbnBgdCv() {
    CnvIbnBgdCv rz = new CnvIbnBgdCv();
    this.convrts.put(CnvIbnBgdCv.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), CnvIbnBgdCv.class.getSimpleName()
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
   * <p>Getter for sqlEsc.</p>
   * @return ISqlEsc
   **/
  public final ISqlEsc getSqlEsc() {
    return this.sqlEsc;
  }

  /**
   * <p>Setter for sqlEsc.</p>
   * @param pSqlEsc reference
   **/
  public final void setSqlEsc(final ISqlEsc pSqlEsc) {
    this.sqlEsc = pSqlEsc;
  }
}
