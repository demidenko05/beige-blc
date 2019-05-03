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
 * <p>Data to connect to email server.</p>
 *
 * @author Yury Demidenko
 */
public class EmCon extends AOrIdNm {

  /**
   * <p>User email.</p>
   **/
  private String eml;

  /**
   * <p>User password.</p>
   **/
  private String pwd;

  /**
   * <p>String properties e.g. "mail.transport.protocol"-"smtp".</p>
   **/
  private List<EmStr> strPrps;

  /**
   * <p>Integer properties e.g. "mail.smtp.port"-465.</p>
   **/
  private List<EmInt> intPrps;

  //Simple getters and setters:
  /**
   * <p>Getter for eml.</p>
   * @return String
   **/
  public final String getEml() {
    return this.eml;
  }

  /**
   * <p>Setter for eml.</p>
   * @param pEml reference
   **/
  public final void setEml(final String pEml) {
    this.eml = pEml;
  }

  /**
   * <p>Getter for pwd.</p>
   * @return String
   **/
  public final String getPwd() {
    return this.pwd;
  }

  /**
   * <p>Setter for pwd.</p>
   * @param pPwd reference
   **/
  public final void setPwd(final String pPwd) {
    this.pwd = pPwd;
  }

  /**
   * <p>Getter for strPrps.</p>
   * @return List<EmStr>
   **/
  public final List<EmStr> getStrPrps() {
    return this.strPrps;
  }

  /**
   * <p>Setter for strPrps.</p>
   * @param pStrPrps reference
   **/
  public final void setStrPrps(final List<EmStr> pStrPrps) {
    this.strPrps = pStrPrps;
  }

  /**
   * <p>Getter for intPrps.</p>
   * @return List<EmInt>
   **/
  public final List<EmInt> getIntPrps() {
    return this.intPrps;
  }

  /**
   * <p>Setter for intPrps.</p>
   * @param pIntPrps reference
   **/
  public final void setIntPrps(final List<EmInt> pIntPrps) {
    this.intPrps = pIntPrps;
  }
}
