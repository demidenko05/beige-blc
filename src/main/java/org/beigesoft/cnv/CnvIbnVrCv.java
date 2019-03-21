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
import java.util.Date;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.ColVals;

/**
 * <p>Converter version to column values according version algorithm.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIbnVrCv implements IConvNmInto<Long, ColVals> {

  /**
   * <p>Put version current and old to column values
   * according version algorithm.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pFrom from a Long object
   * @param pClVl to column values
   * @param pNm field name
   * @throws Exception - an exception
   **/
  @Override
  public final void conv(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Long pFrom,
      final ColVals pClVl, final String pNm) throws Exception {
    Integer verAlg = (Integer) pRqVs.get("verAlg");
    if (verAlg == null) {
      throw new ExcCode(ExcCode.WRCN, "Missed parameter verAlg!");
    }
    Long vlNew = null;
    if (verAlg == 1) {
      vlNew = new Date().getTime();
    } else {
      if (pFrom == null) {
        vlNew = 1L;
      } else {
        vlNew = pFrom + 1L;
      }
    }
    if (pClVl.getLongs() == null) {
      pClVl.setLongs(new HashMap<String, Long>());
    }
    pClVl.getLongs().put(pNm, vlNew);
    pClVl.setOldVer(pFrom);
  }
}
