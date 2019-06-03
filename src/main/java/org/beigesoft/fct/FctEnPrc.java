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

package org.beigesoft.fct;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.prc.PrcEntCr;
import org.beigesoft.prc.PrcEnoDl;
import org.beigesoft.prc.PrcEnofDl;
import org.beigesoft.prc.PrcEntDl;
import org.beigesoft.prc.PrcEmMsgSv;
import org.beigesoft.prc.PrcEntSv;
import org.beigesoft.prc.PrcEnoSv;
import org.beigesoft.prc.PrcEnofSv;
import org.beigesoft.srv.IEmSnd;

/**
 * <p>Factory of entity processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctEnPrc<RS> implements IFctPrcEnt {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  /**
   * <p>Additional factories.</p>
   **/
  private Set<IFctPrcEnt> fctsPrc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrcEnt<?, ?>> procs =
    new HashMap<String, IPrcEnt<?, ?>>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor
   * @throws Exception - an exception
   */
  public final IPrcEnt<?, ?> laz(final Map<String, Object> pRvs,
    final String pPrNm) throws Exception {
    IPrcEnt<?, ?> rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (PrcEntCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntCr(pRvs);
          } else if (PrcEnoDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEnoDl(pRvs);
          } else if (PrcEntDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntDl(pRvs);
          } else if (PrcEmMsgSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEmMsgSv(pRvs);
          } else if (PrcEntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntSv(pRvs);
          } else if (PrcEnofDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEnofDl(pRvs);
          } else if (PrcEnofSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEnofSv(pRvs);
          } else if (PrcEnoSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEnoSv(pRvs);
          } else if (PrcEntRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntRt(pRvs);
          } else {
            if (this.fctsPrc != null) {
              for (IFctPrcEnt fct : this.fctsPrc) {
                rz = fct.laz(pRvs, pPrNm);
                if (rz != null) {
                  break;
                }
              }
            }
            if (rz == null) {
              throw new ExcCode(ExcCode.WRCN, "There is no IProc: " + pPrNm);
            }
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntCr.</p>
   * @param pRvs request scoped vars
   * @return PrcEntCr
   * @throws Exception - an exception
   */
  private PrcEntCr crPuPrcEntCr(
    final Map<String, Object> pRvs) throws Exception {
    PrcEntCr rz = new PrcEntCr();
    this.procs.put(PrcEntCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEntCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEnofDl.</p>
   * @param pRvs request scoped vars
   * @return PrcEnofDl
   * @throws Exception - an exception
   */
  private PrcEnofDl crPuPrcEnofDl(
    final Map<String, Object> pRvs) throws Exception {
    PrcEnofDl rz = new PrcEnofDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setHldGets(this.fctBlc.lazHldGets(pRvs));
    this.procs.put(PrcEnofDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEnofDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEnofSv.</p>
   * @param pRvs request scoped vars
   * @return PrcEnofSv
   * @throws Exception - an exception
   */
  private PrcEnofSv crPuPrcEnofSv(
    final Map<String, Object> pRvs) throws Exception {
    PrcEnofSv rz = new PrcEnofSv();
    rz.setAppPth(this.fctBlc.getFctDt().getAppPth());
    rz.setUplDir(this.fctBlc.getFctDt().getUplDir());
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setHldSets(this.fctBlc.lazHldSets(pRvs));
    rz.setHldGets(this.fctBlc.lazHldGets(pRvs));
    this.procs.put(PrcEnofSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEnofSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEnoSv.</p>
   * @param pRvs request scoped vars
   * @return PrcEnoSv
   * @throws Exception - an exception
   */
  private PrcEnoSv crPuPrcEnoSv(
    final Map<String, Object> pRvs) throws Exception {
    PrcEnoSv rz = new PrcEnoSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(PrcEnoSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEnoSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEnoDl.</p>
   * @param pRvs request scoped vars
   * @return PrcEnoDl
   * @throws Exception - an exception
   */
  private PrcEnoDl crPuPrcEnoDl(
    final Map<String, Object> pRvs) throws Exception {
    PrcEnoDl rz = new PrcEnoDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(PrcEnoDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEnoDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntDl.</p>
   * @param pRvs request scoped vars
   * @return PrcEntDl
   * @throws Exception - an exception
   */
  private PrcEntDl crPuPrcEntDl(
    final Map<String, Object> pRvs) throws Exception {
    PrcEntDl rz = new PrcEntDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(PrcEntDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEntDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEmMsgSv.</p>
   * @param pRvs request scoped vars
   * @return PrcEmMsgSv
   * @throws Exception - an exception
   */
  private PrcEmMsgSv crPuPrcEmMsgSv(
    final Map<String, Object> pRvs) throws Exception {
    PrcEmMsgSv rz = new PrcEmMsgSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setEmSnd((IEmSnd) this.fctBlc.laz(pRvs, IEmSnd.class.getSimpleName()));
    this.procs.put(PrcEmMsgSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEmMsgSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntSv.</p>
   * @param pRvs request scoped vars
   * @return PrcEntSv
   * @throws Exception - an exception
   */
  private PrcEntSv crPuPrcEntSv(
    final Map<String, Object> pRvs) throws Exception {
    PrcEntSv rz = new PrcEntSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(PrcEntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntRt.</p>
   * @param pRvs request scoped vars
   * @return PrcEntRt
   * @throws Exception - an exception
   */
  private PrcEntRt crPuPrcEntRt(
    final Map<String, Object> pRvs) throws Exception {
    PrcEntRt rz = new PrcEntRt();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setHldUvd(this.fctBlc.lazHldUvd(pRvs));
    this.procs.put(PrcEntRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcEntRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final synchronized FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final synchronized void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }

  /**
   * <p>Getter for fctsPrc.</p>
   * @return Set<IFctPrcEnt>
   **/
  public final synchronized Set<IFctPrcEnt> getFctsPrc() {
    return this.fctsPrc;
  }

  /**
   * <p>Setter for fctsPrc.</p>
   * @param pFctsPrc reference
   **/
  public final synchronized void setFctsPrc(final Set<IFctPrcEnt> pFctsPrc) {
    this.fctsPrc = pFctsPrc;
  }
}
