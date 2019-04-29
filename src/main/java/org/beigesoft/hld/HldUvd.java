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

package org.beigesoft.hld;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdl.Page;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.ICnvId;
import org.beigesoft.cnv.IConv;

/**
 * <p>Service that transforms and holds part of settings from ISetng UVD.
 * It's also assembly and it consists of all UVD holders and request scoped
 * variables that are used in JSP.</p>
 *
 * @author Yury Demidenko
 */
public class HldUvd {

  //parts:
  /**
   * <p>Orm settings service.</p>
   **/
  private ISetng stgOrm;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Holder of converters ID-SQL/HTML names.</p>
   **/
  private IHld<Class<?>, String> hldCnvId;

  /**
   * <p>Factory of converters ID-SQL/HTML.</p>
   */
  private IFctNm<ICnvId<?, ?>> fctCnvId;

  /**
   * <p>Holders class string settings.</p>
   **/
  private Map<String, HldClsStg> hlClStgMp;

  /**
   * <p>Holders filed string settings.</p>
   **/
  private Map<String, HldFldStg> hlFdStgMp;

  /**
   * <p>Converters fields to string factory.</p>
   */
  private IFctNm<IConv<?, String>> fcCnToSt;

  /**
   * <p>Fields converters to string names holder.</p>
   **/
  private IHldNm<Class<?>, String> hlCnToSt;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  //derived/transformed settings:
  /**
   * <p>Entities fields in list map.</p>
   **/
  private final Map<Class<?>, String[]> lstFdsMp =
    new HashMap<Class<?>, String[]>();

  /**
   * <p>Owned entities classes map.</p>
   **/
  private final Map<Class<?>, List<Class<IOwned<?, ?>>>> owdEnts =
    new HashMap<Class<?>, List<Class<IOwned<?, ?>>>>();

  /**
   * <p>Entities fields nullable, [ClassSimpleName+FieldName]-[isNullable].</p>
   **/
  private final Map<String, Boolean> fldNulMp =
    new HashMap<String, Boolean>();

  //request scoped vars for JSP:
  /**
   * <p>Connection per thread holder.</p>
   **/
  private final ThreadLocal<UvdVar> hldUvdVar = new ThreadLocal<UvdVar>() { };

