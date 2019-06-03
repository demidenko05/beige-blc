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

import org.beigesoft.mdl.ColVals;

/**
 * <p>Fills column values with given value of Boolean type
 * with transformation into Integer.</p>
 *
 * @author Yury Demidenko
 */
public class FilCvBln implements IFilCvFdv<Boolean> {

  /**
   * <p>Puts Boolean object to column values with transformation
   * into Integer.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pFdNm field name
   * @param pFdv field value
   * @param pCv column values
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final String pFdNm, final Boolean pFdv,
      final ColVals pCv) throws Exception {
    Integer value;
    if (pFdv == null || !pFdv) { // Boolean is non-nullable
      value = 0;
    } else {
      value = 1;
    }
    if (pCv.getInts() == null) {
      pCv.setInts(new HashMap<String, Integer>());
    }
    pCv.getInts().put(pFdNm, value);
  }
}
