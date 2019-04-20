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
import org.beigesoft.hld.HldNmCnToStXml;
import org.beigesoft.hld.HldNmCnFrStXml;
import org.beigesoft.hld.HlNmPrFe;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.hnd.HndEntRq;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.prp.UtlPrp;
import org.beigesoft.prp.Setng;
import org.beigesoft.log.LogFile;
import org.beigesoft.cnv.FilEntRs;
import org.beigesoft.cnv.FilEntRq;
import org.beigesoft.cnv.FilCvEnt;
import org.beigesoft.rpl.RpEntWriXml;
import org.beigesoft.rpl.RpEntReadXml;
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
   * <p>DB-Copy holder field fillers names name.</p>
   **/
  public static final String HLFILFDNMDBCP = "hlFiFdNmDbCp";

  /**
   * <p>DB-Copy entity writer service name.</p>
   **/
  public static final String ENWRDBCPNM = "enWrDbCp";

  /**
   * <p>DB-Copy entity reader service name.</p>
   **/
  public static final String ENRDDBCPNM = "enRdDbCp";

  /**
   * <p>DB-Copy Setting service name.</p>
   **/
  public static final String STGDBCPNM = "stgDbCp";

  /**
   * <p>UVD Setting service name.</p>
   **/
  public static final String STGUVDNM = "stgUvd";

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
   * <p>Handler base entities (no admin, etc) request name.</p>
   **/
  public static final String HNACENRQ = "hnAcEnRq";

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
   * <p>Forbidden entities for base entity request handler, e.g. UsTmc.</p>
   **/
  private List<Class<?>> fbdEnts;

  //parts/services:
  /**
   * <p>Outside app-beans/parts factories final configuration.
   *  They put its beans into this main factory beans.</p>
   **/
  private List<IFctAux<RS>> fctsAux = new ArrayList<IFctAux<RS>>();

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
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @return Object - requested bean or exception if not found
   * @throws Exception - an exception
   */
  @Override
  public final Object laz(final Map<String, Object> pRqVs,
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
            rz = lazHndI18nRq(pRqVs);
          } else if (HNACENRQ.equals(pBnNm)) {
            rz = lazHnAcEnRq(pRqVs);
          } else if (FctPrcFen.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctPrcFen(pRqVs);
          } else if (STGORMNM.equals(pBnNm)) {
            rz = lazStgOrm(pRqVs);
          } else if (IOrm.class.getSimpleName().equals(pBnNm)) {
            rz = lazOrm(pRqVs);
          } else if (SrvClVl.class.getSimpleName().equals(pBnNm)) {
            rz = lazSrvClVl(pRqVs);
          } else if (ISqlQu.class.getSimpleName().equals(pBnNm)) {
            rz = lazSqlQu(pRqVs);
          } else if (FilCvEnt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilCvEnt(pRqVs);
          } else if (FilEntRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilEntRs(pRqVs);
          } else if (HldCnvFdCv.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldCnvFdCv(pRqVs);
          } else if (HldNmFilFdRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmFilFdRs(pRqVs);
          } else if (HldFilFdCv.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldFilFdCv(pRqVs);
          } else if (HldNmCnFrRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnFrRs(pRqVs);
          } else if (FctCnvCv.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctCnvCv(pRqVs);
          } else if (FctNmCnFrRs.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnFrRs(pRqVs);
          } else if (FilEntRq.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilEntRq(pRqVs);
          } else if (ENWRDBCPNM.equals(pBnNm)) {
            rz = lazRpEntWriXmlDbCp(pRqVs);
          } else if (ENRDDBCPNM.equals(pBnNm)) {
            rz = lazRpEntReadXmlDbCp(pRqVs);
          } else if (STGDBCPNM.equals(pBnNm)) {
            rz = lazStgDbCp(pRqVs);
          } else if (STGUVDNM.equals(pBnNm)) {
            rz = lazStgUvd(pRqVs);
          } else if (FctNmCnFrSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnFrSt(pRqVs);
          } else if (FctNmFilFdSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmFilFd(pRqVs);
          } else if (FctNmCnToSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnToSt(pRqVs);
          } else if (HldUvd.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldUvd(pRqVs);
          } else if (HLFILFDNMUVD.equals(pBnNm)) {
            rz = lazHldNmFilFdStUvd(pRqVs);
          } else if (HLFILFDNMDBCP.equals(pBnNm)) {
            rz = lazHldNmFilFdStDbCp(pRqVs);
          } else if (HldNmCnToStXml.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnToStXml(pRqVs);
          } else if (HldNmCnFrStXml.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnFrStXml(pRqVs);
          } else if (HldNmCnFrSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnFrSt(pRqVs);
          } else if (HldNmCnToSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnToSt(pRqVs);
          } else if (HldGets.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldGets(pRqVs);
          } else if (HldSets.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldSets(pRqVs);
          } else if (HldFldCls.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldFldCls(pRqVs);
          } else if (FctFctEnt.class.getSimpleName().equals(pBnNm)) {
            //IT DEPENDS OF ORM!
            rz = lazFctFctEnt(pRqVs);
          } else if (UtlPrp.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlPrp(pRqVs);
          } else if (INumStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazNumStr(pRqVs);
          } else if (ISqlEsc.class.getSimpleName().equals(pBnNm)) {
            rz = lazSqlEsc(pRqVs);
          } else if (IUtlXml.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlXml(pRqVs);
          } else if (LOGSTDNM.equals(pBnNm)) {
            rz = lazLogStd(pRqVs);
          } else if (LOGSECNM.equals(pBnNm)) {
            rz = lazLogSec(pRqVs);
          } else if (IReflect.class.getSimpleName().equals(pBnNm)) {
            rz = lazReflect(pRqVs);
          } else if (ISrvDt.class.getSimpleName().equals(pBnNm)) {
            rz = lazSrvDt(pRqVs);
          } else if (ISrvPg.class.getSimpleName().equals(pBnNm)) {
            rz = lazSrvPg(pRqVs);
          } else if (II18n.class.getSimpleName().equals(pBnNm)) {
            rz = lazI18n(pRqVs);
          } else if (HlpEntPg.class.getSimpleName().equals(pBnNm)) {
            rz = lazHlpEntPg(pRqVs);
          } else {
            for (IFctAux<RS> fau : this.fctsAux) {
              rz = fau.crePut(pRqVs, pBnNm, this);
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
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @param pBean - bean
   * @throws Exception - an exception, e.g. if bean exists
   **/
  @Override
  public final synchronized void put(final Map<String, Object> pRqVs,
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
   * @param pRqVs request scoped vars
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void release(
    final Map<String, Object> pRqVs) throws Exception {
    this.beans.clear();
    for (IFctAux<RS> fau : this.fctsAux) {
      fau.release(pRqVs, this);
    }
    if (this.logStd != null) {
      this.logStd.info(pRqVs, getClass(), "Send stop to LOG STD...");
      this.logStd.setNeedRun(false);
      this.logStd = null;
    }
  }

  //Request handlers:
  /**
   * <p>Lazy getter handler base entities request.</p>
   * @param pRqVs request scoped vars
   * @return HndEntRq
   * @throws Exception - an exception
   */
  public final synchronized HndEntRq<RS> lazHnAcEnRq(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    HndEntRq<RS> rz = (HndEntRq<RS>) this.beans.get(HNACENRQ);
    if (rz == null) {
      rz = new HndEntRq<RS>();
      rz.setWriteTi(getWriteTi());
      rz.setReadTi(getReadTi());
      rz.setWriteReTi(getWriteReTi());
      rz.setWrReSpTr(getWrReSpTr());
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setLogSec(lazLogSec(pRqVs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRqVs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setFilEntRq(lazFilEntRq(pRqVs));
      rz.setFctFctEnt(lazFctFctEnt(pRqVs));
      rz.setFctPrcFen(lazFctPrcFen(pRqVs));
      rz.setHldPrcFenNm(new HlNmPrFe());
      rz.setEntMap(new HashMap<String, Class<?>>());
      Setng setng = lazStgUvd(pRqVs);
      for (Class<?> cls  : setng.lazClss()) {
        if (this.fbdEnts == null || !this.fbdEnts.contains(cls)) {
          rz.getEntMap().put(cls.getSimpleName(), cls);
        }
      }
      this.beans.put(HNACENRQ, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HNACENRQ + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctPrcFen.</p>
   * @param pRqVs request scoped vars
   * @return FctPrcFen
   * @throws Exception - an exception
   */
  public final synchronized FctPrcFen<RS> lazFctPrcFen(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    FctPrcFen<RS> rz = (FctPrcFen<RS>) this.beans
      .get(FctPrcFen.class.getSimpleName());
    if (rz == null) {
      rz = new FctPrcFen<RS>();
      rz.setFctBlc(this);
      this.beans.put(FctPrcFen.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FctPrcFen.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HndI18nRq.</p>
   * @param pRqVs request scoped vars
   * @return HndI18nRq
   * @throws Exception - an exception
   */
  public final synchronized HndI18nRq<RS> lazHndI18nRq(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    HndI18nRq<RS> rz = (HndI18nRq<RS>) this.beans
      .get(HndI18nRq.class.getSimpleName());
    if (rz == null) {
      rz = new HndI18nRq<RS>();
      rz.setOrm(lazOrm(pRqVs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRqVs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setLog(lazLogStd(pRqVs));
      rz.setI18n(lazI18n(pRqVs));
      rz.setUtJsp(new UtlJsp());
      this.beans.put(HndI18nRq.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HndI18nRq.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  //Database full copy:
  /**
   * <p>Lazy getter RpEntReadXmlDbCp.</p>
   * @param pRqVs request scoped vars
   * @return RpEntReadXmlDbCp
   * @throws Exception - an exception
   */
  public final synchronized RpEntReadXml lazRpEntReadXmlDbCp(
    final Map<String, Object> pRqVs) throws Exception {
    RpEntReadXml rz = (RpEntReadXml) this.beans.get(ENRDDBCPNM);
    if (rz == null) {
      rz = new RpEntReadXml();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgDbCp(pRqVs));
      rz.setHldFilFdNms(lazHldNmFilFdStDbCp(pRqVs));
      rz.setUtlXml(lazUtlXml(pRqVs));
      rz.setFctFilFld(lazFctNmFilFd(pRqVs));
      this.beans.put(ENWRDBCPNM, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), ENRDDBCPNM
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter RpEntWriXmlDbCp.</p>
   * @param pRqVs request scoped vars
   * @return RpEntWriXmlDbCp
   * @throws Exception - an exception
   */
  public final synchronized RpEntWriXml lazRpEntWriXmlDbCp(
    final Map<String, Object> pRqVs) throws Exception {
    RpEntWriXml rz = (RpEntWriXml) this.beans.get(ENWRDBCPNM);
    if (rz == null) {
      rz = new RpEntWriXml();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgDbCp(pRqVs));
      rz.setHldGets(lazHldGets(pRqVs));
      rz.setHldNmFdCn(lazHldNmCnToStXml(pRqVs));
      rz.setFctCnvFld(lazFctNmCnToSt(pRqVs));
      this.beans.put(ENWRDBCPNM, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), ENWRDBCPNM
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter DB copy Setng.</p>
   * @param pRqVs request scoped vars
   * @return Setng
   * @throws Exception - an exception
   */
  public final synchronized Setng lazStgDbCp(
    final Map<String, Object> pRqVs) throws Exception {
    Setng rz = (Setng) this.beans.get(STGDBCPNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(getStgDbCpDir());
      rz.setReflect(lazReflect(pRqVs));
      rz.setUtlPrp(lazUtlPrp(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setLog(lazLogStd(pRqVs));
      this.beans.put(STGDBCPNM, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), STGDBCPNM
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter DBCP HldNmFilFdSt.</p>
   * @param pRqVs request scoped vars
   * @return HldNmFilFdSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmFilFdSt lazHldNmFilFdStDbCp(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmFilFdSt rz = (HldNmFilFdSt) this.beans
      .get(HLFILFDNMDBCP);
    if (rz == null) {
      rz = new HldNmFilFdSt();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setFilHasIdNm(FctNmFilFdSt.FILHSIDSTDBCPNM);
      rz.setFilSmpNm(FctNmFilFdSt.FILSMPSTDBCPNM);
      rz.setSetng(lazStgDbCp(pRqVs));
      this.beans.put(HLFILFDNMDBCP, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        HLFILFDNMDBCP + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnToStXml.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnToStXml
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnToStXml lazHldNmCnToStXml(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnToStXml rz = (HldNmCnToStXml) this.beans
      .get(HldNmCnToStXml.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnToStXml();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setCnHsIdToStNm(FctNmCnToSt.CNHSIDSTDBCPNM);
      this.beans.put(HldNmCnToStXml.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnToStXml.class
        .getSimpleName() + " has been created.");
    }
    return rz;
  }

  //Shared replication services:
  /**
   * <p>Lazy getter HldNmCnFrStXml.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnFrStXml
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnFrStXml lazHldNmCnFrStXml(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnFrStXml rz = (HldNmCnFrStXml) this.beans
      .get(HldNmCnFrStXml.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnFrStXml();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HldNmCnFrStXml.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnFrStXml.class
        .getSimpleName() + " has been created.");
    }
    return rz;
  }

  //ORM:
  /**
   * <p>Lazy getter Orm.</p>
   * @param pRqVs request scoped vars
   * @return Orm
   * @throws Exception - an exception
   */
  public final synchronized Orm<RS> lazOrm(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    Orm<RS> rz = (Orm<RS>) this.beans.get(IOrm.class.getSimpleName());
    if (rz == null) {
      rz = new Orm<RS>();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgOrm(pRqVs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRqVs, IRdb.class.getSimpleName());
      rz.setIsAndr(this.isAndr);
      rz.setNewDbId(this.newDbId);
      rz.setRdb(rdb);
      rz.setSqlQu(lazSqlQu(pRqVs));
      rz.setSrvClVl(lazSrvClVl(pRqVs));
      rz.setFilEntRs(lazFilEntRs(pRqVs));
      rz.setFilCvEn(lazFilCvEnt(pRqVs));
      FctFctEnt fctFctEnt = lazFctFctEnt(pRqVs);
      fctFctEnt.setOrm(rz);
      rz.setFctFctEnt(fctFctEnt);
      //initialization must be by the first invoker (servlet)
      this.beans.put(IOrm.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), Orm.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter Setng ORM.</p>
   * @param pRqVs request scoped vars
   * @return Setng ORM
   * @throws Exception - an exception
   */
  public final synchronized Setng lazStgOrm(
    final Map<String, Object> pRqVs) throws Exception {
    Setng rz = (Setng) this.beans.get(STGORMNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(getStgOrmDir());
      rz.setReflect(lazReflect(pRqVs));
      rz.setUtlPrp(lazUtlPrp(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setLog(lazLogStd(pRqVs));
      this.beans.put(STGORMNM, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), STGORMNM + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SrvClVl.</p>
   * @param pRqVs request scoped vars
   * @return SrvClVl
   * @throws Exception - an exception
   */
  public final synchronized SrvClVl lazSrvClVl(
    final Map<String, Object> pRqVs) throws Exception {
    SrvClVl rz = (SrvClVl) this.beans.get(SrvClVl.class.getSimpleName());
    if (rz == null) {
      rz = new SrvClVl();
      rz.setSetng(lazStgOrm(pRqVs));
      this.beans.put(SrvClVl.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), SrvClVl.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SqlQu.</p>
   * @param pRqVs request scoped vars
   * @return SqlQu
   * @throws Exception - an exception
   */
  public final synchronized SqlQu lazSqlQu(
    final Map<String, Object> pRqVs) throws Exception {
    SqlQu rz = (SqlQu) this.beans.get(ISqlQu.class.getSimpleName());
    if (rz == null) {
      rz = new SqlQu();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgOrm(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setHldGets(lazHldGets(pRqVs));
      this.beans.put(ISqlQu.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), SqlQu.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FilCvEnt.</p>
   * @param pRqVs request scoped vars
   * @return FilCvEnt
   * @throws Exception - an exception
   */
  public final synchronized FilCvEnt<IHasId<?>, ?> lazFilCvEnt(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    FilCvEnt<IHasId<?>, ?> rz = (FilCvEnt<IHasId<?>, ?>) this.beans
      .get(FilCvEnt.class.getSimpleName());
    if (rz == null) {
      rz = new FilCvEnt();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgOrm(pRqVs));
      rz.setHldFilFdNms(lazHldFilFdCv(pRqVs));
      rz.setFctFilFld(lazFctFilFdCv(pRqVs));
      this.beans.put(FilCvEnt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FilCvEnt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FilEntRs.</p>
   * @param pRqVs request scoped vars
   * @return FilEntRs
   * @throws Exception - an exception
   */
  public final synchronized FilEntRs<RS> lazFilEntRs(
    final Map<String, Object> pRqVs) throws Exception {
    FilEntRs<RS> rz = (FilEntRs<RS>) this.beans
      .get(FilEntRs.class.getSimpleName());
    if (rz == null) {
      rz = new FilEntRs<RS>();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgOrm(pRqVs));
      rz.setHldFilFdNms(lazHldNmFilFdRs(pRqVs));
      FctNmFilFdRs<RS> fffd = lazFctNmFilFdRs(pRqVs);
      fffd.setFilEnt(rz);
      rz.setFctFilFld(fffd);
      this.beans.put(FilEntRs.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FilEntRs.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldCnvFdCv.</p>
   * @param pRqVs request scoped vars
   * @return HldCnvFdCv
   * @throws Exception - an exception
   */
  public final synchronized HldCnvFdCv lazHldCnvFdCv(
    final Map<String, Object> pRqVs) throws Exception {
    HldCnvFdCv rz = (HldCnvFdCv) this.beans
      .get(HldCnvFdCv.class.getSimpleName());
    if (rz == null) {
      rz = new HldCnvFdCv();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HldCnvFdCv.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        HldCnvFdCv.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmFilFdRs.</p>
   * @param pRqVs request scoped vars
   * @return HldNmFilFdRs
   * @throws Exception - an exception
   */
  public final synchronized HldNmFilFdRs lazHldNmFilFdRs(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmFilFdRs rz = (HldNmFilFdRs) this.beans
      .get(HldNmFilFdRs.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmFilFdRs();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HldNmFilFdRs.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        HldNmFilFdRs.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldFilFdCv.</p>
   * @param pRqVs request scoped vars
   * @return HldFilFdCv
   * @throws Exception - an exception
   */
  public final synchronized HldFilFdCv lazHldFilFdCv(
    final Map<String, Object> pRqVs) throws Exception {
    HldFilFdCv rz = (HldFilFdCv) this.beans
      .get(HldFilFdCv.class.getSimpleName());
    if (rz == null) {
      rz = new HldFilFdCv();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HldFilFdCv.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldFilFdCv.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnFrRs.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnFrRs
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnFrRs lazHldNmCnFrRs(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnFrRs rz = (HldNmCnFrRs) this.beans
      .get(HldNmCnFrRs.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnFrRs();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HldNmCnFrRs.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnFrRs.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctCnvCv.</p>
   * @param pRqVs request scoped vars
   * @return FctCnvCv
   * @throws Exception - an exception
   */
  public final synchronized FctCnvCv lazFctCnvCv(
    final Map<String, Object> pRqVs) throws Exception {
    FctCnvCv rz = (FctCnvCv) this.beans
      .get(FctCnvCv.class.getSimpleName());
    if (rz == null) {
      rz = new FctCnvCv();
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setSqlEsc(lazSqlEsc(pRqVs));
      this.beans.put(FctCnvCv.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FctCnvCv.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnFrRs.</p>
   * @param pRqVs request scoped vars
   * @return FctNmCnFrRs
   * @throws Exception - an exception
   */
  public final synchronized FctNmCnFrRs<RS> lazFctNmCnFrRs(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    FctNmCnFrRs<RS> rz = (FctNmCnFrRs<RS>) this.beans
      .get(FctNmCnFrRs.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnFrRs<RS>();
      rz.setLogStd(lazLogStd(pRqVs));
      this.beans.put(FctNmCnFrRs.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FctNmCnFrRs.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctFilFdCv. Mutual dependency with FilEntRs.</p>
   * @param pRqVs request scoped vars
   * @return FctFilFdCv
   * @throws Exception - an exception
   */
  public final synchronized FctFilFdCv lazFctFilFdCv(
    final Map<String, Object> pRqVs) throws Exception {
    FctFilFdCv rz = (FctFilFdCv) this.beans
      .get(FctFilFdCv.class.getSimpleName());
    if (rz == null) {
      rz = new FctFilFdCv();
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setSetng(lazStgOrm(pRqVs));
      rz.setHldGets(lazHldGets(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setHldNmFdCn(lazHldCnvFdCv(pRqVs));
      rz.setFctCnvFld(lazFctCnvCv(pRqVs));
      rz.setHldFilFdNms(lazHldFilFdCv(pRqVs));
      this.beans.put(FctFilFdCv.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        FctFilFdCv.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmFilFdRs. Mutual dependency with FilEntRs.</p>
   * @param pRqVs request scoped vars
   * @return FctNmFilFdRs
   * @throws Exception - an exception
   */
  public final synchronized FctNmFilFdRs<RS> lazFctNmFilFdRs(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    FctNmFilFdRs<RS> rz = (FctNmFilFdRs<RS>) this.beans
      .get(FctNmFilFdRs.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmFilFdRs<RS>();
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setHldSets(lazHldSets(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setHldNmFdCn(lazHldNmCnFrRs(pRqVs));
      rz.setFctCnvFld(lazFctNmCnFrRs(pRqVs));
      this.beans.put(FctNmFilFdRs.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        FctNmFilFdRs.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  //UVD:
  /**
   * <p>Lazy getter Setng.</p>
   * @param pRqVs request scoped vars
   * @return Setng
   * @throws Exception - an exception
   */
  public final synchronized Setng lazStgUvd(
    final Map<String, Object> pRqVs) throws Exception {
    Setng rz = (Setng) this.beans.get(STGUVDNM);
    if (rz == null) {
      rz = new Setng();
      rz.setDir(getStgUvdDir());
      rz.setReflect(lazReflect(pRqVs));
      rz.setUtlPrp(lazUtlPrp(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setLog(lazLogStd(pRqVs));
      this.beans.put(STGUVDNM, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), STGUVDNM + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FilEntRq.</p>
   * @param pRqVs request scoped vars
   * @return FilEntRq
   * @throws Exception - an exception
   */
  public final synchronized FilEntRq lazFilEntRq(
    final Map<String, Object> pRqVs) throws Exception {
    FilEntRq rz = (FilEntRq) this.beans
      .get(FilEntRq.class.getSimpleName());
    if (rz == null) {
      rz = new FilEntRq();
      rz.setLog(lazLogStd(pRqVs));
      rz.setSetng(lazStgUvd(pRqVs));
      rz.setHldFilFdNms(lazHldNmFilFdStUvd(pRqVs));
      rz.setFctFilFld(lazFctNmFilFd(pRqVs));
      this.beans.put(FilEntRq.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FilEntRq.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter UVD HldNmFilFdSt.</p>
   * @param pRqVs request scoped vars
   * @return HldNmFilFdSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmFilFdSt lazHldNmFilFdStUvd(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmFilFdSt rz = (HldNmFilFdSt) this.beans
      .get(HLFILFDNMUVD);
    if (rz == null) {
      rz = new HldNmFilFdSt();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setFilHasIdNm(FctNmFilFdSt.FILHSIDSTUVDNM);
      rz.setFilSmpNm(FctNmFilFdSt.FILSMPSTUVDNM);
      rz.setSetng(lazStgUvd(pRqVs));
      this.beans.put(HLFILFDNMUVD, rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        HLFILFDNMUVD + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnFrSt.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnFrSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnFrSt lazHldNmCnFrSt(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnFrSt rz = (HldNmCnFrSt) this.beans
      .get(HldNmCnFrSt.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnFrSt();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setSetng(lazStgUvd(pRqVs));
      this.beans.put(HldNmCnFrSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnFrSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnToSt.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnToSt
   * @throws Exception - an exception
   */
  public final synchronized HldNmCnToSt lazHldNmCnToSt(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnToSt rz = (HldNmCnToSt) this.beans
      .get(HldNmCnToSt.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnToSt();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setCnHsIdToStNm(FctNmCnToSt.CNHSIDSTUVDNM);
      rz.setSetng(lazStgUvd(pRqVs));
      this.beans.put(HldNmCnToSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnToSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldUvd.</p>
   * @param pRqVs request scoped vars
   * @return HldUvd
   * @throws Exception - an exception
   */
  public final synchronized HldUvd<IHasId<?>> lazHldUvd(
    final Map<String, Object> pRqVs) throws Exception {
    HldUvd<IHasId<?>> rz = (HldUvd<IHasId<?>>) this.beans
      .get(HldUvd.class.getSimpleName());
    if (rz == null) {
      rz = new HldUvd<IHasId<?>>();
      rz.setSetng(lazStgUvd(pRqVs));
      String stgNm = "liHe"; //list header
      HldClsStg hlClSt = new HldClsStg(stgNm, stgNm);
      hlClSt.setSetng(lazStgUvd(pRqVs));
      rz.getHlClStgMp().put(stgNm, hlClSt);
      stgNm = "liFo"; //list footer
      hlClSt = new HldClsStg(stgNm, stgNm);
      hlClSt.setSetng(lazStgUvd(pRqVs));
      rz.getHlClStgMp().put(stgNm, hlClSt);
      this.beans.put(HldUvd.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldUvd.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnFrSt.</p>
   * @param pRqVs request scoped vars
   * @return FctNmCnFrSt
   * @throws Exception - an exception
   */
  public final synchronized FctNmCnFrSt lazFctNmCnFrSt(
    final Map<String, Object> pRqVs) throws Exception {
    FctNmCnFrSt rz = (FctNmCnFrSt) this.beans
      .get(FctNmCnFrSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnFrSt();
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setUtlXml(lazUtlXml(pRqVs));
      this.beans.put(FctNmCnFrSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FctNmCnFrSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmFilFdSt.</p>
   * @param pRqVs request scoped vars
   * @return FctNmFilFdSt
   * @throws Exception - an exception
   */
  public final synchronized FctNmFilFdSt lazFctNmFilFd(
    final Map<String, Object> pRqVs) throws Exception {
    FctNmFilFdSt rz = (FctNmFilFdSt) this.beans
      .get(FctNmFilFdSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmFilFdSt();
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setHldSets(lazHldSets(pRqVs));
      rz.setStgUvd(lazStgUvd(pRqVs));
      rz.setStgDbCp(lazStgDbCp(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setHldNmFdCnUvd(lazHldNmCnFrSt(pRqVs));
      rz.setHldNmFdCnDbCp(lazHldNmCnFrStXml(pRqVs));
      rz.setFctCnvFld(lazFctNmCnFrSt(pRqVs));
      rz.setHldFilFdNmsUvd(lazHldNmFilFdStUvd(pRqVs));
      rz.setHldFilFdNmsDbCp(lazHldNmFilFdStDbCp(pRqVs));
      this.beans.put(FctNmFilFdSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        FctNmFilFdSt.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnToSt.</p>
   * @param pRqVs request scoped vars
   * @return FctNmCnToSt
   * @throws Exception - an exception
   */
  public final synchronized FctNmCnToSt lazFctNmCnToSt(
    final Map<String, Object> pRqVs) throws Exception {
    FctNmCnToSt rz = (FctNmCnToSt) this.beans
      .get(FctNmCnToSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnToSt();
      rz.setUtlXml(lazUtlXml(pRqVs));
      rz.setNumStr(lazNumStr(pRqVs));
      rz.setHldNmFdCn(lazHldNmCnToSt(pRqVs));
      rz.setHldGets(lazHldGets(pRqVs));
      rz.setStgUvd(lazStgUvd(pRqVs));
      rz.setStgDbCp(lazStgDbCp(pRqVs));
      rz.setLogStd(lazLogStd(pRqVs));
      this.beans.put(FctNmCnToSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FctNmCnToSt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  //Common parts:
  /**
   * <p>Lazy getter HldSets.</p>
   * @param pRqVs request scoped vars
   * @return HldSets
   * @throws Exception - an exception
   */
  public final synchronized HldSets lazHldSets(
    final Map<String, Object> pRqVs) throws Exception {
    HldSets rz = (HldSets) this.beans
      .get(HldSets.class.getSimpleName());
    if (rz == null) {
      rz = new HldSets();
      rz.setReflect(lazReflect(pRqVs));
      this.beans.put(HldSets.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldSets.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldGets.</p>
   * @param pRqVs request scoped vars
   * @return HldGets
   * @throws Exception - an exception
   */
  public final synchronized HldGets lazHldGets(
    final Map<String, Object> pRqVs) throws Exception {
    HldGets rz = (HldGets) this.beans
      .get(HldGets.class.getSimpleName());
    if (rz == null) {
      rz = new HldGets();
      rz.setReflect(lazReflect(pRqVs));
      this.beans.put(HldGets.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldGets.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldFldCls.</p>
   * @param pRqVs request scoped vars
   * @return HldFldCls
   * @throws Exception - an exception
   */
  public final synchronized HldFldCls lazHldFldCls(
    final Map<String, Object> pRqVs) throws Exception {
    HldFldCls rz = (HldFldCls) this.beans
      .get(HldFldCls.class.getSimpleName());
    if (rz == null) {
      rz = new HldFldCls();
      rz.setReflect(lazReflect(pRqVs));
      this.beans.put(HldFldCls.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldFldCls.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctFctEnt IT DEPENDS OF ORM!!</p>
   * @param pRqVs request scoped vars
   * @return FctFctEnt
   * @throws Exception - an exception
   */
  public final synchronized FctFctEnt lazFctFctEnt(
    final Map<String, Object> pRqVs) throws Exception {
    FctFctEnt rz = (FctFctEnt) this.beans.get(FctFctEnt.class.getSimpleName());
    if (rz == null) {
      rz = new FctFctEnt();
      this.beans.put(FctFctEnt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FctFctEnt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HlpEntPg.</p>
   * @param pRqVs request scoped vars
   * @return HlpEntPg
   * @throws Exception - an exception
   */
  public final synchronized HlpEntPg<RS> lazHlpEntPg(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    HlpEntPg<RS> rz = (HlpEntPg<RS>) this.beans
      .get(HlpEntPg.class.getSimpleName());
    if (rz == null) {
      rz = new HlpEntPg<RS>();
      rz.setLog(lazLogStd(pRqVs));
      rz.setI18n(lazI18n(pRqVs));
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRqVs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setOrm(lazOrm(pRqVs));
      rz.setSrvPg(lazSrvPg(pRqVs));
      rz.setSqlQu(lazSqlQu(pRqVs));
      rz.setSrvDt(lazSrvDt(pRqVs));
      rz.setSetng(lazStgUvd(pRqVs));
      rz.setHldUvd(lazHldUvd(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HlpEntPg.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HlpEntPg.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter UtlPrp.</p>
   * @param pRqVs request scoped vars
   * @return UtlPrp
   * @throws Exception - an exception
   */
  public final synchronized UtlPrp lazUtlPrp(
    final Map<String, Object> pRqVs) throws Exception {
    UtlPrp rz = (UtlPrp) this.beans.get(UtlPrp.class.getSimpleName());
    if (rz == null) {
      rz = new UtlPrp();
      this.beans.put(UtlPrp.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), UtlPrp.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter NumStr.</p>
   * @param pRqVs request scoped vars
   * @return NumStr
   * @throws Exception - an exception
   */
  public final synchronized NumStr lazNumStr(
    final Map<String, Object> pRqVs) throws Exception {
    NumStr rz = (NumStr) this.beans.get(INumStr.class.getSimpleName());
    if (rz == null) {
      rz = new NumStr();
      this.beans.put(INumStr.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), INumStr.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SqlEsc.</p>
   * @param pRqVs request scoped vars
   * @return SqlEsc
   * @throws Exception - an exception
   */
  public final synchronized SqlEsc lazSqlEsc(
    final Map<String, Object> pRqVs) throws Exception {
    SqlEsc rz = (SqlEsc) this.beans.get(ISqlEsc.class.getSimpleName());
    if (rz == null) {
      rz = new SqlEsc();
      this.beans.put(ISqlEsc.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), ISqlEsc.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter UtlXml.</p>
   * @param pRqVs request scoped vars
   * @return UtlXml
   * @throws Exception - an exception
   */
  public final synchronized UtlXml lazUtlXml(
    final Map<String, Object> pRqVs) throws Exception {
    UtlXml rz = (UtlXml) this.beans.get(IUtlXml.class.getSimpleName());
    if (rz == null) {
      rz = new UtlXml();
      this.beans.put(IUtlXml.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), IUtlXml.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter I18n.</p>
   * @param pRqVs request scoped vars
   * @return I18n
   * @throws Exception - an exception
   */
  public final synchronized I18n lazI18n(
    final Map<String, Object> pRqVs) throws Exception {
    I18n rz = (I18n) this.beans.get(II18n.class.getSimpleName());
    if (rz == null) {
      rz = new I18n();
      this.beans.put(II18n.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), II18n.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SrvPg.</p>
   * @param pRqVs request scoped vars
   * @return SrvPg
   * @throws Exception - an exception
   */
  public final synchronized SrvPg lazSrvPg(
    final Map<String, Object> pRqVs) throws Exception {
    SrvPg rz = (SrvPg) this.beans.get(ISrvPg.class.getSimpleName());
    if (rz == null) {
      rz = new SrvPg();
      this.beans.put(ISrvPg.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), ISrvPg.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter SrvDt.</p>
   * @param pRqVs request scoped vars
   * @return SrvDt
   * @throws Exception - an exception
   */
  public final synchronized SrvDt lazSrvDt(
    final Map<String, Object> pRqVs) throws Exception {
    SrvDt rz = (SrvDt) this.beans.get(ISrvDt.class.getSimpleName());
    if (rz == null) {
      rz = new SrvDt();
      this.beans.put(ISrvDt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), ISrvDt.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter Reflect.</p>
   * @param pRqVs request scoped vars
   * @return Reflect
   * @throws Exception - an exception
   */
  public final synchronized Reflect lazReflect(
    final Map<String, Object> pRqVs) throws Exception {
    Reflect rz = (Reflect) this.beans.get(IReflect.class.getSimpleName());
    if (rz == null) {
      rz = new Reflect();
      this.beans.put(IReflect.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), IReflect.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter secure logger.</p>
   * @param pRqVs request scoped vars
   * @return Log
   * @throws Exception - an exception
   */
  public final synchronized LogFile lazLogSec(
    final Map<String, Object> pRqVs) throws Exception {
    LogFile logSec = (LogFile) this.beans.get(LOGSECNM);
    if (logSec == null) {
      logSec = new LogFile();
      logSec.setPath(this.logPth + File.separator + LOGSECNM);
      logSec.setDbgSh(getDbgSh());
      logSec.setDbgFl(getDbgFl());
      logSec.setDbgCl(getDbgCl());
      logSec.setMaxSize(this.logSize);
      this.beans.put(LOGSECNM, logSec);
      lazLogStd(pRqVs).info(pRqVs, getClass(), LOGSECNM + " has been created");
    }
    return logSec;
  }

  /**
   * <p>Lazy getter standard loger.</p>
   * @param pRqVs request scoped vars
   * @return Log
   * @throws Exception - an exception
   */
  public final synchronized LogFile lazLogStd(
    final Map<String, Object> pRqVs) throws Exception {
    if (this.logStd == null) {
      this.logStd = new LogFile();
      this.logStd.setPath(this.logPth + File.separator + this.logStdNm);
      this.logStd.setDbgSh(getDbgSh());
      this.logStd.setDbgFl(getDbgFl());
      this.logStd.setDbgCl(getDbgCl());
      this.logStd.setMaxSize(this.logSize);
      this.beans.put(LOGSTDNM, this.logStd);
      this.logStd.info(pRqVs, getClass(), LOGSTDNM + " has been created");
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
}
