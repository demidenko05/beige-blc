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

import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.mdl.LvDep;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;

/**
 * <p>Standard service that fills/converts object's field of type IHasId from
 * given DB result-set. It's an owned entity converter.</p>
 *
 * @param <E> owned entity type
 * @param <ID> owned entity ID type
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FilFldHsIdRs<E extends IHasId<ID>, ID, RS>
  implements IFilFld<IRecSet<RS>> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldSets;

  /**
   * <p>Filler entity factory.</p>
   */
  private IFilObj<IRecSet<RS>> filEnt;

  /**
   * <p>Fills object's field.</p>
   * @param <T> object type
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pObj Object to fill, not null
   * @param pRs Source with field value
   * @param pFlNm Field name
   * @throws Exception - an exception
   **/
  @Override
  public final <T> void fill(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final T pObj,
      final IRecSet<RS> pRs, final String pFlNm) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7002 && this.log.getDbgCl() > 7000;
    @SuppressWarnings("unchecked")
    Class<E> flCls = (Class<E>) this.hldFdCls.get(pObj.getClass(), pFlNm);
    E val = flCls.newInstance();
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
    LvDep clvDep;
    String tbAl = null;
    List<String> tbAls = null;
    Integer dpLv = (Integer) pVs.get(flCls.getSimpleName() + "dpLv");
    if (dpLv != null) { //custom level for subentity(owned) subbranch:
      clvDep = new LvDep();
      clvDep.setDep(dpLv);
      lvDeps.add(clvDep);
      if (isDbgSh) {
        this.log.debug(pRqVs, FilEntRs.class, "Start fill custDL subent/DL/CL: "
            + flCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
    } else { //entering into new sub/branch's sub-entity:
      clvDep = lvDeps.get(lvDeps.size() - 1);
      clvDep.setCur(clvDep.getCur() + 1);
      if (isDbgSh) {
        this.log.debug(pRqVs, FilEntRs.class, "Start subent/DL/CL: "
            + flCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
    }
    if (lvDeps.size() > 1) { //sub-branch, main branch level change:
      lvDeps.get(0).setCur(lvDeps.get(0).getCur() + 1);
      this.log.debug(pRqVs, FilEntRs.class, "Main branch UP DL/CL: "
          + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
    }
    tbAl = pFlNm.toUpperCase() + lvDeps.get(0).getCur();
    tbAls = (List<String>) pVs.get("tbAls");
    tbAls.add(tbAl);
    if (isDbgSh) {
      this.log.debug(pRqVs, FilFldHsIdRs.class, "Added tbAl/cls: " + tbAl
        + "/" + flCls);
    }
    this.filEnt.fill(pRqVs, pVs, val, pRs);
    if (tbAl != null) {
      tbAls.remove(tbAl);
      if (isDbgSh) {
        this.log.debug(pRqVs, FilFldHsIdRs.class, "Removed tbAl/cls: " + tbAl
          + "/" + flCls);
      }
    }
    if (lvDeps.size() > 1) { //move down through custom DL subentities branch:
      LvDep ld = lvDeps.get(lvDeps.size() - 1);
      if (ld.getCur() == 0) { //ending custom DL subentity:
        lvDeps.remove(lvDeps.size() - 1);
        if (isDbgSh) {
          this.log.debug(pRqVs, FilEntRs.class,
            "Finish custom DL root subentity: " + flCls);
        }
      } else { //finish subentity:
        ld.setCur(ld.getCur() - 1);
        if (isDbgSh) {
          this.log.debug(pRqVs, FilEntRs.class,
            "Finish custom DL subentity: " + flCls);
        }
      }
      //sub-branch, main branch level change:
      lvDeps.get(0).setCur(lvDeps.get(0).getCur() - 1);
      this.log.debug(pRqVs, FilEntRs.class, "Main branch DOWN DL/CL: "
          + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
    } else {  //move down through root DL subentities branch:
      LvDep ld = lvDeps.get(0);
      if (ld.getCur() > 0) { //finish subentity:
        ld.setCur(ld.getCur() - 1);
        if (isDbgSh) {
          this.log.debug(pRqVs, FilEntRs.class,
            "Finish custom subentity: " + flCls);
        }
      }
    }
    if (val.getIid() == null) {
      val = null;
    }
    Method setr = this.hldSets.get(pObj.getClass(), pFlNm);
    setr.invoke(pObj, val);
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
   * <p>Getter for hldFdCls.</p>
   * @return IHldNm<Class<?>, Class<?>>
   **/
  public final IHldNm<Class<?>, Class<?>> getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHldNm<Class<?>, Class<?>> pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  /**
   * <p>Getter for hldSets.</p>
   * @return IHldNm<Class<?>, Method>
   **/
  public final IHldNm<Class<?>, Method> getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHldNm<Class<?>, Method> pHldSets) {
    this.hldSets = pHldSets;
  }

  /**
   * <p>Getter for filEnt.</p>
   * @return IFilObj<IRecSet<RS>>
   **/
  public final IFilObj<IRecSet<RS>> getFilEnt() {
    return this.filEnt;
  }

  /**
   * <p>Setter for filEnt.</p>
   * @param pFilEnt reference
   **/
  public final void setFilEnt(final IFilObj<IRecSet<RS>> pFilEnt) {
    this.filEnt = pFilEnt;
  }
}
