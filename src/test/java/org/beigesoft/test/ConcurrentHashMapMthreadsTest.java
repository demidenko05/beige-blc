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

package org.beigesoft.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * <p>Test ConcurrentHashMap multithreading.
 * Due long putting (7ms) without locking getting
 * there are many duplicate putting:
 * <pre>
 * CHT:Thread#12: getting map size - 37
 * CHT:Thread#10: put to map - 37
 * CHT:Thread#10: got from map - 37
 * CHT:Thread#11: put to map - 37
 * CHT:Thread#11: got from map - 37
 * </pre>
 * </p>
 *
 * @author Yury Demidenko
 */
public class ConcurrentHashMapMthreadsTest {

  private final ConcurrentHashMap<Integer, Integer> testMap =
    new ConcurrentHashMap<Integer, Integer>(); 

  //Shared flags:
  private Boolean thread1WasException = false;

  private Boolean thread2WasException = false;

  private Boolean thread3WasException = false;
  
  private Boolean isThread1End = false;

  private Boolean isThread2End = false;

  private Boolean isThread3End = false;

  //Shared data:
  private int index = 0;

  @Test
  public void tstThreads() throws Exception {
    doThread1();
    doThread3();
    doThread2();
    for (this.index = 0; this.index < 40; this.index++) {
      Thread.sleep(15);
    }
    while (!(this.isThread1End
      && this.isThread2End
      && this.isThread3End)) {
      Thread.sleep(5);
    }
    assertTrue(!thread1WasException);
    assertTrue(!thread2WasException);
    assertTrue(!thread3WasException);
  }

  /**
   * <p>Long value initialization.</p>
   **/
  public Integer initializeVal(int pVal) throws Exception {
     Thread.sleep(7);
     return Integer.valueOf(pVal);
  }

  /**
   * <p>Thread1 getting cyclical "i" from Map,
   * if null then "put" it.</p>
   **/
  public void doThread1() throws Exception {
    Thread thread1 = new Thread() {
      
      public void run() {
        try {
          while (ConcurrentHashMapMthreadsTest.this.index < 40) {
            int i = ConcurrentHashMapMthreadsTest.this.index;
            Integer res = ConcurrentHashMapMthreadsTest.this.testMap.get(i);
            if (res != null) {
              System.out.println("CHT:Thread#" + Thread.currentThread().getId()
                + ": got from map - " + res);
              Thread.sleep(5);
            } else {
              ConcurrentHashMapMthreadsTest.this.testMap.put(i, initializeVal(i));
              System.out.println("CHT:Thread#" + Thread.currentThread().getId()
                + ": put to map - " + i);
            }
          }
        } catch (Exception ex) {
          ConcurrentHashMapMthreadsTest.this.thread1WasException = true;
          ex.printStackTrace();
        }
        ConcurrentHashMapMthreadsTest.this.isThread1End = true;
      }
    };
    thread1.start();
  }

  /**
   * <p>Thread3 getting cyclical "i" from Map,
   * if null then "put" it.</p>
   **/
  public void doThread3() throws Exception {
    Thread thread3 = new Thread() {
      
      public void run() {
        try {
          while (ConcurrentHashMapMthreadsTest.this.index < 40) {
            int i = ConcurrentHashMapMthreadsTest.this.index;
            Integer res = ConcurrentHashMapMthreadsTest.this.testMap.get(i);
            if (res != null) {
              System.out.println("CHT:Thread#" + Thread.currentThread().getId()
                + ": got from map - " + res);
              Thread.sleep(5);
            } else {
              ConcurrentHashMapMthreadsTest.this.testMap.put(i, initializeVal(i));
              System.out.println("CHT:Thread#" + Thread.currentThread().getId()
                + ": put to map - " + i);
            }
          }
        } catch (Exception ex) {
          ConcurrentHashMapMthreadsTest.this.thread3WasException = true;
          ex.printStackTrace();
        }
        ConcurrentHashMapMthreadsTest.this.isThread3End = true;
      }
    };
    thread3.start();
  }

  /**
   * <p>Thread2 that just read Map by iterator without locking.</p>
   **/
  public void doThread2() throws Exception {
    Thread thread2 = new Thread() {
      
      public void run() {
        try {
          while (ConcurrentHashMapMthreadsTest.this.index < 40) {
            for (Map.Entry<Integer, Integer> entry
              : ConcurrentHashMapMthreadsTest.this.testMap.entrySet()) {
              System.out.println("CHT:Thread#" + Thread.currentThread().getId()
                + ": getting map size - " + ConcurrentHashMapMthreadsTest.this.testMap.size());
              Thread.sleep(5);
            }
          }
        } catch (Exception ex) {
          ConcurrentHashMapMthreadsTest.this.thread2WasException = true;
          ex.printStackTrace();
        }
        ConcurrentHashMapMthreadsTest.this.isThread2End = true;
      }
    };
    thread2.start();
  }
}
