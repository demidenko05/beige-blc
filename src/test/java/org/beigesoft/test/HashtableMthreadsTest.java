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
import java.util.Hashtable;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * <p>Test Map multithreading.</p>
 * Hashtable with pitting locking is reliable approach
 * to use as beans-holder in a factory:
 * <pre>
 * HT:Thread#14: put to map - 11
 * HT:Thread#13: got from map after locking - 11
 * HT:Thread#14: got from map - 11
 * HT:Thread#13: put to map - 12
 * HT:Thread#13: got from map - 12
 * </pre>
 *
 * @author Yury Demidenko
 */
public class HashtableMthreadsTest {

  private final Hashtable<Integer, Integer> testMap =
    new Hashtable<Integer, Integer>(); 

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
    assertTrue(thread2WasException);
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
   * if null then locking Map, again try to get "i", then "put" it if null.</p>
   **/
  public void doThread1() throws Exception {
    Thread thread1 = new Thread() {
      
      public void run() {
        try {
          while (HashtableMthreadsTest.this.index < 40) {
            int i = HashtableMthreadsTest.this.index;
            Integer res = HashtableMthreadsTest.this.testMap.get(i);
            if (res != null) {
              System.out.println("HT:Thread#" + Thread.currentThread().getId()
                + ": got from map - " + res);
              Thread.sleep(5);
            } else {
              synchronized (HashtableMthreadsTest.this.testMap) {
                // make sure value still null after locking:
                res = HashtableMthreadsTest.this.testMap.get(i);
                if (res != null) {
                  System.out.println("HT:Thread#" + Thread.currentThread().getId()
                    + ": got from map after locking - " + i);
                  Thread.sleep(5);
                } else {
                  HashtableMthreadsTest.this.testMap.put(i, initializeVal(i));
                  System.out.println("HT:Thread#" + Thread.currentThread().getId()
                    + ": put to map - " + i);
                 }
              }
            }
          }
        } catch (Exception ex) {
          HashtableMthreadsTest.this.thread1WasException = true;
          ex.printStackTrace();
        }
        HashtableMthreadsTest.this.isThread1End = true;
      }
    };
    thread1.start();
  }

  /**
   * <p>Thread3 getting cyclical "i" from Map,
   * if null then locking Map, again try to get "i", then "put" it if null.</p>
   **/
  public void doThread3() throws Exception {
    Thread thread3 = new Thread() {
      
      public void run() {
        try {
          while (HashtableMthreadsTest.this.index < 40) {
            int i = HashtableMthreadsTest.this.index;
            Integer res = HashtableMthreadsTest.this.testMap.get(i);
            if (res != null) {
              System.out.println("HT:Thread#" + Thread.currentThread().getId()
                + ": got from map - " + res);
              Thread.sleep(5);
            } else {
              synchronized (HashtableMthreadsTest.this.testMap) {
                res = HashtableMthreadsTest.this.testMap.get(i);
                if (res != null) {
                  System.out.println("HT:Thread#" + Thread.currentThread().getId()
                    + ": got from map after locking - " + i);
                  Thread.sleep(5);
                } else {
                  HashtableMthreadsTest.this.testMap.put(i, initializeVal(i));
                  System.out.println("HT:Thread#" + Thread.currentThread().getId()
                    + ": put to map - " + i);
                 }
              }
            }
          }
        } catch (Exception ex) {
          HashtableMthreadsTest.this.thread3WasException = true;
          ex.printStackTrace();
        }
        HashtableMthreadsTest.this.isThread3End = true;
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
          while (HashtableMthreadsTest.this.index < 40) {
            for (Map.Entry<Integer, Integer> entry
              : HashtableMthreadsTest.this.testMap.entrySet()) {
              System.out.println("HT:Thread#" + Thread.currentThread().getId()
                + ": getting map size - " + HashtableMthreadsTest.this.testMap.size());
              Thread.sleep(5);
            }
          }
        } catch (Exception ex) {
          HashtableMthreadsTest.this.thread2WasException = true;
          ex.printStackTrace();
        }
        HashtableMthreadsTest.this.isThread2End = true;
      }
    };
    thread2.start();
  }
}
