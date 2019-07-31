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

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.hld.HldFldStg;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.hld.HldEnts;

/**
 * <p>Holder of base and additional data for main factory.</p>
 *
 * @author Yury Demidenko
 **/
public class FctDt {

  /**
   * <p>UVD holder field fillers names name.</p>
   **/
  public static final String HLFILFDNMUVD = "hlFiFdNmUvd";

  /**
   * <p>UVD Setting service name.</p>
   **/
  public static final String STGUVDNM = "stgUvd";

  /**
   * <p>DB-Copy Setting service name.</p>
   **/
  public static final String STGDBCPNM = "stgDbCp";

  /**
   * <p>DB-Copy holder converters names to string name.</p>
   **/
  public static final String HLCNTOSTDBCP = "hlCnToStDbCp";

  /**
   * <p>DB-Copy holder filler fields names name.</p>
   **/
  public static final String HLFILFDNMDBCP = "hlFiFdNmDbCp";

  /**
   * <p>ORM Setting service name.</p>
   **/
  public static final String STGORMNM = "stgOrm";

  /**
   * <p>Standard logger name.</p>
   **/
  public static final String LOGSTDNM = "logStd";

  /**
   * <p>Secure logger name.</p>
   **/
  public static final String LOGSECNM = "logSec";

  /**
   * <p>Handler base, secure non-transaction requests name.</p>
   **/
  public static final String HNNTRQSC = "hnNtRqSc";

  /**
   * <p>Handler admin, secure non-transaction requests name.</p>
   **/
  public static final String HNNTRQAD = "hnNtRqAd";

  /**
   * <p>Handler admin/web-store entities request name.</p>
   **/
  public static final String HNADENRQ = "hnAdEnRq";

  /**
   * <p>Handler base entities (no admin, etc) request name.</p>
   **/
  public static final String HNACENRQ = "hnAcEnRq";

  /**
   * <p>Processor admin/web-store entities page name.</p>
   **/
  public static final String PRADENTPG = "prAdEnPg";

  /**
   * <p>Processor base entities (no admin, etc) page name.</p>
   **/
  public static final String PRACENTPG = "prAcEnPg";

  //configuration data:
  /**
   * <p>Database full copy setting base dir.</p>
   **/
  private String stgDbCpDir;

  /**
   * <p>UVD setting base dir.</p>
   **/
  private String stgUvdDir;

  /**
   * <p>ORM setting base dir.</p>
   **/
  private String stgOrmDir;

  /**
   * <p>Languages, countries.</p>
   **/
  private String lngCntr = "en,US";

  /**
   * <p>DB data-source or JDBC class.</p>
   **/
  private String dbCls;

  /**
   * <p>DB URL or database name for Android.</p>
   **/
  private String dbUrl;

  /**
   * <p>DB user.</p>
   **/
  private String dbUsr;

  /**
   * <p>DB password.</p>
   **/
  private String dbPwd;

  /**
   * <p>Android configuration, RDB insert returns autogenerated ID,
   * updating with expression like "VER=VER+1" is not possible.</p>
   **/
  private boolean isAndr = false;

  /**
   * <p>If PostgreSQL. After importing it requires
   * refreshing auto-ID sequences.</p>
   **/
  private boolean isPstg = false;

  /**
   * <p>New database ID.</p>
   **/
  private int newDbId = 1;

  /**
   * <p>Is show debug messages main flag, false default.</p>
   **/
  private boolean dbgSh = false;

  /**
   * <p>Get preferred detail level floor, 0 default.</p>
   **/
  private int dbgFl = 0;

  /**
   * <p>Get preferred detail level ceiling, 99999999 default.</p>
   **/
  private int dbgCl = 99999999;

  /**
   * <p>Transaction isolation for changing DB phase.</p>
   **/
  private Integer writeTi = IRdb.TRRUC;

  /**
   * <p>Transaction isolation for reading DB phase.</p>
   **/
  private Integer readTi = IRdb.TRRUC;

  /**
   * <p>Transaction isolation for writing and reading DB phase.</p>
   **/
  private Integer writeReTi = IRdb.TRRUC;

