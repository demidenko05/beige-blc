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

package org.beigesoft.prp;

import java.util.LinkedHashSet;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.lang.reflect.Field;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.srv.IReflect;

/**
 * <p>Service that loads classes and their fields settings
 * from XML property file.
 * Priority of XML classes properties:
 * <pre>
 * 1. clsCs - class to settings
 * 5. clsTyCs - class type to settings
 * </pre>
 * Priority of XML fields properties:
 * <pre>
 * 1. clsFs - class to settings
 * 2. fldNmClsTyFs - field name class type to settings
 * 3. fldNmTyFs - field name and type to settings
 * 4. fldNmFs - field name to settings
 * 5. fldTyFs - field type to settings
 * </pre>
 * It is also holder of involved classes involved fields names.
 * </p>
 *
 * @author Yury Demidenko
 */
public class Setng implements ISetng {

  //Services:
  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Reflection service.</p>
   **/
  private IReflect reflect;

  /**
   * <p>LnkPrps service.</p>
   **/
  private UtlPrp utlPrp;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  //Configuration:
    //Base settings:
  /**
   * <p>XML properties directory.</p>
   **/
  private String dir;

    //Target data:
  /**
   * <p>Common settings.</p>
   */
  private Map<String, String> cmnStgs;

  /**
   * <p>Classes settings.</p>
   */
  private Map<Class<?>, Map<String, String>> clsStgs;

  /**
   * <p>Fields settings.</p>
   */
  private Map<Class<?>, Map<String, Map<String, String>>> fldStgs;

  /**
   * <p>All involved classes.</p>
   **/
  private List<Class<?>> clss;

  /**
   * <p>Fields names involved excluding IDs.</p>
   */
  private Map<Class<?>, List<String>> fldNms;

  /**
   * <p>ID fields names.</p>
   */
  private Map<Class<?>, List<String>> idFldNms;

    //Loaded sources properties from conf.xml:
  /**
   * <p>Class settings names.</p>
   */
  private LinkedHashSet<String> clsStgNms;

  /**
   * <p>Fields settings names.</p>
   */
  private LinkedHashSet<String> fldStgNms;

  /**
   * <p>Excluded fields names.</p>
   */
  private LinkedHashSet<String> exlFlds;

    //Cached XML properties. Empty (non-null) values means lazy initialized
    //from NULL:
  /**
   * <p>Setting name - Class type to CS properties map.</p>
   */
  private Map<String, Map<Class<?>, String>> clsTyCs;

  /**
   * <p>Class - setting name to classes props.
   * It may contains NULL properties - lazy loaded empty.
   * It also emptied when any setting is revealed from it.</p>
   */
  private Map<Class<?>, Map<String, String>> clsCs;

  /**
   * <p>Class - Field name + setting name to fields props.
   * It may contains NULL properties - lazy loaded empty.
   * It also emptied when any setting is revealed from it.</p>
   */
  private Map<Class<?>, Map<String, String>> clsFs;

  /**
   * <p>Setting name - Field type to FS properties.</p>
   */
  private Map<String, Map<Class<?>, String>> fldTyFs;

  /**
   * <p>Setting name - Field name to FS properties.</p>
   */
  private Map<String, Map<String, String>> fldNmFs;

  /**
   * <p>Field name + Setting name - Field type to FS properties.</p>
   */
  private Map<String, Map<Class<?>, String>> fldNmTyFs;

  /**
   * <p>Field name + Setting name - class type to FS properties.</p>
   */
  private Map<String, Map<Class<?>, String>> fldNmClTyFs;

  /**
   * <p>Lazy gets common settings.</p>
   * @return common settings if exists or empty map
   * @throws Exception - an exception
   **/
  @Override
  public final Map<String, String> lazCmnst() throws Exception {
    if (this.cmnStgs == null) {
      synchronized (this) {
        if (this.cmnStgs == null) {
          String fiNm = "/" + this.dir + "/" + CMNSTFLNM;
          this.cmnStgs = ldPrps(fiNm);
          if (this.cmnStgs == null) {
            this.log.info(null, getClass(),
              "There is no common settings in dir " + this.dir);
            this.cmnStgs = new HashMap<String, String>();
          }
        }
      }
    }
    return this.cmnStgs;
  }

