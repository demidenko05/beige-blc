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

import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.beigesoft.mdl.EPeriod;

/**
 * <p>Java Calendar Test.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CalendarTest {

  @Test
  public void test1() throws Exception {
    Date date = new Date(1470225720000L); // 3 Aug 2016 15:02
    System.out.println("t1 - date is " + date);

    Date periodDayStart = evalDatePeriodStartFor(date, EPeriod.DAILY);
    System.out.println("t1 - period day start is " + periodDayStart);
    assertEquals(new Date(1470171600000L), periodDayStart); //Wed Aug 03 2016 00:00:00
    Date periodWeekStart = evalDatePeriodStartFor(date, EPeriod.WEEKLY);
    System.out.println("t1 - period week start is " + periodWeekStart);
    assertEquals(new Date(1469912400000L), periodWeekStart); //Sun Jul 31 2016 00:00:00 USA standard!!!
    Date periodMonthStart = evalDatePeriodStartFor(date, EPeriod.MONTHLY);
    System.out.println("t1 - period month start is " + periodMonthStart);
    assertEquals(new Date(1469998800000L), periodMonthStart); //Mon Aug 01 2016 00:00:00

    Date nextDayStart = evalDateNextPeriodStart(date, EPeriod.DAILY);
    System.out.println("t1 - next day start is " + nextDayStart);
    assertEquals(new Date(1470258000000L), nextDayStart); //Thu Aug 04 2016 00:00:00
    Date nextWeekStart = evalDateNextPeriodStart(date, EPeriod.WEEKLY);
    System.out.println("t1 - next week start is " + nextWeekStart);
    assertEquals(new Date(1470517200000L), nextWeekStart); //Sun Jul 07 2016 00:00:00 USA standard!!!
    Date nextMonthStart = evalDateNextPeriodStart(date, EPeriod.MONTHLY);
    System.out.println("t1 - next month start is " + nextMonthStart);
    assertEquals(new Date(1472677200000L), nextMonthStart); //Thu Sep 01 2016 00:00:00

    Date prevDayStart = evalDatePrevPeriodStart(date, EPeriod.DAILY);
    System.out.println("t1 - prev day start is " + prevDayStart);
    assertEquals(new Date(1470085200000L), prevDayStart); //Thu Aug 02 2016 00:00:00
    Date prevWeekStart = evalDatePrevPeriodStart(date, EPeriod.WEEKLY);
    System.out.println("t1 - prev week start is " + prevWeekStart);
    assertEquals(new Date(1469307600000L), prevWeekStart); //Sun Jul 24 2016 00:00:00 USA standard!!!
    Date prevMonthStart = evalDatePrevPeriodStart(date, EPeriod.MONTHLY);
    System.out.println("t1 - prev month start is " + prevMonthStart);
    assertEquals(new Date(1467320400000L), prevMonthStart); //Thu Jul 01 2016 00:00:00

    date = new Date(1451649720000L); //Fri Jan 01 2016 15:02:00

    System.out.println("t2 - date is " + date);

    periodDayStart = evalDatePeriodStartFor(date, EPeriod.DAILY);
    System.out.println("t2 - period day start is " + periodDayStart);
    assertEquals(new Date(1451595600000L), periodDayStart); //Fri Jan 01 2016 00:00:00
    periodWeekStart = evalDatePeriodStartFor(date, EPeriod.WEEKLY);
    System.out.println("t2 - period week start is " + periodWeekStart);
    assertEquals(new Date(1451163600000L), periodWeekStart); //Sun Dec 27 2015 00:00:00 USA standard!!!
    periodMonthStart = evalDatePeriodStartFor(date, EPeriod.MONTHLY);
    System.out.println("t2 - period month start is " + periodMonthStart);
    assertEquals(new Date(1451595600000L), periodMonthStart); //Fri Jan 01 2016 00:00:00

    date = new Date(1483185720000L); // Sat Dec 31 2016 15:02:00
    System.out.println("t2 - date is " + date);
    nextDayStart = evalDateNextPeriodStart(date, EPeriod.DAILY);
    System.out.println("t2 - next day start is " + nextDayStart);
    assertEquals(new Date(1483218000000L), nextDayStart);
    nextWeekStart = evalDateNextPeriodStart(date, EPeriod.WEEKLY);
    System.out.println("t2 - next week start is " + nextWeekStart);
    assertEquals(new Date(1483218000000L), nextWeekStart); //Sun Jan 01 2017 00:00:00 USA standard!!!
    nextMonthStart = evalDateNextPeriodStart(date, EPeriod.MONTHLY);
    System.out.println("t2 - next month start is " + nextMonthStart);
    assertEquals(new Date(1483218000000L), nextMonthStart);

    prevDayStart = evalDatePrevPeriodStart(date, EPeriod.DAILY);
    System.out.println("t2 - prev day start is " + prevDayStart);
    assertEquals(new Date(1483045200000L), prevDayStart); //Fri Dec 30 2016 00:00:00
    prevWeekStart = evalDatePrevPeriodStart(date, EPeriod.WEEKLY);
    System.out.println("t2 - prev week start is " + prevWeekStart);
    assertEquals(new Date(1482008400000L), prevWeekStart); //Sun Jan 18 2016 00:00:00 USA standard!!!
    prevMonthStart = evalDatePrevPeriodStart(date, EPeriod.MONTHLY);
    System.out.println("t2 - prev month start is " + prevMonthStart);
    assertEquals(new Date(1477947600000L), prevMonthStart); //Nov 01 2016 00:00:00

  }
  
  Calendar cal = Calendar.getInstance(new Locale("en", "US"));

  /**
   * <p>Evaluate date start of next balance store period.</p>
   * @param pDateFor date for
   * @return Start of next period nearest to pDateFor
   * @throws Exception - an exception
   **/
  public final Date evalDateNextPeriodStart(Date pDateFor, EPeriod period) {
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    if (period.equals(EPeriod.DAILY)) {
      cal.add(Calendar.DATE, 1);
    } else if (period.equals(EPeriod.MONTHLY)) {
      cal.add(Calendar.MONTH, 1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (period.equals(EPeriod.WEEKLY)) {
      cal.add(Calendar.DAY_OF_YEAR, 7);
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }

  /**
   * <p>Evaluate date start of previous balance store period.</p>
   * @param pDateFor date for
   * @return Start of next period nearest to pDateFor
   * @throws Exception - an exception
   **/
  public final Date evalDatePrevPeriodStart(Date pDateFor, EPeriod period) {
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    if (period.equals(EPeriod.DAILY)) {
      cal.add(Calendar.DATE, -1);
    } else if (period.equals(EPeriod.MONTHLY)) {
      cal.add(Calendar.MONTH, -1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (period.equals(EPeriod.WEEKLY)) {
      cal.add(Calendar.DAY_OF_YEAR, -7);
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }

  /**
   * <p>Evaluate start of period nearest to pDateFor.</p>
   * @param pDateFor date for
   * @return Start of period nearest to pDateFor
   * @throws Exception - an exception
   **/
  public final synchronized Date evalDatePeriodStartFor(
    Date pDateFor, EPeriod period) {
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0); //Daily is ready
    if (period.equals(EPeriod.MONTHLY)) {
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (period.equals(EPeriod.WEEKLY)) {
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }
}
