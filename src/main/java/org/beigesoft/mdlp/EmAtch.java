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
 * <p>Persistable model of attachment in email.</p>
 *
 * @author Yury Demidenko
 */
public class EmAtch extends AIdLnVrNm implements IOwned<EmMsg> {

  /**
   * <p>Email message.</p>
   **/
  private EmMsg ownr;

  /**
   * <p>Path.</p>
   **/
  private String pth;

  /**
   * <p>Getter for ownr.</p>
   * @return EmMsg
   **/
  @Override
  public final EmMsg getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final EmMsg pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for pth.</p>
   * @return String
   **/
  public final String getPth() {
    return this.pth;
  }

  /**
   * <p>Setter for pth.</p>
   * @param pPth reference
   **/
  public final void setPth(final String pPth) {
    this.pth = pPth;
  }
}
