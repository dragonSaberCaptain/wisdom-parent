package com.wisdom.tools.datetime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote Java8日期时间工具类
 * ZonedDateTime：最完整的日期时间，包含时区和相对UTC或格林威治的时差。
 * @date 2021/6/28 14:28 星期一
 */
public class DateUtilByZoned {
    //****************************************************************【main start】**************************************************************

    /**
     * 方法主函数，用于测试
     *
     * @author created by captain on 2021-06-28 17:18:59
     */
    public static void main(String[] args) {
        String dataTimeStr = "2021-07-05 17:18:59";
        String dataTimeStr1 = "2021-07-06 17:18:59";
        String dataTimeStr2 = "2021-07-07 17:18:59";
        String dataTimeStr3 = "2021-07-08 17:18:59";
        String dataTimeStr4 = "2021-07-09 17:18:59";
        String dataTimeStr5 = "2021-07-10 17:18:59";
        String dataTimeStr6 = "2021-07-11 17:18:59";

        System.out.println(getDayOfWeekCn(getParse(dataTimeStr)));
        System.out.println(getDayOfWeekCn(getParse(dataTimeStr1)));
        System.out.println(getDayOfWeekCn(getParse(dataTimeStr2)));
        System.out.println(getDayOfWeekCn(getParse(dataTimeStr3)));
        System.out.println(getDayOfWeekCn(getParse(dataTimeStr4)));
        System.out.println(getDayOfWeekCn(getParse(dataTimeStr5)));
        System.out.println(getDayOfWeekCn(getParse(dataTimeStr6)));
    }

    //****************************************************************【main end】**************************************************************
    public static boolean isLeapYear(ZonedDateTime sourceDateTime) {
        return sourceDateTime.toLocalDate().isLeapYear();
    }

    public static boolean isLeapYear(String sourceDateTime) {
        return isLeapYear(getParse(sourceDateTime));
    }

    public static int getYear(ZonedDateTime sourceDateTime) {
        return sourceDateTime.toLocalDate().getYear();
    }

    public static int getYear(String sourceDateTime) {
        return getYear(getParse(sourceDateTime));
    }

    public static boolean isEqual(ZonedDateTime sourceDateTime, ZonedDateTime targetDateTime) {
        return sourceDateTime.equals(targetDateTime);
    }

    public static boolean isEqual(String sourceDateTime, String targetDateTime) {
        return isEqual(getParse(sourceDateTime), getParse(targetDateTime));
    }

    /**
     * 字符串 转 ZonedDateTime
     *
     * @author created by captain on 2021-06-28 17:21:24
     */
    public static ZonedDateTime getParse(String dateTime) {
        return getParse(dateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static ZonedDateTime getParseUn(String dateTime) {
        return getParse(dateTime, "yyyyMMddHHmmss");
    }

    public static ZonedDateTime getParseUnMilli(String dateTime) {
        return getParse(dateTime, "yyyyMMddHHmmssSSS");
    }


    /**
     * 字符串 转 ZonedDateTime
     *
     * @author created by captain on 2021-06-28 17:21:24
     */
    public static ZonedDateTime getParse(String dateTime, String pattern) {
        return ZonedDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()));
    }

