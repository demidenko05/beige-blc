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

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.io.Writer;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.IOrId;
import org.beigesoft.log.ILog;
import org.beigesoft.dlg.IMake;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Service that refresh sequences for auto-incremental ID after importing
 * only for PostgreSQL.
 * This is transactional service.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PsgAft<RS> implements IMake {

  /**
   * <p>Replicators settings manager.</p>
   **/
  private ISetng setng;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Clears current database.</p>
   * @param pRvs request scoped vars
   * @throws Exception - an exception
   **/
  @Override
  public final void make(final Map<String, Object> pRvs) throws Exception {
    List<Class<? extends IHasId<?>>> clss = this.setng.lazClss();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(IRdb.TRRUC); //it must be invoked for single user only
      this.rdb.begin();
      this.log.info(pRvs, PsgAft.class, "Start refresh sequences auto-ID...");
      int rfrc = 0;
      for (int i = clss.size() - 1; i >= 0; i--) {
        Class<? extends IHasId<?>> cls = clss.get(i);
        if (IOrId.class.isAssignableFrom(cls)) {
          rfrc++;
          String queryMaxId = "select max(IID) as MAXID from "
            + cls.getSimpleName().toUpperCase() + ";";
          Integer maxId = this.rdb.evInt(queryMaxId, "MAXID");
          if (maxId != null) {
            maxId++;
            String querySec = "alter sequence " + cls.getSimpleName()
              .toUpperCase() + "_IID_SEQ restart with " + maxId + ";";
            this.rdb.exec(querySec);
          }
        }
      }
      this.rdb.commit();
      Writer wri = (Writer) pRvs.get("htmWri");
      if (wri != null) {
        wri.write("<h4>" + new Date().toString() + ", "
        + PsgAft.class.getSimpleName()
          + ", total refreshed ID sequences " + rfrc + "</h4>");
      }
      this.log.info(pRvs, PsgAft.class, "Finish refreshing sequences auto-ID.");
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
}
