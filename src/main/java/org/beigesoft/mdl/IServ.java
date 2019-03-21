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
 * <p>Abstraction of persistable service.</p>
 *
 * @author Yury Demidenko
 */
public interface IServ extends IIdLnVrNm {

  /**
   * <p>Getter for tmMe.
   * Not null, default TIME, booking time method.</p>
   * @return EServTime
   **/
  ESrvTm getTmMe();

  /**
   * <p>Setter for tmMe.
   * Not null, default TIME, booking time method.</p>
   * @param pTmMe reference
   **/
  void setTmMe(final ESrvTm pTmMe);

  /**
   * <p>Getter for tmAd.
   * Additional time method,
   * e.g. step from zero in minutes (5/10/15/20/30) for tmMe=="*TIME*".</p>
   * @return Integer
   **/
  Integer getTmAd();

  /**
   * <p>Setter for tmAd.
   * Additional time method,
   * e.g. step from zero in minutes (5/10/15/20/30) for tmMe=="*TIME*".</p>
   * @param pTmAd reference
   **/
  void setTmAd(final Integer pTmAd);
}
