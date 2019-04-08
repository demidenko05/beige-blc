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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.LvDep;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that fill object(entity) from DB result-set.
 * Entity often has owned entities, and RS may has its (OE) several
 * fields (not only ID).</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FilEntRs<RS> implements IFilObj<IRecSet<RS>> {

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
  private IFctNm<IFilFld<IRecSet<RS>>> fctFilFld;

  /**
   * <p>Fill entity from DB recods-set.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pEnt Entity to fill
   * @param pRs - request data
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt,
      final IRecSet<RS> pRs) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7001 && this.log.getDbgCl() > 6999;
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
    LvDep clvDep;
    if (lvDeps == null) { //start main(root) entity:
      lvDeps = new ArrayList<LvDep>();
      clvDep = new LvDep();
    Integer dpLv = (Integer) pVs.get(pEnt.getClass().getSimpleName() + "dpLv");
      if (dpLv != null) { //custom root branch deep level
        clvDep.setDep(dpLv);
      }
      lvDeps.add(clvDep);
      pVs.put("lvDeps", lvDeps);
      if (isDbgSh) {
        this.log.debug(pRqVs, getClass(), "Start fill root entity/DL/CL: "
          + pEnt.getClass() + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
      List<String> tbAls = new ArrayList<String>();
      pVs.put("tbAls", tbAls);
      if (isDbgSh) {
        this.log.debug(pRqVs, getClass(), "tbAls created");
      }
    } else {
      clvDep = lvDeps.get(lvDeps.size() - 1);
    }
    String[] ndFds = (String[]) pVs.
      get(pEnt.getClass().getSimpleName() + "ndFds");
    if (ndFds != null && isDbgSh) {
      this.log.debug(pRqVs, getClass(), "Needed fields entity: "
        + pEnt.getClass() + "/" + Arrays.toString(ndFds));
    }
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      boolean isNd = true;
      if (ndFds != null) {
        isNd = Arrays.binarySearch(ndFds, fdNm) >= 0;
      }
      if (isNd) {
        fillFld(pRqVs, pVs, pEnt, pRs, fdNm, isDbgSh);
      }
    }
    if (clvDep.getCur() < clvDep.getDep()) {
      for (String fdNm : this.setng.lazFldNms(pEnt.getClass())) {
        boolean isNd = true;
        if (ndFds != null) {
          isNd = Arrays.binarySearch(ndFds, fdNm) >= 0;
        }
        if (isNd) {
          fillFld(pRqVs, pVs, pEnt, pRs, fdNm, isDbgSh);
        }
      }
    }
    if (lvDeps.size() == 1) { //move down through root DL subentities branch:
      LvDep ld = lvDeps.get(0);
      if (ld.getCur() == 0) { //current is root entity:
        pVs.remove("lvDeps");
        pVs.remove("tbAls");
        if (isDbgSh) {
          this.log.debug(pRqVs, getClass(),
            "Finish filling root entity: " + pEnt.getClass());
        }
      }
    }
  }

  /**
   * <p>Fill entity field from DB recods-set.</p>
   * @param <T> entity type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pEnt Entity to fill
   * @param pRs - request data
   * @param pFdNm field name
   * @param pIsDbgSh show debug msgs
   * @throws Exception - an exception
   **/
  private <T> void fillFld(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pEnt, final IRecSet<RS> pRs,
      final String pFdNm, final boolean pIsDbgSh) throws Exception {
    String filFdNm = this.hldFilFdNms.get(pEnt.getClass(), pFdNm);
    IFilFld<IRecSet<RS>> filFl = this.fctFilFld.laz(pRqVs, filFdNm);
    if (pIsDbgSh) {
      this.log.debug(pRqVs, getClass(),
        "Filling DB fdNm/cls/filler: " + pFdNm + "/" + pEnt.getClass()
          .getSimpleName() + "/" + filFl.getClass().getSimpleName());
    }
    filFl.fill(pRqVs, pVs, pEnt, pRs, pFdNm);
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
   * <p>Getter for fctFilFld.</p>
   * @return IFctNm<IFilFld<IRecSet<RS>>>
   **/
  public final IFctNm<IFilFld<IRecSet<RS>>> getFctFilFld() {
    return this.fctFilFld;
  }

  /**
   * <p>Setter for fctFilFld.</p>
   * @param pFctFilFld reference
   **/
  public final void setFctFilFld(
    final IFctNm<IFilFld<IRecSet<RS>>> pFctFilFld) {
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
}
