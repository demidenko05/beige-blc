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

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.prp.ISetng;

/**
 * <p>Service that retrieves info about this application.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent record set type
 */
public class About<RS> implements IPrc {

  /**
   * <p>RDB service.</p>
   **/
  private IRdb rdb;

  /**
   * <p>Setting service.</p>
   **/
  private ISetng stgUvd;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    pRvs.put("dbInf", this.rdb.getDbInf());
    pRvs.put("appVer", this.stgUvd.lazCmnst().get("appVer"));
    pRqDt.setAttr("rnd", "abj");
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

  /**
   * <p>Getter for stgUvd.</p>
   * @return ISetng
   **/
  public final ISetng getStgUvd() {
    return this.stgUvd;
  }

  /**
   * <p>Setter for stgUvd.</p>
   * @param pStgUvd reference
   **/
  public final void setStgUvd(final ISetng pStgUvd) {
    this.stgUvd = pStgUvd;
  }
}
