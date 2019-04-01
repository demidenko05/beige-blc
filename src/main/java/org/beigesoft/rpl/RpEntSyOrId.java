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

package org.beigesoft.rpl;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdlp.IOrId;
import org.beigesoft.srv.IOrm;

/**
 * <p>Service that synchronizes IOrId {iid, idOr and dbOr}.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class RpEntSyOrId<RS, T extends IOrId> implements IRpEntSync<T> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm<RS> orm;

  /**
   * <p>Synchronizes given entity of type IOrId {iid, idOr and dbOr}.</p>
   * @param pRqVs request scoped vars
   * @param pEnt object
   * @return if entity exists in database (needs to update)
   * @throws Exception - an exception
   **/
  @Override
  public final boolean sync(final Map<String, Object> pRqVs,
    final T pEnt) throws Exception {
    if (getOrm().getDbId().equals(pEnt.getDbOr())) {
      throw new ExcCode(ExcCode.WR,
    "Foreign entity born in this database! {ID, ID BIRTH, DB BIRTH}:"
  + " {" + pEnt.getIid() + ", " + pEnt.getIdOr() + "," + pEnt.getDbOr());
    }
    String tblNm = pEnt.getClass().getSimpleName().toUpperCase();
    String whe = "where " + tblNm + ".IDOR=" + pEnt.getIid()
      + " and " + tblNm + ".DBOR=" + pEnt.getDbOr();
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ndFds = new String[] {"iid", "ver", "idor", "dbor"};
    vs.put("ndFds", ndFds);
    @SuppressWarnings("unchecked")
    T entDb = (T) getOrm().retEntCnd(pRqVs, vs, pEnt.getClass(), whe);
    pEnt.setIdOr(pEnt.getIid());
    if (entDb != null) {
      pEnt.setVer(entDb.getVer());
      pEnt.setIid(entDb.getIid());
      return false;
    } else {
      pEnt.setIid(null);
      return true;
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for orm.</p>
   * @return IOrm<RS>
   **/
  public final IOrm<RS> getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm<RS> pOrm) {
    this.orm = pOrm;
  }
}
