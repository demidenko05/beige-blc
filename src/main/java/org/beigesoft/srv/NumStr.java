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
 * <p>Service that prints a number according given
 * decimal separator, decimal group separator, decimal places after dot,
 * and digits in the group. The number is passed as String with dot decimal
 * separator (or without it) and no group separator,
 * e.g. "12346734.123" or "12667711". This service never does rounding,
 * so if you pass number "1234.2356" and decimal places after dot 3,
 * then result will be "1,234.235". For null or empty string result will be
 * empty string.</p>
 *
 * @author Yury Demidenko
 */
public class NumStr implements INumStr {

  /**
   * <p>Formats number formatted by given decimal separator, decimal group
   * separator and decimal places after dot. Digits in group is 3.</p>
   * @param pNum e.g. "12146678.12"
   * @param pDcSp decimal separator, e.g. ","
   * @param pDcGrSp decimal group separator, e.g. " "
   * @param pDcPl decimal places after dot, e.g. 2
   * @return String internationalized number, e.g. "12 146 678,12"
   **/
  @Override
  public final String frmt(final String pNum, final String pDcSp,
    final String pDcGrSp, final Integer pDcPl) {
    return frmt(pNum, pDcSp, pDcGrSp, pDcPl, 3);
  }

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
  @Override
  public final String frmt(final String pNum, final String pDcSp,
    final String pDcGrSp, final Integer pDcPl, final Integer pDgInGr) {
    if (pNum == null || "".equals(pNum)) {
      return "";
    }
    int dotIdx = pNum.indexOf(".");
    String ltWng;
    String rtWng;
    if (dotIdx == -1) {
      ltWng = pNum;
      rtWng = null;
    } else {
      ltWng = pNum.substring(0, dotIdx);
      rtWng = pNum.substring(dotIdx + 1);
    }
    StringBuffer sb = new StringBuffer();
    if (ltWng.startsWith("-")) {
      ltWng = ltWng.substring(1);
      sb.append("-");
    }
    addLtWng(ltWng, sb, pDcGrSp, pDgInGr);
    if (pDcPl > 0) {
      sb.append(pDcSp);
      for (int i = 0; i < pDcPl; i++) {
        if (rtWng != null && rtWng.length() > i) {
          sb.append(rtWng.charAt(i));
        } else {
          sb.append("0");
        }
      }
    }
    return sb.toString();
  }

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
  @Override
  public final String frmtNtz(final String pNum, final String pDcSp,
    final String pDcGrSp, final Integer pDgInGr) {
    if (pNum == null || "".equals(pNum)) {
      return "";
    }
    int dotIdx = pNum.indexOf(".");
    String ltWng;
    String rtWng;
    if (dotIdx == -1) {
      ltWng = pNum;
      rtWng = null;
    } else {
      ltWng = pNum.substring(0, dotIdx);
      rtWng = pNum.substring(dotIdx + 1);
    }
    StringBuffer sb = new StringBuffer();
    if (ltWng.startsWith("-")) {
      ltWng = ltWng.substring(1);
      sb.append("-");
    }
    addLtWng(ltWng, sb, pDcGrSp, pDgInGr);
    if (rtWng != null && rtWng.length() > 0 && !"0".equals(rtWng)) {
      sb.append(pDcSp);
      int lastIdxZero = rtWng.lastIndexOf("0");
      for (int i = 0; i < lastIdxZero; i++) {
        char ch = rtWng.charAt(i);
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  /**
   * <p>Adds left wing with group separators into string-buffer.</p>
   * @param pLtWng left wing
   * @param pSb string buffer
   * @param pDcGrSp decimal group separator, e.g. " "
   * @param pDgInGr Digits in group, e.g. 3
    **/
  public final void addLtWng(final String pLtWng, final StringBuffer pSb,
    final String pDcGrSp, final Integer pDgInGr) {
    for (int i = 0; i < pLtWng.length(); i++) {
      char ch = pLtWng.charAt(i);
      pSb.append(ch);
      int idxFl = pLtWng.length() - i;
      if (pDgInGr == 2) {
        //hard-coded Indian style 12,12,14,334
        if (idxFl == 4) {
          pSb.append(pDcGrSp);
        } else {
          idxFl -= 3;
        }
      }
      if (idxFl >= pDgInGr) {
        int gc = idxFl / pDgInGr;
        if (gc > 0) {
          int rem;
          if (gc == 1) {
            rem = idxFl % pDgInGr;
          } else {
            rem = idxFl % (pDgInGr * gc);
          }
          if (rem == 1) {
            pSb.append(pDcGrSp);
          }
        }
      }
    }
  }
}
