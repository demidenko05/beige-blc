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

import java.util.List;
import java.util.ArrayList;

/**
 * <p>Filter of itmes.</p>
 * @param <E> type of item
 * @author Yury Demidenko
 */
public class FltItms<E> extends AFlt {

  /**
   * <p>All available items.</p>
   **/
  private List<E> all = new ArrayList<E>();

  /**
   * <p>Chosen itmes.</p>
   **/
  private List<E> itms = new ArrayList<E>();

  //Simple getters and setters:
  /**
   * <p>Getter for all.</p>
   * @return List<E>
   **/
  public final List<E> getAll() {
    return this.all;
  }

  /**
   * <p>Setter for all.</p>
   * @param pAll reference
   **/
  public final void setAll(final List<E> pAll) {
    this.all = pAll;
  }

  /**
   * <p>Getter for itms.</p>
   * @return List<E>
   **/
  public final List<E> getItms() {
    return this.itms;
  }

  /**
   * <p>Setter for itms.</p>
   * @param pItms reference
   **/
  public final void setItms(final List<E> pItms) {
    this.itms = pItms;
  }
}
