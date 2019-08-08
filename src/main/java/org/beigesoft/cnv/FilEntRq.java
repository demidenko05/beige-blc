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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFcFlFdSt;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that fill object(entity) from HTTP request.</p>
 *
 * @author Yury Demidenko
 */
public class FilEntRq implements IFilEntRq {

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
   * <p>Fills entity from request.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars, not null
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt Entity to fill, not null
   * @param pRqDt request data, not null
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> void fill(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pEnt,
      final IReqDt pRqDt) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 7220);
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      fillFld(pRvs, pVs, pEnt, pRqDt, fdNm, dbgSh);
    }
    for (String fdNm : this.setng.lazFldNms(pEnt.getClass())) {
      fillFld(pRvs, pVs, pEnt, pRqDt, fdNm, dbgSh);
    }
  }

  /**
   * <p>Fills entity field from request.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt Entity to fill
   * @param pRqDt - request data
   * @param pFdNm field name
   * @param pIsDbgSh show debug msgs
   * @throws Exception - an exception
   **/
  private <T extends IHasId<?>> void fillFld(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pEnt, final IReqDt pRqDt,
      final String pFdNm, final boolean pIsDbgSh) throws Exception {
    String valStr = pRqDt.getParam(pEnt.getClass().getSimpleName()
      + "." + pFdNm); // standard
    if (valStr != null) { // e.g. Boolean checkbox or none-editable
      String filFdNm = this.hldFilFdNms.get(pEnt.getClass(), pFdNm);
      IFilFldStr filFl = this.fctFilFld.laz(pRvs, filFdNm);
      if (pIsDbgSh) {
        this.log.debug(pRvs, FilEntRq.class,
      "Filling fdNm/cls/val/filler: " + pFdNm + "/" + pEnt.getClass()
    .getSimpleName() + "/" + valStr + "/" + filFl.getClass().getSimpleName());
      }
      filFl.fill(pRvs, pVs, pEnt, pFdNm, valStr);
    }
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
  public final void setHldFilFdNms(
    final IHlNmClSt pHldFilFdNms) {
    this.hldFilFdNms = pHldFilFdNms;
  }
}
