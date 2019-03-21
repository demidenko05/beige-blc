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

import java.util.Set;
import java.util.Map;

/**
 * <p>POJO model to adapt Android ContentValues.</p>
 *
 * @author Yury Demidenko
 */
public class ColVals {

  /**
   * <p>Old version for updating with optimistic locking.</p>
   **/
  private Long oldVer;

  /**
   * <p>Map of Integers values.</p>
   **/
  private Map<String, Integer> ints;

  /**
   * <p>Map of Long values.</p>
   **/
  private Map<String, Long> longs;

  /**
   * <p>Map of Float values.</p>
   **/
  private Map<String, Float> floats;

  /**
   * <p>Map of Double values.</p>
   **/
  private Map<String, Double> doubles;

  /**
   * <p>Map of String values.</p>
   **/
  private Map<String, String> strs;

  /**
   * <p>Columns names with expression(formula) val, i.e. VER+1.
   * It doesn't work on Android update!</p>
   **/
  private Set<String> exprs;

  //Simple getters and setters:
  /**
   * <p>Getter for oldVer.</p>
   * @return Long
   **/
  public final Long getOldVer() {
    return this.oldVer;
  }

  /**
   * <p>Setter for oldVer.</p>
   * @param pOldVer reference
   **/
  public final void setOldVer(final Long pOldVer) {
    this.oldVer = pOldVer;
  }

  /**
   * <p>Getter for ints.</p>
   * @return Map<String, Integer>
   **/
  public final Map<String, Integer> getInts() {
    return this.ints;
  }

  /**
   * <p>Setter for ints.</p>
   * @param pInts reference
   **/
  public final void setInts(final Map<String, Integer> pInts) {
    this.ints = pInts;
  }

  /**
   * <p>Getter for longs.</p>
   * @return Map<String, Long>
   **/
  public final Map<String, Long> getLongs() {
    return this.longs;
  }

  /**
   * <p>Setter for longs.</p>
   * @param pLongs reference
   **/
  public final void setLongs(final Map<String, Long> pLongs) {
    this.longs = pLongs;
  }

  /**
   * <p>Getter for floats.</p>
   * @return Map<String, Float>
   **/
  public final Map<String, Float> getFloats() {
    return this.floats;
  }

  /**
   * <p>Setter for floats.</p>
   * @param pFloats reference
   **/
  public final void setFloats(final Map<String, Float> pFloats) {
    this.floats = pFloats;
  }

  /**
   * <p>Getter for doubles.</p>
   * @return Map<String, Double>
   **/
  public final Map<String, Double> getDoubles() {
    return this.doubles;
  }

  /**
   * <p>Setter for doubles.</p>
   * @param pDoubles reference
   **/
  public final void setDoubles(final Map<String, Double> pDoubles) {
    this.doubles = pDoubles;
  }

  /**
   * <p>Getter for strs.</p>
   * @return Map<String, String>
   **/
  public final Map<String, String> getStrs() {
    return this.strs;
  }

  /**
   * <p>Setter for strs.</p>
   * @param pStrs reference
   **/
  public final void setStrs(final Map<String, String> pStrs) {
    this.strs = pStrs;
  }

  /**
   * <p>Getter for exprs.</p>
   * @return Set<String>
   **/
  public final Set<String> getExprs() {
    return this.exprs;
  }

  /**
   * <p>Setter for exprs.</p>
   * @param pExprs reference
   **/
  public final void setExprs(final Set<String> pExprs) {
    this.exprs = pExprs;
  }
}
