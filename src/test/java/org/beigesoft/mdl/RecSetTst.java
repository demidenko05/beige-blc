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

/**
 * <p>Test record-set to adapt JDBC or Android one.
 * It's SQlite fields types oriented.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class RecSetTst<RS> implements IRecSet<RS> {

  /**
   * <p>Params map.</p>
   **/
  private final Map data = new HashMap();

  /**
   * <p>Retrieve String column value.</p>
   * @param pClNm column name
   * @return String result
   * @throws Exception - an exception
   **/
  @Override
  public final String getStr(final String pClNm) throws Exception {
    if (!this.data.keySet().contains(pClNm)) {
      throw new Exception("There is no column in RS: " + pClNm);
    }
    return (String) this.data.get(pClNm);
  }

  /**
   * <p>Retrieve Double column value.</p>
   * @param pClNm column name
   * @return Double result
   * @throws Exception - an exception
   **/
  @Override
  public final Double getDouble(final String pClNm) throws Exception {
    if (!this.data.keySet().contains(pClNm)) {
      throw new Exception("There is no column in RS: " + pClNm);
    }
    return (Double) this.data.get(pClNm);
  }

  /**
   * <p>Retrieve Float column value.</p>
   * @param pClNm column name
   * @return Float result
   * @throws Exception - an exception
   **/
  @Override
  public final Float getFloat(final String pClNm) throws Exception {
    if (!this.data.keySet().contains(pClNm)) {
      throw new Exception("There is no column in RS: " + pClNm);
    }
    return (Float) this.data.get(pClNm);
  }

  /**
   * <p>Retrieve Integer column value.</p>
   * @param pClNm column name
   * @return Integer result
   * @throws Exception - an exception
   **/
  @Override
  public final Integer getInt(final String pClNm) throws Exception {
    if (!this.data.keySet().contains(pClNm)) {
      throw new Exception("There is no column in RS: " + pClNm);
    }
    return (Integer) this.data.get(pClNm);
  }

  /**
   * <p>Retrieve Long column value.</p>
   * @param pClNm column name
   * @return Long result
   * @throws Exception - an exception
   **/
  @Override
  public final Long getLong(final String pClNm) throws Exception {
    if (!this.data.keySet().contains(pClNm)) {
      throw new Exception("There is no column in RS: " + pClNm);
    }
    return (Long) this.data.get(pClNm);
  }

  /**
   * <p>Getter for record set.</p>
   * @return RS record set
   **/
  @Override
  public final RS getRecSet() {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Move cursor to first record (for Android compatibility).</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean first() throws Exception {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Move cursor to next record.</p>
   * @return boolean if next record exist
   * @throws Exception - an exception
   **/
  @Override
  public final boolean next() throws Exception {
    throw new RuntimeException("NEI");
  }

  /**
   * <p>Close result-set, for JDBC close statement.</p>
   * @throws Exception - an exception
   **/
  @Override
  public final void close() throws Exception {
    throw new RuntimeException("NEI");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for data.</p>
   * @return Map<String, ?>
   **/
  public final Map getData() {
    return this.data;
  }
}
