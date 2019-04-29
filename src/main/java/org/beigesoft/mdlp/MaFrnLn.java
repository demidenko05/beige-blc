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

import org.beigesoft.mdl.IOwned;

/**
 * <p>Persistable model to match native value to foreign one.
 * It's usually entity ID.</p>
 *
 * @author Yury Demidenko
 */
public class MaFrnLn extends AOrId implements IOwned<MaFrn, Long> {

  /**
   * <p>not null, owner.</p>
   **/
  private MaFrn ownr;

  /**
   * <p>not null, native value iid.toString(),
   * for String ID it's original value.</p>
   **/
  private String ntvVl;

  /**
   * <p>not null, foreign value.</p>
   **/
  private String frnVl;

  /**
   * <p>Optional, only for ID value in current database
   * to make SQL queries, hidden field.</p>
   **/
  private Long lnId1;

  /**
   * <p>Optional, only for complex ID value in current database
   * to make SQL queries, hidden field.</p>
   **/
  private Long lnId2;

  /**
   * <p>Getter for ownr.</p>
   * @return MaFrn
   **/
  @Override
  public final MaFrn getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final MaFrn pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for ntvVl.</p>
   * @return String
   **/
  public final String getNtvVl() {
    return this.ntvVl;
  }

  /**
   * <p>Setter for ntvVl.</p>
   * @param pNtvVl reference
   **/
  public final void setNtvVl(final String pNtvVl) {
    this.ntvVl = pNtvVl;
  }

  /**
   * <p>Getter for frnVl.</p>
   * @return String
   **/
  public final String getFrnVl() {
    return this.frnVl;
  }

  /**
   * <p>Setter for frnVl.</p>
   * @param pFrnVl reference
   **/
  public final void setFrnVl(final String pFrnVl) {
    this.frnVl = pFrnVl;
  }

  /**
   * <p>Getter for lnId1.</p>
   * @return Long
   **/
  public final Long getLnId1() {
    return this.lnId1;
  }

  /**
   * <p>Setter for lnId1.</p>
   * @param pLnId1 reference
   **/
  public final void setLnId1(final Long pLnId1) {
    this.lnId1 = pLnId1;
  }

  /**
   * <p>Getter for lnId2.</p>
   * @return Long
   **/
  public final Long getLnId2() {
    return this.lnId2;
  }

  /**
   * <p>Setter for lnId2.</p>
   * @param pLnId2 reference
   **/
  public final void setLnId2(final Long pLnId2) {
    this.lnId2 = pLnId2;
  }
}