  /**
   * <p>Lazy gets field's setting for given class, field and name.
   * Maybe NULL.</p>
   * @param pCls class
   * @param pFldNm field name
   * @param pStgNm setting name
   * @return String setting, maybe NULL
   * @throws Exception - an exception
   **/
  @Override
  public final String lazFldStg(final Class<?> pCls, final String pFldNm,
    final String pStgNm) throws Exception {
    if (pCls == null || pFldNm == null || pStgNm == null) {
      throw new ExcCode(ExcCode.WRPR, "Null parameter cls/fd/stg: " + pCls
        + "/" + pFldNm + "/" + pStgNm);
    }
    lazClsStg(pCls, KEYEXLFLDS);
    if (this.exlFlds != null && this.exlFlds.contains(pFldNm)) {
      throw new ExcCode(ExcCode.WRPR, "Excluded field " + pFldNm);
    }
    lazFldPrp(pCls, pFldNm, pStgNm);
    if (this.fldStgs == null || this.fldStgs.get(pCls) == null
      || !this.fldStgs.get(pCls).keySet().contains(pFldNm)
        || !this.fldStgs.get(pCls).get(pFldNm).keySet().contains(pStgNm)) {
      synchronized (this) {
        if (this.fldStgs == null || this.fldStgs.get(pCls) == null
          || !this.fldStgs.get(pCls).keySet().contains(pFldNm)
            || !this.fldStgs.get(pCls).get(pFldNm).keySet().contains(pStgNm)) {
          String stg = revFldStg(pCls, pFldNm, pStgNm);
          if (this.fldStgs != null && this.fldStgs.get(pCls) != null
            && this.fldStgs.get(pCls).get(pFldNm) != null) {
            this.fldStgs.get(pCls).get(pFldNm).put(pStgNm, stg);
          } else {
            Map<String, String> flSts = new HashMap<String, String>();
            flSts.put(pStgNm, stg);
            if (this.fldStgs != null && this.fldStgs.get(pCls) != null) {
              this.fldStgs.get(pCls).put(pFldNm, flSts);
            } else {
              Map<String, Map<String, String>> flsSts =
                new HashMap<String, Map<String, String>>();
              flsSts.put(pFldNm, flSts);
              if (this.fldStgs != null) {
                this.fldStgs.put(pCls, flsSts);
              } else {
                Map<Class<?>, Map<String, Map<String, String>>> tfldStgs =
                  new HashMap<Class<?>, Map<String, Map<String, String>>>();
                tfldStgs.put(pCls, flsSts);
                this.fldStgs = tfldStgs;
              }
            }
          }
        }
      }
    }
    return this.fldStgs.get(pCls).get(pFldNm).get(pStgNm);
  }

  /**
   * <p>Lazy gets fields names for given class excluding ID, collections and
   * excluded fields by XML file.</p>
   * @param pCls class
   * @return fields names set
   * @throws Exception - an exception
   **/
  @Override
  public final List<String> lazFldNms(final Class<?> pCls) throws Exception {
    if (pCls == null) {
      throw new ExcCode(ExcCode.WRPR, "Null parameter cls!");
    }
    if (this.fldNms == null || this.fldNms.get(pCls) == null) {
      synchronized (this) {
        if (this.fldNms == null || this.fldNms.get(pCls) == null) {
          lazIdFldNms(pCls);
          String exlFdStr = lazClsStg(pCls, KEYEXLFLDS);
          List<String> fNms = new ArrayList<String>();
          for (Field fld : this.reflect.retFlds(pCls)) {
            if (!(Collection.class.isAssignableFrom(fld.getType())
              || this.exlFlds != null && this.exlFlds.contains(fld.getName())
                || exlFdStr != null && exlFdStr.contains(fld.getName())
                  || this.idFldNms != null && this.idFldNms.get(pCls) != null
                    && this.idFldNms.get(pCls).contains(fld.getName()))) {
              fNms.add(fld.getName());
            }
          }
          if (exlFdStr != null) {
            this.clsStgs.get(pCls).remove(KEYEXLFLDS);
            boolean isDbgSh = this.log.getDbgSh(this.getClass())
              && this.log.getDbgFl() < 6010 && this.log.getDbgCl() > 6008;
            if (isDbgSh) {
          this.log.debug(null, Setng.class, "clsStgs deleted stg cls/stg/val: "
                + pCls + "/" + KEYEXLFLDS + "/" + exlFdStr);
            }
          }
          if (this.fldNms == null) {
            this.fldNms = new HashMap<Class<?>, List<String>>();
          }
          this.fldNms.put(pCls, fNms);
        }
      }
    }
    return this.fldNms.get(pCls);
  }

