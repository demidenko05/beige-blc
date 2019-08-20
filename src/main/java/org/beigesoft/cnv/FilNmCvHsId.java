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
import java.util.HashMap;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.fct.IFcFlCvFdv;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prp.ISetng;

/**
 * <p>Fills given column values with given entity's field of entity type.</p>
 *
 * @author Yury Demidenko
 */
public class FilNmCvHsId implements IFilCvFld {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Holder of converters fields names.</p>
  **/
  private IHlNmClSt hldCnvFdNms;

  /**
   * <p>Converters fields factory.</p>
   */
  private IFcFlCvFdv fctCnvFld;

  /**
   * <p>Fills given column values with given entity's field of entity type.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pEnt source entity
   * @param pClVl column values
   * @param pFdNm field name
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> void fill(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pEnt, final String pFdNm,
      final ColVals pClVl) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 7235);
    Method getter = this.hldGets.get(pEnt.getClass(), pFdNm);
    @SuppressWarnings("unchecked")
    IHasId<?> subEnt = (IHasId<?>) getter.invoke(pEnt);
    @SuppressWarnings("unchecked")
    Class<IHasId<?>> fdCls = (Class<IHasId<?>>)
      this.hldFdCls.get(pEnt.getClass(), pFdNm);
    List<String> fdIdNms = this.setng.lazIdFldNms(fdCls);
    if (fdIdNms.size() > 1) {
      throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID!"
        + " ent/fd" + pEnt + "/" + pFdNm);
    }
    if (subEnt != null) {
      String idNm = fdIdNms.get(0);
      getter = this.hldGets.get(subEnt.getClass(), idNm);
      Object val = getter.invoke(subEnt);
      if (IHasId.class.isAssignableFrom(val.getClass())) {
        if (dbgSh) {
          this.log.debug(pRvs, getClass(),
        "Converts pEnt/pFdNm/subEnt/idNm/idCls: " + pEnt + "/" + pFdNm + "/"
      + subEnt + "/" + idNm + "/" + fdCls);
        }
        @SuppressWarnings("unchecked")
        IHasId<?> sse = (IHasId<?>) val;
        Object id = revId(sse);
        if (id.getClass().isEnum() || id.getClass() == Integer.class) {
          if (pClVl.getInts() == null) {
            pClVl.setInts(new HashMap<String, Integer>());
          }
          Integer iv;
          if (id.getClass().isEnum()) {
            iv = ((Enum) id).ordinal();
          } else {
            iv = (Integer) id;
          }
          pClVl.getInts().put(pFdNm, iv);
        } else if (id.getClass() == Long.class) {
          if (pClVl.getLongs() == null) {
            pClVl.setLongs(new HashMap<String, Long>());
          }
          pClVl.getLongs().put(pFdNm, (Long) id);
        } else if (id.getClass() == String.class) {
          if (pClVl.getStrs() == null) {
            pClVl.setStrs(new HashMap<String, String>());
          }
          pClVl.getStrs().put(pFdNm, (String) id);
        } else {
          throw new ExcCode(ExcCode.WRCN, "Subentity with wrong ID!"
            + " ent/fd/idCls" + pEnt + "/" + pFdNm + "/" + id.getClass());
        }
      } else {
        String cnvFdNm = this.hldCnvFdNms.get(fdCls, idNm);
        @SuppressWarnings("unchecked")
        IFilCvFdv<Object> flCvFdv = (IFilCvFdv<Object>)
          this.fctCnvFld.laz(pRvs, cnvFdNm);
        if (dbgSh) {
          this.log.debug(pRvs, getClass(),
        "Converts idNm/cls/val/converter: " + pFdNm + "/" + fdCls
      .getSimpleName() + "/" + val + "/" + flCvFdv.getClass().getSimpleName());
        }
        flCvFdv.fill(pRvs, pVs, pFdNm, val, pClVl);
      }
    } else { //there is no different what is field's SQL type for null value:
      if (pClVl.getLongs() == null) {
        pClVl.setLongs(new HashMap<String, Long>());
      }
      pClVl.getLongs().put(pFdNm, null);
    }
  }

  //Utils:
  /**
   * <p>Reveals last ID value.</p>
   * @param pEnt entity
   * @return ID value Integer/String/Long/Enum
   * @throws Exception - an exception
   **/
  public final Object revId(final IHasId<?> pEnt) throws Exception {
    Object rz;
    List<String> fdIdNms = this.setng.lazIdFldNms(pEnt.getClass());
    if (fdIdNms.size() > 1) {
      throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID - "  + pEnt);
    }
    String idNm = fdIdNms.get(0);
    Method getter = this.hldGets.get(pEnt.getClass(), idNm);
    rz = getter.invoke(pEnt);
    if (IHasId.class.isAssignableFrom(rz.getClass())) {
      @SuppressWarnings("unchecked")
      IHasId<?> sse = (IHasId<?>) rz;
      return revId(sse);
    }
    return rz;
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
   * <p>Getter for hldCnvFdNms.</p>
   * @return IHlNmClSt
   **/
  public final IHlNmClSt getHldCnvFdNms() {
    return this.hldCnvFdNms;
  }

  /**
   * <p>Setter for hldCnvFdNms.</p>
   * @param pHdCnFdNms reference
   **/
  public final void setHldCnvFdNms(final IHlNmClSt pHdCnFdNms) {
    this.hldCnvFdNms = pHdCnFdNms;
  }

  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFcFlCvFdv
   **/
  public final IFcFlCvFdv getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(final IFcFlCvFdv pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }
}
