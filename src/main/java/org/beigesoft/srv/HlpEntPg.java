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

package org.beigesoft.srv;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.Page;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.log.ILog;
import org.beigesoft.dlg.IEvalFr;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.IConv;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.ISqlQu;

/**
 * <p>Service without state that retrieves entities page
 * or filter data according request.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HlpEntPg<RS> {

  //application scope services without request/business-logic state:
  /**
   * <p>Log.</p>
   **/
  private ILog log;


  /**
   * <p>I18N service.</p>
   **/
  private II18n i18n;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Page service.</p>
   */
  private ISrvPg srvPg;

  /**
   * <p>Generating select service.</p>
   **/
  private ISqlQu sqlQu;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Manager UVD settings.</p>
   **/
  private ISetng setng;

  /**
   * <p>Holder transformed UVD settings, other holders and vars.</p>
   */
  private HldUvd hldUvd;

  /**
   * <p>Fields converters factory.</p>
   **/
  private IFctNm<IConv<?, String>> fctCnvFd;

  /**
   * <p>Field converter names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldNmCnFd;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Retrieve ents pg - ents list, pgs, filter map etc.</p>
   * @param pRvs request scoped vars
   * @param pRqd Request Data
   * @param pEntMp Entity Map
   * @param pDbgSh is show debug messages
   * @param pMkFlt Delegate that makes first filter (from start
   * of WHERE). E.g. hard-coded S.E.Seller from request's user principal.
   * It must return either filter string or NULL.
   * It may throw exception, e.g. "Forbidden" if user has no permissions.
   * @throws Exception - an exception
   **/
  public final void retPg(final Map<String, Object> pRvs, final IReqDt pRqd,
    final Map<String, Class<IHasId<?>>> pEntMp, final boolean pDbgSh,
      final IEvalFr<IReqDt, String> pMkFlt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    String ent;
    if (pRvs.get("entOw") != null) {
      // owned entity put it to refresh owner list
      ent = (String) pRvs.get("entOw");
      if (pDbgSh) {
        this.log.debug(pRvs, getClass(), "It's used entOw: " + ent);
      }
    } else {
      ent = pRqd.getParam("ent");
      if (pDbgSh) {
        this.log.debug(pRvs, getClass(), "It's used ent: " + ent);
      }
    }
    Class<IHasId<?>> cls = pEntMp.get(ent);
    Set<String> fltAp = null;
    if (pRqd.getParam("flyNeedFltAppear") != null) {
      fltAp = new HashSet<String>();
      pRvs.put("fltAp", fltAp);
      if (pDbgSh) {
        this.log.debug(pRvs, getClass(), "It's used fltAp: " + fltAp);
      }
    }
    StringBuffer sbWhe = revPgFltDt(pRvs, pRqd, cls, pDbgSh);
    //cause settled either from request or from settings
    Map<String, String> ordMp = new HashMap<String, String>();
    String quOrdBy = "";
    String flOrPrf;
    String rnd = pRqd.getParam("rnd");
    if (rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    if (pDbgSh) {
      this.log.debug(pRvs, getClass(), "It's used flOrPrf: " + flOrPrf);
    }
    String ordBy = pRqd.getParam(flOrPrf + "ordBy");
    if (pDbgSh) {
      this.log.debug(pRvs, getClass(), "It's used ordBy: " + ordBy);
    }
    if (ordBy != null && !ordBy.equals("disabled")) {
      ordMp.put(flOrPrf + "ordBy", ordBy);
      String desc = "";
      if (pRqd.getParam(flOrPrf + "orderByDesc") != null) {
        // HTML form send nothing for unchecked checkbox
        desc = " desc";
        ordMp.put(flOrPrf + "orderByDesc", "on");
      }
      quOrdBy = " order by " + ent.toUpperCase() + "."
        + ordBy.toUpperCase() + desc;
    } else {
      ordMp.put(flOrPrf + "ordBy", this.setng.lazClsStg(cls, "ordDf"));
      String orderByDesc = this.setng.lazClsStg(cls, "ordDscDf");
      String ordDescSt = "";
      if (orderByDesc.equals("on")) {
        ordMp.put(flOrPrf + "orderByDesc", orderByDesc);
        ordDescSt = " desc";
      }
      quOrdBy = " order by " + ent.toUpperCase() + "."
        + ordMp.get(flOrPrf + "ordBy").toUpperCase() + ordDescSt;
    }
    if (pDbgSh) {
      this.log.debug(pRvs, getClass(),
        "It's used quOrdBy: " + quOrdBy);
    }
    Integer roCnt;
    String strWhe = null;
    String strFiWhe = null;
    if (pMkFlt != null) {
      strFiWhe = pMkFlt.eval(pRvs, pRqd);
      if (pDbgSh) {
        this.log.debug(pRvs, getClass(), "It's used pMkFlt/where: "
          + pMkFlt.getClass().getSimpleName() + "/" + strFiWhe);
      }
    }
    if (strFiWhe != null) {
      if (sbWhe.length() > 0) {
        strWhe = strFiWhe + " and " + sbWhe.toString();
      } else {
        strWhe = strFiWhe;
      }
    } else if (sbWhe.length() > 0) {
      strWhe = sbWhe.toString();
    }
    if (strWhe != null) {
      if (strFiWhe != null) { //it's maybe second level conditions:
        String qu = this.sqlQu.evSel(pRvs, vs, cls).toString();
        qu = qu.substring(qu.indexOf("from"));
        qu = "select count(*) as TOROWS " + qu + " where " + strWhe;
        roCnt = this.rdb.evInt(qu, "TOROWS");
      } else {
        roCnt = this.orm.evRowCntWhe(pRvs, vs, cls, strWhe);
      }
    } else {
      roCnt = this.orm.evRowCnt(pRvs, vs, cls);
    }
    Integer pg = Integer.valueOf(pRqd.getParam("pg"));
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    Integer pgSz = cpf.getPgSz();
    int pgCnt = srvPg.evPgCnt(roCnt, pgSz);
    if (pg > pgCnt) {
      pg = pgCnt;
    }
    int fstRz = (pg - 1) * pgSz; //0-20,20-40
    List<IHasId<?>> ents;
    String[] lstFds = this.hldUvd.lazLstFds(cls);
    String[] ndFds = Arrays.copyOf(lstFds, lstFds.length);
    Arrays.sort(ndFds);
    vs.put(cls.getSimpleName() + "ndFds", ndFds);
    if (strWhe != null || quOrdBy.length() > 0) {
      if (strWhe != null) {
        ents = this.orm.retPgCnd(pRvs, vs, cls, "where " + strWhe + quOrdBy,
          fstRz, pgSz);
      } else {
        ents = this.orm.retPgCnd(pRvs, vs, cls, quOrdBy, fstRz, pgSz);
      }
    } else {
      ents = this.orm.retPg(pRvs, vs, cls, fstRz, pgSz);
    }
    vs.remove(cls.getSimpleName() + "ndFds", ndFds);
    Integer pgTl = cpf.getPgTl();
    List<Page> pgs = srvPg.evPgs(pg, pgCnt, pgTl);
    this.hldUvd.setPgs(pgs);
    this.hldUvd.setEnts(ents);
    this.hldUvd.setLstFds(lstFds);
    this.hldUvd.setCls(cls);
    if (fltAp != null) {
      pRqd.setAttr("fltAp", fltAp);
    }
  }

  /**
   * <p>Retrieves pg's filter data like SQL where and filter map.</p>
   * @param pRvs request scoped vars to return revealed data
   * @param pRqd - Request Data
   * @param pCls Entity Class
   * @param pDbgSh is show debug messages
   * @return StringBuffer with "where"
   * @throws Exception - an Exception
   **/
  public final StringBuffer revPgFltDt(final Map<String, Object> pRvs,
    final IReqDt pRqd, final Class<?> pCls,
      final boolean pDbgSh) throws Exception {
    // it is not null if need to client e.g. for bulk operations
    @SuppressWarnings("unchecked")
    Set<String> fltAp = (Set<String>) pRvs.get("fltAp");
    StringBuffer sbWhe = new StringBuffer("");
    Map<String, Object> fltMp = new HashMap<String, Object>();
    String ent = pCls.getSimpleName();
    for (String fdNm : this.setng.lazFldNms(pCls)) {
      String wFl = this.setng.lazFldStg(pCls, fdNm, "wFl");
      if (pDbgSh) {
  this.log.debug(pRvs, getClass(), "It's used wFl/field: " + wFl + "/" + fdNm);
      }
      if (wFl != null) {
        if ("fEnt".equals(wFl)) {
          mkWheEnt(sbWhe, pRqd, ent, fdNm, fltMp, fltAp);
        } else if ("flSt".equals(wFl)) {
          mkWheStr(sbWhe, pRqd, ent, fdNm, fltMp, fltAp);
        } else if (wFl.startsWith("filterDate")) {
          mkWheDtTm(sbWhe, pRqd, ent, fdNm, "1", fltMp, fltAp);
          mkWheDtTm(sbWhe, pRqd, ent, fdNm, "2", fltMp, fltAp);
        } else if ("filterEnum".equals(wFl)) {
          mkWheEnm(sbWhe, pRqd, pCls, fdNm, fltMp, fltAp);
        } else if ("filterBoolean".equals(wFl)) {
          mkWheBln(sbWhe, pRqd, pCls, fdNm, fltMp, fltAp);
        } else if (wFl.startsWith("explFlt")) {
          mkWheExcpl(sbWhe, pRqd, pCls, fdNm, fltMp, fltAp);
        } else {
          mkWheStd(sbWhe, pRqd, ent, fdNm, "1", fltMp, fltAp);
          mkWheStd(sbWhe, pRqd, ent, fdNm, "2", fltMp, fltAp);
        }
      }
    }
    pRqd.setAttr("fltMp", fltMp);
    return sbWhe;
  }

  /**
   * <p>Make SQL WHERE clause if need.</p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pEntNm - entity name
   * @param pFdNm - field name
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheStr(final StringBuffer pSbw, final IReqDt pRqd,
    final String pEntNm, final String pFdNm, final Map<String, Object> pFltMp,
      final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd != null && rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd != null && rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String fltforcedName = flOrPrf + "forcedFor";
    String fltforced = pRqd.getParam(fltforcedName);
    if (fltforced != null) {
      pFltMp.put(fltforcedName, fltforced);
    }
    String nmFldVal = flOrPrf + pFdNm + "Val";
    String fltVal = pRqd.getParam(nmFldVal);
    String nmFldOpr = flOrPrf + pFdNm + "Opr";
    String fltOp = pRqd.getParam(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(fltOp) || "isnull".equals(fltOp)) {
        cond = pEntNm.toUpperCase()
            + "." + pFdNm.toUpperCase() + " "
            + toSqlOp(fltOp);
    } else if (fltVal != null && fltOp != null
      && !fltOp.equals("disabled") && !fltOp.equals("")) {
      cond = pEntNm.toUpperCase()
          + "." + pFdNm.toUpperCase() + " "
          + toSqlOp(fltOp)
          + " '" + fltVal + "'";
    }
    if (cond != null) {
      pFltMp.put(nmFldVal, fltVal);
      pFltMp.put(nmFldOpr, fltOp);
      if (pFltAp != null) {
        pFltAp.add(getI18n().getMsg(pFdNm) + " "
          + getI18n().getMsg(fltOp) + " " + fltVal);
      }
      if (pSbw.toString().length() == 0) {
        pSbw.append(cond);
      } else {
        pSbw.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause if need.</p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pEntNm - entity name
   * @param pFdNm - field name
   * @param pParSuffix - parameter suffix
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheStd(final StringBuffer pSbw, final IReqDt pRqd,
    final String pEntNm, final String pFdNm, final String pParSuffix,
      final Map<String, Object> pFltMp,
        final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd != null && rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd != null && rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String fltforcedName = flOrPrf + "forcedFor";
    String fltforced = pRqd.getParam(fltforcedName);
    if (fltforced != null) {
      pFltMp.put(fltforcedName, fltforced);
    }
    String nmFldVal = flOrPrf + pFdNm + "Val" + pParSuffix;
    String fltVal = pRqd.getParam(nmFldVal);
    String nmFldOpr = flOrPrf + pFdNm
      + "Opr" + pParSuffix;
    String fltOp = pRqd.getParam(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(fltOp) || "isnull".equals(fltOp)) {
        cond = pEntNm.toUpperCase()
            + "." + pFdNm.toUpperCase() + " "
            + toSqlOp(fltOp);
    } else if (fltVal != null && fltOp != null
      && !fltOp.equals("disabled") && !fltOp.equals("")) {
      cond = pEntNm.toUpperCase()
          + "." + pFdNm.toUpperCase() + " "
          + toSqlOp(fltOp)
          + " " + fltVal;
    }
    if (cond != null) {
      pFltMp.put(nmFldVal, fltVal);
      pFltMp.put(nmFldOpr, fltOp);
      if (pFltAp != null) {
        pFltAp.add(getI18n().getMsg(pFdNm) + " "
          + getI18n().getMsg(fltOp) + " " + fltVal);
      }
      if (pSbw.toString().length() == 0) {
        pSbw.append(cond);
      } else {
        pSbw.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for date-time if need.</p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pEntNm - entity name
   * @param pFdNm - field name
   * @param pParSuffix - parameter suffix
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheDtTm(final StringBuffer pSbw, final IReqDt pRqd,
    final String pEntNm, final String pFdNm, final String pParSuffix,
      final Map<String, Object> pFltMp,
        final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd != null && rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd != null && rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String fltforcedName = flOrPrf + "forcedFor";
    String fltforced = pRqd.getParam(fltforcedName);
    if (fltforced != null) {
      pFltMp.put(fltforcedName, fltforced);
    }
    String nmFldVal = flOrPrf + pFdNm + "Val" + pParSuffix;
    String fltVal = pRqd.getParam(nmFldVal);
    String nmFldOpr = flOrPrf + pFdNm
      + "Opr" + pParSuffix;
    String fltOp = pRqd.getParam(nmFldOpr);
    String cond = null;
    if ("isnotnull".equals(fltOp) || "isnull".equals(fltOp)) {
        cond = pEntNm.toUpperCase()
            + "." + pFdNm.toUpperCase() + " "
            + toSqlOp(fltOp);
    } else if (fltVal != null && fltOp != null
      && !fltOp.equals("disabled") && !fltOp.equals("")) {
      Date valDt;
      if (fltVal.contains(".")) { //2001-07-04T12:08:56.235
        valDt = this.srvDt.fromIso8601FullNoTz(fltVal, null);
      } else if (fltVal.contains(":")) {
        if (fltVal.length() == 19) { //2001-07-04T12:08:56
          valDt = this.srvDt.fromIso8601DateTimeSecNoTz(fltVal, null);
        } else { //2001-07-04T12:08
          valDt = this.srvDt.fromIso8601DateTimeNoTz(fltVal, null);
        }
      } else { //2001-07-04
        valDt = this.srvDt.fromIso8601DateNoTz(fltVal, null);
      }
      cond = pEntNm.toUpperCase()
          + "." + pFdNm.toUpperCase() + " "
          + toSqlOp(fltOp)
          + " " + valDt.getTime();
    }
    if (cond != null) {
      pFltMp.put(nmFldVal, fltVal);
      pFltMp.put(nmFldOpr, fltOp);
      if (pFltAp != null) {
        pFltAp.add(getI18n().getMsg(pFdNm) + " "
          + getI18n().getMsg(fltOp) + " " + fltVal);
      }
      if (pSbw.toString().length() == 0) {
        pSbw.append(cond);
      } else {
        pSbw.append(" and " + cond);
      }
    }
  }

  /**
   * <p>Make SQL operator e.g. 'eq'-&gt;'&gt;'.</p>
   * @param pOper operator - eq, gt, lt
   * @return SQL operator
   * @throws ExcCode - code 1003 WRPR
   **/
  public final String toSqlOp(
    final String pOper) throws ExcCode {
    if ("eq".equals(pOper)) {
      return "=";
    } else if ("ne".equals(pOper)) {
      return "!=";
    } else if ("gt".equals(pOper)) {
      return ">";
    } else if ("gte".equals(pOper)) {
      return ">=";
    } else if ("in".equals(pOper)) {
      return "in";
    } else if ("lt".equals(pOper)) {
      return "<";
    } else if ("lte".equals(pOper)) {
      return "<=";
    } else if ("isnull".equals(pOper)) {
      return "is null";
    } else if ("isnotnull".equals(pOper)) {
      return "is not null";
    } else if ("like".equals(pOper)) {
      return "like";
    } else {
      throw new ExcCode(ExcCode.WRPR, "can't match SQL operator: " + pOper);
    }
  }

  /**
   * <p>Make SQL WHERE clause for entity if need.</p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pEntNm - entity name
   * @param pFdNm - field name
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheEnt(final StringBuffer pSbw, final IReqDt pRqd,
    final String pEntNm, final String pFdNm, final Map<String, Object> pFltMp,
      final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String nmFldValId = flOrPrf + pFdNm + "ValId";
    String fltValId = pRqd.getParam(nmFldValId);
    String nmFldOpr = flOrPrf + pFdNm + "Opr";
    String fltOp = pRqd.getParam(nmFldOpr);
    if (fltOp != null && !fltOp.equals("disabled")) {
      // equals or not to empty
      if (fltValId == null || fltValId.length() == 0) {
        if (fltOp.equals("eq")) {
          fltOp = "isnull";
        } else {
          fltOp = "isnotnull";
        }
      }
      pFltMp.put(nmFldOpr, fltOp);
      String fltforcedName = flOrPrf + "forcedFor";
      String fltforced = pRqd.getParam(fltforcedName);
      if (fltforced != null) {
        pFltMp.put(fltforcedName, fltforced);
      }
      if (fltOp.equals("isnull")
        || fltOp.equals("isnotnull")) {
        String cond = pEntNm.toUpperCase()
          + "." + pFdNm.toUpperCase() + " "
            + toSqlOp(fltOp);
        if (pSbw.toString().length() == 0) {
          pSbw.append(cond);
        } else {
          pSbw.append(" and " + cond);
        }
        if (pFltAp != null) {
          pFltAp.add(getI18n().getMsg(pFdNm) + " " + getI18n().getMsg(fltOp));
        }
      } else {
        pFltMp.put(nmFldValId, fltValId);
        String nmFldValAppearance = flOrPrf + pFdNm
          + "ValAppearance";
        String fltValAppearance = pRqd.getParam(nmFldValAppearance);
        pFltMp.put(nmFldValAppearance, fltValAppearance);
        String valId = fltValId;
        if (fltOp.equals("in")) {
          valId = "(" + valId + ")";
        }
        String cond = pEntNm.toUpperCase() + "." + pFdNm.toUpperCase() + " "
          + toSqlOp(fltOp) + " " + valId;
        if (pSbw.toString().length() == 0) {
          pSbw.append(cond);
        } else {
          pSbw.append(" and " + cond);
        }
        if (pFltAp != null) {
          pFltAp.add(getI18n().getMsg(pFdNm) + " "
            + getI18n().getMsg(fltOp) + " " + fltValAppearance);
        }
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for enum if need.</p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pCls - entity class
   * @param pFdNm - field name
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheEnm(final StringBuffer pSbw, final IReqDt pRqd,
    final Class<?> pCls, final String pFdNm, final Map<String, Object> pFltMp,
      final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String fltforcedName = flOrPrf + "forcedFor";
    String fltforced = pRqd.getParam(fltforcedName);
    if (fltforced != null) {
      pFltMp.put(fltforcedName, fltforced);
    }
    String nmFldVal = flOrPrf + pFdNm + "Val";
    String fltVal = pRqd.getParam(nmFldVal);
    String nmFldOpr = flOrPrf + pFdNm + "Opr";
    String fltOp = pRqd.getParam(nmFldOpr);
    if (fltVal != null && fltVal.length() > 0 && fltOp != null
      && !fltOp.equals("disabled") && !fltOp.equals("")) {
      String val;
      String valAppear;
     @SuppressWarnings("unchecked")
     Class<Enum<?>> classEnum = (Class<Enum<?>>) this.hldFdCls.get(pCls, pFdNm);
      if (fltOp.equals("in")) {
        StringBuffer sbVal = new StringBuffer("(");
        StringBuffer sbValAppear = new StringBuffer("(");
        boolean isFirst = true;
        for (String vl : fltVal.split(",")) {
          if (isFirst) {
            isFirst = false;
          } else {
            sbVal.append(", ");
            sbValAppear.append(", ");
          }
          Enum enVal = classEnum.getEnumConstants()[Integer.parseInt(vl)];
          sbVal.append(vl);
          sbValAppear.append(getI18n().getMsg(enVal.name()));
        }
        val = sbVal.toString() + ")";
        valAppear = sbValAppear.toString() + ")";
      } else {
        Enum enVal = classEnum.getEnumConstants()[Integer.parseInt(fltVal)];
        val = fltVal;
        valAppear = getI18n().getMsg(enVal.name());
      }
      pFltMp.put(flOrPrf + pFdNm + "ValAppearance", valAppear);
      pFltMp.put(nmFldVal, fltVal);
      pFltMp.put(nmFldOpr, fltOp);
      String cond = pCls.getSimpleName().toUpperCase()
          + "." + pFdNm.toUpperCase() + " "
          + toSqlOp(fltOp)
          + " " + val;
      if (pSbw.toString().length() == 0) {
        pSbw.append(cond);
      } else {
        pSbw.append(" and " + cond);
      }
      if (pFltAp != null) {
        pFltAp.add(getI18n().getMsg(pFdNm) + " "
          + getI18n().getMsg(fltOp) + " " + valAppear);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for filter that
   * pass explicit escaped "where":
   * " gte "is ">="
   * " lte "is "<="
   * " lt "is "<"
   * " gt "is ">"
   * " apst "is "'"
   * " prcnt "is "%"
   * " undln "is "_"
   * e.g.:
   * "DESCRIPTION apst prcnt 200 undln apst and PAYMENTTOTAL/ITSTOTAL gte 0.05"
   * is treated as
   * "DESCRIPTION '%200' and PAYMENTTOTAL/ITSTOTAL >= 0.05".
   * </p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pCls - entity class
   * @param pFdNm - field name
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheExcpl(final StringBuffer pSbw, final IReqDt pRqd,
    final Class<?> pCls, final String pFdNm, final Map<String, Object> pFltMp,
      final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String fltforcedName = flOrPrf + "forcedFor";
    String fltforced = pRqd.getParam(fltforcedName);
    if (fltforced != null) {
      pFltMp.put(fltforcedName, fltforced);
    }
    String nmFldVal = flOrPrf + pFdNm + "Val";
    String fltVal = pRqd.getParam(nmFldVal);
    if (fltVal != null && fltVal.length() > 0
      && !fltVal.equals("disabled")) {
      pFltMp.put(nmFldVal, fltVal);
      String cond = fltVal.replace(" gte ", ">=");
      cond = cond.replace(" lte ", "<=");
      cond = cond.replace(" lt ", "<");
      cond = cond.replace(" gt ", ">");
      cond = cond.replace(" ne ", "!=");
      cond = cond.replace(" eq ", "=");
      cond = cond.replace(" apst ", "'");
      cond = cond.replace(" prcnt ", "%");
      cond = cond.replace(" undln ", "_");
      if (pSbw.toString().length() == 0) {
        pSbw.append(cond);
      } else {
        pSbw.append(" and " + cond);
      }
      if (pFltAp != null) {
        pFltAp.add(cond);
      }
    }
  }

  /**
   * <p>Make SQL WHERE clause for boolean if need.</p>
   * @param pSbw result clause
   * @param pRqd - Request Data
   * @param pCls - entity class
   * @param pFdNm - field name
   * @param pFltMp - map to store current filter
   * @param pFltAp - set to store current filter appearance
   * if null - not required
   * @throws Exception - an Exception
   **/
  public final void mkWheBln(final StringBuffer pSbw, final IReqDt pRqd,
    final Class<?> pCls, final String pFdNm, final Map<String, Object> pFltMp,
      final Set<String> pFltAp) throws Exception {
    String rnd = pRqd.getParam("rnd");
    String flOrPrf;
    if (rnd.contains("pickerDub")) {
      flOrPrf = "fltordPD";
    } else if (rnd.contains("picker")) {
      flOrPrf = "fltordP";
    } else {
      flOrPrf = "fltordM";
    }
    String fltforcedName = flOrPrf + "forcedFor";
    String fltforced = pRqd.getParam(fltforcedName);
    if (fltforced != null) {
      pFltMp.put(fltforcedName, fltforced);
    }
    String nmFldVal = flOrPrf + pFdNm + "Val";
    String fltVal = pRqd.getParam(nmFldVal);
    if (fltVal != null && (fltVal.length() == 0
      || "null".equals(fltVal))) {
      fltVal = null;
    }
    pFltMp.put(nmFldVal, fltVal);
    if (fltVal != null) {
      int intVal = 0;
      if (fltVal.equals("true")) {
        intVal = 1;
      }
      String cond = pCls.getSimpleName().toUpperCase()
          + "." + pFdNm.toUpperCase() + " = " + intVal;
      if (pSbw.toString().length() == 0) {
        pSbw.append(cond);
      } else {
        pSbw.append(" and " + cond);
      }
      if (pFltAp != null) {
        pFltAp.add(getI18n().getMsg(pFdNm) + " = " + fltVal);
      }
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for log.</p>
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
   * <p>Getter for i18n.</p>
   * @return II18n
   **/
  public final II18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final II18n pI18n) {
    this.i18n = pI18n;
  }

  /**
   * <p>Getter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for srvPg.</p>
   * @return ISrvPg
   **/
  public final ISrvPg getSrvPg() {
    return this.srvPg;
  }

  /**
   * <p>Setter for srvPg.</p>
   * @param pSrvPg reference
   **/
  public final void setSrvPg(final ISrvPg pSrvPg) {
    this.srvPg = pSrvPg;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
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
   * <p>Getter for fctCnvFd.</p>
   * @return IFctNm<IConv<?, String>>
   **/
  public final IFctNm<IConv<?, String>> getFctCnvFd() {
    return this.fctCnvFd;
  }

  /**
   * <p>Setter for fctCnvFd.</p>
   * @param pFctCnvFd reference
   **/
  public final void setFctCnvFd(final IFctNm<IConv<?, String>> pFctCnvFd) {
    this.fctCnvFd = pFctCnvFd;
  }

  /**
   * <p>Getter for hldNmCnFd.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldNmCnFd() {
    return this.hldNmCnFd;
  }

  /**
   * <p>Setter for hldNmCnFd.</p>
   * @param pHldNmCnFd reference
   **/
  public final void setHldNmCnFd(final IHldNm<Class<?>, String> pHldNmCnFd) {
    this.hldNmCnFd = pHldNmCnFd;
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
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for sqlQu.</p>
   * @return ISqlQu
   **/
  public final ISqlQu getSqlQu() {
    return this.sqlQu;
  }

  /**
   * <p>Setter for sqlQu.</p>
   * @param pSqlQu reference
   **/
  public final void setSqlQu(final ISqlQu pSqlQu) {
    this.sqlQu = pSqlQu;
  }

  /**
   * <p>Getter for hldUvd.</p>
   * @return HldUvd
   **/
  public final HldUvd getHldUvd() {
    return this.hldUvd;
  }

  /**
   * <p>Setter for hldUvd.</p>
   * @param pHldUvd reference
   **/
  public final void setHldUvd(final HldUvd pHldUvd) {
    this.hldUvd = pHldUvd;
  }
}
