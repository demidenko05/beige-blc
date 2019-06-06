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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.hld.HldFldCls;
import org.beigesoft.hld.HldGets;
import org.beigesoft.hld.HldSets;
import org.beigesoft.hld.HldCnvFdCv;
import org.beigesoft.hld.HldNmFilFdRs;
import org.beigesoft.hld.HldNmFilFdSt;
import org.beigesoft.hld.HldFilFdCv;
import org.beigesoft.hld.HldNmCnFrRs;
import org.beigesoft.hld.HldNmCnFrSt;
import org.beigesoft.hld.HldNmCnToSt;
import org.beigesoft.hld.HldNmCnFrStXml;
import org.beigesoft.hld.HlNmPrFe;
import org.beigesoft.hld.HlNmPrFeAd;
import org.beigesoft.hld.HlNmBsEnPr;
import org.beigesoft.hld.HlNmAdEnPr;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.hld.HldCnvId;
import org.beigesoft.hnd.HndEntRq;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.hnd.HndNtrRq;
import org.beigesoft.prp.UtlPrp;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.log.ILog;
import org.beigesoft.log.ALogFile;
import org.beigesoft.log.LogFile;
import org.beigesoft.cnv.FilEntRs;
import org.beigesoft.cnv.FilEntRq;
import org.beigesoft.cnv.IFilEntRq;
import org.beigesoft.cnv.FilCvEnt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.Orm;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.ISqlQu;
import org.beigesoft.rdb.SqlQu;
import org.beigesoft.rdb.SrvClVl;
import org.beigesoft.srv.INumStr;
import org.beigesoft.srv.NumStr;
import org.beigesoft.srv.IReflect;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.UtlXml;
import org.beigesoft.srv.IUtlXml;
import org.beigesoft.srv.ISqlEsc;
import org.beigesoft.srv.SqlEsc;
import org.beigesoft.srv.ISrvPg;
import org.beigesoft.srv.SrvPg;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.srv.SrvDt;
import org.beigesoft.srv.I18n;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.UtlJsp;
import org.beigesoft.srv.HlpEntPg;

