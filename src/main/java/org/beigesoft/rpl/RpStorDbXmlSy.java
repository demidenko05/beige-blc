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

package org.beigesoft.rpl;

import java.util.Map;
import java.util.HashMap;
import java.io.Reader;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.IUtlXml;

/**
 * <p>Service that reads entities from given stream XML
 * (file or network connection) then replicates/synchronizes
 * them into database.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class RpStorDbXmlSy<RS> implements IRpStor {

  /**
   * <p>Entity Reader.</p>
   **/
  private IRpEntRead<IHasId<?>> rpEntRead;

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM srv.</p>
   **/
  private IOrm orm;

  /**
   * <p>Database srv.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>XML srv.</p>
   **/
  private IUtlXml utlXml;

  /**
   * <p>Syncer's factory.</p>
   */
  private IFcRpEnSy fctEntSy;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Transaction isolation for changing DB phase.</p>
   **/
  private Integer writeTi;

  /**
   * <p>Reads entities from given stream XML
   * (file or network connection) then replicates/synchronizes
   * them into database.</p>
   * @param pRvs request scoped vars
   * @param pReader Reader
   * @throws Exception - an exception
   **/
  @Override
  public final void storeFr(final Map<String, Object> pRvs,
    final Reader pReader) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.writeTi);
      this.rdb.begin();
      while (this.utlXml.readUntilStart(pReader, "entity")) {
        IHasId<?> ent = this.rpEntRead.read(pRvs, pReader);
        String entSyNm = this.setng
          .lazClsStg(ent.getClass(), IRpEntSync.RPENTSYNCNM);
        if (entSyNm == null) {
          throw new ExcCode(ExcCode.WRCN, "There is no syncer for "
            + ent.getClass());
        }
        @SuppressWarnings("unchecked")
        IRpEntSync<IHasId<?>> entSy = (IRpEntSync<IHasId<?>>) this.fctEntSy
          .laz(pRvs, entSyNm);
        entSy.sync(pRvs, ent);
        if (ent.getIsNew()) {
          this.orm.insert(pRvs, vs, ent);
        } else {
          this.orm.update(pRvs, vs, ent);
        }
      }
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rpEntRead.</p>
   * @return IRpEntRead
   **/
  public final IRpEntRead<IHasId<?>> getRpEntRead() {
    return this.rpEntRead;
  }

  /**
   * <p>Setter for rpEntRead.</p>
   * @param pRpEntRead reference
   **/
  public final void setRpEntRead(final IRpEntRead<IHasId<?>> pRpEntRead) {
    this.rpEntRead = pRpEntRead;
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
   * <p>Getter for utlXml.</p>
   * @return IUtlXml
   **/
  public final IUtlXml getUtlXml() {
    return this.utlXml;
  }

  /**
   * <p>Setter for utlXml.</p>
   * @param pUtlXml reference
   **/
  public final void setUtlXml(final IUtlXml pUtlXml) {
    this.utlXml = pUtlXml;
  }

  /**
   * <p>Getter for fctEntSy.</p>
   * @return IFcRpEnSy
   **/
  public final IFcRpEnSy getFctEntSy() {
    return this.fctEntSy;
  }

  /**
   * <p>Setter for fctEntSy.</p>
   * @param pFctEntSy reference
   **/
  public final void setFctEntSy(final IFcRpEnSy pFctEntSy) {
    this.fctEntSy = pFctEntSy;
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
   * <p>Getter for writeTi.</p>
   * @return Integer
   **/
  public final Integer getWriteTi() {
    return this.writeTi;
  }

  /**
   * <p>Setter for writeTi.</p>
   * @param pWriteTi reference
   **/
  public final void setWriteTi(final Integer pWriteTi) {
    this.writeTi = pWriteTi;
  }
}
