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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.log.LogSmp;
import org.beigesoft.srv.IRdb;
import org.beigesoft.srv.IOrm;

/**
 * <p>Test auxiliary factory.</p>
 *
 * @author Yury Demidenko
 */
public class FctTst implements IFctApp {

  /**
   * <p>Beans map.</p>
   **/
  private final Map<String, Object> beans = new HashMap<String, Object>();

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @return Object - requested bean or NULL cause it's inner factory
   * @throws Exception - an exception
   */
  public final Object laz(final Map<String, Object> pRqVs,
    final String pBnNm) throws Exception {
    if (pBnNm == null) {
      throw new ExcCode(ExcCode.WRPR, "Null bean name!!!");
    }
    Object rz = this.beans.get(pBnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.beans.get(pBnNm);
        if (rz == null) {
          if (FctBlc.LOGSTDNM.equals(pBnNm)) {
            rz = lazLog();
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Lazy getter Reflect.</p>
   * @return Reflect
   */
  private ILog lazLog() {
    ILog rz = (ILog) this.beans.get(FctBlc.LOGSTDNM);
    if (rz == null) {
      rz = new LogSmp();
      this.beans.put(FctBlc.LOGSTDNM, rz);
    }
    return rz;
  }

  /**
   * <p>Release beans (memory). This is "memory friendly" factory.</p>
   * @throws Exception - an exception
   */
  public final synchronized void release() throws Exception {
    this.beans.clear();
  }
}
