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

package org.beigesoft.rdb;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.LvDep;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that generates DML/DDL statements (queries).</p>
 *
 * @author Yury Demidenko
 */
public class SqlQu implements ISqlQu {

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
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Generates DDL Create statement for given entity.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars
   * @param pCls entity class, not null
   * @return Select query in String Buffer
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> String evCreate(
    final Map<String, Object> pRvs,
      final Class<T> pCls) throws Exception {
    StringBuffer sb = new StringBuffer("create table "
      + pCls.getSimpleName().toUpperCase() + "(\n");
    //fields definition:
    boolean isFst = true;
    List<String> idNms = this.setng.lazIdFldNms(pCls);
    for (String fdNm : idNms) {
      String def = this.setng.lazFldStg(pCls, fdNm, DEF);
      if (!def.contains("not null")) {
        def += " not null";
      }
      if (isFst) {
        isFst = false;
      } else {
        sb.append(",\n");
      }
      sb.append(fdNm.toUpperCase() + " " + def);
    }
    for (String fdNm : this.setng.lazFldNms(pCls)) {
      String def = this.setng.lazFldStg(pCls, fdNm, DEF);
      String nul = this.setng.lazFldStg(pCls, fdNm, NUL);
      if ("false".equals(nul) && !def.contains("not null")) {
        def += " not null";
      }
      if (isFst) {
        isFst = false;
      } else {
        sb.append(",\n");
      }
      sb.append(fdNm.toUpperCase() + " " + def);
    }
    //complex primary ID constraint:
    if (idNms.size() > 1) {
      isFst = true;
      sb.append(",\nconstraint pk" + pCls.getSimpleName()
        + " primary key (");
      for (String fdNm : idNms) {
        if (isFst) {
          isFst = false;
        } else {
          sb.append(", ");
        }
        sb.append(fdNm.toUpperCase());
      }
      sb.append(")");
    }
    //foreign ID constraint
    for (String fdNm : idNms) {
      evFrCnstr(pRvs, pCls, fdNm, sb);
    }
    for (String fdNm : this.setng.lazFldNms(pCls)) {
      evFrCnstr(pRvs, pCls, fdNm, sb);
    }
    String cnstr = this.setng.lazClsStg(pCls, CNSTR);
    if (cnstr != null) {
      sb.append(",\n" + cnstr);
    }
    sb.append(");");
    return sb.toString();
  }

