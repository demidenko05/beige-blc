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

package org.beigesoft.prp;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.URL;

/**
 * <p>Simple helper to working with XML properties.</p>
 *
 * @author Yury Demidenko
 */
public class UtlPrp {

  // Utils:
  /**
   * <p>Evaluate string array(include non-unique) properties
   * from string with comma delimeter
   * and removed new lines and trailing spaces.</p>
   * @param pSrc string
   * @return String[] array
   **/
  public final String[] evPrpStrArr(final String pSrc) {
    List<String> resLst = evPrpStrLst(pSrc);
    return resLst.toArray(new String[resLst.size()]);
  }

  /**
   * <p>Evaluate string set properties
   * from string with comma delimiter
   * and removed new lines and trailing spaces.</p>
   * @param pSrc string
   * @return LinkedHashSet<String> properties set
   **/
  public final LinkedHashSet<String> evPrpStrSet(final String pSrc) {
    String srcCor = pSrc.replace("\n", "");
    LinkedHashSet<String> resSet = new LinkedHashSet<String>();
    for (String str : srcCor.split(",")) {
      resSet.add(str.trim());
    }
    return resSet;
  }

  /**
   * <p>Evaluate string list(include non-unique) properties
   * from string with comma delimiter
   * and removed new lines and trailing spaces.</p>
   * @param pSrc string
   * @return List<String> properties set
   **/
  public final List<String> evPrpStrLst(final String pSrc) {
    String srcCor = pSrc.replace("\n", "");
    List<String> resLst = new ArrayList<String>();
    for (String str : srcCor.split(",")) {
      resLst.add(str.trim());
    }
    return resLst;
  }

  /**
   * <p>Evaluate null if value is empty string "".</p>
   * @param pPrps properties
   * @param pPrpNm properties
   * @return String string or NULL
   **/
  public final String evPrpVl(final LnkPrps pPrps,
    final String pPrpNm) {
    String result = pPrps.getProperty(pPrpNm);
    if ("".equals(result)) {
      return null;
    }
    return result;
  }

  /**
   * <p>Load properties from XML file.</p>
   * @param pFlNm file name
   * @return props properties or null
   * @throws Exception - an exception
   **/
  public final LnkPrps load(final String pFlNm) throws Exception {
    LnkPrps props = null;
    URL urlStXml = UtlPrp.class.getResource(pFlNm);
    if (urlStXml != null) {
      props = new LnkPrps();
      InputStream inputStream = null;
      try {
        inputStream = UtlPrp.class.getResourceAsStream(pFlNm);
        props.loadFromXML(inputStream);
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return props;
  }
}
