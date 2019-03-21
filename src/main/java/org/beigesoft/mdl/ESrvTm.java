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
 * <p>Service booking time method.</p>
 *
 * @author Yury Demidenko
 */
public enum ESrvTm {

  /**
   * <p>NONE 0, e.g. goods delivering.</p>
   **/
  NONE,

  /**
   * <p>TIME 1.</p>
   **/
  TIME,

  /**
   * <p>HOUR 2.</p>
   **/
  HOUR,

  /**
   * <p>DAY 3.</p>
   **/
  DAY,

  /**
   * <p>MONTH 4.</p>
   **/
  MONTH,

  /**
   * <p>TIME RANGE 5.</p>
   **/
  TIMERANGE,

  /**
   * <p>HOUR RANGE 6.</p>
   **/
  HOURRANGE,

  /**
   * <p>DAY RANGE 7.</p>
   **/
  DAYRANGE,

  /**
   * <p>MONTH RANGE 8.</p>
   **/
  MONTHRANGE;
}
