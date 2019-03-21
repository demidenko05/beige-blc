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

/**
 * <p>Model of common (system) preferences.</p>
 *
 * @author Yury Demidenko
 */
public class CmnPrf  {

  /**
   * <p>Cost decimal  places.</p>
   **/
  private Integer costDp = 2;

  /**
   * <p>Price decimal  places.</p>
   **/
  private Integer priDp = 2;

  /**
   * <p>Quantity decimal  places.</p>
   **/
  private Integer quanDp = 2;

  /**
   * <p>Char value of decimal separator value, e.g. '\u00A0' for "SPACE".</p>
   **/
  private char dcSpc;

  /**
   * <p>Char value of decimal group separator value,
   * e.g. '\u00A0' for "SPACE".</p>
   **/
  private char dcGrSpc;

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
   * <p>Getter for dcSpc.</p>
   * @return char
   **/
  public final char getDcSpc() {
    return this.dcSpc;
  }

  /**
   * <p>Setter for dcSpc.</p>
   * @param pDcSpc reference
   **/
  public final void setDcSpc(final char pDcSpc) {
    this.dcSpc = pDcSpc;
  }

  /**
   * <p>Getter for dcGrSpc.</p>
   * @return char
   **/
  public final char getDcGrSpc() {
    return this.dcGrSpc;
  }

  /**
   * <p>Setter for dcGrSpc.</p>
   * @param pDcGrSpc reference
   **/
  public final void setDcGrSpc(final char pDcGrSpc) {
    this.dcGrSpc = pDcGrSpc;
  }
}
