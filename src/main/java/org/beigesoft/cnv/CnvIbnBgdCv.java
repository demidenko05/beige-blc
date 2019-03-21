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
import java.util.HashMap;
import java.math.BigDecimal;

import org.beigesoft.mdl.ColVals;

/**
 * <p>Converter from a BigDecimal type to column values
 * with transformation into Double.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnBgdCv implements IConvNmInto<BigDecimal, ColVals> {

  /**
   * <p>Put BigDecimal object to column values with transformation
   * into Double.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pFrom from a BigDecimal object
   * @param pClVl to column values
   * @param pNm field name
   * @throws Exception - an exception
   **/
  @Override
  public final void conv(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final BigDecimal pFrom,
    final ColVals pClVl, final String pNm) throws Exception {
    Double value;
    if (pFrom == null) {
      value = null;
    } else {
      value = pFrom.doubleValue();
    }
    if (pClVl.getDoubles() == null) {
      pClVl.setDoubles(new HashMap<String, Double>());
    }
    pClVl.getDoubles().put(pNm, value);
  }
}
