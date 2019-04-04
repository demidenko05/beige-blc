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
import java.util.ArrayList;
import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.mdl.LvDep;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.IHasId;
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
  private IFilEnt<IRecSet<RS>> filEnt;

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
    @SuppressWarnings("unchecked")
    Class<E> flCls = (Class<E>) this.hldFdCls.get(pObj.getClass(), pFlNm);
    E val = flCls.newInstance();
    @SuppressWarnings("unchecked")
    List<LvDep> lvDeps = (List<LvDep>) pVs.get("lvDeps");
    LvDep clvDep = lvDeps.get(lvDeps.size() - 1);
    String tbAl = null;
    List<String> tbAls = null;
    if (lvDeps.size() > 1 && clvDep.getDep() > 0) {
      Integer alsIdx = lvDeps.size() - 1;
      tbAl = pFlNm.toUpperCase() + alsIdx;
      tbAls = (List<String>) pVs.get("tbAls");
      if (tbAls == null) {
        tbAls = new ArrayList<String>();
        pVs.put("tbAls", tbAls);
      }
      tbAls.add(tbAl);
    }
    this.filEnt.fill(pRqVs, pVs, val, pRs);
    if (tbAl != null) {
      tbAls.remove(tbAl);
    }
    if (val.getIid() == null) {
      val = null;
    }
    Method setr = this.hldSets.get(pObj.getClass(), pFlNm);
    setr.invoke(pObj, val);
  }

  //Simple getters and setters:
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
   * @return IFilEnt<IRecSet<RS>>
   **/
  public final IFilEnt<IRecSet<RS>> getFilEnt() {
    return this.filEnt;
  }

  /**
   * <p>Setter for filEnt.</p>
   * @param pFilEnt reference
   **/
  public final void setFilEnt(final IFilEnt<IRecSet<RS>> pFilEnt) {
    this.filEnt = pFilEnt;
  }
}