  /**
   * <p>Lazy gets all involved classes.</p>
   * @return classes list
   * @throws Exception - an exception
   **/
  @Override
  public final List<Class<?>> lazClss() throws Exception {
    lazConf();
    return this.clss;
  }

  /**
   * <p>Lazy gets field's setting for given class and name. Maybe NULL.</p>
   * @param pCls class
   * @param pStgNm setting name
   * @return String setting, maybe NULL
   * @throws Exception - an exception
   **/
  @Override
  public final String lazClsStg(final Class<?> pCls,
    final String pStgNm) throws Exception {
    if (pCls == null || pStgNm == null) {
      throw new ExcCode(ExcCode.WRPR, "Null parameter cls/stg: " + pCls
        + "/" + pStgNm);
    }
    lazConf();
    if (!this.clss.contains(pCls)) {
      throw new ExcCode(ExcCode.WRPR, "Excluded class " + pCls);
    }
    lazClsPrp(pCls, pStgNm);
    if (this.clsStgs == null || !this.clsStgs.keySet().contains(pCls)
      || !this.clsStgs.get(pCls).keySet().contains(pStgNm)) {
      synchronized (this) {
        if (this.clsStgs == null || !this.clsStgs.keySet().contains(pCls)
          || !this.clsStgs.get(pCls).keySet().contains(pStgNm)) {
          String stg = revClsStg(pCls, pStgNm);
          if (this.clsStgs != null && this.clsStgs.get(pCls) != null) {
            this.clsStgs.get(pCls).put(pStgNm, stg);
          } else {
            Map<String, String> clSts = new HashMap<String, String>();
            clSts.put(pStgNm, stg);
            if (this.clsStgs == null) {
              this.clsStgs = new HashMap<Class<?>, Map<String, String>>();
            }
            this.clsStgs.put(pCls, clSts);
          }
        }
      }
    }
    return this.clsStgs.get(pCls).get(pStgNm);
  }

  /**
   * <p>Lazy gets ID fields names for given class.</p>
   * @param pCls class
   * @return fields names set
   * @throws Exception - an exception
   **/
  @Override
  public final List<String> lazIdFldNms(final Class<?> pCls) throws Exception {
    lazConf();
    if (!this.clss.contains(pCls)) {
      throw new ExcCode(ExcCode.WRPR, "Excluded class " + pCls);
    }
    if (this.idFldNms == null || !this.idFldNms.keySet().contains(pCls)) {
      synchronized (this) {
        if (this.idFldNms == null || !this.idFldNms.keySet().contains(pCls)) {
          String idFldsStr = lazClsStg(pCls, KEYIDFLDS);
          List<String> idNms = null;
          if (idFldsStr != null) {
            this.clsStgs.get(pCls).put(KEYIDFLDS, null);
            boolean isDbgSh = this.log.getDbgSh(this.getClass())
              && this.log.getDbgFl() < 6009 && this.log.getDbgCl() > 6007;
            if (isDbgSh) {
            this.log.debug(null, Setng.class, "clsStgs nulled stg cls/stg/val: "
                + pCls + "/" + KEYIDFLDS + "/" + idFldsStr);
            }
            idNms = new ArrayList<String>();
            for (String idNm : idFldsStr.split(",")) {
              idNms.add(idNm);
            }
          }
          if (this.idFldNms == null) {
            this.idFldNms = new HashMap<Class<?>, List<String>>();
          }
          this.idFldNms.put(pCls, idNms);
        }
      }
    }
    return this.idFldNms.get(pCls);
  }

  /**
   * <p>Clear all loaded data, releases memory.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void release() throws Exception {
    this.cmnStgs = null;
    this.clsStgs = null;
    this.fldStgs = null;
    this.clss = null;
    this.fldNms = null;
    this.idFldNms = null;
    this.clsStgNms = null;
    this.fldStgNms = null;
    this.exlFlds = null;
    this.clsCs = null;
    this.clsTyCs = null;
    this.clsFs = null;
    this.fldTyFs = null;
    this.fldNmFs = null;
    this.fldNmTyFs = null;
    this.fldNmClTyFs = null;
  }

  /**
   * <p>Getter for cmnStgs.</p>
   * @return Map<String, String>
   **/
  @Override
  public final synchronized Map<String, String> getCmnStgs() {
    return this.cmnStgs;
  }

