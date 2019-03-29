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
import java.util.HashMap;
import java.util.Locale;
import org.beigesoft.hld.IAttrs;

/**
 * <p>Fake request data for tests.</p>
 *
 * @author Yury Demidenko
 */
public class ReqDtTst implements IReqDt {

  /**
   * <p>Params map.</p>
   **/
  private final Map<String, String> paramsMp = new HashMap<String, String>();

  /**
   * <p>Get attribute.</p>
   * @param pName attribute name
   * @return attribute
   **/
  @Override
  public final Object getAttr(final String pName) {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Set attribute.</p>
   * @param pName attribute name
   * @param pAttr attribute
   **/
  @Override
  public final void setAttr(final String pName, final Object pAttr) {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Getter for parameter.</p>
   * @param pPrNm Param Name
   * @return parameter
   **/
  @Override
  public final String getParam(final String pPrNm) {
    return this.paramsMp.get(pPrNm);
  }

  /**
   * <p>Getter for parameter with multiply values.</p>
   * @param pPrNm Param Name
   * @return parameter values
   **/
  @Override
  public final String[] getParamVls(final String pPrNm) {
    throw new RuntimeException("NEI");
  }


  /**
   * <p>Getter for Params Map.</p>
   * @return parameters map
   **/
  @Override
  public final Map<String, String[]> getParamMap() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Getter of user name.</p>
   * @return User name if he/she logged
   **/
  @Override
  public final String getUsrNm() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Get cookie value by name.</p>
   * @param pName Name
   * @return cookie value or null
   **/
  @Override
  public final String getCookVl(final String pName) {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Set(add/change) cookie value.</p>
   * @param pName Name
   * @param pValue Value
   **/
  @Override
  public final void setCookVl(final String pName, final String pValue) {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Returns preferred clients locale.</p>
   * @return locale
   **/
  @Override
  public final Locale getLocale() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Returns client's request URL without parameters.</p>
   * @return URL
   **/
  @Override
  public final StringBuffer getReqUrl() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Returns remote host.</p>
   * @return remote host
   **/
  @Override
  public final String getRemHost() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Returns remote address.</p>
   * @return remote address
   **/
  @Override
  public final String getRemAddr() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Returns remote user.</p>
   * @return remote user
   **/
  @Override
  public final String getRemUsr() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Returns remote port.</p>
   * @return remote port
   **/
  @Override
  public final int getRemPort() {
    throw new RuntimeException("NEI");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for paramsMp.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getParamsMp() {
    return this.paramsMp;
  }
}
