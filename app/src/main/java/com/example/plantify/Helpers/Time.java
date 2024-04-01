package com.example.plantify.Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    static String pattern = "dd-MM-yyyy";
    String timePatter = "HH:mm";

   public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    public SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timePatter);

    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public SimpleDateFormat getSimpleTimeFormat() {
        return simpleTimeFormat;
    }

    public strictfp String DateFormat(Date date) {
        return simpleDateFormat.format(date);
    }
    public strictfp String TimeFormat(Date date) {
        return simpleTimeFormat.format(date);
    }
    public strictfp String DatePicerkFormatToDate(int day, int month, int year) {
        return (day<=9?"0"+day+"-":day+"-") +(month<=9?"0"+(month+1)+"-":(month+1)+"-")+year;
    }
}
