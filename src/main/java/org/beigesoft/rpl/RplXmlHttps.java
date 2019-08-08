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

package org.beigesoft.rpl;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Date;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.net.URL;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.CookiePolicy;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.CookieHandler;
import javax.net.ssl.HttpsURLConnection;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdlp.DbInf;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.dlg.IMake;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.IUtlXml;

/**
 * <p>Service replicates database with XML messages trough HTTPS connection.
 * It's maybe assembly to make identical copy DB or for data replication/synchronization.
 * It's support HTTP-base and form authentication.
 * For non-public HTTPS (intranet) that didn't signed by global trusted CA
 * you have to add into Java trusted CA keystore your CA certificate.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class RplXmlHttps<RS> implements IReplicator {

  /**
   * <p>Replicators settings manager.</p>
   **/
  private ISetng setng;

  /**
   * <p>Database entities reader service.</p>
   **/
  private IRpStor rpStor;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>It prepares database before import (may be null),
   * e.g. for full database copy it will clear database.</p>
   **/
  private IMake dbBefore;

  /**
   * <p>It prepares database after import (may be null),
   * e.g. for Postgresql it will remake sequences.</p>
   **/
  private IMake dbAfter;

  /**
   * <p>XML service.</p>
   **/
  private IUtlXml utlXml;

  /**
   * <p>Filters Entity factory.</p>
   **/
  private IFctFltEnt fctFltEnts;

  /**
   * <p>It will clear current database then copy
   * data from another with XML messages trough HTTPS connection.</p>
   * @param pRvs request scoped vars
   * @throws Exception - an exception
   **/
  @Override
  public final void replicate(
    final Map<String, Object> pRvs) throws Exception {
    Writer wri = (Writer) pRvs.get("htmWri");
    try {
      //URL must be
      String urlSrcStr = "https://" + (String) pRvs.get("urlSrc");
      if (urlSrcStr == null || urlSrcStr.length() < 10) {
        throw new ExcCode(ExcCode.WRPR, "Where_is_no_urlSrc");
      }
      this.log.info(pRvs, getClass(), "URL source: " + urlSrcStr);
      URL url = new URL(urlSrcStr);
      String auMt = (String) pRvs.get("auMt");
      if ("base".equals(auMt)) {
        final String usr = (String) pRvs.get("usr");
        final String pwd = (String) pRvs.get("pwd");
        Authenticator.setDefault(new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(usr, pwd.toCharArray());
          }
        });
      } else if ("form".equals(auMt)) {
        CookieManager coMng = new CookieManager();
        CookieHandler.setDefault(coMng);
        coMng.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        reqCookies(pRvs);
        authForm(pRvs, coMng);
      }
      Map<String, Integer> clsCnts = makeJob(url, pRvs);
      if (wri != null) {
        String statusString = ", replication has been done.";
        wri.write("<h4>" + new Date().toString()
          + statusString + "</h4>");
        pRvs.put("statusString", new Date().toString() + ", "
          + RplXmlHttps.class.getSimpleName()
          + statusString);
        this.log.info(null, RplXmlHttps.class, statusString);
        wri.write("<table>");
        wri.write("<tr><th style=\"padding: 5px;\">Class</th><th style=\"padding: 5px;\">Total records</th></tr>");
        for (Map.Entry<String, Integer> entry : clsCnts.entrySet()) {
          wri.write("<tr>");
          wri.write("<td>" + entry.getKey() + "</td>");
          wri.write("<td>" + entry.getValue() + "</td>");
          wri.write("</tr>");
        }
        wri.write("</table>");
      }
    } catch (ExcCode ex) {
      if (wri != null) {
        wri.write(new Date().toString() + ", " + RplXmlHttps.class
          .getSimpleName() + ", " + ex.getMessage());
      }
      throw ex;
    }
    pRvs.remove("cooks");
  }

  /**
   * <p>It copy data from another with XML messages
   * through given HTTP connection.</p>
   * @param pUrl URL
   * @param pRvs request scoped vars
   * @return Map<String, Integer> affected Class - records count
   * @throws Exception - an exception
   **/
  public final Map<String, Integer> makeJob(final URL pUrl,
    final Map<String, Object> pRvs) throws Exception {
    String srDbId = (String) pRvs.get("srDbId");
    String maxRecsStr = (String) pRvs.get("maxRecs");
    if (maxRecsStr == null || maxRecsStr.length() == 0) {
      throw new ExcCode(ExcCode.WRPR, "WhereisnomaxRecs");
    }
    int maxRecs = Integer.parseInt(maxRecsStr);
    Map<String, Integer> clsCnts = new LinkedHashMap<String, Integer>();
    Integer clsCnt = 0;
    boolean isDbBefore = false;
    DbInf dbInf = this.rdb.getDbInf();
    for (Class<? extends IHasId<?>> cls : this.setng.lazClss()) {
      int entRecd = 0;
      int fstRec = 0;
      do {
        // HttpsURLConnection is single request connection
        HttpsURLConnection urlCn = (HttpsURLConnection) pUrl.openConnection();
        if (!pUrl.getHost().equals(urlCn.getURL().getHost())) {
          throw new ExcCode(ExcCode.WR,
            "You should sign-in in browser first!");
        }
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        try {
          urlCn.setDoOutput(true);
          urlCn.setRequestMethod("POST");
          String cooks = (String) pRvs.get("cooks");
          if (cooks != null) {
            urlCn.addRequestProperty("Cookie", cooks);
          }
          writer = new OutputStreamWriter(urlCn
            .getOutputStream(), Charset.forName("UTF-8").newEncoder());
          String fltEntNm = this.setng.lazClsStg(cls, IFltEnts.FLTENTSNM);
          String cond = "";
          if (fltEntNm != null) {
            IFltEnts fltEnts = this.fctFltEnts.laz(pRvs, fltEntNm);
            String cnd = fltEnts.makeWhe(pRvs, cls);
            if (cnd != null) {
              cond = " where " + cnd;
            }
          }
          cond += " limit " + maxRecs + " offset " + fstRec;
          String srDbIdStr = "";
          if (srDbId != null) {
            if (Integer.parseInt(srDbId) == dbInf.getDbId()) {
              throw new ExcCode(ExcCode.WRPR,
                "requested_database_must_be_different");
            }
            srDbIdStr = "&srDbId=" + srDbId;
          }
          writer.write("ent=" + cls.getCanonicalName() + "&cond=" + cond
            + "&dsDbVr=" + dbInf.getDbVr() + srDbIdStr);
          writer.write("&prc=" + pRvs.get("prc"));
          writer.flush();
          if (HttpsURLConnection.HTTP_OK == urlCn.getResponseCode()) {
            reader = new BufferedReader(new InputStreamReader(urlCn
                .getInputStream(), Charset.forName("UTF-8").newDecoder()));
            if (!this.utlXml.readUntilStart(reader, "message")) {
              throw new ExcCode(ExcCode.WR,
                "Wrong XML response without message tag!!!");
            }
            Map<String, String> msgAttrs = this.utlXml.readAttrs(pRvs, reader);
            String error = msgAttrs.get("error");
            if (error != null) {
              throw new ExcCode(ExcCode.WR,
                error);
            }
            String entCntStr = msgAttrs.get("entCnt");
            if (entCntStr == null) {
              throw new ExcCode(ExcCode.WR,
                "Wrong XML response without entCnt in message!!!");
            }
            entRecd = Integer.parseInt(entCntStr);
            if (entRecd > 0) {
              clsCnt += entRecd;
              this.log.info(null, RplXmlHttps.class,
                "Try to parse entities total: " + entRecd + " of "
                  + cls.getCanonicalName());
              if (!isDbBefore) {
                if (this.dbBefore != null) {
                  this.dbBefore.make(pRvs);
                }
                isDbBefore = true;
              }
              this.rpStor.storeFr(pRvs, reader);
              if (entRecd == maxRecs) {
                fstRec += maxRecs;
              } else {
                fstRec = 0;
                entRecd = 0;
              }
            } else {
              fstRec = 0;
            }
          } else {
            throw new ExcCode(ExcCode.WR,
              "Can't receive data!!! Response code=" + urlCn
                .getResponseCode());
          }
        } finally {
          if (reader != null) {
            try {
              reader.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          if (writer != null) {
            try {
              writer.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          urlCn.disconnect();
        }
      } while (entRecd > 0);
      clsCnts.put(cls.getCanonicalName(), clsCnt);
      clsCnt = 0;
    }
    if (this.dbAfter != null) {
      this.dbAfter.make(pRvs);
    }
    return clsCnts;
  }

  /**
   * <p>Connect to secure address with method GET to receive
   * authenticate cookies.</p>
   * @param pRvs request scoped vars
   * @throws Exception - an exception
   **/
  public final void reqCookies(
    final Map<String, Object> pRvs) throws Exception {
    String urlGetAuthCookStr = (String) pRvs.get("urlAuCo");
    URL urlAuCo = new URL(urlGetAuthCookStr);
    HttpsURLConnection urlCn = null;
    BufferedReader reader = null;
    try {
      urlCn = (HttpsURLConnection) urlAuCo.openConnection();
      urlCn.setRequestMethod("GET");
      urlCn.addRequestProperty("Connection", "keep-alive");
      if (HttpsURLConnection.HTTP_OK == urlCn.getResponseCode()) {
        reader = new BufferedReader(new InputStreamReader(urlCn
          .getInputStream(), Charset.forName("UTF-8").newDecoder()));
        while (reader.read() != -1) { //NOPMD
          //just read out
        }
      } else {
        throw new ExcCode(ExcCode.WR,
          "reqCookies Can't receive data!!! Response code="
            + urlCn.getResponseCode());
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (urlCn != null) {
        urlCn.disconnect();
      }
    }
  }

  /**
   * <p>It authenticates by post simulate form.</p>
   * @param pRvs request scoped vars
   * @param pCoMng CookieManager for form auth
   * @throws Exception - an exception
   **/
  public final void authForm(final Map<String, Object> pRvs,
      final CookieManager pCoMng) throws Exception {
    String auUrl = (String) pRvs.get("auUrl");
    String auUsr = (String) pRvs.get("auUsr");
    String auPwd = (String) pRvs.get("auPwd");
    String usr = (String) pRvs.get("usr");
    String pwd = (String) pRvs.get("pwd");
    URL url = new URL(auUrl);
    HttpsURLConnection urlCn = (HttpsURLConnection) url
      .openConnection();
    if (!url.getHost().equals(urlCn.getURL().getHost())) {
      throw new ExcCode(ExcCode.WR,
        "You should sign-in in browser first!");
    }
    OutputStreamWriter writer = null;
    BufferedReader reader = null;
    try {
      urlCn.setDoOutput(true);
      urlCn.setRequestMethod("POST");
      String paramStr = auUsr + "=" + usr + "&"
        + auPwd + "=" + pwd;
      StringBuffer cookiesSb = new StringBuffer();
      for (HttpCookie cookie : pCoMng.getCookieStore().getCookies()) {
        cookiesSb.append(cookie.getName() + "=" + cookie.getValue() + ";");
      }
      String cooks = cookiesSb.toString();
      pRvs.put("cooks", cooks);
      urlCn.addRequestProperty("Cookie", cooks);
      urlCn.addRequestProperty("Connection", "keep-alive");
      urlCn.addRequestProperty("Content-Type",
        "application/x-www-form-urlencoded");
      urlCn.addRequestProperty("Content-Length",
        String.valueOf(paramStr.length()));
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 6500);
      if (dbgSh) {
        getLog().debug(pRvs, RplXmlHttps.class,
          "Request before flush auth:");
        for (Map.Entry<String, List<String>> entry
          : urlCn.getRequestProperties().entrySet()) {
          this.log.debug(pRvs, RplXmlHttps.class,
            "  Request entry key: " + entry.getKey());
          for (String val : entry.getValue()) {
            this.log.debug(pRvs, RplXmlHttps.class,
              "   Request entry value: " + val);
          }
        }
      }
      writer = new OutputStreamWriter(urlCn
        .getOutputStream(), Charset.forName("UTF-8").newEncoder());
      writer.write(paramStr);
      writer.flush();
      reader = new BufferedReader(new InputStreamReader(urlCn
          .getInputStream(), Charset.forName("UTF-8").newDecoder()));
      while (reader.read() != -1) { //NOPMD
        //just read out
      }
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      urlCn.disconnect();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for setng.</p>
   * @return ISetng
   **/
  public final ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final void setSetng(final ISetng pSetng) {
    this.setng = pSetng;
  }

  /**
   * <p>Getter for rpStor.</p>
   * @return IRpStor
   **/
  public final IRpStor getRpStor() {
    return this.rpStor;
  }

  /**
   * <p>Setter for rpStor.</p>
   * @param pRpStor reference
   **/
  public final void setRpStor(final IRpStor pRpStor) {
    this.rpStor = pRpStor;
  }

  /**
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for dbBefore.</p>
   * @return IMake
   **/
  public final IMake getDbBefore() {
    return this.dbBefore;
  }

  /**
   * <p>Setter for dbBefore.</p>
   * @param pDbBefore reference
   **/
  public final void setDbBefore(final IMake pDbBefore) {
    this.dbBefore = pDbBefore;
  }

  /**
   * <p>Getter for dbAfter.</p>
   * @return IMake
   **/
  public final IMake getDbAfter() {
    return this.dbAfter;
  }

  /**
   * <p>Setter for dbAfter.</p>
   * @param pDbAfter reference
   **/
  public final void setDbAfter(final IMake pDbAfter) {
    this.dbAfter = pDbAfter;
  }

  /**
   * <p>Getter for utlXml.</p>
   * @return IUtlXml
   **/
  public final IUtlXml getUtlXml() {
    return this.utlXml;
  }

  /**
   * <p>Setter for utlXml.</p>
   * @param pUtlXml reference
   **/
  public final void setUtlXml(final IUtlXml pUtlXml) {
    this.utlXml = pUtlXml;
  }

  /**
   * <p>Getter for fctFltEnts.</p>
   * @return IFctFltEnt
   **/
  public final IFctFltEnt getFctFltEnts() {
    return this.fctFltEnts;
  }

  /**
   * <p>Setter for fctFltEnts.</p>
   * @param pFctFltEnts reference
   **/
  public final void setFctFltEnts(final IFctFltEnt pFctFltEnts) {
    this.fctFltEnts = pFctFltEnts;
  }
}