  /**
   * <p>Writing and reading phases in separated transactions.</p>
   **/
  private Boolean wrReSpTr = Boolean.FALSE;

  /**
   * <p>Log files path.</p>
   **/
  private String logPth;

  /**
   * <p>Log files size in bytes.</p>
   **/
  private int logSize = 1048576;

  /**
   * <p>Standard log file name.</p>
   **/
  private String logStdNm = LOGSTDNM;

  /**
   * <p>WEB-APP files path.</p>
   **/
  private String appPth;

  /**
   * <p>Upload directory relative to WEB-APP path
   * without start and end separator, e.g. "static/uploads".</p>
   **/
  private String uplDir;

  /**
   * <p>Entities that can be printed via PDF, etc.</p>
   **/
  private List<Class<? extends IHasId<?>>> flRpEnts;

  /**
   * <p>Holders class string settings.</p>
   **/
  private Map<String, HldClsStg> hlClStgMp;

  /**
   * <p>Holders filed string settings.</p>
   **/
  private Map<String, HldFldStg> hlFdStgMp;

  /**
   * <p>Set of classes with custom ID (composite, ID is foreign entity or custom
   * ID name).</p>
   **/
  private Set<Class<? extends IHasId<?>>> custIdClss;

  /**
   * <p>Set of classes for matching to foreign.</p>
   **/
  private Set<Class<? extends IHasId<?>>> maFrClss;

  /**
   * <p>Other work-spaces entities holders.</p>
   **/
  private Set<HldEnts> hldsEnts;

  /**
   * <p>Outside base processors factories.</p>
   **/
  private Set<IFctPrc> fctsPrc;

  /**
   * <p>Outside admin processors factories.</p>
   **/
  private Set<IFctPrc> fctsPrcAd;

  /**
   * <p>Additional entities processors factories.</p>
   **/
  private Set<IFctPrcEnt> fctsPrcEnt;

  /**
   * <p>Additional file report processor factories.</p>
   **/
  private Set<IFctPrcFl> fctrsPrcFl;

  /**
   * <p>Additional base entity processors names holders
   *  with high priority.</p>
   **/
  private Set<IHlNmClSt> hldsBsEnPr;

  /**
   * <p>Additional admin entity processors names holders
   *  with high priority.</p>
   **/
  private Set<IHlNmClSt> hldsAdEnPr;

  /**
   * <p>Outside factories of converters to string.</p>
   **/
  private Set<IFctCnToSt> fcsCnToSt;

  /**
   * <p>Outside factories of fillers fields from string.</p>
   **/
  private Set<IFcFlFdSt> fcsFlFdSt;

  /**
   * <p>Evaluates if entity allowed for given workspace.</p>
   * @param pEnt class
   * @param pIid reference
   * @return if allowed
   **/
  public final boolean isEntAlwd(final Class<? extends IHasId<?>> pEnt,
    final Integer pIid) {
    if (pIid == null) {
      throw new RuntimeException("Null workspace ID");
    }
    if (pIid.equals(HldEnts.ID_BASE)) {
      if (this.hldsEnts == null) {
        return true; //only workspace
      } else {
        for (HldEnts he :this.hldsEnts) {
          if (!he.getIid().equals(pIid) && he.getEnts().contains(pEnt)) {
            Set<Class<? extends IHasId<?>>> ents = he.evShrEnts(pIid);
            if (ents != null && ents.contains(pEnt)) {
              return true;
            }
            return false;
          }
        }
        return true;
      }
    } else {
      if (this.hldsEnts == null) {
        throw new RuntimeException("Workspace has no HldEnts, ID " + pIid);
      } else {
        boolean hasHe = false;
        for (HldEnts he :this.hldsEnts) {
          if (he.getIid().equals(pIid)) {
            if (he.getEnts().contains(pEnt)) {
              return true;
            }
            hasHe = true;
          }
          if (!he.getIid().equals(pIid)) {
            Set<Class<? extends IHasId<?>>> ents = he.evShrEnts(pIid);
            if (ents != null && ents.contains(pEnt)) {
              return true;
            }
          }
        }
        if (!hasHe) {
          throw new RuntimeException("Workspace has no HldEnts, ID " + pIid);
        }
      }
    }
    return false;
  }

