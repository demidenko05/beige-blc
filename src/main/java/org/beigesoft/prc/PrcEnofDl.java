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

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.IOrId;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Service that deletes entity with file from owned list from DB.</p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcEnofDl<T extends IOwned<?, ID>, ID> implements IPrcEnt<T, ID> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Process that deletes entity.</p>
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
    if (IOrId.class.isAssignableFrom(pEnt.getClass())) {
      IOrId oid = (IOrId) pEnt;
      if (!oid.getDbOr().equals(this.orm.getDbId())) {
        throw new ExcCode(ExcCode.WR, "can_not_change_foreign_src");
      }
    }
    String fdFlPth = pRqDt.getParam("fdFlPth");
    Method gfp = this.hldGets.get(pEnt.getClass(), fdFlPth);
    String flPth = (String) gfp.invoke(pEnt);
    if (flPth != null) {
      File fl = new File(flPth);
      if (fl.exists() && !fl.delete()) {
        throw new ExcCode(ExcCode.WR, "Can not delete file: " + fl);
      }
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.del(pRvs, vs, pEnt);
    pRvs.put("msgSuc", "delete_ok");
    this.orm.refrEnt(pRvs, vs, pEnt.getOwnr());
    long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
    if (owVrWs != pEnt.getOwnr().getVer()) {
      throw new ExcCode(IOrm.DRTREAD, "dirty_read");
    }
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setOwnr(pEnt.getOwnr());
    return null;
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
