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

package org.beigesoft.exc;

/**
 * <p>Generic exception with code that
 * useful in servlet to propagate by sendError(int code).</p>
 *
 * @author Yury Demidenko
 */
public class ExcCode extends Exception {

  /**
   * <p>Forbidden.</p>
   **/
  public static final int FORB = 403;

  /**
   * <p>Not yet implemented.</p>
   **/
  public static final int NYI = 1000;

  /**
   * <p>Something goes wrong.</p>
   **/
  public static final int WR = 1001;

  /**
   * <p>Configuration mistake.</p>
   **/
  public static final int WRCN = 1002;

  /**
   * <p>Unexpectable parameter.</p>
   **/
  public static final int WRPR = 1003;

  /**
   * <p>Wrong assemble.</p>
   **/
  public static final int WRASM = 1004;

  /**
   * <p>System is busy, e.g. when factory application beans has been changed
   * during runtime, then it lock factory for clients.</p>
   **/
  public static final int BUSY = 1005;

  /**
   * <p>Code.</p>
   **/
  private int code;

  /**
   * <p>Constructor default.</p>
   **/
  public ExcCode() {
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   **/
  public ExcCode(final int pCode) {
    this.code = pCode;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pCause parent exception
   **/
  public ExcCode(final int pCode, final Throwable pCause) {
    super(pCause);
    this.code = pCode;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pMsg message
   **/
  public ExcCode(final int pCode, final String pMsg) {
    super(pMsg);
    this.code = pCode;
  }

  /**
   * <p>Constructor useful.</p>
   * @param pCode Code
   * @param pMsg message
   * @param pCause parent exception
   **/
  public ExcCode(final int pCode, final String pMsg,
    final Throwable pCause) {
    super(pMsg, pCause);
    this.code = pCode;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for code.</p>
   * @return int
   **/
  public final int getCode() {
    return this.code;
  }

  /**
   * <p>Setter for code.</p>
   * @param pCode reference
   **/
  public final void setCode(final int pCode) {
    this.code = pCode;
  }
}
