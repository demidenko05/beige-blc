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
import org.beigesoft.fct.IFctNm;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that fills/converts given
 * column values with given entity.</p>
 *
 * @param <S> source type
 * @param <ID> ID type
 * @author Yury Demidenko
 */
public class FilCvEnt<S extends IHasId<ID>, ID> implements IFiller<S, ColVals> {

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
  private IHldNm<Class<?>, String> hldFilFdNms;

  /**
   * <p>Fillers fields factory.</p>
   */
  private IFctNm<IFilNm<S, ColVals>> fctFilFld;

  /**
   * <p>Fills/converts given column values with given entity.</p>
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields ndFds {id, ver, nme}.
   * @param pEnt entity
   * @param pClVl column values
   * @throws Exception - an exception
   **/
  @Override
  public final void fill(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final S pEnt,
      final ColVals pClVl) throws Exception {
    String[] ndFds = (String[]) pVs.get("ndFds");
    boolean ndVr = true;
    if (ndFds != null) {
      ndVr = Arrays.binarySearch(ndFds, IHasId.VERNM) >= 0;
    }
    if (ndVr) {
      String verAlg = this.setng
        .lazClsStg(pEnt.getClass(), IHasId.VERALGNM);
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
      if (pClVl.getLongs() == null) {
        pClVl.setLongs(new HashMap<String, Long>());
      }
      pClVl.getLongs().put(IHasId.VERNM, vlNew);
      pClVl.setOldVer(pEnt.getVer());
      pEnt.setVer(vlNew);
    }
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7021 && this.log.getDbgCl() > 7019;
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      fillFd(pRqVs, pVs, pEnt, pClVl, fdNm, isDbgSh);
    }
    for (String fdNm : this.setng.lazFldNms(pEnt.getClass())) {
      if (!IHasId.VERNM.equals(fdNm)) {
        boolean ndFl = true;
        if (ndFds != null) {
          ndFl = Arrays.binarySearch(ndFds, fdNm) >= 0;
        }
        if (ndFl) {
          fillFd(pRqVs, pVs, pEnt, pClVl, fdNm, isDbgSh);
        }
      }
    }
  }

  /**
   * <p>Fills/converts given column values with given entity.</p>
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars, e.g. needed fields ndFds {id, ver, nme}.
   * @param pEnt entity
   * @param pClVl column values
   * @param pFdNm field name
   * @param pIsDbgSh if show debug messages
   * @throws Exception - an exception
   **/
  public final void fillFd(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final S pEnt, final ColVals pClVl,
      final String pFdNm, final boolean pIsDbgSh) throws Exception {
    String filFdNm = this.hldFilFdNms.get(pEnt.getClass(), pFdNm);
    IFilNm<S, ColVals> filFl = this.fctFilFld.laz(pRqVs, filFdNm);
    if (pIsDbgSh) {
      this.log.debug(pRqVs, FilCvEnt.class,
        "Filling CV fdNm/cls/filler: " + pFdNm + "/" + pEnt.getClass()
          .getSimpleName() + "/" + filFl.getClass().getSimpleName());
    }
    filFl.fill(pRqVs, pVs, pEnt, pClVl, pFdNm);
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
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldFilFdNms() {
    return this.hldFilFdNms;
  }

  /**
   * <p>Setter for hldFilFdNms.</p>
   * @param pHdFlFdNms reference
   **/
  public final void setHldFilFdNms(final IHldNm<Class<?>, String> pHdFlFdNms) {
    this.hldFilFdNms = pHdFlFdNms;
  }

  /**
   * <p>Getter for fctFilFld.</p>
   * @return IFctNm<IFilNm<S, ColVals>>
   **/
  public final IFctNm<IFilNm<S, ColVals>> getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(final IFctNm<IFilNm<S, ColVals>> pFctFilFld) {
    this.fctFilFld = pFctFilFld;
  }
}
