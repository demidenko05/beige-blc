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

/**
 * <p>Abstraction of service that prints a number according given
 * decimal separator, decimal group separator, decimal places after dot,
 * and digits in the group. The number is passed as String with dot decimal
 * separator (or without it) and no group separator,
 * e.g. "12346734.123" or "12667711". This service never does rounding,
 * so if you pass number "1234.2356" and decimal places after dot 3,
 * then result will be "1,234.235". For null or empty string result will be
 * empty string. </p>
 *
 * @author Yury Demidenko
 */
public interface INumStr {

  /**
   * <p>Formats number formatted by given decimal separator, decimal group
   * separator and decimal places after dot. Digits in group is 3.</p>
   * @param pNum e.g. "12146678.12"
   * @param pDcSp decimal separator, e.g. ","
   * @param pDcGrSp decimal group separator, e.g. " "
   * @param pDcPl decimal places after dot, e.g. 2
   * @return String internationalized number, e.g. "12 146 678,12"
   **/
  String frmt(String pNum, String pDcSp, String pDcGrSp,
    Integer pDcPl);

  /**
   * <p>Formats number formatted by given decimal separator, decimal group
   * separator, decimal places after dot and digits in group.</p>
   * @param pNum e.g. "12146678.12"
   * @param pDcSp decimal separator, e.g. ","
   * @param pDcGrSp decimal group separator, e.g. " "
   * @param pDcPl decimal places after dot, e.g. 2
   * @param pDgInGr Digits in group, e.g. 3
   * @return String internationalized number, e.g. "12 146 678,12"
   **/
  String frmt(String pNum, String pDcSp, String pDcGrSp,
    Integer pDcPl, Integer pDgInGr);

  /**
   * <p>Formats number without trailing zeros, e.g.:
   * 1.0 -> 1; 1.1234[,sp] -> 1,1234; 1234.0[.,] -> 1,1234;
   * 1234.10[.,] -> 1,1234.1.
   * </p>
   * @param pNum e.g. "1214.10"
   * @param pDcSp decimal separator, e.g. ","
   * @param pDcGrSp decimal group separator, e.g. " "
   * @param pDgInGr Digits in group, e.g. 3
   * @return internationalized number without trailing zeros e.g. "1 214,1"
   **/
  String frmtNtz(String pNum, String pDcSp, String pDcGrSp,
    Integer pDgInGr);
}
