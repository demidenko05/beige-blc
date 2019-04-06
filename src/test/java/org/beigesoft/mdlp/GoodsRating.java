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

import org.beigesoft.mdl.AEditable;
import org.beigesoft.mdl.IHasId;

/**
 * <p>Model of average goods rating.</p>
 *
 * @author Yury Demidenko
 */
public class GoodsRating extends AEditable
  implements IHasId<GoodVersionTime> {

  /**
   * <p>Goods, PK.</p>
   **/
  private GoodVersionTime goods;

  /**
   * <p>Average rating, 0..10, if exist.</p>
   **/
  private Integer averageRating;

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
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final GoodVersionTime getIid() {
    return this.goods;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pId model ID
   **/
  @Override
  public final void setIid(final GoodVersionTime pIid) {
    this.goods = pIid;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for goods.</p>
   * @return InvItem
   **/
  public final GoodVersionTime getGoods() {
    return this.goods;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public final void setGoods(final GoodVersionTime pGoods) {
    this.goods = pGoods;
  }

  /**
   * <p>Getter for averageRating.</p>
   * @return Integer
   **/
  public final Integer getAverageRating() {
    return this.averageRating;
  }

  /**
   * <p>Setter for averageRating.</p>
   * @param pAverageRating reference
   **/
  public final void setAverageRating(final Integer pAverageRating) {
    this.averageRating = pAverageRating;
  }
}
