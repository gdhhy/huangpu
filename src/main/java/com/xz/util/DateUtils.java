/*
 * Created on 2004-2-7
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.xz.util;

import com.xz.ExceptionAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author 黄海晏
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DateUtils {
    static Logger logger = LogManager.getLogger(DateUtils.class);

    /**
     * 把日期字符串转换成日期
     *
     * @param dateString 日期字符串 如："2004-02-12 12:30:10"
     * @param partten    日期表达式 如："yyyy-MM-dd HH:mm:ss"
     * @return java.util.Date
     */
    public static Date StringToDate(String dateString, String partten) {
        SimpleDateFormat df = new SimpleDateFormat(partten);
        return df.parse(dateString, new ParsePosition(0));
    }

    public static java.sql.Date toSQLDate(String dateString, String partten) {
        SimpleDateFormat df = new SimpleDateFormat(partten);
        return DateToSQLDate(df.parse(dateString, new ParsePosition(0)));
    }

    /**
     * 把java.util.Date转换成java.sql.Date
     *
     * @param date java.util.Date
     * @return java.sql.Date
     */
    public static java.sql.Date DateToSQLDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static String toString(Date date, String partten) {
        if (date == null)
            return "";
        SimpleDateFormat df = new SimpleDateFormat(partten);
        return df.format(date);
    }

    public static void main(String[] args) throws Exception {

        String sql = "2017-11-19";
       String ss= DateUtils.nextDaySqlStr(sql);
        System.out.println("ss = " + ss);
    }

    private static final int[] dayArray = new int[]
            {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static synchronized Calendar getCalendar() {
        return GregorianCalendar.getInstance();
    }

    /**
     * @return String
     */
    public static synchronized String getDateMilliFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateMilliFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateMilliFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateMilliFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarMilliFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateMilliFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss,SSS";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateSecondFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateSecondFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateSecondFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateSecondFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarSecondFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateSecondFormat(String strDate) {
        if (strDate == null) return null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateMinuteFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateMinuteFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateMinuteFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateMinuteFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarMinuteFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateMinuteFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateDayFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateDayFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateDayFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateDayFormat(Date date) {
        String pattern = "yyyy-MM-dd";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarDayFormat(String strDate) {
        String pattern = "yyyy-MM-dd";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateDayFormat(String strDate) {
        String pattern = "yyyy-MM-dd";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateFileFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateFileFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateFileFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateFileFormat(Date date) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarFileFormat(String strDate) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateFileFormat(String strDate) {
        String pattern = "yyyy-MM-dd_HH-mm-ss";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @return String
     */
    public static synchronized String getDateW3CFormat() {
        Calendar cal = Calendar.getInstance();
        return getDateW3CFormat(cal);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateW3CFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateW3CFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarW3CFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateW3CFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @param cal
     * @return String
     */
    public static synchronized String getDateFormat(Calendar cal) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getDateFormat(cal, pattern);
    }

    /**
     * @param date
     * @return String
     */
    public static synchronized String getDateFormat(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return getDateFormat(date, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseCalendarFormat(strDate, pattern);
    }

    /**
     * @param strDate
     * @return java.util.Date
     */
    public static synchronized Date parseDateFormat(String strDate) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return parseDateFormat(strDate, pattern);
    }

    /**
     * @param cal
     * @param pattern
     * @return String
     */
    public static synchronized String getDateFormat(Calendar cal, String pattern) {
        return getDateFormat(cal.getTime(), pattern);
    }

    /**
     * @param date
     * @param pattern
     * @return String
     */
    public static synchronized String getDateFormat(Date date, String pattern) {
        synchronized (sdf) {
            String str = null;
            sdf.applyPattern(pattern);
            str = sdf.format(date);
            return str;
        }
    }

    /**
     * @param strDate
     * @param pattern
     * @return java.util.Calendar
     */
    public static synchronized Calendar parseCalendarFormat(String strDate, String pattern) {
        synchronized (sdf) {
            Calendar cal = null;
            sdf.applyPattern(pattern);
            try {
                sdf.parse(strDate);
                cal = sdf.getCalendar();
            } catch (Exception e) {
            }
            return cal;
        }
    }

    /**
     * @param strDate
     * @param pattern
     * @return java.util.Date
     */
    public static synchronized Date parseDateFormat(String strDate, String pattern) {
        synchronized (sdf) {
            Date date = null;
            sdf.applyPattern(pattern);
            try {
                date = sdf.parse(strDate);
            } catch (Exception e) {
            }
            return date;
        }
    }

    public static synchronized int getLastDayOfMonth(int month) {
        if (month < 1 || month > 12) {
            return -1;
        }
        int retn = 0;
        if (month == 2) {
            if (isLeapYear()) {
                retn = 29;
            } else {
                retn = dayArray[month - 1];
            }
        } else {
            retn = dayArray[month - 1];
        }
        return retn;
    }

    public static synchronized int getLastDayOfMonth(int year, int month) {
        if (month < 1 || month > 12) {
            return -1;
        }
        int retn = 0;
        if (month == 2) {
            if (isLeapYear(year)) {
                retn = 29;
            } else {
                retn = dayArray[month - 1];
            }
        } else {
            retn = dayArray[month - 1];
        }
        return retn;
    }

    public static synchronized boolean isLeapYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    public static synchronized boolean isLeapYear(int year) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0) {
            if ((year % 100) == 0)
                return false;
            else
                return true;
        } else
            return false;
    }

    /**
     * 判断指定日期的年份是否是闰年
     *
     * @param date 指定日期。
     * @return 是否闰年
     */
    public static synchronized boolean isLeapYear(Date date) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
//  int year = date.getYear();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        int year = gc.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    public static synchronized boolean isLeapYear(Calendar gc) {
        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        int year = gc.get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 得到指定日期的前一个工作日
     *
     * @param date 指定日期。
     * @return 指定日期的前一个工作日
     */
    public static synchronized Date getPreviousWeekDay(Date date) {
        {
            /**
             * 详细设计：
             * 1.如果date是星期日，则减3天
             * 2.如果date是星期六，则减2天
             * 3.否则减1天
             */
            GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
            gc.setTime(date);
            return getPreviousWeekDay(gc);
//   switch ( gc.get( Calendar.DAY_OF_WEEK ) )
//   {
//    case ( Calendar.MONDAY    ):
//     gc.add( Calendar.DATE, -3 );
//     break;
//    case ( Calendar.SUNDAY    ):
//     gc.add( Calendar.DATE, -2 );
//     break;
//    default:
//     gc.add( Calendar.DATE, -1 );
//     break;
//   }
//   return gc.getTime();
        }
    }

    public static synchronized Date getPreviousWeekDay(Calendar gc) {
        {
            /**
             * 详细设计：
             * 1.如果date是星期日，则减3天
             * 2.如果date是星期六，则减2天
             * 3.否则减1天
             */
            switch (gc.get(Calendar.DAY_OF_WEEK)) {
                case (Calendar.MONDAY):
                    gc.add(Calendar.DATE, -3);
                    break;
                case (Calendar.SUNDAY):
                    gc.add(Calendar.DATE, -2);
                    break;
                default:
                    gc.add(Calendar.DATE, -1);
                    break;
            }
            return gc.getTime();
        }
    }

    /**
     * 得到指定日期的后一个工作日
     *
     * @param date 指定日期。
     * @return 指定日期的后一个工作日
     */
    public static synchronized Date getNextWeekDay(Date date) {
        /**
         * 详细设计：
         * 1.如果date是星期五，则加3天
         * 2.如果date是星期六，则加2天
         * 3.否则加1天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, 3);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, 2);
                break;
            default:
                gc.add(Calendar.DATE, 1);
                break;
        }
        return gc.getTime();
    }

    public static synchronized Calendar getNextWeekDay(Calendar gc) {
        /**
         * 详细设计：
         * 1.如果date是星期五，则加3天
         * 2.如果date是星期六，则加2天
         * 3.否则加1天
         */
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, 3);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, 2);
                break;
            default:
                gc.add(Calendar.DATE, 1);
                break;
        }
        return gc;
    }

    /**
     * 取得指定日期的下一个月的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个月的最后一天
     */
    public static synchronized Date getLastDayOfNextMonth(Date date) {
        /**
         * 详细设计：
         * 1.调用getNextMonth设置当前时间
         * 2.以1为基础，调用getLastDayOfMonth
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtils.getNextMonth(gc.getTime()));
        gc.setTime(DateUtils.getLastDayOfMonth(gc.getTime()));
        return gc.getTime();
    }

    /**
     * 取得指定日期的下一个星期的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个星期的最后一天
     */
    public static synchronized Date getLastDayOfNextWeek(Date date) {
        /**
         * 详细设计：
         * 1.调用getNextWeek设置当前时间
         * 2.以1为基础，调用getLastDayOfWeek
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtils.getNextWeek(gc.getTime()));
        gc.setTime(DateUtils.getLastDayOfWeek(gc.getTime()));
        return gc.getTime();
    }

    /**
     * 取得指定日期的下一个月的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个月的第一天
     */
    public static synchronized Date getFirstDayOfNextMonth(Date date) {
        /**
         * 详细设计：
         * 1.调用getNextMonth设置当前时间
         * 2.以1为基础，调用getFirstDayOfMonth
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtils.getNextMonth(gc.getTime()));
        gc.setTime(DateUtils.getFirstDayOfMonth(gc.getTime()));
        return gc.getTime();
    }

    public static synchronized Calendar getFirstDayOfNextMonth(Calendar gc) {
        /**
         * 详细设计：
         * 1.调用getNextMonth设置当前时间
         * 2.以1为基础，调用getFirstDayOfMonth
         */
        gc.setTime(DateUtils.getNextMonth(gc.getTime()));
        gc.setTime(DateUtils.getFirstDayOfMonth(gc.getTime()));
        return gc;
    }

    /**
     * 取得指定日期的下一个星期的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一个星期的第一天
     */
    public static synchronized Date getFirstDayOfNextWeek(Date date) {
        /**
         * 详细设计：
         * 1.调用getNextWeek设置当前时间
         * 2.以1为基础，调用getFirstDayOfWeek
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.setTime(DateUtils.getNextWeek(gc.getTime()));
        gc.setTime(DateUtils.getFirstDayOfWeek(gc.getTime()));
        return gc.getTime();
    }

    public static synchronized Calendar getFirstDayOfNextWeek(Calendar gc) {
        /**
         * 详细设计：
         * 1.调用getNextWeek设置当前时间
         * 2.以1为基础，调用getFirstDayOfWeek
         */
        gc.setTime(DateUtils.getNextWeek(gc.getTime()));
        gc.setTime(DateUtils.getFirstDayOfWeek(gc.getTime()));
        return gc;
    }

    /**
     * 取得指定日期的下一个月
     *
     * @param date 指定日期。
     * @return 指定日期的下一个月
     */
    public static synchronized Date getNextMonth(Date date) {
        /**
         * 详细设计：
         * 1.指定日期的月份加1
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.MONTH, 1);
        return gc.getTime();
    }

    public static synchronized Calendar getNextMonth(Calendar gc) {
        /**
         * 详细设计：
         * 1.指定日期的月份加1
         */
        gc.add(Calendar.MONTH, 1);
        return gc;
    }

    /**
     * 取得指定日期的下一天
     *
     * @param date 指定日期。
     * @return 指定日期的下一天
     */
    public static synchronized Date getNextDay(Date date) {
        /**
         * 详细设计： 1.指定日期加1天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return gc.getTime();
    }

    public static synchronized Calendar getNextDay(Calendar gc) {
        /**
         * 详细设计： 1.指定日期加1天
         */
        gc.add(Calendar.DATE, 1);
        return gc;
    }

    /**
     * 取得指定日期的下一个星期
     *
     * @param date 指定日期。
     * @return 指定日期的下一个星期
     */
    public static synchronized Date getNextWeek(Date date) {
        /**
         * 详细设计：
         * 1.指定日期加7天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 7);
        return gc.getTime();
    }

    public static synchronized Calendar getNextWeek(Calendar gc) {
        /**
         * 详细设计：
         * 1.指定日期加7天
         */
        gc.add(Calendar.DATE, 7);
        return gc;
    }

    /**
     * 取得指定日期的所处星期的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处星期的最后一天
     */
    public static synchronized Date getLastDayOfWeek(Date date) {
        /**
         * 详细设计：
         * 1.如果date是星期日，则加6天
         * 2.如果date是星期一，则加5天
         * 3.如果date是星期二，则加4天
         * 4.如果date是星期三，则加3天
         * 5.如果date是星期四，则加2天
         * 6.如果date是星期五，则加1天
         * 7.如果date是星期六，则加0天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.SUNDAY):
                gc.add(Calendar.DATE, 6);
                break;
            case (Calendar.MONDAY):
                gc.add(Calendar.DATE, 5);
                break;
            case (Calendar.TUESDAY):
                gc.add(Calendar.DATE, 4);
                break;
            case (Calendar.WEDNESDAY):
                gc.add(Calendar.DATE, 3);
                break;
            case (Calendar.THURSDAY):
                gc.add(Calendar.DATE, 2);
                break;
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, 1);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, 0);
                break;
        }
        return gc.getTime();
    }

    /**
     * 取得指定日期的所处星期的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处星期的第一天
     */
    public static synchronized Date getFirstDayOfWeek(Date date) {
        /**
         * 详细设计：
         * 1.如果date是星期日，则减0天
         * 2.如果date是星期一，则减1天
         * 3.如果date是星期二，则减2天
         * 4.如果date是星期三，则减3天
         * 5.如果date是星期四，则减4天
         * 6.如果date是星期五，则减5天
         * 7.如果date是星期六，则减6天
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.SUNDAY):
                gc.add(Calendar.DATE, 0);
                break;
            case (Calendar.MONDAY):
                gc.add(Calendar.DATE, -1);
                break;
            case (Calendar.TUESDAY):
                gc.add(Calendar.DATE, -2);
                break;
            case (Calendar.WEDNESDAY):
                gc.add(Calendar.DATE, -3);
                break;
            case (Calendar.THURSDAY):
                gc.add(Calendar.DATE, -4);
                break;
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, -5);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, -6);
                break;
        }
        return gc.getTime();
    }

    public static synchronized Calendar getFirstDayOfWeek(Calendar gc) {
        /**
         * 详细设计：
         * 1.如果date是星期日，则减0天
         * 2.如果date是星期一，则减1天
         * 3.如果date是星期二，则减2天
         * 4.如果date是星期三，则减3天
         * 5.如果date是星期四，则减4天
         * 6.如果date是星期五，则减5天
         * 7.如果date是星期六，则减6天
         */
        switch (gc.get(Calendar.DAY_OF_WEEK)) {
            case (Calendar.SUNDAY):
                gc.add(Calendar.DATE, 0);
                break;
            case (Calendar.MONDAY):
                gc.add(Calendar.DATE, -1);
                break;
            case (Calendar.TUESDAY):
                gc.add(Calendar.DATE, -2);
                break;
            case (Calendar.WEDNESDAY):
                gc.add(Calendar.DATE, -3);
                break;
            case (Calendar.THURSDAY):
                gc.add(Calendar.DATE, -4);
                break;
            case (Calendar.FRIDAY):
                gc.add(Calendar.DATE, -5);
                break;
            case (Calendar.SATURDAY):
                gc.add(Calendar.DATE, -6);
                break;
        }
        return gc;
    }

    /**
     * 取得指定日期的所处月份的最后一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处月份的最后一天
     */
    public static synchronized Date getLastDayOfMonth(Date date) {
        /**
         * 详细设计：
         * 1.如果date在1月，则为31日
         * 2.如果date在2月，则为28日
         * 3.如果date在3月，则为31日
         * 4.如果date在4月，则为30日
         * 5.如果date在5月，则为31日
         * 6.如果date在6月，则为30日
         * 7.如果date在7月，则为31日
         * 8.如果date在8月，则为31日
         * 9.如果date在9月，则为30日
         * 10.如果date在10月，则为31日
         * 11.如果date在11月，则为30日
         * 12.如果date在12月，则为31日
         * 1.如果date在闰年的2月，则为29日
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        switch (gc.get(Calendar.MONTH)) {
            case 0:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 1:
                gc.set(Calendar.DAY_OF_MONTH, 28);
                break;
            case 2:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 4:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 5:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 6:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 7:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 8:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 9:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 10:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 11:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        //检查闰年
        if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY)
                && (isLeapYear(gc.get(Calendar.YEAR)))) {
            gc.set(Calendar.DAY_OF_MONTH, 29);
        }
        return gc.getTime();
    }

    public static synchronized Calendar getLastDayOfMonth(Calendar gc) {
        /**
         * 详细设计：
         * 1.如果date在1月，则为31日
         * 2.如果date在2月，则为28日
         * 3.如果date在3月，则为31日
         * 4.如果date在4月，则为30日
         * 5.如果date在5月，则为31日
         * 6.如果date在6月，则为30日
         * 7.如果date在7月，则为31日
         * 8.如果date在8月，则为31日
         * 9.如果date在9月，则为30日
         * 10.如果date在10月，则为31日
         * 11.如果date在11月，则为30日
         * 12.如果date在12月，则为31日
         * 1.如果date在闰年的2月，则为29日
         */
        switch (gc.get(Calendar.MONTH)) {
            case 0:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 1:
                gc.set(Calendar.DAY_OF_MONTH, 28);
                break;
            case 2:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 3:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 4:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 5:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 6:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 7:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 8:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 9:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
            case 10:
                gc.set(Calendar.DAY_OF_MONTH, 30);
                break;
            case 11:
                gc.set(Calendar.DAY_OF_MONTH, 31);
                break;
        }
        //检查闰年
        if ((gc.get(Calendar.MONTH) == Calendar.FEBRUARY)
                && (isLeapYear(gc.get(Calendar.YEAR)))) {
            gc.set(Calendar.DAY_OF_MONTH, 29);
        }
        return gc;
    }

    /**
     * 取得指定日期的所处月份的第一天
     *
     * @param date 指定日期。
     * @return 指定日期的所处月份的第一天
     */
    public static synchronized Date getFirstDayOfMonth(Date date) {
        /**
         * 详细设计： 1.设置为1号
         */
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        return gc.getTime();
    }

    public static synchronized Calendar getFirstDayOfMonth(Calendar gc) {
        /**
         * 详细设计： 1.设置为1号
         */
        gc.set(Calendar.DAY_OF_MONTH, 1);
        return gc;
    }

    /**
     * 将日期对象转换成为指定ORA日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
     *
     * @param theDate 将要转换为字符串的日期对象。
     * @param hasTime 如果返回的字符串带时间则为true
     * @return 转换的结果
     */
    public static synchronized String toOraString(Date theDate, boolean hasTime) {
        /**
         * 详细设计：
         * 1.如果有时间，则设置格式为getOraDateTimeFormat()的返回值
         * 2.否则设置格式为getOraDateFormat()的返回值
         * 3.调用toString(Date theDate, DateFormat
         * theDateFormat)
         */
        DateFormat theFormat;
        if (hasTime) {
            theFormat = getOraDateTimeFormat();
        } else {
            theFormat = getOraDateFormat();
        }
        return toString(theDate, theFormat);
    }

    /**
     * 将日期对象转换成为指定日期、时间格式的字符串形式。如果日期对象为空，返回 一个空字符串对象，而不是一个空对象。
     *
     * @param theDate 将要转换为字符串的日期对象。
     * @param hasTime 如果返回的字符串带时间则为true
     * @return 转换的结果
     */
    public static synchronized String toString(Date theDate, boolean hasTime) {
        /**
         * 详细设计：
         * 1.如果有时间，则设置格式为getDateTimeFormat的返回值
         * 2.否则设置格式为getDateFormat的返回值
         * 3.调用toString(Date theDate, DateFormat theDateFormat)
         */
        DateFormat theFormat;
        if (hasTime) {
            theFormat = getDateTimeFormat();
        } else {
            theFormat = getDateFormat();
        }
        return toString(theDate, theFormat);
    }

    /**
     * 标准日期格式
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    /**
     * 标准时间格式
     */
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    /**
     * 带时分秒的标准时间格式
     */
    //private static final SimpleDateFormat DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    /**
     * ORA标准日期格式
     */
    private static final SimpleDateFormat ORA_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    /**
     * ORA标准时间格式
     */
    private static final SimpleDateFormat ORA_DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");
    /**
     * 带时分秒的ORA标准时间格式
     */
    //private static final SimpleDateFormat ORA_DATE_TIME_EXTENDED_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 创建一个标准日期格式的克隆
     *
     * @return 标准日期格式的克隆
     */
    public static synchronized DateFormat getDateFormat() {
        /**
         * 详细设计： 1.返回DATE_FORMAT
         */
        SimpleDateFormat theDateFormat = (SimpleDateFormat)
                DATE_FORMAT.clone();
        theDateFormat.setLenient(false);
        return theDateFormat;
    }

    /**
     * 创建一个标准时间格式的克隆
     *
     * @return 标准时间格式的克隆
     */
    public static synchronized DateFormat getDateTimeFormat() {
        /**
         * 详细设计： 1.返回DATE_TIME_FORMAT
         */
        SimpleDateFormat theDateTimeFormat = (SimpleDateFormat) DATE_TIME_FORMAT
                .clone();
        theDateTimeFormat.setLenient(false);
        return theDateTimeFormat;
    }

    /**
     * 创建一个标准ORA日期格式的克隆
     *
     * @return 标准ORA日期格式的克隆
     */
    public static synchronized DateFormat getOraDateFormat() {
        /**
         * 详细设计： 1.返回ORA_DATE_FORMAT
         */
        SimpleDateFormat theDateFormat = (SimpleDateFormat) ORA_DATE_FORMAT
                .clone();
        theDateFormat.setLenient(false);
        return theDateFormat;
    }

    /**
     * 创建一个标准ORA时间格式的克隆
     *
     * @return 标准ORA时间格式的克隆
     */
    public static synchronized DateFormat getOraDateTimeFormat() {
        /**
         * 详细设计： 1.返回ORA_DATE_TIME_FORMAT
         */
        SimpleDateFormat theDateTimeFormat = (SimpleDateFormat)
                ORA_DATE_TIME_FORMAT.clone();
        theDateTimeFormat.setLenient(false);
        return theDateTimeFormat;
    }

    /**
     * 将一个日期对象转换成为指定日期、时间格式的字符串。 如果日期对象为空，返回一个空字符串，而不是一个空对象。
     *
     * @param theDate       要转换的日期对象
     * @param theDateFormat 返回的日期字符串的格式
     * @return 转换结果
     */
    public static synchronized String toString(Date theDate,
                                               DateFormat theDateFormat) {
        /**
         * 详细设计：
         * 1.theDate为空，则返回""
         * 2.否则使用theDateFormat格式化
         */
        if (theDate == null)
            return "";
        return theDateFormat.format(theDate);
    }

    private static SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat localDateTimeFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private static SimpleDateFormat sqlDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatDate(Date date) {
        if (date == null) return "";
        return localDateFormat.format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return localDateTimeFormat.format(date);
    }

    public static String formatSqlDate(Date date) {
        if (date == null) return "";
        return sqlDateFormat.format(date);
    }

    /*public static String formatSqlDate(Date date){
        if(date==null) return "";
        return sqlDateFormat.format(date);
    }*/
    public static String formatSqlDateTime(Date date) {
        if (date == null) return "";
        return sqlDateTimeFormat.format(date);
    }

    public static Date parseSqlDate(String date) {
        try {
            return sqlDateFormat.parse(date);
        } catch (ParseException e) {
            throw new ExceptionAdapter(e);
        }
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String nextDaySqlStr(String sqlDate) {
        if (sqlDate != null && !"".equals(sqlDate))
            try {
                Date date = sqlDateFormat.parse(sqlDate);
                GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, 1);
                return sqlDateFormat.format(cal.getTime());
            } catch (Exception e) {
                logger.debug("sqlDate:" + sqlDate);
                e.printStackTrace();
            }
        return "";
    }

    public static String getQuarterFirstDay(String sqlDate) {
        try {
            Date date = sqlDateFormat.parse(sqlDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //System.out.println("cal.get(Calendar.MONTH)  = " + cal.get(Calendar.MONTH));
            int kk = (cal.get(Calendar.MONTH) / 3) * 3;
            System.out.println(kk);
            cal.set(Calendar.MONTH, kk);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            return sqlDateFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getQuarter(String sqlDate) {
        try {
            Date date = sqlDateFormat.parse(sqlDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //System.out.println("cal.get(Calendar.MONTH)  = " + cal.get(Calendar.MONTH));
            int kk = (cal.get(Calendar.MONTH) / 3) + 1;
            return cal.get(Calendar.YEAR) + "0" + kk;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getLastQuarter(String sqlDate) {
        try {
            Date date = sqlDateFormat.parse(sqlDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            //System.out.println("cal.get(Calendar.MONTH)  = " + cal.get(Calendar.MONTH));
            int kk = (cal.get(Calendar.MONTH) / 3);
            int year = cal.get(Calendar.YEAR);
            if (kk == 0) {
                year--;
                kk = 4;
            }
            return year + "0" + kk;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
