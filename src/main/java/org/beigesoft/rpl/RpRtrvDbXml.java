/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following cond are met:

* Redistributions of source code must retain the above copyright notice, this
  list of cond and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of cond and the following disclaimer in the documentation
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

package org.beigesoft.rpl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.Writer;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.DbInf;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.log.ILog;

/**
 * <p>Service that retrieves entities of given type from DB then writes them
 * into stream (file or network connection) as XML by given writer.
 * This is transaction service. It's for replication
 * (export or identical copy) purposes.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class RpRtrvDbXml<RS> implements IRpRtrv {

  /**
   * <p>Entity Writer XML.</p>
   **/
  private IRpEntWri rpEntWri;

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Transaction isolation for reading DB phase.</p>
   **/
  private Integer readTi;

  /**
   * <p>Retrieves requested entities from DB then write them into a stream
   * by given writer.</p>
   * @param T Entity Class
   * @param pRvs request scoped vars (e.g. where clause)
   * @param pCls Entity Class
   * @param pWri writer
   * @return entities count
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> int rtrvTo(final Map<String, Object> pRvs,
      final Class<T> pCls, final Writer pWri) throws Exception {
    //e.g. "limit 20 offset 19":
    //e.g. "where (IID>0 and IDOR=2135) limit 20 offset 19":
    String cond = (String) pRvs.get("cond");
    int dsDbVr = Integer.parseInt((String) pRvs.get("dsDbVr"));
    List<T> entities = null;
    int entCnt = 0;
    DbInf di = this.rdb.getDbInf();
    if (dsDbVr == di.getDbVr()) {
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.readTi);
        this.rdb.begin();
        String srDbIdStr = (String) pRvs.get("srDbId");
        if (srDbIdStr != null) { //replication
          int srDbId = Integer.parseInt(srDbIdStr);
          if (srDbId != di.getDbId()) {
            String error = "Different requested database ID! required/is: "
                + srDbId + "/" + di.getDbId();
            this.log.error(pRvs, RpRtrvDbXml.class, error);
            pWri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            pWri.write("<message error=\"" + error + "\">\n");
            pWri.write("</message>\n");
            return entCnt;
          }
        } //else identical copy
        Map<String, Object> vs = new HashMap<String, Object>();
        vs.put(pCls.getSimpleName() + "dpLv", 1);
        if (cond == null) {
          entities = getOrm().retLst(pRvs, vs, pCls);
        } else {
          entities = getOrm().retLstCnd(pRvs, vs, pCls, cond);
        }
        entCnt = entities.size();
        this.rdb.commit();
      } catch (Exception ex) {
        if (!this.rdb.getAcmt()) {
          this.rdb.rollBack();
        }
        throw ex;
      } finally {
        this.rdb.release();
      }
      this.log.info(pRvs, RpRtrvDbXml.class, "Start write entities of "
        + pCls.getCanonicalName() + " count=" + entCnt);
      pWri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      pWri.write("<message databaseId=\"" + di.getDbId()
        + "\" dbVr=\"" + di.getDbVr()
          + "\" description=\"" + di.getInf() + "\" entCnt=\""
            + entCnt + "\">\n");
      for (T entity : entities) {
        this.rpEntWri.write(pRvs, entity, pWri);
      }
      pWri.write("</message>\n");
      this.log.info(pRvs, RpRtrvDbXml.class,
        "Entities has been wrote");
    } else {
      this.log.error(pRvs, RpRtrvDbXml.class,
        "Send error message - Different database version!");
      pWri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      pWri.write("<message error=\"Different database version!\">\n");
      pWri.write("</message>\n");
    }
    return entCnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rpEntWri.</p>
   * @return IRpEntWri
   **/
  public final IRpEntWri getRpEntWri() {
    return this.rpEntWri;
  }

  /**
   * <p>Setter for rpEntWri.</p>
   * @param pRpEntWri reference
   **/
  public final void setRpEntWri(
    final IRpEntWri pRpEntWri) {
    this.rpEntWri = pRpEntWri;
  }

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
   * <p>Getter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
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
   * <p>Getter for readTi.</p>
   * @return Integer
   **/
  public final Integer getReadTi() {
    return this.readTi;
  }

  /**
   * <p>Setter for readTi.</p>
   * @param pReadTi reference
   **/
  public final void setReadTi(final Integer pReadTi) {
    this.readTi = pReadTi;
  }
}
