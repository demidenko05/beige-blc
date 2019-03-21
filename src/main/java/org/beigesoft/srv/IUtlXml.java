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

import java.io.Reader;
import java.util.Map;

/**
 * <p>Service that escape XML.</p>
 *
 * @author Yury Demidenko
 */
public interface IUtlXml {

  /**
   * <p>Escape XML for given string.</p>
   * @param pSource source
   * @return escaped string
   * @throws Exception - an exception
   **/
  String escStr(String pSource) throws Exception;

  /**
   * <p>Escape XML for given char.</p>
   * @param pChar char
   * @return escaped string
   * @throws Exception - an exception
   **/
  String escChr(char pChar) throws Exception;

  /**
   * <p>Unescape XML for given string.</p>
   * @param pSource source
   * @return unescaped string
   * @throws Exception - an exception
   **/
  String unescStr(String pSource) throws Exception;

  /**
   * <p>
   * Unescape XML for given string.
   * </p>
   * @param pEscaped Escaped
   * @return unescaped char
   * @throws Exception - an exception
   **/
  char unescChr(String pEscaped) throws Exception;

  /**
   * <p>Read attributes from stream. Start the XML element
   * must be read out.</p>
   * @param pReader reader.
   * @param pRqVs request scoped vars
   * @return attributes map
   * @throws Exception - an exception
   **/
  Map<String, String> readAttrs(Reader pReader,
    Map<String, Object> pRqVs) throws Exception;


  /**
   * <p>Read stream until start given element e.g. &lt;message.</p>
   * @param pReader reader.
   * @param pElement element
   * @return true if start element is happen, false if end of stream
   * @throws Exception - an exception
   **/
  boolean readUntilStart(Reader pReader,
    String pElement) throws Exception;
}
