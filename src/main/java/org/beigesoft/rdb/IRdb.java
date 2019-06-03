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

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.DbInf;

/**
 * <p>Abstraction of database service.
 * </p>
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public interface IRdb<RS> {

  /**
   * <p>TRANSACTION ISOLATION READ UNCOMMITTED.</p>
   **/
  Integer TRRUC = 1;

  /**
   * <p>TRANSACTION ISOLATION READ COMMITTED.</p>
   **/
  Integer TRRC = 2;

  /**
   * <p>TRANSACTION ISOLATION REPEATABLE READ.</p>
   **/
  Integer TRRPR = 4;

  /**
   * <p>TRANSACTION ISOLATION SERIALIZABLE.</p>
   **/
  Integer TRSR = 8;

  /**
   * <p>TRANSACTION NOT SUPPORTED.</p>
   **/
  Integer TRNO = 0;

  /**
   * <p>SQL exception.</p>
   **/
  int SQLEX = 1154;

  /**
   * <p>Dirty read.</p>
   **/
  int DITRD = 1151;

  /**
   * <p>Exception update/insert It should be 1 row updated/inserted
   * but it was...</p>
   **/
  int ERRIU = 1152;

  /**
   * <p>Bad parameters e.g. isolation level.</p>
   **/
  int BADPR = 1153;

  /**
   * <p>Getter for database info.</p>
   * @return database info
   * @throws Exception - an exception
   **/
  DbInf getDbInf() throws Exception;

  /**
   * <p>Gets if RDBMS in autocommit mode.</p>
   * @return if autocommit
   * @throws Exception - an exception
   **/
  boolean getAcmt() throws Exception;

  /**
   * <p>Sets RDBMS autocommit mode.</p>
   * @param pAcmt if autocommit
   * @throws Exception - an exception
   **/
  void setAcmt(boolean pAcmt) throws Exception;

  /**
   * <p>Sets transaction isolation level.</p>
   * @param pLevel according IOrm
   * @throws Exception - an exception
   **/
  void setTrIsl(int pLevel) throws Exception;

  /**
   * <p>Get transaction isolation level.</p>
   * @return pLevel according IOrm
   * @throws Exception - an exception
   **/
  int getTrIsl() throws Exception;

  /**
   * <p>Creates savepoint.</p>
   * @param pSavPntNm savepoint name
   * @throws Exception - an exception
   **/
  void creSavPnt(String pSavPntNm) throws Exception;

  /**
   * <p>Releases savepoint.</p>
   * @param pSavPntNm savepoint name
   * @throws Exception - an exception
   **/
  void relSavPnt(String pSavPntNm) throws Exception;

  /**
   * <p>Rollbacks transaction to savepoint.</p>
   * @param pSavPntNm savepoint name
   * @throws Exception - an exception
   **/
  void rollBack(String pSavPntNm) throws Exception;

  /**
   * <p>Start new transaction.</p>
   * @throws Exception - an exception
   **/
  void begin() throws Exception;

  /**
   * <p>Commits transaction.</p>
   * @throws Exception - an exception
   **/
  void commit() throws Exception;

  /**
   * <p>Rollbacks transaction.</p>
   * @throws Exception - an exception
   **/
  void rollBack() throws Exception;

  /**
   * <p>Retrieves records from DB.</p>
   * @param pSelect query SELECT
   * @return IRecSet record set
   * @throws Exception - an exception
   **/
  IRecSet<RS> retRs(String pSelect) throws Exception;

  /**
   * <p>Executes any SQL query that returns no data.
   * E.g. PRAGMA, etc.</p>
   * @param pQuery query
   * @throws Exception - an exception
   **/
  void exec(String pQuery) throws Exception;

  /**
   * <p>Executes SQL UPDATE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.</p>
   * @param pCls entity class
   * @param pClVls type-safe map column name - column value
   * @param pWhe where conditions e.g. "IID=2" or NULL - update all
   * @return row count affected
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> int update(Class<T> pCls, ColVals pClVls,
    String pWhe) throws Exception;

  /**
   * <p>Executes SQL INSERT that returns affected rows.
   * It is to adapt Android insert/update/delete interface.</p>
   * @param pCls entity class
   * @param pClVls type-safe map column name - column value
   * @return row count affected for JDBC
   * for Android -1 - error or row IDX
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> long insert(Class<T> pCls,
    ColVals pClVls) throws Exception;

  /**
   * <p>Executes SQL DELETE that returns affected rows.
   * It is to adapt Android insert/update/delete interface.</p>
   * @param pTbl table name
   * @param pWhe where conditions e.g. "IID=2" or NULL - delete all
   * @return row count affected
   * @throws Exception - an exception
   **/
  int delete(String pTbl, String pWhe) throws Exception;

  /**
   * <p>Releases resources if they is not null.</p>
   * @throws Exception - an exception
   **/
  void release() throws Exception;

  //some useful utilities that must be:
  /**
   * <p>Evaluates single Integer result.</p>
   * @param pQuery Query
   * @param pClNm Column Name
   * @return Integer result e.g 11231 or NULL
   * @throws Exception - an exception
   */
  Integer evInt(String pQuery, String pClNm) throws Exception;

  /**
   * <p>Evaluates single Long result.</p>
   * @param pQuery Query
   * @param pClNm Column Name
   * @return Long result e.g 11231 or NULL
   * @throws Exception - an exception
   */
  Long evLong(String pQuery, String pClNm) throws Exception;

  /**
   * <p>Evaluates single Float result.</p>
   * @param pQuery Query
   * @param pClNm Column Name
   * @return Float result e.g 1.1231 or NULL
   * @throws Exception - an exception
   */
  Float evFloat(String pQuery, String pClNm) throws Exception;

  /**
   * <p>Evaluates single Double result.</p>
   * @param pQuery Query
   * @param pClNm Column Name
   * @return Double result e.g 1.1231 or NULL
   * @throws Exception - an exception
   */
  Double evDouble(String pQuery, String pClNm) throws Exception;

  /**
   * <p>Evaluates Double results.</p>
   * @param pQuery Query
   * @param pClNms Column Names
   * @return Double[] result e.g. [2.14, NULL, 111.456]
   * @throws Exception - an exception
   */
  Double[] evDoubles(String pQuery, String[] pClNms) throws Exception;
}
