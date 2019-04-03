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

package org.beigesoft.hld;

import java.util.Map;

import org.beigesoft.mdlp.MaFrn;
import org.beigesoft.mdlp.MaFrnLn;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Retriever Match Foreign from database.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class HlMaFrn<RS> implements IHldEx<Long, MaFrn>  {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm<RS> orm;

  /**
   * <p>Get match foreign  given ID.</p>
   * @param pRqVs request scoped vars
   * @param pId ID
   * @return match foreign
   * @throws Exception an Exception
   **/
  public final MaFrn get(final Map<String, Object> pRqVs,
    final Long pId) throws Exception {
    MaFrn mf = getOrm().retEntId(pRqVs, null, MaFrn.class, pId);
    mf.setLns(getOrm().retLstCnd(pRqVs, null, MaFrnLn.class,
      "where OWNR=" + pId));
    return mf;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for orm.</p>
   * @return IOrm<RS>
   **/
  public final IOrm<RS> getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm<RS> pOrm) {
    this.orm = pOrm;
  }
}
