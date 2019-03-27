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

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

import org.beigesoft.mdl.EStatus;

/**
 * <p>Model for test ORM. Version auto-increment.</p>
 *
 * @author Yury Demidenko
 */
 public class PersistableHead extends AOrIdVr {

  /**
   * <p>Date.</p>
   **/
  private Date itsDate;

  /**
   * <p>Status.</p>
   **/
  private EStatus itsStatus;

  /**
   * <p>Waiting time from 1 to 200.</p>
   **/
  private Department itsDepartment;

  /**
   * <p>Waiting time from 1 to 200.</p>
   **/
  private Integer waitingTime;

  /**
   * <p>Is it closed?</p>
   **/
  private Boolean isClosed = false;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal itsTotal;

  /**
   * <p>Lines.</p>
   **/
  private List<PersistableLine> persistableLines;

  /**
   * <p>Integer.</p>
   **/
  private Integer itsInteger;

  /**
   * <p>Long.</p>
   **/
  private Long itsLong;

  /**
   * <p>Float.</p>
   **/
  private Float itsFloat;

  /**
   * <p>Double.</p>
   **/
  private Double itsDouble;

  /**
   * <p>Non-persistable, hidden.</p>
   **/
  private String tmpDescription;

  //Simple getters and setters:
  /**
   * <p>Geter for itsDate.</p>
   * @return Date
   **/
  public final Date getItsDate() {
    return this.itsDate;
  }

  /**
   * <p>Setter for itsDate.</p>
   * @param pItsDate reference/value
   **/
  public final void setItsDate(final Date pItsDate) {
    this.itsDate = pItsDate;
  }

  /**
   * <p>Geter for itsStatus.</p>
   * @return EStatus
   **/
  public final EStatus getItsStatus() {
    return this.itsStatus;
  }

  /**
   * <p>Setter for itsStatus.</p>
   * @param pItsStatus reference/value
   **/
  public final void setItsStatus(final EStatus pItsStatus) {
    this.itsStatus = pItsStatus;
  }

  /**
   * <p>Geter for itsDepartment.</p>
   * @return Department
   **/
  public final Department getItsDepartment() {
    return this.itsDepartment;
  }

  /**
   * <p>Setter for itsDepartment.</p>
   * @param pItsDepartment reference/value
   **/
  public final void setItsDepartment(final Department pItsDepartment) {
    this.itsDepartment = pItsDepartment;
  }

  /**
   * <p>Geter for waitingTime.</p>
   * @return Integer
   **/
  public final Integer getWaitingTime() {
    return this.waitingTime;
  }

  /**
   * <p>Setter for waitingTime.</p>
   * @param pWaitingTime reference/value
   **/
  public final void setWaitingTime(final Integer pWaitingTime) {
    this.waitingTime = pWaitingTime;
  }

  /**
   * <p>Geter for isClosed.</p>
   * @return Boolean
   **/
  public final Boolean getIsClosed() {
    return this.isClosed;
  }

  /**
   * <p>Setter for isClosed.</p>
   * @param pIsClosed reference/value
   **/
  public final void setIsClosed(final Boolean pIsClosed) {
    this.isClosed = pIsClosed;
  }

  /**
   * <p>Geter for itsTotal.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItsTotal() {
    return this.itsTotal;
  }

  /**
   * <p>Setter for itsTotal.</p>
   * @param pItsTotal reference/value
   **/
  public final void setItsTotal(final BigDecimal pItsTotal) {
    this.itsTotal = pItsTotal;
  }

  /**
   * <p>Geter for persistableLines.</p>
   * @return List<PersistableLine>
   **/
  public final List<PersistableLine> getPersistableLines() {
    return this.persistableLines;
  }

  /**
   * <p>Setter for persistableLines.</p>
   * @param pPersistableLines reference/value
   **/
  public final void setPersistableLines(final List<PersistableLine> pPersistableLines) {
    this.persistableLines = pPersistableLines;
  }

  /**
   * <p>Geter for itsInteger.</p>
   * @return Integer
   **/
  public final Integer getItsInteger() {
    return this.itsInteger;
  }

  /**
   * <p>Setter for itsInteger.</p>
   * @param pItsInteger reference/value
   **/
  public final void setItsInteger(final Integer pItsInteger) {
    this.itsInteger = pItsInteger;
  }

  /**
   * <p>Geter for itsLong.</p>
   * @return Long
   **/
  public final Long getItsLong() {
    return this.itsLong;
  }

  /**
   * <p>Setter for itsLong.</p>
   * @param pItsLong reference/value
   **/
  public final void setItsLong(final Long pItsLong) {
    this.itsLong = pItsLong;
  }

  /**
   * <p>Geter for itsFloat.</p>
   * @return Float
   **/
  public final Float getItsFloat() {
    return this.itsFloat;
  }

  /**
   * <p>Setter for itsFloat.</p>
   * @param pItsFloat reference/value
   **/
  public final void setItsFloat(final Float pItsFloat) {
    this.itsFloat = pItsFloat;
  }

  /**
   * <p>Geter for itsDouble.</p>
   * @return Double
   **/
  public final Double getItsDouble() {
    return this.itsDouble;
  }

  /**
   * <p>Setter for itsDouble.</p>
   * @param pItsDouble reference/value
   **/
  public final void setItsDouble(final Double pItsDouble) {
    this.itsDouble = pItsDouble;
  }

  /**
   * <p>Geter for tmpDescription.</p>
   * @return String
   **/
  public final String getTmpDescription() {
    return this.tmpDescription;
  }

  /**
   * <p>Setter for tmpDescription.</p>
   * @param pTmpDescription reference/value
   **/
  public final void setTmpDescription(final String pTmpDescription) {
    this.tmpDescription = pTmpDescription;
  }
}
