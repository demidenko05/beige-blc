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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.fct.IFcFlCvFd;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.SrvClVl;

/**
 * <p>Service that fills/converts given
 * column values with given entity.</p>
 *
 * @author Yury Demidenko
 */
public class FilCvEnt implements IFilCvEnt {

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
  private IFcFlCvFd fctFilFld;

  /**
   * <p>Generating insert/update and CV service.</p>
   **/
  private SrvClVl srvClVl;

  /**
   * <p>Fills/converts given column values with given entity.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt entity
   * @param pCv column values
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> void fill(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pEnt,
      final ColVals pCv) throws Exception {
    String[] ndFds = (String[]) pVs.get("ndFds");
    boolean ndVr = true;
    if (ndFds != null) {
      ndVr = Arrays.binarySearch(ndFds, IHasId.VERNM) >= 0;
    }
    if (ndVr) {
      String verAlg = this.setng.lazClsStg(pEnt.getClass(), IHasId.VERALGNM);
      if (verAlg == null) {
        throw new ExcCode(ExcCode.WRCN, "There is no vrAlg for class: "
          + pEnt.getClass());
      }
      Long vlNew = null;
      if ("1".equals(verAlg)) {
        vlNew = new Date().getTime();
      } else {
        if (pEnt.getVer() == null) {
          vlNew = 1L;
        } else {
          vlNew = pEnt.getVer() + 1L;
        }
      }
      if (pCv.getLongs() == null) {
        pCv.setLongs(new HashMap<String, Long>());
      }
      pCv.getLongs().put(IHasId.VERNM, vlNew);
      pCv.setOldVer(pEnt.getVer());
      pEnt.setVer(vlNew);
    }
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 7230);
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      fillFd(pRvs, pVs, pEnt, pCv, fdNm, dbgSh);
    }
    for (String fdNm : this.setng.lazFldNms(pEnt.getClass())) {
      if (!IHasId.VERNM.equals(fdNm)) {
        boolean ndFl = true;
        if (ndFds != null) {
          ndFl = Arrays.binarySearch(ndFds, fdNm) >= 0;
        }
        if (ndFl) {
          fillFd(pRvs, pVs, pEnt, pCv, fdNm, dbgSh);
        }
      }
    }
    if (dbgSh) {
      this.log.debug(pRvs, getClass(), "Filled CV: "
        + this.srvClVl.str(pEnt.getClass(), pCv));
    }
  }

  /**
   * <p>Fills/converts given column values with given entity.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields {id, ver, nme} not null.
   * @param pEnt entity
   * @param pCv column values
   * @param pFdNm field name
   * @param pIsDbgSh if show debug messages
   * @throws Exception - an exception
   **/
  public final <T extends IHasId<?>> void fillFd(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pEnt, final ColVals pCv,
      final String pFdNm, final boolean pIsDbgSh) throws Exception {
    String filFdNm = this.hldFilFdNms.get(pEnt.getClass(), pFdNm);
    IFilCvFld filFl = this.fctFilFld.laz(pRvs, filFdNm);
    if (pIsDbgSh) {
      this.log.debug(pRvs, FilCvEnt.class,
        "Filling CV fdNm/cls/filler: " + pFdNm + "/" + pEnt.getClass()
          .getSimpleName() + "/" + filFl.getClass().getSimpleName());
    }
    filFl.fill(pRvs, pVs, pEnt, pFdNm, pCv);
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
   * @param pHdFlFdNms reference
   **/
  public final void setHldFilFdNms(final IHlNmClSt pHdFlFdNms) {
    this.hldFilFdNms = pHdFlFdNms;
  }

  /**
   * <p>Getter for fctFilFld.</p>
   * @return IFcFlCvFd
   **/
  public final IFcFlCvFd getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(final IFcFlCvFd pFctFilFld) {
    this.fctFilFld = pFctFilFld;
  }

  /**
   * <p>Getter for srvClVl.</p>
   * @return SrvClVl
   **/
  public final SrvClVl getSrvClVl() {
    return this.srvClVl;
  }

  /**
   * <p>Setter for srvClVl.</p>
   * @param pSrvClVl reference
   **/
  public final void setSrvClVl(final SrvClVl pSrvClVl) {
    this.srvClVl = pSrvClVl;
  }
}
