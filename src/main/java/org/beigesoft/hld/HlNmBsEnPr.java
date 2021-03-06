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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.prc.PrcEnoCr;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.prc.PrcEntCr;
import org.beigesoft.prc.PrcEntSv;
import org.beigesoft.prc.PrcEntDl;
import org.beigesoft.prc.PrcEnoDl;
import org.beigesoft.prc.PrcEnoSv;

/**
 * <p>Holder of names of base entities processors.</p>
 *
 * @author Yury Demidenko
 */
public class HlNmBsEnPr implements IHlNmClSt {

  /**
   * <p>Shared non-editable entities for base entity request handler,
   * e.g. email connection EmCon.</p>
   **/
  private Set<Class<? extends IHasId<?>>> shrEnts;

  /**
   * <p>Additional base entity processors names holders
   *  with high priority.</p>
   **/
  private Set<IHlNmClSt> hldsBsEnPr;

  /**
   * <p>Get processor name for given class and action name.</p>
   * @param pCls a Class
   * @param pAct action name
   * @return processor FE name
   * @throws Exception an Exception
   **/
  @Override
  public final <T extends IHasId<?>> String get(final Class<T> pCls,
    final String pAct) throws Exception {
    if (this.shrEnts != null && this.shrEnts.contains(pCls)) {
      return null;
    }
    if (this.hldsBsEnPr != null) {
      for (IHlNmClSt hep : this.hldsBsEnPr) {
        String rz = hep.get(pCls, pAct);
        if (rz != null) {
          if (NULL.equals(rz)) {
            return null;
          } else {
            return rz;
          }
        }
      }
    }
    if ("entEd".equals(pAct) || "entCd".equals(pAct) || "entPr".equals(pAct)) {
      return PrcEntRt.class.getSimpleName();
    }
    if ("entCr".equals(pAct)) {
      if (IOwned.class.isAssignableFrom(pCls)) {
        return PrcEnoCr.class.getSimpleName();
      }
      return PrcEntCr.class.getSimpleName();
    }
    if ("entDl".equals(pAct)) {
      if (IOwned.class.isAssignableFrom(pCls)) {
        return PrcEnoDl.class.getSimpleName();
      }
      return PrcEntDl.class.getSimpleName();
    }
    if ("entSv".equals(pAct)) {
      if (IOwned.class.isAssignableFrom(pCls)) {
        return PrcEnoSv.class.getSimpleName();
      }
      return PrcEntSv.class.getSimpleName();
    }
    return null;
  }

  //SGS:
  /**
   * <p>Getter for shrEnts.</p>
   * @return Set<Class<? extends IHasId<?>>>
   **/
  public final Set<Class<? extends IHasId<?>>> getShrEnts() {
    return this.shrEnts;
  }

  /**
   * <p>Setter for shrEnts.</p>
   * @param pShrEnts reference
   **/
  public final void setShrEnts(
    final Set<Class<? extends IHasId<?>>> pShrEnts) {
    this.shrEnts = pShrEnts;
  }

  /**
   * <p>Getter for hldsBsEnPr.</p>
   * @return Set<IHlNmClSt>
   **/
  public final Set<IHlNmClSt> getHldsBsEnPr() {
    return this.hldsBsEnPr;
  }

  /**
   * <p>Setter for hldsBsEnPr.</p>
   * @param pHldsBsEnPr reference
   **/
  public final void setHldsBsEnPr(final Set<IHlNmClSt> pHldsBsEnPr) {
    this.hldsBsEnPr = pHldsBsEnPr;
  }
}