  //Utils(delegates):
  /**
   * <p>Set JS variables JS function.</p>
   * @param pUsdDp used decimal places
   * @param pPlNm ID DOM place name
   * @return JS function
   * @throws Exception - an exception
   **/
  public final String setJs(final Map<String, String> pUsdDp,
    final String pPlNm) {
    CmnPrf cpf = (CmnPrf) getRvs().get("cpf");
    UsPrf upf = (UsPrf) getRvs().get("upf");
    StringBuffer sb = new StringBuffer("bsSetNumVs('" + cpf.getDcSpv() + "','"
      + cpf.getDcSpv() + "'," + upf.getDgInGr() + ");");
    if (pUsdDp != null && pUsdDp.size() > 0) {
      sb.append("bsIniInpNum('");
      boolean isFst = true;
      for (String udp : pUsdDp.keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          sb.append(",");
        }
        sb.append(udp);
      }
      sb.append("','" + pPlNm + "');");
    }
    return sb.toString();
  }

  /**
   * <p>Converts to HTML ready ID, e.g. "IID=PAYB" for Account with String ID,
   * or "usr=User1&rol=Role1" for User-Role with composite ID.</p>
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  public final String idHtml(final IHasId<?> pEnt) throws Exception {
    String cvIdSqNm = this.hldCnvId.get(pEnt.getClass());
    @SuppressWarnings("rawtypes")
    ICnvId cvIdSq = this.fctCnvId.laz(getRvs(), cvIdSqNm);
    return cvIdSq.idHtml(pEnt);
  }

  /**
   * <p>Converts to SQL ready ID, e.g. "'PAYB'" for Account with String ID,
   * or "'User1','Role1'" for User-Role with composite ID.</p>
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  public final String idSql(final IHasId<?> pEnt) throws Exception {
    String cvIdSqNm = this.hldCnvId.get(pEnt.getClass());
    @SuppressWarnings("rawtypes")
    ICnvId cvIdSq = this.fctCnvId.laz(getRvs(), cvIdSqNm);
    return cvIdSq.idSql(pEnt);
  }

  /**
   * <p>Gets class string setting for given class.</p>
   * @param pCls class
   * @param pStgNm setting name
   * @return string setting
   * @throws Exception - an exception
   **/
  public final String stg(final Class<?> pCls,
    final String pStgNm) throws Exception {
    return this.hlClStgMp.get(pStgNm).get(pCls);
  }

  /**
   * <p>Gets field string setting for given class, field name.</p>
   * @param pCls class
   * @param pFdNm field name
   * @param pStgNm setting name
   * @return string setting
   * @throws Exception - an exception
   **/
  public final String stg(final Class<?> pCls, final String pFdNm,
    final String pStgNm) throws Exception {
    return this.hlFdStgMp.get(pStgNm).get(pCls, pFdNm);
  }

  /**
   * <p>Gets field's class.</p>
   * @param pCls class
   * @param pFdNm field name
   * @return field class
   * @throws Exception - an exception
   **/
  public final Class<?> fldCls(final Class<?> pCls, final String pFdNm) {
    return this.hldFdCls.get(pCls, pFdNm);
  }

  /**
   * <p>Formats (converts) field value to string for given class, field name.
   * It delegates this to registered converter.</p>
   * @param pCls class
   * @param pFdNm field name
   * @param pFdVl field value
   * @return string setting
   * @throws Exception - an exception
   **/
  public final String toStr(final Class<?> pCls, final String pFdNm,
    final Object pFdVl) throws Exception {
    String cnm = this.hlCnToSt.get(pCls, pFdNm);
    @SuppressWarnings("unchecked")
    IConv<Object, String> cnv = (IConv<Object, String>) this.fcCnToSt
      .laz(getRvs(), cnm);
    return cnv.conv(getRvs(), pFdVl);
  }

  /**
   * <p>Gets class fields in list in lazy mode.</p>
   * @param pCls Entity class
   * @return fields list
   * @throws Exception - an exception
   **/
  public final String[] lazLstFds(
    final Class<?> pCls) throws Exception {
    if (!this.owdEnts.keySet().contains(pCls)) {
      synchronized (this) {
        if (!this.lstFdsMp.keySet().contains(pCls)) {
          String lFdSt = null;
          synchronized (this.setng) {
            lFdSt = this.setng.lazClsStg(pCls, "lstFds");
            this.setng.getClsStgs().get(pCls).remove("lstFds");
          }
          if (lFdSt != null) {
            List<String> lFdLst = new ArrayList<String>();
            for (String fn : lFdSt.split(",")) {
              lFdLst.add(fn);
            }
            String[] rz = new String[lFdLst.size()];
            this.lstFdsMp.put(pCls, lFdLst.toArray(rz));
          } else {
            this.lstFdsMp.put(pCls, null);
          }
        }
      }
    }
    return this.lstFdsMp.get(pCls);
  }

  /**
   * <p>Gets if field nullable in lazy mode.</p>
   * @param pCls Entity class
   * @param pFdNm field name
   * @return if field nullable
   * @throws Exception - an exception
   **/
  public final Boolean lazNulb(final Class<?> pCls,
    final String pFdNm) throws Exception {
    String key = pCls.getSimpleName() + pFdNm;
    if (!this.fldNulMp.keySet().contains(key)) {
      synchronized (this) {
        if (!this.fldNulMp.keySet().contains(key)) {
          String def = null;
          synchronized (this.stgOrm) {
            def = this.stgOrm.lazFldStg(pCls, pFdNm, "def");
            if (this.stgOrm.getFldStgs().get(pCls).get(pFdNm).size() == 1) {
              this.stgOrm.getFldStgs().get(pCls).remove(pFdNm);
              if (this.stgOrm.getFldStgs().get(pCls).size() == 1) {
                this.stgOrm.getFldStgs().remove(pCls);
              }
            } else {
              this.stgOrm.getFldStgs().get(pCls).get(pFdNm).remove("def");
            }
          }
          if (def != null) {
            this.fldNulMp.put(key, def.contains("not null"));
          } else {
            throw new ExcCode(ExcCode.WR, "There is no fld def for cls/fld: "
              + pCls + "/" + pFdNm);
          }
        }
      }
    }
    return this.fldNulMp.get(key);
  }

  /**
   * <p>Gets owned list in lazy mode.</p>
   * @param pCls Entity class
   * @return owned list
   * @throws Exception - an exception
   **/
  public final List<Class<IOwned<?, ?>>> lazOwnd(
    final Class<?> pCls) throws Exception {
    if (!this.owdEnts.keySet().contains(pCls)) {
      synchronized (this) {
        if (!this.owdEnts.keySet().contains(pCls)) {
          String owdes = null;
          synchronized (this.setng) {
            owdes = this.setng.lazClsStg(pCls, "owdEnts");
            if (owdes != null) {
              this.setng.getClsStgs().get(pCls).remove("owdEnts");
            }
          }
          if (owdes != null) {
            List<Class<IOwned<?, ?>>> oeLst =
              new ArrayList<Class<IOwned<?, ?>>>();
            for (String oec : owdes.split(",")) {
              @SuppressWarnings("unchecked")
              Class<IOwned<?, ?>> cl = (Class<IOwned<?, ?>>) Class.forName(oec);
              oeLst.add(cl);
            }
            this.owdEnts.put(pCls, oeLst);
          } else {
            this.owdEnts.put(pCls, null);
          }
        }
      }
    }
    return this.owdEnts.get(pCls);
  }

  //Request/thread scoped vars:
  /**
   * <p>Getter variable per thread.</p>
   * @return variables
   **/
  public final UvdVar lazUvdVar() {
    UvdVar uvdVar = this.hldUvdVar.get();
    if (uvdVar == null) {
      uvdVar = new UvdVar();
      hldUvdVar.set(uvdVar);
    }
    return uvdVar;
  }

  /**
   * <p>Getter for cls.</p>
   * @return Class<?>
   **/
  public final Class<?> getCls() {
    return lazUvdVar().getCls();
  }

  /**
   * <p>Setter for cls.</p>
   * @param pCls reference
   **/
  public final void setCls(final Class<?> pCls) {
    lazUvdVar().setCls(pCls);
  }

  /**
   * <p>Getter for ents.</p>
   * @return List<?>
   **/
  public final List<?> getEnts() {
    return lazUvdVar().getEnts();
  }

  /**
   * <p>Setter for ents.</p>
   * @param pEnts reference
   **/
  public final void setEnts(final List<?> pEnts) {
    lazUvdVar().setEnts(pEnts);
  }

  /**
   * <p>Getter for lstFds.</p>
   * @return String[]
   **/
  public final String[] getLstFds() {
    return lazUvdVar().getLstFds();
  }

  /**
   * <p>Setter for lstFds.</p>
   * @param pLstFds reference
   **/
  public final void setLstFds(final String[] pLstFds) {
    lazUvdVar().setLstFds(pLstFds);
  }

  /**
   * <p>Getter for pgs.</p>
   * @return List<Page>
   **/
  public final List<Page> getPgs() {
    return lazUvdVar().getPgs();
  }

  /**
   * <p>Setter for pgs.</p>
   * @param pPgs reference
   **/
  public final void setPgs(final List<Page> pPgs) {
    lazUvdVar().setPgs(pPgs);
  }

  /**
   * <p>Getter for rvs.</p>
   * @return Map<String, Object>
   **/
  public final Map<String, Object> getRvs() {
    return lazUvdVar().getRvs();
  }

  /**
   * <p>Setter for rvs.</p>
   * @param pRvs reference
   **/
  public final void setRvs(final Map<String, Object> pRvs) {
    lazUvdVar().setRvs(pRvs);
  }

  /**
   * <p>Getter for ownr.</p>
   * @return IHasId<?>
   **/
  public final IHasId<?> getOwnr() {
    return lazUvdVar().getOwnr();
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  public final void setOwnr(final IHasId<?> pOwnr) {
    lazUvdVar().setOwnr(pOwnr);
  }

  /**
   * <p>Getter for ent.</p>
   * @return IHasId<?>
   **/
  public final IHasId<?> getEnt() {
    return lazUvdVar().getEnt();
  }

  /**
   * <p>Setter for ent.</p>
   * @param pEnt reference
   **/
  public final void setEnt(final IHasId<?> pEnt) {
    lazUvdVar().setEnt(pEnt);
    if (pEnt == null) {
      lazUvdVar().setCls(null);
    } else {
      lazUvdVar().setCls(pEnt.getClass());
    }
  }

  /**
   * <p>Getter for owdEntsMp.</p>
   * @return Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>>
   **/
  public final Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>> getOwdEntsMp() {
    return lazUvdVar().getOwdEntsMp();
  }

  /**
   * <p>Setter for owdEntsMp.</p>
   * @param pOwdEntsMp reference
   **/
  public final void setOwdEntsMp(
    final Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>> pOwdEntsMp) {
    lazUvdVar().setOwdEntsMp(pOwdEntsMp);
  }

  /**
   * <p>Getter for fltAp.</p>
   * @return Set<String>
   **/
  public final Set<String> getFltAp() {
    return lazUvdVar().getFltAp();
  }

  /**
   * <p>Setter for fltAp.</p>
   * @param pFltAp reference
   **/
  public final void setFltAp(final Set<String> pFltAp) {
    lazUvdVar().setFltAp(pFltAp);
  }

  /**
   * <p>Getter for fltMp.</p>
   * @return Map<String, Object>
   **/
  public final Map<String, Object> getFltMp() {
    return lazUvdVar().getFltMp();
  }

  /**
   * <p>Setter for fltMp.</p>
   * @param pFltMp reference
   **/
  public final void setFltMp(final Map<String, Object> pFltMp) {
    lazUvdVar().setFltMp(pFltMp);
  }

  /**
   * <p>Getter for ordMp.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getOrdMp() {
    return lazUvdVar().getOrdMp();
  }

  /**
   * <p>Setter for ordMp.</p>
   * @param pOrdMp reference
   **/
  public final void setOrdMp(final Map<String, String> pOrdMp) {
    lazUvdVar().setOrdMp(pOrdMp);
  }

  //Synchronized/simple SGS:
  /**
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final synchronized ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final synchronized void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
  }

  /**
   * <p>Getter for stgOrm.</p>
   * @return ISetng
   **/
  public final synchronized ISetng getStgOrm() {
    return this.stgOrm;
  }

  /**
   * <p>Setter for stgOrm.</p>
   * @param pStgOrm reference
   **/
  public final synchronized void setStgOrm(final ISetng pStgOrm) {
    this.stgOrm = pStgOrm;
  }

  /**
   * <p>Getter for hlClStgMp.</p>
   * @return Map<String, HldClsStg>
   **/
  public final Map<String, HldClsStg> getHlClStgMp() {
    return this.hlClStgMp;
  }

  /**
   * <p>Setter for hlClStgMp.</p>
   * @param pHlClStgMp reference
   **/
  public final void setHlClStgMp(final Map<String, HldClsStg> pHlClStgMp) {
    this.hlClStgMp = pHlClStgMp;
  }

  /**
   * <p>Getter for hldCnvId.</p>
   * @return IHld<Class<?>, String>
   **/
  public final IHld<Class<?>, String> getHldCnvId() {
    return this.hldCnvId;
  }

  /**
   * <p>Setter for hldCnvId.</p>
   * @param pHldCnvId reference
   **/
  public final void setHldCnvId(final IHld<Class<?>, String> pHldCnvId) {
    this.hldCnvId = pHldCnvId;
  }

  /**
   * <p>Getter for fctCnvId.</p>
   * @return IFctNm<ICnvId<?, ?>>
   **/
  public final IFctNm<ICnvId<?, ?>> getFctCnvId() {
    return this.fctCnvId;
  }

  /**
   * <p>Setter for fctCnvId.</p>
   * @param pFctCnvId reference
   **/
  public final void setFctCnvId(final IFctNm<ICnvId<?, ?>> pFctCnvId) {
    this.fctCnvId = pFctCnvId;
  }

  /**
   * <p>Getter for fcCnToSt.</p>
   * @return IFctNm<IConv<?, String>>
   **/
  public final IFctNm<IConv<?, String>> getFcCnToSt() {
    return this.fcCnToSt;
  }

  /**
   * <p>Setter for fcCnToSt.</p>
   * @param pFcCnToSt reference
   **/
  public final void setFcCnToSt(final IFctNm<IConv<?, String>> pFcCnToSt) {
    this.fcCnToSt = pFcCnToSt;
  }

  /**
   * <p>Getter for hlCnToSt.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHlCnToSt() {
    return this.hlCnToSt;
  }

  /**
   * <p>Setter for hlCnToSt.</p>
   * @param pHlCnToSt reference
   **/
  public final void setHlCnToSt(final IHldNm<Class<?>, String> pHlCnToSt) {
    this.hlCnToSt = pHlCnToSt;
  }

  /**
   * <p>Getter for hlFdStgMp.</p>
   * @return Map<String, HldFldStg>
   **/
  public final Map<String, HldFldStg> getHlFdStgMp() {
    return this.hlFdStgMp;
  }

  /**
   * <p>Setter for hlFdStgMp.</p>
   * @param pHlFdStgMp reference
   **/
  public final void setHlFdStgMp(final Map<String, HldFldStg> pHlFdStgMp) {
    this.hlFdStgMp = pHlFdStgMp;
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
