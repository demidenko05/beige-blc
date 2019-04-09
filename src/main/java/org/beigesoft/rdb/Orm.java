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

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.fct.IFctCls;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.log.ILog;
import org.beigesoft.cnv.IFilObj;
import org.beigesoft.prp.ISetng;

/**
 * <p>ORM service.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class Orm<RS> implements IOrm {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>RDBMS service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Generating select service.</p>
   **/
  private ISqlQu selct;

  /**
   * <p>Filler entity from RS.</p>
   **/
  private IFilObj<IRecSet<RS>> filEntRs;

  /**
   * <p>Factory of entity's factory.</p>
   **/
  private IFctCls<IFctRq<?>> fctFctEnt;

  /**
   * <p>Initializes database, e.g. create/updates tables if need.
   * It must be invoked once by factory initializing this service.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @throws Exception - an exception
   **/
  @Override
  public final void init(final Map<String, Object> pRqVs) throws Exception {
  }

  /**
   * <p>Retrieve entity from DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @return entity or null
   * @throws Exception - an exception
   **/
  @Override
  public final <T> T retEnt(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt) throws Exception {
    StringBuffer sb = this.selct.evSel(pRqVs, pVs, pEnt.getClass());
    sb.append(" where ");
    this.selct.evCndId(pRqVs, pEnt, sb);
    sb.append(";");
    @SuppressWarnings("unchecked")
    T ent = (T) retEntQu(pRqVs, pVs, pEnt.getClass(), sb.toString());
    return ent;
  }

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
  @Override
  public final <T> T retEntCnd(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pCond) throws Exception {
    StringBuffer sb = this.selct.evSel(pRqVs, pVs, pCls);
    sb.append(" " + pCond + ";");
    T ent = retEntQu(pRqVs, pVs, pCls, sb.toString());
    return ent;
  }

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
  @Override
  public final <T> T retEntQu(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pQu) throws Exception {
    T ent = null;
    IRecSet<RS> rs = null;
    try {
      rs = this.rdb.retRs(pQu);
      if (rs.first()) {
        @SuppressWarnings("unchecked")
        IFctRq<T> fctEnt = (IFctRq<T>) this.fctFctEnt.laz(pRqVs, pCls);
        ent = fctEnt.create(pRqVs);
        this.filEntRs.fill(pRqVs, pVs, ent, rs);
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return ent;
  }

  /**
   * <p>Insert entity into DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void insert(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt) throws Exception {
    throw new Exception("NEI");
  }

  /**
   * <p>Update entity with ID in DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void update(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt) throws Exception {
    throw new Exception("NEI");
  }

  /**
   * <p>Delete entity with ID from DB.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pEnt entity
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void del(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt) throws Exception {
    throw new Exception("NEI");
  }

  /**
   * <p>Delete entity(is) with condition.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @param pWhere Not Null e.g. "WAREHOUSESITE=1 and PRODUCT=1"
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void delWhe(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pWhere) throws Exception {
    throw new Exception("NEI");
  }

  /**
   * <p>Retrieve a list of all entities.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @return list of all business objects or empty list, not null
   * @throws Exception - an exception
   */
  @Override
  public final <T> List<T> retLst(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> List<T> retLstCnd(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pCond) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> List<T> retLstQu(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pQu) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> List<T> retLstFld(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt,
      final String pFieldFor) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> List<T> retPg(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final Integer pFst, final Integer pPgSz) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> List<T> retPgCnd(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls, final String pCond,
      final Integer pFst, final Integer pPgSz) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> List<T> retPgQu(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls, final String pQu,
      final Integer pFst, final Integer pPgSz) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> Integer evRowCntWhe(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pWhere) throws Exception {
    throw new Exception("NEI");
  }

  /**
   * <p>Calculate total rows.</p>
   * @param <T> - type of business object,
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. "needed fields", nullable
   * @param pCls entity class
   * @return Integer row count
   * @throws Exception - an exception
   */
  @Override
  public final <T> Integer evRowCnt(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls) throws Exception {
    throw new Exception("NEI");
  }

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
  @Override
  public final <T> Integer evRowCntQu(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final String pQu) throws Exception {
    throw new Exception("NEI");
  }

  /**
   * <p>Getter for database ID.
   * Any database mist has ID, int is suitable type for that cause
   * its range is enough and it's faster than String.</p>
   * @return ID database
   **/
  @Override
  public final Integer getDbId() {
    return this.rdb.getDbId();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
  }

  /**
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for selct.</p>
   * @return ISqlQu
   **/
  public final ISqlQu getSqlQu() {
    return this.selct;
  }

  /**
   * <p>Setter for selct.</p>
   * @param pSqlQu reference
   **/
  public final void setSqlQu(final ISqlQu pSqlQu) {
    this.selct = pSqlQu;
  }

  /**
   * <p>Getter for filEntRs.</p>
   * @return IFilObj<IRecSet<RS>>
   **/
  public final IFilObj<IRecSet<RS>> getFilEntRs() {
    return this.filEntRs;
  }

  /**
   * <p>Setter for filEntRs.</p>
   * @param pFilEntRs reference
   **/
  public final void setFilEntRs(final IFilObj<IRecSet<RS>> pFilEntRs) {
    this.filEntRs = pFilEntRs;
  }

  /**
   * <p>Getter for fctFctEnt.</p>
   * @return IFctCls<IFctRq<?>>
   **/
  public final IFctCls<IFctRq<?>> getFctFctEnt() {
    return this.fctFctEnt;
  }

  /**
   * <p>Setter for fctFctEnt.</p>
   * @param pFctFctEnt reference
   **/
  public final void setFctFctEnt(final IFctCls<IFctRq<?>> pFctFctEnt) {
    this.fctFctEnt = pFctFctEnt;
  }
}
