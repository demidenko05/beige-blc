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
import org.beigesoft.mdl.IHasNm;
import org.beigesoft.mdl.IIdLnNm;
import org.beigesoft.mdl.AHasVr;

/**
 * <p>Model of I18N name. Beige-ORM does not support generic fields,
 * so internationalized things IHasNm should be in extending class.
 * And so does language cause it's used in the ID.</p>
 *
 * @param <T> item type
 * @param <ID> ID type
 * @author Yury Demidenko
 */
public abstract class AI18nNm<T extends IIdLnNm, ID extends AI18nNmId<T>>
    extends AHasVr<ID> implements IHasId<ID>, IHasNm {

  /**
   * <p>Name in the language.</p>
   **/
  private String nme;

  /**
   * <p>Getter for hasName.</p>
   * @return IHasNm
   **/
  public abstract T getHasNm();

  /**
   * <p>Setter for has name.</p>
   * @param pHasNm reference
   **/
  public abstract void setHasNm(T pHasNm);

  /**
   * <p>Getter for lang.</p>
   * @return Lng
   **/
  public abstract Lng getLng();

  /**
   * <p>Setter for lang.</p>
   * @param pLang reference
   **/
  public abstract void setLng(Lng pLang);

  /**
   * <p>Geter for nme.</p>
   * @return String
   **/
  @Override
  public final String getNme() {
    return this.nme;
  }

  /**
   * <p>Setter for nme.</p>
   * @param pNme reference
   **/
  @Override
  public final void setNme(final String pNme) {
    this.nme = pNme;
  }
}
