package com.af.dateparser;


import java.util.Calendar;
import java.util.Date;


public class Util {
    public static Date getNextSunday(Date today) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(today);

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        while (dow != Calendar.SUNDAY) {
            int date = cal.get(Calendar.DATE);

            int month = cal.get(Calendar.MONTH);

            int year = cal.get(Calendar.YEAR);

            if (date == getMonthLastDate(month, year)) {

                if (month == Calendar.DECEMBER) {
                    month = Calendar.JANUARY;

                    cal.set(Calendar.YEAR, year + 1);
                } else {
                    month++;
                }

                cal.set(Calendar.MONTH, month);

                date = 1;
            } else {
                date++;
            }

            cal.set(Calendar.DATE, date);

            dow = cal.get(Calendar.DAY_OF_WEEK);
        }

        return cal.getTime();
    }

    private static int getMonthLastDate(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;

            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;

            default:    //  Calendar.FEBRUARY
                return year % 4 == 0 ? 29 : 28;
        }
    }
}