    /**
     * 判断某个时间是否在某个时间范围之间
     *
     * @param sourceDateTime 时间源
     * @param startDateTime  开始时间
     * @param endDateTime    结束时间
     * @author created by captain on 2021-07-01 16:35:58
     */
    public static boolean isInRange(ZonedDateTime sourceDateTime, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            ZonedDateTime tmpZonedDateTime = startDateTime;
            startDateTime = endDateTime;
            endDateTime = tmpZonedDateTime;
        }
        return sourceDateTime.isAfter(startDateTime) && sourceDateTime.isBefore(endDateTime);
    }

    /**
     * 判断某个时间是否在开始时间到系统当前时间之间
     *
     * @param sourceDateTime 时间源
     * @param startDateTime  开始时间
     * @author created by captain on 2021-07-01 16:35:58
     */
    public static boolean isInRange(ZonedDateTime sourceDateTime, ZonedDateTime startDateTime) {
        return isInRange(sourceDateTime, startDateTime, getNow());
    }

    public static boolean isInRange(String sourceDateTime, String startDateTime, String endDateTime) {
        return isInRange(getParse(sourceDateTime), getParse(startDateTime), getParse(endDateTime));
    }

    public static boolean isInRange(String sourceDateTime, String startDateTime) {
        return isInRange(getParse(sourceDateTime), getParse(startDateTime), getNow());
    }

    /**
     * 根据条件，获取时间字符串. 例如：获取当月第一个星期天的时间、获取当月最后一天的时间等等
     *
     * @param zonedDateTime    时间源
     * @param temporalAdjuster 条件 例如：TemporalAdjusters.firstDayOfMonth()、TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.SUNDAY)
     * @param pattern          时间格式
     * @author created by captain on 2021-07-01 15:35:30
     */
    public static String getDayOfStr(ZonedDateTime zonedDateTime, TemporalAdjuster temporalAdjuster, String pattern) {
        return getPattern(zonedDateTime.with(temporalAdjuster), pattern);
    }

    public static String getDayOfStr(String dateTime, TemporalAdjuster temporalAdjuster, String pattern) {
        return getPattern(getParse(dateTime).with(temporalAdjuster), pattern);
    }


    /**
     * 根据时间单位,计算两个日期的时间间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param chronoUnit    日期时间单位
     * @author created by captain on 2021-07-01 15:29:14
     */
    public static long getChronoUnitBetween(ZonedDateTime startDateTime, ZonedDateTime endDateTime, ChronoUnit chronoUnit) {
        return Math.abs(startDateTime.until(endDateTime, chronoUnit));
    }

    public static long getChronoUnitBetween(String start, String end, ChronoUnit chronoUnit) {
        return Math.abs(getParse(start).until(getParse(end), chronoUnit));
    }

    public static long getChronoUnitBetweenUn(String start, String end, ChronoUnit chronoUnit) {
        return Math.abs(getParseUn(start).until(getParseUn(end), chronoUnit));
    }

    public static long getChronoUnitBetweenUnMilli(String start, String end, ChronoUnit chronoUnit) {
        return Math.abs(getParseUnMilli(start).until(getParseUnMilli(end), chronoUnit));
    }

    /**
     * 根据时间单位,计算指的时间减去 num 后得到的时间
     *
     * @param zonedDateTime 日期时间
     * @param num           数量
     * @param chronoUnit    日期时间单位
     */
    public static String getMinus(ZonedDateTime zonedDateTime, long num, ChronoUnit chronoUnit) {
        return getPattern(zonedDateTime.minus(num, chronoUnit));
    }

    public static String getMinus(String dateTime, long num, ChronoUnit chronoUnit) {
        return getMinus(getParse(dateTime), num, chronoUnit);
    }

    /**
     * 根据时间单位,计算指的时间加上 num 后得到的时间
     *
     * @param zonedDateTime 日期时间
     * @param num           数量
     * @param chronoUnit    日期时间单位
     * @author created by captain on 2021-07-01 15:32:56
     */
    public static String getPlus(ZonedDateTime zonedDateTime, long num, ChronoUnit chronoUnit) {
        return getPattern(zonedDateTime.plus(num, chronoUnit));
    }

    public static String getPlus(String dateTime, int num, ChronoUnit chronoUnit) {
        return getPlus(getParse(dateTime), num, chronoUnit);
    }

    /**
     * ZonedDateTime 转 字符串 自定义格式
     *
     * @author created by captain on 2021-06-28 17:22:01
     */
    public static String getPattern(ZonedDateTime zonedDateTime, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(zonedDateTime);
    }

    public static String getPatternUn(ZonedDateTime zonedDateTime) {
        return getPattern(zonedDateTime, "yyyyMMddHHmmss");
    }

    public static String getPattern(ZonedDateTime zonedDateTime) {
        return getPattern(zonedDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static ZonedDateTime getNow() {
        return ZonedDateTime.now();
    }

    /**
     * 获取当前时间字符串 自定义格式
     *
     * @author created by captain on 2021-06-28 17:23:06
     */
    public static String getNowDate(String pattern) {
        return getPattern(getNow(), pattern);
    }

    /**
     * 获取无符号当前时间 默认格式：yyyyMMddHHmmss
     *
     * @author created by captain on 2021-06-28 17:23:06
     */
    public static String getNowDateUn() {
        return getNowDate("yyyyMMddHHmmss");
    }

    /**
     * 获取无符号当前时间 默认格式：yyyyMMddHHmmssSSS
     *
     * @author created by captain on 2021-06-28 17:23:06
     */
    public static String getNowDateUnMilli() {
        return getNowDate("yyyyMMddHHmmssSSS");
    }


    /**
     * 获取当前时间 默认格式：yyyy-MM-dd HH:mm:ss
     *
     * @author created by captain on 2021-06-28 17:23:06
     */
    public static String getNowDate() {
        return getNowDate("yyyy-MM-dd HH:mm:ss");
    }

    public static String getDayOfWeekCn(ZonedDateTime zonedDateTime) {
        String weekStr;
        switch (getDayOfWeekToLowerCase(zonedDateTime)) {
            case "monday":
                weekStr = "星期一";
                break;
            case "tuesday":
                weekStr = "星期二";
                break;
            case "wednesday":
                weekStr = "星期三";
                break;
            case "thursday":
                weekStr = "星期四";
                break;
            case "friday":
                weekStr = "星期五";
                break;
            case "saturday":
                weekStr = "星期六";
                break;
            case "sunday":
                weekStr = "星期日";
                break;
            default:
                weekStr = "未知";
                break;
        }
        return weekStr;
    }

    public static String getDayOfWeek(ZonedDateTime zonedDateTime) {
        return String.valueOf(zonedDateTime.getDayOfWeek());
    }

    public static String getDayOfWeekToLowerCase(ZonedDateTime zonedDateTime) {
        return getDayOfWeek(zonedDateTime).toLowerCase();
    }
}
