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
import java.util.HashMap;

import org.beigesoft.mdlp.IOrId;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Entities factories factory.</p>
 *
 * @author Yury Demidenko
 */
public class FctFctEnt implements IFctCls<IFctRq<?>> {

  /**
   * <p>Factories map "object's class"-"object's factory".</p>
   **/
  private final Map<Class<?>, IFctRq<?>> fcts =
    new HashMap<Class<?>, IFctRq<?>>();

  /**
   * <p>Database service to get ID database.</p>
   **/
  private IRdb rdb;

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pCls - bean name
   * @return requested bean
   * @throws Exception - an exception
   */
  @Override
  public final IFctRq<?> laz(//NOPMD
    // Rule:DoubleCheckedLocking isn't true see in beigesoft-bcommon test:
    // org.beigesoft.test.DoubleCkeckLockingWrApTest
    final Map<String, Object> pRqVs, final Class<?> pCls) throws Exception {
    IFctRq<?> fct = this.fcts.get(pCls);
    if (fct == null) {
      // locking:
      synchronized (this.fcts) {
        // make sure again whether it's null after locking:
        fct = this.fcts.get(pCls);
        if (IOrId.class.isAssignableFrom(pCls)) {
          fct = crPuFctOrId(pCls);
        } else {
          fct = crPuFctSmp(pCls);
        }
      }
    }
    return fct;
  }

  /**
   * <p>Create FctOrId and put into beans map.</p>
   * @param pCls - bean class
   * @return requested FctOrId
   * @throws Exception - an exception
   */
  protected final FctOrId crPuFctOrId(final Class<?> pCls) throws Exception {
    FctOrId fct = new FctOrId();
    fct.setCls(pCls);
    fct.setDbOr(this.rdb.getDbId());
    //assigning fully initialized object:
    this.fcts.put(pCls, fct);
    return fct;
  }

  /**
   * <p>Get FctSmp and put into beans map.</p>
   * @param pCls - bean class
   * @return requested FctSmp
   * @throws Exception - an exception
   */
  protected final FctSmp crPuFctSmp(final Class<?> pCls) throws Exception {
    FctSmp fct = new FctSmp();
    fct.setCls(pCls);
    //assigning fully initialized object:
    this.fcts.put(pCls, fct);
    return fct;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rdb.</p>
   * @return IRdb
   **/
  public final IRdb getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb pRdb) {
    this.rdb = pRdb;
  }
}