  /**
   * <p>Getter for clsStgs.</p>
   * @return Map<Class<?>, Map<String, String>>
   **/
  @Override
  public final synchronized Map<Class<?>, Map<String, String>> getClsStgs() {
    return this.clsStgs;
  }

  /**
   * <p>Getter for fldStgs.</p>
   * @return Map<Class<?>, Map<String, Map<String, String>>>
   **/
  @Override
  public final synchronized
    Map<Class<?>, Map<String, Map<String, String>>> getFldStgs() {
    return this.fldStgs;
  }

  //Utils:
  /**
   * <p>Reveal field setting for given class, field and setting name.</p>
   * @param pCls class
   * @param pFldNm field name
   * @param pStgNm setting name
   * @return string or NULL
   * @throws Exception - an exception
   **/
  public final synchronized String revFldStg(final Class<?> pCls,
    final String pFldNm, final String pStgNm) throws Exception {
    String kyFdSt = pFldNm + pStgNm;
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6005 && this.log.getDbgCl() > 6003;
    if (this.clsFs != null && this.clsFs.get(pCls) != null
      && this.clsFs.get(pCls).keySet().contains(kyFdSt)) {
      String rz = this.clsFs.get(pCls).get(kyFdSt);
      this.clsFs.get(pCls).remove(kyFdSt);
      if (isDbgSh) {
        this.log.debug(null, Setng.class, "clsFs deleted entry for cls/fd/stg: "
          + pCls + "/" + pFldNm + "/" + pStgNm);
      }
      if (this.clsFs.get(pCls).size() == 0) {
        this.clsFs.put(pCls, null);
        if (isDbgSh) {
          this.log.debug(null, Setng.class, "clsFs nulled for cls: " + pCls);
        }
      }
      return rz;
    }
    if (this.fldNmClTyFs != null && this.fldNmClTyFs.get(kyFdSt) != null) {
      String trz = revStgByTy(this.fldNmClTyFs.get(kyFdSt), pCls);
      if (!"".equals(trz)) {
        return trz;
      }
    }
    if (this.fldNmTyFs != null && this.fldNmTyFs.get(kyFdSt) != null) {
      Class<?> fdCls = this.hldFdCls.get(pCls, pFldNm);
      String trz = revStgByTy(this.fldNmTyFs.get(kyFdSt), fdCls);
      if (!"".equals(trz)) {
        return trz;
      }
    }
    if (this.fldNmFs != null && this.fldNmFs.get(pStgNm) != null
      && this.fldNmFs.get(pStgNm).keySet().contains(pFldNm)) {
      return this.fldNmFs.get(pStgNm).get(pFldNm);
    }
    if (this.fldTyFs != null && this.fldTyFs.get(pStgNm) != null) {
      Class<?> fdCls = this.hldFdCls.get(pCls, pFldNm);
      String trz = revStgByTy(this.fldTyFs.get(pStgNm), fdCls);
      if (!"".equals(trz)) {
        return trz;
      }
    }
    if (isDbgSh) {
      this.log.debug(null, Setng.class, "Setting not found for cls/fd/stg: "
        + pCls + "/" + pFldNm + "/" + pStgNm);
    }
    return null;
  }

  /**
   * <p>Reveal class setting for given class and setting name.</p>
   * @param pCls class
   * @param pStgNm setting name
   * @return string or NULL
   * @throws Exception - an exception
   **/
  public final synchronized String revClsStg(final Class<?> pCls,
    final String pStgNm) throws Exception {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6008 && this.log.getDbgCl() > 6006;
    if (this.clsCs != null && this.clsCs.get(pCls) != null
      && this.clsCs.get(pCls).keySet().contains(pStgNm)) {
      String rz = this.clsCs.get(pCls).get(pStgNm);
      this.clsCs.get(pCls).remove(pStgNm);
      if (isDbgSh) {
        this.log.debug(null, Setng.class, "clsCs deleted entry for cls/stg: "
          + pCls + "/" + pStgNm);
      }
      if (this.clsCs.get(pCls).size() == 0) {
        this.clsCs.put(pCls, null);
        if (isDbgSh) {
          this.log.debug(null, Setng.class, "clsCs nulled for cls: " + pCls);
        }
      }
      return rz;
    }
    if (this.clsTyCs != null && this.clsTyCs.get(pStgNm) != null) {
      String trz = revStgByTy(this.clsTyCs.get(pStgNm), pCls);
      if (!"".equals(trz)) {
        return trz;
      }
    }
    if (isDbgSh) {
      this.log.debug(null, Setng.class, "Setting not found for cls/stg: "
        + pCls + "/" + pStgNm);
    }
    return null;
  }

