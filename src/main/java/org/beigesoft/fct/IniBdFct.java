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

package org.beigesoft.fct;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.io.File;

import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdl.IHasNm;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.EmCon;
import org.beigesoft.mdlp.EmMsg;
import org.beigesoft.mdlp.EmAtch;
import org.beigesoft.mdlp.EmAdr;
import org.beigesoft.mdlp.EmInt;
import org.beigesoft.mdlp.EmStr;
import org.beigesoft.mdlp.EmRcp;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.mdlp.UsRlTmc;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.DcGrSp;
import org.beigesoft.mdlp.Cntr;
import org.beigesoft.mdlp.Lng;
import org.beigesoft.hld.HldFldStg;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.ICtx;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Business-logic dependent sub-initializer main
 * factory during startup.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class IniBdFct<RS> implements IIniBdFct<RS> {

  /**
   * <p>Initializes factory.</p>
   * @param pRvs request scoped vars
   * @param pFct factory
   * @param pCtx Context
   * @throws Exception - an exception
   **/
  @Override
  public final void iniBd(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct, final ICtx pCtx) throws Exception {
    makeVars(pRvs, pFct, pCtx);
    makeUvdCls(pRvs, pFct);
    makeUvdFds(pRvs, pFct);
    pFct.getFctBlc().getFctDt()
      .setMaFrClss(new HashSet<Class<? extends IHasId<?>>>());
    pFct.getFctBlc().getFctDt().getMaFrClss().add(Lng.class);
  }

  /**
   * <p>Makes variables from web.xml.</p>
   * @param pRvs request scoped vars
   * @param pFct factory app
   * @param pCtx Context
   * @throws Exception - an exception
   **/
  public final void makeVars(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct, final ICtx pCtx) throws Exception {
    pFct.getFctBlc().getFctDt().setUplDir(pCtx.getParam("uplDir"));
    pFct.getFctBlc().getFctDt().setStgUvdDir(pCtx.getParam("uvdDir"));
    pFct.getFctBlc().getFctDt().setStgOrmDir(pCtx.getParam("ormDir"));
    pFct.getFctBlc().getFctDt().setStgDbCpDir(pCtx.getParam("dbcpDir"));
    pFct.getFctBlc().getFctDt().setLngCntr(pCtx.getParam("lngCntr"));
    pFct.getFctBlc().getFctDt().setNewDbId(Integer.parseInt(pCtx
      .getParam("newDbId")));
    pFct.getFctBlc().getFctDt().setDbgSh(Boolean.parseBoolean(pCtx
      .getParam("dbgSh")));
    pFct.getFctBlc().getFctDt().setDbgFl(Integer.parseInt(pCtx
      .getParam("dbgFl")));
    pFct.getFctBlc().getFctDt().setDbgCl(Integer.parseInt(pCtx
      .getParam("dbgCl")));
    pFct.getFctBlc().getFctDt().setWriteTi(Integer.valueOf(pCtx
      .getParam("writeTi")));
    pFct.getFctBlc().getFctDt().setReadTi(Integer.valueOf(pCtx
      .getParam("readTi")));
    pFct.getFctBlc().getFctDt().setWriteReTi(Integer.valueOf(pCtx
      .getParam("writeReTi")));
    pFct.getFctBlc().getFctDt().setWrReSpTr(Boolean.valueOf(pCtx
      .getParam("wrReSpTr")));
    pFct.getFctBlc().getFctDt().setLogSize(Integer.parseInt(pCtx
      .getParam("logSize")));
    pFct.getFctBlc().getFctDt().setAppPth(pCtx.getAppPth());
    pFct.getFctBlc().getFctDt()
      .setLogPth(pFct.getFctBlc().getFctDt().getAppPth());
    ISetng setng = pFct.getFctBlc().lazStgOrm(pRvs);
    String dbUrl = setng.lazCmnst().get(IOrm.DBURL);
    if (dbUrl.contains(IOrm.CURDIR)) { //sqlite
      dbUrl = dbUrl.replace(IOrm.CURDIR, pFct.getFctBlc().getFctDt().getAppPth()
        + File.separator);
    }
    pFct.getFctBlc().getFctDt().setDbUrl(dbUrl);
    String dbCls = setng.lazCmnst().get(IOrm.JDBCCLS);
    if (dbCls == null) {
      dbCls = setng.lazCmnst().get(IOrm.DSCLS);
    }
    pFct.getFctBlc().getFctDt().setDbCls(dbCls);
    pFct.getFctBlc().getFctDt().setDbUsr(setng.lazCmnst().get(IOrm.DBUSR));
    pFct.getFctBlc().getFctDt().setDbPwd(setng.lazCmnst().get(IOrm.DBPSW));
  }

  /**
   * <p>Getter for Admin non-shared Ents.</p>
   * @return List<Class<? extends IHasId<?>>>
   **/
  public final List<Class<? extends IHasId<?>>> getAdmEnts() {
    List<Class<? extends IHasId<?>>> admEnts =
      new ArrayList<Class<? extends IHasId<?>>>();
    admEnts.add(UsTmc.class);
    admEnts.add(UsRlTmc.class);
    admEnts.add(EmCon.class);
    admEnts.add(EmMsg.class);
    admEnts.add(EmAtch.class);
    admEnts.add(EmAdr.class);
    admEnts.add(EmInt.class);
    admEnts.add(EmStr.class);
    admEnts.add(EmRcp.class);
    return admEnts;
  }

  /**
   * <p>Makes UVD class settings.</p>
   * @param pRvs request scoped vars
   * @param pFct factory app
   * @throws Exception - an exception
   **/
  public final void makeUvdCls(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct) throws Exception {
    //UVD base entities restrictions:
    List<Class<? extends IHasId<?>>> admEnts = getAdmEnts();
    pFct.getFctBlc().getFctDt()
      .setAdmEnts(new ArrayList<Class<? extends IHasId<?>>>());
    pFct.getFctBlc().getFctDt().getAdmEnts().addAll(admEnts);
    pFct.getFctBlc().getFctDt()
      .setFbdEnts(new ArrayList<Class<? extends IHasId<?>>>());
    pFct.getFctBlc().getFctDt().getFbdEnts().addAll(admEnts);
    //Entities with custom ID:
    pFct.getFctBlc().getFctDt()
      .setCustIdClss(new HashSet<Class<? extends IHasId<?>>>());
    pFct.getFctBlc().getFctDt().getCustIdClss().add(UsTmc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(UsRlTmc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(UsPrf.class);
    //classes settings:
    pFct.getFctBlc().getFctDt().setHlClStgMp(new HashMap<String, HldClsStg>());
    String stgNm = "liHe"; //list header
    HldClsStg hlClSt = new HldClsStg(stgNm, stgNm);
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "flOr"; //list filter order
    hlClSt = new HldClsStg(stgNm, stgNm);
    hlClSt.setNulSclss(new HashSet<Class<?>>());
    hlClSt.getNulSclss().add(IOwned.class);
    hlClSt.setNulClss(new HashSet<Class<? extends IHasId<?>>>());
    hlClSt.getNulClss().add(EmCon.class);
    hlClSt.getNulClss().add(UsPrf.class);
    hlClSt.getNulClss().add(UsTmc.class);
    hlClSt.getNulClss().add(UsRlTmc.class);
    hlClSt.getNulClss().add(DcSp.class);
    hlClSt.getNulClss().add(DcGrSp.class);
    hlClSt.getNulClss().add(Cntr.class);
    hlClSt.getNulClss().add(Lng.class);
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "liFo"; //list footer
    hlClSt = new HldClsStg(stgNm, stgNm);
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(EmAtch.class, "oflf");
    hlClSt.getStgSclss().put(IOwned.class, "olf");
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "liAc"; //list item actions
    hlClSt = new HldClsStg(stgNm, stgNm);
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(IOwned.class, "acd");
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(EmMsg.class, "eml");
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "ordDf"; //list order by field default
    hlClSt = new HldClsStg(stgNm, stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(UsTmc.class, "usr");
    hlClSt.getStgClss().put(UsRlTmc.class, "usr");
    hlClSt.getStgClss().put(UsPrf.class, "cntr");
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(IHasNm.class, "nme");
    hlClSt.getStgSclss().put(IHasId.class, "iid");
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "orDeDf"; //list order by DESC default
    hlClSt = new HldClsStg(stgNm, stgNm);
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(IHasNm.class, "off");
    hlClSt.getStgSclss().put(IHasId.class, "on");
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "fmAc"; //form actions
    hlClSt = new HldClsStg(stgNm, stgNm);
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(IOwned.class, "aco");
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(EmMsg.class, "eml");
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "dlAc"; //form delete actions
    hlClSt = new HldClsStg(stgNm, stgNm);
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "pic"; //picker IHasNm default
    hlClSt = new HldClsStg(stgNm, stgNm);
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "piFo"; //picker footer
    hlClSt = new HldClsStg(stgNm, stgNm);
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
    stgNm = "prn"; //print
    hlClSt = new HldClsStg(stgNm, stgNm);
    pFct.getFctBlc().getFctDt().getHlClStgMp().put(stgNm, hlClSt);
  }

  /**
   * <p>Makes UVD fields settings.</p>
   * @param pRvs request scoped vars
   * @param pFct factory app
   * @throws Exception - an exception
   **/
  public final void makeUvdFds(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct) throws Exception {
    //fields settings:
    pFct.getFctBlc().getFctDt().setHlFdStgMp(new HashMap<String, HldFldStg>());
    String stgNm = "str"; //to string
    HldFldStg hlFdSt = new HldFldStg(stgNm, stgNm);
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setEnumVal("enm");
    hlFdSt.setSetng(pFct.getFctBlc().lazStgUvd(pRvs));
    hlFdSt.setCustClss(new HashSet<Class<?>>());
    hlFdSt.getCustClss().add(Date.class);
    hlFdSt.getCustClss().add(Long.class);
    hlFdSt.setStgClss(new HashMap<Class<?>, String>());
    hlFdSt.getStgClss().put(Boolean.class, "bln");
    hlFdSt.getStgClss().put(String.class, "smp");
    hlFdSt.getStgClss().put(UsTmc.class, "usTmc");
    hlFdSt.getStgClss().put(UsRlTmc.class, "usRlTmc");
    hlFdSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlFdSt.getStgSclss().put(IHasNm.class, "nme");
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "ceDe"; //to cell detail
    hlFdSt = new HldFldStg(stgNm, stgNm);
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("idOr", null);
    hlFdSt.getStgFdNm().put("dbOr", null);
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "ceHe"; //to cell header
    hlFdSt = new HldFldStg(stgNm, stgNm);
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("idOr", null);
    hlFdSt.getStgFdNm().put("dbOr", null);
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "flt"; //filter
    hlFdSt = new HldFldStg(stgNm, stgNm);
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setEnumVal("enm");
    hlFdSt.setSetng(pFct.getFctBlc().lazStgUvd(pRvs));
    hlFdSt.setCustClss(new HashSet<Class<?>>());
    hlFdSt.getCustClss().add(Date.class);
    hlFdSt.getCustClss().add(BigDecimal.class);
    hlFdSt.setStgClss(new HashMap<Class<?>, String>());
    hlFdSt.getStgClss().put(Boolean.class, "bln");
    hlFdSt.getStgClss().put(String.class, "str");
    hlFdSt.getStgClss().put(Long.class, "int");
    hlFdSt.getStgClss().put(Integer.class, "int");
    hlFdSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlFdSt.getStgSclss().put(IHasId.class, "ent");
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "ord"; //order
    hlFdSt = new HldFldStg(stgNm, stgNm);
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setEnumVal("ord");
    hlFdSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlFdSt.getStgSclss().put(IHasNm.class, "nme");
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "inWr"; //input wrapper
    hlFdSt = new HldFldStg(stgNm, stgNm);
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("idOr", null);
    hlFdSt.getStgFdNm().put("dbOr", null);
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "inp"; //input
    hlFdSt = new HldFldStg(stgNm, HldFldStg.NOSTD);
    hlFdSt.setEnumVal("enm");
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setSetng(pFct.getFctBlc().lazStgUvd(pRvs));
    hlFdSt.setCustClss(new HashSet<Class<?>>());
    //ID-able:
    hlFdSt.getCustClss().add(Long.class);
    hlFdSt.getCustClss().add(String.class);
    //custom:
    hlFdSt.getCustClss().add(Boolean.class);
    hlFdSt.getCustClss().add(Date.class);
    hlFdSt.getCustClss().add(BigDecimal.class);
    hlFdSt.setCustSclss(new HashSet<Class<?>>());
    //ID-able:
    hlFdSt.getCustSclss().add(IHasNm.class);
    //Standard:
    hlFdSt.setStgClss(new HashMap<Class<?>, String>());
    hlFdSt.getStgClss().put(Integer.class, "int"); //WARN avoid use as ID
    hlFdSt.getStgClss().put(Float.class, "max");
    hlFdSt.getStgClss().put(Double.class, "max");
    hlFdSt.getStgClss().put(UsTmc.class, "usr");
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
    stgNm = "wde"; //confirm delete input
    hlFdSt = new HldFldStg(stgNm, "dis");
    hlFdSt.setHldFdCls(pFct.getFctBlc().lazHldFldCls(pRvs));
    hlFdSt.setSetng(pFct.getFctBlc().lazStgUvd(pRvs));
    hlFdSt.setCustClss(new HashSet<Class<?>>());
    //ID-able:
    hlFdSt.getCustClss().add(Long.class);
    hlFdSt.getCustClss().add(String.class);
    hlFdSt.setCustSclss(new HashSet<Class<?>>());
    hlFdSt.getCustSclss().add(IHasNm.class);
    hlFdSt.setStgClss(new HashMap<Class<?>, String>());
    hlFdSt.getStgClss().put(UsTmc.class, "usr");
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("dbOr", null);
    pFct.getFctBlc().getFctDt().getHlFdStgMp().put(stgNm, hlFdSt);
  }
}
