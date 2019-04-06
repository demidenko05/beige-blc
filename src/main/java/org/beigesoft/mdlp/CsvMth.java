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

import java.util.List;

/**
 * <p>Persistable model of basic CSV method, i.e. how to import/export
 * data from/to CSV file, common rules.</p>
 *
 * @author Yury Demidenko
 */
public class CsvMth extends AIdLnNm {

  /**
   * <p>Charset encoding name, "UTF-8" default.</p>
   **/
  private String chrst = "UTF-8";

  /**
   * <p>Column separator, comma default.</p>
   **/
  private String clSp = ",";

  /**
   * <p>The first row contains of columns names, false default.</p>
   **/
  private Boolean hasHd = false;

  /**
   * <p>If used, name to get ICsvDataRetriever.</p>
   **/
  private String rtrNm;

  /**
   * <p>Columns all or useful to read.</p>
   **/
  private List<CsvCl> clns;

  //Simple getters and setters:
  /**
   * <p>Getter for chrst.</p>
   * @return String
   **/
  public final String getChrst() {
    return this.chrst;
  }

  /**
   * <p>Setter for chrst.</p>
   * @param pChrst reference
   **/
  public final void setChrst(final String pChrst) {
    this.chrst = pChrst;
  }

  /**
   * <p>Getter for clSp.</p>
   * @return String
   **/
  public final String getClSp() {
    return this.clSp;
  }

  /**
   * <p>Setter for clSp.</p>
   * @param pClSp reference
   **/
  public final void setClSp(final String pClSp) {
    this.clSp = pClSp;
  }

  /**
   * <p>Getter for hasHd.</p>
   * @return Boolean
   **/
  public final Boolean getHasHd() {
    return this.hasHd;
  }

  /**
   * <p>Setter for hasHd.</p>
   * @param pHasHd reference
   **/
  public final void setHasHd(final Boolean pHasHd) {
    this.hasHd = pHasHd;
  }

  /**
   * <p>Getter for rtrNm.</p>
   * @return String
   **/
  public final String getRtrNm() {
    return this.rtrNm;
  }

  /**
   * <p>Setter for rtrNm.</p>
   * @param pRtrNm reference
   **/
  public final void setRtrNm(final String pRtrNm) {
    this.rtrNm = pRtrNm;
  }

  /**
   * <p>Getter for clns.</p>
   * @return List<CsvCl>
   **/
  public final List<CsvCl> getClns() {
    return this.clns;
  }

  /**
   * <p>Setter for clns.</p>
   * @param pClns reference
   **/
  public final void setClns(final List<CsvCl> pClns) {
    this.clns = pClns;
  }
}
