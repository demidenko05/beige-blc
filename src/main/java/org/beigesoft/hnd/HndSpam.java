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

package org.beigesoft.hnd;

import java.util.Map;

import org.beigesoft.log.ILog;
import org.beigesoft.mdl.IReqDt;

/**
 * <p>Spam/swindler request base handler.
 * Any service that reveal/suspect spam can invokes this.</p>
 *
 * @author Yury Demidenko
 */
public class HndSpam implements IHndSpam {

  /**
   * <p>Logger security.</p>
   **/
  private ILog secLog;

  /**
   * <p>Handle suspected spam request.</p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @param pDang danger, e.g. 1 - wrong parameters,
   * 100 invasion attempt into authorized session
   * @param pMsg message maybe null
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt, final int pDang,
      final String pMsg) throws Exception  {
    String msg = "Spam request from host/addr/port/user/danger: " + pRqDt
      .getRemHost() + "/" + pRqDt.getRemAddr() + "/" + pRqDt
        .getRemPort() + "/" + pRqDt.getRemUsr() + "/" + pDang + ". ";
    if (pMsg != null) {
      msg += pMsg;
    }
    if (pDang < 10) {
      this.secLog.warn(pRqVs, HndSpam.class, msg);
    } else {
      this.secLog.error(pRqVs, HndSpam.class, msg);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for secLog.</p>
   * @return ILog
   **/
  public final ILog getSecLog() {
    return this.secLog;
  }

  /**
   * <p>Setter for secLog.</p>
   * @param pSecLog reference
   **/
  public final void setSecLog(final ILog pSecLog) {
    this.secLog = pSecLog;
  }
}