  /**
   * <p>Reveal setting from type map for given type.</p>
   * @param pTy class or field type
   * @param pStgs map
   * @return string value including NULL, empty string "" if not found
   * @throws Exception - an exception
   **/
  public final synchronized String revStgByTy(
    final Map<Class<?>, String> pStgs, final Class<?> pTy) {
    boolean isDbgSh = this.log.getDbgSh(this.getClass())
      && this.log.getDbgFl() < 6004 && this.log.getDbgCl() > 6002;
    if (pStgs.keySet().contains(pTy)) {
      if (isDbgSh) {
        this.log.debug(null, Setng.class, "found exact type/value: "
          + pTy + "/" + pStgs.get(pTy));
      }
      return pStgs.get(pTy);
    }
    for (Entry<Class<?>, String> enr : pStgs.entrySet()) {
      if (enr.getKey().isAssignableFrom(pTy)) {
        if (isDbgSh) {
          this.log.debug(null, Setng.class, "found sub-type/type/value: "
            + pTy + "/" + enr.getKey() + "/" + enr.getValue());
        }
        return enr.getValue();
      }
    }
    return "";
  }

  /**
   * <p>Lazy load XML fields properties for given class,
   * field and setting name.</p>
   * @param pCls class
   * @param pFldNm field name
   * @param pStgNm setting name
   * @throws Exception - an exception
   **/
  public final void lazFldPrp(final Class<?> pCls, final String pFldNm,
    final String pStgNm) throws Exception {
    String fdStNm = pFldNm + pStgNm;
    if (this.clsFs == null || !this.clsFs.keySet().contains(pCls)
      || !this.fldTyFs.keySet().contains(pStgNm)
        || !this.fldNmFs.keySet().contains(pStgNm)
          || !this.fldNmTyFs.keySet().contains(fdStNm)
            || !this.fldNmClTyFs.keySet().contains(fdStNm)) {
      synchronized (this) {
        if (this.clsFs == null || !this.clsFs.keySet().contains(pCls)
          || !this.fldTyFs.keySet().contains(pStgNm)
            || !this.fldNmFs.keySet().contains(pStgNm)
              || !this.fldNmTyFs.keySet().contains(fdStNm)
                || !this.fldNmClTyFs.keySet().contains(fdStNm)) {
          boolean isDbgSh = this.log.getDbgSh(this.getClass())
            && this.log.getDbgFl() < 6006 && this.log.getDbgCl() > 6004;
          if (isDbgSh) {
           this.log.debug(null, Setng.class, "Try get XML FDPR for cls/fd/stg: "
              + pCls + "/" + pFldNm + "/" + pStgNm);
          }
          String fiPa;
          if (this.clsFs == null || !this.clsFs.keySet().contains(pCls)) {
            fiPa = "/" + this.dir + "/" + DIRCLSFS + "/"
              + pCls.getSimpleName() + ".xml";
            Map<String, String> clFsPr = ldPrps(fiPa);
            if (this.clsFs == null) {
              this.clsFs = new HashMap<Class<?>, Map<String, String>>();
            }
            this.clsFs.put(pCls, clFsPr);
          }
          if (this.fldTyFs == null || !this.fldTyFs.keySet().contains(pStgNm)) {
            fiPa = "/" + this.dir + "/" + DIRFLDTYFS + "/" + pStgNm
              + ".xml";
            Map<Class<?>, String> flTyFsMp = ldClPrps(pStgNm, fiPa);
            if (this.fldTyFs == null) {
              this.fldTyFs = new HashMap<String, Map<Class<?>, String>>();
            }
            this.fldTyFs.put(pStgNm, flTyFsMp);
          }
          if (this.fldNmFs == null || !this.fldNmFs.keySet().contains(pStgNm)) {
            fiPa = "/" + this.dir + "/" + DIRFLDNMFS + "/" + pStgNm + ".xml";
            Map<String, String> flNmFsPr = ldPrps(fiPa);
            if (this.fldNmFs == null) {
              this.fldNmFs = new HashMap<String, Map<String, String>>();
            }
            this.fldNmFs.put(pStgNm, flNmFsPr);
          }
          if (this.fldNmTyFs == null
            || !this.fldNmTyFs.keySet().contains(fdStNm)) {
            fiPa = "/" + this.dir + "/" + DIRFLDNMTYFS + "/" + fdStNm + ".xml";
            Map<Class<?>, String> flNmTyFsMp = ldClPrps(fdStNm, fiPa);
            if (this.fldNmTyFs == null) {
              this.fldNmTyFs = new HashMap<String, Map<Class<?>, String>>();
            }
            this.fldNmTyFs.put(fdStNm, flNmTyFsMp);
          }
          if (this.fldNmClTyFs == null
            || !this.fldNmClTyFs.keySet().contains(fdStNm)) {
            fiPa = "/" + this.dir + "/" + DIRFLDNMCLSTYFS + "/"
              + fdStNm + ".xml";
            Map<Class<?>, String> flNmClTyFsMp = ldClPrps(fdStNm, fiPa);
            if (this.fldNmClTyFs == null) {
              this.fldNmClTyFs = new HashMap<String, Map<Class<?>, String>>();
            }
            this.fldNmClTyFs.put(fdStNm, flNmClTyFsMp);
          }
        }
      }
    }
  }

