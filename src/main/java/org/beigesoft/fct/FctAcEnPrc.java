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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.prc.PrcEntCr;
import org.beigesoft.prc.PrcEntSv;
import org.beigesoft.prc.PrcEnoSv;

/**
 * <p>Factory of entity processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctAcEnPrc<RS> implements IFctNm<IPrcEnt<IHasId<?>, ?>> {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrcEnt<IHasId<?>, ?>> procs =
    new HashMap<String, IPrcEnt<IHasId<?>, ?>>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor
   * @throws Exception - an exception
   */
  public final IPrcEnt<IHasId<?>, ?> laz(final Map<String, Object> pRqVs,
    final String pPrNm) throws Exception {
    IPrcEnt<IHasId<?>, ?> rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (PrcEntCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntCr(pRqVs);
          } else if (PrcEntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntSv(pRqVs);
          } else if (PrcEnoSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEnoSv(pRqVs);
          } else if (PrcEntRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrcEntRt(pRqVs);
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no IProc: " + pPrNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntCr.</p>
   * @param pRqVs request scoped vars
   * @return PrcEntCr
   * @throws Exception - an exception
   */
  private PrcEntCr crPuPrcEntCr(
    final Map<String, Object> pRqVs) throws Exception {
    PrcEntCr rz = new PrcEntCr();
    rz.setHldUvd(this.fctBlc.lazHldUvd(pRqVs));
    this.procs.put(PrcEntCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRqVs).info(pRqVs, getClass(), PrcEntCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEnoSv.</p>
   * @param pRqVs request scoped vars
   * @return PrcEnoSv
   * @throws Exception - an exception
   */
  private PrcEnoSv crPuPrcEnoSv(
    final Map<String, Object> pRqVs) throws Exception {
    PrcEnoSv rz = new PrcEnoSv();
    rz.setOrm(this.fctBlc.lazOrm(pRqVs));
    rz.setHldUvd(this.fctBlc.lazHldUvd(pRqVs));
    this.procs.put(PrcEnoSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRqVs).info(pRqVs, getClass(), PrcEnoSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntSv.</p>
   * @param pRqVs request scoped vars
   * @return PrcEntSv
   * @throws Exception - an exception
   */
  private PrcEntSv crPuPrcEntSv(
    final Map<String, Object> pRqVs) throws Exception {
    PrcEntSv rz = new PrcEntSv();
    rz.setOrm(this.fctBlc.lazOrm(pRqVs));
    this.procs.put(PrcEntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRqVs).info(pRqVs, getClass(), PrcEntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntRt.</p>
   * @param pRqVs request scoped vars
   * @return PrcEntRt
   * @throws Exception - an exception
   */
  private PrcEntRt crPuPrcEntRt(
    final Map<String, Object> pRqVs) throws Exception {
    PrcEntRt rz = new PrcEntRt();
    rz.setOrm(this.fctBlc.lazOrm(pRqVs));
    rz.setHldUvd(this.fctBlc.lazHldUvd(pRqVs));
    this.procs.put(PrcEntRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRqVs).info(pRqVs, getClass(), PrcEntRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }
}
