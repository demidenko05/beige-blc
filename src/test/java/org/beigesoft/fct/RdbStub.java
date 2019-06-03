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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.rdb.ARdb;

/**
 * <p>Stub of database service.</p>
 * 
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class RdbStub<RS> extends ARdb<RS> {

  /**
   * <p>Gets if RDBMS in autocommit mode.</p>
   * @return if autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final boolean getAcmt() throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Sets RDBMS autocommit mode.</p>
   * @param pAcmt if autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final void setAcmt(final boolean pAcmt) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Set transaction isolation level.</p>
   * @param pLevel according IOrm
   * @throws Exception - an exception
   **/
  @Override
  public final void setTrIsl(final int pLevel) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Get transaction isolation level.</p>
   * @return pLevel according IOrm
   * @throws Exception - an exception
   **/
  @Override
  public final int getTrIsl() throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Create savepoint.</p>
   * @param pSpNm savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void creSavPnt(final String pSpNm) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Release savepoint.</p>
   * @param pSpNm savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void relSavPnt(final String pSpNm) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Rollback transaction to savepoint.</p>
   * @param pSpNm savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void rollBack(final String pSpNm) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Start new transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void begin() throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Commit transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void commit() throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Rollback transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void rollBack() throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Retrieves records from DB.</p>
   * @param pSel query SELECT
   * @return IRecSet record set
   * @throws Exception - an exception
   **/
  @Override
  public final IRecSet<RS> retRs(final String pSel) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Executes any SQL query that returns no data, e.g. PRAGMA, etc.</p>
   * @param pQu query
   * @throws Exception - an exception
   **/
  @Override
  public final void exec(final String pQu) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Executes SQL UPDATE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.</p>
   * @param pCls entity class
   * @param pCv type-safe map column name - column value
   * @param pWhe where conditions e.g. "itsId=2"
   * @return row count affected
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> int update(final Class<T> pCls, final ColVals pCv,
    final String pWhe) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Executes SQL INSERT that returns affected rows.
   * It is to adapt Android insert/update/delete interface.</p>
   * @param pCls entity class
   * @param pCv type-safe map column name - column value
   * @return row count affected for JDBC
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> long insert(final Class<T> pCls,
    final ColVals pCv) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Executes SQL DELETE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.</p>
   * @param pTbl table name
   * @param pWhe where conditions e.g. "IID=2" or NULL -delete all
   * @return row count affected
   * @throws Exception - an exception
   **/
  @Override
  public final int delete(final String pTbl,
    final String pWhe) throws Exception {
    throw new Exception("STUB!");
  }

  /**
   * <p>Releases resources if they is not null.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void release() throws Exception {
    throw new Exception("STUB!");
  }
}
