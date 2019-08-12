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
import org.beigesoft.mdlp.DbInf;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.mdlp.UsRlTmc;
import org.beigesoft.mdlp.UserRoleTomcatPriority;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.PersistableLine;
import org.beigesoft.mdlp.Department;
import org.beigesoft.mdlp.GoodsRating;
import org.beigesoft.mdlp.GoodVersionTime;
import org.beigesoft.mdlp.GdCat;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctTst;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctDt;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.ISqlQu;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IReflect;

/**
 * <p>Converters from RS tests.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class FromRsTest<RS> {

  private FctTst<RS> fctApp;
 
  private Map<String, Object> rqVs = new HashMap<String, Object>();

  public FromRsTest() throws Exception {
    this.fctApp = new FctTst<RS>();
    this.fctApp.getFctBlc().getFctDt().setLogStdNm(FromRsTest.class.getSimpleName());
    this.fctApp.getFctBlc().getFctDt().setStgOrmDir("sqlite");
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).setDbgFl(4001);
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).setDbgCl(8005);
  }

  @Test
  public void tst1() throws Exception {
    //writing:
    Department dp = new Department();
    dp.setIid(235569L);
    dp.setVer(new Date().getTime());
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
    rs.getData().put("OWNR", pl.getOwnr().getIid()); //only ID, PersistableHeaddpLv=0 slv=0  exit
    //1st level:
    rs.getData().put("ITSPRODUCT16IID", pl.getItsProduct().getIid()); //GoodVersionTimedpLv=2 slv=0
    rs.getData().put("ITSPRODUCT16VER", pl.getItsProduct().getVer());
    rs.getData().put("ITSPRODUCT16DBOR", pl.getItsProduct().getDbOr());
    rs.getData().put("ITSPRODUCT16IDOR", pl.getItsProduct().getIdOr());
    rs.getData().put("ITSPRODUCT16NME", pl.getItsProduct().getNme());
    //2nd level:
    rs.getData().put("GDCAT211IID", pl.getItsProduct().getGdCat().getIid()); //GoodVersionTimedpLv=2 slv=1
    rs.getData().put("GDCAT211VER", pl.getItsProduct().getGdCat().getVer());
    rs.getData().put("GDCAT211DBOR", pl.getItsProduct().getGdCat().getDbOr());
    rs.getData().put("GDCAT211IDOR", pl.getItsProduct().getGdCat().getIdOr());
    rs.getData().put("GDCAT211NME", pl.getItsProduct().getGdCat().getNme());
    rs.getData().put("GDCAT211DEP", pl.getItsProduct().getGdCat().getDep().getIid()); //GoodVersionTimedpLv=2 slv=2 exit
    //filling:
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("PersistableHeaddpLv", 0);
    vs.put("GoodVersionTimedpLv", 2);
    PersistableLine plf = new PersistableLine();
    ISqlQu selct = (ISqlQu) this.fctApp.laz(this.rqVs, ISqlQu.class.getSimpleName());
    String sel = selct.evSel(this.rqVs, vs, pl.getClass()).toString();
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), sel);
    String cr = selct.evCreate(this.rqVs, pl.getClass());
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), cr);
    assertTrue(sel.contains("PERSISTABLELINE.VER as VER"));
    assertTrue(sel.contains("GDCAT211.NME as GDCAT211NME"));
    assertTrue(sel.contains("GDCAT211.DEP as GDCAT211DEP"));
    assertFalse(sel.contains("left join DEPARTMENT"));
    assertTrue(sel.contains("PERSISTABLELINE.OWNR as OWNR"));
    assertFalse(sel.contains("OWNR15.ITSSTATUS as OWNR15ITSSTATUS"));
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
    String[] ndFds = new String[] {"isClosed", "itsDate", "itsStatus"};
    vs.put("PersistableHeadndFds", ndFds);
    sel = selct.evSel(this.rqVs, vs, pl.getClass()).toString();
    assertTrue(sel.contains("OWNR15.ITSSTATUS as OWNR15ITSSTATUS"));
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), sel);
    rs.getData().put("OWNR15IID", pl.getOwnr().getIid());
    rs.getData().put("OWNR15ITSSTATUS", pl.getOwnr().getItsStatus().ordinal());
    rs.getData().put("OWNR15ITSDATE", pl.getOwnr().getItsDate().getTime());
    rs.getData().put("OWNR15ISCLOSED", pl.getOwnr().getIsClosed() ? 1 : 0);
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
    sel = selct.evSel(this.rqVs, vs, UsRlTmc.class).toString();
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), sel);
    cr = selct.evCreate(this.rqVs, UsRlTmc.class);
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), cr);
    sel = selct.evSel(this.rqVs, vs, UserRoleTomcatPriority.class).toString();
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), sel);
    cr = selct.evCreate(this.rqVs, UserRoleTomcatPriority.class);
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), cr);
    assertTrue(cr.contains("constraint urtpprioritygt0 check (PRIORITY>0)"));
    Setng stgOrm = (Setng) this.fctApp.laz(this.rqVs, FctDt.STGORMNM);
    String jdbcCls = stgOrm.lazCmnst().get(IOrm.JDBCCLS);
    assertEquals("org.sqlite.JDBC", jdbcCls);
    cr = selct.evCreate(this.rqVs, UsTmc.class);
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), cr);
    cr = selct.evCreate(this.rqVs, DbInf.class);
    this.fctApp.getFctBlc().lazLogStd(this.rqVs).test(this.rqVs, getClass(), cr);
    this.fctApp.release(this.rqVs);
  }
}
