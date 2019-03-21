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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.Reader;

import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;


/**
 * <p>Basic reading service that reads next line from CSV file.
 * Any delimiter can be only single char.</p>
 *
 * @author Yury Demidenko
 */
public class CsvRdr implements ICsvRdr {

  /**
   * <p>Reads next line from CSV file as list of Strings,
   * returns null if no more rows left.
   * Any delimiter can be only single char.</p>
   * @param pRqVs request scoped vars
   * @param pReader Reader
   * @param pCsvMth Csv Method
   * @return List<String> list of columns values or null if no more rows left
   * @throws Exception an Exception
   **/
  @Override
  public final List<String> readNext(final Map<String, Object> pRqVs,
    final Reader pReader, final CsvMth pCsvMth) throws Exception {
    List<String> res = null;
    StringBuffer sb = new StringBuffer();
    char clSp = pCsvMth.getClSp().charAt(0);
    CsvCl csCl = null;
    Character txDlm = null;
    Character txDlmWas = null;
    for (CsvCl cc : pCsvMth.getClns()) {
      if (cc.getSrIdx() == 1) {
        csCl = cc;
        if (csCl.getTxDlm() != null) {
          txDlm = csCl.getTxDlm().charAt(0);
        }
        break;
      }
    }
    int colIdx = 1;
    int txDlmOccr = 0;
    int chi;
    int chCnt = 0;
    char cr  = (char) 0x0D;
    char lf  = (char) 0x0A;
    while ((chi = pReader.read()) != -1) {
      char ch = (char) chi;
      chCnt++;
      if (ch == lf || ch == cr || txDlm != null && ch == txDlm
        || txDlmOccr != 1 && ch == clSp) {
        if (chCnt == 1 && (ch == lf || ch == cr)) { //CRLF - > LF
          continue;
        }
        if (txDlmWas != null && ch != txDlmWas && txDlmOccr == 2) {
          //second delimiter after text delimiter
          txDlmOccr = 0;
          continue;
        }
        if (txDlm != null && ch == txDlm) {
          txDlmOccr++;
        }
        if (txDlmOccr != 1) {
          if (csCl != null) {
            if (res == null) {
              res = new ArrayList<String>();
            }
            if (sb.length() > 0) {
              res.add(sb.toString());
            } else {
              res.add(null);
            }
          }
          if (ch == lf || ch == cr) {
            //the end of line
            break;
          }
          if (sb.length() > 0) {
            sb.delete(0, sb.length());
          }
          colIdx++;
          csCl = null;
          txDlmWas = txDlm;
          txDlm = null;
          for (CsvCl cc : pCsvMth.getClns()) {
            if (cc.getSrIdx() == colIdx) {
              csCl = cc;
              if (csCl.getTxDlm() != null) {
                txDlm = csCl.getTxDlm().charAt(0);
              }
              break;
            }
          }
        }
      } else {
        sb.append(ch);
      }
    }
    return res;
  }
}
