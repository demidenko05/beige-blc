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
import java.io.StringReader;
import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import org.beigesoft.log.LogSmp;

import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;

/**
 * <p>CsvReader test.</p>
 *
 * @author Yury Demidenko
 */
public class CsvReaderTest {

  //LogSmp logger = new LogSmp();

  CsvRdr csvReader = new CsvRdr();

  CsvMth csvMethod;

  public CsvReaderTest() {
    this.csvMethod = new CsvMth();
    this.csvMethod.setChrst("ASCII");
    this.csvMethod.setClns(new ArrayList<CsvCl>());
    CsvCl colDate = new CsvCl();
    colDate.setIndx(1);
    colDate.setSrIdx(1);
    this.csvMethod.getClns().add(colDate);
    CsvCl colDesc = new CsvCl();
    colDesc.setIndx(2);
    colDesc.setSrIdx(2);
    colDesc.setTxDlm("\"");
    this.csvMethod.getClns().add(colDesc);
    CsvCl colAm = new CsvCl();
    colAm.setIndx(3);
    colAm.setSrIdx(4);
    this.csvMethod.getClns().add(colAm);
    CsvCl colCause = new CsvCl();
    colCause.setIndx(4);
    colCause.setSrIdx(5);
    this.csvMethod.getClns().add(colCause);
  }

  @Test
  public void test1() {
    String filePath = "src" + File.separator + "test" + File.separator
      + "resources" + File.separator + "bnk-stm-lf.csv";
    read1(filePath);
    filePath = "src" + File.separator + "test" + File.separator
      + "resources" + File.separator + "bnk-stm-crlf.csv";
    read1(filePath);
    filePath = "src" + File.separator + "test" + File.separator
      + "resources" + File.separator + "bnk-stm-cr.csv";
    read1(filePath);
  }

  @Test
  public void test2() {
    String filePath = "src" + File.separator + "test" + File.separator
      + "resources" + File.separator + "bnk-stm-head-lf.csv";
    read2(filePath);
  }

  public void read1(String pFilePath) {
    List<String> row1 = null;
    List<String> row2 = null;
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(new FileInputStream(pFilePath),
        Charset.forName(this.csvMethod.getChrst()).newDecoder());
      row1 = this.csvReader.readNext(null, reader, this.csvMethod);
      row2 = this.csvReader.readNext(null, reader, this.csvMethod);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("row1: ");
    for (String v : row1) {
      System.out.print(v + ",");
    }
    System.out.println("");
    assertEquals("01/01/2018", row1.get(0)); 
    assertEquals("01/01/2018", row2.get(0)); 
    assertEquals("23.15", row1.get(2)); 
    assertEquals("23.15", row2.get(2)); 
  }

  public void read2(String pFilePath) {
    List<String> row1 = null;
    List<String> row2 = null;
    List<String> row3 = null;
    List<String> row4 = null;
    List<String> row5 = null;
    List<String> row6 = null;
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(new FileInputStream(pFilePath),
        Charset.forName(this.csvMethod.getChrst()).newDecoder());
      row1 = this.csvReader.readNext(null, reader, this.csvMethod);
      row2 = this.csvReader.readNext(null, reader, this.csvMethod);
      row3 = this.csvReader.readNext(null, reader, this.csvMethod);
      row4 = this.csvReader.readNext(null, reader, this.csvMethod);
      row5 = this.csvReader.readNext(null, reader, this.csvMethod);
      row6 = this.csvReader.readNext(null, reader, this.csvMethod);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("row1: ");
    for (String v : row1) {
      System.out.print(v + ",");
    }
    System.out.println("\nrow2: ");
    for (String v : row2) {
      System.out.print(v + ",");
    }
    System.out.println("\nrow3: ");
    for (String v : row3) {
      System.out.print(v + ",");
    }
    System.out.println("\nrow4: ");
    for (String v : row4) {
      System.out.print(v + ",");
    }
    System.out.println("\nrow5: ");
    for (String v : row5) {
      System.out.print(v + ",");
    }
    System.out.println("");
    assertEquals("Date", row1.get(0)); 
    assertEquals("Description", row1.get(1)); 
    assertEquals("ABC check #175762, ws", row2.get(1)); 
    assertEquals("ABC check #175763", row3.get(1)); 
    assertNull(row4.get(1)); 
    assertEquals("monthly fee", row5.get(1)); 
    assertEquals("23.15", row2.get(2)); 
    assertEquals("-14.15", row4.get(2)); 
    assertEquals("-11.15", row5.get(2)); 
    assertEquals("Cause", row1.get(3)); 
    assertEquals("wrong signature", row2.get(3)); 
    assertNull(row3.get(3)); 
    assertNull(row4.get(3)); 
    assertNull(row5.get(3)); 
    assertNull(row6); 
  }
}