/**
 * <p>Main application beans factory. All configuration dependent inner
 * factories must be inside "fctConf".</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FctBlc<RS> implements IFctApp {

  //parts/services:
  /**
   * <p>Factory data.</p>
   **/
  private final FctDt fctDt = new FctDt();

  /**
   * <p>Outside app-beans/parts factories final configuration.
   *  They put its beans into this main factory beans.</p>
   **/
  private final List<IFctAux<RS>> fctsAux = new ArrayList<IFctAux<RS>>();

    //cached services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  //requested beans map, it's filled by this and external factories,
  //it's for high performance:
  /**
   * <p>Beans map.</p>
   **/
  private final Map<String, Object> beans = new HashMap<String, Object>();

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pBnNm - bean name
   * @return Object - requested bean or exception if not found
   * @throws Exception - an exception
   */
  @Override
  public final Object laz(final Map<String, Object> pRvs,
    final String pBnNm) throws Exception {
    if (pBnNm == null) {
      throw new ExcCode(ExcCode.WRPR, "Null bean name!!!");
    }
    Object rz = this.beans.get(pBnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.beans.get(pBnNm);
        if (rz == null) {
          if (HndI18nRq.class.getSimpleName().equals(pBnNm)) {
            rz = lazHndI18nRq(pRvs);
          } else if (FctDt.HNNTRQAD.equals(pBnNm)) {
            rz = lazHndNtrRqAd(pRvs);
          } else if (FctDt.HNNTRQSC.equals(pBnNm)) {
            rz = lazHndNtrRq(pRvs);
          } else if (FctDt.HNACENRQ.equals(pBnNm)) {
            rz = lazHnAcEnRq(pRvs);
          } else if (FctDt.HNADENRQ.equals(pBnNm)) {
            rz = lazHnAdEnRq(pRvs);
          } else if (FctPrcFen.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctPrcFen(pRvs);
          } else if (FctDt.STGORMNM.equals(pBnNm)) {
            rz = lazStgOrm(pRvs);
          } else if (IOrm.class.getSimpleName().equals(pBnNm)) {
            rz = lazOrm(pRvs);
          } else if (SrvClVl.class.getSimpleName().equals(pBnNm)) {
            rz = lazSrvClVl(pRvs);
          } else if (ISqlQu.class.getSimpleName().equals(pBnNm)) {
            rz = lazSqlQu(pRvs);
          } else if (FilCvEnt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilCvEnt(pRvs);
          } else if (FilEntRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilEntRs(pRvs);
          } else if (HldCnvFdCv.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldCnvFdCv(pRvs);
          } else if (HldNmFilFdRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmFilFdRs(pRvs);
          } else if (HldFilFdCv.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldFilFdCv(pRvs);
          } else if (HldNmCnFrRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnFrRs(pRvs);
          } else if (FctCnvCv.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctCnvCv(pRvs);
          } else if (FctNmCnFrRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnFrRs(pRvs);
          } else if (IFilEntRq.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilEntRq(pRvs);
          } else if (FctDt.STGUVDNM.equals(pBnNm)) {
            rz = lazStgUvd(pRvs);
          } else if (FctNmCnFrSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnFrSt(pRvs);
          } else if (FctNmFilFdSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmFilFd(pRvs);
          } else if (FctNmCnToSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnToSt(pRvs);
          } else if (HldUvd.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldUvd(pRvs);
          } else if (FctDt.HLFILFDNMUVD.equals(pBnNm)) {
            rz = lazHldNmFilFdStUvd(pRvs);
          } else if (HldNmCnFrStXml.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnFrStXml(pRvs);
          } else if (HldNmCnFrSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnFrStUvd(pRvs);
          } else if (HldNmCnToSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnToStUvd(pRvs);
          } else if (HldGets.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldGets(pRvs);
          } else if (HldSets.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldSets(pRvs);
          } else if (HldFldCls.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldFldCls(pRvs);
          } else if (FctEnPrc.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctEnPrc(pRvs);
          } else if (FctFctEnt.class.getSimpleName().equals(pBnNm)) {
            //IT DEPENDS OF ORM!
            rz = lazFctFctEnt(pRvs);
          } else if (UtlPrp.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlPrp(pRvs);
          } else if (INumStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazNumStr(pRvs);
          } else if (ISqlEsc.class.getSimpleName().equals(pBnNm)) {
            rz = lazSqlEsc(pRvs);
          } else if (IUtlXml.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlXml(pRvs);
          } else if (FctDt.LOGSTDNM.equals(pBnNm)) {
            rz = lazLogStd(pRvs);
          } else if (FctDt.LOGSECNM.equals(pBnNm)) {
            rz = lazLogSec(pRvs);
          } else if (IReflect.class.getSimpleName().equals(pBnNm)) {
            rz = lazReflect(pRvs);
          } else if (ISrvDt.class.getSimpleName().equals(pBnNm)) {
            rz = lazSrvDt(pRvs);
          } else if (ISrvPg.class.getSimpleName().equals(pBnNm)) {
            rz = lazSrvPg(pRvs);
          } else if (II18n.class.getSimpleName().equals(pBnNm)) {
            rz = lazI18n(pRvs);
          } else if (HlpEntPg.class.getSimpleName().equals(pBnNm)) {
            rz = lazHlpEntPg(pRvs);
          } else {
            for (IFctAux<RS> fau : this.fctsAux) {
              rz = fau.crePut(pRvs, pBnNm, this);
              if (rz != null) {
                break;
              }
            }
            if (rz == null) {
              throw new ExcCode(ExcCode.WRPR, "There is no bean: " + pBnNm);
            }
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Puts beans by external AUX factory.</p>
   * @param pRvs request scoped vars
   * @param pBnNm - bean name
   * @param pBean - bean
   * @throws Exception - an exception, e.g. if bean exists
   **/
  @Override
  public final synchronized void put(final Map<String, Object> pRvs,
    final String pBnNm, final Object pBean) throws Exception {
    if (pBnNm == null || pBean == null) {
      throw new ExcCode(ExcCode.WRPR, "Null bean or name: nm/bn"
        + pBnNm + "/" + pBean);
    }
    if (this.beans.keySet().contains(pBnNm)) {
      throw new ExcCode(ExcCode.WRPR, "Bean exists: " + pBnNm);
    }
    this.beans.put(pBnNm, pBean);
  }

  /**
   * <p>Release beans (memory). This is "memory friendly" factory.</p>
   * @param pRvs request scoped vars
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void release(
    final Map<String, Object> pRvs) throws Exception {
    this.beans.clear();
    for (IFctAux<RS> fau : this.fctsAux) {
      fau.release(pRvs, this);
    }
    if (this.logStd != null && this.logStd instanceof ALogFile) {
      LogFile logFl = (LogFile) this.logStd;
      logFl.info(pRvs, getClass(), "Send stop to LOG STD...");
      logFl.setNeedRun(false);
      this.logStd = null;
    }
  }

  //Request handlers:
  /**
   * <p>Lazy getter handler admin/webstore entities request.</p>
   * @param pRvs request scoped vars
   * @return HndEntRq
   * @throws Exception - an exception
   */
  public final synchronized HndEntRq<RS> lazHnAdEnRq(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    HndEntRq<RS> rz = (HndEntRq<RS>) this.beans.get(FctDt.HNADENRQ);
    if (rz == null) {
      rz = new HndEntRq<RS>();
      rz.setWriteTi(this.fctDt.getWriteTi());
      rz.setReadTi(this.fctDt.getReadTi());
      rz.setWriteReTi(this.fctDt.getWriteReTi());
      rz.setWrReSpTr(this.fctDt.getWrReSpTr());
      rz.setLogStd(lazLogStd(pRvs));
      rz.setLogSec(lazLogSec(pRvs));
      rz.setHldUvd(lazHldUvd(pRvs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRvs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setFilEntRq(lazFilEntRq(pRvs));
      rz.setFctFctEnt(lazFctFctEnt(pRvs));
      rz.setHldPrcFenNm(new HlNmPrFeAd());
      FctPrcFenAd<RS> fen = new FctPrcFenAd<RS>();
      fen.setFctBlc(this);
      rz.setFctPrcFen(fen);
      HlNmAdEnPr hlNmAdEnPr = new HlNmAdEnPr();
      hlNmAdEnPr.setHldsAdEnPr(this.fctDt.getHldsAdEnPr());
      rz.setHldEntPrcNm(hlNmAdEnPr);
      rz.setFctEntPrc(lazFctEnPrc(pRvs));
      rz.setEntMap(new HashMap<String, Class<? extends IHasId<?>>>());
      Setng setng = lazStgUvd(pRvs);
      for (Class<? extends IHasId<?>> cls  : setng.lazClss()) {
        if (this.fctDt.getAdmEnts() == null
          || this.fctDt.getAdmEnts().contains(cls)) {
          rz.getEntMap().put(cls.getSimpleName(), cls);
        }
      }
      this.beans.put(FctDt.HNADENRQ, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.HNADENRQ
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter handler base entities request.</p>
   * @param pRvs request scoped vars
   * @return HndEntRq
   * @throws Exception - an exception
   */
  public final synchronized HndEntRq<RS> lazHnAcEnRq(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    HndEntRq<RS> rz = (HndEntRq<RS>) this.beans.get(FctDt.HNACENRQ);
    if (rz == null) {
      rz = new HndEntRq<RS>();
      rz.setWriteTi(this.fctDt.getWriteTi());
      rz.setReadTi(this.fctDt.getReadTi());
      rz.setWriteReTi(this.fctDt.getWriteReTi());
      rz.setWrReSpTr(this.fctDt.getWrReSpTr());
      rz.setLogStd(lazLogStd(pRvs));
      rz.setLogSec(lazLogSec(pRvs));
      rz.setHldUvd(lazHldUvd(pRvs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRvs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setFilEntRq(lazFilEntRq(pRvs));
      rz.setFctFctEnt(lazFctFctEnt(pRvs));
      rz.setFctPrcFen(lazFctPrcFen(pRvs));
      HlNmBsEnPr hlep = new HlNmBsEnPr();
      hlep.setShrEnts(this.fctDt.getShrEnts());
      hlep.setHldsBsEnPr(this.fctDt.getHldsBsEnPr());
      rz.setHldEntPrcNm(hlep);
      rz.setFctEntPrc(lazFctEnPrc(pRvs));
      rz.setHldPrcFenNm(new HlNmPrFe());
      rz.setEntMap(new HashMap<String, Class<? extends IHasId<?>>>());
      Setng setng = lazStgUvd(pRvs);
      for (Class<? extends IHasId<?>> cls  : setng.lazClss()) {
        if (this.fctDt.getFbdEnts() == null
          || !this.fctDt.getFbdEnts().contains(cls)) {
          rz.getEntMap().put(cls.getSimpleName(), cls);
        }
      }
      this.beans.put(FctDt.HNACENRQ, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.HNACENRQ
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctPrcFen.</p>
   * @param pRvs request scoped vars
   * @return FctPrcFen
   * @throws Exception - an exception
   */
  public final synchronized FctPrcFen<RS> lazFctPrcFen(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    FctPrcFen<RS> rz = (FctPrcFen<RS>) this.beans
      .get(FctPrcFen.class.getSimpleName());
    if (rz == null) {
      rz = new FctPrcFen<RS>();
      rz.setFctBlc(this);
      this.beans.put(FctPrcFen.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctPrcFen.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HndI18nRq.</p>
   * @param pRvs request scoped vars
   * @return HndI18nRq
   * @throws Exception - an exception
   */
  public final synchronized HndI18nRq<RS> lazHndI18nRq(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    HndI18nRq<RS> rz = (HndI18nRq<RS>) this.beans
      .get(HndI18nRq.class.getSimpleName());
    if (rz == null) {
      rz = new HndI18nRq<RS>();
      rz.setNumStr(lazNumStr(pRvs));
      rz.setSrvDt(lazSrvDt(pRvs));
      rz.setOrm(lazOrm(pRvs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRvs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setLog(lazLogStd(pRvs));
      rz.setI18n(lazI18n(pRvs));
      rz.setUtJsp(new UtlJsp());
      this.beans.put(HndI18nRq.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HndI18nRq.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HndNtrRq.</p>
   * @param pRvs request scoped vars
   * @return HndNtrRq
   * @throws Exception - an exception
   */
  public final synchronized HndNtrRq lazHndNtrRqAd(
    final Map<String, Object> pRvs) throws Exception {
    HndNtrRq rz = (HndNtrRq) this.beans.get(FctDt.HNNTRQAD);
    if (rz == null) {
      rz = new HndNtrRq();
      FctPrcNtrAd<RS> fct = new FctPrcNtrAd<RS>();
      fct.setFctBlc(this);
      fct.setFctsPrc(this.fctDt.getFctsPrcAd());
      rz.setFctPrc(fct);
      this.beans.put(FctDt.HNNTRQAD, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.HNNTRQAD
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HndNtrRq.</p>
   * @param pRvs request scoped vars
   * @return HndNtrRq
   * @throws Exception - an exception
   */
  public final synchronized HndNtrRq lazHndNtrRq(
    final Map<String, Object> pRvs) throws Exception {
    HndNtrRq rz = (HndNtrRq) this.beans.get(FctDt.HNNTRQSC);
    if (rz == null) {
      rz = new HndNtrRq();
      FctPrcNtr<RS> fct = new FctPrcNtr<RS>();
      fct.setFctBlc(this);
      fct.setFctsPrc(this.fctDt.getFctsPrc());
      rz.setFctPrc(fct);
      this.beans.put(FctDt.HNNTRQSC, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.HNNTRQSC
        + " has been created.");
    }
    return rz;
  }

  //Shared replication services:
  /**
   * <p>Lazy getter HldNmCnFrStXml.</p>
   * @param pRvs request scoped vars
   * @return HldNmCnFrStXml
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnFrStXml lazHldNmCnFrStXml(
    final Map<String, Object> pRvs) throws Exception {
    HldNmCnFrStXml rz = (HldNmCnFrStXml) this.beans
      .get(HldNmCnFrStXml.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnFrStXml();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      this.beans.put(HldNmCnFrStXml.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldNmCnFrStXml.class
        .getSimpleName() + " has been created.");
    }
    return rz;
  }

  //ORM:
  /**
   * <p>Lazy getter Orm.</p>
   * @param pRvs request scoped vars
   * @return Orm
   * @throws Exception - an exception
   */
  public final synchronized Orm<RS> lazOrm(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    Orm<RS> rz = (Orm<RS>) this.beans.get(IOrm.class.getSimpleName());
    if (rz == null) {
      rz = new Orm<RS>();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgOrm(pRvs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRvs, IRdb.class.getSimpleName());
      rz.setIsAndr(this.fctDt.getIsAndr());
      rz.setNewDbId(this.fctDt.getNewDbId());
      rz.setRdb(rdb);
      rz.setSqlQu(lazSqlQu(pRvs));
      rz.setSrvClVl(lazSrvClVl(pRvs));
      rz.setFilEntRs(lazFilEntRs(pRvs));
      rz.setFilCvEn(lazFilCvEnt(pRvs));
      FctFctEnt fctFctEnt = lazFctFctEnt(pRvs);
      fctFctEnt.setOrm(rz);
      rz.setFctFctEnt(fctFctEnt);
      //initialization must be by the first invoker (servlet)
      this.beans.put(IOrm.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), Orm.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter Setng ORM.</p>
   * @param pRvs request scoped vars
   * @return Setng ORM
   * @throws Exception - an exception
   */
  public final synchronized Setng lazStgOrm(
    final Map<String, Object> pRvs) throws Exception {
    Setng rz = (Setng) this.beans.get(FctDt.STGORMNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(this.fctDt.getStgOrmDir());
      rz.setReflect(lazReflect(pRvs));
      rz.setUtlPrp(lazUtlPrp(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setLog(lazLogStd(pRvs));
      this.beans.put(FctDt.STGORMNM, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.STGORMNM
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SrvClVl.</p>
   * @param pRvs request scoped vars
   * @return SrvClVl
   * @throws Exception - an exception
   */
  public final synchronized SrvClVl lazSrvClVl(
    final Map<String, Object> pRvs) throws Exception {
    SrvClVl rz = (SrvClVl) this.beans.get(SrvClVl.class.getSimpleName());
    if (rz == null) {
      rz = new SrvClVl();
      rz.setSetng(lazStgOrm(pRvs));
      this.beans.put(SrvClVl.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), SrvClVl.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SqlQu.</p>
   * @param pRvs request scoped vars
   * @return SqlQu
   * @throws Exception - an exception
   */
  public final synchronized SqlQu lazSqlQu(
    final Map<String, Object> pRvs) throws Exception {
    SqlQu rz = (SqlQu) this.beans.get(ISqlQu.class.getSimpleName());
    if (rz == null) {
      rz = new SqlQu();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgOrm(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHldGets(lazHldGets(pRvs));
      this.beans.put(ISqlQu.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), SqlQu.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FilCvEnt.</p>
   * @param pRvs request scoped vars
   * @return FilCvEnt
   * @throws Exception - an exception
   */
  public final synchronized FilCvEnt lazFilCvEnt(
    final Map<String, Object> pRvs) throws Exception {
    FilCvEnt rz = (FilCvEnt) this.beans
      .get(FilCvEnt.class.getSimpleName());
    if (rz == null) {
      rz = new FilCvEnt();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgOrm(pRvs));
      rz.setHldFilFdNms(lazHldFilFdCv(pRvs));
      rz.setFctFilFld(lazFctFilFdCv(pRvs));
      this.beans.put(FilCvEnt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FilCvEnt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FilEntRs.</p>
   * @param pRvs request scoped vars
   * @return FilEntRs
   * @throws Exception - an exception
   */
  public final synchronized FilEntRs<RS> lazFilEntRs(
    final Map<String, Object> pRvs) throws Exception {
    FilEntRs<RS> rz = (FilEntRs<RS>) this.beans
      .get(FilEntRs.class.getSimpleName());
    if (rz == null) {
      rz = new FilEntRs<RS>();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgOrm(pRvs));
      rz.setHldFilFdNms(lazHldNmFilFdRs(pRvs));
      FctNmFilFdRs<RS> fffd = lazFctNmFilFdRs(pRvs);
      fffd.setFilEnt(rz);
      rz.setFctFilFld(fffd);
      this.beans.put(FilEntRs.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FilEntRs.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldCnvFdCv.</p>
   * @param pRvs request scoped vars
   * @return HldCnvFdCv
   * @throws Exception - an exception
   */
  public final synchronized HldCnvFdCv lazHldCnvFdCv(
    final Map<String, Object> pRvs) throws Exception {
    HldCnvFdCv rz = (HldCnvFdCv) this.beans
      .get(HldCnvFdCv.class.getSimpleName());
    if (rz == null) {
      rz = new HldCnvFdCv();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      this.beans.put(HldCnvFdCv.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        HldCnvFdCv.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmFilFdRs.</p>
   * @param pRvs request scoped vars
   * @return HldNmFilFdRs
   * @throws Exception - an exception
   */
  public final synchronized HldNmFilFdRs lazHldNmFilFdRs(
    final Map<String, Object> pRvs) throws Exception {
    HldNmFilFdRs rz = (HldNmFilFdRs) this.beans
      .get(HldNmFilFdRs.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmFilFdRs();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      this.beans.put(HldNmFilFdRs.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        HldNmFilFdRs.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldFilFdCv.</p>
   * @param pRvs request scoped vars
   * @return HldFilFdCv
   * @throws Exception - an exception
   */
  public final synchronized HldFilFdCv lazHldFilFdCv(
    final Map<String, Object> pRvs) throws Exception {
    HldFilFdCv rz = (HldFilFdCv) this.beans
      .get(HldFilFdCv.class.getSimpleName());
    if (rz == null) {
      rz = new HldFilFdCv();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      this.beans.put(HldFilFdCv.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldFilFdCv.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnFrRs.</p>
   * @param pRvs request scoped vars
   * @return HldNmCnFrRs
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnFrRs lazHldNmCnFrRs(
    final Map<String, Object> pRvs) throws Exception {
    HldNmCnFrRs rz = (HldNmCnFrRs) this.beans
      .get(HldNmCnFrRs.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnFrRs();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      this.beans.put(HldNmCnFrRs.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldNmCnFrRs.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctCnvCv.</p>
   * @param pRvs request scoped vars
   * @return FctCnvCv
   * @throws Exception - an exception
   */
  public final synchronized FctCnvCv lazFctCnvCv(
    final Map<String, Object> pRvs) throws Exception {
    FctCnvCv rz = (FctCnvCv) this.beans
      .get(FctCnvCv.class.getSimpleName());
    if (rz == null) {
      rz = new FctCnvCv();
      rz.setLogStd(lazLogStd(pRvs));
      rz.setSqlEsc(lazSqlEsc(pRvs));
      this.beans.put(FctCnvCv.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctCnvCv.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnFrRs.</p>
   * @param pRvs request scoped vars
   * @return FctNmCnFrRs
   * @throws Exception - an exception
   */
  public final synchronized FctNmCnFrRs<RS> lazFctNmCnFrRs(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    FctNmCnFrRs<RS> rz = (FctNmCnFrRs<RS>) this.beans
      .get(FctNmCnFrRs.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnFrRs<RS>();
      rz.setLogStd(lazLogStd(pRvs));
      this.beans.put(FctNmCnFrRs.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctNmCnFrRs.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctFilFdCv. Mutual dependency with FilEntRs.</p>
   * @param pRvs request scoped vars
   * @return FctFilFdCv
   * @throws Exception - an exception
   */
  public final synchronized FctFilFdCv lazFctFilFdCv(
    final Map<String, Object> pRvs) throws Exception {
    FctFilFdCv rz = (FctFilFdCv) this.beans
      .get(FctFilFdCv.class.getSimpleName());
    if (rz == null) {
      rz = new FctFilFdCv();
      rz.setLogStd(lazLogStd(pRvs));
      rz.setSetng(lazStgOrm(pRvs));
      rz.setHldGets(lazHldGets(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHldNmFdCn(lazHldCnvFdCv(pRvs));
      rz.setFctCnvFld(lazFctCnvCv(pRvs));
      rz.setHldFilFdNms(lazHldFilFdCv(pRvs));
      this.beans.put(FctFilFdCv.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        FctFilFdCv.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmFilFdRs. Mutual dependency with FilEntRs.</p>
   * @param pRvs request scoped vars
   * @return FctNmFilFdRs
   * @throws Exception - an exception
   */
  public final synchronized FctNmFilFdRs<RS> lazFctNmFilFdRs(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    FctNmFilFdRs<RS> rz = (FctNmFilFdRs<RS>) this.beans
      .get(FctNmFilFdRs.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmFilFdRs<RS>();
      rz.setLogStd(lazLogStd(pRvs));
      rz.setHldSets(lazHldSets(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHldNmFdCn(lazHldNmCnFrRs(pRvs));
      rz.setFctCnvFld(lazFctNmCnFrRs(pRvs));
      this.beans.put(FctNmFilFdRs.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        FctNmFilFdRs.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  //UVD:
  /**
   * <p>Lazy getter Setng.</p>
   * @param pRvs request scoped vars
   * @return Setng
   * @throws Exception - an exception
   */
  public final synchronized Setng lazStgUvd(
    final Map<String, Object> pRvs) throws Exception {
    Setng rz = (Setng) this.beans.get(FctDt.STGUVDNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(this.fctDt.getStgUvdDir());
      rz.setReflect(lazReflect(pRvs));
      rz.setUtlPrp(lazUtlPrp(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setLog(lazLogStd(pRvs));
      this.beans.put(FctDt.STGUVDNM, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.STGUVDNM
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FilEntRq.</p>
   * @param pRvs request scoped vars
   * @return FilEntRq
   * @throws Exception - an exception
   */
  public final synchronized FilEntRq lazFilEntRq(
    final Map<String, Object> pRvs) throws Exception {
    FilEntRq rz = (FilEntRq) this.beans
      .get(IFilEntRq.class.getSimpleName());
    if (rz == null) {
      rz = new FilEntRq();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgUvd(pRvs));
      rz.setHldFilFdNms(lazHldNmFilFdStUvd(pRvs));
      rz.setFctFilFld(lazFctNmFilFd(pRvs));
      this.beans.put(IFilEntRq.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), IFilEntRq.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter UVD HldNmFilFdSt.</p>
   * @param pRvs request scoped vars
   * @return HldNmFilFdSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmFilFdSt lazHldNmFilFdStUvd(
    final Map<String, Object> pRvs) throws Exception {
    HldNmFilFdSt rz = (HldNmFilFdSt) this.beans
      .get(FctDt.HLFILFDNMUVD);
    if (rz == null) {
      rz = new HldNmFilFdSt();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setFilHasIdNm(FctNmFilFdSt.FILHSIDSTUVDNM);
      rz.setFilSmpNm(FctNmFilFdSt.FILSMPSTUVDNM);
      rz.setSetng(lazStgUvd(pRvs));
      this.beans.put(FctDt.HLFILFDNMUVD, rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        FctDt.HLFILFDNMUVD + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnFrSt.</p>
   * @param pRvs request scoped vars
   * @return HldNmCnFrSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnFrSt lazHldNmCnFrStUvd(
    final Map<String, Object> pRvs) throws Exception {
    HldNmCnFrSt rz = (HldNmCnFrSt) this.beans
      .get(HldNmCnFrSt.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnFrSt();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setSetng(lazStgUvd(pRvs));
      this.beans.put(HldNmCnFrSt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldNmCnFrSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnToSt.</p>
   * @param pRvs request scoped vars
   * @return HldNmCnToSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnToSt lazHldNmCnToStUvd(
    final Map<String, Object> pRvs) throws Exception {
    HldNmCnToSt rz = (HldNmCnToSt) this.beans
      .get(HldNmCnToSt.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnToSt();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setCnHsIdToStNm(FctNmCnToSt.CNHSIDSTUVDNM);
      rz.setSetng(lazStgUvd(pRvs));
      this.beans.put(HldNmCnToSt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldNmCnToSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldUvd.</p>
   * @param pRvs request scoped vars
   * @return HldUvd
   * @throws Exception - an exception
   */
  public final synchronized HldUvd lazHldUvd(
    final Map<String, Object> pRvs) throws Exception {
    HldUvd rz = (HldUvd) this.beans
      .get(HldUvd.class.getSimpleName());
    if (rz == null) {
      rz = new HldUvd();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgUvd(pRvs));
      rz.setStgOrm(lazStgOrm(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHlCnToSt(lazHldNmCnToStUvd(pRvs));
      rz.setFcCnToSt(lazFctNmCnToSt(pRvs));
      rz.setHlClStgMp(this.fctDt.getHlClStgMp());
      rz.setHlFdStgMp(this.fctDt.getHlFdStgMp());
      FctCnvId fci = new FctCnvId();
      fci.setLogStd(lazLogStd(pRvs));
      fci.setSetng(lazStgUvd(pRvs));
      fci.setHldGets(lazHldGets(pRvs));
      fci.setHldFdCls(lazHldFldCls(pRvs));
      rz.setFctCnvId(fci);
      HldCnvId hci = new HldCnvId();
      hci.setCustIdClss(this.fctDt.getCustIdClss());
      rz.setHldCnvId(hci);
      this.beans.put(HldUvd.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldUvd.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnFrSt.</p>
   * @param pRvs request scoped vars
   * @return FctNmCnFrSt
   * @throws Exception - an exception
   */
  public final synchronized FctNmCnFrSt lazFctNmCnFrSt(
    final Map<String, Object> pRvs) throws Exception {
    FctNmCnFrSt rz = (FctNmCnFrSt) this.beans
      .get(FctNmCnFrSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnFrSt();
      rz.setLogStd(lazLogStd(pRvs));
      rz.setUtlXml(lazUtlXml(pRvs));
      rz.setSrvDt(lazSrvDt(pRvs));
      this.beans.put(FctNmCnFrSt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctNmCnFrSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmFilFdSt.</p>
   * @param pRvs request scoped vars
   * @return FctNmFilFdSt
   * @throws Exception - an exception
   */
  public final synchronized FctNmFilFdSt lazFctNmFilFd(
    final Map<String, Object> pRvs) throws Exception {
    FctNmFilFdSt rz = (FctNmFilFdSt) this.beans
      .get(FctNmFilFdSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmFilFdSt();
      rz.setLogStd(lazLogStd(pRvs));
      rz.setHldSets(lazHldSets(pRvs));
      rz.setStgUvd(lazStgUvd(pRvs));
      rz.setStgDbCp((ISetng) laz(pRvs, FctDt.STGDBCPNM));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHldNmFdCnUvd(lazHldNmCnFrStUvd(pRvs));
      rz.setHldNmFdCnDbCp(lazHldNmCnFrStXml(pRvs));
      rz.setFctCnvFld(lazFctNmCnFrSt(pRvs));
      rz.setHldFilFdNmsUvd(lazHldNmFilFdStUvd(pRvs));
      IHlNmClSt hlFilFd = (IHlNmClSt) laz(pRvs, FctDt.HLFILFDNMDBCP);
      rz.setHldFilFdNmsDbCp(hlFilFd);
      this.beans.put(FctNmFilFdSt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        FctNmFilFdSt.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnToSt.</p>
   * @param pRvs request scoped vars
   * @return FctNmCnToSt
   * @throws Exception - an exception
   */
  public final synchronized FctNmCnToSt lazFctNmCnToSt(
    final Map<String, Object> pRvs) throws Exception {
    FctNmCnToSt rz = (FctNmCnToSt) this.beans
      .get(FctNmCnToSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnToSt();
      rz.setUtlXml(lazUtlXml(pRvs));
      rz.setSrvDt(lazSrvDt(pRvs));
      rz.setNumStr(lazNumStr(pRvs));
      rz.setHldNmFdCnUvd(lazHldNmCnToStUvd(pRvs));
      IHlNmClSt hlFdCnDbCp = (IHlNmClSt) laz(pRvs, FctDt.HLCNTOSTDBCP);
      rz.setHldNmFdCnDbcp(hlFdCnDbCp);
      rz.setHldGets(lazHldGets(pRvs));
      rz.setStgUvd(lazStgUvd(pRvs));
      rz.setStgDbCp((ISetng) laz(pRvs, FctDt.STGDBCPNM));
      rz.setLogStd(lazLogStd(pRvs));
      this.beans.put(FctNmCnToSt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctNmCnToSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  //Common parts:
  /**
   * <p>Lazy getter HldSets.</p>
   * @param pRvs request scoped vars
   * @return HldSets
   * @throws Exception - an exception
   */
  public final synchronized HldSets lazHldSets(
    final Map<String, Object> pRvs) throws Exception {
    HldSets rz = (HldSets) this.beans
      .get(HldSets.class.getSimpleName());
    if (rz == null) {
      rz = new HldSets();
      rz.setReflect(lazReflect(pRvs));
      this.beans.put(HldSets.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldSets.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldGets.</p>
   * @param pRvs request scoped vars
   * @return HldGets
   * @throws Exception - an exception
   */
  public final synchronized HldGets lazHldGets(
    final Map<String, Object> pRvs) throws Exception {
    HldGets rz = (HldGets) this.beans
      .get(HldGets.class.getSimpleName());
    if (rz == null) {
      rz = new HldGets();
      rz.setReflect(lazReflect(pRvs));
      this.beans.put(HldGets.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldGets.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldFldCls.</p>
   * @param pRvs request scoped vars
   * @return HldFldCls
   * @throws Exception - an exception
   */
  public final synchronized HldFldCls lazHldFldCls(
    final Map<String, Object> pRvs) throws Exception {
    HldFldCls rz = (HldFldCls) this.beans
      .get(HldFldCls.class.getSimpleName());
    if (rz == null) {
      rz = new HldFldCls();
      rz.setReflect(lazReflect(pRvs));
      this.beans.put(HldFldCls.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HldFldCls.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctEnPrc.</p>
   * @param pRvs request scoped vars
   * @return FctEnPrc
   * @throws Exception - an exception
   */
  public final synchronized FctEnPrc<RS> lazFctEnPrc(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> rz = (FctEnPrc<RS>) this.beans
      .get(FctEnPrc.class.getSimpleName());
    if (rz == null) {
      rz = new FctEnPrc<RS>();
      rz.setFctBlc(this);
      rz.setFctsPrc(this.fctDt.getFctsPrcEnt());
      this.beans.put(FctEnPrc.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctEnPrc.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctFctEnt IT DEPENDS OF ORM!!</p>
   * @param pRvs request scoped vars
   * @return FctFctEnt
   * @throws Exception - an exception
   */
  public final synchronized FctFctEnt lazFctFctEnt(
    final Map<String, Object> pRvs) throws Exception {
    FctFctEnt rz = (FctFctEnt) this.beans.get(FctFctEnt.class.getSimpleName());
    if (rz == null) {
      rz = new FctFctEnt();
      this.beans.put(FctFctEnt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FctFctEnt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HlpEntPg.</p>
   * @param pRvs request scoped vars
   * @return HlpEntPg
   * @throws Exception - an exception
   */
  public final synchronized HlpEntPg<RS> lazHlpEntPg(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    HlpEntPg<RS> rz = (HlpEntPg<RS>) this.beans
      .get(HlpEntPg.class.getSimpleName());
    if (rz == null) {
      rz = new HlpEntPg<RS>();
      rz.setLog(lazLogStd(pRvs));
      rz.setI18n(lazI18n(pRvs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRvs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setOrm(lazOrm(pRvs));
      rz.setSrvPg(lazSrvPg(pRvs));
      rz.setSqlQu(lazSqlQu(pRvs));
      rz.setSrvDt(lazSrvDt(pRvs));
      rz.setHldUvd(lazHldUvd(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      this.beans.put(HlpEntPg.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HlpEntPg.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter UtlPrp.</p>
   * @param pRvs request scoped vars
   * @return UtlPrp
   * @throws Exception - an exception
   */
  public final synchronized UtlPrp lazUtlPrp(
    final Map<String, Object> pRvs) throws Exception {
    UtlPrp rz = (UtlPrp) this.beans.get(UtlPrp.class.getSimpleName());
    if (rz == null) {
      rz = new UtlPrp();
      this.beans.put(UtlPrp.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), UtlPrp.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter NumStr.</p>
   * @param pRvs request scoped vars
   * @return NumStr
   * @throws Exception - an exception
   */
  public final synchronized NumStr lazNumStr(
    final Map<String, Object> pRvs) throws Exception {
    NumStr rz = (NumStr) this.beans.get(INumStr.class.getSimpleName());
    if (rz == null) {
      rz = new NumStr();
      this.beans.put(INumStr.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), INumStr.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SqlEsc.</p>
   * @param pRvs request scoped vars
   * @return SqlEsc
   * @throws Exception - an exception
   */
  public final synchronized SqlEsc lazSqlEsc(
    final Map<String, Object> pRvs) throws Exception {
    SqlEsc rz = (SqlEsc) this.beans.get(ISqlEsc.class.getSimpleName());
    if (rz == null) {
      rz = new SqlEsc();
      this.beans.put(ISqlEsc.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), ISqlEsc.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter UtlXml.</p>
   * @param pRvs request scoped vars
   * @return UtlXml
   * @throws Exception - an exception
   */
  public final synchronized UtlXml lazUtlXml(
    final Map<String, Object> pRvs) throws Exception {
    UtlXml rz = (UtlXml) this.beans.get(IUtlXml.class.getSimpleName());
    if (rz == null) {
      rz = new UtlXml();
      this.beans.put(IUtlXml.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), IUtlXml.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter I18n.</p>
   * @param pRvs request scoped vars
   * @return I18n
   * @throws Exception - an exception
   */
  public final synchronized I18n lazI18n(
    final Map<String, Object> pRvs) throws Exception {
    I18n rz = (I18n) this.beans.get(II18n.class.getSimpleName());
    if (rz == null) {
      rz = new I18n();
      rz.setLog(lazLogStd(pRvs));
      rz.add(this.fctDt.getLngCntr().split(","));
      this.beans.put(II18n.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), II18n.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SrvPg.</p>
   * @param pRvs request scoped vars
   * @return SrvPg
   * @throws Exception - an exception
   */
  public final synchronized SrvPg lazSrvPg(
    final Map<String, Object> pRvs) throws Exception {
    SrvPg rz = (SrvPg) this.beans.get(ISrvPg.class.getSimpleName());
    if (rz == null) {
      rz = new SrvPg();
      this.beans.put(ISrvPg.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), ISrvPg.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SrvDt.</p>
   * @param pRvs request scoped vars
   * @return SrvDt
   * @throws Exception - an exception
   */
  public final synchronized SrvDt lazSrvDt(
    final Map<String, Object> pRvs) throws Exception {
    SrvDt rz = (SrvDt) this.beans.get(ISrvDt.class.getSimpleName());
    if (rz == null) {
      rz = new SrvDt();
      this.beans.put(ISrvDt.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), ISrvDt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter Reflect.</p>
   * @param pRvs request scoped vars
   * @return Reflect
   * @throws Exception - an exception
   */
  public final synchronized Reflect lazReflect(
    final Map<String, Object> pRvs) throws Exception {
    Reflect rz = (Reflect) this.beans.get(IReflect.class.getSimpleName());
    if (rz == null) {
      rz = new Reflect();
      this.beans.put(IReflect.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), IReflect.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter secure logger.</p>
   * @param pRvs request scoped vars
   * @return Log
   * @throws Exception - an exception
   */
  public final synchronized LogFile lazLogSec(
    final Map<String, Object> pRvs) throws Exception {
    LogFile logSec = (LogFile) this.beans.get(FctDt.LOGSECNM);
    if (logSec == null) {
      logSec = new LogFile();
      logSec.setPath(this.fctDt.getLogPth() + File.separator + FctDt.LOGSECNM);
      logSec.setClsImm(true);
      logSec.setMaxSize(this.fctDt.getLogSize());
      this.beans.put(FctDt.LOGSECNM, logSec);
      lazLogStd(pRvs).info(pRvs, getClass(), FctDt.LOGSECNM
        + " has been created");
    }
    return logSec;
  }

  /**
   * <p>Lazy getter standard loger.</p>
   * @param pRvs request scoped vars
   * @return Log
   * @throws Exception - an exception
   */
  public final synchronized ILog lazLogStd(
    final Map<String, Object> pRvs) throws Exception {
    if (this.logStd == null) {
      LogFile logFl = new LogFile();
      logFl.setPath(this.fctDt.getLogPth() + File.separator
        + this.fctDt.getLogStdNm());
      logFl.setDbgSh(this.fctDt.getDbgSh());
      logFl.setDbgFl(this.fctDt.getDbgFl());
      logFl.setDbgCl(this.fctDt.getDbgCl());
      logFl.setMaxSize(this.fctDt.getLogSize());
      this.logStd = logFl;
      this.beans.put(FctDt.LOGSTDNM, this.logStd);
      this.logStd.info(pRvs, getClass(), FctDt.LOGSTDNM + " has been created");
    }
    return this.logStd;
  }

  /**
   * <p>Setter standard logger.</p>
   * @param pLog logger
   * @throws Exception - an exception
   */
  public final synchronized void setLogStd(final ILog pLog) throws Exception {
    if (this.logStd != null) {
      throw new ExcCode(ExcCode.WRPR, "Log STD exists!");
    }
    this.logStd = pLog;
    this.beans.put(FctDt.LOGSTDNM, this.logStd);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for List<IFctAux<RS>>.</p>
   * @return List<IFctAux<RS>>
   **/
  public final synchronized List<IFctAux<RS>> getFctsAux() {
    return this.fctsAux;
  }

  /**
   * <p>Getter for fctDt.</p>
   * @return FctDt
   **/
  public final FctDt getFctDt() {
    return this.fctDt;
  }
}
