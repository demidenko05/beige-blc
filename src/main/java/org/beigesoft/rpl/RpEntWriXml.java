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
import java.io.Writer;
import java.lang.reflect.Method;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctCnToSt;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.ICnToSt;

/**
 * <p>Service that writes given entity into given stream (writer)
 * in XML format.
 * New version uses same parameter presentation standard as HTML,
 * except String non-null values are XML escaped
 * and date is in milliseconds, BigDecimal is also non-formatted.</p>
 *
 * @author Yury Demidenko
 */
public class RpEntWriXml implements IRpEntWri {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Converters fields factory.</p>
   */
  private IFctCnToSt fctCnvFld;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Fields converters names holder.</p>
   **/
  private IHlNmClSt hldNmFdCn;

  /**
   * <p>Writes given entity into given stream (writer) in XML format.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars
   * @param pEnt object
   * @param pWri writer
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> void write(final Map<String, Object> pRvs,
    final T pEnt, final Writer pWri) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6501 && this.log.getDbgCl() > 6499;
    if (isDbgSh) {
      this.log.debug(pRvs, RpEntWriXml.class, "Writing entity to XML: "
        + pEnt.getClass());
    }
    pWri.write("<entity class=\"" + pEnt.getClass().getCanonicalName()
      + "\"\n");
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      writeFld(pRvs, pEnt, pWri, fdNm);
    }
    for (String fdNm : this.setng.lazFldNms(pEnt.getClass())) {
      writeFld(pRvs, pEnt, pWri, fdNm);
    }
    pWri.write("/>\n");
  }

  /**
   * <p>Writes given field into given stream (writer) in XML format.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars
   * @param pEnt object
   * @param pWri writer
   * @param pFdNm field name
   * @throws Exception - an exception
   **/
  private <T extends IHasId<?>> void writeFld(final Map<String, Object> pRvs,
    final T pEnt, final Writer pWri, final String pFdNm) throws Exception {
    Method getter = this.hldGets.get(pEnt.getClass(), pFdNm);
    Object fdVl = getter.invoke(pEnt);
    String fdVlSt;
    if (fdVl == null) {
      fdVlSt = "";
    } else {
      String cnNm = this.hldNmFdCn.get(pEnt.getClass(), pFdNm);
      @SuppressWarnings("unchecked")
      ICnToSt<Object> flCn = (ICnToSt<Object>) this.fctCnvFld
        .laz(pRvs, cnNm);
      fdVlSt = flCn.conv(pRvs, fdVl);
    }
    pWri.write(" " + pFdNm + "=\"" + fdVlSt + "\"\n");
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
   * <p>Getter for fctCnvFld.</p>
   * @return IFctCnToSt
   **/
  public final IFctCnToSt getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(final IFctCnToSt pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }

  /**
   * <p>Getter for hldGets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHlNmClMt pHldGets) {
    this.hldGets = pHldGets;
  }

  /**
   * <p>Getter for hldNmFdCn.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldNmFdCn() {
    return this.hldNmFdCn;
  }

  /**
   * <p>Setter for hldNmFdCn.</p>
   * @param pHldNmFdCn reference
   **/
  public final void setHldNmFdCn(final IHlNmClSt pHldNmFdCn) {
    this.hldNmFdCn = pHldNmFdCn;
  }
}
