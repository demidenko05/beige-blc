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

import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.mdlp.UsRlTmc;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.Department;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctTst;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctNmCnvStr;
import org.beigesoft.hld.HldNmCnvStr;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.IConv;
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

  private CnvStrDbl cnvStrDbl;

  private UsPrf upf;

  private CmnPrf cpf;

  private Map<String, Object> rqVs;

  private HndI18nRq hndI18nRq;

  public CnvTest() throws Exception {
    this.fctApp = new FctBlc<RS>();
    FctTst fctTst = new FctTst();
    ILog logStd = (ILog) fctTst.laz(null, FctBlc.LOGSTDNM);
    logStd.setDbgSh(true);
    this.fctApp.setStgUvdDir("tstUvd");
    this.fctApp.setFctOut(fctTst);
    this.cnvStrDbl = new CnvStrDbl();
    this.upf = new UsPrf();
    DcSp sp = new DcSp();
    sp.setIid(".");
    sp.setNme("Dot");
    this.upf.setDcSp(sp);
    DcGrSp gsp = new DcGrSp();
    gsp.setIid(",");
    gsp.setNme("Comma");
    this.upf.setDcGrSp(gsp);
    this.hndI18nRq = (HndI18nRq) this.fctApp.laz(null, HndI18nRq.class.getSimpleName());
    this.cpf = this.hndI18nRq.revCmnPrf(this.upf);
    this.rqVs = new HashMap<String, Object>();
    this.rqVs.put("cpf", this.cpf);
    this.rqVs.put("upf", this.upf);
  }

  @Test
  public void tst1() throws Exception {
    Double dbVl = 12345.69;
    String dbStVl = "123,45.69";
    Double dbVlc = this.cnvStrDbl.conv(this.rqVs, dbStVl);
    assertEquals(dbVl, dbVlc);
  }
  
  @Test
  public void tst2() throws Exception {
    HldNmCnvStr hldNmCnvStr = (HldNmCnvStr) this.fctApp.laz(null, HldNmCnvStr.class.getSimpleName());
    FctNmCnvStr fctNmCnvStr = (FctNmCnvStr) this.fctApp.laz(null, FctNmCnvStr.class.getSimpleName());
    Reflect reflect = (Reflect) this.fctApp.laz(this.rqVs, IReflect.class.getSimpleName());
    ISetng stgUvd = (ISetng) this.fctApp.laz(this.rqVs, this.fctApp.STGUVDNM);
    PersistableHead ent = new PersistableHead();
    ent.setIid(1L);
    String dt = "2019-03-27T15:19";
    ent.setItsDate(this.dateTimeNoTzFormatIso8601.parse(dt));
    ent.setItsTotal(new BigDecimal("1234.56"));
    Department dep = new Department();
    dep.setIid(2L);
    dep.setNme("Dep1");
    ent.setItsDepartment(dep);
    Map<String, String> rz = new HashMap<String, String>();
    for (String fdNm : stgUvd.lazFldNms(PersistableHead.class)) {
      String cnNm = hldNmCnvStr.get(PersistableHead.class, fdNm);
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(this.rqVs, cnNm);
      Method gets = reflect.retGet(PersistableHead.class, fdNm);
      Object fdVl = gets.invoke(ent);
      String fdSvl = cnv.conv(this.rqVs, fdVl);
      System.out.println("Class/field/value: PersistableHead/" + fdNm + "/" + fdSvl);
      rz.put(fdNm, fdSvl);
    }
    assertEquals(Boolean.FALSE.toString(), rz.get("isNew"));
    assertEquals(dt, rz.get("itsDate"));
    assertEquals("1,234.56", rz.get("itsTotal"));
    assertEquals("1", rz.get("iid"));
    assertEquals("2", rz.get("itsDepartment"));
    assertEquals("", rz.get("ver"));
    assertTrue(!rz.keySet().contains("persistableLines"));
    assertTrue(!rz.keySet().contains("tmpDescription"));
  }
  
  @Test
  public void tst3() throws Exception {
    HldNmCnvStr hldNmCnvStr = (HldNmCnvStr) this.fctApp.laz(null, HldNmCnvStr.class.getSimpleName());
    FctNmCnvStr fctNmCnvStr = (FctNmCnvStr) this.fctApp.laz(null, FctNmCnvStr.class.getSimpleName());
    Reflect reflect = (Reflect) this.fctApp.laz(this.rqVs, IReflect.class.getSimpleName());
    ISetng stgUvd = (ISetng) this.fctApp.laz(this.rqVs, this.fctApp.STGUVDNM);
    UsRlTmc ent = new UsRlTmc();
    ent.setRol("adminr");
    UsTmc usr = new UsTmc();
    usr.setIid("adminu");
    ent.setUsr(usr);
    Map<String, String> rz = new HashMap<String, String>();
    for (String fdNm : stgUvd.lazFldNms(UsRlTmc.class)) {
      String cnNm = hldNmCnvStr.get(UsRlTmc.class, fdNm);
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(this.rqVs, cnNm);
      Method gets = reflect.retGet(UsRlTmc.class, fdNm);
      Object fdVl = gets.invoke(ent);
      String fdSvl = cnv.conv(this.rqVs, fdVl);
      System.out.println("Class/field/value: UsRlTmc/" + fdNm + "/" + fdSvl);
      rz.put(fdNm, fdSvl);
    }
    assertEquals(ent.getRol(), rz.get("rol"));
    assertEquals(ent.getUsr().getUsr(), rz.get("usr"));
  }
}
