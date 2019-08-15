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

package org.beigesoft.prc;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdlp.IOrId;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Service that saves entity with file into DB. If file is already uploaded,
 * then it acts like standard PrcEntSv.
 * There are two kind of entity with file:
 * 1. an attachment (e.g. email)
 * 2. image/file on HTML page (e.g. WEB-Store's item specifics "Image")
 * WEB-form must POST parameters:
 * <ul>
 * <li><b>Inserts/updates with file/uploaded path:</b></li>
 * <li>parFile - parameter file to upload
 *   or NULL for already  uploaded path.</li>
 * <li>fdFlNm - entity's field name that holds file name,
 *  e.g. attachment name, maybe NULL</li>
 * <li>fdFlPth - entity's field name that holds file path,
 *  it's NULL for already uploaded URL</li>
 * <li>fdFlUrl - entity's field name that holds file URL,
 *  it's used to make file URL in HTML page from uploading file path,
 *  maybe NULL (e.g. for mail attachment, or uploaded URL).</li>
 * </ul></p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class PrcEnfSv<T extends IHasId<ID>, ID> implements IPrcEnt<T, ID> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHlNmClMt hldSets;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHlNmClMt hldGets;

  /**
   * <p>Upload directory relative to WEB-APP path
   * without start and end separator, e.g. "static/uploads".</p>
   **/
  private String uplDir;

  /**
   * <p>Full WEB-APP path without end separator,
   * revealed from servlet context and used for upload files.</p>
   **/
  private String appPth;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(final Map<String, Object> pRvs, final T pEnt,
    final IReqDt pRqDt) throws Exception {
    if (!pEnt.getIsNew() && IOrId.class.isAssignableFrom(pEnt.getClass())) {
      IOrId oid = (IOrId) pEnt;
      if (!oid.getDbOr().equals(this.orm.getDbId())) {
        throw new ExcCode(ExcCode.WR, "can_not_change_foreign_src");
      }
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    String fileUplNm = (String) pRqDt.getAttr("fileUplNm");
    String fdFlPth = pRqDt.getParam("fdFlPth");
    String fdFlNm = pRqDt.getParam("fdFlNm");
    if (fdFlNm != null) {
      Method sfn = this.hldSets.get(pEnt.getClass(), fdFlNm);
      if (fileUplNm != null) {
        sfn.invoke(pEnt, fileUplNm);
      } else { // either uploaded path or null
        Method getFpn = this.hldGets.get(pEnt.getClass(), fdFlPth);
        String pth = (String) getFpn.invoke(pEnt);
        if (pth == null || "".equals(pth)) {
          sfn.invoke(pEnt, null);
        } else {
          //uploaded URL separator is always "/"
          int idxs = pth.lastIndexOf("/");
          if (idxs > 1) {
            sfn.invoke(pEnt, pth.substring(idxs + 1));
          } else {
            sfn.invoke(pEnt, pth);
          }
        }
      }
    }
    if (fileUplNm != null) {
      OutputStream outs = null;
      InputStream ins = null;
      try {
        //fill file and filePath field:
        String ft = String.valueOf(new Date().getTime());
        String filePath = this.appPth + File.separator
          + this.uplDir + File.separator + ft + fileUplNm;
        ins = (InputStream) pRqDt.getAttr("fileUplIs");
        outs = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] data = new byte[1024];
        int count;
        while ((count = ins.read(data)) != -1) {
          outs.write(data, 0, count);
        }
        outs.flush();
        Method sfp = this.hldSets.get(pEnt.getClass(), fdFlPth);
        sfp.invoke(pEnt, filePath);
        String fdFlUrl = pRqDt.getParam("fdFlUrl");
        if (fdFlUrl != null) {
          Method sfu = this.hldSets.get(pEnt.getClass(), fdFlUrl);
          sfu.invoke(pEnt, this.uplDir + "/" + ft + fileUplNm);
        }
      } finally {
        if (ins != null) {
          ins.close();
        }
        if (outs != null) {
          outs.close();
        }
      }
    } // else NULL or uploaded
    if (pEnt.getIsNew()) {
      this.orm.insert(pRvs, vs, pEnt);
      pRvs.put("msgSuc", "insert_ok");
    } else {
      this.orm.update(pRvs, vs, pEnt);
      pRvs.put("msgSuc", "update_ok");
    }
    return pEnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for hldSets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldSets() {
    return this.hldSets;
  }

  /**
   * <p>Setter for hldSets.</p>
   * @param pHldSets reference
   **/
  public final void setHldSets(final IHlNmClMt pHldSets) {
    this.hldSets = pHldSets;
  }

  /**
   * <p>Getter for hldGets.</p>
   * @return IHlNmClMt
   **/
  public final IHlNmClMt getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHlNmClMt pHldGets) {
    this.hldGets = pHldGets;
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
}
