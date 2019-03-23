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
 * <pre>This is about http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html.
 * It's very easy to understand that Java instruction:
 *   this.sharedBean = new SharedBean(); //create and assign reference in one
 * can be compiled into processor's (native) instructions:
 *   1. allocate memory for new instance SharedBean and create it
 *   2. assign reference of new instance to this.sharedBean
 *   3. call to SharedBean constructor code
 * On step #3 OS may switch to another thread that will be used
 * this.sharedBean whose constructor's code doesn't completed.
 * 
 * But it's very hard to understand why author didn't separate instruction in his code for creation object
 * and assigning reference to it into two instructions, even though lazy initialization of well designed bean
 * (that is usually POJO) means initialization it with setters rather than with constructor.
 * JVM must insure that step # 4 will be deal with completely initialized object:
 *   1. Bean lBean = new Bean();
 *   2. lBean.setServiceA(new ServiceA()); // POJO initialization
 *   3. this.bean = lBean;
 *   4. this.bean.do(); // this.bean is completely initialized
 * 
 * Even though putting reference to map is non-atomic (multi-instructions),
 * at the end assigning reference (pointer) is atomic CPU instruction e.g. for 64bit pointer:
 *    mov qword ptr [rbp], rax
 * 
 * This is test for right approach - creation and referencing in two instructions:
 *    LongInitializedBean lLongInitializedBean = new LongInitializedBean();
 *    this.longInitializedBean = lLongInitializedBean;
 * 
 * </pre>
 *
 * @author Yury Demidenko
 */
public class DoubleCkeckLockingRiApTest {

  //Shared Long Initialized Bean:
  private LongInitializedBean longInitializedBean = null;

  //Shared flags:  
  private Boolean isThread1End = false;

  private Boolean isThread2End = false;

  private Boolean isThread3End = false;

  //Shared data:
  private int index = 0;

  private int countUsingNonInitialized = 0;

  private int countInitialized = 0;

  private int countNulled = 0;

  @Test
  public void tstThreads() throws Exception {
    doThread1();
    doThread3();
    doThread2();
    for (this.index = 0; this.index < 40; this.index++) {
      Thread.sleep(5);
    }
    while (!(this.isThread1End
      && this.isThread2End
      && this.isThread3End)) {
      Thread.sleep(5);
    }
    System.out.println("this.countUsingNonInitialized=" + this.countUsingNonInitialized);
    System.out.println("this.countNulled=" + this.countNulled);
    System.out.println("this.countInitialized=" + this.countInitialized);
    assertTrue(this.countUsingNonInitialized == 0);
  }

  public void doThread3() throws Exception {
    Thread thread3 = new Thread() {
      
      public void run() {
        try {
          while (DoubleCkeckLockingRiApTest.this.index < 40) {
            if (DoubleCkeckLockingRiApTest.this.longInitializedBean != null) {
              if (DoubleCkeckLockingRiApTest.this.longInitializedBean.getItsPrice() == null) {
                // using partially initialized bean:
                DoubleCkeckLockingRiApTest.this.countUsingNonInitialized++;
              }
              synchronized (DoubleCkeckLockingRiApTest.this) {
                // make sure value still not null after locking:
                if (DoubleCkeckLockingRiApTest.this.longInitializedBean != null) {
                  DoubleCkeckLockingRiApTest.this.longInitializedBean = null;
                  DoubleCkeckLockingRiApTest.this.countNulled++;
                }
              }
            } else {
              synchronized (DoubleCkeckLockingRiApTest.this) {
                // make sure value still null after locking:
                if (DoubleCkeckLockingRiApTest.this.longInitializedBean == null) {
                  // right approach - creation and referencing in two instructions:
                  LongInitializedBean lLongInitializedBean = new LongInitializedBean();
                  DoubleCkeckLockingRiApTest.this.longInitializedBean = lLongInitializedBean;
                  DoubleCkeckLockingRiApTest.this.countInitialized++;
                }
              }
            }
            Thread.sleep(5);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        DoubleCkeckLockingRiApTest.this.isThread3End = true;
      }
    };
    thread3.start();
  }

  public void doThread2() throws Exception {
    Thread thread2 = new Thread() {
      
      public void run() {
        try {
          while (DoubleCkeckLockingRiApTest.this.index < 40) {
            if (DoubleCkeckLockingRiApTest.this.longInitializedBean != null) {
              if (DoubleCkeckLockingRiApTest.this.longInitializedBean.getItsPrice() == null) {
                // using partially initialized bean:
                DoubleCkeckLockingRiApTest.this.countUsingNonInitialized++;
              }
              synchronized (DoubleCkeckLockingRiApTest.this) {
                // make sure value still not null after locking:
                if (DoubleCkeckLockingRiApTest.this.longInitializedBean != null) {
                  DoubleCkeckLockingRiApTest.this.longInitializedBean = null;
                  DoubleCkeckLockingRiApTest.this.countNulled++;
                }
              }
            } else {
              synchronized (DoubleCkeckLockingRiApTest.this) {
                // make sure value still null after locking:
                if (DoubleCkeckLockingRiApTest.this.longInitializedBean == null) {
                  // right approach - creation and referencing in two instructions:
                  LongInitializedBean lLongInitializedBean = new LongInitializedBean();
                  DoubleCkeckLockingRiApTest.this.longInitializedBean = lLongInitializedBean;
                  DoubleCkeckLockingRiApTest.this.countInitialized++;
                }
              }
            }
            Thread.sleep(5);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        DoubleCkeckLockingRiApTest.this.isThread2End = true;
      }
    };
    thread2.start();
  }

  public void doThread1() throws Exception {
    Thread thread1 = new Thread() {
      
      public void run() {
        try {
          while (DoubleCkeckLockingRiApTest.this.index < 40) {
            if (DoubleCkeckLockingRiApTest.this.longInitializedBean != null) {
              if (DoubleCkeckLockingRiApTest.this.longInitializedBean.getItsPrice() == null) {
                // using partially initialized bean:
                DoubleCkeckLockingRiApTest.this.countUsingNonInitialized++;
              }
              synchronized (DoubleCkeckLockingRiApTest.this) {
                // make sure value still not null after locking:
                if (DoubleCkeckLockingRiApTest.this.longInitializedBean != null) {
                  DoubleCkeckLockingRiApTest.this.longInitializedBean = null;
                  DoubleCkeckLockingRiApTest.this.countNulled++;
                }
              }
            } else {
              synchronized (DoubleCkeckLockingRiApTest.this) {
                // make sure value still null after locking:
                if (DoubleCkeckLockingRiApTest.this.longInitializedBean == null) {
                  // right approach - creation and referencing in two instructions:
                  LongInitializedBean lLongInitializedBean = new LongInitializedBean();
                  DoubleCkeckLockingRiApTest.this.longInitializedBean = lLongInitializedBean;
                  DoubleCkeckLockingRiApTest.this.countInitialized++;
                }
              }
            }
            Thread.sleep(5);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        DoubleCkeckLockingRiApTest.this.isThread1End = true;
      }
    };
    thread1.start();
  }
}
