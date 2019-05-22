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

package org.beigesoft.srv;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <p>Date service standard.</p>
 *
 * @author Yury Demidenko
 */
public class SrvDt implements ISrvDt {

  /**
   * <p>Format date-hour-min-sec-millisecond ISO8601 with time zone,
   * e.g. 2001-07-04T12:08:56.235-0700.</p>
   **/
  private final DateFormat dfDtMsTz8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); Android bug

  /**
   * <p>Format date-hour-sec-millisecond ISO8601 no time zone,
   * e.g. 2001-07-04T12:08:56.235.</p>
   **/
  private final DateFormat dfDtMs8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  /**
   * <p>Format date-hour-min-sec ISO8601 no time zone,
   * e.g. 2001-07-04T12:08:56.</p>
   **/
  private final DateFormat dfDtSc8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  /**
   * <p>Format date-hour-min ISO8601 no time zone,
   * e.g. 2001-07-04T12:08.</p>
   **/
  private final DateFormat dfDtMn8601 =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

  /**
   * <p>Format date ISO8601 no time zone,
   * e.g. 2001-07-04.</p>
   **/
  private final DateFormat dfDt8601 = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * <p>Format year-month ISO8601, e.g. 2001-07.</p>
   **/
  private final DateFormat dfYearMonth = new SimpleDateFormat("yyyy-MM");

  /**
   * <p>Format date localized for logging.</p>
   **/
  private final DateFormat dateFormatLog = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

  /**
   * <p>Format date-time localized.</p>
   **/
  private final DateFormat dateTimeFormat = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

  /**
   * <p>Format date for file name ddMMyy, e.g. 310118.</p>
   **/
  private final DateFormat dateDdMmYy = new SimpleDateFormat("ddMMyy");

  /**
   * <p>Format date-time-sec localized.</p>
   **/
  private final DateFormat dateTimeSecFormat = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

  /**
   * <p>Format date-time-sec-ms localized.</p>
   **/
  private final DateFormat dateTimeSecMsFormat = DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG);

  /**
   * <p>Format date localized.</p>
   **/
  private final DateFormat dateFormat = DateFormat
        .getDateInstance(DateFormat.MEDIUM);

  /**
   * <p>Parse date from ISO8601 year month,
   * e.g. from 2001-07.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  @Override
  public final Date fromYearMonth(final String pDtSt) throws Exception {
    return this.dfYearMonth.parse(pDtSt);
  }

  /**
   * <p>Format date to IS8601 year month.</p>
   * @param pDt date
   * @return year-month string
   * @throws Exception - an Exception
   **/
  @Override
  public final String toYearMonth(final Date pDt) throws Exception {
    return this.dfYearMonth.format(pDt);
  }

  /**
   * <p>Format date for file name ddMMyy, 310118.</p>
   * @param pDt date
   * @return ddMMyy
   * @throws Exception - an Exception
   **/
  @Override
  public final String toDdMmYy(final Date pDt) throws Exception {
    return this.dateDdMmYy.format(pDt);
  }

  /**
   * <p>Format date to ISO8601 full string,
   * e.g. 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  @Override
  public final String to8601FullTz(final Date pDt) throws Exception {
    return this.dfDtMsTz8601.format(pDt);
  }

  /**
   * <p>Format date to ISO8601 full string without TZ,
   * e.g. 2001-07-04T12:08:56.235.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  @Override
  public final String to8601Full(final Date pDt) throws Exception {
    return this.dfDtMs8601.format(pDt);
  }

  /**
   * <p>Format date to ISO8601 date-time-sec string without TZ,
   * e.g. 2001-07-04T12:08:56.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  @Override
  public final String to8601DateTimeSec(final Date pDt) throws Exception {
    return this.dfDtSc8601.format(pDt);
  }

  /**
   * <p>Format date to ISO8601 date-time string without TZ,
   * e.g. 2001-07-04T12:08.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  @Override
  public final String to8601DateTime(final Date pDt) throws Exception {
    return this.dfDtMn8601.format(pDt);
  }

  /**
   * <p>Format date to ISO8601 date string without TZ,
   * e.g. 2001-07-04.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  @Override
  public final String to8601Date(final Date pDt) throws Exception {
    return this.dfDt8601.format(pDt);
  }

  /**
   * <p>Format date to localized string with or without seconds,
   * e.g. Jan 2, 2016 12:00 AM. Can be used for logging.</p>
   * @param pDt date
   * @return date string
   * @throws Exception - an Exception
   **/
  @Override
  public final String toLocal(final Date pDt) throws Exception {
    return this.dateFormatLog.format(pDt);
  }

  /**
   * <p>Format date to localized date string,
   * e.g. Jan 2, 2016".</p>
   * @param pDt date
   * @return date string
   * @throws Exception - an Exception
   **/
  @Override
  public final String toDate(final Date pDt) throws Exception {
    return this.dateFormat.format(pDt);
  }

  /**
   * <p>Format date to localized date-time string,
   * e.g. Jan 2, 2016 12:00 AM.</p>
   * @param pDt date
   * @return date-time string
   * @throws Exception - an Exception
   **/
  @Override
  public final String toDateTime(final Date pDt) throws Exception {
    return this.dateTimeFormat.format(pDt);
  }

  /**
   * <p>Format date to localized date-time-sec string,
   * e.g. Jan 2, 2016 12:00:12 AM.</p>
   * @param pDt date
   * @return date-time-sec string
   * @throws Exception - an Exception
   **/
  @Override
  public final String toDateTimeSec(final Date pDt) throws Exception {
    return this.dateTimeSecFormat.format(pDt);
  }

  /**
   * <p>Format date to localized date-time-sec-ms string,
   * e.g. Jan 2, 2016 12:00:12.12 AM.</p>
   * @param pDt date
   * @return date-time-sec-ms string
   * @throws Exception - an Exception
   **/
  @Override
  public final String toDateTimeSecMs(final Date pDt) throws Exception {
    return this.dateTimeSecMsFormat.format(pDt);
  }

  /**
   * <p>Parse date from ISO8601 full string,
   * e.g. from 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  @Override
  public final Date from8601FullTz(final String pDtSt) throws Exception {
    return this.dfDtMsTz8601.parse(pDtSt);
  }

  /**
   * <p>Parse date from ISO8601 full string without TZ,
   * e.g. from 2001-07-04T12:08:56.235.</p>
   * @param pDtSt date in ISO8601 format
   * @throws Exception - an Exception
   * @return String representation
   **/
  @Override
  public final Date from8601Full(final String pDtSt) throws Exception {
    return this.dfDtMs8601.parse(pDtSt);
  }

  /**
   * <p>Parse date from ISO8601 date-time-sec string without TZ,
   * e.g. from 2001-07-04T12:08:56.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  @Override
  public final Date from8601DateTimeSec(final String pDtSt) throws Exception {
    return this.dfDtSc8601.parse(pDtSt);
  }

  /**
   * <p>Parse date from ISO8601 date-time string without TZ,
   * e.g. from 2001-07-04T12:08.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  @Override
  public final Date from8601DateTime(final String pDtSt) throws Exception {
    return this.dfDtMn8601.parse(pDtSt);
  }

  /**
   * <p>Parse date from ISO8601 date string without TZ,
   * e.g. from 2001-07-04.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  @Override
  public final Date from8601Date(final String pDtSt) throws Exception {
    return this.dfDt8601.parse(pDtSt);
  }
}
