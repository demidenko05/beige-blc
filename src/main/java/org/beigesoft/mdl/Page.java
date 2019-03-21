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
 * <p>Page model for pagination.</p>
 *
 * @author Yury Demidenko
 */
public class Page {

  /**
   * <p>Page number.</p>
   **/
  private String val;

  /**
   * <p>is page current.</p>
   **/
  private boolean cur;

  /**
   * <p>Default constructor.</p>
   **/
  public Page() {
  }

  /**
   * <p>Useful constructor.</p>
   * @param pVal page #
   * @param pCur Is current?
   **/
  public Page(final String pVal, final boolean pCur) {
    this.val = pVal;
    this.cur = pCur;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for val.</p>
   * @return String
   **/
  public final String getVal() {
    return this.val;
  }

  /**
   * <p>Setter for val.</p>
   * @param pVal reference
   **/
  public final void setVal(final String pVal) {
    this.val = pVal;
  }

  /**
   * <p>Geter for cur.</p>
   * @return boolean
   **/
  public final boolean getCur() {
    return this.cur;
  }

  /**
   * <p>Setter for cur.</p>
   * @param pCur reference
   **/
  public final void setCur(final boolean pCur) {
    this.cur = pCur;
  }
}
