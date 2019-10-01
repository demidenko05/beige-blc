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

package org.beigesoft.mdlp;

import java.util.List;

/**
 * <p>Persistable model to match native values to foreign ones.
 * It's usually entities IDs.</p>
 *
 * @author Yury Demidenko
 */
public class MaFrn extends AIdLnNm {

  /**
   * <p>Not null, simple class name of native entity,
   * e.g. InvItemCategory.</p>
   **/
  private String clsNm;

  /**
   * <p>Columns all or useful to read.</p>
   **/
  private List<MaFrnLn> lns;

  //Simple getters and setters:
  /**
   * <p>Getter for clsNm.</p>
   * @return String
   **/
  public final String getClsNm() {
    return this.clsNm;
  }

  /**
   * <p>Setter for clsNm.</p>
   * @param pClsNm reference
   **/
  public final void setClsNm(final String pClsNm) {
    this.clsNm = pClsNm;
  }

  /**
   * <p>Getter for lns.</p>
   * @return List<MaFrnLn>
   **/
  public final List<MaFrnLn> getLns() {
    return this.lns;
  }

  /**
   * <p>Setter for lns.</p>
   * @param pLns reference
   **/
  public final void setLns(final List<MaFrnLn> pLns) {
    this.lns = pLns;
  }
}
