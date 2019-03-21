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

package org.beigesoft.flt;

/**
 * <p>Filter numeric generic.</p>
 * @param <T> numeric ttpe
 * @author Yury Demidenko
 */
public class FltNum<T> extends AFlt {

  /**
   * <p>Val#1.</p>
   **/
  private T val1;

  /**
   * <p>Val#2 if used.</p>
   **/
  private T val2;

  //Simple getters and setters:
  /**
   * <p>Getter for val1.</p>
   * @return T
   **/
  public final T getVal1() {
    return this.val1;
  }

  /**
   * <p>Setter for val1.</p>
   * @param pVal1 reference
   **/
  public final void setVal1(final T pVal1) {
    this.val1 = pVal1;
  }

  /**
   * <p>Getter for val2.</p>
   * @return T
   **/
  public final T getVal2() {
    return this.val2;
  }

  /**
   * <p>Setter for val2.</p>
   * @param pVal2 reference
   **/
  public final void setVal2(final T pVal2) {
    this.val2 = pVal2;
  }
}
