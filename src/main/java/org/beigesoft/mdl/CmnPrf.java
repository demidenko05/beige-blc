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

package org.beigesoft.mdl;

import java.util.Locale;

import org.beigesoft.mdlp.Lng;

/**
 * <p>Model of common (system) preferences, and non-persistent user ones.</p>
 *
 * @author Yury Demidenko
 */
public class CmnPrf  {

  /**
   * <p>Cost decimal places.</p>
   **/
  private Integer costDp = 2;

  /**
   * <p>Price decimal places.</p>
   **/
  private Integer priDp = 2;

  /**
   * <p>Quantity decimal places.</p>
   **/
  private Integer quanDp = 2;

  /**
   * <p>Maximum decimal places, same as in database. It's for properties
   * like currency rate, page margins, etc.</p>
   **/
  private Integer maxDp = 4;

  /**
   * <p>Value of decimal separator, e.g. "\u00A0" for "SPACEID".</p>
   **/
  private String dcSpv;

  /**
   * <p>Value of decimal group separator value,
   * e.g. "\u00A0" for "SPACEID".</p>
   **/
  private String dcGrSpv;

  /**
   * <p>Default language.</p>
   **/
  private Lng lngDef;

  //User preferences:
  /**
   * <p>Preferred/current locale.</p>
   **/
  private Locale usLoc;

  //Simple getters and setters:
  /**
   * <p>Getter for costDp.</p>
   * @return Integer
   **/
  public final Integer getCostDp() {
    return this.costDp;
  }

  /**
   * <p>Setter for costDp.</p>
   * @param pCostDp reference
   **/
  public final void setCostDp(final Integer pCostDp) {
    this.costDp = pCostDp;
  }

  /**
   * <p>Getter for priDp.</p>
   * @return Integer
   **/
  public final Integer getPriDp() {
    return this.priDp;
  }

  /**
   * <p>Setter for priDp.</p>
   * @param pPriDp reference
   **/
  public final void setPriDp(final Integer pPriDp) {
    this.priDp = pPriDp;
  }

  /**
   * <p>Getter for maxDp.</p>
   * @return Integer
   **/
  public final Integer getMaxDp() {
    return this.maxDp;
  }

  /**
   * <p>Setter for maxDp.</p>
   * @param pMaxDp reference
   **/
  public final void setMaxDp(final Integer pMaxDp) {
    this.maxDp = pMaxDp;
  }

  /**
   * <p>Getter for quanDp.</p>
   * @return Integer
   **/
  public final Integer getQuanDp() {
    return this.quanDp;
  }

  /**
   * <p>Setter for quanDp.</p>
   * @param pQuanDp reference
   **/
  public final void setQuanDp(final Integer pQuanDp) {
    this.quanDp = pQuanDp;
  }

  /**
   * <p>Getter for dcSpv.</p>
   * @return String
   **/
  public final String getDcSpv() {
    return this.dcSpv;
  }

  /**
   * <p>Setter for dcSpv.</p>
   * @param pDcSpv reference
   **/
  public final void setDcSpv(final String pDcSpv) {
    this.dcSpv = pDcSpv;
  }

  /**
   * <p>Getter for dcGrSpv.</p>
   * @return String
   **/
  public final String getDcGrSpv() {
    return this.dcGrSpv;
  }

  /**
   * <p>Setter for dcGrSpv.</p>
   * @param pDcGrSpv reference
   **/
  public final void setDcGrSpv(final String pDcGrSpv) {
    this.dcGrSpv = pDcGrSpv;
  }

  /**
   * <p>Getter for lngDef.</p>
   * @return Lng
   **/
  public final Lng getLngDef() {
    return this.lngDef;
  }

  /**
   * <p>Setter for lngDef.</p>
   * @param pLngDef reference
   **/
  public final void setLngDef(final Lng pLngDef) {
    this.lngDef = pLngDef;
  }

  /**
   * <p>Getter for usLoc.</p>
   * @return Locale
   **/
  public final Locale getUsLoc() {
    return this.usLoc;
  }

  /**
   * <p>Setter for usLoc.</p>
   * @param pUsLoc reference
   **/
  public final void setUsLoc(final Locale pUsLoc) {
    this.usLoc = pUsLoc;
  }
}
