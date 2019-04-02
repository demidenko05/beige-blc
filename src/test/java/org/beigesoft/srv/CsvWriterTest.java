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

package org.beigesoft.srv;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.math.BigDecimal;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import org.beigesoft.log.LogSmp;
import org.beigesoft.mdl.ECsvClTy;
import org.beigesoft.mdlp.PersistableHead;
import org.beigesoft.mdlp.PersistableLine;
import org.beigesoft.mdlp.GoodVersionTime;
import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;
import org.beigesoft.mdlp.MaFrn;
import org.beigesoft.mdlp.MaFrnLn;

/**
 * <p>CsvWriter test.</p>
 *
 * @author Yury Demidenko
 */
public class CsvWriterTest {

  //LoggerSimple logger = new LoggerSimple();

  CsvWri csvWriter;

  CsvMth csvMethod;

  MaFrn mfp = new MaFrn();

  public CsvWriterTest() {
    this.csvWriter = new CsvWri();
    this.csvWriter.setReflect(new Reflect());
    this.csvWriter.setNumStr(new NumStr());
    this.csvMethod = new CsvMth();
    this.csvMethod.setHasHd(true);
    this.csvMethod.setClns(new ArrayList<CsvCl>());
    CsvCl colDate = new CsvCl();
    colDate.setTyp(ECsvClTy.DATE);
    colDate.setNme("Date");
    colDate.setIndx(1);
    colDate.setSrIdx(1);
    colDate.setDtIdx(1);
    colDate.setFldPh("persistableHead" + "," + "itsDate");
    colDate.setFrmt("MM/dd/yyyy");
    this.csvMethod.getClns().add(colDate);
    CsvCl colDesc = new CsvCl();
    colDesc.setTyp(ECsvClTy.STRING);
    colDesc.setNme("Description");
    colDesc.setIndx(2);
    colDesc.setSrIdx(2);
    colDesc.setDtIdx(1);
    colDesc.setTxDlm("\"");
    colDesc.setFldPh("persistableHead" + "," + "tmpDescription");
    this.csvMethod.getClns().add(colDesc);
    CsvCl colAm = new CsvCl();
    colAm.setTyp(ECsvClTy.NUMERIC);
    colAm.setNme("Total");
    colAm.setIndx(3);
    colAm.setSrIdx(3);
    colAm.setDtIdx(1);
    colAm.setFldPh("itsTotal");
    this.csvMethod.getClns().add(colAm);
    CsvCl colIsClosed = new CsvCl();
    colIsClosed.setTyp(ECsvClTy.BOOLEAN);
    colIsClosed.setNme("IsClosed");
    colIsClosed.setIndx(4);
    colIsClosed.setSrIdx(4);
    colIsClosed.setFrmt("1,0");
    colIsClosed.setDtIdx(1);
    colIsClosed.setFldPh("persistableHead" + "," + "isClosed");
    this.csvMethod.getClns().add(colIsClosed);
    CsvCl colRating = new CsvCl();
    colRating.setNme("Rating");
    colRating.setTyp(ECsvClTy.INTEGER);
    colRating.setIndx(5);
    colRating.setSrIdx(5);
    colRating.setDtIdx(2);
    this.csvMethod.getClns().add(colRating);
    CsvCl colConstant = new CsvCl();
    colConstant.setNme("Constant");
    colConstant.setTyp(ECsvClTy.BOOLEAN);
    colConstant.setIndx(6);
    colConstant.setSrIdx(6);
    colConstant.setCnst("true");
    this.csvMethod.getClns().add(colConstant);
    CsvCl colOptional = new CsvCl();
    colOptional.setNme("Optional");
    colOptional.setTyp(ECsvClTy.STRING);
    colOptional.setIndx(7);
    colOptional.setSrIdx(7);
    this.csvMethod.getClns().add(colOptional);
    CsvCl colPrFr = new CsvCl();
    colPrFr.setTyp(ECsvClTy.STRING);
    colPrFr.setNme("GoodsIdToForeign");
    colPrFr.setIndx(8);
    colPrFr.setSrIdx(8);
    colPrFr.setDtIdx(1);
    colPrFr.setFldPh("itsProduct,iid");
    this.csvMethod.getClns().add(colPrFr);
    mfp = new MaFrn();
    colPrFr.setMaFrn(mfp);
    mfp.setIid(1L);
    mfp.setLns(new ArrayList<MaFrnLn>());
    MaFrnLn mpr1 = new MaFrnLn();
    mpr1.setNtvVl("1");
    mpr1.setFrnVl("PR98JXPE");
    mfp.getLns().add(mpr1);
    MaFrnLn mpr2 = new MaFrnLn();
    mpr2.setNtvVl("2");
    mpr2.setFrnVl("PL98JXKL");
    mfp.getLns().add(mpr2);
    HldMatchForeignOnly hmf = new HldMatchForeignOnly();
    hmf.setMatchForeign(mfp);
    this.csvWriter.setHlMaFrn(hmf);
  }

  @Test
  public void test1() throws Exception {
    List<List<Object>> data = new ArrayList<List<Object>>();
    List<Object> dataRow = new ArrayList<Object>();
    data.add(dataRow);
    List<Object> dataRow2 = new ArrayList<Object>();
    data.add(dataRow2);
    List<Object> dataRow3 = new ArrayList<Object>();
    data.add(dataRow3);
    PersistableLine pl = new PersistableLine();
    PersistableHead ph = new PersistableHead();
    ph.setTmpDescription("sample a, sample b...");
    ph.setItsDate(new Date());
    ph.setIsClosed(true);
    pl.setPersistableHead(ph);
    GoodVersionTime pr1 = new GoodVersionTime();
    pr1.setIid(1L);
    pl.setItsProduct(pr1);
    pl.setItsTotal(new BigDecimal("12.22"));
    dataRow.add(pl); 
    dataRow.add(Integer.valueOf(2));
    PersistableLine pl2 = new PersistableLine();
    PersistableHead ph2 = new PersistableHead();
    ph2.setItsDate(new Date());
    pl2.setPersistableHead(ph2);
    GoodVersionTime pr2 = new GoodVersionTime();
    pr2.setIid(2L);
    pl2.setItsProduct(pr2);
    pl2.setItsTotal(new BigDecimal("52.22"));
    dataRow2.add(pl2);
    dataRow2.add(Integer.valueOf(5));
    dataRow3.add(null);
    dataRow3.add(null);
    File file = new File("target" + File.separator + "persistableLines.csv");
    this.csvWriter.write(null, data, this.csvMethod, new FileOutputStream(file));
  }
}
