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
import java.util.HashMap;
import java.util.List;
import java.io.OutputStream;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;
import org.beigesoft.fct.IFcCsvDrt;
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.ICsvDtRet;
import org.beigesoft.srv.ICsvWri;

/**
 * <p>Service that retrieves data by given CSV method and retriever,
 * then writes CSV data to given output stream. It requires CsvMth parameter
 * that holds CsvMth ID.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndCsvWri<RS> implements IHndFlRpRq {

  /**
   * <p>Standard logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>Database service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>CSV writer service.</p>
   **/
  private ICsvWri csvWri;

  /**
   * <p>Retrievers factory.</p>
   **/
  private IFcCsvDrt fctRet;

  /**
   * <p>Handle file-report request.</p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final OutputStream pSous) throws Exception {
    String csMtIdStr = pRqDt.getParam("CsvMth");
    Long csMtId = Long.parseLong(csMtIdStr);
    CsvMth csMt = null;
    List<List<Object>> data = null;
    ICsvDtRet ret = null;
    Map<String, Object> vs = new HashMap<String, Object>();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(IRdb.TRRUC);
      this.rdb.begin();
      CsvMth csMtt = new CsvMth();
      csMtt.setIid(csMtId);
      csMt = getOrm().retEnt(pRvs, vs, csMtt);
      List<CsvCl> cols = getOrm().retLstCnd(pRvs, vs,
        CsvCl.class, "where OWNR=" + csMt.getIid());
      csMt.setClns(cols);
      ret = this.fctRet.laz(pRvs, csMt.getRtrNm());
      if (ret == null) {
        throw new ExcCode(ExcCode.WR,
          "Can't find retriever " + csMt.getRtrNm());
      }
      data = ret.retData(pRvs, pRqDt);
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
    if (data != null) {
      this.csvWri.write(pRvs, data, csMt, pSous);
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
   * <p>Getter for csvWri.</p>
   * @return ICsvWri
   **/
  public final ICsvWri getCsvWri() {
    return this.csvWri;
  }

  /**
   * <p>Setter for csvWri.</p>
   * @param pCsvWri reference
   **/
  public final void setCsvWri(final ICsvWri pCsvWri) {
    this.csvWri = pCsvWri;
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
   * <p>Getter for fctRet.</p>
   * @return IFcCsvDrt
   **/
  public final IFcCsvDrt getFctRet() {
    return this.fctRet;
  }

  /**
   * <p>Setter for fctRet.</p>
   * @param pFctRet reference
   **/
  public final void setFctRet(final IFcCsvDrt pFctRet) {
    this.fctRet = pFctRet;
  }
}
