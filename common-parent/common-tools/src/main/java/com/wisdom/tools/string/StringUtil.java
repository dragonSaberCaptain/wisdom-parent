/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.string.StringUtil
 * 当前文件的名称:StringUtil.java
 * 当前文件的类名:StringUtil
 * 上一次文件修改的日期时间:2023/8/17 下午2:43
 *
 */

package com.wisdom.tools.string;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil extends StringUtils {
    /**
     * 数字
     */
    public static final String NUMBER = "0123456789";
    /**
     * 字母
     */
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwyxz";
    /**
     * 符号
     */
    public static final String SYMBOL = "~!@#$%^&*()_+[]{};,.<>?-=";

    public static List<String> getStr(int length, boolean assignLen, boolean includeNumber, boolean includeAlphabet, boolean includeSymbol) {
        StringBuilder sb = new StringBuilder();
        if (includeNumber) {
            sb.append(NUMBER);
        }
        if (includeAlphabet) {
            sb.append(ALPHABET);
            sb.append(ALPHABET.toUpperCase());
        }
        if (includeSymbol) {
            sb.append(SYMBOL);
        }
        char[] chars = sb.toString().toCharArray();
        List<String> strings = new ArrayList<>(chars.length);
        for (int i = 0; i < chars.length; i++) {
            strings.add(i, String.valueOf(chars[i]));
        }
        return getAllLists(strings, assignLen, length);
    }

    /**
     * 获取指定的字符
     *
     * @param minLen          字符最小长度
     * @param maxLen          字符最大长度
     * @param includeNumber   是否包含数字
     * @param includeAlphabet 是否包含字母
     * @param includeSymbol   是否包含字符
     */
    public static List<String> getStr(int minLen, int maxLen, boolean includeNumber, boolean includeAlphabet, boolean includeSymbol) {
        StringBuilder sb = new StringBuilder();
        if (includeNumber) {
            sb.append(NUMBER);
        }
        if (includeAlphabet) {
            sb.append(ALPHABET);
            sb.append(ALPHABET.toUpperCase());
        }
        if (includeSymbol) {
            sb.append(SYMBOL);
        }
        char[] chars = sb.toString().toCharArray();
        List<String> strings = new ArrayList<>(chars.length);
        for (int i = 0; i < chars.length; i++) {
            strings.add(i, String.valueOf(chars[i]));
        }
        return getAllLists(strings, minLen, maxLen);
    }

    public static List<String> getAllLists(List<String> elements, int minLen, int maxLen) {
        List<String> allLists = new ArrayList<>();
        if (maxLen == 1) {
            return elements;
        } else {
            if (maxLen > minLen) {
                allLists.addAll(elements);
            }
            List<String> allSublists = getAllLists(elements, minLen, maxLen - 1);
            for (int i = 0; i < elements.size(); i++) {
                for (int j = 0; j < allSublists.size(); j++) {
                    allLists.add(elements.get(i) + allSublists.get(j));
                }
            }
            return allLists;
        }
    }

    /**
     * 获取指定位数的数据集合
     *
     * @param elements 基类字符数组
     * @param length   指定字符串位数
     */
    public static List<String> getAllLists(List<String> elements, boolean assignLen, int length) {
        List<String> allLists = new ArrayList<>();
        if (length == 1) {
            return elements;
        } else {
            if (!assignLen) {
                allLists.addAll(elements);
            }
            List<String> allSublists = getAllLists(elements, assignLen, length - 1);
            for (int i = 0; i < elements.size(); i++) {
                for (int j = 0; j < allSublists.size(); j++) {
                    allLists.add(elements.get(i) + allSublists.get(j));
                }
            }
            return allLists;
        }
    }

    /**
     * 获取数字字符集合
     */
    public static List<String> getNumberStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, true, false, false);
    }

    /**
     * 获取字母字符集合
     */
    public static List<String> getAlphabetStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, false, true, false);
    }

    /**
     * 获取特殊符号字符集合
     */
    public static List<String> getSymbolStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, false, false, true);
    }

    /**
     * 获取全部字符集合，包含数字，字母，特殊字符
     */
    public static List<String> getFullStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, true, true, true);
    }

    /**
     * 获取数字字母集合
     */
    public static List<String> getNumberAlphabetStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, true, true, false);
    }

    /**
     * 获取数字特殊字符集合
     */
    public static List<String> getNumberSymbolStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, true, false, true);
    }

    /**
     * 获取字母特殊字符集合
     */
    public static List<String> getAlphabetSymbolStr(int minLen, int maxLen) {
        return getStr(minLen, maxLen, false, true, true);
    }

    /**
     * 对字符加星号处理:把从开始位置到结束位置的字符串以给定符号代替
     * 例如：begin：2  end：7
     * 源字符串：hello world
     * 目标字符串：he*****orld
     *
     * @param sourceStr  字符串源
     * @param beginIndex 开始位置
     * @param endIndex   结束位置
     * @param symbol     自定义替换符号
     * @author dragonSaberCaptain
     * @dateTime 2018-07-31 15:20:33
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

    /**
     * 对字符加星号处理:把从开始位置到结束位置的字符串以默认星号代替
     */
    public static char[] replaceRangeIndex(char[] sourceStr, int beginIndex, int endIndex) {
        return replaceRangeIndex(sourceStr, beginIndex, endIndex, "*".charAt(0));
    }

    /**
     * 对字符加星号处理:把从开始位置到结束位置的字符串以默认星号代替
     */
    public static String replaceRangeIndex(String sourceStr, int beginIndex, int endIndex) {
        return new String(replaceRangeIndex(sourceStr.toCharArray(), beginIndex, endIndex));
    }


    /**
     * 对字符加星号处理：保留前N位和后N位，其他的字符以星号代替
     */
    public static String replaceRangeNum(String sourceStr, int frontNum, int endNum) {
        return new String(replaceRangeIndex(sourceStr.toCharArray(), frontNum, sourceStr.length() - endNum));
    }

    /**
     * 对字符加星号处理:手机号脱敏，例如：123****8910
     */
    public static String replaceRangeToPhone(String sourceStr) {
        return replaceRangeNum(sourceStr, 3, 4);
    }

    /**
     * 对字符加星号处理:身份证脱敏，例如：123456*******2131
     */
    public static String replaceRangeToIdCard(String sourceStr) {
        return replaceRangeNum(sourceStr, 6, 4);
    }

    /**
     * 对字符加星号处理：保留前N位和后N位，其他的字符以4个自定义符号代替
     *
     * @param sourceStr 字符串源
     * @description 字符串长度变化 例如：hello world 到 hell****orld
     * @author dragonSaberCaptain
     * @dateTime 2018-07-31 15:24:55
     */
    public static String retainFour(String sourceStr, int frontNum, int endNum, char symbol) {
        StringBuilder sb = new StringBuilder(sourceStr);
        String repeat = StringUtils.repeat(symbol, 4);
        return sb.replace(frontNum, sb.length() - endNum, repeat).toString();
    }

    /**
     * 对字符加星号处理：保留前N位和后N位，其他的字符以4个星号(*)代替
     */
    public static String retainFour(String sourceStr, int frontNum, int endNum) {
        return retainFour(sourceStr, frontNum, endNum, "*".charAt(0));
    }

    /**
     * 对字符加星号处理：身份证脱敏，保留前4位和后4位，其他的字符以4个星号代替，例如：123456****2131
     */
    public static String retainFourToIdCard(String sourceStr) {
        return retainFour(sourceStr, 6, 4);
    }

    /**
     * 对字符加星号处理:手机号脱敏，例如：138****1234
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
     * @author captain
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
     * @author captain
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

    /**
     * 首字母大写
     */
    public static String upperFirstCase(String str) {
        return firstToCase(str, true);
    }

    /**
     * 首字母小写
     */
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

    /**
     * 字符串左补零
     */
    public static String addLeftZero(String strSource, long len) {
        return addSymbol(strSource, len, true, '0');
    }

    /**
     * 字符串右补零
     */
    public static String addRightZero(String strSource, long len) {
        return addSymbol(strSource, len, false, '0');
    }

    /**
     * 给字符串左补或者右补指定次数的字符串
     *
     * @param strSource 字符串源
     * @param len       位数
     * @param isLeft    左补
     * @param symbol    字符串
     * @author captain
     * @datetime 2022-03-08 17:48:55
     */
    public static String addSymbol(String strSource, long len, boolean isLeft, char symbol) {
        if (strSource.length() >= len) {
            return strSource;
        }
        StringBuilder strBuilder = new StringBuilder(strSource);
        if (isLeft) {
            for (int i = strBuilder.length(); i < len; i++) {
                strBuilder.insert(0, symbol);
            }
        } else {
            for (int i = strBuilder.length(); i < len; i++) {
                strBuilder.append(symbol);
            }
        }
        strSource = strBuilder.toString();
        return strSource;
    }

    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     *
     * @param testStr 要+1的字符串
     * @return +1后的字符串,没有变化返回原值
     */
    public static String addOne(String testStr) {
        String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
        String numStr = strs[strs.length - 1];//取出最后一组数字
        if (numStr != null && numStr.length() > 0) {//如果最后一组没有数字(也就是不以数字结尾)
            int n = numStr.length();//取出字符串的长度
            int num = Integer.parseInt(numStr) + 1;//将该数字加一
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length() - n) + added;
        } else {
            return testStr;
        }
    }


    public static void main(String[] args) {
        String testStr = "dragonSaberCaptain";
        String phoneStr = "12345678910";
        String idCardStr = "123456789101112131";
        String testNull = "null";

//        long startMili = System.currentTimeMillis();
//        String str1 = addLeftZero("11", 100000);
//        long endMili = System.currentTimeMillis();
//        System.out.println("str1.length():" + str1.length() + " 总耗时为：" + (endMili - startMili) + "毫秒");

//        System.out.println(addLeftZero(testNull, 10));
//        System.out.println(addRightZero(testNull, 10));
//        System.out.println("替换从开始到结束内的" + replaceRangeIndex(testStr, 2, 7));
//        System.out.println("替换前N个和后N个的" + replaceRangeNum(testStr, 4, 4));
//        System.out.println("手机号替换的" + replaceRangeToPhone(phoneStr));
//        System.out.println("身份证号替换的" + replaceRangeToIdCard(idCardStr));
//        System.out.println("保留前N个和后N个的，其他用4个代替" + retainFour(testStr, 2, 2));
//        System.out.println("手机号保留4个" + retainFourToPhone(phoneStr));
//        System.out.println("身份证保留4个" + retainFourToIdCard(idCardStr));
//        System.out.println(firstToCase(testStr, true));
        List<String> str = getStr(3, 3, true, false, false);
        System.out.println("str size:" + str.size());
        for (String s : str) {
            System.out.print(s + ",");
        }
    }
}
