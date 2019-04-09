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

package org.beigesoft.cnv;

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
import org.beigesoft.rdb.ISqlQu;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IReflect;

/**
 * <p>Converters to column values tests.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class ToCvTest<RS> {

  private FctBlc<RS> fctApp;

  private ILog logStd;
 
  private Map<String, Object> rqVs = new HashMap<String, Object>();

  public ToCvTest() throws Exception {
    this.fctApp = new FctBlc<RS>();
    FctTst fctTst = new FctTst();
    fctTst.setLogStdNm(ToCvTest.class.getSimpleName());
    this.fctApp.setStgOrmDir("sqlite");
    this.fctApp.setFctConf(fctTst);
    this.logStd = (ILog) fctApp.laz(this.rqVs, FctBlc.LOGSTDNM);
    this.logStd.setDbgSh(true);
    this.logStd.setDbgFl(4001);
    this.logStd.setDbgCl(8002);
  }

  @Test
  public void tst1() throws Exception {
    Department dp = new Department();
    dp.setIid(1L);
    dp.setVer(new Date().getTime());
    dp.setDbOr(1);
    dp.setNme("Dep1 1");
    GdCat gdc = new GdCat();
    gdc.setIid(2L);
    gdc.setVer(new Date().getTime());
    gdc.setDbOr(1);
    gdc.setNme("Cat 1");
    gdc.setDep(dp);
    GoodVersionTime gd = new GoodVersionTime();
    gd.setIid(3L);
    gd.setVer(new Date().getTime());
    gd.setDbOr(1);
    gd.setNme("Good 1");
    gd.setGdCat(gdc);
    PersistableHead ph = new PersistableHead();
    ph.setIid(4L);
    ph.setVer(1L);
    ph.setDbOr(1);
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
    Map<String, Object> vs = new HashMap<String, Object>();
    ISqlQu selct = (ISqlQu) this.fctApp.laz(this.rqVs, ISqlQu.class.getSimpleName());
    StringBuffer sb  = selct.evSel(this.rqVs, vs, pl.getClass());
    sb.append(" where ");
    selct.evCndId(this.rqVs, pl, sb);
    String sel = sb.toString();
    assertTrue(sel.contains("PERSISTABLELINE.IID=5"));
    this.logStd.test(this.rqVs, getClass(), sel);
    FilCvEnt filCvEnt = (FilCvEnt) this.fctApp.laz(this.rqVs, FilCvEnt.class.getSimpleName());
    ColVals cv = new ColVals();
    filCvEnt.fill(this.rqVs, vs, pl, cv);
    assertEquals(pl.getIid(), cv.getLongs().get("iid"));
    assertEquals(pl.getOwnr().getIid(), cv.getLongs().get("ownr"));
    assertEquals(pl.getItsProduct().getIid(), cv.getLongs().get("itsProduct"));
    assertEquals(Long.valueOf(1L), cv.getLongs().get("ver"));
    assertEquals(Double.valueOf(pl.getItsPrice().doubleValue()), cv.getDoubles().get("itsPrice"));
    assertEquals(Double.valueOf(pl.getItsTotal().doubleValue()), cv.getDoubles().get("itsTotal"));
    assertEquals(Double.valueOf(pl.getItsQuantity().doubleValue()), cv.getDoubles().get("itsQuantity"));
    cv = new ColVals();
    filCvEnt.fill(this.rqVs, vs, ph, cv);
    assertEquals(ph.getIid(), cv.getLongs().get("iid"));
    assertEquals(Long.valueOf(ph.getItsDate().getTime()), cv.getLongs().get("itsDate"));
    assertEquals(Integer.valueOf(ph.getItsStatus().ordinal()), cv.getInts().get("itsStatus"));
    long curTm = new Date().getTime();
    assertTrue(curTm - cv.getLongs().get("ver") < 1000);
    String[] ndFds = new String[] {"iid", "itsStatus", "ver"}; //only change status with optimistic locking
    vs.put("ndFds", ndFds);
    Long oldVer = ph.getVer();
    cv = new ColVals();
    filCvEnt.fill(this.rqVs, vs, ph, cv);
    assertEquals(ph.getIid(), cv.getLongs().get("iid"));
    assertTrue(!cv.getLongs().entrySet().contains("itsDate"));
    assertEquals(Integer.valueOf(ph.getItsStatus().ordinal()), cv.getInts().get("itsStatus"));
    assertEquals(oldVer, cv.getOldVer());
    assertTrue(!oldVer.equals(cv.getLongs().get("ver")));
    assertTrue(cv.getLongs().get("ver") - oldVer < 1000);
    vs.remove("ndFds");
    UsTmc ut = new UsTmc();
    ut.setUsr("usr1");
    UsRlTmc urt = new UsRlTmc();
    urt.setUsr(ut);
    urt.setRol("rol1");
    UserRoleTomcatPriority urtp = new UserRoleTomcatPriority();
    urtp.setPriority(334);
    urtp.setUserRoleTomcat(urt);
    assertNotNull(urtp.getUsr());
    assertNotNull(urtp.getRol());
    cv = new ColVals();
    filCvEnt.fill(this.rqVs, vs, urtp, cv);
    assertEquals(urtp.getIid().getRol(), cv.getStrs().get("rol"));
    assertEquals(urtp.getIid().getUsr().getUsr(), cv.getStrs().get("usr"));
    assertEquals(urtp.getPriority(), cv.getInts().get("priority"));
    assertEquals(Long.valueOf(1L), cv.getLongs().get("ver"));
    sb  = selct.evSel(this.rqVs, vs, urtp.getClass());
    sb.append(" where ");
    selct.evCndId(this.rqVs, urtp, sb);
    sel = sb.toString();
    assertTrue(sel.contains("USERROLETOMCATPRIORITY.ROL='rol1'"));
    assertTrue(sel.contains("USERROLETOMCATPRIORITY.USR='usr1'"));
    this.logStd.test(this.rqVs, getClass(), sel);
    cv = new ColVals();
    urtp.setIid(null);
    filCvEnt.fill(this.rqVs, vs, urtp, cv);
    assertTrue(cv.getStrs().keySet().contains("rol"));
    assertNull(cv.getStrs().get("rol"));
    assertTrue(cv.getStrs().keySet().contains("usr"));
    assertNull(cv.getStrs().get("usr"));
    assertEquals(urtp.getPriority(), cv.getInts().get("priority"));
    assertEquals(Long.valueOf(2L), cv.getLongs().get("ver"));
  }
}
