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

/**
 * <p>Abstraction of date service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvDt {

  /**
   * <p>Format date for file name ddMMyy, 310118.</p>
   * @param pDt date
   * @return ddMMyy
   * @throws Exception - an Exception
   **/
  String toDdMmYy(Date pDt) throws Exception;

  /**
   * <p>Format date to ISO8601 full string,
   * e.g. 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  String to8601FullTz(Date pDt) throws Exception;

  /**
   * <p>Format date to ISO8601 full string without TZ,
   * e.g. 2001-07-04T12:08:56.235.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  String to8601Full(Date pDt) throws Exception;

  /**
   * <p>Format date to ISO8601 date-time-sec string without TZ,
   * e.g. 2001-07-04T12:08:56.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  String to8601DateTimeSec(Date pDt) throws Exception;

  /**
   * <p>Format date to ISO8601 date-time string without TZ,
   * e.g. 2001-07-04T12:08.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  String to8601DateTime(Date pDt) throws Exception;

  /**
   * <p>Format date to ISO8601 date string without TZ,
   * e.g. 2001-07-04.</p>
   * @param pDt date
   * @return ISO8601 string
   * @throws Exception - an Exception
   **/
  String to8601Date(Date pDt) throws Exception;

  /**
   * <p>Format date to localized string with or without seconds,
   * e.g. Jan 2, 2016 12:00 AM. Can be used for logging.</p>
   * @param pDt date
   * @return date string
   * @throws Exception - an Exception
   **/
  String toLocal(Date pDt) throws Exception;

  /**
   * <p>Format date to localized date string,
   * e.g. Jan 2, 2016".</p>
   * @param pDt date
   * @return date string
   * @throws Exception - an Exception
   **/
  String toDate(Date pDt) throws Exception;

  /**
   * <p>Format date to localized date-time string,
   * e.g. Jan 2, 2016 12:00 AM.</p>
   * @param pDt date
   * @return date-time string
   * @throws Exception - an Exception
   **/
  String toDateTime(Date pDt) throws Exception;

  /**
   * <p>Format date to localized date-time-sec string,
   * e.g. Jan 2, 2016 12:00:12 AM.</p>
   * @param pDt date
   * @return date-time-sec string
   * @throws Exception - an Exception
   **/
  String toDateTimeSec(Date pDt) throws Exception;

  /**
   * <p>Format date to localized date-time-sec-ms string,
   * e.g. Jan 2, 2016 12:00:12.12 AM.</p>
   * @param pDt date
   * @return date-time-sec-ms string
   * @throws Exception - an Exception
   **/
  String toDateTimeSecMs(Date pDt) throws Exception;

  /**
   * <p>Parse date from ISO8601 full string,
   * e.g. from 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  Date from8601FullTz(String pDtSt) throws Exception;

  /**
   * <p>Parse date from ISO8601 full string without TZ,
   * e.g. from 2001-07-04T12:08:56.235.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  Date from8601Full(String pDtSt) throws Exception;

  /**
   * <p>Parse date from ISO8601 date-time-sec string without TZ,
   * e.g. from 2001-07-04T12:08:56.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  Date from8601DateTimeSec(String pDtSt) throws Exception;

  /**
   * <p>Parse date from ISO8601 date-time string without TZ,
   * e.g. from 2001-07-04T12:08.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  Date from8601DateTime(String pDtSt) throws Exception;

  /**
   * <p>Parse date from ISO8601 date string without TZ,
   * e.g. from 2001-07-04.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  Date from8601Date(String pDtSt) throws Exception;

  /**
   * <p>Parse date from ISO8601 year month,
   * e.g. from 2001-07.</p>
   * @param pDtSt date in ISO8601 format
   * @return String representation
   * @throws Exception - an Exception
   **/
  Date fromYearMonth(String pDtSt) throws Exception;

  /**
   * <p>Format date to IS8601 year month.</p>
   * @param pDt date
   * @return year-month string
   * @throws Exception - an Exception
   **/
  String toYearMonth(Date pDt) throws Exception;
}
