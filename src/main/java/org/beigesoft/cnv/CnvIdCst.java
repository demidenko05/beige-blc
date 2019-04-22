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

import java.util.List;
import java.lang.reflect.Method;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.hld.IHldNm;
import org.beigesoft.prp.ISetng;

/**
 * <p>Generic converter of entity of custom ID (composite, ID is foreign entity
 * or custom ID name).</p>
 *
 * @author Yury Demidenko
 */
public class CnvIdCst implements ICnvId<IHasId<Object>, Object> {

  /**
   * <p>Settings service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Fields classes holder.</p>
   **/
  private IHldNm<Class<?>, Class<?>> hldFdCls;

  /**
   * <p>Fields getters RAPI holder.</p>
   **/
  private IHldNm<Class<?>, Method> hldGets;

  /**
   * <p>Converts to HTML ready ID, e.g. "IID=PAYB" for Account with String ID,
   * or "usr=User1&rol=Role1" for User-Role with composite ID.</p>
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  @Override
  public final String idHtml(final IHasId<Object> pEnt) throws Exception {
    StringBuffer sb = new StringBuffer();
    boolean isFst = true;
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      Object fdVl = null;
      Class<?> fdCls = this.hldFdCls.get(pEnt.getClass(), fdNm);
      Method getter = this.hldGets.get(pEnt.getClass(), fdNm);
      fdVl = getter.invoke(pEnt);
      if (fdVl == null) {
        throw new ExcCode(ExcCode.WRPR, "Entity with NULL ID!");
      }
      if (IHasId.class.isAssignableFrom(fdCls)) {
        List<String> fdIdNms = this.setng.lazIdFldNms(fdCls);
        if (fdIdNms.size() > 1) {
          throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID!"
            + " cls/fd" + pEnt.getClass() + "/" + fdNm);
        }
        Class<?> fcs = this.hldFdCls.get(fdCls, fdIdNms.get(0));
        if (IHasId.class.isAssignableFrom(fcs)) {
          throw new ExcCode(ExcCode.WRCN, "Subentity with double foreign ID!"
            + " cls/fd/fcl/f" + pEnt.getClass() + "/" + fdNm + "/"
              + fdCls + "/" + fdIdNms.get(0));
        }
        getter = this.hldGets.get(fdCls, fdIdNms.get(0));
        fdVl = getter.invoke(fdVl);
      }
      if (isFst) {
        isFst = false;
      } else {
        sb.append("&");
      }
      sb.append(pEnt.getClass().getSimpleName() + "." + fdNm + "=" + fdVl);
    }
    return sb.toString();
  }

  /**
   * <p>Converts to SQL ready ID, e.g. "'PAYB'" for Account with String ID,
   * or "'User1','Role1'" for User-Role with composite ID.</p>
   * @param pEnt entity
   * @return to value
   * @throws Exception - an exception
   **/
  @Override
  public final String idSql(final IHasId<Object> pEnt) throws Exception {
    StringBuffer sb = new StringBuffer();
    boolean isFst = true;
    for (String fdNm : this.setng.lazIdFldNms(pEnt.getClass())) {
      Object fdVl = null;
      Class<?> fdCls = this.hldFdCls.get(pEnt.getClass(), fdNm);
      Method getter = this.hldGets.get(pEnt.getClass(), fdNm);
      fdVl = getter.invoke(pEnt);
      if (fdVl == null) {
        throw new ExcCode(ExcCode.WRPR, "Entity with NULL ID!");
      }
      if (IHasId.class.isAssignableFrom(fdCls)) {
        List<String> fdIdNms = this.setng.lazIdFldNms(fdCls);
        if (fdIdNms.size() > 1) {
          throw new ExcCode(ExcCode.WRCN, "Subentity with composite ID!"
            + " cls/fd" + pEnt.getClass() + "/" + fdNm);
        }
        Class<?> fcs = this.hldFdCls.get(fdCls, fdIdNms.get(0));
        if (IHasId.class.isAssignableFrom(fcs)) {
          throw new ExcCode(ExcCode.WRCN, "Subentity with double foreign ID!"
            + " cls/fd/fcl/f" + pEnt.getClass() + "/" + fdNm + "/"
              + fdCls + "/" + fdIdNms.get(0));
        }
        getter = this.hldGets.get(fdCls, fdIdNms.get(0));
        fdVl = getter.invoke(fdVl);
      }
      if (isFst) {
        isFst = false;
      } else {
        sb.append(",");
      }
      String val;
      if (fdVl instanceof String) {
        val = "'" + fdVl.toString() + "'";
      } else {
        val = fdVl.toString();
      }
      sb.append(val);
    }
    return sb.toString();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
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

  /**
   * <p>Getter for hldGets.</p>
   * @return IHldNm<Class<?>, Method>
   **/
  public final IHldNm<Class<?>, Method> getHldGets() {
    return this.hldGets;
  }

  /**
   * <p>Setter for hldGets.</p>
   * @param pHldGets reference
   **/
  public final void setHldGets(final IHldNm<Class<?>, Method> pHldGets) {
    this.hldGets = pHldGets;
  }
}
