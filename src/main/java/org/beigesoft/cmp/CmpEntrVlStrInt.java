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

package org.beigesoft.cmp;

import java.util.Comparator;
import java.util.Map;
import java.io.Serializable;

/**
 * <p>Comparator for Entry.getValue().get(orderKey).Value of String that
 * contain Integer.toString() value.</p>
 *
 * @author Yury Demidenko
 */
public class CmpEntrVlStrInt
  implements Comparator<Map.Entry<String, Map<String, String>>>, Serializable {

  /**
   * <p>serialVersionUID.</p>
   **/
  static final long serialVersionUID = 49732947987715L;

  /**
   * <p>Key of order property.</p>
   **/
  private final String orderKey;

  /**
   * <p>Only constructor.</p>
   * @param pOrderKey order key
   **/
  public CmpEntrVlStrInt(final String pOrderKey) {
    this.orderKey = pOrderKey;
  }

  @Override
  public final int compare(final Map.Entry<String, Map<String, String>> o1,
          final Map.Entry<String, Map<String, String>> o2) {
    Integer intO1 = Integer.valueOf(o1.getValue().get(orderKey));
    Integer intO2 = Integer.valueOf(o2.getValue().get(orderKey));
    return intO1.compareTo(intO2);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for orderKey.</p>
   * @return String
   **/
  public final String getOrderKey() {
    return this.orderKey;
  }
}
