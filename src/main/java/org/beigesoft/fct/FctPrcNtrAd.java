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

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.prc.IPrc;
import org.beigesoft.prc.MngSft;
import org.beigesoft.prc.DbImp;
import org.beigesoft.prc.DbExp;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rpl.RpRtrvDbXml;
import org.beigesoft.rpl.RpStorDbXmlCp;
import org.beigesoft.rpl.RplXmlHttps;
import org.beigesoft.rpl.ClrDb;
import org.beigesoft.rpl.RpEntWriXml;
import org.beigesoft.rpl.RpEntReadXml;
import org.beigesoft.rdb.IRdb;

/**
 * <p>Factory of processors for admin, secure non-transactional requests.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctPrcNtrAd<RS> implements IFctNm<IPrc> {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  /**
   * <p>Outside factories.</p>
   **/
  private Set<IFctNm<IPrc>> fctsPrc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrc> procs = new HashMap<String, IPrc>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor
   * @throws Exception - an exception
   */
  public final IPrc laz(final Map<String, Object> pRvs,
    final String pPrNm) throws Exception {
    IPrc rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (MngSft.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMngSft(pRvs);
          } else if (DbImp.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDbImp(pRvs);
          } else if (DbExp.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDbExp(pRvs);
          } else {
            if (this.fctsPrc != null) {
              for (IFctNm<IPrc> fp : this.fctsPrc) {
                rz = fp.laz(pRvs, pPrNm);
                if (rz != null) {
                  break;
                }
              }
            }
            if (rz == null) {
              throw new ExcCode(ExcCode.WRCN, "There is no IProc: " + pPrNm);
            }
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map MngSft.</p>
   * @param pRvs request scoped vars
   * @return MngSft
   * @throws Exception - an exception
   */
  private MngSft crPuMngSft(
    final Map<String, Object> pRvs) throws Exception {
    MngSft rz = new MngSft();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    this.procs.put(MngSft.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MngSft.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF DbExp.</p>
   * @param pRvs request scoped vars
   * @return DbExp
   * @throws Exception - an exception
   */
  private DbExp crPuDbExp(final Map<String, Object> pRvs) throws Exception {
    DbExp rz = new DbExp();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    RpRtrvDbXml<RS> retr = new RpRtrvDbXml<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    retr.setRdb(rdb);
    retr.setOrm(this.fctBlc.lazOrm(pRvs));
    retr.setLog(this.fctBlc.lazLogStd(pRvs));
    RpEntWriXml entWr = (RpEntWriXml) this.fctBlc.laz(pRvs, FctDbCp.ENWRDBCPNM);
    retr.setRpEntWri(entWr);
    rz.setRetr(retr);
    this.procs.put(DbExp.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      DbExp.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF DbImp.</p>
   * @param pRvs request scoped vars
   * @return DbImp
   * @throws Exception - an exception
   */
  private DbImp crPuDbImp(final Map<String, Object> pRvs) throws Exception {
    DbImp rz = new DbImp();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    rz.setFctApp(this.fctBlc);
    RplXmlHttps<RS> repl = new RplXmlHttps<RS>();
    repl.setSetng((ISetng) this.fctBlc.laz(pRvs, FctDt.STGDBCPNM));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    repl.setRdb(rdb);
    repl.setLog(this.fctBlc.lazLogStd(pRvs));
    repl.setUtlXml(this.fctBlc.lazUtlXml(pRvs));
    RpStorDbXmlCp<RS> rpStor = new RpStorDbXmlCp<RS>();
    rpStor.setLog(this.fctBlc.lazLogStd(pRvs));
    rpStor.setOrm(this.fctBlc.lazOrm(pRvs));
    rpStor.setUtlXml(this.fctBlc.lazUtlXml(pRvs));
    rpStor.setRdb(rdb);
    RpEntReadXml erd = (RpEntReadXml) this.fctBlc.laz(pRvs, FctDbCp.ENRDDBCPNM);
    rpStor.setRpEntRead(erd);
    repl.setRpStor(rpStor);
    ClrDb<RS> clrDb = new ClrDb<RS>();
    clrDb.setLog(this.fctBlc.lazLogStd(pRvs));
    clrDb.setRdb(rdb);
    clrDb.setSetng(repl.getSetng());
    repl.setDbBefore(clrDb);
    rz.setRepl(repl);
    this.procs.put(DbImp.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      DbImp.class.getSimpleName() + " has been created");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final synchronized FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final synchronized void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }

  /**
   * <p>Getter for fctsPrc.</p>
   * @return Set<IFctNm<IPrc>>
   **/
  public final synchronized Set<IFctNm<IPrc>> getFctsPrc() {
    return this.fctsPrc;
  }

  /**
   * <p>Setter for fctsPrc.</p>
   * @param pFctsPrc reference
   **/
  public final synchronized void setFctsPrc(final Set<IFctNm<IPrc>> pFctsPrc) {
    this.fctsPrc = pFctsPrc;
  }
}
