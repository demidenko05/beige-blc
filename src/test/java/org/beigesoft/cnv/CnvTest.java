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

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.beigesoft.mdl.ReqDtTst;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdl.EStatus;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.mdlp.UsRlTmc;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.Department;
import org.beigesoft.mdlp.GoodsRating;
import org.beigesoft.mdlp.GoodVersionTime;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctTst;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctNmCnToSt;
import org.beigesoft.hld.HldNmCnToSt;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.srv.Reflect;
import org.beigesoft.srv.IReflect;

/**
 * <p>Converters tests.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class CnvTest<RS> {

  /**
   * <p>Format date-time ISO8601 no time zone,
   * e.g. 2001-07-04T21:55.</p>
   **/
  private final DateFormat dateTimeNoTzFormatIso8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

  private FctBlc<RS> fctApp;

  private UsPrf upf;

  private CmnPrf cpf;

  private Map<String, Object> rqVs;

  private HndI18nRq hndI18nRq;

  private ILog logStd;

  public CnvTest() throws Exception {
    this.rqVs = new HashMap<String, Object>();
    this.fctApp = new FctBlc<RS>();
    FctTst fctTst = new FctTst();
    fctTst.setLogStdNm(CnvTest.class.getSimpleName());
    this.fctApp.setStgUvdDir("tstUvd");
    this.fctApp.setFctConf(fctTst);
    this.logStd = (ILog) fctApp.laz(this.rqVs, FctBlc.LOGSTDNM);
    this.logStd.setDbgSh(true);
    this.logStd.setDbgFl(4001);
    this.logStd.setDbgCl(7002);
    this.upf = new UsPrf();
    DcSp sp = new DcSp();
    sp.setIid(".");
    sp.setNme("Dot");
    this.upf.setDcSp(sp);
    DcGrSp gsp = new DcGrSp();
    gsp.setIid(",");
    gsp.setNme("Comma");
    this.upf.setDcGrSp(gsp);
    this.hndI18nRq = (HndI18nRq) this.fctApp.laz(this.rqVs, HndI18nRq.class.getSimpleName());
    this.cpf = this.hndI18nRq.revCmnPrf(this.upf);
    this.rqVs.put("cpf", this.cpf);
    this.rqVs.put("upf", this.upf);
  }

  @Test
  public void tst1() throws Exception {
    HldNmCnToSt hldNmCnvStr = (HldNmCnToSt) this.fctApp.laz(this.rqVs, HldNmCnToSt.class.getSimpleName());
    FctNmCnToSt fctNmCnvStr = (FctNmCnToSt) this.fctApp.laz(this.rqVs, FctNmCnToSt.class.getSimpleName());
    Reflect reflect = (Reflect) this.fctApp.laz(this.rqVs, IReflect.class.getSimpleName());
    FilEntRq filEntRq = (FilEntRq) this.fctApp.laz(this.rqVs, FilEntRq.class.getSimpleName());
    Setng stgUvd = (Setng) this.fctApp.laz(this.rqVs, this.fctApp.STGUVDNM);
    stgUvd.lazConf();
    stgUvd.lazFldPrp(PersistableHead.class, IHasId.IDNM, HldNmCnToSt.CNVTOSTRNM);
    assertTrue(stgUvd.getClsFs().get(PersistableHead.class).size() == 1);
    //write:
    PersistableHead prsh = new PersistableHead();
    prsh.setIid(1L);
    String dt = "2019-03-27T15:19";
    prsh.setItsDate(this.dateTimeNoTzFormatIso8601.parse(dt));
    prsh.setItsTotal(new BigDecimal("1234.56"));
    Department dep = new Department();
    dep.setIid(2L);
    dep.setNme("Dep1");
    prsh.setItsDepartment(dep);
    ReqDtTst reqDt = new ReqDtTst();
    String parPref = PersistableHead.class.getSimpleName() + ".";
    for (String fdNm : stgUvd.lazFldNms(PersistableHead.class)) {
      String cnNm = hldNmCnvStr.get(PersistableHead.class, fdNm);
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(this.rqVs, cnNm);
      Method gets = reflect.retGet(PersistableHead.class, fdNm);
      Object fdVl = gets.invoke(prsh);
      String fdSvl = cnv.conv(this.rqVs, fdVl);
      this.logStd.test(null, CnvTest.class, "Class/field/value: PersistableHead/" + fdNm + "/" + fdSvl);
      reqDt.getParamsMp().put(parPref + fdNm, fdSvl);
    }
    assertNull(stgUvd.getClsFs().get(PersistableHead.class));
    assertEquals(Boolean.FALSE.toString(), reqDt.getParam(parPref + "isNew"));
    assertEquals(dt, reqDt.getParam(parPref + "itsDate"));
    assertEquals("1,234.56", reqDt.getParam(parPref + "itsTotal"));
    assertEquals("1", reqDt.getParam(parPref + "iid"));
    assertEquals("2", reqDt.getParam(parPref + "itsDepartment"));
    assertEquals("", reqDt.getParam(parPref + "ver"));
    assertTrue(!reqDt.getParamsMp().keySet().contains(parPref + "persistableLines"));
    assertTrue(!reqDt.getParamsMp().keySet().contains(parPref + "tmpDescription"));
    //write:
    UsRlTmc usRlTmc = new UsRlTmc();
    usRlTmc.setRol("adminr");
    UsTmc usr = new UsTmc();
    usr.setIid("adminu");
    usRlTmc.setUsr(usr);
    reqDt = new ReqDtTst();
    parPref = UsRlTmc.class.getSimpleName() + ".";
    for (String fdNm : stgUvd.lazFldNms(UsRlTmc.class)) {
      String cnNm = hldNmCnvStr.get(UsRlTmc.class, fdNm);
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(this.rqVs, cnNm);
      Method gets = reflect.retGet(UsRlTmc.class, fdNm);
      Object fdVl = gets.invoke(usRlTmc);
      String fdSvl = cnv.conv(this.rqVs, fdVl);
      this.logStd.test(null, CnvTest.class, "Class/field/value: UsRlTmc/" + fdNm + "/" + fdSvl);
      reqDt.getParamsMp().put(parPref + fdNm, fdSvl);
    }
    assertEquals("usr=adminu,rol=adminr", reqDt.getParam(parPref + "iid"));
    assertEquals(usRlTmc.getRol(), reqDt.getParam(parPref + "rol"));
    assertEquals(usRlTmc.getUsr().getUsr(), reqDt.getParam(parPref + "usr"));
    assertNotNull(stgUvd.getClsFs().get(UsRlTmc.class));
    //fill:
    UsRlTmc usRlTmcf = new UsRlTmc();
    filEntRq.fill(this.rqVs, usRlTmcf, reqDt);
    assertEquals(usRlTmc.getIid().getRol(), usRlTmcf.getIid().getRol());
    assertEquals(usRlTmc.getIid().getUsr().getIid(), usRlTmcf.getIid().getUsr().getIid());
    assertEquals(usRlTmc.getIsNew(), usRlTmcf.getIsNew());
    assertEquals(usRlTmc.getUsr().getIid(), usRlTmcf.getUsr().getIid());
    assertEquals(usRlTmc.getRol(), usRlTmcf.getRol());
    assertNull(stgUvd.getClsFs().get(UsRlTmc.class));
    //write:
    prsh = new PersistableHead();
    prsh.setIid(3L);
    prsh.setVer(124L);
    prsh.setItsStatus(EStatus.STATUS_A);
    prsh.setItsInteger(12397);
    prsh.setItsLong(5674L);
    prsh.setItsFloat(123.6934F);
    prsh.setItsDouble(56325.5687);
    dt = "2019-03-26T15:19";
    prsh.setItsDate(this.dateTimeNoTzFormatIso8601.parse(dt));
    prsh.setItsTotal(new BigDecimal("12345.60"));
    dep = new Department();
    dep.setIid(3L);
    dep.setNme("Dep3");
    prsh.setItsDepartment(dep);
    reqDt = new ReqDtTst();
    parPref = PersistableHead.class.getSimpleName() + ".";
    for (String fdNm : stgUvd.lazFldNms(PersistableHead.class)) {
      String cnNm = hldNmCnvStr.get(PersistableHead.class, fdNm);
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(this.rqVs, cnNm);
      Method gets = reflect.retGet(PersistableHead.class, fdNm);
      Object fdVl = gets.invoke(prsh);
      String fdSvl = cnv.conv(this.rqVs, fdVl);
      this.logStd.test(null, CnvTest.class, "Class/field/value: PersistableHead/" + fdNm + "/" + fdSvl);
      reqDt.getParamsMp().put(parPref + fdNm, fdSvl);
    }
    assertNull(stgUvd.getClsFs().get(PersistableHead.class));
    assertEquals(Boolean.FALSE.toString(), reqDt.getParam(parPref + "isNew"));
    assertEquals(dt, reqDt.getParam(parPref + "itsDate"));
    assertEquals("12,345.60", reqDt.getParam(parPref + "itsTotal"));
    assertEquals(prsh.getIid().toString(), reqDt.getParam(parPref + "iid"));
    assertEquals(prsh.getItsInteger().toString(), reqDt.getParam(parPref + "itsInteger"));
    assertEquals(prsh.getItsLong().toString(), reqDt.getParam(parPref + "itsLong"));
    assertEquals(prsh.getItsFloat().toString(), reqDt.getParam(parPref + "itsFloat"));
    assertEquals(prsh.getItsDouble().toString(), reqDt.getParam(parPref + "itsDouble"));
    assertEquals(prsh.getItsStatus().name(), reqDt.getParam(parPref + "itsStatus"));
    assertEquals("3", reqDt.getParam(parPref + "itsDepartment"));
    assertEquals("124", reqDt.getParam(parPref + "ver"));
    assertTrue(!reqDt.getParamsMp().keySet().contains(parPref + "persistableLines"));
    assertTrue(!reqDt.getParamsMp().keySet().contains(parPref + "tmpDescription"));
    //fill:
    PersistableHead prshf = new PersistableHead();
    filEntRq.fill(this.rqVs, prshf, reqDt);
    assertEquals(prsh.getIid(), prshf.getIid());
    assertEquals(prsh.getIsNew(), prshf.getIsNew());
    assertEquals(prsh.getVer(), prshf.getVer());
    assertEquals(prsh.getItsDate(), prshf.getItsDate());
    assertEquals(prsh.getItsDepartment().getIid(), prshf.getItsDepartment().getIid());
    assertEquals(prsh.getItsTotal(), prshf.getItsTotal());
    assertEquals(prsh.getItsStatus(), prshf.getItsStatus());
    assertEquals(prsh.getItsInteger(), prshf.getItsInteger());
    assertEquals(prsh.getItsFloat(), prshf.getItsFloat());
    assertEquals(prsh.getItsDouble(), prshf.getItsDouble());
    assertEquals(prsh.getItsLong(), prshf.getItsLong());
    //write:
    GoodsRating goodsRating = new GoodsRating();
    goodsRating.setAverageRating(357);
    GoodVersionTime gvt = new GoodVersionTime();
    gvt.setIid(5L);
    goodsRating.setIid(gvt);
    parPref = GoodsRating.class.getSimpleName() + ".";
    for (String fdNm : stgUvd.lazFldNms(GoodsRating.class)) {
      String cnNm = hldNmCnvStr.get(GoodsRating.class, fdNm);
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(this.rqVs, cnNm);
      Method gets = reflect.retGet(GoodsRating.class, fdNm);
      Object fdVl = gets.invoke(goodsRating);
      String fdSvl = cnv.conv(this.rqVs, fdVl);
      this.logStd.test(null, CnvTest.class, "Class/field/value: GoodsRating/" + fdNm + "/" + fdSvl);
      reqDt.getParamsMp().put(parPref + fdNm, fdSvl);
    }
    assertEquals(gvt.getIid().toString(), reqDt.getParam(parPref + "goods"));
    assertEquals(goodsRating.getAverageRating().toString(), reqDt.getParam(parPref + "averageRating"));
    //fill:
    GoodsRating goodsRatingf = new GoodsRating();
    filEntRq.fill(this.rqVs, goodsRatingf, reqDt);
    assertEquals(goodsRating.getIid().getIid(), goodsRatingf.getIid().getIid());
    assertEquals(goodsRating.getIsNew(), goodsRatingf.getIsNew());
    assertEquals(goodsRating.getGoods().getIid(), goodsRatingf.getGoods().getIid());
    assertEquals(goodsRating.getAverageRating(), goodsRatingf.getAverageRating());
  }
}
