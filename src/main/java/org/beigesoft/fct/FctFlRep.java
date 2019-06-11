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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.hnd.HndFlRpRq;
import org.beigesoft.hnd.HndEntFlRpRq;
import org.beigesoft.hnd.IFcEnFlRp;
import org.beigesoft.cnv.IFilEntRq;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Auxiliary factory for base file reporters services.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctFlRep<RS> implements IFctAux<RS> {

  /**
   * <p>Creates requested bean and put into given main factory.
   * The main factory is already synchronized when invokes this.</p>
   * @param pRvs request scoped vars
   * @param pBnNm - bean name
   * @param pFctApp main factory
   * @return Object - requested bean or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Object crePut(final Map<String, Object> pRvs,
    final String pBnNm, final FctBlc<RS> pFctApp) throws Exception {
    Object rz = null;
    if (HndFlRpRq.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHndFlRpRq(pRvs, pFctApp);
    } else if (IFctPrcFl.class.getSimpleName().equals(pBnNm)) {
      rz = crPuFctPrcFl(pRvs, pFctApp);
    } else if (HndEntFlRpRq.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHndEntFlRpRq(pRvs, pFctApp);
    }
    return rz;
  }

  /**
   * <p>Releases state when main factory is releasing.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @throws Exception - an exception
   */
  @Override
  public final void release(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    //nothing;
  }

  /**
   * <p>Creates and puts into MF DB copy FctPrcFl.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return FctPrcFl
   * @throws Exception - an exception
   */
  private FctPrcFl crPuFctPrcFl(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    FctPrcFl rz = new FctPrcFl();
    rz.setFctrsPrcFl(pFctApp.getFctDt().getFctrsPrcFl());
    pFctApp.put(pRvs, IFctPrcFl.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), IFctPrcFl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF DB copy HndEntFlRpRq.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HndEntFlRpRq
   * @throws Exception - an exception
   */
  private HndEntFlRpRq<RS> crPuHndEntFlRpRq(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HndEntFlRpRq<RS> rz = new HndEntFlRpRq<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    IFcEnFlRp fcPrFl = (IFcEnFlRp) pFctApp
      .laz(pRvs, IFcEnFlRp.class.getSimpleName());
    rz.setFctEntFlRp(fcPrFl);
    IFilEntRq filEnRq = (IFilEntRq) pFctApp
      .laz(pRvs, IFilEntRq.class.getSimpleName());
    rz.setFilEntRq(filEnRq);
    rz.setFctFctEnt(pFctApp.lazFctFctEnt(pRvs));
    rz.setLogStd(pFctApp.lazLogStd(pRvs));
    Map<String, Class<? extends IHasId<?>>> entMap =
      new HashMap<String, Class<? extends IHasId<?>>>();
    for (Class<? extends IHasId<?>> cls : pFctApp.getFctDt().getFlRpEnts()) {
      entMap.put(cls.getSimpleName(), cls);
    }
    rz.setEntMap(entMap);
    rz.setTrIsl(pFctApp.getFctDt().getReadTi());
    pFctApp.put(pRvs, HndEntFlRpRq.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), HndEntFlRpRq.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF DB copy HndFlRpRq.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HndFlRpRq
   * @throws Exception - an exception
   */
  private HndFlRpRq<RS> crPuHndFlRpRq(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HndFlRpRq<RS> rz = new HndFlRpRq<RS>();
    rz.setLogStd(pFctApp.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    IFctPrcFl fcPrFl = (IFctPrcFl) pFctApp
      .laz(pRvs, IFctPrcFl.class.getSimpleName());
    rz.setFctPrcFl(fcPrFl);
    rz.setTrIsl(pFctApp.getFctDt().getReadTi());
    pFctApp.put(pRvs, HndFlRpRq.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), HndFlRpRq.class
      .getSimpleName() + " has been created.");
    return rz;
  }
}
