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
import org.beigesoft.hld.HldNmCnvStr;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.prp.UtlPrp;
import org.beigesoft.prp.ISetng;
import org.beigesoft.prp.Setng;
import org.beigesoft.log.ILog;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IRdb;
import org.beigesoft.srv.IOrm;

/**
 * <p>Application beans factory of beige-blc beans. It's inner factory
 * inside final application beans factory.</p>
 *
 * @author Yury Demidenko
 */
public class FctBlc implements IFctApp {

  /**
   * <p>Beans map.</p>
   **/
  private final Map<String, Object> beans = new HashMap<String, Object>();

  /**
   * <p>Outside app-beans factory.</p>
   **/
  private IFctApp fctOut;
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
          } else if (ISetng.class.getSimpleName().equals(pBnNm)) {
            rz = lazSetng(pRqVs);
          } else if (HldNmCnvStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldNmCnvStr();
          } else if (HldFldCls.class.getSimpleName().equals(pBnNm)) {
            rz = lazHldFldCls();
          } else if (FctNmCnvStr.class.getSimpleName().equals(pBnNm)) {
            rz = lazFctNmCnvStr();
          } else if (UtlPrp.class.getSimpleName().equals(pBnNm)) {
            rz = lazUtlPrp();
          } else if (Reflect.class.getSimpleName().equals(pBnNm)) {
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
  private HndI18nRq lazHndI18nRq(
    final Map<String, Object> pRqVs) throws Exception {
    HndI18nRq rz = (HndI18nRq) this.beans
      .get(HndI18nRq.class.getSimpleName());
    if (rz == null) {
      rz = new HndI18nRq();
      rz.setOrm((IOrm) this.fctOut.laz(pRqVs, IOrm.class.getSimpleName()));
      rz.setRdb((IRdb) this.fctOut.laz(pRqVs, IRdb.class.getSimpleName()));
      rz.setLog((ILog) this.fctOut.laz(pRqVs, ILog.class.getSimpleName()));
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
  private Setng lazSetng(final Map<String, Object> pRqVs) throws Exception {
    Setng rz = (Setng) this.beans.get(ISetng.class.getSimpleName());
    if (rz == null) {
      rz = new Setng();
      rz.setReflect(lazReflect());
      rz.setUtlPrp(lazUtlPrp());
      rz.setLog((ILog) this.fctOut.laz(pRqVs, ILog.class.getSimpleName()));
      this.beans.put(ISetng.class.getSimpleName(), rz);
    }
    return rz;
  }

  /**
   * <p>Lazy getter HldNmCnvStr.</p>
   * @return HldNmCnvStr
   */
  private HldNmCnvStr lazHldNmCnvStr() {
    HldNmCnvStr rz = (HldNmCnvStr) this.beans
      .get(HldNmCnvStr.class.getSimpleName());
    if (rz == null) {
      rz = new HldNmCnvStr();
      rz.setHldFdCls(lazHldFldCls());
      this.beans.put(HldNmCnvStr.class.getSimpleName(), rz);
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
   * @return FctNmCnvStr
   */
  private FctNmCnvStr lazFctNmCnvStr() {
    FctNmCnvStr rz = (FctNmCnvStr) this.beans
      .get(FctNmCnvStr.class.getSimpleName());
    if (rz == null) {
      rz = new FctNmCnvStr();
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
   * <p>Lazy getter Reflect.</p>
   * @return Reflect
   */
  private Reflect lazReflect() {
    Reflect rz = (Reflect) this.beans.get(Reflect.class.getSimpleName());
    if (rz == null) {
      rz = new Reflect();
      this.beans.put(Reflect.class.getSimpleName(), rz);
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
}
