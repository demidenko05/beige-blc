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

package org.beigesoft.hnd;

import java.util.Set;
import java.util.Map;
import java.io.OutputStream;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFctPrcFl;
import org.beigesoft.prc.IPrcFl;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Generic transactional handler that responses
 * with a file (PDF, CSV...).</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndFlRpRq<RS> implements IHndFlRpRq {

  /**
   * <p>Standard logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>Database service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>Processors factory.</p>
   **/
  private IFctPrcFl fctPrcFl;

  /**
   * <p>Transaction isolation for file report request handling.</p>
   **/
  private Integer trIsl;

  /**
   * <p>Handle file-report request.</p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs, final IReqDt pRqDt,
    final OutputStream pSous) throws Exception {
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      String nmPrcFl = pRqDt.getParam("prc");
      IPrcFl proc = this.fctPrcFl.laz(pRvs, nmPrcFl);
      proc.process(pRvs, pRqDt, pSous);
      this.rdb.commit();
    } catch (Exception ex) {
      @SuppressWarnings("unchecked")
      Set<IHnTrRlBk> hnsTrRlBk = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
      if (hnsTrRlBk != null) {
        pRvs.remove(IHnTrRlBk.HNSTRRLBK);
        for (IHnTrRlBk hnTrRlBk : hnsTrRlBk) {
          try {
            hnTrRlBk.hndRlBk(pRvs);
          } catch (Exception ex1) {
            this.logStd.error(pRvs, getClass(), "Handler roll back: ", ex1);
          }
        }
      }
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
   * <p>Geter for logStd.</p>
   * @return ILog
   **/
  public final ILog getLogStd() {
    return this.logStd;
  }

  /**
   * <p>Setter for logStd.</p>
   * @param pLogStd reference
   **/
  public final void setLogStd(final ILog pLogStd) {
    this.logStd = pLogStd;
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
   * <p>Getter for fctPrcFl.</p>
   * @return IFctPrcFl
   **/
  public final IFctPrcFl getFctPrcFl() {
    return this.fctPrcFl;
  }

  /**
   * <p>Setter for fctPrcFl.</p>
   * @param pFctPrcFl reference
   **/
  public final void setFctPrcFl(final IFctPrcFl pFctPrcFl) {
    this.fctPrcFl = pFctPrcFl;
  }

  /**
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }
}
