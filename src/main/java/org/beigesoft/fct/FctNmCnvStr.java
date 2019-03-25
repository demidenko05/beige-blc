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

package org.beigesoft.fct;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.cnv.IConv;
import org.beigesoft.cnv.CnvSmpStr;
import org.beigesoft.cnv.CnvBlnStr;

/**
 * <p>Factory of fields converters to string.</p>
 *
 * @author Yury Demidenko
 */
public class FctNmCnvStr implements IFctNm<IConv<?, String>> {

  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, IConv<?, String>> convrts
    = new HashMap<String, IConv<?, String>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  public final IConv<?, String> laz(final Map<String, Object> pRqVs,
    final String pCnNm) throws Exception {
    IConv<?, String> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null) {
          if (CnvSmpStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvSmpStr();
          } else if (CnvBlnStr.class.getSimpleName().equals(pCnNm)) {
            rz = crPuCnvBlnStr();
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no CNV STR: " + pCnNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvBlnStr.</p>
   * @return CnvBlnStr
   */
  private CnvBlnStr crPuCnvBlnStr() {
    CnvBlnStr rz = new CnvBlnStr();
    this.convrts.put(CnvBlnStr.class.getSimpleName(), rz);
    return rz;
  }

  /**
   * <p>Create and put into the Map CnvSmpStr.</p>
   * @return CnvSmpStr
   */
  private CnvSmpStr crPuCnvSmpStr() {
    CnvSmpStr rz = new CnvSmpStr();
    this.convrts.put(CnvSmpStr.class.getSimpleName(), rz);
    return rz;
  }
}
