package com.wisdom.tools.datetime;

import com.wisdom.config.enums.DateTimeEnum;
import com.wisdom.config.enums.WeekEnum;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

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
    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    /**
     * 日期时间字符串 转换 ZonedDateTime
     *
     * @param dateTime 待转换的日期时间
     * @param pattern  日期时间格式
     * @return java.time.ZonedDateTime
     * @author captain
     * @datetime 2021-10-08 10:30:20
     */
    public static ZonedDateTime getParse(String dateTime, DateTimeEnum pattern, boolean isCn) {
        String resultPattern = pattern.getCode();
        if (isCn) {
            resultPattern = pattern.getMsg();
        }
        return ZonedDateTime.parse(dateTime, DateTimeFormatter.ofPattern(resultPattern).withZone(ZoneId.systemDefault()));
    }

    /**
     * 日期时间字符串 转换 ZonedDateTime
     *
     * @param dateTime 待转换的日期时间
     * @param pattern  日期时间格式
     * @return java.time.ZonedDateTime
     * @author captain
     * @datetime 2021-10-08 10:30:20
     */

    public static ZonedDateTime getParse(String dateTime, DateTimeEnum pattern) {
        return getParse(dateTime, pattern, false);
    }

    /**
     * 日期时间字符串 转换 ZonedDateTime
     *
     * @param dateTime 日期时间字符串
     * @return java.time.ZonedDateTime 默认日期时间格式
     * @author captain
     * @datetime 2021-10-08 14:17:20
     */
    public static ZonedDateTime getParse(String dateTime) {
        return getParse(dateTime, DateTimeEnum.DATETIME_PATTERN, false);
    }

    /**
     * ZonedDateTime 转换 日期时间字符串
     *
     * @param zonedDateTime 日期时间
     * @param pattern       日期时间格式
     * @param isCn          是否是中文的日期时间格式
     * @return java.lang.String
     * @author captain
     * @datetime 2021-10-08 14:22:53
     */
    public static String getPattern(ZonedDateTime zonedDateTime, DateTimeEnum pattern, boolean isCn) {
        String resultPattern = pattern.getCode();
        if (isCn) {
            resultPattern = pattern.getMsg();
        }
        return DateTimeFormatter.ofPattern(resultPattern).format(zonedDateTime);
    }

    /**
     * ZonedDateTime 转换 日期时间字符串
     *
     * @param zonedDateTime 日期时间
     * @param pattern       日期时间格式
     * @return java.lang.String 日期时间字符串
     * @author captain
     * @datetime 2021-10-08 13:51:54
     */
    public static String getPattern(ZonedDateTime zonedDateTime, DateTimeEnum pattern) {
        return getPattern(zonedDateTime, pattern, false);
    }

    /**
     * 获取当前日期时间字符串
     *
     * @param pattern 返回的日期时间格式
     * @return java.lang.String 日期时间字符串
     * @author captain
     * @datetime 2021-10-08 11:01:21
     */
    public static String getDateTime(DateTimeEnum pattern) {
        return getPattern(now(), pattern);
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return java.lang.String 默认格式的日期时间字符串
     * @author captain
     * @datetime 2021-10-08 13:08:04
     */
    public static String getDateTime() {
        return getDateTime(DateTimeEnum.DATETIME_PATTERN);
    }

    /**
     * 根据条件，获取日期时间字符串. 例如：获取当月第一个星期天的时间、获取当月最后一天的时间等等
     *
     * @param zonedDateTime    时间源
     * @param temporalAdjuster 获取条件 例如：TemporalAdjusters.firstDayOfMonth()、TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.FRIDAY)
     * @param pattern          时间格式
     * @return java.lang.String 日期时间字符串
     * @author captain
     * @datetime 2021-10-08 13:37:43
     */
    public static String getDateTimeByCondition(ZonedDateTime zonedDateTime, TemporalAdjuster temporalAdjuster, DateTimeEnum pattern) {
        return getPattern(zonedDateTime.with(temporalAdjuster), pattern);
    }

    /**
     * 根据条件，获取日期时间字符串. 例如：获取当月第一个星期天的时间、获取当月最后一天的时间等等
     *
     * @param zonedDateTime    时间源
     * @param temporalAdjuster 获取条件 例如：TemporalAdjusters.firstDayOfMonth()、TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.FRIDAY)
     * @return java.lang.String 日期时间字符串
     * @author captain
     * @datetime 2021-10-08 13:42:38
     */
    public static String getDateTimeByCondition(ZonedDateTime zonedDateTime, TemporalAdjuster temporalAdjuster) {
        return getPattern(zonedDateTime.with(temporalAdjuster), DateTimeEnum.DATETIME_PATTERN);
    }

    /**
     * 根据条件，获取日期时间字符串. 例如：获取当月第一个星期天的时间、获取当月最后一天的时间等等
     *
     * @param temporalAdjuster 获取条件 例如：TemporalAdjusters.firstDayOfMonth()、TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.FRIDAY)
     * @return java.lang.String 日期时间字符串
     * @author captain
     * @datetime 2021-10-08 13:48:07
     */
    public static String getDateTimeByCondition(TemporalAdjuster temporalAdjuster) {
        return getDateTimeByCondition(now(), temporalAdjuster);
    }

    /**
     * 根据时间单位,计算指的时间减去 num 后得到的时间
     *
     * @param zonedDateTime 源日期时间
     * @param num           数量
     * @param chronoUnit    日期时间单位
     * @param pattern       返回的日期时间格式
     * @return java.lang.String
     * @author captain
     * @datetime 2021-10-08 10:53:25
     */
    public static String getMinus(ZonedDateTime zonedDateTime, long num, ChronoUnit chronoUnit, DateTimeEnum pattern) {
        return getPattern(zonedDateTime.minus(num, chronoUnit), pattern);
    }

    /**
     * 根据时间单位,计算指的时间加上 num 后得到的时间
     *
     * @param zonedDateTime 日期时间
     * @param num           数量
     * @param chronoUnit    日期时间单位
     * @author captain  2021-07-01 15:32:56
     */
    public static String getPlus(ZonedDateTime zonedDateTime, long num, ChronoUnit chronoUnit, DateTimeEnum pattern) {
        return getPattern(zonedDateTime.plus(num, chronoUnit), pattern);
    }

    /**
     * 根据时间单位,计算两个日期的时间间隔
     *
     * @param startDateTime 开始时间
     * @param endDateTime   结束时间
     * @param chronoUnit    日期时间单位
     * @author captain  2021-07-01 15:29:14
     */
    public static long getChronoUnitBetween(ZonedDateTime startDateTime, ZonedDateTime endDateTime, ChronoUnit chronoUnit) {
        return Math.abs(startDateTime.until(endDateTime, chronoUnit));
    }

    /**
     * 判断某个时间是否在某个日期时间范围之间
     *
     * @param srcDateTime   源日期时间
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @author captain  2021-07-01 16:35:58
     */
    public static boolean isInRange(ZonedDateTime srcDateTime, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime)) {
            ZonedDateTime tmpZonedDateTime = startDateTime;
            startDateTime = endDateTime;
            endDateTime = tmpZonedDateTime;
        }
        return srcDateTime.isAfter(startDateTime) && srcDateTime.isBefore(endDateTime);
    }

    /**
     *  获取日期时间是星期几
     *
     * @param zonedDateTime 日期时间
     * @author captain
     * @datetime 2021-10-08 13:04:26
     * @return java.lang.String 返回大写字符串
     */
    public static String getDayOfWeek(ZonedDateTime zonedDateTime) {
        return String.valueOf(zonedDateTime.getDayOfWeek());
    }

    /**
     * 获取日期时间是星期几
     *
     * @param zonedDateTime 日期时间
     * @return java.lang.String 返回小写字符串
     * @author captain
     * @datetime 2021-10-08 13:05:44
     */
    public static String getDayOfWeekToLowerCase(ZonedDateTime zonedDateTime) {
        return getDayOfWeek(zonedDateTime).toLowerCase();
    }

    /**
     * 获取当前日期时间字符串
     *
     * @param pattern 日期格式
     * @return java.lang.String 中文格式的字符串
     * @author captain
     * @datetime 2021-10-08 13:33:17
     */
    public static String getDateTimeCn(DateTimeEnum pattern) {
        return getPattern(now(), pattern, true);
    }

    /**
     * 获取当前日期时间字符串
     *
     * @return java.lang.String 中文格式的字符串
     * @author captain
     * @datetime 2021-10-08 13:08:04
     */
    public static String getDateTimeCn() {
        return getDateTimeCn(DateTimeEnum.DATETIME_PATTERN);
    }

    /**
     * 根据条件，获取日期时间字符串. 例如：获取当月第一个星期天的时间、获取当月最后一天的时间等等
     *
     * @param zonedDateTime    时间源
     * @param temporalAdjuster 获取条件 例如：TemporalAdjusters.firstDayOfMonth()、TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.FRIDAY)
     * @param pattern          时间格式
     * @return java.lang.String 中文格式的字符串
     * @author captain
     * @datetime 2021-10-08 13:37:43
     */
    public static String getDateTimeByConditionCn(ZonedDateTime zonedDateTime, TemporalAdjuster temporalAdjuster, DateTimeEnum pattern) {
        return getPattern(zonedDateTime.with(temporalAdjuster), pattern, true);
    }

    public static String getMinusCn(ZonedDateTime zonedDateTime, long num, ChronoUnit chronoUnit, DateTimeEnum pattern) {
        return getPattern(zonedDateTime.minus(num, chronoUnit), pattern, true);
    }

    public static String getPlusCn(ZonedDateTime zonedDateTime, long num, ChronoUnit chronoUnit, DateTimeEnum pattern) {
        return getPattern(zonedDateTime.plus(num, chronoUnit), pattern, true);
    }

    public static ZonedDateTime getParseCn(String dateTime, DateTimeEnum pattern) {
        return getParse(dateTime, pattern, true);
    }

    /**
     * 获取日期时间是星期几，返回中文字符串
     *
     * @param zonedDateTime 日期时间
     * @return java.lang.String
     * @author captain
     * @datetime 2021-10-08 13:06:38
     */
    public static String getDayOfWeekCn(ZonedDateTime zonedDateTime) {
        String weekDay = getDayOfWeek(zonedDateTime);
        WeekEnum weekDayCn = WeekEnum.findByCode(weekDay);
        if (weekDayCn == null) {
            return WeekEnum.UNKNOWN.getMsg();
        }
        return weekDayCn.getMsg();
    }

    //****************************************************************【main start】

    /**
     * 方法主函数，用于测试
     *
     * @author captain  2021-06-28 17:18:59
     */
    public static void main(String[] args) {
        ZonedDateTime nowDateTime = now();
        String dataTimeStr = "2021-07-05 17:18:59";
        String dataTimeStr1 = "2021-07-06 17:18:59";
        String dataTimeStr2 = "2021-07-07 17:18:59";
        String dataTimeStr3 = "2021-07-08 17:18:59";
        String dataTimeStr4 = "2021-07-09 17:18:59";
        String dataTimeStr5 = "2021-07-10 17:18:59";
        String dataTimeStr6 = "2021-07-11 17:18:59";

        System.out.println(getDayOfWeekCn(nowDateTime));
        System.out.println(getDateTimeByCondition(TemporalAdjusters.firstDayOfMonth()));
        System.out.println(getDateTimeByCondition(TemporalAdjusters.lastDayOfYear()));
        System.out.println(getDateTimeByCondition(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.FRIDAY)));
    }
}
