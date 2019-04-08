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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.LvDep;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that generates DML Select statement
 * for given entity and vars.</p>
 *
 * @author Yury Demidenko
 */
public class Selct implements ISelct {

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
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Generates DML Select statement for given entity and vars.</p>
   * @param <T> object (entity) type
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, nullable.
   * @param pCls entity class, not null
   * @return Select query
   * @throws Exception - an exception
   **/
  @Override
  public final <T> String gen(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls) throws Exception {
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
      this.log.debug(pRqVs, getClass(), "Start select root entity/DL/CL: "
        + pCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
    }
    List<String> tbAls = new ArrayList<String>();
    pVs.put("tbAls", tbAls);
    if (isDbgSh) {
      this.log.debug(pRqVs, getClass(), "tbAls created");
    }
    makeCls(pRqVs, pVs, pCls, sb, sbe, isDbgSh);
    pVs.remove("lvDeps");
    pVs.remove("tbAls");
    if (isDbgSh) {
      this.log.debug(pRqVs, getClass(),
        "Finish selecting root entity: " + pCls);
    }
    sb.append(sbe);
    return sb.toString();
  }

  /**
   * <p>Makes Select statement for given entity class and vars.</p>
   * @param <T> object (entity) type
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, nullable.
   * @param pCls entity class, not null
   * @param pSb string buffer
   * @param pSbe string buffer joints
   * @param pIsDbgSh is show debug messages
   * @throws Exception - an exception
   **/
  public final <T> void makeCls(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls,
      final StringBuffer pSb, final StringBuffer pSbe,
        final boolean pIsDbgSh) throws Exception {
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
    LvDep clvDep = lvDeps.get(lvDeps.size() - 1);
    String[] ndFds = (String[]) pVs.
      get(pCls.getSimpleName() + "ndFds");
    if (ndFds != null && pIsDbgSh) {
      this.log.debug(pRqVs, getClass(), "Needed fields entity: "
        + pCls + "/" + Arrays.toString(ndFds));
    }
    boolean isFst = true;
    for (String fdNm : this.setng.lazIdFldNms(pCls)) {
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
        makeFld(pRqVs, pVs, pCls, fdNm, pSb, pSbe, pIsDbgSh);
      }
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
          makeFld(pRqVs, pVs, pCls, fdNm, pSb, pSbe, pIsDbgSh);
        }
      }
    }
  }

  /**
   * <p>Makes Select statement for given entity class, field and vars.</p>
   * @param <T> object (entity) type
   * @param pRqVs request scoped vars
   * @param pVs invoker scoped vars, e.g. entity's needed fields, nullable.
   * @param pCls entity class, not null
   * @param pFdNm field name
   * @param pSb string buffer
   * @param pSbe string buffer joints
   * @param pIsDbgSh is show debug messages
   * @throws Exception - an exception
   **/
  public final <T> void makeFld(final Map<String, Object> pRqVs,
    final Map<String, Object> pVs, final Class<T> pCls, final String pFdNm,
      final StringBuffer pSb, final StringBuffer pSbe,
        final boolean pIsDbgSh) throws Exception {
    Class<?> fdCls = this.hldFdCls.get(pCls, pFdNm);
    if (IHasId.class.isAssignableFrom(fdCls)) {
      List<String> fdIdNms = this.setng.lazIdFldNms(fdCls);
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
          this.log.debug(pRqVs, getClass(), "Start fill custDL subent/DL/CL: "
              + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
        }
      } else { //entering into new sub/branch's sub-entity:
        clvDep = lvDeps.get(lvDeps.size() - 1);
        clvDep.setCur(clvDep.getCur() + 1);
        if (pIsDbgSh) {
          this.log.debug(pRqVs, getClass(), "Start subent/DL/CL: "
              + fdCls + "/" + clvDep.getDep() + "/" + clvDep.getCur());
        }
      }
      if (lvDeps.size() > 1) { //sub-branch, main branch level change:
        lvDeps.get(0).setCur(lvDeps.get(0).getCur() + 1);
        this.log.debug(pRqVs, getClass(), "Main branch UP DL/CL: "
            + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      }
      String tbAl = pFdNm.toUpperCase() + lvDeps.get(0).getCur();
      List<String> tbAls = (List<String>) pVs.get("tbAls");
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
        this.log.debug(pRqVs, getClass(), "Added tbAl/cls: " + tbAl
          + "/" + fdCls);
      }
      makeCls(pRqVs, pVs, fdCls, pSb, pSbe, pIsDbgSh);
      tbAls.remove(tbAl);
      if (lvDeps.size() > 1) { //move down through custom DL subentities branch:
        LvDep ld = lvDeps.get(lvDeps.size() - 1);
        if (ld.getCur() == 0) { //ending custom DL subentity:
          lvDeps.remove(lvDeps.size() - 1);
          if (pIsDbgSh) {
            this.log.debug(pRqVs, getClass(),
              "Finish custom DL root subentity: " + fdCls);
          }
        } else { //finish subentity:
          ld.setCur(ld.getCur() - 1);
          if (pIsDbgSh) {
            this.log.debug(pRqVs, getClass(),
              "Finish custom DL subentity: " + fdCls);
          }
        }
        //sub-branch, main branch level change:
        lvDeps.get(0).setCur(lvDeps.get(0).getCur() - 1);
        this.log.debug(pRqVs, getClass(), "Main branch DOWN DL/CL: "
            + lvDeps.get(0).getDep() + "/" + lvDeps.get(0).getCur());
      } else {  //move down through root DL subentities branch:
        LvDep ld = lvDeps.get(0);
        if (ld.getCur() > 0) { //finish subentity:
          ld.setCur(ld.getCur() - 1);
          if (pIsDbgSh) {
            this.log.debug(pRqVs, getClass(),
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
}
