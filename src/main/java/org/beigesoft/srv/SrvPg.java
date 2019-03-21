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

import java.util.ArrayList;
import java.util.List;

import org.beigesoft.mdl.Page;


/**
 * <p>Page service for pagination.</p>
 *
 * @author Yury Demidenko
 */
public class SrvPg implements ISrvPg {

  /**
   * <p>Evaluate pages list.</p>
   * <pre>
   * example:
   * 1 ... 13 14 15 currPg-16 17 18 19 ... 42
   * currPg-1 2 3 4 ...  42
   * 1 currPg-2 3 4 5 ...  42
   * 1 ... 37 38 39 currPg-40 41 42
   * 1 2 3 currPg-4 5 6
   * </pre>
   * @param pCurPgNo current page #
   * @param pTotPgs total pages
   * @param pTailSz quantity of pages after and before current page
   * @return List<Page> list of pages
   **/
  @Override
  public final List<Page> evPgs(final int pCurPgNo,
    final int pTotPgs, final int pTailSz) {
    List<Page> pages = new ArrayList<Page>();
    boolean wasLeft3dots = false;
    boolean wasRight3dots = false;
    for (Integer i = 1; i <= pTotPgs; i++) {
      if (i == 1 || i == pTotPgs) {
        pages.add(new Page(i.toString(), pCurPgNo == i));
      } else if ((pCurPgNo - i) >= 0
          && (pCurPgNo - i) <= pTailSz) {
        pages.add(new Page(i.toString(), pCurPgNo == i));
      } else if ((pCurPgNo - i) < 0
          && (i - pCurPgNo) <= pTailSz) {
        pages.add(new Page(i.toString(), pCurPgNo == i));
      } else if ((pCurPgNo - i) > 0 && !wasLeft3dots) {
        wasLeft3dots = true;
        pages.add(new Page("...", false));
      } else if ((pCurPgNo - i) < 0 && !wasRight3dots) {
        wasRight3dots = true;
        pages.add(new Page("...", false));
      }
    }
    return pages;
  }

  /**
   * <p>Eval page count.</p>
   **/
  @Override
  public final int evPgCnt(final int pRowCount, final int pPgSz) {
    return (int) (pRowCount - 1) / pPgSz + 1;
  }
}
