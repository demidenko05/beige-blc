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

package org.beigesoft.fct;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdlp.GoodsRating;
import org.beigesoft.log.ILog;
import org.beigesoft.log.LogFile;
import org.beigesoft.hld.HldIdFdNm;
import org.beigesoft.srv.IRdb;
import org.beigesoft.srv.IOrm;

/**
 * <p>Tests final configuration auxiliary factory.</p>
 *
 * @author Yury Demidenko
 */
public class FctTst implements IFctAux {

  //configuration data:
  /**
   * <p>Standard log file name.</p>
   **/
  private String logStdNm = "tst-blc";

  //cached services/parts:
  /**
   * <p>Logger.</p>
   **/
  private ILog logStd;

  /**
   * <p>Creates requested bean and put into given main factory.
   * The main factory is already synchronized when invokes this.</p>
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @param pFctApp main factory
   * @return Object - requested bean
   * @throws Exception - an exception, e.g. if bean name not found
   */
  @Override
  public final Object crePut(final Map<String, Object> pRqVs,
    final String pBnNm, final IFctApp pFctApp) throws Exception {
    Object rz = null;
    if (FctBlc.LOGSTDNM.equals(pBnNm)) {
      rz = lazLogStd(pRqVs, pFctApp);
    } else if (HldIdFdNm.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHldIdFdNm(pRqVs, pFctApp);
    //} else {
      //throw new ExcCode(ExcCode.WRPR, "There is no bean: " + pBnNm);
    }
    return rz;
  }

  /**
   * <p>Releases state when main factory is releasing.</p>
   * @throws Exception - an exception
   */
  @Override
  public final void release() throws Exception {
    this.logStd = null;
  }

  /**
   * <p>Lazy getter standard logger.</p>
   * @param pRqVs request scoped vars
   * @param pFctApp main factory
   * @return Logger
   * @throws Exception - an exception
   */
  private ILog lazLogStd(final Map<String, Object> pRqVs,
    final IFctApp pFctApp) throws Exception {
    if (this.logStd == null) {
      LogFile log = new LogFile();
      String currDir = System.getProperty("user.dir") + File.separator
        + "target" + File.separator;
      log.setPath(currDir + this.logStdNm);
      log.setClsImm(true);
      this.logStd = log;
      pFctApp.put(pRqVs, FctBlc.LOGSTDNM, this.logStd);
      this.logStd.info(pRqVs, getClass(), FctBlc.LOGSTDNM
        + " has been created");
    }
    return this.logStd;
  }

  /**
   * <p>Creates HldIdFdNm and puts into main factory.</p>
   * @param pRqVs request scoped vars
   * @param pFctApp main factory
   * @return HldIdFdNm
   * @throws Exception - an exception
   */
  private HldIdFdNm crPuHldIdFdNm(final Map<String, Object> pRqVs,
    final IFctApp pFctApp) throws Exception {
    HldIdFdNm rz = new HldIdFdNm();
    rz.getCstIdNms().put(GoodsRating.class, "goods");
    pFctApp.put(pRqVs, HldIdFdNm.class.getSimpleName(), rz);
    lazLogStd(pRqVs, pFctApp).info(null, getClass(), HldIdFdNm.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for logStdNm.</p>
   * @return String
   **/
  public final String getLogStdNm() {
    return this.logStdNm;
  }

  /**
   * <p>Setter for logStdNm.</p>
   * @param pLogStdNm reference
   **/
  public final void setLogStdNm(final String pLogStdNm) {
    this.logStdNm = pLogStdNm;
  }
}
