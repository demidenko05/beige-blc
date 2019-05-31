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

package org.beigesoft.cnv;

import java.util.Map;
import java.util.Date;

import org.beigesoft.srv.ISrvDt;

/**
 * <p>Converter date from string ISO8601 with time and seconds,
 * e.g. "2001-07-04T21:55:34".</p>
 *
 * @author Yury Demidenko
 */
public class CnvStrDtSc implements IConv<String, Date> {

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Convert Date from string.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pStrVal string value
   * @return date value
   * @throws Exception - an exception
   **/
  @Override
  public final Date conv(final Map<String, Object> pRqVs,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    return this.srvDt.from8601DateTimeSec(pStrVal);
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }
}
