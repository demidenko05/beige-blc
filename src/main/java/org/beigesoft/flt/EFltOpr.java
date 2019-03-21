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
 * <p>Filter Opr.</p>
 *
 * @author Yury Demidenko
 */
public enum EFltOpr {

  /**
   * <p>0, in set of items.</p>
   **/
  IN,

  /**
   * <p>1, not in set of items.</p>
   **/
  NOT_IN,

  /**
   * <p>2, equal.</p>
   **/
  EQUAL,

  /**
   * <p>3, not equal.</p>
   **/
  NOT_EQUAL,

  /**
   * <p>4, greater than.</p>
   **/
  GREATER_THAN,

  /**
   * <p>5, greater than or equal.</p>
   **/
  GREATER_THAN_EQUAL,

  /**
   * <p>6, less than.</p>
   **/
  LESS_THAN,

  /**
   * <p>7, less than or equal.</p>
   **/
  LESS_THAN_EQUAL,

  /**
   * <p>8, like.</p>
   **/
  LIKE,

  /**
   * <p>9, greater than value#1 and less than value#2.</p>
   **/
  BETWEEN,

  /**
   * <p>10, greater/equal than value#1 and less/equal than value#2.</p>
   **/
  BETWEEN_INCLUDE;
}
