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
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.log.ILog;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.ICnvId;
import org.beigesoft.cnv.IConv;

/**
 * <p>Service that transforms and holds part of settings from ISetng UVD.
 * It's also assembly and it consists of all UVD holders variables
 * that are used in JSP.</p>
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
   * <p>Logger.</p>
   **/
  private ILog log;

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
  private IHlNmClSt hlCnToSt;

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

  //Utils(delegates):
  /**
   * <p>Set JS variables JS function.</p>
   * @param pRvs request scoped vars
   * @param pUsdDp used decimal places
   * @param pPlNm ID DOM place name
   * @return JS function
   * @throws Exception - an exception
   **/
  public final String setJs(final Map<String, Object> pRvs,
    final Map<String, String> pUsdDp, final String pPlNm) {
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    UsPrf upf = (UsPrf) pRvs.get("upf");
    StringBuffer sb = new StringBuffer("bsSetNumVs('" + cpf.getDcSpv() + "','"
      + cpf.getDcGrSpv() + "'," + upf.getDgInGr() + ");");
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
   * @param pRvs request scoped vars
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  public final String idHtml(final Map<String, Object> pRvs,
    final IHasId<?> pEnt) throws Exception {
    String cvIdSqNm = this.hldCnvId.get(pEnt.getClass());
    @SuppressWarnings("rawtypes")
    ICnvId cvIdSq = this.fctCnvId.laz(pRvs, cvIdSqNm);
    return cvIdSq.idHtml(pEnt);
  }

  /**
   * <p>Converts to SQL ready ID, e.g. "'PAYB'" for Account with String ID,
   * or "'User1','Role1'" for User-Role with composite ID.</p>
   * @param pRvs request scoped vars
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  public final String idSql(final Map<String, Object> pRvs,
    final IHasId<?> pEnt) throws Exception {
    String cvIdSqNm = this.hldCnvId.get(pEnt.getClass());
    @SuppressWarnings("rawtypes")
    ICnvId cvIdSq = this.fctCnvId.laz(pRvs, cvIdSqNm);
    return cvIdSq.idSql(pEnt);
  }

  /**
   * <p>Gets class string setting for given class.</p>
   * @param pCls class
   * @param pStgNm setting name
   * @return string setting, not null
   * @throws Exception - an exception
   **/
  public final String stg(final Class<?> pCls,
    final String pStgNm) throws Exception {
    HldClsStg hl = this.hlClStgMp.get(pStgNm);
    if (hl == null) {
      throw new ExcCode(ExcCode.WRCN, "There is no HldClsStg for cls/stgNm: "
        + pCls.getSimpleName() + "/" + pStgNm);
    }
    String rz = hl.get(pCls);
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6101 && this.log.getDbgCl() > 6099;
    if (isDbgSh) {
      this.log.debug(null, getClass(), "Setting for cls/stgNm/stg: "
        + pCls.getSimpleName() + "/" + pStgNm + "/" + rz);
    }
    return rz;
  }

  /**
   * <p>Gets field string setting for given class, field name.</p>
   * @param pCls class
   * @param pFdNm field name
   * @param pStgNm setting name
   * @return string setting, not null
   * @throws Exception - an exception
   **/
  public final String stg(final Class<?> pCls, final String pFdNm,
    final String pStgNm) throws Exception {
    HldFldStg hl = this.hlFdStgMp.get(pStgNm);
    if (hl == null) {
      throw new ExcCode(ExcCode.WRCN, "There is no HldFldStg for cls/fd/stg: "
        + pCls.getSimpleName() + "/" + pFdNm + "/" + pStgNm);
    }
    String rz = hl.get(pCls, pFdNm);
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6102 && this.log.getDbgCl() > 6100;
    if (isDbgSh) {
      this.log.debug(null, getClass(), "Setting for cls/fdNm/stgNm/stg: "
        + pCls.getSimpleName() + "/" + pFdNm + "/" + pStgNm + "/" + rz);
    }
    return rz;
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
   * @param pRvs request scoped vars
   * @param pCls class
   * @param pFdNm field name
   * @param pFdVl field value
   * @return string setting
   * @throws Exception - an exception
   **/
  public final String toStr(final Map<String, Object> pRvs, final Class<?> pCls,
    final String pFdNm, final Object pFdVl) throws Exception {
    String cnm = this.hlCnToSt.get(pCls, pFdNm);
    @SuppressWarnings("unchecked")
    IConv<Object, String> cnv = (IConv<Object, String>) this.fcCnToSt
      .laz(pRvs, cnm);
    return cnv.conv(pRvs, pFdVl);
  }

  /**
   * <p>Gets class fields in list in lazy mode.</p>
   * @param pCls Entity class
   * @return fields list, not null
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
            String[] rzt = new String[lFdLst.size()];
            this.lstFdsMp.put(pCls, lFdLst.toArray(rzt));
          } else {
            this.lstFdsMp.put(pCls, null);
          }
        }
      }
    }
    String[] rz = this.lstFdsMp.get(pCls);
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6103 && this.log.getDbgCl() > 6101;
    if (isDbgSh) {
      this.log.debug(null, getClass(), "lstFds for cls/lstFds: "
        + pCls.getSimpleName() + "/" + Arrays.toString(rz));
    }
    return rz;
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

  //Synchronized/simple SGS:
  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final synchronized ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final synchronized void setLog(final ILog pLog) {
    this.log = pLog;
  }

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
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHlCnToSt() {
    return this.hlCnToSt;
  }

  /**
   * <p>Setter for hlCnToSt.</p>
   * @param pHlCnToSt reference
   **/
  public final void setHlCnToSt(final IHlNmClSt pHlCnToSt) {
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
