package com.pakpublishers.infosquare.envision.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * Created by M Asad Hashmi on 10/13/2016.
 */

public class TimeZone_App {
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String DATEFORMAT_12 = "yyyy-MM-dd 'T' HH:mm:ss";
    public static Date getUTCTime(Date d)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getTimeZone("utc"));
        calendar.setTime(d);
        Date time = calendar.getTime();
        return time;

    }
    public static Date GetUTCdatetime(Date d) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(d);
        final SimpleDateFormat sdd = new SimpleDateFormat(DATEFORMAT);
        Date nd=sdd.parse(utcTime);
        return nd;
    }

    public static int getoffsetdifference()
    {
       TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000;
        return offsetFromUtc;//mGMTOffset;
    }
    public static Date GetLocaldatetime(Date d) throws ParseException {

        Date localTime = new Date();



//        final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_12);
     //   TimeZone defaultTimeZone = TimeZone.getDefault();
      //  String strDefaultTimeZone = defaultTimeZone.getDisplayName(false, TimeZone.SHORT);
  //      sdf.setTimeZone(tz);//TimeZone.getTimeZone(strDefaultTimeZone));
    //    final String utcTime = sdf.format(d);
      //  final SimpleDateFormat sdd = new SimpleDateFormat(DATEFORMAT_12);
        Date nd=new Date(d.getTime() + TimeZone.getDefault().getOffset(localTime.getTime()));//sdd.parse(utcTime);
        return nd;
    }

}
