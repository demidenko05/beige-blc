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
import java.io.Reader;
import java.lang.reflect.Constructor;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFcFlFdSt;
import org.beigesoft.log.ILog;
import org.beigesoft.prp.ISetng;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.cnv.IFilFldStr;
import org.beigesoft.srv.IUtlXml;

/**
 * <p>Service that reads/fills standard entity from given stream (reader).</p>
 *
 * @author Yury Demidenko
 */
public class RpEntReadXml implements IRpEntRead<IHasId<?>> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Holder of fillers fields names.</p>
   **/
  private IHlNmClSt hldFilFdNms;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFcFlFdSt fctFilFld;

  /**
   * <p>XML service.</p>
   **/
  private IUtlXml utlXml;

  /**
   * <p>Reads/fills an entity from given stream (reader).</p>
   * @param pRqVs request scoped vars
   * @param pReader reader.
   * @return entity filled/refreshed.
   * @throws Exception - an exception
   **/
  @Override
  public final IHasId<?> read(final Map<String, Object> pRqVs,
    final Reader pReader) throws Exception {
    Map<String, String> attrs = this.utlXml.readAttrs(pRqVs, pReader);
    if (attrs.get("class") == null) {
     throw new ExcCode(ExcCode.WRCN, "There is no class attribute for entity!");
    }
    @SuppressWarnings("unchecked")
    Class<IHasId<?>> cls = (Class<IHasId<?>>) Class
      .forName(attrs.get("class"));
    Constructor<IHasId<?>> constructor = cls.getDeclaredConstructor();
    IHasId<?> ent = constructor.newInstance();
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6502 && this.log.getDbgCl() > 6500;
    if (isDbgSh) {
      this.log.debug(pRqVs, RpEntReadXml.class, "Filling entity from XML: "
        + cls);
    }
    for (String flNm : this.setng.lazIdFldNms(cls)) {
      String filFdNm = this.hldFilFdNms.get(cls, flNm);
      IFilFldStr filFl = this.fctFilFld.laz(pRqVs, filFdNm);
      filFl.fill(pRqVs, null, ent, flNm, attrs.get(flNm));
    }
    for (String flNm : this.setng.lazFldNms(cls)) {
      String filFdNm = this.hldFilFdNms.get(cls, flNm);
      IFilFldStr filFl = this.fctFilFld.laz(pRqVs, filFdNm);
      filFl.fill(pRqVs, null, ent, flNm, attrs.get(flNm));
    }
    return ent;
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
   * <p>Getter for hldFilFdNms.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldFilFdNms() {
    return this.hldFilFdNms;
  }

  /**
   * <p>Setter for hldFilFdNms.</p>
   * @param pHldFilFdNms reference
   **/
  public final void setHldFilFdNms(final IHlNmClSt pHldFilFdNms) {
    this.hldFilFdNms = pHldFilFdNms;
  }

  /**
   * <p>Getter for fctFilFld.</p>
   * @return IFcFlFdSt
   **/
  public final IFcFlFdSt getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(final IFcFlFdSt pFctFilFld) {
    this.fctFilFld = pFctFilFld;
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
}
