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
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;

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
  implements IFilFldRs<RS> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHlNmClMt hldSets;

  /**
   * <p>Filler entity factory.</p>
   */
  private IFilEntRs<RS> filEnt;

  /**
   * <p>Fills object's field.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars, not null
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt Entity to fill, not null
   * @param pFlNm Field name, not null
   * @param pRs record-set, not null
   * @return if not-null value
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> boolean fill(
    final Map<String, Object> pRvs, final Map<String, Object> pVs, final T pEnt,
      final String pFlNm, final IRecSet<RS> pRs) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7002 && this.log.getDbgCl() > 7000;
    @SuppressWarnings("unchecked")
    Class<E> fdCls = (Class<E>) this.hldFdCls.get(pEnt.getClass(), pFlNm);
    E val = fdCls.newInstance();
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
    LvDep clvDep;
    Integer dpLv = (Integer) pVs.get(fdCls.getSimpleName() + "dpLv");
    if (dpLv != null) { //custom level for subentity(owned) subbranch:
      clvDep = new LvDep();
      clvDep.setDep(dpLv);
      lvDeps.add(clvDep);
      if (isDbgSh) {
        this.log.debug(pRvs, getClass(), "Start fill custDL subent/DL/CL: "
            + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
    } else { //entering into new sub/branch's sub-entity:
      clvDep = lvDeps.get(lvDeps.size() - 1);
      clvDep.setCur(clvDep.getCur() + 1);
      if (isDbgSh) {
        this.log.debug(pRvs, getClass(), "Start subent/DL/CL: "
            + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
    }
    if (lvDeps.size() > 1) { //sub-branch, main branch level change:
      lvDeps.get(0).setCur(lvDeps.get(0).getCur() + 1);
      if (isDbgSh) {
        this.log.debug(pRvs, getClass(), "Main branch UP DL/CL: "
          + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      }
    }
    @SuppressWarnings("unchecked")
    List<String> tbAls = (List<String>) pVs.get("tbAls");
    //WrhEnr.wpFr.wrh and WrhEnr.wpTo.wrh
    Integer cuFdIdx = (Integer) pVs.get("cuFdIdx");
    String tbAl = pFlNm.toUpperCase() + lvDeps.get(0).getCur() + cuFdIdx;
    tbAls.add(tbAl);
    this.filEnt.fill(pRvs, pVs, val, pRs);
    tbAls.remove(tbAl);
    if (lvDeps.size() > 1) { //move down through custom DL subentities branch:
      LvDep ld = lvDeps.get(lvDeps.size() - 1);
      if (ld.getCur() == 0) { //ending custom DL subentity:
        lvDeps.remove(lvDeps.size() - 1);
        if (isDbgSh) {
          this.log.debug(pRvs, getClass(),
            "Finish custom DL root subentity: " + fdCls);
        }
      } else { //finish subentity:
        ld.setCur(ld.getCur() - 1);
        if (isDbgSh) {
          this.log.debug(pRvs, getClass(),
            "Finish custom DL subentity: " + fdCls);
        }
      }
      //sub-branch, main branch level change:
      lvDeps.get(0).setCur(lvDeps.get(0).getCur() - 1);
      if (isDbgSh) {
        this.log.debug(pRvs, getClass(), "Main branch DOWN DL/CL: "
          + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      }
    } else {  //move down through root DL subentities branch:
      LvDep ld = lvDeps.get(0);
      if (ld.getCur() > 0) { //finish subentity:
        ld.setCur(ld.getCur() - 1);
        if (isDbgSh) {
          this.log.debug(pRvs, getClass(),
            "Finish custom subentity: " + fdCls);
        }
      }
    }
    if (val.getIid() == null) {
      val = null;
    }
    Method setr = this.hldSets.get(pEnt.getClass(), pFlNm);
    setr.invoke(pEnt, val);
    return val != null;
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
   * @return IHlNmClCl
   **/
  public final IHlNmClCl getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHlNmClCl pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  /**
   * <p>Getter for hldSets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHlNmClMt pHldSets) {
    this.hldSets = pHldSets;
  }

  /**
   * <p>Getter for filEnt.</p>
   * @return IFilEntRs<RS>
   **/
  public final IFilEntRs<RS> getFilEnt() {
    return this.filEnt;
  }

  /**
   * <p>Setter for filEnt.</p>
   * @param pFilEnt reference
   **/
  public final void setFilEnt(final IFilEntRs<RS> pFilEnt) {
    this.filEnt = pFilEnt;
  }
}
