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

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.Lng;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctNmCnvStr;
import org.beigesoft.hld.HldNmCnvStr;
import org.beigesoft.hnd.HndI18nRq;
import org.beigesoft.cnv.IConv;
import org.beigesoft.srv.Reflect;

/**
 * <p>Converters tests.</p>
 *
 * @author Yury Demidenko
 */
public class CnvTest {

  private FctBlc fctApp;

  private CnvStrDbl cnvStrDbl;

  private Map<Class<?>, Map<String, Map<String, String>>> uvdStMp;

  public CnvTest() {
    this.fctApp = new FctBlc();
    this.cnvStrDbl = new CnvStrDbl();
    this.uvdStMp = new HashMap<Class<?>, Map<String, Map<String, String>>>();
    Map<String, Map<String, String>> phFldSts = new HashMap<String, Map<String, String>>();
    this.uvdStMp.put(PersistableHead.class, phFldSts);
    Map<String, String> phDtSt = new HashMap<String, String>();
    phFldSts.put("itsDate", phDtSt);
    phDtSt.put("cnvFrSt", "cnvStrDtTm");
    Map<String, String> phTotSt = new HashMap<String, String>();
    phFldSts.put("itsTotal", phTotSt);
    phTotSt.put("toSt", "toTot");
  }

  @Test
  public void tst1() throws Exception {
    Map<String, Object> rqVs = new HashMap<String, Object>();
    UsPrf upf = new UsPrf();
    DcSp sp = new DcSp();
    sp.setIid(".");
    sp.setNme("Dot");
    upf.setDcSp(sp);
    DcGrSp gsp = new DcGrSp();
    gsp.setIid(",");
    gsp.setNme("Comma");
    upf.setDcGrSp(gsp);
    HndI18nRq hndI18nRq = (HndI18nRq) this.fctApp.laz(null, HndI18nRq.class.getSimpleName());
    CmnPrf cpf = hndI18nRq.revCmnPrf(upf);
    Double dbVl = 12345.69;
    String dbStVl = "123,45.69";
    rqVs.put("cpf", cpf);
    rqVs.put("upf", upf);
    Double dbVlc = this.cnvStrDbl.conv(rqVs, dbStVl);
    assertEquals(dbVl, dbVlc);
  }
  
  @Test
  public void tst2() throws Exception {
    HldNmCnvStr hldNmCnvStr = (HldNmCnvStr) this.fctApp.laz(null, HldNmCnvStr.class.getSimpleName());
    FctNmCnvStr fctNmCnvStr = (FctNmCnvStr) this.fctApp.laz(null, FctNmCnvStr.class.getSimpleName());
    Reflect reflect = (Reflect) this.fctApp.laz(null, Reflect.class.getSimpleName());
    Lng lng = new Lng();
    lng.setIid("ru");
    lng.setNme("Russian");
    Map<String, String> rz = new HashMap<String, String>();
    for (Field fld : reflect.retFlds(Lng.class)) {
      String cnNm = hldNmCnvStr.get(Lng.class, fld.getName());
      IConv<Object, String> cnv = (IConv<Object, String>) fctNmCnvStr.laz(null, cnNm);
      Method gets = reflect.retGet(Lng.class, fld.getName());
      Object fdVl = gets.invoke(lng);
      String fdSvl = cnv.conv(null, fdVl);
      System.out.println("Class/field/value: Lng/" + fld.getName() + "/" + fdSvl);
      rz.put(fld.getName(), fdSvl);
    }
    assertEquals(Boolean.FALSE.toString(), rz.get("isNew"));
    assertEquals(lng.getNme(), rz.get("nme"));
    assertEquals(lng.getIid(), rz.get("iid"));
    assertEquals("", rz.get("ver"));
  }
}
