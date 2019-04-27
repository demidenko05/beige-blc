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
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.hld.HldUvd;
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
   * <p>Holder UVD settings, vars.</p>
   */
  private HldUvd hldUvd;

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
    String[] lstFds = this.hldUvd.lazLstFds(pEnt.getClass());
    this.hldUvd.setEnt(pEnt);
    this.hldUvd.setLstFds(lstFds);
    List<Class<IOwned<?, ?>>> oeLst = this.hldUvd
      .lazOwnd(pEnt.getClass());
    if (oeLst != null) {
      Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>> owdEntsMp =
        new LinkedHashMap<Class<IOwned<?, ?>>, List<IOwned<?, ?>>>();
      String idOwnr = this.hldUvd.idSql(pEnt);
      for (Class oecg : oeLst) {
        Class<IOwned<T, ?>> oec = (Class<IOwned<T, ?>>) oecg;
        lstFds = this.hldUvd.lazLstFds(oec);
        String[] ndFds = Arrays.copyOf(lstFds, lstFds.length);
        Arrays.sort(ndFds);
        vs.put(oec.getSimpleName() + "ndFds", ndFds);
        List<IOwned<T, ?>> lst = this.orm.retLstCnd(pRvs, vs, oec,
          "where OWNR=" + idOwnr);
        vs.remove(oec.getSimpleName() + "ndFds");
        owdEntsMp.put(oecg, (List) lst);
        for (IOwned<T, ?> owd : lst) {
          owd.setOwnr(pEnt);
        }
      }
      this.hldUvd.setOwdEntsMp(owdEntsMp);
    }
   return pEnt;
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
