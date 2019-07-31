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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.IOrId;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Base filter DB ID and version time (!) for replication.</p>
 *
 * @author Yury Demidenko
 */
public class FltDbOrVt implements IFltEnts {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Makes SQL WHERE filter for given entity.</p>
   * @param pCls Entity Class
   * @param pRvs request scoped vars mast has ARplMth replication method
   * @return filter, e.g. "DBOR=1 and VER>786786788"
   * @throws Exception - an exception
   **/
  @Override
  public final String makeWhe(final Map<String, Object> pRvs,
    final Class<? extends IHasId<?>> pCls) throws Exception {
    if (!IOrId.class.isAssignableFrom(pCls)) {
  throw new Exception("Wrong configuration! This filter for IOrId!");
    }
    ARplMth rplMth = (ARplMth) pRvs.get("ARplMth");
    if (this.orm.getDbId().equals(rplMth.getRqDbId())) {
      throw new Exception("Wrong DB ID! this DB ID/requested: "
        + this.orm.getDbId() + "/" + rplMth.getRqDbId());
    }
    String tbNm = pCls.getSimpleName().toUpperCase();
    StringBuffer sb = new StringBuffer(tbNm + ".DBOR=" + rplMth.getRqDbId());
    if (rplMth.getLstDt() != null) {
      sb.append(" and " + tbNm + ".VER>" + rplMth.getLstDt().getTime());
    }
    return sb.toString();
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
}
