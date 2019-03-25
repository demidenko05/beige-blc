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

package org.beigesoft.cnv;

import java.util.Map;
import java.util.Set;

import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.hld.IHld;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.mdl.IReqDt;

/**
 * <p>Service that fill object(entity) from HTTP request.</p>
 *
 * @author Yury Demidenko
 */
public class FilEntRq implements IFilEnt<IReqDt> {

  /**
   * <p>Logger.</p>
   **/
  private ILog logger;

  /**
   * <p>Holder of entities fields names. It's a delegate to UVD-settings.</p>
   **/
  private IHld<Class<?>, Set<String>> hldFlNms;

  /**
   * <p>Holder of  fillersfields names.</p>
   **/
  private IHldNm<Class<?>, String> hldFilFdNms;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFctNm<IFilFld<String>> fctFilFld;

  /**
   * <p>Fill entity from pequest.</p>
   * @param T entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pEnt Entity to fill
   * @param pReq - request
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pRqVs,
    final T pEnt, final IReqDt pReq) throws Exception {
    boolean isDbgSh = this.logger.getDbgSh(this.getClass())
      && this.logger.getDbgFl() < 5001 && this.logger.getDbgCl() > 4999;
    for (String flNm : this.hldFlNms.get(pEnt.getClass())) {
      String valStr = pReq.getParam(pEnt.getClass().getSimpleName()
        + "." + flNm); // standard
      if (valStr != null) { // e.g. Boolean checkbox or none-editable
        String filFdNm = this.hldFilFdNms.get(pEnt.getClass(), flNm);
        IFilFld<String> filFl = this.fctFilFld.laz(pRqVs, filFdNm);
        if (isDbgSh) {
          this.logger.debug(pRqVs, FilEntRq.class,
        "Filling fieldNm/inClass/value/filler: " + flNm + "/" + pEnt.getClass()
      .getSimpleName() + "/" + valStr + "/" + filFl.getClass().getSimpleName());
        }
        filFl.fill(pRqVs, pEnt, valStr, flNm);
      }
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for logger.</p>
   * @return ILog
   **/
  public final ILog getLogger() {
    return this.logger;
  }

  /**
   * <p>Setter for logger.</p>
   * @param pLogger reference
   **/
  public final void setLogger(final ILog pLogger) {
    this.logger = pLogger;
  }

  /**
   * <p>Getter for fctFilFld.</p>
   * @return IFctNm<IFilFld<String>>
   **/
  public final IFctNm<IFilFld<String>> getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(final IFctNm<IFilFld<String>> pFctFilFld) {
    this.fctFilFld = pFctFilFld;
  }

  /**
   * <p>Getter for hldFilFdNms.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldFilFdNms() {
    return this.hldFilFdNms;
  }

  /**
   * <p>Setter for hldFilFdNms.</p>
   * @param pHldFilFdNms reference
   **/
  public final void setHldFilFdNms(
    final IHldNm<Class<?>, String> pHldFilFdNms) {
    this.hldFilFdNms = pHldFilFdNms;
  }

  /**
   * <p>Getter for hldFlNms.</p>
   * @return IHld<Class<?>, Set<String>>
   **/
  public final IHld<Class<?>, Set<String>> getHldFlNms() {
    return this.hldFlNms;
  }

  /**
   * <p>Setter for hldFlNms.</p>
   * @param pHldFlNms reference
   **/
  public final void setHldFlNms(final IHld<Class<?>, Set<String>> pHldFlNms) {
    this.hldFlNms = pHldFlNms;
  }
}