  /**
   * <p>Lazy load XML class properties for given class and setting name.</p>
   * @param pCls class
   * @param pStgNm setting name
   * @throws Exception - an exception
   **/
  public final void lazClsPrp(final Class<?> pCls,
    final String pStgNm) throws Exception {
    if (this.clsCs == null || !this.clsCs.keySet().contains(pCls)
      || !this.clsTyCs.keySet().contains(pStgNm)) {
      synchronized (this) {
        if (this.clsCs == null || !this.clsCs.keySet().contains(pCls)
          || !this.clsTyCs.keySet().contains(pStgNm)) {
          boolean isDbgSh = this.log.getDbgSh(this.getClass())
            && this.log.getDbgFl() < 6007 && this.log.getDbgCl() > 6005;
          if (isDbgSh) {
            this.log.debug(null, Setng.class, "Try get XML CLPR for cls/stg: "
              + pCls + "/" + pStgNm);
          }
          String fiPa;
          if (this.clsCs == null || !this.clsCs.keySet().contains(pCls)) {
            fiPa = "/" + this.dir + "/" + DIRCLSCS + "/"
              + pCls.getSimpleName() + ".xml";
            Map<String, String> clFsPr = ldPrps(fiPa);
            if (this.clsCs == null) {
              this.clsCs = new HashMap<Class<?>, Map<String, String>>();
            }
            this.clsCs.put(pCls, clFsPr);
          }
          if (this.clsTyCs == null || !this.clsTyCs.keySet().contains(pStgNm)) {
            fiPa = "/" + this.dir + "/" + DIRCLSTYCS + "/" + pStgNm
              + ".xml";
            Map<Class<?>, String> flTyFsMp = ldClPrps(pStgNm, fiPa);
            if (this.clsTyCs == null) {
              this.clsTyCs = new HashMap<String, Map<Class<?>, String>>();
            }
            this.clsTyCs.put(pStgNm, flTyFsMp);
          }
        }
      }
    }
  }

