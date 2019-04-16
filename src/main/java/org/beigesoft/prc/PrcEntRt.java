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

package org.beigesoft.prc;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.hld.IHld;
import org.beigesoft.cnv.ICnvId;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Service that retrieves entity from DB.</p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcEntRt<T extends IHasId<ID>, ID> implements IPrcEnt<T, ID> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

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
  private IFctNm<ICnvId<T, ID>> fctCnvId;

  /**
   * <p>Owned entities map.</p>
   **/
  private final Map<Class<?>, List<Class<IOwned<T, ?>>>> owdEnts =
    new HashMap<Class<?>, List<Class<IOwned<T, ?>>>>();

  /**
   * <p>Process that retrieves entity from DB.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(final Map<String, Object> pRvs, final T pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt);
    pEnt.setIsNew(false);
    pRqDt.setAttr("ent",  pEnt);
    if (!this.owdEnts.keySet().contains(pEnt.getClass())) {
      synchronized (this) {
        if (!this.owdEnts.keySet().contains(pEnt.getClass())) {
          String owdes = null;
          synchronized (this.setng) {
            owdes = this.setng.lazClsStg(pEnt.getClass(), "owdEnts");
            if (owdes != null) {
              this.setng.getClsStgs().get(pEnt.getClass()).remove("owdEnts");
            }
          }
          if (owdes != null) {
            List<Class<IOwned<T, ?>>> oeLst =
              new ArrayList<Class<IOwned<T, ?>>>();
            for (String oec : owdes.split(",")) {
              @SuppressWarnings("unchecked")
              Class<IOwned<T, ?>> cl = (Class<IOwned<T, ?>>)
                Class.forName(oec);
              oeLst.add(cl);
            }
            this.owdEnts.put(pEnt.getClass(), oeLst);
          } else {
            this.owdEnts.put(pEnt.getClass(), null);
          }
        }
      }
    }
    List<Class<IOwned<T, ?>>> oeLst = this.owdEnts.get(pEnt.getClass());
    if (oeLst != null) {
      Map<Class<IOwned<T, ?>>, List<IOwned<T, ?>>> owdEntsMp =
        new LinkedHashMap<Class<IOwned<T, ?>>, List<IOwned<T, ?>>>();
      String cvIdSqNm = this.hldCnvId.get(pEnt.getClass());
      ICnvId<T, ID> cvIdSq = this.fctCnvId.laz(pRvs, cvIdSqNm);
      String idOwnr = cvIdSq.idSql(pEnt);
      for (Class<IOwned<T, ?>> oec : oeLst) {
        vs.put(oec.getSimpleName() + "dpLv", 0);
        List<IOwned<T, ?>> lst = this.orm.retLstCnd(pRvs, vs, oec,
          "where OWNR=" + idOwnr);
        vs.remove(oec.getSimpleName() + "dpLv");
        owdEntsMp.put(oec, lst);
        for (IOwned<T, ?> owd : lst) {
          owd.setOwnr(pEnt);
        }
      }
      pRqDt.setAttr("owdEntsMp",  owdEntsMp);
    }
    return pEnt;
  }

  //Synchronized SGS:
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

  //Simple getters and setters:
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
   * @return IFctNm<ICnvId<T, ID>>
   **/
  public final IFctNm<ICnvId<T, ID>> getFctCnvId() {
    return this.fctCnvId;
  }

  /**
   * <p>Setter for fctCnvId.</p>
   * @param pFctCnvId reference
   **/
  public final void setFctCnvId(final IFctNm<ICnvId<T, ID>> pFctCnvId) {
    this.fctCnvId = pFctCnvId;
  }
}
