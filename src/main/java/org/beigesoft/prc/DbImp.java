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

package org.beigesoft.prc;

import java.util.Map;
import java.io.PrintWriter;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.rpl.IReplicator;

/**
 * <p>Imports full database from source with WEB-service.</p>
 *
 * @author Yury Demidenko
 */
public class DbImp implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Replicator.</p>
   **/
  private IReplicator repl;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    PrintWriter htmWri = (PrintWriter) pRvs.get("htmWri");
    String urlSrc = "https://" + pRqDt.getParam("urlSrc");
    String usr = pRqDt.getParam("usr");
    pRvs.put("srDbId", pRqDt.getParam("srDbId"));
    pRvs.put("maxRecs", pRqDt.getParam("maxRecs"));
    if (usr != null) {
      pRvs.put("pwd", pRqDt.getParam("pwd"));
      pRvs.put("usr", usr);
      pRvs.put("auMt", "form");
      String urlBase = urlSrc.substring(0, urlSrc.indexOf("adm") - 1);
      pRvs.put("auUrl", urlBase + "/adm/j_security_check");
      pRvs.put("urlAuCo", urlBase + "/adm/ntr");
      pRvs.put("auUsr", "j_username");
      pRvs.put("auPwd", "j_password");
    }
    htmWri.println("<!DOCTYPE html>");
    htmWri.println("<html>");
    htmWri.println("<head>");
    htmWri.println("<meta charset=\"UTF-8\"/>");
    htmWri.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\"/>");
    htmWri.println("<link rel=\"shortcut icon\" href=\"../img/favicon.png\"/>");
    htmWri.println("<link rel=\"stylesheet\" href=\"../css/bsCmn.css\"/>");
    htmWri.println("<title>Replication data</title>");
    htmWri.println("</head>");
    htmWri.println("<body style=\"padding: 20px;\">");
    htmWri.println("<a href=\"../\" class=\"btn\">Home</a>");
    htmWri.println("<div style=\"text-align: center;\">");
    htmWri.println("<h3>Replication data from " + urlSrc + "</h3>");
    htmWri.println("</div>");
    htmWri.println("<div>");
    this.repl.replicate(pRvs);
    htmWri.println("</div>");
    htmWri.println("</body>");
    htmWri.println("</html>");
  }

  //Simple getters and setters:
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
   * <p>Getter for repl.</p>
   * @return IReplicator
   **/
  public final IReplicator getRepl() {
    return this.repl;
  }

  /**
   * <p>Setter for repl.</p>
   * @param pRepl reference
   **/
  public final void setRepl(final IReplicator pRepl) {
    this.repl = pRepl;
  }
}
