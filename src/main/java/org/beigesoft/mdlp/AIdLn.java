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

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.mdl.IIdLn;

/**
 * <p>Base abstraction mdlp model with ID Long type and version.
 * This is for replicable model, i.e. ID is non-auto-generated.</p>
 *
 * @author Yury Demidenko
 */
public abstract class AIdLn extends AHasVr<Long> implements IIdLn {

  /**
   * <p>Fast explicit, usable ID of type Long.</p>
   **/
  private Long iid;

  /**
   * <p>Geter for id.</p>
   * @return Long
   **/
  @Override
  public final Long getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for id.</p>
   * @param pId reference/value
   **/
  @Override
  public final void setIid(final Long pId) {
    this.iid = pId;
  }
}
