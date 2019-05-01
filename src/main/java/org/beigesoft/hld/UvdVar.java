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
import java.util.Map;
import java.util.Set;

import org.beigesoft.mdl.Page;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdl.IHasId;

/**
 * <p>It consists of request scoped variables that are used in JSP.</p>
 *
 * @author Yury Demidenko
 */
public class UvdVar {
  //list/form:
  /**
   * <p>Requested entity class.</p>
   **/
  private Class<?> cls;

    //list:
  /**
   * <p>Requested entity fields.</p>
   **/
  private List<?> ents;

  /**
   * <p>Pages.</p>
   **/
  private List<Page> pgs;

      //filter/order:
  /**
   * <p>Filter appearance to print used filter,
   *  e.g. used filter in assign goods to catalog.</p>
   **/
  private Set<String> fltAp;

  /**
   * <p>List filter map data.</p>
   **/
  private Map<String, Object> fltMp;

  /**
   * <p>List order map data.</p>
   **/
  private Map<String, String> ordMp;

  /**
   * <p>Requested entity's owner.</p>
   **/
  private IHasId<?> ownr;

    //form:
  /**
   * <p>Requested entity.</p>
   **/
  private IHasId<?> ent;

  /**
   * <p>Owned lists.</p>
   **/
  private Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>> owdEntsMp;

  //Simple getters and setters:

  /**
   * <p>Getter for cls.</p>
   * @return Class<?>
   **/
  public final Class<?> getCls() {
    return this.cls;
  }

  /**
   * <p>Setter for cls.</p>
   * @param pCls reference
   **/
  public final void setCls(final Class<?> pCls) {
    this.cls = pCls;
  }

  /**
   * <p>Getter for ents.</p>
   * @return List<?>
   **/
  public final List<?> getEnts() {
    return this.ents;
  }

  /**
   * <p>Setter for ents.</p>
   * @param pEnts reference
   **/
  public final void setEnts(final List<?> pEnts) {
    this.ents = pEnts;
  }

  /**
   * <p>Getter for pgs.</p>
   * @return List<Page>
   **/
  public final List<Page> getPgs() {
    return this.pgs;
  }

  /**
   * <p>Setter for pgs.</p>
   * @param pPgs reference
   **/
  public final void setPgs(final List<Page> pPgs) {
    this.pgs = pPgs;
  }

  /**
   * <p>Getter for ownr.</p>
   * @return IHasId<?>
   **/
  public final IHasId<?> getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  public final void setOwnr(final IHasId<?> pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for ent.</p>
   * @return IHasId<?>
   **/
  public final IHasId<?> getEnt() {
    return this.ent;
  }

  /**
   * <p>Setter for ent.</p>
   * @param pEnt reference
   **/
  public final void setEnt(final IHasId<?> pEnt) {
    this.ent = pEnt;
    if (this.ent == null) {
      this.cls = null;
    } else {
      this.cls = this.ent.getClass();
    }
  }

  /**
   * <p>Getter for owdEntsMp.</p>
   * @return Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>>
   **/
  public final Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>> getOwdEntsMp() {
    return this.owdEntsMp;
  }

  /**
   * <p>Setter for owdEntsMp.</p>
   * @param pOwdEntsMp reference
   **/
  public final void setOwdEntsMp(
    final Map<Class<IOwned<?, ?>>, List<IOwned<?, ?>>> pOwdEntsMp) {
    this.owdEntsMp = pOwdEntsMp;
  }

  /**
   * <p>Getter for fltAp.</p>
   * @return Set<String>
   **/
  public final Set<String> getFltAp() {
    return this.fltAp;
  }

  /**
   * <p>Setter for fltAp.</p>
   * @param pFltAp reference
   **/
  public final void setFltAp(final Set<String> pFltAp) {
    this.fltAp = pFltAp;
  }

  /**
   * <p>Getter for fltMp.</p>
   * @return Map<String, Object>
   **/
  public final Map<String, Object> getFltMp() {
    return this.fltMp;
  }

  /**
   * <p>Setter for fltMp.</p>
   * @param pFltMp reference
   **/
  public final void setFltMp(final Map<String, Object> pFltMp) {
    this.fltMp = pFltMp;
  }

  /**
   * <p>Getter for ordMp.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getOrdMp() {
    return this.ordMp;
  }

  /**
   * <p>Setter for ordMp.</p>
   * @param pOrdMp reference
   **/
  public final void setOrdMp(final Map<String, String> pOrdMp) {
    this.ordMp = pOrdMp;
  }
}
