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

import java.sql.Savepoint;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.rdb.SrvClVl;
import org.beigesoft.rdb.ARdb;

/**
 * <p>JDBC of database service.</p>

 * @author Yury Demidenko
 */
public class Rdb extends ARdb<ResultSet> {

  //services:
  /**
   * <p>Generating insert/update and CV service.</p>
   **/
  private SrvClVl srvClVl;

  //DS:
  /**
   * <p>Data source.</p>
   */
  private DataSource ds;

  //Thread scoped vars:
  /**
   * <p>Connection per thread holder.</p>
   **/
  private static final ThreadLocal<Connection> HLDCON =
    new ThreadLocal<Connection>() { };

  /**
   * <p>Savepoints map per thread holder.</p>
   **/
  private static final ThreadLocal<Map<String, Savepoint>> HLDAVPS =
    new ThreadLocal<Map<String, Savepoint>>() { };

  /**
   * <p>Gets if RDBMS in autocommit mode.</p>
   * @return if autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final boolean getAcmt() throws Exception {
    return lazCon().getAutoCommit();
  }

  /**
   * <p>Sets RDBMS autocommit mode.</p>
   * @param pAcmt if autocommit
   * @throws Exception - an exception
   **/
  @Override
  public final void setAcmt(final boolean pAcmt) throws Exception {
    lazCon().setAutoCommit(pAcmt);
  }

  /**
   * <p>Set transaction isolation level.</p>
   * @param pLevel according IOrm
   * @throws Exception - an exception
   **/
  @Override
  public final void setTrIsl(final int pLevel) throws Exception {
    if (!(pLevel == Connection.TRANSACTION_READ_COMMITTED
      || pLevel == Connection.TRANSACTION_READ_UNCOMMITTED
        || pLevel == Connection.TRANSACTION_REPEATABLE_READ
          || pLevel == Connection.TRANSACTION_SERIALIZABLE)) {
      throw new ExcCode(ExcCode.WRCN,
        "Transaction isolation not supported# " + pLevel);
    }
    lazCon().setTransactionIsolation(pLevel);
  }

  /**
   * <p>Get transaction isolation level.</p>
   * @return pLevel according IOrm
   * @throws Exception - an exception
   **/
  @Override
  public final int getTrIsl() throws Exception {
    return lazCon().getTransactionIsolation();
  }

