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

import org.beigesoft.mdl.IOwned;

/**
 * <p>String properties to connect to email server
 * e.g. "mail.transport.protocol"-"smtp".</p>
 *
 * @author Yury Demidenko
 */
public class EmStr extends AIdLn implements IOwned<EmCon, Long> {

  /**
   * <p>Email connection.</p>
   **/
  private EmCon ownr;

  /**
   * <p>Property name.</p>
   **/
  private String prNm;

  /**
   * <p>Property value.</p>
   **/
  private String prVl;

  /**
   * <p>Getter for ownr.</p>
   * @return EmCon
   **/
  @Override
  public final EmCon getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final EmCon pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for prNm.</p>
   * @return String
   **/
  public final String getPrNm() {
    return this.prNm;
  }

  /**
   * <p>Setter for prNm.</p>
   * @param pPrNm reference
   **/
  public final void setPrNm(final String pPrNm) {
    this.prNm = pPrNm;
  }

  /**
   * <p>Getter for prVl.</p>
   * @return String
   **/
  public final String getPrVl() {
    return this.prVl;
  }

  /**
   * <p>Setter for prVl.</p>
   * @param pPrVl reference
   **/
  public final void setPrVl(final String pPrVl) {
    this.prVl = pPrVl;
  }
}
