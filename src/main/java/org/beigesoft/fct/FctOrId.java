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

import java.util.Map;

import org.beigesoft.mdlp.IOrId;

/**
 * <p>Simple factory that create a request(or) scoped IOrId
 * by using reflection.</p>
 *
 * @author Yury Demidenko
 * @param <M> type of created bean
 **/
public class FctOrId<M extends IOrId> implements IFctRq<M> {

  /**
   * <p>Object class.</p>
   **/
  private Class<M> cls;

  /**
   * <p>ID Database.</p>
   **/
  private Integer dbOr;

  /**
   * <p>Create a bean.</p>
   * @param pRqVs request scoped vars
   * @return M request(or) scoped bean
   * @throws Exception - an exception
   */
  @Override
  public final M create(final Map<String, Object> pRqVs) throws Exception {
    M object = this.cls.newInstance();
    object.setIsNew(false);
    object.setDbOr(this.dbOr);
    return object;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cls.</p>
   * @return Class<M>
   **/
  public final Class<M> getCls() {
    return this.cls;
  }

  /**
   * <p>Setter for cls.</p>
   * @param pCls reference
   **/
  public final void setCls(final Class<M> pCls) {
    this.cls = pCls;
  }

  /**
   * <p>Getter for dbOr.</p>
   * @return Integer
   **/
  public final Integer getDbOr() {
    return this.dbOr;
  }

  /**
   * <p>Setter for dbOr.</p>
   * @param pDbOr reference
   **/
  public final void setDbOr(final Integer pDbOr) {
    this.dbOr = pDbOr;
  }
}
