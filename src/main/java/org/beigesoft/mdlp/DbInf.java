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

package org.beigesoft.mdlp;

/**
 * <p>Every database must has ID for replication purposes
 * and version for upgrade purpose.</p>
 *
 * @author Yury Demidenko
 */
public class DbInf {

  /**
   * <p>ID of type Integer.</p>
   **/
  private Integer dbId;

  /**
   * <p>Version of type Integer.</p>
   **/
  private Integer dbVr;

  /**
   * <p>Infription.</p>
   **/
  private String inf;

  //Simple getters and setters:
  /**
   * <p>Getter for dbId.</p>
   * @return Integer
   **/
  public final Integer getDbId() {
    return this.dbId;
  }

  /**
   * <p>Setter for dbId.</p>
   * @param pDbId reference
   **/
  public final void setDbId(final Integer pDbId) {
    this.dbId = pDbId;
  }

  /**
   * <p>Getter for dbVr.</p>
   * @return Integer
   **/
  public final Integer getDbVr() {
    return this.dbVr;
  }

  /**
   * <p>Setter for dbVr.</p>
   * @param pDbVr reference
   **/
  public final void setDbVr(final Integer pDbVr) {
    this.dbVr = pDbVr;
  }

  /**
   * <p>Getter for inf.</p>
   * @return String
   **/
  public final String getInf() {
    return this.inf;
  }

  /**
   * <p>Setter for inf.</p>
   * @param pInf reference
   **/
  public final void setInf(final String pInf) {
    this.inf = pInf;
  }
}