  /**
   * <p>Create savepoint.</p>
   * @param pSpNm savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void creSavPnt(final String pSpNm) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 8000);
    if (dbgSh) {
      getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
        .getId() + ", create SP " + pSpNm);
    }
    Savepoint sp = lazCon().setSavepoint(pSpNm);
    lazSvps().put(pSpNm, sp);
  }

  /**
   * <p>Release savepoint.</p>
   * @param pSpNm savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void relSavPnt(final String pSpNm) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 8001);
    if (dbgSh) {
      getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
        .getId() + ", release SP " + pSpNm);
    }
    Map<String, Savepoint> svps = lazSvps();
    lazCon().releaseSavepoint(svps.get(pSpNm));
    lazSvps().remove(pSpNm);
  }

  /**
   * <p>Rollback transaction to savepoint.</p>
   * @param pSpNm savepoint name
   * @throws Exception - an exception
   **/
  @Override
  public final void rollBack(final String pSpNm) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 8002);
    if (dbgSh) {
      getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
        .getId() + ", roll back to " + pSpNm);
    }
    Map<String, Savepoint> svps = lazSvps();
    lazCon().rollback(svps.get(pSpNm));
    svps.remove(pSpNm);
  }

  /**
   * <p>Start new transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void begin() throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 8003);
    if (dbgSh) {
      getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
        .getId() + ", start.");
    }
    lazCon();
  }

  /**
   * <p>Commit transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void commit() throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 8004);
    if (dbgSh) {
      getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
        .getId() + ", commit.");
    }
    lazCon().commit();
  }

  /**
   * <p>Rollback transaction.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void rollBack() throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 8005);
    if (dbgSh) {
      getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
        .getId() + ", roll back!");
    }
    lazCon().rollback();
  }

  /**
   * <p>Retrieves records from DB.</p>
   * @param pSel query SELECT
   * @return IRecSet record set
   * @throws Exception - an exception
   **/
  @Override
  public final IRecSet<ResultSet> retRs(final String pSel) throws Exception {
    try {
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 8006);
      if (dbgSh) {
        getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
          .getId() + ", try to execute: " + pSel);
      }
      final Statement stmt = lazCon().createStatement();
      final ResultSet rs = stmt.executeQuery(pSel);
      return new RecSet(rs, stmt);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", qu:\n" + pSel;
      throw new ExcCode(SQLEX, msg);
    }
  }

  /**
   * <p>Executes any SQL query that returns no data, e.g. PRAGMA, etc.</p>
   * @param pQu query
   * @throws Exception - an exception
   **/
  @Override
  public final void exec(final String pQu) throws Exception {
    Statement stmt = null;
    try {
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 8007);
      if (dbgSh) {
        getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
          .getId() + ", try to execute: " + pQu);
      }
      stmt = lazCon().createStatement();
      stmt.executeUpdate(pQu);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", qu:\n" + pQu;
      throw new ExcCode(SQLEX, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
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
  public final <T extends IHasId<?>> int update(final Class<T> pCls,
    final ColVals pCv, final String pWhe) throws Exception {
    Statement stmt = null;
    String qu = getSrvClVl().evUpdateCnd(pCls, pCv, pWhe);
    try {
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 8008);
      if (dbgSh) {
        getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
          .getId() + ", try to execute update: " + qu);
      }
      stmt = lazCon().createStatement();
      return stmt.executeUpdate(qu);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", qu:\n" + qu;
      throw new ExcCode(SQLEX, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
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
    Statement stmt = null;
    String qu = getSrvClVl().evInsert(pCls, pCv);
    try {
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 8009);
      if (dbgSh) {
        getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
          .getId() + ", try to execute insert: " + qu);
      }
      stmt = lazCon().createStatement();
      return stmt.executeUpdate(qu);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", qu:\n" + qu;
      throw new ExcCode(SQLEX, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
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
    Statement stmt = null;
    String strWhe = "";
    if (pWhe != null) {
      strWhe = " where " + pWhe;
    }
    String qu = "delete from " + pTbl + strWhe + ";";
    try {
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 8010);
      if (dbgSh) {
        getLog().debug(null, getClass(), "Thread ID=" + Thread.currentThread()
          .getId() + ", try to execute delete: " + qu);
      }
      stmt = lazCon().createStatement();
      return stmt.executeUpdate(qu);
    } catch (SQLException sqle) {
      String msg = sqle.getMessage() + ", RDBMS error code "
        + sqle.getErrorCode() + ", qu:\n" + qu;
      throw new ExcCode(SQLEX, msg);
    } finally {
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  /**
   * <p>Releases resources if they is not null.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void release() throws Exception {
    Connection con = HLDCON.get();
    if (con != null) {
      con.setAutoCommit(true);
      con.close();
      HLDCON.set(null);
    }
  }

  /**
   * <p>Getter/establisher connection per thread.</p>
   * @return Connection
   * @throws Exception - an exception
   **/
  public final Connection lazCon() throws Exception {
    Connection con = HLDCON.get();
    if (con == null) {
      con = ds.getConnection();
      HLDCON.set(con);
    }
    return con;
  }

  /**
   * <p>Lazy gets savepoints map per thread.</p>
   * @return savepoints map
   * @throws Exception - an exception
   **/
  public final Map<String, Savepoint> lazSvps() throws Exception {
    Map<String, Savepoint> svps = HLDAVPS.get();
    if (svps == null) {
      svps = new HashMap<String, Savepoint>();
      HLDAVPS.set(svps);
    }
    return svps;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvClVl.</p>
   * @return SrvClVl
   **/
  public final SrvClVl getSrvClVl() {
    return this.srvClVl;
  }

  /**
   * <p>Setter for srvClVl.</p>
   * @param pSrvClVl reference
   **/
  public final void setSrvClVl(final SrvClVl pSrvClVl) {
    this.srvClVl = pSrvClVl;
  }

  /**
   * <p>Getter for ds.</p>
   * @return DataSource
   **/
  public final DataSource getDs() {
    return this.ds;
  }

  /**
   * <p>Setter for ds.</p>
   * @param pDs reference
   **/
  public final void setDs(final DataSource pDs) {
    this.ds = pDs;
  }
}
