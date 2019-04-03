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

/**
 * <p>Service that synchronizes just read foreign entity with home one.
 * It usually just returns if foreign entity is new (there is not in home).
 * It's for exactly replication purpose. It's no need for identical copy.
 * For IOrId it must fills properly {iid, idOr and dbOr}. For IHasVr
 * it must fills home version for existing (synchronized) entity.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public interface IRpEntSync<T> {

  /**
   * <p>Setting name of service that sync entity.</p>
   **/
  String RPENTSYNCNM = "entSy";

  /**
   * <p>Synchronizes just read foreign entity with home one.</p>
   * @param pRqVs request scoped vars
   * @param pEnt object
   * @return if entity exists in database (needs to update)
   * @throws Exception - an exception
   **/
  boolean sync(Map<String, Object> pRqVs, T pEnt) throws Exception;
}
