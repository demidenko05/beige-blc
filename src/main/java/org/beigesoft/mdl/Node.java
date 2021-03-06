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

import java.util.List;

/**
 * <p>Node to make generic tree.</p>
 *
 * @param <T> value type
 * @author Yury Demidenko
 */
public class Node<T> {

  /**
   * <p>Name.</p>
   **/
  private String nme;

  /**
   * <p>Value.</p>
   **/
  private T val;

  /**
   * <p>Owned nodes.</p>
   **/
  private List<Node<T>> nodes;

  //Simple getters and setters:
  /**
   * <p>Getter for nme.</p>
   * @return String
   **/
  public final String getNme() {
    return this.nme;
  }

  /**
   * <p>Setter for nme.</p>
   * @param pNme reference
   **/
  public final void setNme(final String pNme) {
    this.nme = pNme;
  }

  /**
   * <p>Getter for val.</p>
   * @return T
   **/
  public final T getVal() {
    return this.val;
  }

  /**
   * <p>Setter for val.</p>
   * @param pVal reference
   **/
  public final void setVal(final T pVal) {
    this.val = pVal;
  }

  /**
   * <p>Getter for nodes.</p>
   * @return List<Node>
   **/
  public final List<Node<T>> getNodes() {
    return this.nodes;
  }

  /**
   * <p>Setter for nodes.</p>
   * @param pNodes reference
   **/
  public final void setNodes(final List<Node<T>> pNodes) {
    this.nodes = pNodes;
  }
}
