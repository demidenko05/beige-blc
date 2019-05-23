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
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdlp.EmMsg;
import org.beigesoft.mdlp.EmRcp;
import org.beigesoft.mdlp.EmAtch;
import org.beigesoft.mdlp.EmInt;
import org.beigesoft.mdlp.EmStr;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.IEmSnd;

/**
 * <p>Service that saves email message into DB and sends it with email
 * sender if it's required.</p>
 *
 * @author Yury Demidenko
 */
public class PrcEmMsgSv implements IPrcEnt<EmMsg, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Email sender.</p>
   **/
  private IEmSnd emSnd;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final EmMsg process(final Map<String, Object> pRvs, final EmMsg pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pRqDt.getParam("frLs") != null) { //from list
       this.orm.refrEnt(pRvs, vs, pEnt);
    }
    if (!pEnt.getIsNew() && !pEnt.getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.WRPR, "can_not_change_foreign_src");
    }
    if (pEnt.getIsNew()) {
      this.orm.insert(pRvs, vs, pEnt);
      pRvs.put("msgSuc", "insert_ok");
      pEnt.setIsNew(false);
    } else {
      if (pEnt.getSent()) {
        throw new ExcCode(ExcCode.WRPR, "can_not_change_sent_email");
      }
      if (pRqDt.getParam("ndSnd") != null) {
        //ORM refresh:
        if (pEnt.getEmCn() == null) {
          throw new ExcCode(ExcCode.WRPR, "choose_email_con");
        }
        String[] ndFds = new String[] {"eml"};
        vs.put("EmRcpndFds", ndFds);
        pEnt.setRcps(this.orm.retLstCnd(pRvs, vs, EmRcp.class,
          "where OWNR=" + pEnt.getIid()));
        vs.clear();
        if (pEnt.getRcps().size() == 0) {
          throw new ExcCode(ExcCode.WRPR, "choose_recipient");
        }
        ndFds = new String[] {"nme", "pth"};
        vs.put("EmAtchndFds", ndFds);
        pEnt.setAtchs(this.orm.retLstCnd(pRvs, vs, EmAtch.class,
          "where OWNR=" + pEnt.getIid()));
        vs.clear();
        this.orm.refrEnt(pRvs, vs, pEnt.getEmCn());
        ndFds = new String[] {"prNm", "prVl"};
        vs.put("EmIntndFds", ndFds);
        pEnt.getEmCn().setIntPrps(this.orm.retLstCnd(pRvs, vs, EmInt.class,
          "where OWNR=" + pEnt.getIid()));
        vs.clear();
        vs.put("EmStrndFds", ndFds);
        pEnt.getEmCn().setStrPrps(this.orm.retLstCnd(pRvs, vs, EmStr.class,
          "where OWNR=" + pEnt.getIid()));
        vs.clear();
        this.emSnd.send(pRvs, pEnt);
        pEnt.setSent(true);
        pRvs.put("msgSuc", "send_ok");
      } else {
        pRvs.put("msgSuc", "update_ok");
      }
      this.orm.update(pRvs, vs, pEnt);
    }
    return pEnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for emSnd.</p>
   * @return IEmSnd
   **/
  public final IEmSnd getEmSnd() {
    return this.emSnd;
  }

  /**
   * <p>Setter for emSnd.</p>
   * @param pEmSnd reference
   **/
  public final void setEmSnd(final IEmSnd pEmSnd) {
    this.emSnd = pEmSnd;
  }
}
