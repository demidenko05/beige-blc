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

package org.beigesoft.mdlp;

import org.beigesoft.mdl.ECsvClTy;
import org.beigesoft.mdl.IOwned;

/**
 * <p>Persistable model of column CSV method, i.e. how to import/export
 * data from/to column, common rules.</p>
 *
 * @author Yury Demidenko
 */
public class CsvCl extends AOrIdNm implements IOwned<CsvMth, Long> {

  /**
   * <p>CSV method.</p>
   **/
  private CsvMth ownr;

  /**
   * <p>Column data type.</p>
   **/
  private ECsvClTy typ;

  /**
   * <p>Column index, from 1.</p>
   **/
  private Integer indx;

  /**
   * <p>Column index, from 1 in source file
   * cause some columns might be omitted.</p>
   **/
  private Integer srIdx;

  /**
   * <p>Column's text braced with delimiters, null default,
   * e.g. quoted - "taxes, penalty and other fees are included".</p>
   **/
  private String txDlm = null;

  /**
   * <p>Column's data format, null default, e.g. "dd/MM/yyyy" for Date,
   * "space,comma" for number "123 31,78EUR", "true,false" for Boolean.</p>
   **/
  private String frmt = null;

  //rules to write:
  /**
   * <p>If used, constant for default value for all rows,
   * e.g. "1" for column "isTaxIncludedInPrice".</p>
   **/
  private String cnst;

  /**
   * <p>If used, index of data column, starts from 1.</p>
   **/
  private Integer dtIdx;

  /**
   * <p>If used, comma separated fields names
   * trough destination field, e.g.
   * "itsCategory,iid"
   * to retrieve product.getItsCategory().getIid() from column
   * with given dataIndex.</p>
   **/
  private String fldPh;

  /**
   * <p>If used, match foreign method.</p>
   **/
  private MaFrn maFrn;

  /**
   * <p>Getter for ownr.</p>
   * @return CsvMth
   **/
  @Override
  public final CsvMth getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final CsvMth pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for typ.</p>
   * @return ECsvClTy
   **/
  public final ECsvClTy getTyp() {
    return this.typ;
  }

  /**
   * <p>Setter for typ.</p>
   * @param pTyp reference
   **/
  public final void setTyp(final ECsvClTy pTyp) {
    this.typ = pTyp;
  }

  /**
   * <p>Getter for indx.</p>
   * @return Integer
   **/
  public final Integer getIndx() {
    return this.indx;
  }

  /**
   * <p>Setter for indx.</p>
   * @param pIndx reference
   **/
  public final void setIndx(final Integer pIndx) {
    this.indx = pIndx;
  }

  /**
   * <p>Getter for srIdx.</p>
   * @return Integer
   **/
  public final Integer getSrIdx() {
    return this.srIdx;
  }

  /**
   * <p>Setter for srIdx.</p>
   * @param pSrIdx reference
   **/
  public final void setSrIdx(final Integer pSrIdx) {
    this.srIdx = pSrIdx;
  }

  /**
   * <p>Getter for txDlm.</p>
   * @return String
   **/
  public final String getTxDlm() {
    return this.txDlm;
  }

  /**
   * <p>Setter for txDlm.</p>
   * @param pTxDlm reference
   **/
  public final void setTxDlm(final String pTxDlm) {
    this.txDlm = pTxDlm;
  }

  /**
   * <p>Getter for frmt.</p>
   * @return String
   **/
  public final String getFrmt() {
    return this.frmt;
  }

  /**
   * <p>Setter for frmt.</p>
   * @param pFrmt reference
   **/
  public final void setFrmt(final String pFrmt) {
    this.frmt = pFrmt;
  }

  /**
   * <p>Getter for cnst.</p>
   * @return String
   **/
  public final String getCnst() {
    return this.cnst;
  }

  /**
   * <p>Setter for cnst.</p>
   * @param pCnst reference
   **/
  public final void setCnst(final String pCnst) {
    this.cnst = pCnst;
  }

  /**
   * <p>Getter for dtIdx.</p>
   * @return Integer
   **/
  public final Integer getDtIdx() {
    return this.dtIdx;
  }

  /**
   * <p>Setter for dtIdx.</p>
   * @param pDtIdx reference
   **/
  public final void setDtIdx(final Integer pDtIdx) {
    this.dtIdx = pDtIdx;
  }

  /**
   * <p>Getter for fldPh.</p>
   * @return String
   **/
  public final String getFldPh() {
    return this.fldPh;
  }

  /**
   * <p>Setter for fldPh.</p>
   * @param pFldPh reference
   **/
  public final void setFldPh(final String pFldPh) {
    this.fldPh = pFldPh;
  }

  /**
   * <p>Getter for maFrn.</p>
   * @return MaFrn
   **/
  public final MaFrn getMaFrn() {
    return this.maFrn;
  }

  /**
   * <p>Setter for maFrn.</p>
   * @param pMaFrn reference
   **/
  public final void setMaFrn(final MaFrn pMaFrn) {
    this.maFrn = pMaFrn;
  }
}
