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
import org.beigesoft.mdl.IHasId;

/**
 * <p>Model User's Role for Tomcat standard JDBC autentification.</p>
 *
 * @author Yury Demidenko
 */
public class UsRlTmc extends AEditable implements IHasId<UsRlTmcId> {

  /**
   * <p>Complex ID.</p>
   **/
  private UsRlTmcId iid;

  /**
   * <p>User.</p>
   **/
  private UsTmc usr;

  /**
   * <p>User's role.</p>
   **/
  private String rol;

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
  public final UsRlTmcId getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference/value
   **/
  @Override
  public final void setIid(final UsRlTmcId pIid) {
    this.iid = pIid;
    if (this.iid == null) {
      this.usr = null;
      this.rol = null;
    } else {
      this.usr = this.iid.getUsr();
      this.rol = this.iid.getRol();
    }
  }

  /**
   * <p>Setter for usr.</p>
   * @param pUsr reference/value
   **/
  public final void setUsr(final UsTmc pUsr) {
    this.usr = pUsr;
    if (this.iid == null) {
      this.iid = new UsRlTmcId();
    }
    this.iid.setUsr(this.usr);
  }

  /**
   * <p>Setter for rol.</p>
   * @param pRol reference
   **/
  public final void setRol(final String pRol) {
    this.rol = pRol;
    if (this.iid == null) {
      this.iid = new UsRlTmcId();
    }
    this.iid.setRol(this.rol);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for usr.</p>
   * @return UsTmc
   **/
  public final UsTmc getUsr() {
    return this.usr;
  }

  /**
   * <p>Getter for rol.</p>
   * @return String
   **/
  public final String getRol() {
    return this.rol;
  }
}
