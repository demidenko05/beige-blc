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
import org.beigesoft.cnv.IFilCvFdv;
import org.beigesoft.cnv.FilCvBgd;
import org.beigesoft.cnv.FilCvBln;
import org.beigesoft.cnv.FilCvDbl;
import org.beigesoft.cnv.FilCvDt;
import org.beigesoft.cnv.FilCvEnm;
import org.beigesoft.cnv.FilCvFlt;
import org.beigesoft.cnv.FilCvInt;
import org.beigesoft.cnv.FilCvLng;
import org.beigesoft.cnv.FilCvStr;
import org.beigesoft.srv.ISqlEsc;
/**
 * <p>Factory of converters fields to column values.</p>
 *
 * @author Yury Demidenko
 */
public class FctCnvCv implements IFcFlCvFdv {

  //services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>SQL Escape srv.</p>
   **/
  private ISqlEsc sqlEsc;

  /**
   * <p>If need to SQL escape for value string.
   * Android do it itself.</p>
   **/
  private boolean isSqlEsc = true;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, IFilCvFdv<?>> convrts
    = new HashMap<String, IFilCvFdv<?>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final IFilCvFdv<?> laz(final Map<String, Object> pRvs,
    final String pCnNm) throws Exception {
    IFilCvFdv<?> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (FilCvBgd.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnBgdCv();
          } else if (FilCvBln.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnBlnCv();
          } else if (FilCvDbl.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnDblCv();
          } else if (FilCvDt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnDtCv();
          } else if (FilCvEnm.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnEnmCv();
          } else if (FilCvFlt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnFltCv();
          } else if (FilCvInt.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnIntCv();
          } else if (FilCvLng.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvIbnLngCv();
          } else if (FilCvStr.class.getSimpleName().equals(pCnNm)) {
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
   * <p>Create and put into the Map FilCvStr.</p>
   * @return FilCvStr
   */
  private FilCvStr crPuCnvIbnStrCv() {
    FilCvStr rz = new FilCvStr();
    rz.setSqlEsc(getSqlEsc());
    rz.setIsSqlEsc(getIsSqlEsc());
    this.convrts.put(FilCvStr.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvStr.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvLng.</p>
   * @return FilCvLng
   */
  private FilCvLng crPuCnvIbnLngCv() {
    FilCvLng rz = new FilCvLng();
    this.convrts.put(FilCvLng.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvLng.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvInt.</p>
   * @return FilCvInt
   */
  private FilCvInt crPuCnvIbnIntCv() {
    FilCvInt rz = new FilCvInt();
    this.convrts.put(FilCvInt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvInt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvFlt.</p>
   * @return FilCvFlt
   */
  private FilCvFlt crPuCnvIbnFltCv() {
    FilCvFlt rz = new FilCvFlt();
    this.convrts.put(FilCvFlt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvFlt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvEnm.</p>
   * @return FilCvEnm
   */
  private FilCvEnm crPuCnvIbnEnmCv() {
    FilCvEnm rz = new FilCvEnm();
    this.convrts.put(FilCvEnm.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvEnm.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvDt.</p>
   * @return FilCvDt
   */
  private FilCvDt crPuCnvIbnDtCv() {
    FilCvDt rz = new FilCvDt();
    this.convrts.put(FilCvDt.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvDt.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvDbl.</p>
   * @return FilCvDbl
   */
  private FilCvDbl crPuCnvIbnDblCv() {
    FilCvDbl rz = new FilCvDbl();
    this.convrts.put(FilCvDbl.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvDbl.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvBln.</p>
   * @return FilCvBln
   */
  private FilCvBln crPuCnvIbnBlnCv() {
    FilCvBln rz = new FilCvBln();
    this.convrts.put(FilCvBln.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvBln.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map FilCvBgd.</p>
   * @return FilCvBgd
   */
  private FilCvBgd crPuCnvIbnBgdCv() {
    FilCvBgd rz = new FilCvBgd();
    this.convrts.put(FilCvBgd.class.getSimpleName(), rz);
    getLogStd().info(null, getClass(), FilCvBgd.class.getSimpleName()
      + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for isSqlEsc.</p>
   * @return boolean
   **/
  public final boolean getIsSqlEsc() {
    return this.isSqlEsc;
  }

  /**
   * <p>Setter for isSqlEsc.</p>
   * @param pIsSqlEsc reference
   **/
  public final void setIsSqlEsc(final boolean pIsSqlEsc) {
    this.isSqlEsc = pIsSqlEsc;
  }

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
