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

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.prc.IPrc;

/**
 * <p>Simple non-transactional request handler.
 * It delegate request to processor that should handle transaction management
 * if it's need. Transaction management maybe also handled by other
 * handlers in chain or by JEE request filters.</p>
 *
 * @author Yury Demidenko
 */
public class HndNtrRq implements IHndRq {

  /**
   * <p>Processors factory.</p>
   **/
  private IFctNm<IPrc> fctPrc;

  /**
   * <p>Handle request.
   * WHandlerAndJsp requires handle NULL request, so if parameter
   * "nmPrc" is null then do nothing.
   * </p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt) throws Exception {
    String nmPrc = pRqDt.getParam("nmPrc");
    if (nmPrc == null) {
      //WHandlerAndJsp requires handle NULL request:
      return;
    }
    IPrc proc = this.fctPrc.laz(pRqVs, nmPrc);
    proc.process(pRqVs, pRqDt);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctPrc.</p>
   * @return IFctNm<IPrc>
   **/
  public final IFctNm<IPrc> getFctPrc() {
    return this.fctPrc;
  }

  /**
   * <p>Setter for fctPrc.</p>
   * @param pFctPrc reference
   **/
  public final void setFctPrc(
    final IFctNm<IPrc> pFctPrc) {
    this.fctPrc = pFctPrc;
  }
}
