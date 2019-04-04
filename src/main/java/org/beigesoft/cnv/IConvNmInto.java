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

package org.beigesoft.cnv;

import java.util.Map;

/**
 * <p>Abstraction of generic converter from a type into another one
 * with using a name e.g. fill ColVals with field, and some sources
 * can transformed to set of another, e.g. foreign entity with composite ID
 * will be converted to set fieldId-valueId.</p>
 *
 * @author Yury Demidenko
 * @param <FR> type of original
 * @param <TO> type of converted
 */
public interface IConvNmInto<FR, TO> {

  /**
   * <p>Converts named thing from one type into another one.</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pVs invoker scoped vars, e.g. a current converted field's class of
   * an entity. Maybe NULL, e.g. for converting simple entity {id, ver, nme}.
   * @param pFrom value source
   * @param pTo to value container
   * @param pNm field name
   * @throws Exception - an exception
   **/
  void conv(Map<String, Object> pRqVs, Map<String, Object> pVs, FR pFrom,
    TO pTo, String pNm) throws Exception;
}