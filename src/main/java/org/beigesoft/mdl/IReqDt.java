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

package org.beigesoft.mdl;

import java.util.Map;
import java.util.Locale;
import org.beigesoft.hld.IAttrs;

/**
 * <p>Abstraction of request data (get/set param, attribute)
 * that usually wrap HttpServletRequest/HttpServletRresponse.</p>
 *
 * @author Yury Demidenko
 */
public interface IReqDt extends IAttrs {

  /**
   * <p>Getter for parameter.</p>
   * @param pPrNm Param Name
   * @return parameter
   **/
  String getParam(String pPrNm);

  /**
   * <p>Getter for parameter with multiply values.</p>
   * @param pPrNm Param Name
   * @return parameter values
   **/
  String[] getParamVls(String pPrNm);


  /**
   * <p>Getter for Params Map.</p>
   * @return parameters map
   **/
  Map<String, String[]> getParamMap();

  /**
   * <p>Getter of user name.</p>
   * @return User name if he/she logged
   **/
  String getUsrNm();

  /**
   * <p>Get cookie value by name.</p>
   * @param pName Name
   * @return cookie value or null
   **/
  String getCookVls(String pName);

  /**
   * <p>Set(add/change) cookie value.</p>
   * @param pName Name
   * @param pValue Value
   **/
  void setCookVl(String pName, String pValue);

  /**
   * <p>Returns preferred clients locale.</p>
   * @return locale
   **/
  Locale getLocale();

  /**
   * <p>Returns client's request URL without parameters.</p>
   * @return URL
   **/
  StringBuffer getReqUrl();

  /**
   * <p>Returns remote host.</p>
   * @return remote host
   **/
  String getRemHost();

  /**
   * <p>Returns remote address.</p>
   * @return remote address
   **/
  String getRemAddr();

  /**
   * <p>Returns remote user.</p>
   * @return remote user
   **/
  String getRemUsr();

  /**
   * <p>Returns remote port.</p>
   * @return remote port
   **/
  int getRemPort();
}
