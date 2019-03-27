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
import org.beigesoft.hld.HldNmCnvStr;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.prp.UtlPrp;
import org.beigesoft.prp.Setng;
import org.beigesoft.log.ILog;
import org.beigesoft.srv.INumStr;
import org.beigesoft.srv.NumStr;
import org.beigesoft.srv.IReflect;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IRdb;
import org.beigesoft.srv.IOrm;

/**
 * <p>Application beans factory of beige-blc beans. It's inner factory
 * inside final application beans factory.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class FctBlc<RS> implements IFctApp {

  /**
   * <p>UVD Setting service name.</p>
   **/
  public static final String STGUVDNM = "stgUvd";

  /**
   * <p>Standard logger name.</p>
   **/
  public static final String LOGSTDNM = "logStd";

  //configuration data:
  /**
   * <p>UVD setting base dir.</p>
   **/
  private String stgUvdDir;

  //parts:
  /**
   * <p>Outside app-beans factory.</p>
   **/
  private IFctApp fctOut;

  //requested data map:
  /**
   * <p>Beans map.</p>
   **/
  private final Map<String, Object> beans = new HashMap<String, Object>();

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @return Object - requested bean or NULL cause it's inner factory
   * @throws Exception - an exception
   */
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
          } else if (STGUVDNM.equals(pBnNm)) {
            rz = lazStgUvd(pRqVs);
          } else if (HldNmCnvStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnvStr(pRqVs);
          } else if (HldIdFdNm.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldIdFdNm();
          } else if (HldGets.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldGets();
          } else if (HldFldCls.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldFldCls();
          } else if (FctNmCnvStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnvStr(pRqVs);
          } else if (UtlPrp.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlPrp();
          } else if (INumStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazNumStr();
          } else if (IReflect.class.getSimpleName().equals(pBnNm)) {
            rz = lazReflect();
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Release beans (memory). This is "memory friendly" factory.</p>
   * @throws Exception - an exception
   */
  public final synchronized void release() throws Exception {
    this.beans.clear();
  }

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
      IOrm<RS> orm = (IOrm<RS>) this.fctOut
        .laz(pRqVs, IOrm.class.getSimpleName());
      rz.setOrm(orm);
      @SuppressWarnings("unchecked")
      IRdb<RS> rdb = (IRdb<RS>) this.fctOut
        .laz(pRqVs, IRdb.class.getSimpleName());
      rz.setRdb(rdb);
      rz.setLog((ILog) this.fctOut.laz(pRqVs, LOGSTDNM));
      this.beans.put(HndI18nRq.class.getSimpleName(), rz);
    }
    return rz;
  }

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
      rz.setReflect(lazReflect());
      rz.setUtlPrp(lazUtlPrp());
      rz.setHldFdCls(lazHldFldCls());
      rz.setLog((ILog) this.fctOut.laz(pRqVs, LOGSTDNM));
      this.beans.put(STGUVDNM, rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnvStr.</p>
   * @param pRqVs request scoped vars
   * @return HldNmCnvStr
   * @throws Exception - an exception
   */
  private HldNmCnvStr lazHldNmCnvStr(
    final Map<String, Object> pRqVs) throws Exception {
    HldNmCnvStr rz = (HldNmCnvStr) this.beans
      .get(HldNmCnvStr.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnvStr();
      rz.setHldFdCls(lazHldFldCls());
      rz.setSetng(lazStgUvd(pRqVs));
      this.beans.put(HldNmCnvStr.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldIdFdNm.</p>
   * @return HldIdFdNm
   */
  private HldIdFdNm lazHldIdFdNm() {
    HldIdFdNm rz = (HldIdFdNm) this.beans
      .get(HldIdFdNm.class.getSimpleName());
    if (rz == null) {
      rz = new HldIdFdNm();
      this.beans.put(HldIdFdNm.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldGets.</p>
   * @return HldGets
   */
  private HldGets lazHldGets() {
    HldGets rz = (HldGets) this.beans
      .get(HldGets.class.getSimpleName());
    if (rz == null) {
      rz = new HldGets();
      rz.setReflect(lazReflect());
      this.beans.put(HldGets.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldFldCls.</p>
   * @return HldFldCls
   */
  private HldFldCls lazHldFldCls() {
    HldFldCls rz = (HldFldCls) this.beans
      .get(HldFldCls.class.getSimpleName());
    if (rz == null) {
      rz = new HldFldCls();
      rz.setReflect(lazReflect());
      this.beans.put(HldFldCls.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter FctNmCnvStr.</p>
   * @param pRqVs request scoped vars
   * @return FctNmCnvStr
   * @throws Exception - an exception
   */
  private FctNmCnvStr lazFctNmCnvStr(
    final Map<String, Object> pRqVs) throws Exception {
    FctNmCnvStr rz = (FctNmCnvStr) this.beans
      .get(FctNmCnvStr.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnvStr();
      rz.setNumStr(lazNumStr());
      rz.setHldNmFdCn(lazHldNmCnvStr(pRqVs));
      rz.setHldGets(lazHldGets());
      rz.setHldIdFdNm(lazHldIdFdNm());
      rz.setHldFdNms(lazStgUvd(pRqVs));
      this.beans.put(FctNmCnvStr.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter UtlPrp.</p>
   * @return UtlPrp
   */
  private UtlPrp lazUtlPrp() {
    UtlPrp rz = (UtlPrp) this.beans.get(UtlPrp.class.getSimpleName());
    if (rz == null) {
      rz = new UtlPrp();
      this.beans.put(UtlPrp.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter NumStr.</p>
   * @return NumStr
   */
  private NumStr lazNumStr() {
    NumStr rz = (NumStr) this.beans.get(INumStr.class.getSimpleName());
    if (rz == null) {
      rz = new NumStr();
      this.beans.put(INumStr.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter Reflect.</p>
   * @return Reflect
   */
  private Reflect lazReflect() {
    Reflect rz = (Reflect) this.beans.get(IReflect.class.getSimpleName());
    if (rz == null) {
      rz = new Reflect();
      this.beans.put(IReflect.class.getSimpleName(), rz);
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctOut.</p>
   * @return IFctNm
   **/
  public final synchronized IFctApp getFctOut() {
    return this.fctOut;
  }

  /**
   * <p>Setter for fctOut.</p>
   * @param pFctOut reference
   **/
  public final synchronized void setFctOut(final IFctApp pFctOut) {
    this.fctOut = pFctOut;
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