  /**
   * <p>Evaluates shared entities for given workspace.</p>
   * @param pIid reference
   * @return Set<Class<? extends IHasId<?>>> or null
   **/
  public final Set<Class<? extends IHasId<?>>> evShrEnts(final Integer pIid) {
    if (pIid == null) {
      throw new RuntimeException("Null workspace ID");
    }
    Set<Class<? extends IHasId<?>>> rz = null;
    if (this.hldsEnts != null) {
      for (HldEnts he :this.hldsEnts) {
        if (!he.getIid().equals(pIid)) {
          Set<Class<? extends IHasId<?>>> ents = he.evShrEnts(pIid);
          if (ents != null) {
            if (rz == null) {
              rz = new HashSet<Class<? extends IHasId<?>>>();
            }
            rz.addAll(ents);
          }
        }
      }
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for stgDbCpDir.</p>
   * @return String
   **/
  public final String getStgDbCpDir() {
    return this.stgDbCpDir;
  }

  /**
   * <p>Setter for stgDbCpDir.</p>
   * @param pStgDbCpDir reference
   **/
  public final void setStgDbCpDir(final String pStgDbCpDir) {
    this.stgDbCpDir = pStgDbCpDir;
  }

  /**
   * <p>Getter for stgOrmDir.</p>
   * @return String
   **/
  public final String getStgOrmDir() {
    return this.stgOrmDir;
  }

  /**
   * <p>Setter for stgOrmDir.</p>
   * @param pStgOrmDir reference
   **/
  public final void setStgOrmDir(final String pStgOrmDir) {
    this.stgOrmDir = pStgOrmDir;
  }

  /**
   * <p>Getter for stgUvdDir.</p>
   * @return String
   **/
  public final String getStgUvdDir() {
    return this.stgUvdDir;
  }

  /**
   * <p>Setter for stgUvdDir.</p>
   * @param pStgUvdDir reference
   **/
  public final void setStgUvdDir(final String pStgUvdDir) {
    this.stgUvdDir = pStgUvdDir;
  }

  /**
   * <p>Getter for lngCntr.</p>
   * @return String
   **/
  public final String getLngCntr() {
    return this.lngCntr;
  }

  /**
   * <p>Setter for lngCntr.</p>
   * @param pLngCntr reference
   **/
  public final void setLngCntr(final String pLngCntr) {
    this.lngCntr = pLngCntr;
  }

  /**
   * <p>Getter for dbCls.</p>
   * @return String
   **/
  public final String getDbCls() {
    return this.dbCls;
  }

  /**
   * <p>Setter for dbCls.</p>
   * @param pDbCls reference
   **/
  public final void setDbCls(final String pDbCls) {
    this.dbCls = pDbCls;
  }

  /**
   * <p>Getter for dbUrl.</p>
   * @return String
   **/
  public final String getDbUrl() {
    return this.dbUrl;
  }

  /**
   * <p>Setter for dbUrl.</p>
   * @param pDbUrl reference
   **/
  public final void setDbUrl(final String pDbUrl) {
    this.dbUrl = pDbUrl;
  }

  /**
   * <p>Getter for dbUsr.</p>
   * @return String
   **/
  public final String getDbUsr() {
    return this.dbUsr;
  }

  /**
   * <p>Setter for dbUsr.</p>
   * @param pDbUsr reference
   **/
  public final void setDbUsr(final String pDbUsr) {
    this.dbUsr = pDbUsr;
  }

  /**
   * <p>Getter for dbPwd.</p>
   * @return String
   **/
  public final String getDbPwd() {
    return this.dbPwd;
  }

  /**
   * <p>Setter for dbPwd.</p>
   * @param pDbPwd reference
   **/
  public final void setDbPwd(final String pDbPwd) {
    this.dbPwd = pDbPwd;
  }

  /**
   * <p>Getter for isAndr.</p>
   * @return boolean
   **/
  public final boolean getIsAndr() {
    return this.isAndr;
  }

  /**
   * <p>Setter for isAndr.</p>
   * @param pIsAndr reference
   **/
  public final void setIsAndr(final boolean pIsAndr) {
    this.isAndr = pIsAndr;
  }

  /**
   * <p>Getter for newDbId.</p>
   * @return int
   **/
  public final int getNewDbId() {
    return this.newDbId;
  }

  /**
   * <p>Setter for newDbId.</p>
   * @param pNewDbId reference
   **/
  public final void setNewDbId(final int pNewDbId) {
    this.newDbId = pNewDbId;
  }

  /**
   * <p>Getter for dbgSh.</p>
   * @return boolean
   **/
  public final boolean getDbgSh() {
    return this.dbgSh;
  }

  /**
   * <p>Setter for dbgSh.</p>
   * @param pDbgSh reference
   **/
  public final void setDbgSh(final boolean pDbgSh) {
    this.dbgSh = pDbgSh;
  }

  /**
   * <p>Getter for dbgFl.</p>
   * @return int
   **/
  public final int getDbgFl() {
    return this.dbgFl;
  }

  /**
   * <p>Setter for dbgFl.</p>
   * @param pDbgFl reference
   **/
  public final void setDbgFl(final int pDbgFl) {
    this.dbgFl = pDbgFl;
  }

  /**
   * <p>Getter for dbgCl.</p>
   * @return int
   **/
  public final int getDbgCl() {
    return this.dbgCl;
  }

  /**
   * <p>Setter for dbgCl.</p>
   * @param pDbgCl reference
   **/
  public final void setDbgCl(final int pDbgCl) {
    this.dbgCl = pDbgCl;
  }

  /**
   * <p>Getter for writeTi.</p>
   * @return Integer
   **/
  public final Integer getWriteTi() {
    return this.writeTi;
  }

  /**
   * <p>Setter for writeTi.</p>
   * @param pWriteTi reference
   **/
  public final void setWriteTi(final Integer pWriteTi) {
    this.writeTi = pWriteTi;
  }

  /**
   * <p>Getter for readTi.</p>
   * @return Integer
   **/
  public final Integer getReadTi() {
    return this.readTi;
  }

  /**
   * <p>Setter for readTi.</p>
   * @param pReadTi reference
   **/
  public final void setReadTi(final Integer pReadTi) {
    this.readTi = pReadTi;
  }

  /**
   * <p>Getter for writeReTi.</p>
   * @return Integer
   **/
  public final Integer getWriteReTi() {
    return this.writeReTi;
  }

  /**
   * <p>Setter for writeReTi.</p>
   * @param pWriteReTi reference
   **/
  public final void setWriteReTi(final Integer pWriteReTi) {
    this.writeReTi = pWriteReTi;
  }

  /**
   * <p>Getter for wrReSpTr.</p>
   * @return Boolean
   **/
  public final Boolean getWrReSpTr() {
    return this.wrReSpTr;
  }

  /**
   * <p>Setter for wrReSpTr.</p>
   * @param pWrReSpTr reference
   **/
  public final void setWrReSpTr(final Boolean pWrReSpTr) {
    this.wrReSpTr = pWrReSpTr;
  }

  /**
   * <p>Getter for logPth.</p>
   * @return String
   **/
  public final String getLogPth() {
    return this.logPth;
  }

  /**
   * <p>Setter for logPth.</p>
   * @param pLogPth reference
   **/
  public final void setLogPth(final String pLogPth) {
    this.logPth = pLogPth;
  }

  /**
   * <p>Getter for logSize.</p>
   * @return size
   **/
  public final int getLogSize() {
    return this.logSize;
  }

  /**
   * <p>Setter for logSize.</p>
   * @param pLogSize value
   **/
  public final void setLogSize(final int pLogSize) {
    this.logSize = pLogSize;
  }

  /**
   * <p>Getter for logStdNm.</p>
   * @return String
   **/
  public final String getLogStdNm() {
    return this.logStdNm;
  }

  /**
   * <p>Setter for logStdNm.</p>
   * @param pLogStdNm reference
   **/
  public final void setLogStdNm(final String pLogStdNm) {
    this.logStdNm = pLogStdNm;
  }

  /**
   * <p>Getter for appPth.</p>
   * @return String
   **/
  public final String getAppPth() {
    return this.appPth;
  }

  /**
   * <p>Setter for appPth.</p>
   * @param pAppPth reference
   **/
  public final void setAppPth(final String pAppPth) {
    this.appPth = pAppPth;
  }

  /**
   * <p>Getter for flRpEnts.</p>
   * @return List<? extends Class<? extends IHasId<?>>>
   **/
  public final List<Class<? extends IHasId<?>>> getFlRpEnts() {
    return this.flRpEnts;
  }

  /**
   * <p>Setter for flRpEnts.</p>
   * @param pFlRpEnts reference
   **/
  public final void setFlRpEnts(
    final List<Class<? extends IHasId<?>>> pFlRpEnts) {
    this.flRpEnts = pFlRpEnts;
  }

  /**
   * <p>Getter for hlClStgMp.</p>
   * @return Map<String, HldClsStg>
   **/
  public final Map<String, HldClsStg> getHlClStgMp() {
    return this.hlClStgMp;
  }

  /**
   * <p>Setter for hlClStgMp.</p>
   * @param pHlClStgMp reference
   **/
  public final void setHlClStgMp(final Map<String, HldClsStg> pHlClStgMp) {
    this.hlClStgMp = pHlClStgMp;
  }

  /**
   * <p>Getter for hlFdStgMp.</p>
   * @return Map<String, HldFldStg>
   **/
  public final Map<String, HldFldStg> getHlFdStgMp() {
    return this.hlFdStgMp;
  }

  /**
   * <p>Setter for hlFdStgMp.</p>
   * @param pHlFdStgMp reference
   **/
  public final void setHlFdStgMp(final Map<String, HldFldStg> pHlFdStgMp) {
    this.hlFdStgMp = pHlFdStgMp;
  }

  /**
   * <p>Getter for maFrClss.</p>
   * @return Set<Class<? extends IHasId<?>>>
   **/
  public final Set<Class<? extends IHasId<?>>> getMaFrClss() {
    return this.maFrClss;
  }

  /**
   * <p>Setter for maFrClss.</p>
   * @param pMaFrClss reference
   **/
  public final void setMaFrClss(
    final Set<Class<? extends IHasId<?>>> pMaFrClss) {
    this.maFrClss = pMaFrClss;
  }

  /**
   * <p>Getter for custIdClss.</p>
   * @return Set<Class<? extends IHasId<?>>>
   **/
  public final Set<Class<? extends IHasId<?>>> getCustIdClss() {
    return this.custIdClss;
  }

  /**
   * <p>Setter for custIdClss.</p>
   * @param pCustIdClss reference
   **/
  public final void setCustIdClss(
    final Set<Class<? extends IHasId<?>>> pCustIdClss) {
    this.custIdClss = pCustIdClss;
  }

  /**
   * <p>Getter for uplDir.</p>
   * @return String
   **/
  public final String getUplDir() {
    return this.uplDir;
  }

  /**
   * <p>Setter for uplDir.</p>
   * @param pUplDir reference
   **/
  public final void setUplDir(final String pUplDir) {
    this.uplDir = pUplDir;
  }

  /**
   * <p>Getter for fctsPrcAd.</p>
   * @return Set<IFctPrc>
   **/
  public final Set<IFctPrc> getFctsPrcAd() {
    return this.fctsPrcAd;
  }

  /**
   * <p>Setter for fctsPrcAd.</p>
   * @param pFctsPrcAd reference
   **/
  public final void setFctsPrcAd(final Set<IFctPrc> pFctsPrcAd) {
    this.fctsPrcAd = pFctsPrcAd;
  }

  /**
   * <p>Getter for fctsPrc.</p>
   * @return Set<IFctPrc>
   **/
  public final Set<IFctPrc> getFctsPrc() {
    return this.fctsPrc;
  }

  /**
   * <p>Setter for fctsPrc.</p>
   * @param pFctsPrc reference
   **/
  public final void setFctsPrc(final Set<IFctPrc> pFctsPrc) {
    this.fctsPrc = pFctsPrc;
  }

  /**
   * <p>Getter for fctsPrcEnt.</p>
   * @return Set<IFctPrcEnt>
   **/
  public final Set<IFctPrcEnt> getFctsPrcEnt() {
    return this.fctsPrcEnt;
  }

  /**
   * <p>Setter for fctsPrcEnt.</p>
   * @param pFctsPrcEnt reference
   **/
  public final void setFctsPrcEnt(final Set<IFctPrcEnt> pFctsPrcEnt) {
    this.fctsPrcEnt = pFctsPrcEnt;
  }

  /**
   * <p>Getter for hldsBsEnPr.</p>
   * @return Set<IHlNmClSt>
   **/
  public final Set<IHlNmClSt> getHldsBsEnPr() {
    return this.hldsBsEnPr;
  }

  /**
   * <p>Setter for hldsBsEnPr.</p>
   * @param pHldsBsEnPr reference
   **/
  public final void setHldsBsEnPr(final Set<IHlNmClSt> pHldsBsEnPr) {
    this.hldsBsEnPr = pHldsBsEnPr;
  }

  /**
   * <p>Getter for hldsAdEnPr.</p>
   * @return Set<IHlNmClSt>
   **/
  public final Set<IHlNmClSt> getHldsAdEnPr() {
    return this.hldsAdEnPr;
  }

  /**
   * <p>Setter for hldsAdEnPr.</p>
   * @param pHldsAdEnPr reference
   **/
  public final void setHldsAdEnPr(final Set<IHlNmClSt> pHldsAdEnPr) {
    this.hldsAdEnPr = pHldsAdEnPr;
  }

  /**
   * <p>Getter for fctrsPrcFl.</p>
   * @return Set<IFctPrcFl>
   **/
  public final Set<IFctPrcFl> getFctrsPrcFl() {
    return this.fctrsPrcFl;
  }

  /**
   * <p>Setter for fctrsPrcFl.</p>
   * @param pFctrsPrcFl reference
   **/
  public final void setFctrsPrcFl(final Set<IFctPrcFl> pFctrsPrcFl) {
    this.fctrsPrcFl = pFctrsPrcFl;
  }


  /**
   * <p>Getter for fcsCnToSt.</p>
   * @return Set<IFctCnToSt>
   **/
  public final Set<IFctCnToSt> getFcsCnToSt() {
    return this.fcsCnToSt;
  }

  /**
   * <p>Setter for fcsCnToSt.</p>
   * @param pFcsCnToSt reference
   **/
  public final void setFcsCnToSt(final Set<IFctCnToSt> pFcsCnToSt) {
    this.fcsCnToSt = pFcsCnToSt;
  }

  /**
   * <p>Getter for fcsFlFdSt.</p>
   * @return Set<IFcFlFdSt>
   **/
  public final Set<IFcFlFdSt> getFcsFlFdSt() {
    return this.fcsFlFdSt;
  }

  /**
   * <p>Setter for fcsFlFdSt.</p>
   * @param pFcsFlFdSt reference
   **/
  public final void setFcsFlFdSt(final Set<IFcFlFdSt> pFcsFlFdSt) {
    this.fcsFlFdSt = pFcsFlFdSt;
  }

  /**
   * <p>Getter for hldsEnts.</p>
   * @return Set<HldEnts>
   **/
  public final Set<HldEnts> getHldsEnts() {
    return this.hldsEnts;
  }

  /**
   * <p>Setter for hldsEnts.</p>
   * @param pHldsEnts reference
   **/
  public final void setHldsEnts(final Set<HldEnts> pHldsEnts) {
    this.hldsEnts = pHldsEnts;
  }

  /**
   * <p>Getter for isPstg.</p>
   * @return boolean
   **/
  public final boolean getIsPstg() {
    return this.isPstg;
  }

  /**
   * <p>Setter for isPstg.</p>
   * @param pIsPstg reference
   **/
  public final void setIsPstg(final boolean pIsPstg) {
    this.isPstg = pIsPstg;
  }
}
