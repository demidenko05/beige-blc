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
import java.util.Map;

/**
 * <p>Abstraction of date service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrvDt {

  /**
   * <p>Format date for file name ddMMyy, 310118.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return ddMMyy
   * @throws Exception - an exception
   **/
  String toDdMmYy(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to ISO8601 full string,
   * e.g. 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601Full(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to ISO8601 full string without TZ,
   * e.g. 2001-07-04T12:08:56.235.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601FullNoTz(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to ISO8601 date-time-sec string without TZ,
   * e.g. 2001-07-04T12:08:56.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601DateTimeSecNoTz(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to ISO8601 date-time string without TZ,
   * e.g. 2001-07-04T12:08.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601DateTimeNoTz(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to ISO8601 date string without TZ,
   * e.g. 2001-07-04.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return ISO8601 string
   * @throws Exception - an exception
   **/
  String toIso8601DateNoTz(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to localized string with or without seconds,
   * e.g. Jan 2, 2016 12:00 AM. Can be used for logging.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return date string
   * @throws Exception - an exception
   **/
  String toLocalString(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to localized date string,
   * e.g. Jan 2, 2016".</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return date string
   * @throws Exception - an exception
   **/
  String toDateString(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to localized date-time string,
   * e.g. Jan 2, 2016 12:00 AM.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return date-time string
   * @throws Exception - an exception
   **/
  String toDateTimeString(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to localized date-time-sec string,
   * e.g. Jan 2, 2016 12:00:12 AM.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return date-time-sec string
   * @throws Exception - an exception
   **/
  String toDateTimeSecString(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Format date to localized date-time-sec-ms string,
   * e.g. Jan 2, 2016 12:00:12.12 AM.</p>
   * @param pDate date
   * @param pRqVs request scoped varss
   * @return date-time-sec-ms string
   * @throws Exception - an exception
   **/
  String toDateTimeSecMsString(Date pDate,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Parse date from ISO8601 full string,
   * e.g. from 2001-07-04T12:08:56.235-07:00.</p>
   * @param pDateStr date in ISO8601 format
   * @param pRqVs request scoped varss
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601Full(String pDateStr,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Parse date from ISO8601 full string without TZ,
   * e.g. from 2001-07-04T12:08:56.235.</p>
   * @param pDateStr date in ISO8601 format
   * @param pRqVs request scoped varss
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601FullNoTz(String pDateStr,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Parse date from ISO8601 date-time-sec string without TZ,
   * e.g. from 2001-07-04T12:08:56.</p>
   * @param pDateStr date in ISO8601 format
   * @param pRqVs request scoped varss
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601DateTimeSecNoTz(String pDateStr,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Parse date from ISO8601 date-time string without TZ,
   * e.g. from 2001-07-04T12:08.</p>
   * @param pDateStr date in ISO8601 format
   * @param pRqVs request scoped varss
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601DateTimeNoTz(String pDateStr,
    Map<String, Object> pRqVs) throws Exception;

  /**
   * <p>Parse date from ISO8601 date string without TZ,
   * e.g. from 2001-07-04.</p>
   * @param pDateStr date in ISO8601 format
   * @param pRqVs request scoped varss
   * @return String representation
   * @throws Exception - an exception
   **/
  Date fromIso8601DateNoTz(String pDateStr,
    Map<String, Object> pRqVs) throws Exception;
}