  /**
   * <p>Try to add constraint foreign key.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars
   * @param pCls entity class, not null
   * @param pFdNm field name
   * @param pSb String Buffer
   * @throws Exception - an exception
   **/
  public final <T extends IHasId<?>> void evFrCnstr(
    final Map<String, Object> pRvs, final Class<T> pCls,
      final String pFdNm, final StringBuffer pSb) throws Exception {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFdNm);
    if (IHasId.class.isAssignableFrom(fdCls)) {
      @SuppressWarnings("unchecked")
      Class<? extends IHasId<?>> fdeCls = (Class<? extends IHasId<?>>) fdCls;
      List<String> fdIdNms = this.setng.lazIdFldNms(fdeCls);
      if (fdIdNms.size() > 1) {
        throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID!"
          + " cls/fd" + pCls + "/" + pFdNm);
      }
      Class<?> fcs = this.hldFdCls.get(fdeCls, fdIdNms.get(0));
      if (IHasId.class.isAssignableFrom(fcs)) {
        throw new ExcCode(ExcCode.WRCN, "Subentity with double foreign ID!"
          + " cls/fd/fcl/f" + pCls + "/" + pFdNm + "/"
            + fdCls + "/" + fdIdNms.get(0));
      }
      pSb.append(",\nconstraint fk" + pCls.getSimpleName() + pFdNm
        + " foreign key (" + pFdNm.toUpperCase() + ") references ");
      pSb.append(fdCls.getSimpleName().toUpperCase() + "(");
      pSb.append(fdIdNms.get(0).toUpperCase() + ")");
    }
  }

  /**
   * <p>Generates DML Select statement for given entity and vars.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, not null.
   * @param pCls entity class, not null
   * @return Select query in String Buffer
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> StringBuffer evSel(
    final Map<String, Object> pRvs, final Map<String, Object> pVs,
      final Class<T> pCls) throws Exception {
    StringBuffer sb = new StringBuffer("select ");
    StringBuffer sbe = new StringBuffer(" from "
      + pCls.getSimpleName().toUpperCase());
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7101 && this.log.getDbgCl() > 7099;
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = new ArrayList<LvDep>();
    LvDep clvDep = new LvDep();
    Integer dpLv = (Integer) pVs.get(pCls.getSimpleName() + "dpLv");
    if (dpLv != null) { //custom root branch deep level
      clvDep.setDep(dpLv);
    }
    lvDeps.add(clvDep);
    pVs.put("lvDeps", lvDeps);
    if (isDbgSh) {
      this.log.debug(pRvs, getClass(), "Start select root entity/DL/CL: "
        + pCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
    }
    List<String> tbAls = new ArrayList<String>();
    pVs.put("tbAls", tbAls);
    pVs.put("cuFdIdx", Integer.valueOf(0));
    if (isDbgSh) {
      this.log.debug(pRvs, getClass(), "tbAls created");
    }
    makeCls(pRvs, pVs, pCls, sb, sbe, isDbgSh);
    pVs.remove("lvDeps");
    pVs.remove("tbAls");
    pVs.remove("cuFdIdx");
    if (isDbgSh) {
      this.log.debug(pRvs, getClass(),
        "Finish selecting root entity: " + pCls);
    }
    sb.append(sbe);
    return sb;
  }

  /**
   * <p>Generates condition ID for given entity and appends into given
   * String Buffer.</p>
   * @param <T> entity type
   * @param pRvs request scoped vars
   * @param pEnt entity, not null
   * @param pSb String Buffer to put ID condition e.g. "[TBL].IID=2"
   *    or "[TBL].WHOUS=1 and [TBL].ITM=2 and [TBL].UOM=5"
   * @throws Exception - an exception
   **/
  @Override
 public final <T extends IHasId<?>> void evCndId(final Map<String, Object> pRvs,
    final T pEnt, final StringBuffer pSb) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 7102 && this.log.getDbgCl() > 7100;
    boolean isFst = true;
    String als = pEnt.getClass().getSimpleName().toUpperCase() + ".";
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      Object fdVl = null;
      Class<?> fdCls = this.hldFdCls.get(pEnt.getClass(), fdNm);
      Method getter = this.hldGets.get(pEnt.getClass(), fdNm);
      fdVl = getter.invoke(pEnt);
      if (isDbgSh) {
        this.log.debug(pRvs, getClass(), "EV CND ID ent/fd/fcls/vl: "
          + pEnt.getClass() + "/" + fdNm + "/" + fdCls + "/" + fdVl);
      }
      if (fdVl == null) {
        throw new ExcCode(ExcCode.WR, "Entity with NULL ID!");
      }
      if (IHasId.class.isAssignableFrom(fdCls)) {
        @SuppressWarnings("unchecked")
        Class<? extends IHasId<?>> fdeCls = (Class<? extends IHasId<?>>) fdCls;
        List<String> fdIdNms = this.setng.lazIdFldNms(fdeCls);
        if (fdIdNms.size() > 1) {
          throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID!"
            + " cls/fd" + pEnt.getClass() + "/" + fdNm);
        }
        Class<?> fcs = this.hldFdCls.get(fdeCls, fdIdNms.get(0));
        if (IHasId.class.isAssignableFrom(fcs)) {
          throw new ExcCode(ExcCode.WRCN, "Subentity with double foreign ID!"
            + " cls/fd/fcl/f" + pEnt.getClass() + "/" + fdNm + "/"
              + fdCls + "/" + fdIdNms.get(0));
        }
        if (isDbgSh) {
          this.log.debug(pRvs, getClass(), "EV CND ID sent/sfd: "
            + fcs + "/" + fdIdNms.get(0));
        }
        getter = this.hldGets.get(fdeCls, fdIdNms.get(0));
        fdVl = getter.invoke(fdVl);
      }
      if (isFst) {
        isFst = false;
      } else {
        pSb.append(" and ");
      }
      String val;
      if (fdVl instanceof String) {
        val = "'" + fdVl.toString() + "'";
      } else {
        val = fdVl.toString();
      }
      pSb.append(als + fdNm.toUpperCase() + "=" + val);
    }
  }

  /**
   * <p>Makes Select statement for given entity class and vars.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, not null.
   * @param pCls entity class, not null
   * @param pSb string buffer
   * @param pSbe string buffer joints
   * @param pIsDbgSh is show debug messages
   * @throws Exception - an exception
   **/
  public final <T extends IHasId<?>> void makeCls(
    final Map<String, Object> pRvs, final Map<String, Object> pVs,
      final Class<T> pCls, final StringBuffer pSb, final StringBuffer pSbe,
        final boolean pIsDbgSh) throws Exception {
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
    LvDep clvDep = lvDeps.get(lvDeps.size() - 1);
    String[] ndFds = (String[]) pVs.
      get(pCls.getSimpleName() + "ndFds");
    if (ndFds != null && pIsDbgSh) {
      this.log.debug(pRvs, getClass(), "Needed fields entity: "
        + pCls + "/" + Arrays.toString(ndFds));
    }
    boolean isFst = true;
    Integer cuFdIdx = (Integer) pVs.get("cuFdIdx");
    for (String fdNm : this.setng.lazIdFldNms(pCls)) {
      if (isFst) {
        isFst = false;
      } else {
        pSb.append(", ");
      }
      pVs.put("cuFdIdx", cuFdIdx++);
      makeFld(pRvs, pVs, pCls, fdNm, pSb, pSbe, pIsDbgSh);
    }
    if (clvDep.getCur() < clvDep.getDep()) {
      for (String fdNm : this.setng.lazFldNms(pCls)) {
        boolean isNd = true;
        if (ndFds != null) {
          isNd = Arrays.binarySearch(ndFds, fdNm) >= 0;
        }
        if (isNd) {
          if (isFst) {
            isFst = false;
          } else {
            pSb.append(", ");
          }
          pVs.put("cuFdIdx", cuFdIdx++);
          makeFld(pRvs, pVs, pCls, fdNm, pSb, pSbe, pIsDbgSh);
        }
      }
    }
  }

  /**
   * <p>Makes Select statement for given entity class, field and vars.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, not null.
   * @param pCls entity class, not null
   * @param pFdNm field name
   * @param pSb string buffer
   * @param pSbe string buffer joints
   * @param pIsDbgSh is show debug messages
   * @throws Exception - an exception
   **/
  public final <T extends IHasId<?>> void makeFld(
    final Map<String, Object> pRvs, final Map<String, Object> pVs,
      final Class<T> pCls, final String pFdNm, final StringBuffer pSb,
        final StringBuffer pSbe, final boolean pIsDbgSh) throws Exception {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFdNm);
    if (IHasId.class.isAssignableFrom(fdCls)) {
      @SuppressWarnings("unchecked")
      Class<? extends IHasId<?>> fdeCls = (Class<? extends IHasId<?>>) fdCls;
      List<String> fdIdNms = this.setng.lazIdFldNms(fdeCls);
      if (fdIdNms.size() > 1) {
        throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID!");
      }
      @SuppressWarnings("unchecked")
      List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
      LvDep clvDep;
      Integer dpLv = (Integer) pVs.get(fdCls.getSimpleName() + "dpLv");
      if (dpLv != null) { //custom level for subentity(owned) subbranch:
        clvDep = new LvDep();
        clvDep.setDep(dpLv);
        lvDeps.add(clvDep);
        if (pIsDbgSh) {
          this.log.debug(pRvs, getClass(), "Start fill custDL subent/DL/CL: "
              + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
        }
      } else { //entering into new sub/branch's sub-entity:
        clvDep = lvDeps.get(lvDeps.size() - 1);
        clvDep.setCur(clvDep.getCur() + 1);
        if (pIsDbgSh) {
          this.log.debug(pRvs, getClass(), "Start subent/DL/CL: "
              + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
        }
      }
      if (lvDeps.size() > 1) { //sub-branch, main branch level change:
        lvDeps.get(0).setCur(lvDeps.get(0).getCur() + 1);
        this.log.debug(pRvs, getClass(), "Main branch UP DL/CL: "
            + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      }
      @SuppressWarnings("unchecked")
      List<String> tbAls = (List<String>) pVs.get("tbAls");
      //WrhEnr.wpFr.wrh and WrhEnr.wpTo.wrh
      Integer cuFdIdx = (Integer) pVs.get("cuFdIdx");
      String tbAl = pFdNm.toUpperCase() + lvDeps.get(0).getCur() + cuFdIdx;
      tbAls.add(tbAl);
      String owEnNm;
      if (tbAls.size() == 1) {
        owEnNm = pCls.getSimpleName().toUpperCase();
      } else {
        owEnNm = tbAls.get(tbAls.size() - 2);
      }
      pSbe.append(" left join " + fdCls.getSimpleName().toUpperCase() + " as "
        + tbAl + " on " + owEnNm + "." + pFdNm.toUpperCase() + "=" + tbAl + "."
          + fdIdNms.get(0).toUpperCase());
      if (pIsDbgSh) {
        this.log.debug(pRvs, getClass(), "Added tbAl/cls: " + tbAl
          + "/" + fdCls);
      }
      makeCls(pRvs, pVs, fdeCls, pSb, pSbe, pIsDbgSh);
      tbAls.remove(tbAl);
      if (lvDeps.size() > 1) { //move down through custom DL subentities branch:
        LvDep ld = lvDeps.get(lvDeps.size() - 1);
        if (ld.getCur() == 0) { //ending custom DL subentity:
          lvDeps.remove(lvDeps.size() - 1);
          if (pIsDbgSh) {
            this.log.debug(pRvs, getClass(),
              "Finish custom DL root subentity: " + fdCls);
          }
        } else { //finish subentity:
          ld.setCur(ld.getCur() - 1);
          if (pIsDbgSh) {
            this.log.debug(pRvs, getClass(),
              "Finish custom DL subentity: " + fdCls);
          }
        }
        //sub-branch, main branch level change:
        lvDeps.get(0).setCur(lvDeps.get(0).getCur() - 1);
        this.log.debug(pRvs, getClass(), "Main branch DOWN DL/CL: "
            + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      } else {  //move down through root DL subentities branch:
        LvDep ld = lvDeps.get(0);
        if (ld.getCur() > 0) { //finish subentity:
          ld.setCur(ld.getCur() - 1);
          if (pIsDbgSh) {
            this.log.debug(pRvs, getClass(),
              "Finish custom subentity: " + fdCls);
          }
        }
      }
    } else {
      String fnu = pFdNm.toUpperCase();
      @SuppressWarnings("unchecked")
      List<String> tbAls = (List<String>) pVs.get("tbAls");
      if (tbAls.size() > 0) {
        pSb.append(tbAls.get(tbAls.size() - 1) + "." + fnu + " as "
          + tbAls.get(tbAls.size() - 1) + fnu);
      } else {
      pSb.append(pCls.getSimpleName().toUpperCase() + "." + fnu + " as " + fnu);
      }
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
}
