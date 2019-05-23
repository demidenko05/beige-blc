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

  //request scoped:
  /**
   * <p>Params map.</p>
   **/
  private final Map<String, String> paramsMp = new HashMap<String, String>();

  /**
   * <p>Attributes map.</p>
   **/
  private final Map<String, Object> attrs = new HashMap<String, Object>();

  /**
   * <p>Param map.</p>
   **/
  private final Map<String, String[]> paramMp = new HashMap<String, String[]>();

  //"global" scoped:
  /**
   * <p>Cookies map.</p>
   **/
  private final Map<String, String> cookies = new HashMap<String, String>();

  /**
   * <p>Context attributes map.</p>
   **/
  private final Map<String, Object> ctxAttrs = new HashMap<String, Object>();

  /**
   * <p>User name.</p>
   **/
  private String usrNm;

  /**
   * <p>Context path.</p>
   **/
  private String ctxPth;

  /**
   * <p>Preferred locale.</p>
   **/
  private Locale usLoc = Locale.getDefault();

  private StringBuffer reqUrl;

  /**
   * <p>remote host.</p>
   **/
  private String remHost;

  /**
   * <p>remote address.</p>
   **/
  private String remAddr;

  /**
   * <p>remote user.</p>
   **/
  private String remUsr;

  /**
   * <p>remote port.</p>
   **/
  private int remPort;

  /**
   * <p>Get attribute.</p>
   * @param pNm attribute name
   * @return attribute
   **/
  @Override
  public final Object getCtxAttr(final String pNm) {
    return this.ctxAttrs.get(pNm);
  }

  /**
   * <p>Set attribute.</p>
   * @param pNm attribute name
   * @param pAttr attribute
   **/
  @Override
  public final void setCtxAttr(final String pNm, final Object pAttr) {
    this.ctxAttrs.put(pNm, pAttr);
  }

  /**
   * <p>Removes attribute.</p>
   * @param pNm attribute name
   **/
  @Override
  public final void remCtxAttr(final String pNm) {
    this.ctxAttrs.remove(pNm);
  }

  /**
   * <p>Get attribute.</p>
   * @param pNm attribute name
   * @return attribute
   **/
  @Override
  public final Object getAttr(final String pNm) {
    return this.attrs.get(pNm);
  }

  /**
   * <p>Returns context path.</p>
   * @return context path
   **/
  @Override
  public final String getCtxPth() {
    return this.ctxPth;
  }

  /**
   * <p>Set attribute.</p>
   * @param pNm attribute name
   * @param pAttr attribute
   **/
  @Override
  public final void setAttr(final String pNm, final Object pAttr) {
    this.attrs.put(pNm, pAttr);
  }

  /**
   * <p>Removes attribute.</p>
   * @param pNm attribute name
   **/
  @Override
  public final void remAttr(final String pNm) {
    this.attrs.remove(pNm);
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
    return this.paramMp.get(pPrNm);
  }

  /**
   * <p>Getter of user name.</p>
   * @return User name if he/she logged
   **/
  @Override
  public final String getUsrNm() {
    return this.usrNm;
  }

  /**
   * <p>Get cookie value by name.</p>
   * @param pNm Name
   * @return cookie value or null
   **/
  @Override
  public final String getCookVl(final String pNm) {
    return this.cookies.get(pNm);
  }

  /**
   * <p>Set(add/change) cookie value.</p>
   * @param pNm Name
   * @param pVal Value
   **/
  @Override
  public final void setCookVl(final String pNm, final String pVal) {
   this.cookies.put(pNm, pVal) ;
  }

  /**
   * <p>Returns preferred clients locale.</p>
   * @return locale
   **/
  @Override
  public final Locale getLocale() {
    return this.usLoc;
  }

  /**
   * <p>Returns client's request URL without parameters.</p>
   * @return URL
   **/
  @Override
  public final StringBuffer getReqUrl() {
    return this.reqUrl;
  }

  /**
   * <p>Returns remote host.</p>
   * @return remote host
   **/
  @Override
  public final String getRemHost() {
    return this.remHost;
  }

  /**
   * <p>Returns remote address.</p>
   * @return remote address
   **/
  @Override
  public final String getRemAddr() {
    return this.remAddr;
  }

  /**
   * <p>Returns remote user.</p>
   * @return remote user
   **/
  @Override
  public final String getRemUsr() {
    return this.remUsr;
  }

  /**
   * <p>Returns remote port.</p>
   * @return remote port
   **/
  @Override
  public final int getRemPort() {
    return this.remPort;
  }

  /**
   * <p>Getter for paramMp.</p>
   * @return Map<String, String[]>
   **/
  @Override
  public final Map<String, String[]> getParamMap() {
    return this.paramMp;
  }

  /**
   * <p>Initialize like new request, i.e cookie, port, etc are
   * still the same, only request data and attributes will be cleared.</p>
   **/
  public final void iniNewRq() {
    this.attrs.clear();
    this.paramMp.clear();
    this.paramsMp.clear();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for paramsMp.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getParamsMp() {
    return this.paramsMp;
  }

  /**
   * <p>Getter for cookies.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getCookies() {
    return this.cookies;
  }

  /**
   * <p>Getter for attrs.</p>
   * @return Map<String, Object>
   **/
  public final Map<String, Object> getAttrs() {
    return this.attrs;
  }

  /**
   * <p>Getter for ctxAttrs.</p>
   * @return Map<String, Object>
   **/
  public final Map<String, Object> getCtxAttrs() {
    return this.ctxAttrs;
  }

  /**
   * <p>Setter for usrNm.</p>
   * @param pUsrNm reference
   **/
  public final void setUsrNm(final String pUsrNm) {
    this.usrNm = pUsrNm;
  }

  /**
   * <p>Setter for ctxPth.</p>
   * @param pCtxPth reference
   **/
  public final void setCtxPth(final String pCtxPth) {
    this.ctxPth = pCtxPth;
  }

  /**
   * <p>Setter for usLoc.</p>
   * @param pUsLoc reference
   **/
  public final void setUsLoc(final Locale pUsLoc) {
    this.usLoc = pUsLoc;
  }

  /**
   * <p>Setter for reqUrl.</p>
   * @param pReqUrl reference
   **/
  public final void setReqUrl(final StringBuffer pReqUrl) {
    this.reqUrl = pReqUrl;
  }

  /**
   * <p>Setter for remHost.</p>
   * @param pRemHost reference
   **/
  public final void setRemHost(final String pRemHost) {
    this.remHost = pRemHost;
  }

  /**
   * <p>Setter for remAddr.</p>
   * @param pRemAddr reference
   **/
  public final void setRemAddr(final String pRemAddr) {
    this.remAddr = pRemAddr;
  }

  /**
   * <p>Setter for remUsr.</p>
   * @param pRemUsr reference
   **/
  public final void setRemUsr(final String pRemUsr) {
    this.remUsr = pRemUsr;
  }

  /**
   * <p>Setter for remPort.</p>
   * @param pRemPort reference
   **/
  public final void setRemPort(final int pRemPort) {
    this.remPort = pRemPort;
  }
}
