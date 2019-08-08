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

import java.util.ArrayList;
import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.log.ERngMth;
import org.beigesoft.log.Range;

/**
 * <p>Manager software.</p>
 *
 * @author Yury Demidenko
 */
public class MngSft implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String act = pRqDt.getParam("act");
    if ("sonl".equals(act) || "amul".equals(act)) {
      String dbgShStr = pRqDt.getParam("dbgSh");
      this.log.setDbgSh(Boolean.valueOf(dbgShStr));
      String dbgFlStr = pRqDt.getParam("dbgFl");
      String dbgClStr = pRqDt.getParam("dbgCl");
      int dbgFl = Integer.parseInt(dbgFlStr);
      int dbgCl = Integer.parseInt(dbgClStr);
      if ("sonl".equals(act)) {
        this.log.setRngMth(ERngMth.ONLY);
        this.log.setDbgFl(dbgFl);
        this.log.setDbgCl(dbgCl);
      } else {
        synchronized (this.log) {
          this.log.setRngMth(ERngMth.MULTI);
          Range rng = new Range();
          rng.setDbgFl(dbgFl);
          rng.setDbgCl(dbgCl);
          if (this.log.getRanges() == null) {
            this.log.setRanges(new ArrayList<Range>());
          }
          this.log.getRanges().add(rng);
        }
      }
    } else if ("drng".equals(act)) {
      synchronized (this.log) {
        String ridxS = pRqDt.getParam("ridx");
        int ridx = Integer.parseInt(ridxS);
        if (this.log.getRanges() != null
          && this.log.getRanges().size() > ridx) {
          this.log.getRanges().remove(ridx);
        }
      }
    }
    pRqDt.setAttr("rnd", "mng");
    pRqDt.setAttr("mngSft", this);
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
}
