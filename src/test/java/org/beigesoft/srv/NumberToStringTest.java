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
 * <p>I18N tests.</p>
 *
 * @author Yury Demidenko
 */
public class NumberToStringTest {

  /**
   * <p>NumberToString service.</p>
   **/
  private final NumStr srvNumberToString = new NumStr();
  
  @Test
  public void tst1(){
    assertEquals("1 236 789,12", srvNumberToString.frmtNtz("1236789.120", ",", " ", 3));
    assertEquals("1 236 789,1", srvNumberToString.frmtNtz("1236789.10", ",", " ", 3));
    assertEquals("1,236,789.1", srvNumberToString.frmtNtz("1236789.10", ".", ",", 3));
    assertEquals("1 236 789", srvNumberToString.frmtNtz("1236789.0", ",", " ", 3));
    assertEquals("1", srvNumberToString.frmtNtz("1.0", ",", " ", 3));
    assertEquals("1 236 789,12", srvNumberToString.frmt("1236789.1278", ",", " ", 2, 3));
    assertEquals("1 236 789,1", srvNumberToString.frmt("1236789.1278", ",", " ", 1, 3));
    assertEquals("1 236 789", srvNumberToString.frmt("1236789.1278", ",", " ", 0, 3));
    assertEquals("1 236 789", srvNumberToString.frmt("1236789", ",", " ", 0, 3));
    assertEquals("1 236 789,00", srvNumberToString.frmt("1236789", ",", " ", 2, 3));
    assertEquals("1,236,789.12", srvNumberToString.frmt("1236789.1278", ".", ",", 2, 3));
    assertEquals("1 236 789,1278", srvNumberToString.frmt("1236789.1278", ",", " ", 4, 3));
    assertEquals("1,236,789.1278", srvNumberToString.frmt("1236789.1278", ".", ",", 4, 3));
    assertEquals("12,36,78,91,278", srvNumberToString.frmt("12367891278", ".", ",", 0, 2));
    assertEquals("-1 236 789,12", srvNumberToString.frmt("-1236789.1278", ",", " ", 2, 3));
    assertEquals("-1 236 789,1", srvNumberToString.frmt("-1236789.1278", ",", " ", 1, 3));
    assertEquals("-1 236 789", srvNumberToString.frmt("-1236789.1278", ",", " ", 0, 3));
    assertEquals("-1 236 789", srvNumberToString.frmt("-1236789", ",", " ", 0, 3));
    assertEquals("-1 236 789,00", srvNumberToString.frmt("-1236789", ",", " ", 2, 3));
    assertEquals("-1,236,789.12", srvNumberToString.frmt("-1236789.1278", ".", ",", 2, 3));
    assertEquals("-1 236 789,1278", srvNumberToString.frmt("-1236789.1278", ",", " ", 4, 3));
    assertEquals("-1,236,789.1278", srvNumberToString.frmt("-1236789.1278", ".", ",", 4, 3));
    assertEquals("-12,36,78,91,278", srvNumberToString.frmt("-12367891278", ".", ",", 0, 2));
    assertEquals("", srvNumberToString.frmt("", ",", " ", 4, 2));
    assertEquals("", srvNumberToString.frmt(null, ",", " ", 4, 2));
  }
  
}
