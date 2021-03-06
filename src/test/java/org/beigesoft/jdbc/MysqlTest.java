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

package org.beigesoft.jdbc;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.ResultSet;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.Tst1;
import org.beigesoft.prp.ISetng;

/**
 * <p>Mysql tests.</p>
 *
 * @author Yury Demidenko
 */
public class MysqlTest {

  private FctTstMysql fctApp;
 
  private Map<String, Object> rqVs = new HashMap<String, Object>();

  public MysqlTest() throws Exception {
    this.fctApp = new FctTstMysql();
    this.fctApp.getFctBlc().getFctDt().setLogStdNm(MysqlTest.class.getSimpleName());
    this.fctApp.getFctBlc().getFctDt().setStgOrmDir("mysql");
    ISetng setng = this.fctApp.getFctBlc().lazStgOrm(rqVs);
    this.fctApp.getFctBlc().getFctDt().setDbCls(setng.lazCmnst().get(IOrm.DSCLS));
    this.fctApp.getFctBlc().getFctDt().setDbUsr(setng.lazCmnst().get(IOrm.DBUSR));
    this.fctApp.getFctBlc().getFctDt().setDbPwd(setng.lazCmnst().get(IOrm.DBPSW));
    this.fctApp.getFctBlc().getFctDt().setDbUrl(setng.lazCmnst().get(IOrm.DBURL));
  }

  @Test
  public void tst1() throws Exception {
    Tst1 tst1 = new Tst1();
    tst1.setFctApp(this.fctApp);
    tst1.tst1();
    this.fctApp.release(this.rqVs);
  }
}
