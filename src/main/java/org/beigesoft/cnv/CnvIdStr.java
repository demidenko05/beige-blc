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

import org.beigesoft.mdl.IIdStr;

/**
 * <p>Standard converter of entity of String ID and name IID to ID SQL/HTML
 * ready representation.</p>
 *
 * @author Yury Demidenko
 */
public class CnvIdStr implements ICnvId<IIdStr, String> {

  /**
   * <p>Converts to HTML ready ID, e.g. "IID=PAYB" for Account with String ID,
   * or "usr=User1&rol=Role1" for User-Role with composite ID.</p>
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  @Override
  public final String idHtml(final IIdStr pEnt) throws Exception {
    return pEnt.getClass().getSimpleName() + ".iid=" + pEnt.getIid();
  }

  /**
   * <p>Converts to SQL ready ID, e.g. "'PAYB'" for Account with String ID,
   * or "'User1','Role1'" for User-Role with composite ID.</p>
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  @Override
  public final String idSql(final IIdStr pEnt) throws Exception {
    return "'" + pEnt.getIid() + "'";
  }
}
