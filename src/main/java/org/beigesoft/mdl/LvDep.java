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

/**
 * <p>Model of branch current level and required deep.</p>
 *
 * @author Yury Demidenko
 */
public class LvDep  {

  /**
   * <p>Current level, from 0.</p>
   **/
  private int cur = 0;

  /**
   * <p>Required deep, 1 default.</p>
   **/
  private int dep = 1;

  //Simple getters and setters:
  /**
   * <p>Getter for cur.</p>
   * @return int
   **/
  public final int getCur() {
    return this.cur;
  }

  /**
   * <p>Setter for cur.</p>
   * @param pCur reference
   **/
  public final void setCur(final int pCur) {
    this.cur = pCur;
  }

  /**
   * <p>Getter for dep.</p>
   * @return int
   **/
  public final int getDep() {
    return this.dep;
  }

  /**
   * <p>Setter for dep.</p>
   * @param pDep reference
   **/
  public final void setDep(final int pDep) {
    this.dep = pDep;
  }
}
