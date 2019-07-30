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

import java.util.Set;
import java.util.HashSet;

import org.beigesoft.mdl.IHasId;

/**
 * <p>Holder of entities for sub-workspace, e.g.
 * entities or S.E.Seller ones.</p>
 *
 * @author Yury Demidenko
 **/
public class HldEnts {

  /**
   * <p>All entities ID.</p>
   **/
  public static final Integer ID_ALL = 0;

  /**
   * <p>Base entities ID.</p>
   **/
  public static final Integer ID_BASE = 1;

  /**
   * <p>Admin and web-store ID.</p>
   **/
  public static final Integer ID_ADMIN = 2;

  /**
   * <p>ID, not null.</p>
   **/
  private Integer iid;

  /**
   * <p>All own entities, null for base workspace.</p>
   **/
  private Set<Class<? extends IHasId<?>>> ents;

  /**
   * <p>Shared to others (non-editable for them) entities.</p>
   **/
  private Set<EntShr> shrEnts;

  /**
   * <p>Evaluates shared entities for given workspace.</p>
   * @param pIid reference
   * @return Set<Class<? extends IHasId<?>>> or null
   **/
  public final Set<Class<? extends IHasId<?>>> evShrEnts(final Integer pIid) {
    if (pIid == null || this.iid.equals(pIid)) {
      throw new RuntimeException("Wrong workspace ID, this/requestor: "
        + this.iid + "/" + pIid);
    }
    Set<Class<? extends IHasId<?>>> rz = null;
    if (this.shrEnts != null) {
      for (EntShr es :this.shrEnts) {
        for (Integer rd : es.getRdrs()) {
          if (rd.equals(ID_ALL) || rd.equals(pIid)) {
            if (rz == null) {
              rz = new HashSet<Class<? extends IHasId<?>>>();
            }
            rz.add(es.getEnt());
          }
        }
      }
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for iid.</p>
   * @return Integer
   **/
  public final Integer getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  public final void setIid(final Integer pIid) {
    this.iid = pIid;
  }

  /**
   * <p>Getter for shrEnts.</p>
   * @return Set<EntShr>
   **/
  public final Set<EntShr> getShrEnts() {
    return this.shrEnts;
  }

  /**
   * <p>Setter for shrEnts.</p>
   * @param pShrEnts reference
   **/
  public final void setShrEnts(final Set<EntShr> pShrEnts) {
    this.shrEnts = pShrEnts;
  }

  /**
   * <p>Getter for ents.</p>
   * @return Set<Class<? extends IHasId<?>>>
   **/
  public final Set<Class<? extends IHasId<?>>> getEnts() {
    return this.ents;
  }

  /**
   * <p>Setter for ents.</p>
   * @param pEnts reference
   **/
  public final void setEnts(final Set<Class<? extends IHasId<?>>> pEnts) {
    this.ents = pEnts;
  }
}
