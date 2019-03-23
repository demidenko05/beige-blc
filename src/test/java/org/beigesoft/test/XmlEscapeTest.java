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

package org.beigesoft.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.beigesoft.srv.UtlXml;

/**
 * <p>Service XML Escape Test.
 * </p>
 *
 * @author Yury Demidenko
 */
public class XmlEscapeTest {
  
  UtlXml utilXml = new UtlXml();

  @Test
  public void test1() throws Exception {
    String strXmlUnescaped1 = "a=\"b-2\" & c > 1 and b<=5 'j' ";
    String strXmlEscaped1 = "a=&quot;b-2&quot; &amp; c &gt; 1 and b&lt;=5 &apos;j&apos; ";
    System.out.println(utilXml.escStr(strXmlUnescaped1));
    System.out.println(utilXml.unescStr(strXmlEscaped1));
    assertEquals(utilXml.escStr(strXmlUnescaped1), strXmlEscaped1); 
    assertEquals(utilXml.unescStr(strXmlEscaped1), strXmlUnescaped1); 
  }
}
