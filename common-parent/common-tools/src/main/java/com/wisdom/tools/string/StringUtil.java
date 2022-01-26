package com.wisdom.tools.string;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 * 字符串工具类
 *
 * @author dragonSaberCaptain
 * @date 2018-07-31 15:11 星期二
 */
public class StringUtil extends StringUtils {
    public static void main(String[] args) {
        String testStr = "dragonSaberCaptain";
        String phoneStr = "12345678910";
        String idCardStr = "123456789101112131";
        String testNull = "null";

//        System.out.println("替换从开始到结束内的" + replaceRangeIndex(testStr, 2, 7));
//        System.out.println("替换前N个和后N个的" + replaceRangeNum(testStr, 4, 4));
//        System.out.println("手机号替换的" + replaceRangeToPhone(phoneStr));
//        System.out.println("身份证号替换的" + replaceRangeToIdCard(idCardStr));
//        System.out.println("保留前N个和后N个的，其他用4个代替" + retainFour(testStr, 2, 2));
//        System.out.println("手机号保留4个" + retainFourToPhone(phoneStr));
//        System.out.println("身份证保留4个" + retainFourToIdCard(idCardStr));
//        System.out.println(firstToCase(testStr, true));
    }

    /**
     * 对字符加星号处理:把从开始位置到结束位置的字符串以星号代替
     * 例如：begin：2  end：7
     * 源字符串：hello world
     * 目标字符串：he*****orld
     *
     * @param sourceStr  字符串源
     * @param beginIndex 开始位置
     * @param endIndex   结束位置
     * @param symbol     自定义替换符号
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:20:33
     */
    public static char[] replaceRangeIndex(char[] sourceStr, int beginIndex, int endIndex, char symbol) {
        if (beginIndex < 0 || endIndex < 0 || beginIndex >= sourceStr.length || endIndex >= sourceStr.length || beginIndex == endIndex) {
            return sourceStr;
        }
        if (beginIndex > endIndex) {
            beginIndex = beginIndex ^ endIndex;
            endIndex = beginIndex ^ endIndex;
            beginIndex = beginIndex ^ endIndex;
        }

        for (int j = beginIndex; j < endIndex; j++) {
            sourceStr[j] = symbol;
        }
        return sourceStr;
    }

    public static char[] replaceRangeIndex(char[] sourceStr, int beginIndex, int endIndex) {
        return replaceRangeIndex(sourceStr, beginIndex, endIndex, "*".charAt(0));
    }

    public static String replaceRangeIndex(String sourceStr, int beginIndex, int endIndex) {
        return new String(replaceRangeIndex(sourceStr.toCharArray(), beginIndex, endIndex));
    }


    /**
     * 对字符加星号处理：保留前N位和后N位，其他的字符以星号代替
     *
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:22:29
     */
    public static String replaceRangeNum(String sourceStr, int frontNum,
                                         int endNum) {
        return new String(replaceRangeIndex(sourceStr.toCharArray(), frontNum, sourceStr.length() - endNum));
    }

    /**
     * 对字符加星号处理:手机号脱敏，例如：123****8910
     *
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:25:59
     */
    public static String replaceRangeToPhone(String sourceStr) {
        return replaceRangeNum(sourceStr, 3, 4);
    }

    /**
     * 对字符加星号处理:身份证脱敏，例如：123456*******2131
     *
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:25:59
     */
    public static String replaceRangeToIdCard(String sourceStr) {
        return replaceRangeNum(sourceStr, 6, 4);
    }

    /**
     * 对字符加星号处理：保留前N位和后N位，其他的字符以4个星号代替
     *
     * @param sourceStr 字符串源
     * @warning 字符串长度变化 例如：hello world 到 hell****orld
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:24:55
     */
    public static String retainFour(String sourceStr, int frontNum,
                                    int endNum, char symbol) {
        StringBuilder sb = new StringBuilder(sourceStr);
        String repeat = StringUtils.repeat(symbol, 4);
        return sb.replace(frontNum, sb.length() - endNum, repeat).toString();
    }

    public static String retainFour(String sourceStr, int frontNum,
                                    int endNum) {
        return retainFour(sourceStr, frontNum, endNum, "*".charAt(0));
    }

    /**
     * 对字符加星号处理：身份证脱敏，保留前4位和后4位，其他的字符以4个星号代替，例如：123456****2131
     *
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:24:55
     */
    public static String retainFourToIdCard(String sourceStr) {
        return retainFour(sourceStr, 6, 4);
    }

    /**
     * 对字符加星号处理:手机号脱敏，例如：138****1234
     *
     * @author dragonSaberCaptain
     * @date 2018-07-31 15:25:59
     */
    public static String retainFourToPhone(String sourceStr) {
        return retainFour(sourceStr, 3, 4);
    }

    /**
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("null")    = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     *
     * @param cs 字符串
     * @return boolean
     * @author admin
     * @datetime 2021-09-23 10:14:37
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0 || "null".contentEquals(cs);
    }

    /**
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("null")    = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     *
     * @param cs 字符串
     * @return boolean
     * @author admin
     * @datetime 2021-09-23 10:14:37
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("null")    = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     *
     * @param cs 字符串
     * @author captain
     * @datetime 2021-09-23 10:01:58
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0 || "null".contentEquals(cs)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("null")    = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     *
     * @param cs 字符串
     * @author captain
     * @datetime 2021-09-23 10:01:58
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    //首字母大写
    public static String upperFirstCase(String str) {
        return firstToCase(str, true);
    }

    //首字母小写
    public static String firstLowerCase(String str) {
        return firstToCase(str, false);
    }

    /**
     * 首字母大小写转换
     *
     * @param str         待转换字符串
     * @param isUpperCase true:首字母转大写 false:首字母转小写
     * @author captain
     * @datetime 2021-12-27 09:54:07
     */
    public static String firstToCase(String str, boolean isUpperCase) {
        if (str.length() == 0) {
            return str;
        }
        if (isUpperCase) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        } else {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
    }
}
