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

package org.beigesoft.prc;

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hnd.IHndCh;

/**
 * <p>Service refreshes the first I18N handler.</p>
 *
 * @author Yury Demidenko
 */
public class RefrI18n implements IPrc {

  /**
   * <p>RDB service.</p>
   **/
  private IHndCh hndI18nRq;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    this.hndI18nRq.hndChange();
    pRqDt.setAttr("rnd", "rin");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for hndI18nRq.</p>
   * @return IHndCh
   **/
  public final IHndCh getHndI18nRq() {
    return this.hndI18nRq;
  }

  /**
   * <p>Setter for hndI18nRq.</p>
   * @param pHndI18nRq reference
   **/
  public final void setHndI18nRq(final IHndCh pHndI18nRq) {
    this.hndI18nRq = pHndI18nRq;
  }
}
