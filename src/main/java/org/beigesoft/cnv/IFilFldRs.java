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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IRecSet;

/**
 * <p>Abstraction of service that fills object's field with data from given
 * record-set.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public interface IFilFldRs<RS> {

  /**
   * <p>Fills entity's field with given record-set.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars, not null
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt Entity to fill, not null
   * @param pFlNm Field name, not null
   * @param pRs record-set, not null
   * @return if not-null value
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> boolean fill(Map<String, Object> pRvs,
    Map<String, Object> pVs, T pEnt, String pFlNm,
      IRecSet<RS> pRs) throws Exception;
}