  /**
   * <p>Lazy loads configuration.</p>
   * @throws Exception - an exception
   **/
  public final void lazConf() throws Exception {
    if (this.clss == null) {
      synchronized (this) {
        if (this.clss == null) {
          String cnfFiNm = "/" + this.dir + "/" + CONFFLNM;
          this.log.info(null, Setng.class, "try to load: " + cnfFiNm);
          LnkPrps conf = this.utlPrp.load(cnfFiNm);
          if (conf == null) {
            throw new ExcCode(ExcCode.WRCN, "There is no configuration file "
              + cnfFiNm);
          }
          String strClss = conf.getProperty(KEYCLSS);
          if (strClss == null) {
            throw new ExcCode(ExcCode.WRCN, "There is no property " + KEYCLSS
              + " in configuration file!");
          }
          this.log.info(null, Setng.class, "classes: " + strClss);
          LinkedHashSet<String> clsNms = this.utlPrp.evPrpStrSet(strClss);
          List<Class<?>> tclss = new ArrayList<Class<?>>();
          for (String clsNm : clsNms) {
            tclss.add(Class.forName(clsNm));
          }
          String strClsStgNms = conf.getProperty(KEYCLSSTNMS);
          LinkedHashSet<String> tclsStgNms = null;
          if (strClsStgNms == null) {
            this.log.warn(null, Setng.class, "There is no classes settings!");
          } else {
          this.log.info(null, Setng.class, "classes settings: " + strClsStgNms);
            LinkedHashSet<String> clsStNmPrs = this.utlPrp
              .evPrpStrSet(strClsStgNms);
            tclsStgNms = new LinkedHashSet<String>();
            for (String clsStNmPr : clsStNmPrs) {
              tclsStgNms.add(clsStNmPr);
            }
          }
          String strFldStgNms = conf.getProperty(KEYFLDSTNMS);
          LinkedHashSet<String> tfldStgNms = null;
          if (strFldStgNms == null) {
            this.log.warn(null, Setng.class, "There is no fields settings!");
          } else {
           this.log.info(null, Setng.class, "fields settings: " + strFldStgNms);
            LinkedHashSet<String> fldStNmPrs = this.utlPrp
              .evPrpStrSet(strFldStgNms);
            tfldStgNms = new LinkedHashSet<String>();
            for (String fldStNmPr : fldStNmPrs) {
              tfldStgNms.add(fldStNmPr);
            }
          }
          String strExlFlds = conf.getProperty(KEYEXLFLDS);
          LinkedHashSet<String> texlFlds = null;
          if (strExlFlds == null) {
            this.log.warn(null, Setng.class, "There is no excluded fields!");
          } else {
            this.log.info(null, Setng.class, "excluded fields: " + strExlFlds);
            LinkedHashSet<String> fldStNmPrs = this.utlPrp
              .evPrpStrSet(strExlFlds);
            texlFlds = new LinkedHashSet<String>();
            for (String fldStNmPr : fldStNmPrs) {
              texlFlds.add(fldStNmPr);
            }
          }
          //assign fully initialized beans:
          this.clsStgNms = tclsStgNms;
          this.fldStgNms = tfldStgNms;
          this.exlFlds = texlFlds;
          this.clss = tclss;
        }
      }
    }
  }

  /**
   * <p>Load properties String-String from XML file.</p>
   * @param pFiNm File Name
   * @return Map<String, String> properties
   * @throws Exception - an exception
   **/
  public final synchronized Map<String, String> ldPrps(
    final String pFiNm) throws Exception {
    Map<String, String> rz = null;
    LnkPrps prp = this.utlPrp.load(pFiNm);
    if (prp != null) {
      rz = new LinkedHashMap<String, String>();
      boolean isDbgSh = this.log.getDbgSh(this.getClass())
        && this.log.getDbgFl() < 6002 && this.log.getDbgCl() > 6000;
      if (isDbgSh) {
        this.log.debug(null, Setng.class, "added setting BN file: " + pFiNm);
      }
      for (String ky : prp.getOrdKeys()) {
        String valOr = prp.getProperty(ky);
        String val = this.utlPrp.evPrpVl(prp, ky);
        rz.put(ky, val);
        if (isDbgSh) {
          this.log.debug(null, Setng.class, "added stg/valOr/val: " + ky
            + "/" + valOr + "/" + val);
        }
      }
    }
    return rz;
  }

  /**
   * <p>Load properties Class-String from XML file.</p>
   * @param pKey setting file name - setting name or
   *  field name + setting name or class name
   * @param pFiNm File Name
   * @return class-property map
   * @throws Exception - an exception
   **/
  public final synchronized Map<Class<?>, String> ldClPrps(
    final String pKey, final String pFiNm) throws Exception {
    Map<Class<?>, String> rz = new LinkedHashMap<Class<?>, String>();
    LnkPrps lprp = this.utlPrp.load(pFiNm);
    if (lprp != null) {
      boolean isDbgSh = this.log.getDbgSh(this.getClass())
        && this.log.getDbgFl() < 6003 && this.log.getDbgCl() > 6001;
      if (isDbgSh) {
        this.log.debug(null, Setng.class, "added setting BT file: " + pFiNm);
      }
      for (String ky : lprp.getOrdKeys()) {
        String valOr = lprp.getProperty(ky);
        String val = this.utlPrp.evPrpVl(lprp, ky);
        rz.put(Class.forName(ky), val);
        if (isDbgSh) {
          this.log.debug(null, Setng.class, "added stg/valOr/val: " + ky
            + "/" + valOr + "/" + val);
        }
      }
    }
    return rz;
  }

  //Simple getters and setters:
    //configuration:
  /**
   * <p>Getter for dir.</p>
   * @return String
   **/
  public final synchronized String getDir() {
    return this.dir;
  }

