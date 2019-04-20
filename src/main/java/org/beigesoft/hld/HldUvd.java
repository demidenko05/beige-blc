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

package org.beigesoft.hld;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdl.Page;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that transforms and holds part of settings from ISetng UVD.
 * It's also assembly and it consists of all UVD holders and request scoped
 * variables that are used in JSP.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class HldUvd<T extends IHasId<?>> {

  //parts:
  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Holders class string settings.</p>
   **/
  private final Map<String, HldClsStg> hlClStgMp =
    new HashMap<String, HldClsStg>();

  //derived/transformed settings:
  /**
   * <p>Entities fields inlist map.</p>
   **/
  private final Map<Class<T>, String[]> lstFdsMp =
    new HashMap<Class<T>, String[]>();

  /**
   * <p>Owned entities map.</p>
   **/
  private final Map<Class<T>, List<Class<IOwned<T, ?>>>> owdEnts =
    new HashMap<Class<T>, List<Class<IOwned<T, ?>>>>();

  //request scoped vars for JSP:
    //list/form:
  /**
   * <p>Requested entity class.</p>
   **/
  private Class<T> cls;

    //list:
  /**
   * <p>Requested entity fields.</p>
   **/
  private List<T> ents;

  /**
   * <p>Requested entity fields.</p>
   **/
  private String[] lstFds;

  /**
   * <p>Pages.</p>
   **/
  private List<Page> pgs;

  /**
   * <p>Gets class string setting for current class.</p>
   * @param pStgNm setting name
   * @return string setting
   * @throws Exception - an exception
   **/
  public final String stg(final String pStgNm) throws Exception {
    return this.hlClStgMp.get(pStgNm).get(this.cls);
  }

  /**
   * <p>Gets class fields in list in lazy mode.</p>
   * @param pCls Entity class
   * @return fields list
   * @throws Exception - an exception
   **/
  public final String[] lazLstFds(
    final Class<T> pCls) throws Exception {
    if (!this.owdEnts.keySet().contains(pCls)) {
      synchronized (this) {
        if (!this.lstFdsMp.keySet().contains(pCls)) {
          String lFdSt = null;
          synchronized (this.setng) {
            lFdSt = this.setng.lazClsStg(pCls, "lstFds");
            if (lFdSt != null) {
              this.setng.getClsStgs().get(pCls).remove("lstFds");
            }
          }
          if (lFdSt != null) {
            List<String> lFdLst = new ArrayList<String>();
            for (String fn : lFdSt.split(",")) {
              lFdLst.add(fn);
            }
            String[] rz = new String[lFdLst.size()];
            this.lstFdsMp.put(pCls, lFdLst.toArray(rz));
          } else {
            this.lstFdsMp.put(pCls, null);
          }
        }
      }
    }
    return this.lstFdsMp.get(pCls);
  }

  /**
   * <p>Gets owned list in lazy mode.</p>
   * @param pCls Entity class
   * @return owned list
   * @throws Exception - an exception
   **/
  public final List<Class<IOwned<T, ?>>> lazOwnd(
    final Class<T> pCls) throws Exception {
    if (!this.owdEnts.keySet().contains(pCls)) {
      synchronized (this) {
        if (!this.owdEnts.keySet().contains(pCls)) {
          String owdes = null;
          synchronized (this.setng) {
            owdes = this.setng.lazClsStg(pCls, "owdEnts");
            if (owdes != null) {
              this.setng.getClsStgs().get(pCls).remove("owdEnts");
            }
          }
          if (owdes != null) {
            List<Class<IOwned<T, ?>>> oeLst =
              new ArrayList<Class<IOwned<T, ?>>>();
            for (String oec : owdes.split(",")) {
              @SuppressWarnings("unchecked")
              Class<IOwned<T, ?>> cl = (Class<IOwned<T, ?>>) Class.forName(oec);
              oeLst.add(cl);
            }
            this.owdEnts.put(pCls, oeLst);
          } else {
            this.owdEnts.put(pCls, null);
          }
        }
      }
    }
    return this.owdEnts.get(pCls);
  }

  //Synchronized SGS:
  /**
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final synchronized ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final synchronized void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
  }

  /**
   * <p>Getter for cls.</p>
   * @return Class<T>
   **/
  public final Class<T> getCls() {
    return this.cls;
  }

  /**
   * <p>Setter for cls.</p>
   * @param pCls reference
   **/
  public final void setCls(final Class<T> pCls) {
    this.cls = pCls;
  }

  /**
   * <p>Getter for ents.</p>
   * @return List<T>
   **/
  public final List<T> getEnts() {
    return this.ents;
  }

  /**
   * <p>Setter for ents.</p>
   * @param pEnts reference
   **/
  public final void setEnts(final List<T> pEnts) {
    this.ents = pEnts;
  }

  /**
   * <p>Getter for lstFds.</p>
   * @return String[]
   **/
  public final String[] getLstFds() {
    return this.lstFds;
  }

  /**
   * <p>Setter for lstFds.</p>
   * @param pLstFds reference
   **/
  public final void setLstFds(final String[] pLstFds) {
    this.lstFds = pLstFds;
  }

  /**
   * <p>Getter for pgs.</p>
   * @return List<Page>
   **/
  public final List<Page> getPgs() {
    return this.pgs;
  }

  /**
   * <p>Setter for pgs.</p>
   * @param pPgs reference
   **/
  public final void setPgs(final List<Page> pPgs) {
    this.pgs = pPgs;
  }

  /**
   * <p>Getter for hlClStgMp.</p>
   * @return Map<String, HldClsStg>
   **/
  public final Map<String, HldClsStg> getHlClStgMp() {
    return this.hlClStgMp;
  }
}
