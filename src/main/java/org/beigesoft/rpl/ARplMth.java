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

package org.beigesoft.rpl;

import java.util.Date;

import org.beigesoft.mdlp.AOrIdNm;

/**
 * <p>Base model of replication method.</p>
 *
 * @author Yury Demidenko
 */
public abstract class ARplMth extends AOrIdNm {

  /**
   * <p>Last date replication, nullable.</p>
   **/
  private Date lstDt;

  /**
   * <p>Requested (source) database ID, not null.</p>
   **/
  private Integer rqDbId;

  //Simple getters and setters:
  /**
   * <p>Getter for lstDt.</p>
   * @return Date
   **/
  public final Date getLstDt() {
    return this.lstDt;
  }

  /**
   * <p>Setter for lstDt.</p>
   * @param pLstDt reference
   **/
  public final void setLstDt(final Date pLstDt) {
    this.lstDt = pLstDt;
  }

  /**
   * <p>Getter for rqDbId.</p>
   * @return Integer
   **/
  public final Integer getRqDbId() {
    return this.rqDbId;
  }

  /**
   * <p>Setter for rqDbId.</p>
   * @param pRqDbId reference
   **/
  public final void setRqDbId(final Integer pRqDbId) {
    this.rqDbId = pRqDbId;
  }
}
