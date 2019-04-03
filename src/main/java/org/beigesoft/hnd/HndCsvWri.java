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

import java.util.Map;
import java.util.List;
import java.math.BigDecimal;
import java.io.OutputStream;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.ICsvDtRet;
import org.beigesoft.srv.ICsvWri;

/**
 * <p>Service that retrieves data by given CSV method and retriever,
 *  then writes CSV data to given output stream.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HndCsvWri<RS> implements IHndFlRpRq {

  /**
   * <p>Database service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm<RS> orm;

  /**
   * <p>CSV writer service.</p>
   **/
  private ICsvWri csvWri;

  /**
   * <p>Retrs map.</p>
   **/
  private Map<String, ICsvDtRet> retrs;

  /**
   * <p>Handle file-report request.</p>
   * @param pRqVs Request scoped variables
   * @param pRqDt Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRqVs,
    final IReqDt pRqDt, final OutputStream pSous) throws Exception {
    String csMtIdStr = pRqDt.getParam("csMt");
    Long csMtId = Long.parseLong(csMtIdStr);
    String priCtIdStr = pRqDt.getParam("priCt");
    Long priCtId = Long.parseLong(priCtIdStr);
    pRqVs.put("priCtId", priCtId);
    String unPriStr = pRqDt.getParam("unPri");
    if (unPriStr != null && !"".equals(unPriStr)) {
      BigDecimal unPri = new BigDecimal(unPriStr);
      if (unPri.compareTo(BigDecimal.ZERO) != 0) {
        pRqVs.put("unPri", unPri);
      }
    }
    String optQuanStr = pRqDt.getParam("optQuan");
    if (optQuanStr != null && !"".equals(optQuanStr)) {
      BigDecimal optQuan = new BigDecimal(optQuanStr);
      pRqVs.put("optQuan", optQuan);
    }
    CsvMth csMt = null;
    List<List<Object>> data = null;
    ICsvDtRet ret = null;
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(IRdb.TRRC);
      this.rdb.begin();
      csMt = getOrm().retEntId(pRqVs, null, CsvMth.class, csMtId);
      List<CsvCl> cols = getOrm().retLstCnd(pRqVs, null,
        CsvCl.class, "where ITSOWNER=" + csMt.getIid());
      csMt.setClns(cols);
      ret = this.retrs.get(csMt.getRtrNm());
      if (ret == null) {
        throw new ExcCode(ExcCode.WRPR,
          "Can't find retriever " + csMt.getRtrNm());
      }
      data = ret.retData(pRqVs);
      this.rdb.commit();
    } catch (Exception ex) {
      this.rdb.rollBack();
      throw ex;
    } finally {
      this.rdb.release();
    }
    if (data != null) {
      this.csvWri.write(pRqVs, data, csMt, pSous);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for retrs.</p>
   * @return Map<String, ICsvDtRet>
   **/
  public final Map<String, ICsvDtRet> getRetrs() {
    return this.retrs;
  }

  /**
   * <p>Setter for retrs.</p>
   * @param pRetrs reference
   **/
  public final void setRetrs(
    final Map<String, ICsvDtRet> pRetrs) {
    this.retrs = pRetrs;
  }

  /**
   * <p>Getter for orm.</p>
   * @return IOrm<RS>
   **/
  public final IOrm<RS> getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm<RS> pOrm) {
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
}