  /**
   * <p>Setter for dir.</p>
   * @param pDir reference
   **/
  public final synchronized void setDir(final String pDir) {
    this.dir = pDir;
  }

    //parts:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final synchronized ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final synchronized void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for reflect.</p>
   * @return IReflect
   **/
  public final synchronized IReflect getReflect() {
    return this.reflect;
  }

  /**
   * <p>Setter for reflect.</p>
   * @param pReflect reference
   **/
  public final synchronized void setReflect(final IReflect pReflect) {
    this.reflect = pReflect;
  }

  /**
   * <p>Getter for utlPrp.</p>
   * @return UtlPrp
   **/
  public final synchronized UtlPrp getUtlPrp() {
    return this.utlPrp;
  }

  /**
   * <p>Setter for utlPrp.</p>
   * @param pUtlPrp reference
   **/
  public final synchronized void setUtlPrp(final UtlPrp pUtlPrp) {
    this.utlPrp = pUtlPrp;
  }

  /**
   * <p>Getter for hldFdCls.</p>
   * @return IHldNm<Class<?>, Class<?>>
   **/
  public final IHldNm<Class<?>, Class<?>> getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHldNm<Class<?>, Class<?>> pHldFdCls) {
    this.hldFdCls = pHldFdCls;
  }

  //For debugging purposes:
  /**
   * <p>Getter for fldNms.</p>
   * @return Map<Class<?>, List<String>>
   **/
  public final synchronized Map<Class<?>, List<String>> getFldNms() {
    return this.fldNms;
  }

  /**
   * <p>Getter for idFldNms.</p>
   * @return Map<Class<?>, List<String>>
   **/
  public final synchronized Map<Class<?>, List<String>> getIdFldNms() {
    return this.idFldNms;
  }

  /**
   * <p>Getter for clss.</p>
   * @return LinkedHashSet<Class<?>>
   **/
  public final synchronized List<Class<?>> getClss() {
    return this.clss;
  }

  /**
   * <p>Getter for clsStgNms.</p>
   * @return LinkedHashSet<String>
   **/
  public final synchronized LinkedHashSet<String> getClsStgNms() {
    return this.clsStgNms;
  }

  /**
   * <p>Getter for fldStgNms.</p>
   * @return LinkedHashSet<String>
   **/
  public final synchronized LinkedHashSet<String> getFldStgNms() {
    return this.fldStgNms;
  }

  /**
   * <p>Getter for exlFlds.</p>
   * @return LinkedHashSet<String>
   **/
  public final synchronized LinkedHashSet<String> getExlFlds() {
    return this.exlFlds;
  }

  /**
   * <p>Getter for clsTyCs.</p>
   * @return Map<String, Map<Class<?>, String>>
   **/
  public final synchronized Map<String, Map<Class<?>, String>> getClsTyCs() {
    return this.clsTyCs;
  }

  /**
   * <p>Getter for clsFs.</p>
   * @return Map<Class<?>, Map<String, String>>
   **/
  public final synchronized Map<Class<?>, Map<String, String>> getClsFs() {
    return this.clsFs;
  }

  /**
   * <p>Getter for clsCs.</p>
   * @return Map<Class<?>, Map<String, String>>
   **/
  public final synchronized Map<Class<?>, Map<String, String>> getClsCs() {
    return this.clsCs;
  }

  /**
   * <p>Getter for fldTyFs.</p>
   * @return Map<String, Map<Class<?>, String>>
   **/
  public final synchronized Map<String, Map<Class<?>, String>> getFldTyFs() {
    return this.fldTyFs;
  }

  /**
   * <p>Getter for fldNmFs.</p>
   * @return Map<String, Map<String, String>>
   **/
  public final synchronized Map<String, Map<String, String>> getFldNmFs() {
    return this.fldNmFs;
  }

  /**
   * <p>Getter for fldNmTyFs.</p>
   * @return Map<String, Map<Class<?>, String>>
   **/
  public final synchronized Map<String, Map<Class<?>, String>> getFldNmTyFs() {
    return this.fldNmTyFs;
  }

  /**
   * <p>Getter for fldNmClTyFs.</p>
   * @return Map<String, Map<Class<?>, String>>
   **/
  public final synchronized
    Map<String, Map<Class<?>, String>> getFldNmClTyFs() {
    return this.fldNmClTyFs;
  }
}
