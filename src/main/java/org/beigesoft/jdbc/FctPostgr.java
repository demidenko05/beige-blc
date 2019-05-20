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

import java.util.Map;
import java.util.Properties;
import java.sql.ResultSet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.beigesoft.fct.IFctAux;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Auxiliary factory for Postgres JDBC.</p>
 *
 * @author Yury Demidenko
 */
public class FctPostgr implements IFctAux<ResultSet> {

  /**
   * <p>Data-source to close when releasing.</p>
   **/
  private HikariDataSource ds;

  /**
   * <p>Creates requested bean and put into given main factory.
   * The main factory is already synchronized when invokes this.</p>
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @param pFctApp main factory
   * @return Object - requested bean or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Object crePut(final Map<String, Object> pRqVs,
    final String pBnNm, final FctBlc<ResultSet> pFctApp) throws Exception {
    Object rz = null;
    if (IRdb.class.getSimpleName().equals(pBnNm)) {
      rz = crPuRdb(pRqVs, pFctApp);
    }
    return rz;
  }

  /**
   * <p>Releases state when main factory is releasing.</p>
   * @param pRqVs request scoped vars
   * @param pFctApp main factory
   * @throws Exception - an exception
   */
  @Override
  public final void release(final Map<String, Object> pRqVs,
    final FctBlc<ResultSet> pFctApp) throws Exception {
    if (this.ds != null) {
      pFctApp.lazLogStd(null).info(pRqVs, getClass(), "Try to close DS...");
      try {
        this.ds.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        this.ds = null;
      }
    }
  }

  /**
   * <p>Creates and puts into MF Rdb.</p>
   * @param pRqVs request scoped vars
   * @param pFctApp main factory
   * @return Rdb
   * @throws Exception - an exception
   */
  private Rdb crPuRdb(final Map<String, Object> pRqVs,
    final FctBlc<ResultSet> pFctApp) throws Exception {
    Rdb rdb = new Rdb();
    rdb.setLog(pFctApp.lazLogStd(pRqVs));
    rdb.setSrvClVl(pFctApp.lazSrvClVl(pRqVs));
    Properties props = new Properties();
    props.setProperty("dataSource.user", pFctApp.getFctDt().getDbUsr());
    props.setProperty("dataSource.password", pFctApp.getFctDt().getDbPwd());
    props.setProperty("dataSource.databaseName", pFctApp.getFctDt().getDbUrl());
    props.setProperty("dataSourceClassName", pFctApp.getFctDt().getDbCls());
    HikariConfig hc = new HikariConfig(props);
    this.ds = new HikariDataSource(hc);
    rdb.setDs(this.ds);
    pFctApp.put(pRqVs, IRdb.class.getSimpleName(), rdb);
    pFctApp.lazLogStd(pRqVs).info(pRqVs, getClass(), IRdb.class.getSimpleName()
      + " has been created");
    return rdb;
  }
}
