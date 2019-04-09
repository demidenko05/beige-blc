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

package org.beigesoft.rdb;

import java.util.Map;

/**
 * <p>Abstraction of service that generates DML/DDL statements(queries).</p>
 *
 * @author Yury Demidenko
 */
public interface ISqlQu {

  /**
   * <p>Definition key.</p>
   **/
  String DEF = "def";

  /**
   * <p>Nullable key.</p>
   **/
  String NUL = "nul";

  /**
   * <p>Addition constraint key.</p>
   **/
  String CNSTR = "cnstr";

  /**
   * <p>Generates DDL Create statement for given entity.</p>
   * @param <T> object (entity) type
   * @param pRqVs request scoped vars
   * @param pCls entity class, not null
   * @return Select query in String Buffer
   * @throws Exception - an exception
   **/
  <T> String evCreate(Map<String, Object> pRqVs,
    Class<T> pCls) throws Exception;

  /**
   * <p>Generates DML Select statement for given entity and vars.</p>
   * @param <T> object (entity) type
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, nullable.
   * @param pCls entity class, not null
   * @return Select query in String Buffer
   * @throws Exception - an exception
   **/
  <T> StringBuffer evSel(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls) throws Exception;

  /**
   * <p>Generates condition ID for given entity and appends into given
   * String Buffer.</p>
   * @param <T> object (entity) type
   * @param pRqVs request scoped vars
   * @param pEnt entity, not null
   * @param pSb String Buffer to put ID condition e.g. "[TBL].IID=2"
   *   or "[TBL].WHOUS=1 and [TBL].ITM=2 and [TBL].UOM=5"
   * @throws Exception - an exception
   **/
  <T> void evCndId(Map<String, Object> pRqVs,
    T pEnt, StringBuffer pSb) throws Exception;
}
