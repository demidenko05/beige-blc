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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.LvDep;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFcCnRsFdv;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;

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
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Fields converters names holder.</p>
   **/
  private IHlNmClSt hldNmFdCn;

  /**
   * <p>Factory simple converters.</p>
   **/
  private IFcCnRsFdv<RS> fctCnvFld;

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
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 7215);
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
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Start fill custDL subent/DL/CL: "
            + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
    } else { //entering into new sub/branch's sub-entity:
      clvDep = lvDeps.get(lvDeps.size() - 1);
      clvDep.setCur(clvDep.getCur() + 1);
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Start subent/DL/CL: "
            + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
      }
    }
    if (lvDeps.size() > 1) { //sub-branch, main branch level change:
      lvDeps.get(0).setCur(lvDeps.get(0).getCur() + 1);
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Main branch UP DL/CL: "
          + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      }
    }
    @SuppressWarnings("unchecked")
    List<String> tbAls = (List<String>) pVs.get("tbAls");
    if (clvDep.getCur() < clvDep.getDep()) {
      //WrhEnr.wpFr.wrh and WrhEnr.wpTo.wrh
      Integer cuFdIdx = (Integer) pVs.get("cuFdIdx");
      String tbAl = pFlNm.toUpperCase() + lvDeps.get(0).getCur() + cuFdIdx;
      tbAls.add(tbAl);
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Added tbAl/cls: " + tbAl
          + "/" + fdCls);
      }
      this.filEnt.fill(pRvs, pVs, val, pRs);
      tbAls.remove(tbAl);
    } else { //only ID without joins:
      fillId(pRvs, pVs, val, pFlNm, pRs, tbAls);
    }
    if (lvDeps.size() > 1) { //move down through custom DL subentities branch:
      LvDep ld = lvDeps.get(lvDeps.size() - 1);
      if (ld.getCur() == 0) { //ending custom DL subentity:
        lvDeps.remove(lvDeps.size() - 1);
        if (dbgSh) {
          this.log.debug(pRvs, getClass(),
            "Finish custom DL root subentity: " + fdCls);
        }
      } else { //finish subentity:
        ld.setCur(ld.getCur() - 1);
        if (dbgSh) {
          this.log.debug(pRvs, getClass(),
            "Finish custom DL subentity: " + fdCls);
        }
      }
      //sub-branch, main branch level change:
      lvDeps.get(0).setCur(lvDeps.get(0).getCur() - 1);
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Main branch DOWN DL/CL: "
          + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      }
    } else {  //move down through root DL subentities branch:
      LvDep ld = lvDeps.get(0);
      if (ld.getCur() > 0) { //finish subentity:
        ld.setCur(ld.getCur() - 1);
        if (dbgSh) {
          this.log.debug(pRvs, getClass(), "Finish custom subentity: " + fdCls);
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

  //Utils:
  /**
   * <p>Fills given entity ID from result-set.</p>
   * @param pRvs request scoped vars, not null
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt entity
   * @param pFlNm main entity subentity field name, not null
   * @param pRs record-set, not null
   * @param pTbAls tables aliases
   * @return if ID null
   * @throws Exception - an exception
   **/
  public final boolean fillId(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final IHasId<?> pEnt, final String pFlNm,
      final IRecSet<RS> pRs, final List<String> pTbAls) throws Exception {
    boolean idNull = false;
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 7216);
    List<String> fdIdNms = this.setng.lazIdFldNms(pEnt.getClass());
    if (fdIdNms.size() > 1) {
      throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID - "  + pEnt);
    }
    String idNm = fdIdNms.get(0);
    Class<?> fdCls = this.hldFdCls.get(pEnt.getClass(), idNm);
    if (IHasId.class.isAssignableFrom(fdCls)) {
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Deep subentity idNm/idCls: "
          + idNm + "/" + fdCls);
      }
      @SuppressWarnings("unchecked")
      Class<IHasId<?>> fdeCls = (Class<IHasId<?>>) fdCls;
      IHasId<?> oe = fdeCls.newInstance();
      idNull = fillId(pRvs, pVs, oe, pFlNm, pRs, pTbAls);
      if (idNull) {
        oe = null;
      }
      Method setr = this.hldSets.get(pEnt.getClass(), idNm);
      setr.invoke(pEnt, oe);
    } else {
      String cnNm = this.hldNmFdCn.get(pEnt.getClass(), idNm);
      @SuppressWarnings("unchecked")
      ICnvRsFdv<Object, RS> flCnv =
        (ICnvRsFdv<Object, RS>) this.fctCnvFld.laz(pRvs, cnNm);
      String clNm;
      if (pTbAls.size() > 0) {
        clNm = pTbAls.get(pTbAls.size() - 1) + pFlNm.toUpperCase();
      } else {
        clNm = pFlNm.toUpperCase();
      }
      if (dbgSh) {
        this.log.debug(pRvs, getClass(), "Column alias/fdCls: "
          + clNm + "/" + pEnt.getClass());
      }
      Object id = flCnv.conv(pRvs, pVs, pRs, clNm);
      Method setr = this.hldSets.get(pEnt.getClass(), idNm);
      setr.invoke(pEnt, id);
      if (id == null) {
        idNull = true;
      }
    }
    return idNull;
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

  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFcCnRsFdv<RS>
   **/
  public final IFcCnRsFdv<RS> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFcCnRsFdv<RS> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
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
