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

import java.util.List;
import java.util.Map;

/**
 * <p>Abstraction of ORM service.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public interface IOrm<RS> {

  /**
   * <p>Refresh entity from DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retEnt(Map<String, Object> pRqVs, Map<String, Object> pVs,
    T pEnt) throws Exception;

  /**
   * <p>Refresh entity from DB by its ID.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pItsId entity ID
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retEntId(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, Object pItsId) throws Exception;

  /**
   * <p>Retrieve entity from DB by given query conditions.
   * The first record in record-set will be returned.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pCond Not NULL e.g. "where name='U1' ORDER BY id"
   * or "" that means without filter/order
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retEntCnd(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pCond) throws Exception;

  /**
   * <p>Retrieve entity from DB by query.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pQu SELECT statement
   * @return entity or null
   * @throws Exception - an exception
   **/
  <T> T retEntQu(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pQu) throws Exception;

  /**
   * <p>Insert entity into DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T> void insert(Map<String, Object> pRqVs, Map<String, Object> pVs,
    T pEnt) throws Exception;

  /**
   * <p>Update entity with ID in DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T> void update(Map<String, Object> pRqVs, Map<String, Object> pVs,
    T pEnt) throws Exception;

  /**
   * <p>Delete entity with ID from DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  <T> void del(Map<String, Object> pRqVs, Map<String, Object> pVs,
    T pEnt) throws Exception;

  /**
   * <p>Delete entity(is) with condition.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pWhere Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  <T> void delWhere(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pWhere) throws Exception;

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retLst(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls) throws Exception;

  /**
   * <p>Retrieve a list of entities.</p>
   * @param <T> - type of business object
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pCond Not NULL e.g. "where name='U1' ORDER BY id"
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retLstCnd(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pCond) throws Exception;

  /**
   * <p>Retrieve a list of entities by complex query that may contain
   * additional joins and filters, see Beige-Webstore for example.</p>
   * @param <T> - type of business object
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pQu Not NULL complex query
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retLstQu(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pQu) throws Exception;

  /**
   * <p>Retrieve entity's lists for field that used as filter
   * (e.g. invoice lines for invoice).</p>
   * @param <T> - type of entity owned
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity InvoiceLine
   * @param pFieldFor - name of field to be filter, e.g. "invoice"
   * to retrieve invoices lines for invoice
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retLstFld(Map<String, Object> pRqVs, Map<String, Object> pVs,
    T pEnt, String pFieldFor) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pFst number of the first record (from 0)
   * @param pPgSz page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retPg(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, Integer pFst, Integer pPgSz) throws Exception;

  /**
   * <p>Retrieve a page of entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pCond not null e.g. "WHERE name='U1' ORDER BY id"
   * @param pFst number of the first record (from 0)
   * @param pPgSz page size (max records)
   * @return list of business objects or empty list, not null
   * @throws Exception - an exception
   */
  <T> List<T> retPgCnd(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pCond, Integer pFst, Integer pPgSz) throws Exception;

  /**
   * <p>Retrieve a page of entities by given complex query.
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
  <T> List<T> retPgQu(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pQu, Integer pFst, Integer pPgSz) throws Exception;

  /**
   * <p>Evaluate common(without WHERE) SQL SELECT
   * statement for entity type. It's used externally
   * to make more complex query with additional joints and filters.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @return String SQL DML query
   * @throws Exception - an exception
   **/
  <T> String evSelect(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls) throws Exception;

  /**
   * <p>Calculate total rows.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pWhere not null e.g. "ITSID > 33"
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evRowCntWhere(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pWhere) throws Exception;

  /**
   * <p>Calculate total rows.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evRowCnt(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls) throws Exception;

  /**
   * <p>Calculate total rows for pagination by custom query.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pQu not null custom query with column named TOTROWS
   * @return Integer row count
   * @throws Exception - an exception
   */
  <T> Integer evRowCntQu(Map<String, Object> pRqVs, Map<String, Object> pVs,
    Class<T> pCls, String pQu) throws Exception;

  /**
   * <p>Getter for database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID database
   **/
  Integer getDbId();
}
