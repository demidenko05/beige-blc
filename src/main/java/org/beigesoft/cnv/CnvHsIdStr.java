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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctNm;
import org.beigesoft.hld.IHldNm;

/**
 * <p>Converter of an owned entity to string.
 * It's entity ID field converted to string. ID either simple - String/Long
 * or complex - composite or a foreign entity.</p>
 *
 * @param <T> entity type
 * @author Yury Demidenko
 */
public class CnvHsIdStr<T extends IHasId<?>> implements IConv<T, String> {

  /**
   * <p>Converters fields factory.</p>
   */
  private IFctNm<IConv<?, String>> fctCnvFld;

  /**
   * <p>Fields converters names holder.</p>
   **/
  private IHldNm<Class<?>, String> hldNmFdCn;

  /**
   * <p>Converts any entity to string (ID).</p>
   * @param pRqVs request scoped vars, e.g. user preference decimal separator
   * @param pHsId Entity that ID
   * @return string representation
   * @throws Exception - an exception
   **/
  @Override
  public final String conv(final Map<String, Object> pRqVs,
    final T pHsId) throws Exception {
    String cnNm = this.hldNmFdCn.get(pHsId.getClass(), IHasId.IDNM);
    @SuppressWarnings("unchecked")
    IConv<Object, String> flCn = (IConv<Object, String>) this.fctCnvFld
      .laz(pRqVs, cnNm);
    return flCn.conv(pRqVs, pHsId.getIid());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctCnvFld.</p>
   * @return IFctNm<IConv<?, String>>
   **/
  public final IFctNm<IConv<?, String>> getFctCnvFld() {
    return this.fctCnvFld;
  }

  /**
   * <p>Setter for fctCnvFld.</p>
   * @param pFctCnvFld reference
   **/
  public final void setFctCnvFld(
    final IFctNm<IConv<?, String>> pFctCnvFld) {
    this.fctCnvFld = pFctCnvFld;
  }

  /**
   * <p>Getter for hldNmFdCn.</p>
   * @return IHldNm<Class<?>, String>
   **/
  public final IHldNm<Class<?>, String> getHldNmFdCn() {
    return this.hldNmFdCn;
  }

  /**
   * <p>Setter for hldNmFdCn.</p>
   * @param pHldNmFdCn reference
   **/
  public final void setHldNmFdCn(final IHldNm<Class<?>, String> pHldNmFdCn) {
    this.hldNmFdCn = pHldNmFdCn;
  }
}
