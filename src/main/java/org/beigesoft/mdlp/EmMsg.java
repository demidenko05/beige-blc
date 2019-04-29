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
 * <p>Persistable model of message to email.</p>
 *
 * @author Yury Demidenko
 */
public class EmMsg extends AOrId {

  /**
   * <p>Subject.</p>
   **/
  private String subj;

  /**
   * <p>Text.</p>
   **/
  private String txt;

  /**
   * <p>Email connection data.</p>
   **/
  private EmCon emCn;

  /**
   * <p>If sent.</p>
   **/
  private Boolean sent = false;

  /**
   * <p>Recipients emails.</p>
   **/
  private List<EmRcp> rcps;

  /**
   * <p>Attachments.</p>
   **/
  private List<EmAtch> atchs;

  //Simple getters and setters:
  /**
   * <p>Getter for subj.</p>
   * @return String
   **/
  public final String getSubj() {
    return this.subj;
  }

  /**
   * <p>Setter for subj.</p>
   * @param pSubj reference
   **/
  public final void setSubj(final String pSubj) {
    this.subj = pSubj;
  }

  /**
   * <p>Getter for txt.</p>
   * @return String
   **/
  public final String getTxt() {
    return this.txt;
  }

  /**
   * <p>Setter for txt.</p>
   * @param pTxt reference
   **/
  public final void setTxt(final String pTxt) {
    this.txt = pTxt;
  }

  /**
   * <p>Getter for emCn.</p>
   * @return EmCon
   **/
  public final EmCon getEmCn() {
    return this.emCn;
  }

  /**
   * <p>Setter for emCn.</p>
   * @param pEmCn reference
   **/
  public final void setEmCn(final EmCon pEmCn) {
    this.emCn = pEmCn;
  }

  /**
   * <p>Getter for sent.</p>
   * @return Boolean
   **/
  public final Boolean getSent() {
    return this.sent;
  }

  /**
   * <p>Setter for sent.</p>
   * @param pSent reference
   **/
  public final void setSent(final Boolean pSent) {
    this.sent = pSent;
  }

  /**
   * <p>Getter for rcps.</p>
   * @return List<EmRcp>
   **/
  public final List<EmRcp> getRcps() {
    return this.rcps;
  }

  /**
   * <p>Setter for rcps.</p>
   * @param pRcps reference
   **/
  public final void setRcps(final List<EmRcp> pRcps) {
    this.rcps = pRcps;
  }

  /**
   * <p>Getter for atchs.</p>
   * @return List<EmAtch>
   **/
  public final List<EmAtch> getAtchs() {
    return this.atchs;
  }

  /**
   * <p>Setter for atchs.</p>
   * @param pAtchs reference
   **/
  public final void setAtchs(final List<EmAtch> pAtchs) {
    this.atchs = pAtchs;
  }
}
