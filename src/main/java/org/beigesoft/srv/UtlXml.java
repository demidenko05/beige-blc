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
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;

/**
 * <p>Service that escape XML.
 * For improving performance unescStr should invoked
 * explicitly when it's need (for field like itsName or description).</p>
 *
 * @author Yury Demidenko
 */
public class UtlXml implements IUtlXml {

  /**
   * <p>
   * Escape XML for given string.
   * </p>
   * @param pSource source
   * @return escaped string
   * @throws Exception - an exception
   **/
  @Override
  public final String escStr(final String pSource) throws Exception {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < pSource.length(); i++) {
      char ch = pSource.charAt(i);
      sb.append(escChr(ch));
    }
    return sb.toString();
  }

  /**
   * <p>
   * Escape XML for given char.
   * </p>
   * @param pChar char
   * @return escaped string
   * @throws Exception - an exception
   **/
  @Override
  public final String escChr(final char pChar) throws Exception {
    if (pChar == '<') {
      return "&lt;";
    } else if (pChar == '>') {
      return "&gt;";
    } else if (pChar == '"') {
      return "&quot;";
    } else if (pChar == '\'') {
      return "&apos;";
    } else if (pChar == '&') {
      return "&amp;";
    }
    return String.valueOf(pChar);
  }

  /**
   * <p>
   * Unescape XML for given string.
   * For improving performance unescStr should invoked
   * explicitly when it's need (for field like itsName or description).
   * </p>
   * @param pSource source
   * @return unescaped string
   * @throws Exception - an exception
   **/
  @Override
  public final String unescStr(final String pSource) throws Exception {
    StringBuffer sb = new StringBuffer();
    StringBuffer sbEsc = new StringBuffer();
    boolean isStartEsc = false;
    for (int i = 0; i < pSource.length(); i++) {
      char ch = pSource.charAt(i);
      if (!isStartEsc && ch == '&') {
        isStartEsc = true;
        sbEsc.append(ch);
        continue;
      } else if (isStartEsc) {
        sbEsc.append(ch);
        if (ch == ';') {
          sb.append(unescChr(sbEsc.toString()));
          sbEsc.delete(0, sbEsc.length());
          isStartEsc = false;
        }
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  /**
   * <p>
   * Unescape XML for given string.
   * </p>
   * @param pEscaped Escaped
   * @return unescaped char
   * @throws Exception - an exception
   **/
  @Override
  public final char unescChr(final String pEscaped) throws Exception {
    if ("&lt;".equals(pEscaped)) {
      return '<';
    } else if ("&gt;".equals(pEscaped)) {
      return '>';
    } else if ("&quot;".equals(pEscaped)) {
      return '"';
    } else if ("&apos;".equals(pEscaped)) {
      return '\'';
    } else if ("&amp;".equals(pEscaped)) {
      return '&';
    }
    throw new ExcCode(ExcCode.WRPR,
      "There is no escape char for " + pEscaped);
  }

  /**
   * <p>
   * Read attributes from stream. Start the XML element
   * must be read out.
   * </p>
   * @param pReader reader.
   * @param pAddParam additional params
   * @return attributes map
   * @throws Exception - an exception
   **/
  @Override
  public final Map<String, String> readAttrs(final Reader pReader,
    final Map<String, Object> pAddParam) throws Exception {
    Map<String, String> attributesMap = new HashMap<String, String>();
    StringBuffer sb = new StringBuffer();
    int chi;
    while ((chi = pReader.read()) != -1) {
      char ch = (char) chi;
      if (ch == '>') {
        break;
      }
      switch (ch) {
        case '\\':
          sb.append("\\");
          break;
        case '"':
          sb.append("\"");
          break;
        case '\n':
          sb.append("\n");
          break;
        case '\r':
          sb.append("\r");
          break;
        case '\t':
          sb.append("\t");
          break;
        default:
          sb.append(ch);
          break;
      }
      evalAttributes(sb, attributesMap);
    }
    return attributesMap;
  }

  /**
   * <p>Try to eval content of string buffer if it's an attribute
   * with value then fill map and clear buffer.
   * For improving performance unescStr should invoked
   * explicitly when it's need (for field like itsName or description).</p>
   * @param pSb StringBuffer
   * @param pAttributesMap Attributes Map
   * @throws Exception - an exception
   **/
  public final void evalAttributes(final StringBuffer pSb,
    final Map<String, String> pAttributesMap) throws Exception {
    String str = pSb.toString().trim();
    if (str.length() > 3 //minimum is a=""
      && str.endsWith("\"") && str.indexOf("\"") != str.length() - 1) {
      int equalsIdx = str.indexOf("=");
      if (equalsIdx == -1) {
        throw new ExcCode(ExcCode.WR, "There is no equals character in " + str);
      }
      String attrName = str.substring(0, equalsIdx);
      String attrVal = str.substring(str.indexOf("\"") + 1, str.length() - 1);
      pAttributesMap.put(attrName, attrVal);
      pSb.delete(0, pSb.length());
    }
  }

  /**
   * <p>
   * Read stream until start given element e.g. &lt;message.
   * </p>
   * @param pReader reader.
   * @param pElement element
   * @return true if start element is happen, false if end of stream
   * @throws Exception - an exception
   **/
  @Override
  public final boolean readUntilStart(final Reader pReader,
    final String pElement) throws Exception {
    StringBuffer sb = new StringBuffer();
    int chi;
    boolean isLtOccured = false;
    while ((chi = pReader.read()) != -1) {
      char ch = (char) chi;
      if (isLtOccured) {
        if (ch ==  '>' || ch == '\n' || ch == '\\' || ch == '"' || ch == '\r'
          || ch == '\t') {
          isLtOccured = false;
          sb.delete(0, sb.length());
          continue;
        }
        sb.append(ch);
        String readedStr = sb.toString();
        if (readedStr.length() > pElement.length()) {
          isLtOccured = false;
          sb.delete(0, sb.length());
          continue;
        }
        if (pElement.equals(readedStr)) {
          return true;
        }
      } else if (ch == '<') {
        isLtOccured = true;
      }
    }
    return false;
  }
}
