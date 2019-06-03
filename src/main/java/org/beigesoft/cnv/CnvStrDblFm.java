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

import org.beigesoft.mdl.CmnPrf;

/**
 * <p>Converter of Double from string representation,
 * null represents as "". String value must formatted e.g. "12,34.56".</p>
 *
 * @author Yury Demidenko
 */
public class CnvStrDblFm implements ICnFrSt<Double> {

  /**
   * <p>Converts Double from string.</p>
   * @param pRqVs request scoped vars must has cpf-CmnPrf
   * @param pStrVal string representation
   * @return Double value
   * @throws Exception - an exception
   **/
  @Override
  public final Double conv(final Map<String, Object> pRqVs,
    final String pStrVal) throws Exception {
    if (pStrVal == null || "".equals(pStrVal)) {
      return null;
    }
    String strVal = pStrVal;
    CmnPrf cmnPrf = (CmnPrf) pRqVs.get("cpf");
    if (!"".equals(cmnPrf.getDcGrSpv())) {
      strVal = strVal.replace(cmnPrf.getDcGrSpv(), "");
    }
    if (!"".equals(cmnPrf.getDcSpv()) && !".".equals(cmnPrf.getDcSpv())) {
      strVal = strVal.replace(cmnPrf.getDcSpv(), ".");
    }
    return Double.valueOf(strVal);
  }
}
