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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.AHasVr;

/**
 * <p>Model of user basic preferences.</p>
 *
 * @author Yury Demidenko
 */
public class UsPrf extends AHasVr<UsPrfId> implements IHasId<UsPrfId> {

  /**
   * <p>ID.</p>
   **/
  private UsPrfId iid = new UsPrfId();

  /**
   * <p>Country, composite ID.</p>
   **/
  private Cntr cntr;

  /**
   * <p>Lnguage, composite ID.</p>
   **/
  private Lng lng;

  /**
   * <p>Decimal Separator.</p>
   **/
  private DcSp dcSp;

  /**
   * <p>Decimal Group Separator.</p>
   **/
  private DcGrSp dcGrSp;

  /**
   * <p>Digits in group e.g. 2 for "1 21 23,90", 3 default.</p>
   **/
  private Integer dgInGr = 3;

  /**
   * <p>Is default.</p>
   **/
  private Boolean def;

  /**
   * <p>Getter for iid.</p>
   * @return UsPrfId
   **/
  @Override
  public final UsPrfId getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  @Override
  public final void setIid(final UsPrfId pIid) {
    this.iid = pIid;
    if (this.iid == null) {
      this.lng = null;
      this.cntr = null;
    } else {
      this.lng = this.iid.getLng();
      this.cntr = this.iid.getCntr();
    }
  }

  //Customized setters:
  /**
   * <p>Setter for cntr.</p>
   * @param pCntr reference
   **/
  public final void setCntr(final Cntr pCntr) {
    this.cntr = pCntr;
    if (this.iid == null) {
      this.iid = new UsPrfId();
    }
    this.iid.setCntr(this.cntr);
  }

  /**
   * <p>Setter for lng.</p>
   * @param pLng reference
   **/
  public final void setLng(final Lng pLng) {
    this.lng = pLng;
    if (this.iid == null) {
      this.iid = new UsPrfId();
    }
    this.iid.setLng(this.lng);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cntr.</p>
   * @return Cntr
   **/
  public final Cntr getCntr() {
    return this.cntr;
  }

  /**
   * <p>Getter for lng.</p>
   * @return Lng
   **/
  public final Lng getLng() {
    return this.lng;
  }

  /**
   * <p>Getter for dcSp.</p>
   * @return DcSp
   **/
  public final DcSp getDcSp() {
    return this.dcSp;
  }

  /**
   * <p>Setter for dcSp.</p>
   * @param pDcSp reference
   **/
  public final void setDcSp(final DcSp pDcSp) {
    this.dcSp = pDcSp;
  }

  /**
   * <p>Getter for dcGrSp.</p>
   * @return DcGrSp
   **/
  public final DcGrSp getDcGrSp() {
    return this.dcGrSp;
  }

  /**
   * <p>Setter for dcGrSp.</p>
   * @param pDcGrSp reference
   **/
  public final void setDcGrSp(final DcGrSp pDcGrSp) {
    this.dcGrSp = pDcGrSp;
  }

  /**
   * <p>Getter for dgInGr.</p>
   * @return Integer
   **/
  public final Integer getDgInGr() {
    return this.dgInGr;
  }

  /**
   * <p>Setter for dgInGr.</p>
   * @param pDgInGr reference
   **/
  public final void setDgInGr(final Integer pDgInGr) {
    this.dgInGr = pDgInGr;
  }

  /**
   * <p>Getter for def.</p>
   * @return Boolean
   **/
  public final Boolean getDef() {
    return this.def;
  }

  /**
   * <p>Setter for def.</p>
   * @param pDef reference
   **/
  public final void setDef(final Boolean pDef) {
    this.def = pDef;
  }
}
