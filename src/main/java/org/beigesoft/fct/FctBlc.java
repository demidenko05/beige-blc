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
import org.beigesoft.hld.HldFldCls;
import org.beigesoft.hld.HldIdFdNm;
import org.beigesoft.hld.HldGets;
import org.beigesoft.hld.HldSets;
import org.beigesoft.hld.HldNmFilFdSt;
import org.beigesoft.hld.HldNmCnFrSt;
import org.beigesoft.hld.HldNmCnToSt;
import org.beigesoft.hld.HldNmCnToStXml;
import org.beigesoft.hld.HldNmCnFrStXml;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.prp.UtlPrp;
import org.beigesoft.prp.Setng;
import org.beigesoft.log.ILog;
import org.beigesoft.cnv.FilEntRq;
import org.beigesoft.rpl.RpEntWriXml;
import org.beigesoft.srv.INumStr;
import org.beigesoft.srv.NumStr;
import org.beigesoft.srv.IReflect;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IRdb;
import org.beigesoft.srv.IOrm;
import org.beigesoft.srv.UtlXml;
import org.beigesoft.srv.IUtlXml;

/**
 * <p>Main application beans factory. All configuration dependent inner
 * factories must be inside "fctConf".</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FctBlc<RS> implements IFctApp {

  /**
   * <p>DB-Copy entity writer service name.</p>
   **/
  public static final String ENWRDBCPNM = "enWrDbCp";

  /**
   * <p>DB-Copy Setting service name.</p>
   **/
  public static final String STGDBCPNM = "stgDbCp";

  /**
   * <p>UVD Setting service name.</p>
   **/
  public static final String STGUVDNM = "stgUvd";

  /**
   * <p>Standard logger name.</p>
   **/
  public static final String LOGSTDNM = "logStd";

  /**
   * <p>Secure logger name.</p>
   **/
  public static final String LOGSECNM = "logSec";

  //configuration data:
  /**
   * <p>Database full copy setting base dir.</p>
   **/
  private String stgDbCpDir;

  /**
   * <p>UVD setting base dir.</p>
   **/
  private String stgUvdDir;

  //parts/services:
  /**
   * <p>Outside app-beans/parts factory final configuration.
   * It must throws exception if bean not found. It may consist of
   * sub-factories. It puts its beans into this main factory beans.</p>
   **/
  private IFctAux fctConf;

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
          } else if (FilEntRq.class.getSimpleName().equals(pBnNm)) {
            rz = lazFilEntRq(pRqVs);
          } else if (ENWRDBCPNM.equals(pBnNm)) {
            rz = lazRpEntWriXmlDbCp(pRqVs);
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
          } else if (HldNmFilFdSt.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmFilFdSt(pRqVs);
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
          } else if (UtlPrp.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlPrp(pRqVs);
          } else if (INumStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazNumStr(pRqVs);
          } else if (IUtlXml.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlXml(pRqVs);
          } else if (IReflect.class.getSimpleName().equals(pBnNm)) {
            rz = lazReflect(pRqVs);
          } else {
            rz = this.fctConf.crePut(pRqVs, pBnNm, this);
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
   * @throws Exception - an exception
   */
  @Override
  public final synchronized void release() throws Exception {
    this.beans.clear();
    this.fctConf.release();
  }

  //Request handlers:
  /**
   * <p>Lazy getter HndI18nRq.</p>
   * @param pRqVs request scoped vars
   * @return HndI18nRq
   * @throws Exception - an exception
   */
  private HndI18nRq<RS> lazHndI18nRq(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    HndI18nRq<RS> rz = (HndI18nRq<RS>) this.beans
      .get(HndI18nRq.class.getSimpleName());
    if (rz == null) {
      rz = new HndI18nRq<RS>();
      @SuppressWarnings("unchecked")
      IOrm<RS> orm = (IOrm<RS>) laz(pRqVs, IOrm.class.getSimpleName());
      rz.setOrm(orm);
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) laz(pRqVs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setLog(lazLogStd(pRqVs));
      this.beans.put(HndI18nRq.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HndI18nRq.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  //Database full copy:
  /**
   * <p>Lazy getter RpEntWriXmlDbCp.</p>
   * @param pRqVs request scoped vars
   * @return RpEntWriXmlDbCp
   * @throws Exception - an exception
   */
  private RpEntWriXml<?> lazRpEntWriXmlDbCp(
    final Map<String, Object> pRqVs) throws Exception {
    @SuppressWarnings("unchecked")
    RpEntWriXml<Object> rz = (RpEntWriXml<Object>) this.beans.get(ENWRDBCPNM);
    if (rz == null) {
      rz = new RpEntWriXml<Object>();
      rz.setLog(lazLogStd(pRqVs));
      rz.setHldFdNms(lazStgDbCp(pRqVs));
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
  private Setng lazStgDbCp(final Map<String, Object> pRqVs) throws Exception {
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

  //Shared replication services:
  /**
   * <p>Lazy getter HldNmCnToStXml.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnToStXml
   * @throws Exception - an exception
   */
  private HldNmCnToStXml lazHldNmCnToStXml(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnToStXml rz = (HldNmCnToStXml) this.beans
      .get(HldNmCnToStXml.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnToStXml();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      this.beans.put(HldNmCnToStXml.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnToStXml.class
        .getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnFrStXml.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnFrStXml
   * @throws Exception - an exception
   */
  private HldNmCnFrStXml lazHldNmCnFrStXml(
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

  //UVD:
  /**
   * <p>Lazy getter Setng.</p>
   * @param pRqVs request scoped vars
   * @return Setng
   * @throws Exception - an exception
   */
  private Setng lazStgUvd(final Map<String, Object> pRqVs) throws Exception {
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
  private FilEntRq lazFilEntRq(
    final Map<String, Object> pRqVs) throws Exception {
    FilEntRq rz = (FilEntRq) this.beans
      .get(FilEntRq.class.getSimpleName());
    if (rz == null) {
      rz = new FilEntRq();
      rz.setLog(lazLogStd(pRqVs));
      rz.setHldFdNms(lazStgUvd(pRqVs));
      rz.setHldFilFdNms(lazHldNmFilFdSt(pRqVs));
      rz.setFctFilFld(lazFctNmFilFd(pRqVs));
      this.beans.put(FilEntRq.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), FilEntRq.class.getSimpleName()
        + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmFilFdSt.</p>
   * @param pRqVs request scoped vars
   * @return HldNmFilFdSt
   * @throws Exception - an exception
   */
  private HldNmFilFdSt lazHldNmFilFdSt(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmFilFdSt rz = (HldNmFilFdSt) this.beans
      .get(HldNmFilFdSt.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmFilFdSt();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setSetng(lazStgUvd(pRqVs));
      this.beans.put(HldNmFilFdSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(),
        HldNmFilFdSt.class.getSimpleName() + " has been created.");
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnFrSt.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnFrSt
   * @throws Exception - an exception
   */
  private HldNmCnFrSt lazHldNmCnFrSt(
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
  private HldNmCnToSt lazHldNmCnToSt(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnToSt rz = (HldNmCnToSt) this.beans
      .get(HldNmCnToSt.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnToSt();
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setSetng(lazStgUvd(pRqVs));
      this.beans.put(HldNmCnToSt.class.getSimpleName(), rz);
      lazLogStd(pRqVs).info(pRqVs, getClass(), HldNmCnToSt.class.getSimpleName()
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
  private FctNmCnFrSt lazFctNmCnFrSt(
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
  private FctNmFilFdSt lazFctNmFilFd(
    final Map<String, Object> pRqVs) throws Exception {
    FctNmFilFdSt rz = (FctNmFilFdSt) this.beans
      .get(FctNmFilFdSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmFilFdSt();
      rz.setLogStd(lazLogStd(pRqVs));
      rz.setHldSets(lazHldSets(pRqVs));
      rz.setHldFdNms(lazStgUvd(pRqVs));
      rz.setHldFdCls(lazHldFldCls(pRqVs));
      rz.setHldNmFdCn(lazHldNmCnFrSt(pRqVs));
      rz.setHldIdFdNm((HldIdFdNm) laz(pRqVs, HldIdFdNm.class.getSimpleName()));
      rz.setFctCnvFld(lazFctNmCnFrSt(pRqVs));
      rz.setHldFilFdNms(lazHldNmFilFdSt(pRqVs));
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
  private FctNmCnToSt lazFctNmCnToSt(
    final Map<String, Object> pRqVs) throws Exception {
    FctNmCnToSt rz = (FctNmCnToSt) this.beans
      .get(FctNmCnToSt.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnToSt();
      rz.setUtlXml(lazUtlXml(pRqVs));
      rz.setNumStr(lazNumStr(pRqVs));
      rz.setHldNmFdCn(lazHldNmCnToSt(pRqVs));
      rz.setHldGets(lazHldGets(pRqVs));
      rz.setHldIdFdNm((HldIdFdNm) laz(pRqVs, HldIdFdNm.class.getSimpleName()));
      rz.setHldFdNms(lazStgUvd(pRqVs));
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
  private HldSets lazHldSets(final Map<String, Object> pRqVs) throws Exception {
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
  private HldGets lazHldGets(final Map<String, Object> pRqVs) throws Exception {
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
  private HldFldCls lazHldFldCls(
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
   * <p>Lazy getter UtlPrp.</p>
   * @param pRqVs request scoped vars
   * @return UtlPrp
   * @throws Exception - an exception
   */
  private UtlPrp lazUtlPrp(final Map<String, Object> pRqVs) throws Exception {
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
  private NumStr lazNumStr(final Map<String, Object> pRqVs) throws Exception {
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
   * <p>Lazy getter UtlXml.</p>
   * @param pRqVs request scoped vars
   * @return UtlXml
   * @throws Exception - an exception
   */
  private UtlXml lazUtlXml(final Map<String, Object> pRqVs) throws Exception {
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
   * <p>Lazy getter Reflect.</p>
   * @param pRqVs request scoped vars
   * @return Reflect
   * @throws Exception - an exception
   */
  private Reflect lazReflect(final Map<String, Object> pRqVs) throws Exception {
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
   * <p>Lazy getter Log.</p>
   * @param pRqVs request scoped vars
   * @return Log
   * @throws Exception - an exception
   */
  private ILog lazLogStd(final Map<String, Object> pRqVs) throws Exception {
    if (this.logStd == null) {
      this.logStd = (ILog) this.fctConf.crePut(pRqVs, LOGSTDNM, this);
    }
    return this.logStd;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctConf.</p>
   * @return IFctNm
   **/
  public final synchronized IFctAux getFctConf() {
    return this.fctConf;
  }

  /**
   * <p>Setter for fctConf.</p>
   * @param pFctConf reference
   **/
  public final synchronized void setFctConf(final IFctAux pFctConf) {
    this.fctConf = pFctConf;
  }

  /**
   * <p>Getter for stgDbCpDir.</p>
   * @return String
   **/
  public final String getStgDbCpDir() {
    return this.stgDbCpDir;
  }

  /**
   * <p>Setter for stgDbCpDir.</p>
   * @param pStgDbCpDir reference
   **/
  public final void setStgDbCpDir(final String pStgDbCpDir) {
    this.stgDbCpDir = pStgDbCpDir;
  }

  /**
   * <p>Getter for stgUvdDir.</p>
   * @return String
   **/
  public final String getStgUvdDir() {
    return this.stgUvdDir;
  }

  /**
   * <p>Setter for stgUvdDir.</p>
   * @param pStgUvdDir reference
   **/
  public final void setStgUvdDir(final String pStgUvdDir) {
    this.stgUvdDir = pStgUvdDir;
  }
}
