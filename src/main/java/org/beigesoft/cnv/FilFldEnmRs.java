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
import java.util.Map;
import java.lang.reflect.Method;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlNmClMt;
import org.beigesoft.hld.IHlNmClCl;

/**
 * <p>Standard service that fills/converts object's field of type Enum from
 * given result-set.</p>
 *
 * @param <E> Enum type
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FilFldEnmRs<E extends Enum<E>, RS>
  implements IFilFldRs<RS> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Holder of an entity's field's class.</p>
   **/
  private IHlNmClCl hldFdCls;

  /**
   * <p>Fields setters RAPI holder.</p>
   **/
  private IHlNmClMt hldSets;

  /**
   * <p>Fills/converts object's field of type Enum from
   * given result-set.</p>
   * @param <T> object (entity) type
   * @param pRvs request scoped vars, not null
   * @param pVs invoker scoped vars, e.g. needed fields {id, nme}, not null.
   * @param pEnt Entity to fill, not null
   * @param pFlNm Field name, not null
   * @param pRs record-set, not null
   * @return if not-null value
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends IHasId<?>> boolean fill(
    final Map<String, Object> pRvs, final Map<String, Object> pVs, final T pEnt,
      final String pFdNm, final IRecSet<RS> pRs) throws Exception {
    String clNm;
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 7210);
    @SuppressWarnings("unchecked")
    List<String> tbAls = (List<String>) pVs.get("tbAls");
    if (tbAls.size() > 0) {
      clNm = tbAls.get(tbAls.size() - 1) + pFdNm.toUpperCase();
    } else {
      clNm = pFdNm.toUpperCase();
    }
    if (dbgSh) {
      this.log.debug(pRvs, FilFldSmpRs.class, "Column alias/cls: "
        + clNm + "/" + pEnt.getClass());
    }
    Integer intVal = pRs.getInt(clNm);
    Enum<?> val = null;
    if (intVal != null) {
      @SuppressWarnings("unchecked")
      Class<E> fdCls = (Class<E>) hldFdCls.get(pEnt.getClass(), pFdNm);
      val = fdCls.getEnumConstants()[intVal];
    }
    Method setr = this.hldSets.get(pEnt.getClass(), pFdNm);
    setr.invoke(pEnt, val);
    return val != null;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for hldFdCls.</p>
   * @return IHlNmClCl
   **/
  public final IHlNmClCl getHldFdCls() {
    return this.hldFdCls;
  }

  /**
   * <p>Setter for hldFdCls.</p>
   * @param pHldFdCls reference
   **/
  public final void setHldFdCls(final IHlNmClCl pHldFdCls) {
    this.hldFdCls = pHldFdCls;
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
}
