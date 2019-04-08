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

import org.beigesoft.mdl.RecSetTst;
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
import org.beigesoft.mdlp.GdCat;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctTst;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.ISelct;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IReflect;

/**
 * <p>Converters from RS tests.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class FromRsTest<RS> {

  private FctBlc<RS> fctApp;

  private ILog logStd;
 
  private Map<String, Object> rqVs = new HashMap<String, Object>();

  public FromRsTest() throws Exception {
    this.fctApp = new FctBlc<RS>();
    FctTst fctTst = new FctTst();
    fctTst.setLogStdNm(FromRsTest.class.getSimpleName());
    this.fctApp.setStgOrmDir("tstOrm");
    this.fctApp.setFctConf(fctTst);
    this.logStd = (ILog) fctApp.laz(this.rqVs, FctBlc.LOGSTDNM);
    this.logStd.setDbgSh(true);
    this.logStd.setDbgFl(4001);
    this.logStd.setDbgCl(8002);
  }

  @Test
  public void tst1() throws Exception {
    //writing:
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
    pl.setVer(1L);
    pl.setDbOr(1);
    pl.setOwnr(ph);
    pl.setItsProduct(gd);
    pl.setItsPrice(new BigDecimal("13.12"));
    pl.setItsTotal(new BigDecimal("13.12"));
    pl.setItsQuantity(new BigDecimal("1.0"));
    RecSetTst<RS> rs = new RecSetTst<RS>();
    //0 level default dpLv=1:
    rs.getData().put("IID", pl.getIid());
    rs.getData().put("VER", pl.getVer());
    rs.getData().put("DBOR", pl.getDbOr());
    rs.getData().put("IDOR", pl.getIdOr());
    rs.getData().put("ITSPRICE", pl.getItsPrice().doubleValue());
    rs.getData().put("ITSTOTAL", pl.getItsTotal().doubleValue());
    rs.getData().put("ITSQUANTITY", pl.getItsQuantity().doubleValue());
      //dpLv 0 - only ID, 1 - its owned entities only ID...
    rs.getData().put("OWNR1IID", pl.getOwnr().getIid()); //only ID, PersistableHeaddpLv=0 slv=0  exit
    //1st level:
    rs.getData().put("ITSPRODUCT1IID", pl.getItsProduct().getIid()); //GoodVersionTimedpLv=2 slv=0
    rs.getData().put("ITSPRODUCT1VER", pl.getItsProduct().getVer());
    rs.getData().put("ITSPRODUCT1DBOR", pl.getItsProduct().getDbOr());
    rs.getData().put("ITSPRODUCT1IDOR", pl.getItsProduct().getIdOr());
    rs.getData().put("ITSPRODUCT1NME", pl.getItsProduct().getNme());
    //2nd level:
    rs.getData().put("GDCAT2IID", pl.getItsProduct().getGdCat().getIid()); //GoodVersionTimedpLv=2 slv=1
    rs.getData().put("GDCAT2VER", pl.getItsProduct().getGdCat().getVer());
    rs.getData().put("GDCAT2DBOR", pl.getItsProduct().getGdCat().getDbOr());
    rs.getData().put("GDCAT2IDOR", pl.getItsProduct().getGdCat().getIdOr());
    rs.getData().put("GDCAT2NME", pl.getItsProduct().getGdCat().getNme());
    rs.getData().put("DEP3IID", pl.getItsProduct().getGdCat().getDep().getIid()); //GoodVersionTimedpLv=2 slv=2 exit
    //filling:
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("PersistableHeaddpLv", 0);
    vs.put("GoodVersionTimedpLv", 2);
    PersistableLine plf = new PersistableLine();
    ISelct selct = (ISelct) this.fctApp.laz(this.rqVs, ISelct.class.getSimpleName());
    String sel = selct.gen(this.rqVs, vs, pl.getClass());
    this.logStd.test(this.rqVs, getClass(), sel);
    assertTrue(sel.contains("PERSISTABLELINE.VER as VER"));
    assertTrue(sel.contains("GDCAT2.NME as GDCAT2NME"));
    assertTrue(sel.contains("left join DEPARTMENT as DEP3 on GDCAT2.DEP=DEP3.IID"));
    assertFalse(sel.contains("OWNR1.ITSSTATUS as OWNR1ITSSTATUS"));
    FilEntRs<RS> filEntRs = (FilEntRs<RS>) this.fctApp.laz(this.rqVs, FilEntRs.class.getSimpleName());
    filEntRs.fill(this.rqVs, vs, plf, rs);
    assertEquals(pl.getIid(), plf.getIid());
    assertEquals(pl.getVer(), plf.getVer());
    assertEquals(pl.getDbOr(), plf.getDbOr());
    assertEquals(pl.getItsPrice(), plf.getItsPrice());
    assertEquals(pl.getItsTotal(), plf.getItsTotal());
    assertEquals(pl.getItsQuantity(), plf.getItsQuantity());
    assertEquals(pl.getOwnr().getIid(), plf.getOwnr().getIid());
    assertNull(plf.getOwnr().getVer());
    assertEquals(pl.getItsProduct().getIid(), plf.getItsProduct().getIid());
    assertEquals(pl.getItsProduct().getVer(), plf.getItsProduct().getVer());
    assertEquals(pl.getItsProduct().getDbOr(), plf.getItsProduct().getDbOr());
    assertEquals(pl.getItsProduct().getIdOr(), plf.getItsProduct().getIdOr());
    assertEquals(pl.getItsProduct().getNme(), plf.getItsProduct().getNme());
    assertEquals(pl.getItsProduct().getGdCat().getIid(), plf.getItsProduct().getGdCat().getIid());
    assertEquals(pl.getItsProduct().getGdCat().getVer(), plf.getItsProduct().getGdCat().getVer());
    assertEquals(pl.getItsProduct().getGdCat().getDbOr(), plf.getItsProduct().getGdCat().getDbOr());
    assertEquals(pl.getItsProduct().getGdCat().getNme(), plf.getItsProduct().getGdCat().getNme());
    assertEquals(pl.getItsProduct().getGdCat().getDep().getIid(), plf.getItsProduct().getGdCat().getDep().getIid());
    assertNull(plf.getItsProduct().getGdCat().getDep().getVer());
    vs.remove("PersistableHeaddpLv");
    String[] ndFds = new String[] {"iid", "isClosed", "itsDate", "itsStatus"};
    vs.put("PersistableHeadndFds", ndFds);
    sel = selct.gen(this.rqVs, vs, pl.getClass());
    this.logStd.test(this.rqVs, getClass(), sel);
    assertTrue(sel.contains("OWNR1.ITSSTATUS as OWNR1ITSSTATUS"));
    rs.getData().put("OWNR1ITSSTATUS", pl.getOwnr().getItsStatus().ordinal());
    rs.getData().put("OWNR1ITSDATE", pl.getOwnr().getItsDate().getTime());
    rs.getData().put("OWNR1ISCLOSED", pl.getOwnr().getIsClosed() ? 1 : 0);
    plf = new PersistableLine();
    filEntRs.fill(this.rqVs, vs, plf, rs);
    assertEquals(pl.getIid(), plf.getIid());
    assertEquals(pl.getVer(), plf.getVer());
    assertEquals(pl.getDbOr(), plf.getDbOr());
    assertEquals(pl.getItsPrice(), plf.getItsPrice());
    assertEquals(pl.getItsTotal(), plf.getItsTotal());
    assertEquals(pl.getItsQuantity(), plf.getItsQuantity());
    assertEquals(pl.getOwnr().getIid(), plf.getOwnr().getIid());
    assertEquals(pl.getOwnr().getItsDate(), plf.getOwnr().getItsDate());
    assertEquals(pl.getOwnr().getItsStatus(), plf.getOwnr().getItsStatus());
    assertEquals(pl.getOwnr().getIsClosed(), plf.getOwnr().getIsClosed());
    assertNull(plf.getOwnr().getVer());
  }
}
