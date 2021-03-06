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

import org.beigesoft.mdl.IIdLna;

/**
 * <p>Abstraction of base persistable model for distributed databases.
 * Native ID is autogenerated. Origin ID is used for replication.
 * For improving performance purpose all ID is
 * triple of autogenerated explicit ID Long type
 * and two implicit(there is no database constraints for them)
 * ID for replication - int(DATABASE_NUMBER)+Long(ID FOREIGN)</p>
 *
 * @author Yury Demidenko
 */
public interface IOrId extends IIdLna {

  /**
   * <p>Geter for ID of Original Database (Birth).</p>
   * @return Integer
   **/
  Integer getDbOr();

  /**
   * <p>Setter for .</p>
   * @param pDbOr reference
   **/
  void setDbOr(Integer pDbOr);

  /**
   * <p>Geter for id in Original Database (NULL for natives).</p>
   * @return Long
   **/
  Long getIdOr();

  /**
   * <p>Setter for idBirth.</p>
   * @param pIdOr reference
   **/
  void setIdOr(Long pIdOr);
}
