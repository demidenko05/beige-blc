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

package org.beigesoft.rdb;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.beigesoft.mdl.ColVals;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdl.EStatus;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.mdlp.UsRlTmc;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.PersistableLine;
import org.beigesoft.mdlp.Department;
import org.beigesoft.mdlp.GoodsRating;
import org.beigesoft.mdlp.GoodVersionTime;
import org.beigesoft.mdlp.UserRoleTomcatPriority;
import org.beigesoft.mdlp.GdCat;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctTst;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;

/**
 * <p>RDB test 1.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class Tst1<RS> {

  private FctBlc<RS> fctApp;

  public void tst1() throws Exception {
    Map<String, Object> rqVs = new HashMap<String, Object>();
    Map<String, Object> vs = new HashMap<String, Object>();
    IOrm orm = (IOrm) this.fctApp.laz(rqVs, IOrm.class.getSimpleName());
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(rqVs, IRdb.class.getSimpleName());
    orm.init(rqVs);
    Setng stgOrm = (Setng) this.fctApp.laz(rqVs, this.fctApp.STGORMNM);
    stgOrm.release();
    Department dp = new Department();
    dp.setIid(1L);
    dp.setDbOr(orm.getDbId());
    dp.setNme("Dep1");
    Department dpf = new Department();
    dpf.setIid(1L);
    try {
      rdb.setAcmt(false);
      rdb.setTrIsl(IRdb.TRRUC);
      rdb.begin();
      orm.refrEnt(rqVs, vs, dpf);
      if (dpf.getIid() == null) {
        dp.setIid(null);
        orm.insIdLn(rqVs, vs, dp);
        dpf.setIid(dp.getIid());
        orm.refrEnt(rqVs, vs, dpf);
      }
      assertEquals(dp.getIid(), dpf.getIid());
      assertNotNull(dpf.getVer());
      assertEquals(dp.getDbOr(), dpf.getDbOr());
      assertEquals(dp.getNme(), dpf.getNme());
      GdCat gdc = new GdCat();
      gdc.setIid(1L);
      gdc.setNme("Cat 1");
      gdc.setDep(dp);
      GoodVersionTime gd = new GoodVersionTime();
      gd.setIid(1L);
      gd.setVer(new Date().getTime());
      gd.setNme("Good 1");
      gd.setGdCat(gdc);
      PersistableHead ph = new PersistableHead();
      ph.setIid(1L);
      ph.setItsStatus(EStatus.STATUS_A);
      ph.setItsDate(new Date());
      PersistableLine pl = new PersistableLine();
      pl.setIid(5L);
      //pl.setVer(1L);
      pl.setDbOr(1);
      pl.setOwnr(ph);
      pl.setItsProduct(gd);
      pl.setItsPrice(new BigDecimal("13.12"));
      pl.setItsTotal(new BigDecimal("13.12"));
      pl.setItsQuantity(new BigDecimal("1.0"));
      rdb.commit();
    } catch (Exception e) {
      if (!rdb.getAcmt()) {
        rdb.rollBack();
      }
      throw e;
    } finally {
      rdb.release();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctApp.</p>
   * @return FctBlc<RS>
   **/
  public final FctBlc<RS> getFctApp() {
    return this.fctApp;
  }

  /**
   * <p>Setter for fctApp.</p>
   * @param pFctApp reference
   **/
  public final void setFctApp(final FctBlc<RS> pFctApp) {
    this.fctApp = pFctApp;
  }
}
