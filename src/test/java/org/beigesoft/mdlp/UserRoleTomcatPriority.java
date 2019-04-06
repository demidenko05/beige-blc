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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.AEditable;

/**
 * <p>Test model User's Role for Tomcat priority.</p>
 *
 * @author Yury Demidenko
 */
public class UserRoleTomcatPriority extends AEditable
  implements IHasId<UsRlTmc> {

  /**
   * <p>Composite primary and foreign ID.</p>
   **/
  private UsRlTmc userRoleTomcat;

  /**
   * <p>Priority.</p>
   **/
  private Integer priority;

  /**
   * <p>Version to check dirty or replication.</p>
   **/
  private Long ver;

  /**
   * <p>Geter for ver.</p>
   * @return Long
   **/
  @Override
  public final Long getVer() {
    return this.ver;
  }

  /**
   * <p>Setter for ver.</p>
   * @param pVer reference
   **/
  @Override
  public final void setVer(final Long pVer) {
    this.ver = pVer;
  }

  /**
   * <p>Getter for itsId.</p>
   * @return UsRlTmc
   **/
  @Override
  public final UsRlTmc getIid() {
    return this.userRoleTomcat;
  }

  /**
   * <p>Setter for itsId.</p>
   * @param pIid reference/value
   **/
  @Override
  public final void setIid(final UsRlTmc pIid) {
    this.userRoleTomcat = pIid;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for userRoleTomcat.</p>
   * @return UsRlTmc
   **/
  public final UsRlTmc getUserRoleTomcat() {
    return this.userRoleTomcat;
  }

  /**
   * <p>Setter for userRoleTomcat.</p>
   * @param pUserRoleTomcat reference
   **/
  public final void setUserRoleTomcat(final UsRlTmc pUserRoleTomcat) {
    this.userRoleTomcat = pUserRoleTomcat;
  }

  /**
   * <p>Getter for priority.</p>
   * @return Integer
   **/
  public final Integer getPriority() {
    return this.priority;
  }

  /**
   * <p>Setter for priority.</p>
   * @param pPriority reference
   **/
  public final void setPriority(final Integer pPriority) {
    this.priority = pPriority;
  }
}
