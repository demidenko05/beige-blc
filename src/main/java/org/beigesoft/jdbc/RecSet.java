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

package org.beigesoft.jdbc;

import java.sql.Statement;
import java.sql.ResultSet;

import org.beigesoft.mdl.IRecSet;

/**
 * <p>Record-set adapter implementation with JDBC.</p>
 *
 * @author Yury Demidenko
 */
public class RecSet implements IRecSet<ResultSet> {

  /**
   * <p>JDBC recordset.</p>
   **/
  private final ResultSet recSet;

  /**
   * <p>JDBC recordset.</p>
   **/
  private final Statement stm;

  /**
   * <p>Only constructor.</p>
   * @param pResSet result set
   * @param pStm JDBC statement
   **/
  public RecSet(final ResultSet pResSet, final Statement pStm) {
    this.recSet = pResSet;
    this.stm = pStm;
  }

  /**
   * <p>Geter for ResultSet.</p>
   * @return ResultSet
   **/
  @Override
  public final ResultSet getRecSet() {
    return this.recSet;
  }

  /**
   * <p>Move cursor to next record.</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean next() throws Exception {
    return this.recSet.next();
  }

  /**
   * <p>Move cursor to first record (for Android compatible).</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean first() throws Exception {
    return this.recSet.next();
  }

  /**
   * <p>Close resultset, for JDBC close stm.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void close() throws Exception {
    this.recSet.close();
    this.stm.close();
  }


  /**
   * <p>Retrieve String column value.</p>
   * @param pClNm column name
   * @return String result
   * @throws Exception - an exception
   **/
  @Override
  public final String getStr(final String pClNm) throws Exception {
    // JSE API -  if the value is SQL NULL, the value returned is null
    return this.recSet.getString(pClNm);
  }

  /**
   * <p>Retrieve Double column value.</p>
   * @param pClNm column name
   * @return Double result
   * @throws Exception - an exception
   **/
  @Override
  public final Double getDouble(final String pClNm) throws Exception {
    Double result = this.recSet.getDouble(pClNm);
    if (this.recSet.wasNull()) {
      return null;
    }
    return result;
  }

  /**
   * <p>Retrieve Float column value.</p>
   * @param pClNm column name
   * @return Float result
   * @throws Exception - an exception
   **/
  @Override
  public final Float getFloat(final String pClNm) throws Exception {
    Float result = this.recSet.getFloat(pClNm);
    if (this.recSet.wasNull()) {
      return null;
    }
    return result;
  }

  /**
   * <p>Retrieve Integer column value.</p>
   * @param pClNm column name
   * @return Integer result
   * @throws Exception - an exception
   **/
  @Override
  public final Integer getInt(final String pClNm) throws Exception {
    Integer result = this.recSet.getInt(pClNm);
    if (this.recSet.wasNull()) {
      return null;
    }
    return result;
  }

  /**
   * <p>Retrieve Long column value.</p>
   * @param pClNm column name
   * @return Long result
   * @throws Exception - an exception
   **/
  @Override
  public final Long getLong(final String pClNm) throws Exception {
    Long result = this.recSet.getLong(pClNm);
    if (this.recSet.wasNull()) {
      return null;
    }
    return result;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for Statement.</p>
   * @return Statement
   **/
  public final Statement getStm() {
    return this.stm;
  }
}
