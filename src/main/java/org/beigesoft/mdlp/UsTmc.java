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

import org.beigesoft.mdl.AEditable;
import org.beigesoft.mdl.IIdStr;

/**
 * <p>Model User for Tomcat standard JDBC authentication.</p>
 *
 * @author Yury Demidenko
 */
public class UsTmc extends AEditable implements IIdStr {

  /**
   * <p>User's name/id.
   * FIELD MUST BE SAME NAME usr AS IN USERROLETOMCAT!
   * And so in database.</p>
   **/
  private String usr;

  /**
   * <p>User's password.</p>
   **/
  private String pwd;

  /**
   * <p>Version to check dirty or replication.</p>
   **/
  private Long ver;

  /**
   * <p>Geter for ver.</p>
   * @return Long
   **/
  @Override
  public final Long getVer() {
    return this.ver;
  }

  /**
   * <p>Setter for ver.</p>
   * @param pVer reference
   **/
  @Override
  public final void setVer(final Long pVer) {
    this.ver = pVer;
  }

  /**
   * <p>Geter for iid.</p>
   * @return UsRlTmcId
   **/
  @Override
  public final String getIid() {
    return this.usr;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference/value
   **/
  @Override
  public final void setIid(final String pIid) {
    this.usr = pIid;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for usr.</p>
   * @return String
   **/
  public final String getUsr() {
    return this.usr;
  }

  /**
   * <p>Setter for usr.</p>
   * @param pUsr reference
   **/
  public final void setUsr(final String pUsr) {
    this.usr = pUsr;
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
}
