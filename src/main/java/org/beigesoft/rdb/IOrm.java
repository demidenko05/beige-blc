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

import java.util.List;
import java.util.Map;

import org.beigesoft.mdl.IHasId;

/**
 * <p>Abstraction of ORM service.</p>
 *
 * @author Yury Demidenko
 */
public interface IOrm {

  /**
   * <p>Setting APP info.</p>
   **/
  String APPINF = "appInf";

  /**
   * <p>Setting JDBC class.</p>
   **/
  String JDBCCLS = "jdbcCls";

  /**
   * <p>Setting DS class.</p>
   **/
  String DSCLS = "dsCls";

  /**
   * <p>Setting DB user name.</p>
   **/
  String DBUSR = "dbUsr";

  /**
   * <p>Setting DB user password.</p>
   **/
  String DBPSW = "dbPsw";

  /**
   * <p>Setting DB URL.</p>
   **/
  String DBURL = "dbUrl";

  /**
   * <p>Setting check if table exists query.</p>
   **/
  String CHECKTBL = "checkTbl";

  /**
   * <p>Table var in check if table exists query.</p>
   **/
  String TBLNM = ":tblNm";

  /**
   * <p>Query initializing.</p>
   **/
  String INITSQL = "init.sql";

  /**
   * <p>Query insert.</p>
   **/
  String INSTSQL = "inst.sql";

  /**
   * <p>Query upgrade without suffix.</p>
   **/
  String UPGRSQL = "upgr";

  /**
   * <p>Query last inserted ID, e.g. for SQLITE, MySql.</p>
   **/
  String LASTID = "lastId";

  /**
   * <p>Sub-query returning ID, e.g. for Postgresql.</p>
   **/
  String RETID = "retId";

  /**
   * <p>Word that point to current dir System.getProperty("user.dir").</p>
   **/
  String CURDIR = "#currentDir#";

  /**
   * <p>Word that point to current parent dir
   * System.getProperty("user.dir").parent.</p>
   **/
  String CURPDIR = "#currentParentDir#";

  /**
   * <p>Optimistic locking fail, update/delete returns 0.</p>
   **/
  int DRTREAD = 1151;

  /**
   * <p>Error insert/update/delete entity, row count != 1.</p>
   **/
  int ACTROWERR = 1152;

  /**
   * <p>Initializes database, e.g. create/updates tables if need.
   * It must be invoked once by an initializer (e.g. load on startup servlet).
   * </p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @throws Exception - an exception
   **/
  void init(Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Retrieves entity from DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> T retEnt(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Refreshes entity from DB. If not found then ID will be nulled.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> void refrEnt(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Retrieves entity from DB by query conditions, if more than 1 result,
   * then trows exception.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pCond Not NULL e.g. "ORID=1 and DBID=2"
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> T retEntCnd(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pCond) throws Exception;

  /**
   * <p>Retrieves entity from DB by query, if more than 1 result,
   * then trows exception.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pQu SELECT statement
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> T retEntQu(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pQu) throws Exception;

  /**
   * <p>Inserts entity into DB. It's should be used by generic requester that
   * is not dedicated to concrete entity type, e.g. HTML request handler.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> void insert(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Inserts entity with no Long ID into DB.
   * It's should be used by requester that is dedicated to concrete entity
   * type with no Long ID, e.g. account saver and account has string ID.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> void insIdNln(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Inserts entity with Long ID (maybe auto-generated) into DB.
   * It's should be used by requester that is dedicated to concrete entity
   * type with Long ID, e.g. invoice saver.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T extends IHasId<Long>> void insIdLn(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Updates entity with ID in DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> void update(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Deletes entity with ID from DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T extends IHasId<?>> void del(Map<String, Object> pRqVs,
    Map<String, Object> pVs, T pEnt) throws Exception;

  /**
   * <p>Retrieves a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> List<T> retLst(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls) throws Exception;

  /**
   * <p>Retrieves a list of entities.</p>
   * @param <T> - type of business object
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pCond Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> List<T> retLstCnd(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pCond) throws Exception;

  /**
   * <p>Retrieves a list of entities by complex query that may contain
   * additional joins and filters, see Beige-Webstore for example.</p>
   * @param <T> - type of business object
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pQu Not NULL complex query
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> List<T> retLstQu(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pQu) throws Exception;

  /**
   * <p>Retrieves a page of entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pFst number of the first record (from 0)
   * @param pPgSz page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> List<T> retPg(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, Integer pFst,
      Integer pPgSz) throws Exception;

  /**
   * <p>Retrieves a page of entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pCond not null e.g. "name='U1' ORDER BY id"
   * @param pFst number of the first record (from 0)
   * @param pPgSz page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> List<T> retPgCnd(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pCond, Integer pFst,
      Integer pPgSz) throws Exception;

  /**
   * <p>Retrieves a page of entities by given complex query.
   * For example it used to retrieve page Itms to sell in Beige-Webstore
   * by complex query what might consist of joints to filtered goods/services,
   * it also may has not all fields e.g. omit unused auctioning fields for
   * performance advantage.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pQu not null complex query without page conditions
   * @param pFst number of the first record (from 0)
   * @param pPgSz page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> List<T> retPgQu(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pQu, Integer pFst,
      Integer pPgSz) throws Exception;

  /**
   * <p>Calculates total rows for given entity class.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars
   * @param pCls entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> Integer evRowCnt(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls) throws Exception;

  /**
   * <p>Calculates total rows for given entity class and conditions.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pWhe not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T extends IHasId<?>> Integer evRowCntWhe(Map<String, Object> pRqVs,
    Map<String, Object> pVs, Class<T> pCls, String pWhe) throws Exception;

  /**
   * <p>Getter for database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID database
   * @throws Exception - an exception
   **/
  Integer getDbId() throws Exception;
}
