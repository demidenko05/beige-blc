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
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.hld.IHldNm;
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
import org.beigesoft.hld.HlNmAcEnPr;
import org.beigesoft.hld.HlNmAdEnPr;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.HldFldStg;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.hld.HldCnvId;
import org.beigesoft.hnd.HndEntRq;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.hnd.HndNtrRq;
import org.beigesoft.prp.UtlPrp;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.log.LogFile;
import org.beigesoft.cnv.FilEntRs;
import org.beigesoft.cnv.FilEntRq;
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

  /**
   * <p>UVD holder field fillers names name.</p>
   **/
  public static final String HLFILFDNMUVD = "hlFiFdNmUvd";

  /**
   * <p>UVD Setting service name.</p>
   **/
  public static final String STGUVDNM = "stgUvd";

  /**
   * <p>DB-Copy Setting service name.</p>
   **/
  public static final String STGDBCPNM = "stgDbCp";

  /**
   * <p>DB-Copy holder converters names to string name.</p>
   **/
  public static final String HLCNTOSTDBCP = "hlCnToStDbCp";

  /**
   * <p>DB-Copy holder filler fields names name.</p>
   **/
  public static final String HLFILFDNMDBCP = "hlFiFdNmDbCp";

  /**
   * <p>ORM Setting service name.</p>
   **/
  public static final String STGORMNM = "stgOrm";

  /**
   * <p>Standard logger name.</p>
   **/
  public static final String LOGSTDNM = "logStd";

  /**
   * <p>Secure logger name.</p>
   **/
  public static final String LOGSECNM = "logSec";

  /**
   * <p>Handler base, secure non-transaction requests name.</p>
   **/
  public static final String HNNTRQSC = "hnNtRqSc";

  /**
   * <p>Handler admin, secure non-transaction requests name.</p>
   **/
  public static final String HNNTRQAD = "hnNtRqAd";

  /**
   * <p>Handler admin/web-store entities request name.</p>
   **/
  public static final String HNADENRQ = "hnAdEnRq";

  /**
   * <p>Handler base entities (no admin, etc) request name.</p>
   **/
  public static final String HNACENRQ = "hnAcEnRq";

  /**
   * <p>Processor admin/web-store entities page name.</p>
   **/
  public static final String PRADENTPG = "prAdEnPg";

  /**
   * <p>Processor base entities (no admin, etc) page name.</p>
   **/
  public static final String PRACENTPG = "prAcEnPg";

  //configuration data:
  /**
   * <p>Database full copy setting base dir.</p>
   **/
  private String stgDbCpDir;

  /**
   * <p>UVD setting base dir.</p>
   **/
  private String stgUvdDir;

  /**
   * <p>ORM setting base dir.</p>
   **/
  private String stgOrmDir;

  /**
   * <p>Languages, countries.</p>
   **/
  private String lngCntr = "en,US";

  /**
   * <p>DB data-source or JDBC class.</p>
   **/
  private String dbCls;

  /**
   * <p>DB URL.</p>
   **/
  private String dbUrl;

  /**
   * <p>DB user.</p>
   **/
  private String dbUsr;

  /**
   * <p>DB password.</p>
   **/
  private String dbPwd;

  /**
   * <p>Android configuration, RDB insert returns autogenerated ID,
   * updating with expression like "VER=VER+1" is not possible.</p>
   **/
  private boolean isAndr = false;

  /**
   * <p>New database ID.</p>
   **/
  private int newDbId = 1;

  /**
   * <p>Is show debug messages main flag, false default.</p>
   **/
  private boolean dbgSh = false;

  /**
   * <p>Get preferred detail level floor, 0 default.</p>
   **/
  private int dbgFl = 0;

  /**
   * <p>Get preferred detail level ceiling, 99999999 default.</p>
   **/
  private int dbgCl = 99999999;

  /**
   * <p>Transaction isolation for changing DB phase.</p>
   **/
  private Integer writeTi = IRdb.TRRUC;

  /**
   * <p>Transaction isolation for reading DB phase.</p>
   **/
  private Integer readTi = IRdb.TRRC;

  /**
   * <p>Transaction isolation for writing and reading DB phase.</p>
   **/
  private Integer writeReTi = IRdb.TRRUC;

  /**
   * <p>Writing and reading phases in separated transactions.</p>
   **/
  private Boolean wrReSpTr = Boolean.FALSE;

  /**
   * <p>Log files path.</p>
   **/
  private String logPth;

  /**
   * <p>Log files size in bytes.</p>
   **/
  private int logSize = 1048576;

  /**
   * <p>Standard log file name.</p>
   **/
  private String logStdNm = LOGSTDNM;

  /**
   * <p>WEB-APP files path.</p>
   **/
  private String appPth;

  /**
   * <p>Upload directory relative to WEB-APP path
   * without start and end separator, e.g. "static/uploads".</p>
   **/
  private String uplDir;

  /**
   * <p>Admin/webstore non-shared entities.</p>
   **/
  private List<Class<?>> admEnts;

  /**
   * <p>Forbidden entities for base entity request handler, e.g. UsTmc.</p>
   **/
  private List<Class<?>> fbdEnts;

  /**
   * <p>Shared non-editable entities for base entity request handler,
   * e.g. email connection EmCon.</p>
   **/
  private List<Class<?>> shrEnts;

  /**
   * <p>Holders class string settings.</p>
   **/
  private Map<String, HldClsStg> hlClStgMp;

  /**
   * <p>Holders filed string settings.</p>
   **/
  private Map<String, HldFldStg> hlFdStgMp;

  /**
   * <p>Set of classes with custom ID (composite, ID is foreign entity or custom
   * ID name).</p>
   **/
  private Set<Class<?>> custIdClss;

  //parts/services:
  /**
   * <p>Outside app-beans/parts factories final configuration.
   *  They put its beans into this main factory beans.</p>
   **/
  private final List<IFctAux<RS>> fctsAux = new ArrayList<IFctAux<RS>>();

    //cached services/parts:
  /**
   * <p>Logger.</p>
   **/
  private LogFile logStd;

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
          } else if (HNNTRQAD.equals(pBnNm)) {
            rz = lazHndNtrRqAd(pRvs);
          } else if (HNNTRQSC.equals(pBnNm)) {
            rz = lazHndNtrRq(pRvs);
          } else if (HNACENRQ.equals(pBnNm)) {
            rz = lazHnAcEnRq(pRvs);
          } else if (HNADENRQ.equals(pBnNm)) {
            rz = lazHnAdEnRq(pRvs);
          } else if (FctPrcFen.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctPrcFen(pRvs);
          } else if (STGORMNM.equals(pBnNm)) {
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
          } else if (FilEntRq.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilEntRq(pRvs);
          } else if (STGUVDNM.equals(pBnNm)) {
            rz = lazStgUvd(pRvs);
          } else if (FctNmCnFrSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnFrSt(pRvs);
          } else if (FctNmFilFdSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmFilFd(pRvs);
          } else if (FctNmCnToSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnToSt(pRvs);
          } else if (HldUvd.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldUvd(pRvs);
          } else if (HLFILFDNMUVD.equals(pBnNm)) {
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
          } else if (LOGSTDNM.equals(pBnNm)) {
            rz = lazLogStd(pRvs);
          } else if (LOGSECNM.equals(pBnNm)) {
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
    if (this.logStd != null) {
      this.logStd.info(pRvs, getClass(), "Send stop to LOG STD...");
      this.logStd.setNeedRun(false);
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
    HndEntRq<RS> rz = (HndEntRq<RS>) this.beans.get(HNADENRQ);
    if (rz == null) {
      rz = new HndEntRq<RS>();
      rz.setWriteTi(getWriteTi());
      rz.setReadTi(getReadTi());
      rz.setWriteReTi(getWriteReTi());
      rz.setWrReSpTr(getWrReSpTr());
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
      rz.setHldEntPrcNm(new HlNmAdEnPr());
      FctAcEnPrc<RS> fep = new FctAcEnPrc<RS>();
      fep.setFctBlc(this);
      rz.setFctEntPrc(fep);
      rz.setEntMap(new HashMap<String, Class<?>>());
      Setng setng = lazStgUvd(pRvs);
      for (Class<?> cls  : setng.lazClss()) {
        if (this.admEnts == null || this.admEnts.contains(cls)) {
          rz.getEntMap().put(cls.getSimpleName(), cls);
        }
      }
      /*if (this.admEnts != null) {
        this.admEnts.clear();
      }*/
      this.beans.put(HNADENRQ, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HNADENRQ + " has been created.");
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
    HndEntRq<RS> rz = (HndEntRq<RS>) this.beans.get(HNACENRQ);
    if (rz == null) {
      rz = new HndEntRq<RS>();
      rz.setWriteTi(getWriteTi());
      rz.setReadTi(getReadTi());
      rz.setWriteReTi(getWriteReTi());
      rz.setWrReSpTr(getWrReSpTr());
      rz.setLogStd(lazLogStd(pRvs));
      rz.setLogSec(lazLogSec(pRvs));
      rz.setHldUvd(lazHldUvd(pRvs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRvs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setFilEntRq(lazFilEntRq(pRvs));
      rz.setFctFctEnt(lazFctFctEnt(pRvs));
      rz.setFctPrcFen(lazFctPrcFen(pRvs));
      HlNmAcEnPr hlep = new HlNmAcEnPr();
      hlep.setShrEnts(this.shrEnts);
      rz.setHldEntPrcNm(hlep);
      FctAcEnPrc<RS> fep = new FctAcEnPrc<RS>();
      fep.setFctBlc(this);
      rz.setFctEntPrc(fep);
      rz.setHldPrcFenNm(new HlNmPrFe());
      rz.setEntMap(new HashMap<String, Class<?>>());
      Setng setng = lazStgUvd(pRvs);
      for (Class<?> cls  : setng.lazClss()) {
        if (this.fbdEnts == null || !this.fbdEnts.contains(cls)) {
          rz.getEntMap().put(cls.getSimpleName(), cls);
        }
      }
      /*if (this.fbdEnts != null) {
        this.fbdEnts.clear();
      }*/
      this.beans.put(HNACENRQ, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HNACENRQ + " has been created.");
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
    HndNtrRq rz = (HndNtrRq) this.beans.get(HNNTRQAD);
    if (rz == null) {
      rz = new HndNtrRq();
      FctPrcNtrAd<RS> fct = new FctPrcNtrAd<RS>();
      fct.setFctBlc(this);
      rz.setFctPrc(fct);
      this.beans.put(HNNTRQAD, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HNNTRQAD
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
    HndNtrRq rz = (HndNtrRq) this.beans.get(HNNTRQSC);
    if (rz == null) {
      rz = new HndNtrRq();
      FctPrcNtr<RS> fct = new FctPrcNtr<RS>();
      fct.setFctBlc(this);
      rz.setFctPrc(fct);
      this.beans.put(HNNTRQSC, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), HNNTRQSC
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
      rz.setIsAndr(this.isAndr);
      rz.setNewDbId(this.newDbId);
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
    Setng rz = (Setng) this.beans.get(STGORMNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(getStgOrmDir());
      rz.setReflect(lazReflect(pRvs));
      rz.setUtlPrp(lazUtlPrp(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setLog(lazLogStd(pRvs));
      this.beans.put(STGORMNM, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), STGORMNM + " has been created.");
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
  public final synchronized FilCvEnt<IHasId<?>, ?> lazFilCvEnt(
    final Map<String, Object> pRvs) throws Exception {
    @SuppressWarnings("unchecked")
    FilCvEnt<IHasId<?>, ?> rz = (FilCvEnt<IHasId<?>, ?>) this.beans
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
    Setng rz = (Setng) this.beans.get(STGUVDNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(getStgUvdDir());
      rz.setReflect(lazReflect(pRvs));
      rz.setUtlPrp(lazUtlPrp(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setLog(lazLogStd(pRvs));
      this.beans.put(STGUVDNM, rz);
      lazLogStd(pRvs).info(pRvs, getClass(), STGUVDNM + " has been created.");
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
      .get(FilEntRq.class.getSimpleName());
    if (rz == null) {
      rz = new FilEntRq();
      rz.setLog(lazLogStd(pRvs));
      rz.setSetng(lazStgUvd(pRvs));
      rz.setHldFilFdNms(lazHldNmFilFdStUvd(pRvs));
      rz.setFctFilFld(lazFctNmFilFd(pRvs));
      this.beans.put(FilEntRq.class.getSimpleName(), rz);
      lazLogStd(pRvs).info(pRvs, getClass(), FilEntRq.class.getSimpleName()
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
      .get(HLFILFDNMUVD);
    if (rz == null) {
      rz = new HldNmFilFdSt();
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setFilHasIdNm(FctNmFilFdSt.FILHSIDSTUVDNM);
      rz.setFilSmpNm(FctNmFilFdSt.FILSMPSTUVDNM);
      rz.setSetng(lazStgUvd(pRvs));
      this.beans.put(HLFILFDNMUVD, rz);
      lazLogStd(pRvs).info(pRvs, getClass(),
        HLFILFDNMUVD + " has been created.");
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
      rz.setSetng(lazStgUvd(pRvs));
      rz.setStgOrm(lazStgOrm(pRvs));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHlCnToSt(lazHldNmCnToStUvd(pRvs));
      rz.setFcCnToSt(lazFctNmCnToSt(pRvs));
      rz.setHlClStgMp(this.hlClStgMp);
      rz.setHlFdStgMp(this.hlFdStgMp);
      FctCnvId fci = new FctCnvId();
      fci.setLogStd(lazLogStd(pRvs));
      fci.setSetng(lazStgUvd(pRvs));
      fci.setHldGets(lazHldGets(pRvs));
      fci.setHldFdCls(lazHldFldCls(pRvs));
      rz.setFctCnvId(fci);
      HldCnvId hci = new HldCnvId();
      hci.setCustIdClss(this.custIdClss);
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
      rz.setStgDbCp((ISetng) laz(pRvs, STGDBCPNM));
      rz.setHldFdCls(lazHldFldCls(pRvs));
      rz.setHldNmFdCnUvd(lazHldNmCnFrStUvd(pRvs));
      rz.setHldNmFdCnDbCp(lazHldNmCnFrStXml(pRvs));
      rz.setFctCnvFld(lazFctNmCnFrSt(pRvs));
      rz.setHldFilFdNmsUvd(lazHldNmFilFdStUvd(pRvs));
      @SuppressWarnings("unchecked")
      IHldNm<Class<?>, String> hlFilFd = (IHldNm<Class<?>, String>)
        laz(pRvs, HLFILFDNMDBCP);
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
      rz.setNumStr(lazNumStr(pRvs));
      rz.setHldNmFdCnUvd(lazHldNmCnToStUvd(pRvs));
      @SuppressWarnings("unchecked")
      IHldNm<Class<?>, String> hlFdCnDbCp = (IHldNm<Class<?>, String>)
        laz(pRvs, HLCNTOSTDBCP);
      rz.setHldNmFdCnDbcp(hlFdCnDbCp);
      rz.setHldGets(lazHldGets(pRvs));
      rz.setStgUvd(lazStgUvd(pRvs));
      rz.setStgDbCp((ISetng) laz(pRvs, STGDBCPNM));
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
      rz.add(this.lngCntr.split(","));
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
    LogFile logSec = (LogFile) this.beans.get(LOGSECNM);
    if (logSec == null) {
      logSec = new LogFile();
      logSec.setPath(this.logPth + File.separator + LOGSECNM);
      logSec.setDbgSh(getDbgSh());
      logSec.setDbgFl(getDbgFl());
      logSec.setDbgCl(getDbgCl());
      logSec.setMaxSize(this.logSize);
      this.beans.put(LOGSECNM, logSec);
      lazLogStd(pRvs).info(pRvs, getClass(), LOGSECNM + " has been created");
    }
    return logSec;
  }

  /**
   * <p>Lazy getter standard loger.</p>
   * @param pRvs request scoped vars
   * @return Log
   * @throws Exception - an exception
   */
  public final synchronized LogFile lazLogStd(
    final Map<String, Object> pRvs) throws Exception {
    if (this.logStd == null) {
      this.logStd = new LogFile();
      this.logStd.setPath(this.logPth + File.separator + this.logStdNm);
      this.logStd.setDbgSh(getDbgSh());
      this.logStd.setDbgFl(getDbgFl());
      this.logStd.setDbgCl(getDbgCl());
      this.logStd.setMaxSize(this.logSize);
      this.beans.put(LOGSTDNM, this.logStd);
      this.logStd.info(pRvs, getClass(), LOGSTDNM + " has been created");
    }
    return this.logStd;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctConf.</p>
   * @return IFctNm
   **/
  public final synchronized List<IFctAux<RS>> getFctsAux() {
    return this.fctsAux;
  }

  /**
   * <p>Getter for stgDbCpDir.</p>
   * @return String
   **/
  public final synchronized String getStgDbCpDir() {
    return this.stgDbCpDir;
  }

  /**
   * <p>Setter for stgDbCpDir.</p>
   * @param pStgDbCpDir reference
   **/
  public final synchronized void setStgDbCpDir(final String pStgDbCpDir) {
    this.stgDbCpDir = pStgDbCpDir;
  }

  /**
   * <p>Getter for stgOrmDir.</p>
   * @return String
   **/
  public final synchronized String getStgOrmDir() {
    return this.stgOrmDir;
  }

  /**
   * <p>Setter for stgOrmDir.</p>
   * @param pStgOrmDir reference
   **/
  public final synchronized void setStgOrmDir(final String pStgOrmDir) {
    this.stgOrmDir = pStgOrmDir;
  }

  /**
   * <p>Getter for stgUvdDir.</p>
   * @return String
   **/
  public final synchronized String getStgUvdDir() {
    return this.stgUvdDir;
  }

  /**
   * <p>Setter for stgUvdDir.</p>
   * @param pStgUvdDir reference
   **/
  public final synchronized void setStgUvdDir(final String pStgUvdDir) {
    this.stgUvdDir = pStgUvdDir;
  }

  /**
   * <p>Getter for lngCntr.</p>
   * @return String
   **/
  public final synchronized String getLngCntr() {
    return this.lngCntr;
  }

  /**
   * <p>Setter for lngCntr.</p>
   * @param pLngCntr reference
   **/
  public final synchronized void setLngCntr(final String pLngCntr) {
    this.lngCntr = pLngCntr;
  }

  /**
   * <p>Getter for dbCls.</p>
   * @return String
   **/
  public final synchronized String getDbCls() {
    return this.dbCls;
  }

  /**
   * <p>Setter for dbCls.</p>
   * @param pDbCls reference
   **/
  public final synchronized void setDbCls(final String pDbCls) {
    this.dbCls = pDbCls;
  }

  /**
   * <p>Getter for dbUrl.</p>
   * @return String
   **/
  public final synchronized String getDbUrl() {
    return this.dbUrl;
  }

  /**
   * <p>Setter for dbUrl.</p>
   * @param pDbUrl reference
   **/
  public final synchronized void setDbUrl(final String pDbUrl) {
    this.dbUrl = pDbUrl;
  }

  /**
   * <p>Getter for dbUsr.</p>
   * @return String
   **/
  public final synchronized String getDbUsr() {
    return this.dbUsr;
  }

  /**
   * <p>Setter for dbUsr.</p>
   * @param pDbUsr reference
   **/
  public final synchronized void setDbUsr(final String pDbUsr) {
    this.dbUsr = pDbUsr;
  }

  /**
   * <p>Getter for dbPwd.</p>
   * @return String
   **/
  public final synchronized String getDbPwd() {
    return this.dbPwd;
  }

  /**
   * <p>Setter for dbPwd.</p>
   * @param pDbPwd reference
   **/
  public final synchronized void setDbPwd(final String pDbPwd) {
    this.dbPwd = pDbPwd;
  }

  /**
   * <p>Getter for isAndr.</p>
   * @return boolean
   **/
  public final synchronized boolean getIsAndr() {
    return this.isAndr;
  }

  /**
   * <p>Setter for isAndr.</p>
   * @param pIsAndr reference
   **/
  public final synchronized void setIsAndr(final boolean pIsAndr) {
    this.isAndr = pIsAndr;
  }

  /**
   * <p>Getter for newDbId.</p>
   * @return int
   **/
  public final synchronized int getNewDbId() {
    return this.newDbId;
  }

  /**
   * <p>Setter for newDbId.</p>
   * @param pNewDbId reference
   **/
  public final synchronized void setNewDbId(final int pNewDbId) {
    this.newDbId = pNewDbId;
  }

  /**
   * <p>Getter for dbgSh.</p>
   * @return boolean
   **/
  public final synchronized boolean getDbgSh() {
    return this.dbgSh;
  }

  /**
   * <p>Setter for dbgSh.</p>
   * @param pDbgSh reference
   **/
  public final synchronized void setDbgSh(final boolean pDbgSh) {
    this.dbgSh = pDbgSh;
  }

  /**
   * <p>Getter for dbgFl.</p>
   * @return int
   **/
  public final synchronized int getDbgFl() {
    return this.dbgFl;
  }

  /**
   * <p>Setter for dbgFl.</p>
   * @param pDbgFl reference
   **/
  public final synchronized void setDbgFl(final int pDbgFl) {
    this.dbgFl = pDbgFl;
  }

  /**
   * <p>Getter for dbgCl.</p>
   * @return int
   **/
  public final synchronized int getDbgCl() {
    return this.dbgCl;
  }

  /**
   * <p>Setter for dbgCl.</p>
   * @param pDbgCl reference
   **/
  public final synchronized void setDbgCl(final int pDbgCl) {
    this.dbgCl = pDbgCl;
  }

  /**
   * <p>Getter for writeTi.</p>
   * @return Integer
   **/
  public final synchronized Integer getWriteTi() {
    return this.writeTi;
  }

  /**
   * <p>Setter for writeTi.</p>
   * @param pWriteTi reference
   **/
  public final synchronized void setWriteTi(final Integer pWriteTi) {
    this.writeTi = pWriteTi;
  }

  /**
   * <p>Getter for readTi.</p>
   * @return Integer
   **/
  public final synchronized Integer getReadTi() {
    return this.readTi;
  }

  /**
   * <p>Setter for readTi.</p>
   * @param pReadTi reference
   **/
  public final synchronized void setReadTi(final Integer pReadTi) {
    this.readTi = pReadTi;
  }

  /**
   * <p>Getter for writeReTi.</p>
   * @return Integer
   **/
  public final synchronized Integer getWriteReTi() {
    return this.writeReTi;
  }

  /**
   * <p>Setter for writeReTi.</p>
   * @param pWriteReTi reference
   **/
  public final synchronized void setWriteReTi(final Integer pWriteReTi) {
    this.writeReTi = pWriteReTi;
  }

  /**
   * <p>Getter for wrReSpTr.</p>
   * @return Boolean
   **/
  public final synchronized Boolean getWrReSpTr() {
    return this.wrReSpTr;
  }

  /**
   * <p>Setter for wrReSpTr.</p>
   * @param pWrReSpTr reference
   **/
  public final synchronized void setWrReSpTr(final Boolean pWrReSpTr) {
    this.wrReSpTr = pWrReSpTr;
  }

  /**
   * <p>Getter for logPth.</p>
   * @return String
   **/
  public final synchronized String getLogPth() {
    return this.logPth;
  }

  /**
   * <p>Setter for logPth.</p>
   * @param pLogPth reference
   **/
  public final synchronized void setLogPth(final String pLogPth) {
    this.logPth = pLogPth;
  }

  /**
   * <p>Getter for logSize.</p>
   * @return size
   **/
  public final synchronized int getLogSize() {
    return this.logSize;
  }

  /**
   * <p>Setter for logSize.</p>
   * @param pLogSize value
   **/
  public final synchronized void setLogSize(final int pLogSize) {
    this.logSize = pLogSize;
  }

  /**
   * <p>Getter for logStdNm.</p>
   * @return String
   **/
  public final synchronized String getLogStdNm() {
    return this.logStdNm;
  }

  /**
   * <p>Setter for logStdNm.</p>
   * @param pLogStdNm reference
   **/
  public final synchronized void setLogStdNm(final String pLogStdNm) {
    this.logStdNm = pLogStdNm;
  }

  /**
   * <p>Getter for appPth.</p>
   * @return String
   **/
  public final synchronized String getAppPth() {
    return this.appPth;
  }

  /**
   * <p>Setter for appPth.</p>
   * @param pAppPth reference
   **/
  public final synchronized void setAppPth(final String pAppPth) {
    this.appPth = pAppPth;
  }

  /**
   * <p>Getter for shrEnts.</p>
   * @return List<Class<?>>
   **/
  public final synchronized List<Class<?>> getShrEnts() {
    return this.shrEnts;
  }

  /**
   * <p>Setter for shrEnts.</p>
   * @param pShrEnts reference
   **/
  public final synchronized void setShrEnts(final List<Class<?>> pShrEnts) {
    this.shrEnts = pShrEnts;
  }

  /**
   * <p>Getter for admEnts.</p>
   * @return List<Class<?>>
   **/
  public final synchronized List<Class<?>> getAdmEnts() {
    return this.admEnts;
  }

  /**
   * <p>Setter for admEnts.</p>
   * @param pAdmEnts reference
   **/
  public final synchronized void setAdmEnts(final List<Class<?>> pAdmEnts) {
    this.admEnts = pAdmEnts;
  }

  /**
   * <p>Getter for fbdEnts.</p>
   * @return List<Class<?>>
   **/
  public final synchronized List<Class<?>> getFbdEnts() {
    return this.fbdEnts;
  }

  /**
   * <p>Setter for fbdEnts.</p>
   * @param pFbdEnts reference
   **/
  public final synchronized void setFbdEnts(final List<Class<?>> pFbdEnts) {
    this.fbdEnts = pFbdEnts;
  }

  /**
   * <p>Getter for hlClStgMp.</p>
   * @return Map<String, HldClsStg>
   **/
  public final synchronized Map<String, HldClsStg> getHlClStgMp() {
    return this.hlClStgMp;
  }

  /**
   * <p>Setter for hlClStgMp.</p>
   * @param pHlClStgMp reference
   **/
  public final synchronized void setHlClStgMp(
    final Map<String, HldClsStg> pHlClStgMp) {
    this.hlClStgMp = pHlClStgMp;
  }

  /**
   * <p>Getter for hlFdStgMp.</p>
   * @return Map<String, HldFldStg>
   **/
  public final synchronized Map<String, HldFldStg> getHlFdStgMp() {
    return this.hlFdStgMp;
  }

  /**
   * <p>Setter for hlFdStgMp.</p>
   * @param pHlFdStgMp reference
   **/
  public final synchronized void setHlFdStgMp(
    final Map<String, HldFldStg> pHlFdStgMp) {
    this.hlFdStgMp = pHlFdStgMp;
  }

  /**
   * <p>Getter for custIdClss.</p>
   * @return Set<Class<?>>
   **/
  public final synchronized Set<Class<?>> getCustIdClss() {
    return this.custIdClss;
  }

  /**
   * <p>Setter for custIdClss.</p>
   * @param pCustIdClss reference
   **/
  public final synchronized void setCustIdClss(
    final Set<Class<?>> pCustIdClss) {
    this.custIdClss = pCustIdClss;
  }

  /**
   * <p>Getter for uplDir.</p>
   * @return String
   **/
  public final synchronized String getUplDir() {
    return this.uplDir;
  }

  /**
   * <p>Setter for uplDir.</p>
   * @param pUplDir reference
   **/
  public final synchronized void setUplDir(final String pUplDir) {
    this.uplDir = pUplDir;
  }
}
