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

package org.beigesoft.test;


import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.mdl.EPeriod;


/**
 * <p>It's a long initialized bean to test Double Checked Locking.</p>
 *
 * @author Yury Demidenko
 */
public class LongInitializedBean {

  private EPeriod itsPeriod = null;
  
  private Date itsDate = null;

  private BigDecimal itsPrice = null;
  
  private Boolean isClosed = false;

  public LongInitializedBean() {
    // long initialization
    try {
      Thread.sleep(5);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.itsPeriod = EPeriod.DAILY;
    this.itsDate = new Date();
    try {
      Thread.sleep(5);
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.itsPrice = new BigDecimal("12.11");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for itsPeriod.</p>
   * @return EPeriod
   **/
  public final EPeriod getItsPeriod() {
    return this.itsPeriod;
  }

  /**
   * <p>Setter for itsPeriod.</p>
   * @param pItsPeriod reference
   **/
  public final void setItsPeriod(final EPeriod pItsPeriod) {
    this.itsPeriod = pItsPeriod;
  }

  /**
   * <p>Getter for itsDate.</p>
   * @return Date
   **/
  public final Date getItsDate() {
    return this.itsDate;
  }

  /**
   * <p>Setter for itsDate.</p>
   * @param pItsDate reference
   **/
  public final void setItsDate(final Date pItsDate) {
    this.itsDate = pItsDate;
  }

  /**
   * <p>Getter for itsPrice.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsPrice() {
    return this.itsPrice;
  }

  /**
   * <p>Setter for itsPrice.</p>
   * @param pItsPrice reference
   **/
  public final void setItsPrice(final BigDecimal pItsPrice) {
    this.itsPrice = pItsPrice;
  }

  /**
   * <p>Getter for isClosed.</p>
   * @return Boolean
   **/
  public final Boolean getIsClosed() {
    return this.isClosed;
  }

  /**
   * <p>Setter for isClosed.</p>
   * @param pIsClosed reference
   **/
  public final void setIsClosed(final Boolean pIsClosed) {
    this.isClosed = pIsClosed;
  }
}
