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
import java.math.BigDecimal;

import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.srv.INumStr;

/**
 * <p>Converter of a BigDecimal quantity to string representation with digital
 * separators, null represents as "". It requires request scoped
 * digital preferences.</p>
 *
 * @author Yury Demidenko
 */
public class CnvQuanStr implements ICnToSt<BigDecimal> {

  /**
   * <p>Number to string service.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Converts BigDecimal to string.</p>
   * @param pRqVs request scoped vars, must has upf - UsPrf, and cpf - CmnPrf
   * @param pObj BigDecimal quantity
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String conv(final Map<String, Object> pRqVs,
    final BigDecimal pObj) throws Exception {
    if (pObj == null) {
      return "";
    }
    CmnPrf cpf = (CmnPrf) pRqVs.get("cpf");
    UsPrf upf = (UsPrf) pRqVs.get("upf");
    return this.numStr.frmt(pObj.toString(), cpf.getDcSpv(),
      cpf.getDcGrSpv(), cpf.getQuanDp(), upf.getDgInGr());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }
}
