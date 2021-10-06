package com.af.dateparser;


import edu.emory.mathcs.backport.java.util.Arrays;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Tester {

    @Test
    public void test() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();

        List<DateGroup> parsed = parser.parse("1/1/2021");
        Date date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(1 - 1, calendar.get(Calendar.MONTH));
        assertEquals(2021, calendar.get(Calendar.YEAR));

        parsed = parser.parse("1-1-2021");
        date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(1 - 1, calendar.get(Calendar.MONTH));
        assertEquals(2021, calendar.get(Calendar.YEAR));

        parsed = parser.parse("The day after 2021/10/4");
        date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);
        assertEquals(5, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(10 - 1, calendar.get(Calendar.MONTH));
        assertEquals(2021, calendar.get(Calendar.YEAR));

        parsed = parser.parse("2021/11/12");
        date = parsed.get(0).getDates().get(0);
        System.out.println(date);
    }

    @Test
    public void keywordDaysTest() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        Calendar currentCalender = Calendar.getInstance();
        currentCalender.setTime(currentDate);

        List<String> weekdays = Arrays.asList(new String[]{"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"});

        List<Integer> actual = new ArrayList<Integer>();
        List<Integer> expected = new ArrayList<Integer>();

        for (int i = 1; i <= 7; i++) {
            expected.add(i);
        }

        for (String day : weekdays) {
            List<DateGroup> parsed = parser.parse(day);
            Date date = parsed.get(0).getDates().get(0);
            calendar.setTime(date);

            actual.add(calendar.get(Calendar.DAY_OF_WEEK));

            assertEquals(currentCalender.get(Calendar.MONTH),calendar.get((Calendar.MONTH)));
            assertEquals(currentCalender.get(Calendar.YEAR),calendar.get((Calendar.YEAR)));
        }
        assertEquals(expected, actual);
    }

    @Test
    public void keywordMonthsTest() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        Calendar currentCalender = Calendar.getInstance();
        currentCalender.setTime(currentDate);

        List<String> months = Arrays.asList(new String[]{"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"});

        List<Integer> actual = new ArrayList<Integer>();
        List<Integer> expected = new ArrayList<Integer>();

        for (int i = 0; i < 12; i++) {
            expected.add(i);
        }

        for (String month : months) {
            List<DateGroup> parsed = parser.parse(month);
            Date date = parsed.get(0).getDates().get(0);
            calendar.setTime(date);
            actual.add(calendar.get(Calendar.MONTH));

            assertEquals(currentCalender.get(Calendar.YEAR),calendar.get((Calendar.YEAR)));
        }

        assertEquals(expected, actual);
    }

    @Test
    public void relativesToday() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        Calendar currentCalender = Calendar.getInstance();
        currentCalender.setTime(currentDate);

        List<String> relatives = Arrays.asList(new String[]{"one day ago", "1 day before now", "Today",
                "one day before tomorrow", "one day after yesterday", "Tomorrow", "Next day", "in 1 day",
                "in two days", "two days from today"});


        int currentDay = currentCalender.get((Calendar.DAY_OF_MONTH));
        List<Integer> expected = Arrays.asList(new Integer[]{currentDay - 1, currentDay - 1, currentDay, currentDay,
                currentDay, currentDay + 1, currentDay + 1, currentDay + 1, currentDay + 2, currentDay + 2});

        List<Integer> actual = new ArrayList<Integer>();
        for (String relative : relatives) {
            List<DateGroup> parsed = parser.parse(relative);
            Date date = parsed.get(0).getDates().get(0);
            calendar.setTime(date);
            actual.add(calendar.get(Calendar.DAY_OF_MONTH));

            assertEquals(currentCalender.get(Calendar.MONTH),calendar.get((Calendar.MONTH)));
            assertEquals(currentCalender.get(Calendar.YEAR),calendar.get((Calendar.YEAR)));
        }

        assertEquals(expected, actual);
    }

    @Test
    public void relativeDays() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        Calendar currentCalender = Calendar.getInstance();

        List<String> relatives = Arrays.asList(new String[]{"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"});

        Date getNextSunday= Util.getNextSunday(currentDate);
        currentCalender.setTime(getNextSunday);

        int nextSunday=currentCalender.get(Calendar.DAY_OF_MONTH);
        int lastMonthDay= currentCalender.getActualMaximum(Calendar.DATE);

        List<Integer> expected =new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
           expected.add((nextSunday+i) % lastMonthDay);
        }

        List<Integer> actual = new ArrayList<Integer>();
        for (String relative : relatives) {
            List<DateGroup> parsed = parser.parse("Next "+relative);
            Date date = parsed.get(0).getDates().get(0);
            calendar.setTime(date);
            actual.add(calendar.get(Calendar.DAY_OF_MONTH));

            assertEquals(currentCalender.get(Calendar.MONTH),calendar.get((Calendar.MONTH)));
            assertEquals(currentCalender.get(Calendar.YEAR),calendar.get((Calendar.YEAR)));
        }

        assertEquals(expected, actual);
    }

    @Test
    public void relativeMonths() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();
        Calendar currentCalender = Calendar.getInstance();
        List<DateGroup> parsed;

         parsed = parser.parse("Next Month");
        Date date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);
        assertEquals( currentCalender.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals( currentCalender.get(Calendar.MONTH)+1, calendar.get(Calendar.MONTH));
        assertEquals(currentCalender.get(Calendar.YEAR), calendar.get(Calendar.YEAR));

        parsed = parser.parse("Last Month");
        date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);
        assertEquals( currentCalender.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals( currentCalender.get(Calendar.MONTH)-1, calendar.get(Calendar.MONTH));
        assertEquals(currentCalender.get(Calendar.YEAR), calendar.get(Calendar.YEAR));

        parsed = parser.parse("First Monday in one month");
        date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);

        assertEquals( Calendar.MONDAY, calendar.get(Calendar.DAY_OF_WEEK));
        assertEquals( 1, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals( currentCalender.get(Calendar.MONTH)+1, calendar.get(Calendar.MONTH));
        assertEquals(currentCalender.get(Calendar.YEAR), calendar.get(Calendar.YEAR));

        parsed = parser.parse("Last Friday in next december");
        date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);

        assertEquals( Calendar.FRIDAY, calendar.get(Calendar.DAY_OF_WEEK));
        assertEquals( 31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals( Calendar.DECEMBER, calendar.get(Calendar.MONTH));
        assertEquals(currentCalender.get(Calendar.YEAR), calendar.get(Calendar.YEAR));

        parsed = parser.parse("in 3 months");
        date = parsed.get(0).getDates().get(0);
        calendar.setTime(date);

        assertEquals( (currentCalender.get(Calendar.MONTH)+3) %12, calendar.get(Calendar.MONTH));
        assertEquals(currentCalender.get(Calendar.YEAR)+1, calendar.get(Calendar.YEAR));
    }

    @Test
    public void datesVariations() {
        Parser parser = new Parser();
        Calendar calendar = Calendar.getInstance();

        List<String> weekdays = Arrays.asList(new String[]{"jan 1 2020","1-jan-2020","1/1/2020",
                "first day of 2020","1-1-2020","first of January 2020"});


        for (String day : weekdays) {
            List<DateGroup> parsed = parser.parse(day);
            Date date = parsed.get(0).getDates().get(0);
            calendar.setTime(date);

            assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
            assertEquals(1 - 1, calendar.get(Calendar.MONTH));
            assertEquals(2020, calendar.get(Calendar.YEAR));
        }
    }


}

