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

package org.beigesoft.rdb;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.mdl.IRecSet;

/**
 * <p>Base Database service that handles cross-platform methods.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public abstract class ARdb<RS> implements IRdb<RS> {

  /**
   * <p>Database ID.</p>
   **/
  private Integer dbId;

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Evaluate single Integer result.</p>
   * @param pQu Query
   * @param pClNm Column Name
   * @return Integer result e.g 11231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Integer evInt(final String pQu,
    final String pClNm) throws Exception {
    Integer result = null;
    IRecSet<RS> rs = null;
    try {
      rs = retRs(pQu);
      if (rs.first()) {
        result = rs.getInt(pClNm);
        if (rs.next()) {
          throw new ExcCode(ExcCode.WRPR,
            "Query returns more than 1 result - " + pQu);
        }
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate single Long result.</p>
   * @param pQu Query
   * @param pClNm Column Name
   * @return Long result e.g 11231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Long evLong(final String pQu,
    final String pClNm) throws Exception {
    Long result = null;
    IRecSet<RS> rs = null;
    try {
      rs = retRs(pQu);
      if (rs.first()) {
        result = rs.getLong(pClNm);
        if (rs.next()) {
          throw new ExcCode(ExcCode.WRPR,
            "Query returns more than 1 result - " + pQu);
        }
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate single Float result.</p>
   * @param pQu Query
   * @param pClNm Column Name
   * @return Float result e.g 1.1231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Float evFloat(final String pQu,
    final String pClNm) throws Exception {
    Float result = null;
    IRecSet<RS> rs = null;
    try {
      rs = retRs(pQu);
      if (rs.first()) {
        result = rs.getFloat(pClNm);
        if (rs.next()) {
          throw new ExcCode(ExcCode.WRPR,
            "Query returns more than 1 result - " + pQu);
        }
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate single Double result.</p>
   * @param pQu Query
   * @param pClNm Column Name
   * @return Double result e.g 1.1231 or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Double evDouble(final String pQu,
    final String pClNm) throws Exception {
    Double result = null;
    IRecSet<RS> rs = null;
    try {
      rs = retRs(pQu);
      if (rs.first()) {
        result = rs.getDouble(pClNm);
        if (rs.next()) {
          throw new ExcCode(ExcCode.WRPR,
            "Query returns more than 1 result - " + pQu);
        }
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return result;
  }

  /**
   * <p>Evaluate Double results.</p>
   * @param pQu Query
   * @param pClNms Column Names
   * @return Double[] result e.g. [2.14, NULL, 111.456]
   * @throws Exception - an exception
   */
  @Override
  public final Double[] evDoubles(final String pQu,
    final String[] pClNms) throws Exception {
    Double[] result = new Double[pClNms.length];
    IRecSet<RS> rs = null;
    try {
      rs = retRs(pQu);
      if (rs.first()) {
        for (int i = 0; i < pClNms.length; i++) {
          result[i] = rs.getDouble(pClNms[i]);
        }
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return result;
  }

  /**
   * <p>Geter for database ID.</p>
   * @return ID database
   **/
  @Override
  public final synchronized Integer getDbId() {
    if (this.dbId == null) {
      try {
        String query = "select DBID from DBINF;";
        Integer di = evInt(query, "DBID");
        if (di == null) {
          throw new ExcCode(ExcCode.WRCN, "database_info_config_error");
        }
        this.dbId = di;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return this.dbId;
  }

  /**
   * <p>Geter for database version.</p>
   * @return database version
   **/
  @Override
  public final Integer getDbVr() {
    try {
      String query = "select DBVR from DBINF;";
      Integer dbVr = evInt(query, "DBVR");
      if (dbVr == null) {
        throw new ExcCode(ExcCode.WRCN, "database_info_config_error");
      }
      return dbVr;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for log.</p>
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
}
