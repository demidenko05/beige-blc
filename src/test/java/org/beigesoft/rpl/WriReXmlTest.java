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

package org.beigesoft.rpl;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.io.StringReader;
import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdl.EStatus;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.mdlp.UsRlTmc;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.Department;
import org.beigesoft.mdlp.GoodsRating;
import org.beigesoft.mdlp.GoodVersionTime;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctTst;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prp.Setng;
import org.beigesoft.srv.UtlXml;
import org.beigesoft.srv.IUtlXml;

/**
 * <p>Write/read XML tests.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class WriReXmlTest<RS> {

  String strXmlDepartment = "class=\"org.beigesoft.test.persistable.Department\" itsId=\"1\" itsName=\"ICT\"/&gt;";

  String strXmlPersHead = "class=\"org.beigesoft.test.persistable.PersistableHead\" itsDate=\"1475156484845\" itsStatus=\"0\" isClosed=\"true\" itsTotal=\"523.66\" itsInteger=\"NULL\"/&gt;";

  private FctBlc<RS> fctApp;

  private Map<String, Object> rqVs;

  private ILog logStd;

  public WriReXmlTest() throws Exception {
    this.rqVs = new HashMap<String, Object>();
    this.fctApp = new FctBlc<RS>();
    FctTst fctTst = new FctTst();
    fctTst.setLogStdNm(WriReXmlTest.class.getSimpleName());
    this.fctApp.setStgDbCpDir("tstDbCp");
    this.fctApp.setFctConf(fctTst);
    this.logStd = (ILog) fctApp.laz(this.rqVs, FctBlc.LOGSTDNM);
    this.logStd.setDbgSh(true);
    this.logStd.setDbgFl(4001);
    this.logStd.setDbgCl(7002);
  }

  @Test
  public void tst1() throws Exception {
    //String:
    UtlXml utlXml = (UtlXml) this.fctApp.laz(this.rqVs, IUtlXml.class.getSimpleName());
    this.logStd.test(this.rqVs, getClass(), this.strXmlDepartment + " ->:");
    this.logStd.test(this.rqVs, getClass(), utlXml.unescStr(this.strXmlDepartment));
    StringReader reader = new StringReader(this.strXmlDepartment);
    Map<String, String> attributesMap = utlXml.readAttrs(this.rqVs, reader);
    assertEquals("org.beigesoft.test.persistable.Department", attributesMap.get("class")); 
    assertEquals("1", attributesMap.get("itsId")); 
    assertEquals("ICT", attributesMap.get("itsName")); 
    //Entity:
    RpEntWriXml rpEntWriXml = (RpEntWriXml) this.fctApp.laz(this.rqVs, FctBlc.ENWRDBCPNM);
    //write:
    UsRlTmc usRlTmc = new UsRlTmc();
    usRlTmc.setRol("adminr");
    UsTmc usr = new UsTmc();
    usr.setIid("adminu");
    usRlTmc.setUsr(usr);
    File fl = new File("target" + File.separator + "UsRlTmc.xml");
    OutputStreamWriter wri = new OutputStreamWriter(
      new FileOutputStream(fl), Charset.forName("UTF-8").newEncoder());
    try {
      wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      wri.write("<data sourceId=\"127\">\n");
      rpEntWriXml.write(this.rqVs, usRlTmc, wri);
      wri.write("</data>\n");
    } finally {
      if (wri != null) {
        wri.close();
      }
    }
    //fill:
    UsRlTmc usRlTmcf = null;
    RpEntReadXml rpEntReadXml = (RpEntReadXml) this.fctApp.laz(this.rqVs, FctBlc.ENRDDBCPNM);
    InputStreamReader isr = new InputStreamReader(
      new FileInputStream(fl), Charset.forName("UTF-8").newDecoder());
    try {
      utlXml.readUntilStart(isr, "data");
      attributesMap = utlXml.readAttrs(this.rqVs, isr);
      assertEquals("127", attributesMap.get("sourceId")); 
      utlXml.readUntilStart(isr, "entity");
      usRlTmcf = (UsRlTmc) rpEntReadXml.read(this.rqVs, isr);
    } finally {
      if (isr != null) {
        isr.close();
      }
    }
    assertEquals(usRlTmc.getIsNew(), usRlTmcf.getIsNew());
    assertEquals(usRlTmc.getRol(), usRlTmcf.getRol());
    assertEquals(usRlTmc.getUsr().getIid(), usRlTmcf.getUsr().getIid());
    assertEquals(usRlTmc.getIid().getRol(), usRlTmcf.getIid().getRol());
    assertEquals(usRlTmc.getIid().getUsr().getIid(), usRlTmcf.getIid().getUsr().getIid());
    //write:
    PersistableHead prsh = new PersistableHead();
    prsh.setIid(3L);
    prsh.setVer(124L);
    prsh.setItsStatus(EStatus.STATUS_A);
    prsh.setItsInteger(12397);
    prsh.setItsLong(5674L);
    prsh.setItsFloat(123.6934F);
    prsh.setItsDouble(56325.5687);
    prsh.setItsDate(new Date());
    prsh.setItsTotal(new BigDecimal("12345.60"));
    Department dep = new Department();
    dep.setIid(3L);
    dep.setNme("Dep3");
    prsh.setItsDepartment(dep);
    //fill:
    /*PersistableHead prshf = new PersistableHead();
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
    assertEquals(prsh.getItsLong(), prshf.getItsLong());*/
    //write:
    GoodsRating goodsRating = new GoodsRating();
    goodsRating.setAverageRating(357);
    GoodVersionTime gvt = new GoodVersionTime();
    gvt.setIid(5L);
    goodsRating.setIid(gvt);
    //fill:
    /*GoodsRating goodsRatingf = new GoodsRating();
    filEntRq.fill(this.rqVs, goodsRatingf, reqDt);
    assertEquals(goodsRating.getIid().getIid(), goodsRatingf.getIid().getIid());
    assertEquals(goodsRating.getIsNew(), goodsRatingf.getIsNew());
    assertEquals(goodsRating.getGoods().getIid(), goodsRatingf.getGoods().getIid());
    assertEquals(goodsRating.getAverageRating(), goodsRatingf.getAverageRating());*/
  }
}
