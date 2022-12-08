package com.maripavlova.agroserver.meteo.utils;

import java.util.TimeZone;

public class DateUtils {

    /**
     * Function for getting next date of given string data
     * @param startTime the time presented in unix epochs timestamp
     * @return the array of two string of the next day time:
     *          1) dateString - presented in pattern dd.MM
     *          2) String.valueOf(new_time) - presented in unix epochs timestamp
     */
    public static String[] getNeedTime(String startTime){
        long stepForDay = 86400;
        long new_time = Long.parseLong(startTime)+stepForDay;
//        String date = new java.text.SimpleDateFormat("dd.MM").format(new java.util.Date (new_time*1000));

        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("dd.MM");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = date.format(new java.util.Date (new_time*1000));

        return new String[]{dateString, String.valueOf(new_time)};

    }

    /**
     * Function for getting current date of given string data in formatting
     * @param startTime - the time presented in unix epochs timestamp
     * @return the array of two string of the current day time:
     *                1) dateString - presented in pattern dd.MM
     *                2) String.valueOf(new_time) - presented in unix epochs timestamp
     */
    public static String[] getSimpleNeedTime(String startTime){
        long new_time = Long.parseLong(startTime);

        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("dd.MM");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = date.format(new java.util.Date (new_time*1000));

        return new String[]{dateString, String.valueOf(new_time)};

    }

    /**
     * Function for getting count of hours in current day given by startTimeStr
     * @param startTimeStr - the time presented in unix epochs timestamp
     * @param status
     *               -1 - first not full day
     *                0 - medium full day
     *                1 - last not full day
     * @return count of hours in current day given by startTimeStr
     */
    public static Integer getCountOfHourByEndDay(String startTimeStr, Byte status) {
        long startTime = Long.parseLong(startTimeStr);
        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("HH:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateString = date.format(new java.util.Date (startTime*1000));
        Integer hour = Integer.valueOf(dateString.split(":")[0]);
        if (status == -1) { //первый неполный день
            return 24 - hour - 1;
        }
        if (status == 1) {
            return hour + 1;
        }
        return 24;
    }

    /**
     * Function for getting count of full days between 2 date
     * @param startTimeStr - date presented in unix epochs timestamp
     * @param endTimeStr - date presented in unix epochs timestamp
     * @return count of full days between 2 date
     */
    public static Integer getDaysBetween(String startTimeStr, String endTimeStr) {
        long startTime = Long.parseLong(startTimeStr);
        long endTime = Long.parseLong(endTimeStr);
        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("dd");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startDateString = date.format(new java.util.Date (startTime*1000));
        String endDateString = date.format(new java.util.Date (endTime*1000));
        Integer between = Integer.valueOf(endDateString)-Integer.valueOf(startDateString);

        return between-1;
    }

    public static Integer getMonthBetween(String startTimeStr, String endTimeStr){
        long startTime = Long.parseLong(startTimeStr);
        long endTime = Long.parseLong(endTimeStr);
        java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("MM");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startDateString = date.format(new java.util.Date (startTime*1000));
        String endDateString = date.format(new java.util.Date (endTime*1000));
        Integer between = Integer.valueOf(endDateString)-Integer.valueOf(startDateString);

        return between+1;
    }
}
