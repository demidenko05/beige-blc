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

import org.beigesoft.mdl.IRecSet;

/**
 * <p>Converts named field from result-set to Enum.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class CnvBnRsEnm<RS> implements IConvNm<IRecSet<RS>, Enum<?>> {

  /**
   * <p>Converts named field from resultset.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, must has "fldCls" - field's class!
   * @param pRs result set
   * @param pNm field name
   * @return pTo to value
   * @throws Exception - an exception
   **/
  @Override
  public final Enum<?> conv(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final IRecSet<RS> pRs,
      final String pNm) throws Exception {
    Integer intVal = pRs.getInt(pNm);
    if (intVal != null) {
      Class fldCls = (Class) pVs.get("fldCls");
      return (Enum) fldCls.getEnumConstants()[intVal];
    }
    return null;
  }
}
