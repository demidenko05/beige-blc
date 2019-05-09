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

import org.beigesoft.exc.ExcCode;
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
import org.beigesoft.fct.IFctAsm;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.FilCvEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;

/**
 * <p>RDB test 1.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class Tst1<RS> {

  private IFctAsm<RS> fctApp;

  public void tst1() throws Exception {
    Map<String, Object> rvs = new HashMap<String, Object>();
    Map<String, Object> vs = new HashMap<String, Object>();
    IOrm orm = (IOrm) this.fctApp.laz(rvs, IOrm.class.getSimpleName());
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(rvs, IRdb.class.getSimpleName());
    orm.init(rvs);
    Setng stgOrm = (Setng) this.fctApp.laz(rvs, FctBlc.STGORMNM);
    stgOrm.release();
    try {
      rdb.setAcmt(false);
      rdb.setTrIsl(IRdb.TRRUC);
      rdb.begin();
      //insert autoID/select:
      Department dp = new Department();
      dp.setIid(1L);
      orm.refrEnt(rvs, vs, dp);
      if (dp.getIid() == null) {
        dp.setIid(null);
        dp.setNme("Dep1");
        dp.setDbOr(orm.getDbId());
        orm.insIdLn(rvs, vs, dp);
      }
      assertEquals("1", stgOrm.lazClsStg(Department.class, "vrAlg"));
      Department dpf = new Department();
      dpf.setIid(dp.getIid());
      orm.refrEnt(rvs, vs, dpf);
      assertEquals(dp.getIid(), dpf.getIid());
      assertNotNull(dp.getVer());
      assertNotNull(dpf.getVer());
      assertEquals(dp.getVer(), dpf.getVer());
      assertEquals(dp.getDbOr(), dpf.getDbOr());
      assertEquals(dp.getNme(), dpf.getNme());
      //custom retrieving:
      GdCat gdc = new GdCat();
      gdc.setIid(1L);
      orm.refrEnt(rvs, vs, gdc);
      if (gdc.getIid() == null) {
        gdc.setIid(null);
        gdc.setDbOr(orm.getDbId());
        gdc.setNme("Cat 1");
        gdc.setDep(dp);
        orm.insIdLn(rvs, vs, gdc);
      }
      GoodVersionTime gd = new GoodVersionTime();
      assertEquals("1", stgOrm.lazClsStg(GoodVersionTime.class, "vrAlg"));
      gd.setIid(1L);
      orm.refrEnt(rvs, vs, gd);
      if (gd.getIid() == null) {
        gd.setIid(null);
        gd.setDbOr(orm.getDbId());
        gd.setNme("Good 1");
        gd.setGdCat(gdc);
        orm.insIdLn(rvs, vs, gd);
      }
      PersistableHead ph = new PersistableHead();
      assertEquals("1", stgOrm.lazClsStg(PersistableHead.class, "vrAlg"));
      ph.setIid(1L);
      orm.refrEnt(rvs, vs, ph);
      if (ph.getIid() == null) {
        ph.setDbOr(orm.getDbId());
        ph.setItsDepartment(dp);
        ph.setIid(null);
        ph.setItsStatus(EStatus.STATUS_A);
        ph.setItsDate(new Date());
        ph.setItsInteger(12397);
        ph.setItsLong(5674L);
        ph.setItsFloat(123.6934F);
        ph.setItsDouble(56325.5687);
        ph.setItsTotal(new BigDecimal("12345.60"));
        orm.insIdLn(rvs, vs, ph);
      }
      assertTrue(ph.getItsTotal().compareTo(BigDecimal.ZERO) == 1);
      PersistableLine pl = new PersistableLine();
      pl.setIid(1L);
      orm.refrEnt(rvs, vs, pl);
      if (pl.getIid() == null) {
        pl.setIid(null);
        pl.setDbOr(orm.getDbId());
        pl.setOwnr(ph);
        pl.setItsProduct(gd);
        pl.setItsPrice(new BigDecimal("13.12"));
        pl.setItsTotal(new BigDecimal("13.12"));
        pl.setItsQuantity(new BigDecimal("1.0"));
        orm.insIdLn(rvs, vs, pl);
      }
      vs.put("PersistableHeaddpLv", 0);
      vs.put("GoodVersionTimedpLv", 2);
      PersistableLine plf = new PersistableLine();
      plf.setIid(pl.getIid());
      orm.refrEnt(rvs, vs, plf);
      assertEquals(pl.getIid(), plf.getIid());
      assertEquals(pl.getVer(), plf.getVer());
      assertEquals(pl.getDbOr(), plf.getDbOr());
      assertEquals(pl.getItsPrice(), plf.getItsPrice());
      assertEquals(pl.getItsTotal(), plf.getItsTotal());
      assertEquals(pl.getItsQuantity(), plf.getItsQuantity());
      assertEquals(pl.getOwnr().getIid(), plf.getOwnr().getIid());
      assertNull(plf.getOwnr().getVer());
      assertNull(plf.getOwnr().getItsStatus());
      assertNull(plf.getOwnr().getItsDate());
      assertEquals(pl.getItsProduct().getIid(), plf.getItsProduct().getIid());
      assertEquals(pl.getItsProduct().getVer(), plf.getItsProduct().getVer());
      assertEquals(pl.getItsProduct().getDbOr(), plf.getItsProduct().getDbOr());
      assertEquals(pl.getItsProduct().getIdOr(), plf.getItsProduct().getIdOr());
      assertEquals(pl.getItsProduct().getNme(), plf.getItsProduct().getNme());
      assertEquals(pl.getItsProduct().getGdCat().getIid(), plf.getItsProduct().getGdCat().getIid());
      assertEquals(gdc.getVer(), plf.getItsProduct().getGdCat().getVer());
      assertEquals(gdc.getDbOr(), plf.getItsProduct().getGdCat().getDbOr());
      assertEquals(gdc.getNme(), plf.getItsProduct().getGdCat().getNme());
      assertEquals(gdc.getDep().getIid(), plf.getItsProduct().getGdCat().getDep().getIid());
      assertNull(plf.getItsProduct().getGdCat().getDep().getVer());
      vs.clear();
      String[] ndFds = new String[] {"iid", "isClosed", "itsDate", "itsStatus"};
      vs.put("PersistableHeadndFds", ndFds);
      plf = new PersistableLine();
      assertEquals("0", stgOrm.lazClsStg(PersistableLine.class, "vrAlg"));
      plf.setIid(pl.getIid());
      orm.refrEnt(rvs, vs, plf);
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
      //update chosen fields:
      ndFds = new String[] {"iid", "itsStatus", "ver"}; //only change status with optimistic locking
      PersistableHead phf = new PersistableHead();
      phf.setIid(ph.getIid());
      vs.clear();
      vs.put("PersistableHeadndFds", ndFds);
      orm.refrEnt(rvs, vs, phf);
      assertNull(phf.getItsDate());
      if (phf.getItsStatus().equals(EStatus.STATUS_B)) {
        phf.setItsStatus(EStatus.STATUS_A);
      } else {
        phf.setItsStatus(EStatus.STATUS_B);
      }
      vs.clear();
      vs.put("ndFds", ndFds);
      orm.update(rvs, vs, phf);
      assertTrue(!phf.getItsStatus().equals(ph.getItsStatus()));
      assertTrue(!phf.getVer().equals(ph.getVer()));
      orm.refrEnt(rvs, vs, phf);
      assertTrue(!phf.getItsStatus().equals(ph.getItsStatus()));
      assertTrue(!phf.getVer().equals(ph.getVer()));
      vs.clear();
      if (!this.fctApp.getFctBlc().getIsAndr()) {
        //fast update:
        SrvClVl srvClVl = (SrvClVl) this.fctApp.laz(rvs, SrvClVl.class.getSimpleName());
        Long newVer = phf.getVer() + 1;
        ColVals cv = new ColVals();
        srvClVl.put(cv, "iid", phf.getIid());
        srvClVl.put(cv, "itsTotal", "ITSTOTAL-" + ph.getItsTotal().add(BigDecimal.ONE));
        srvClVl.put(cv, "ver", "VER+1");
        srvClVl.putExpr(cv, "ver");
        srvClVl.putExpr(cv, "itsTotal");
        //without version OL/ OL by constraint itsTotal >= 0
        String fastloc = "fastloc"; //Postgres after exception required rollback!!!
        rdb.creSavPnt(fastloc);
        muExFstLoc(rdb, phf.getClass(), cv, srvClVl.evWheUpd(phf.getClass(), cv));
        rdb.rollBack(fastloc);
        srvClVl.put(cv, "itsTotal", "ITSTOTAL-" + ph.getItsTotal());
        rdb.update(phf.getClass(), cv, srvClVl.evWheUpd(phf.getClass(), cv));
        orm.refrEnt(rvs, vs, phf);
        assertTrue(phf.getItsTotal().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(!phf.getVer().equals(ph.getVer()));
        assertEquals(newVer, phf.getVer());
        srvClVl.put(cv, "itsTotal", "ITSTOTAL+" + ph.getItsTotal());
        rdb.update(phf.getClass(), cv, srvClVl.evWheUpd(phf.getClass(), cv));
        orm.refrEnt(rvs, vs, phf);
        assertTrue(phf.getItsTotal().compareTo(ph.getItsTotal()) == 0);
      }
      //composite ID:
      UsTmc ut = new UsTmc();
      assertEquals("0", stgOrm.lazClsStg(UsTmc.class, "vrAlg"));
      ut.setUsr("usr1");
      orm.refrEnt(rvs, vs, ut);
      if (ut.getIid() == null) {
        ut.setUsr("usr1");
        ut.setPwd("pwd1");
        orm.insIdNln(rvs, vs, ut);
        orm.refrEnt(rvs, vs, ut);
      }
      assertEquals("usr1", ut.getUsr());
      assertEquals("pwd1", ut.getPwd());
      UsRlTmc urt = new UsRlTmc();
      urt.setUsr(ut);
      urt.setRol("rol1");
      orm.refrEnt(rvs, vs, urt);
      if (urt.getIid() == null) {
        urt.setUsr(ut);
        urt.setRol("rol1");
        orm.insIdNln(rvs, vs, urt);
        orm.refrEnt(rvs, vs, urt);
        assertEquals("0", stgOrm.getClsStgs().get(UsRlTmc.class).get("vrAlg"));
        
      }
      assertEquals("usr1", urt.getUsr().getUsr());
      assertEquals("rol1", urt.getRol());
      //inner transaction rollback and OL by VER:
      String cakeins = "cakeins";
      rdb.creSavPnt(cakeins);
      GoodVersionTime ca = new GoodVersionTime();
      ca.setIid(2L);
      orm.refrEnt(rvs, vs, ca);
      if (ca.getIid() == null) {
        ca.setIid(null);
        ca.setDbOr(orm.getDbId());
        ca.setNme("Cake 1");
        ca.setGdCat(gdc);
        orm.insIdLn(rvs, vs, ca);
        orm.refrEnt(rvs, vs, ca);
      }
      assertNotNull(ca.getIid());
      ca.setVer(ca.getVer() + 1L);
      muExOptLoc(orm, rvs, vs, ca);
      rdb.rollBack(cakeins);
      orm.refrEnt(rvs, vs, ca);
      assertNull(ca.getIid());
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

  private void muExOptLoc(IOrm pOrm, Map<String, Object> pRvs,
    Map<String, Object> pVs, GoodVersionTime pCa) throws Exception {
    boolean isEx = false;
    try {
      pOrm.update(pRvs, pVs, pCa);
    } catch (ExcCode e) {
      if (e.getCode() == IOrm.DRTREAD) {
        isEx = true;
      }
      e.printStackTrace();
    }
    if (!isEx) {
      throw new Exception("It must be exception!");
    }
  }

  private void muExFstLoc(IRdb<RS> pRdb, Class<?> pCls, ColVals pCv,
    String pWhe) throws Exception {
    boolean isEx = false;
    try {
      pRdb.update(pCls, pCv, pWhe);
    } catch (Exception e) {
      isEx = true;
      e.printStackTrace();
    }
    if (!isEx) {
      throw new Exception("It must be exception!");
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctApp.</p>
   * @return FctBlc<RS>
   **/
  public final IFctAsm<RS> getFctApp() {
    return this.fctApp;
  }

  /**
   * <p>Setter for fctApp.</p>
   * @param pFctApp reference
   **/
  public final void setFctApp(final IFctAsm<RS> pFctApp) {
    this.fctApp = pFctApp;
  }
}
