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
import org.beigesoft.srv.ISqlEsc;

/**
 * <p>Converter from a String type to column values
 * with SQL escaping for JDBC.</p>
 *
 * @author Yury Demidenko
 */
public class FilCvStr implements IFilCvFdv<String> {

  /**
   * <p>If need to SQL escape for value string.
   * Android do it itself.</p>
   **/
  private boolean isSqlEsc = true;

  /**
   * <p>SQL Escape service.</p>
   **/
  private ISqlEsc sqlEsc;

  /**
   * <p>Puts String object to column values with SQL escaping
   * for JDBC.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pFdNm field name
   * @param pFdv field value
   * @param pCv column values
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final String pFdNm, final String pFdv,
      final ColVals pCv) throws Exception {
    String value;
    if (this.isSqlEsc && pFdv != null) {
      value = this.sqlEsc.esc(pFdv);
    } else {
      value = pFdv;
    }
    if (pCv.getStrs() == null) {
      pCv.setStrs(new HashMap<String, String>());
    }
    pCv.getStrs().put(pFdNm, value);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for isSqlEsc.</p>
   * @return boolean
   **/
  public final boolean getIsSqlEsc() {
    return this.isSqlEsc;
  }

  /**
   * <p>Setter for isSqlEsc.</p>
   * @param pIsSqlEsc reference
   **/
  public final void setIsSqlEsc(final boolean pIsSqlEsc) {
    this.isSqlEsc = pIsSqlEsc;
  }

  /**
   * <p>Getter for sqlEsc.</p>
   * @return ISqlEsc
   **/
  public final ISqlEsc getSqlEsc() {
    return this.sqlEsc;
  }

  /**
   * <p>Setter for sqlEsc.</p>
   * @param pSqlEsc reference
   **/
  public final void setSqlEsc(final ISqlEsc pSqlEsc) {
    this.sqlEsc = pSqlEsc;
  }
}
