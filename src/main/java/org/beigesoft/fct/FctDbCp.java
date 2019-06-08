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

import org.beigesoft.hld.HldNmFilFdSt;
import org.beigesoft.hld.HldNmCnToStXml;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rpl.RpEntWriXml;
import org.beigesoft.rpl.RpEntReadXml;

/**
 * <p>Auxiliary factory for database full copy.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctDbCp<RS> implements IFctAux<RS> {

  /**
   * <p>DB-Copy entity writer service name.</p>
   **/
  public static final String ENWRDBCPNM = "enWrDbCp";

  /**
   * <p>DB-Copy entity reader service name.</p>
   **/
  public static final String ENRDDBCPNM = "enRdDbCp";

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
    if (FctDt.STGDBCPNM.equals(pBnNm)) {
      rz = crPuStgDbCp(pRvs, pFctApp);
    } else if (ENRDDBCPNM.equals(pBnNm)) {
      rz = crPuRpEntReadXmlDbCp(pRvs, pFctApp);
    } else if (ENWRDBCPNM.equals(pBnNm)) {
      rz = crPuRpEntWriXmlDbCp(pRvs, pFctApp);
    } else if (FctDt.HLCNTOSTDBCP.equals(pBnNm)) {
      rz = crPuRpHldNmCnToStXml(pRvs, pFctApp);
    } else if (FctDt.HLFILFDNMDBCP.equals(pBnNm)) {
      rz = crPuHldNmFilFdStDbCp(pRvs, pFctApp);
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
   * <p>Creates and puts into MF DB copy Setng.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return Setng
   * @throws Exception - an exception
   */
  private Setng crPuStgDbCp(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    Setng rz = new Setng();
    rz.setDir(pFctApp.getFctDt().getStgDbCpDir());
    rz.setReflect(pFctApp.lazReflect(pRvs));
    rz.setUtlPrp(pFctApp.lazUtlPrp(pRvs));
    rz.setHldFdCls(pFctApp.lazHldFldCls(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, FctDt.STGDBCPNM, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), FctDt.STGDBCPNM
      + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RpEntWriXmlDbCp.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RpEntWriXmlDbCp
   * @throws Exception - an exception
   */
  private RpEntWriXml crPuRpEntWriXmlDbCp(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RpEntWriXml rz = new RpEntWriXml();
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setSetng((ISetng) pFctApp.laz(pRvs, FctDt.STGDBCPNM));
    rz.setHldGets(pFctApp.lazHldGets(pRvs));
    HldNmCnToStXml hlCnToSt = (HldNmCnToStXml) pFctApp
      .laz(pRvs, FctDt.HLCNTOSTDBCP);
    rz.setHldNmFdCn(hlCnToSt);
    rz.setFctCnvFld(pFctApp.lazFctNmCnToSt(pRvs));
    pFctApp.put(pRvs, ENWRDBCPNM, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), ENWRDBCPNM
      + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RpHldNmCnToStXml.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RpHldNmCnToStXml
   * @throws Exception - an exception
   */
  private HldNmCnToStXml crPuRpHldNmCnToStXml(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HldNmCnToStXml rz = new HldNmCnToStXml();
    rz.setHldFdCls(pFctApp.lazHldFldCls(pRvs));
    rz.setCnHsIdToStNm(FctNmCnToSt.CNHSIDSTDBCPNM);
    pFctApp.put(pRvs, FctDt.HLCNTOSTDBCP, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), FctDt.HLCNTOSTDBCP
      + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RpEntReadXmlDbCp.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RpEntReadXmlDbCp
   * @throws Exception - an exception
   */
  private RpEntReadXml crPuRpEntReadXmlDbCp(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RpEntReadXml rz =  new RpEntReadXml();
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setSetng((ISetng) pFctApp.laz(pRvs, FctDt.STGDBCPNM));
    HldNmFilFdSt hldFilFdSt = (HldNmFilFdSt) pFctApp
      .laz(pRvs, FctDt.HLFILFDNMDBCP);
    rz.setHldFilFdNms(hldFilFdSt);
    rz.setUtlXml(pFctApp.lazUtlXml(pRvs));
    rz.setFctFilFld(pFctApp.lazFctNmFilFd(pRvs));
    pFctApp.put(pRvs, ENRDDBCPNM, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), ENRDDBCPNM
      + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF HldNmFilFdStDbCp.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HldNmFilFdStDbCp
   * @throws Exception - an exception
   */
  private HldNmFilFdSt crPuHldNmFilFdStDbCp(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HldNmFilFdSt rz = new HldNmFilFdSt();
    rz.setHldFdCls(pFctApp.lazHldFldCls(pRvs));
    rz.setFilHasIdNm(FctNmFilFdSt.FILHSIDSTDBCPNM);
    rz.setFilSmpNm(FctNmFilFdSt.FILSMPSTDBCPNM);
    rz.setSetng((ISetng) pFctApp.laz(pRvs, FctDt.STGDBCPNM));
    pFctApp.put(pRvs, FctDt.HLFILFDNMDBCP, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), FctDt.HLFILFDNMDBCP
      + " has been created.");
    return rz;
  }
}
