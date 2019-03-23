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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * <p>Pagination tests.</p>
 *
 * @author Yury Demidenko
 */
public class PagenationalTest {

  /**
   * <p>Page service.</p>
   **/
  private final SrvPg srvPage = new SrvPg();
  
  @Test
  public void tst1(){
    assertEquals(1, srvPage .evPgCnt(14, 15));
    assertEquals(1, srvPage .evPgCnt(0, 15));
    assertEquals(1, srvPage .evPgCnt(15, 15));
    assertEquals(2, srvPage .evPgCnt(16, 15));
    //must be:
    //1 ... 11 12 13 14pg 15
    assertEquals(7, srvPage .evPgs(14, 15, 3).size());
    assertEquals("1", srvPage.evPgs(14, 15, 3).get(0).getVal());
    assertEquals("...", srvPage.evPgs(14, 15, 3).get(1).getVal());
    assertEquals("11", srvPage.evPgs(14, 15, 3).get(2).getVal());
    assertEquals(false, srvPage.evPgs(14, 15, 3).get(2).getCur());
    assertEquals("12", srvPage.evPgs(14, 15, 3).get(3).getVal());
    assertEquals("13", srvPage.evPgs(14, 15, 3).get(4).getVal());
    assertEquals("14", srvPage.evPgs(14, 15, 3).get(5).getVal());
    assertEquals(true, srvPage.evPgs(14, 15, 3).get(5).getCur());
    assertEquals("15", srvPage.evPgs(14, 15, 3).get(6).getVal());
  }
  
  @Test
  public void tst2(){
    //must be:
    //1pg 2 3
    assertEquals(3, srvPage.evPgs(1, 3, 3).size());
    assertEquals("1", srvPage.evPgs(1, 3, 3).get(0).getVal());
    assertEquals(true, srvPage.evPgs(1, 3, 3).get(0).getCur());
    assertEquals("2", srvPage.evPgs(1, 3, 3).get(1).getVal());
    assertEquals(false, srvPage.evPgs(1, 3, 3).get(1).getCur());
    assertEquals("3", srvPage.evPgs(1, 3, 3).get(2).getVal());
  }
}
