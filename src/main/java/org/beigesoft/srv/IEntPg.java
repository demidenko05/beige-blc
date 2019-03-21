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

package org.beigesoft.srv;

import java.util.Map;

import org.beigesoft.mdl.IReqDt;

/**
 * <p>Abstraction of service that retrieve entities page
 * or filter data according request.</p>
 *
 * @author Yury Demidenko
 */
public interface IEntPg {

  /**
   * <p>Retrieve entities page - entities list, pages, filter map etc.</p>
   * @param pRqVs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  void retPg(Map<String, Object> pRqVs, IReqDt pRqDt) throws Exception;

  /**
   * <p>Retrieve page filter data like SQL where and filter map.
   * It's used in other services e.g. PrcAsItsCt.</p>
   * @param pRqVs request scoped vars to return revealed data
   * @param pRqDt - Request Data
   * @param pCls Entity Class
   * @return StringBuffer with empty string "" or one like "ITSID=12"
   * @throws Exception - an Exception
   **/
  StringBuffer revPgFltDt(Map<String, Object> pRqVs,
    IReqDt pRqDt, Class<?> pCls) throws Exception;
}
