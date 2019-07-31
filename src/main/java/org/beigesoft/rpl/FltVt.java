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

package org.beigesoft.rpl;

import java.util.Map;

import org.beigesoft.mdl.IHasId;

/**
 * <p>Base filter version time (!) for replication.</p>
 *
 * @author Yury Demidenko
 */
public class FltVt implements IFltEnts {

  /**
   * <p>Makes SQL WHERE filter for given entity.</p>
   * @param pCls Entity Class
   * @param pRvs request scoped vars mast has ARplMth replication method
   * @return filter, e.g. "VER>786786788" or null
   * @throws Exception - an exception
   **/
  @Override
  public final String makeWhe(final Map<String, Object> pRvs,
    final Class<? extends IHasId<?>> pCls) throws Exception {
    ARplMth rplMth = (ARplMth) pRvs.get("ARplMth");
    if (rplMth.getLstDt() != null) {
      return pCls.getSimpleName().toUpperCase() + ".VER>"
        + rplMth.getLstDt().getTime();
    }
    return null;
  }
}
