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

/**
 * <p>Composite ID for UserRole for Tomcat standard JDBC autentification.</p>
 *
 * @author Yury Demidenko
 */
public class UsRlTmcId {

  /**
   * <p>User.</p>
   **/
  private UsTmc usr;

  /**
   * <p>User's role.</p>
   **/
  private String rol;

  /**
   * <p>Default constructor.</p>
   **/
  public UsRlTmcId() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pItsUser user
   * @param pRol role
   **/
  public UsRlTmcId(final UsTmc pItsUser, final String pRol) {
    this.usr = pItsUser;
    this.rol = pRol;
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
   * <p>Setter for usr.</p>
   * @param pUsr reference
   **/
  public final void setUsr(final UsTmc pUsr) {
    this.usr = pUsr;
  }

  /**
   * <p>Getter for rol.</p>
   * @return String
   **/
  public final String getRol() {
    return this.rol;
  }

  /**
   * <p>Setter for rol.</p>
   * @param pRol reference
   **/
  public final void setRol(final String pRol) {
    this.rol = pRol;
  }
}
