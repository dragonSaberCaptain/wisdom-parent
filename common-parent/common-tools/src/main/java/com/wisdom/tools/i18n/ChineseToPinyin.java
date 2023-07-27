package com.wisdom.tools.i18n;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 将中文转换为拼音
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/12/16 17:13 星期五
 */
public class ChineseToPinyin {
    /**
     * 获取全拼拼音、简写拼音（取每个汉字开头拼音字母）
     *
     * @param src    字符串全拼拼音
     * @param isAbbr 是否缩写
     * @return java.lang.String
     * @author captain
     * @datetime 2022-12-19 14:40:18
     */
    public static String getFieldName(String src, boolean isAbbr) {
        if (isAbbr) {
            return getPinYinHeadChar(src).replaceAll("\\W", "");
        } else {
            return getPingYin(src).replaceAll("\\W", "");
        }
    }

    public static String get(String src) {
        String result;
        boolean isHeader = false;
        if (src.length() > 7) {
            result = getPinYinHeadChar(src);
            isHeader = true;
        } else {
            result = getPingYin(src);
        }
        result = result.replaceAll("\\W", "");
        if (isHeader && result.length() < 6) {
            result = getPingYin(src).replaceAll("\\W", "");
        }
        return result;
    }

    /**
     * 首字母转大写
     *
     * @param str 待转换字符串
     */
    public static String toUpperCaseFirstOne(String str) {
        if (Character.isUpperCase(str.charAt(0)))
            return str;
        else
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 将汉字转换为全拼
     *
     * @param src 待转换字符串
     */
    public static String getPingYin(String src) {
        char[] t1;
        t1 = src.toCharArray();
        String[] t2;
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder t4 = new StringBuilder();
        int t0 = t1.length;
        try {
            boolean first = false;
            for (char c : t1) {
                // 判断是否为汉字字符
                if (Character.toString(c).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(c, t3);
                    t4.append(first ? toUpperCaseFirstOne(t2[0]) : t2[0]);
                    first = true;
                } else
                    t4.append(Character.toString(c));
            }
            // System.out.println(t4);
            return t4.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4.toString();
    }

    /**
     * 返回中文的首字母
     *
     * @param str 待处理字符串
     */
    public static String getPinYinHeadChar(String str) {

        StringBuilder convert = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert.append(pinyinArray[0].charAt(0));
            } else {
                convert.append(word);
            }
        }
        return convert.toString();
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr 待处理字符串
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        byte[] bGBK = cnStr.getBytes();
        for (byte b : bGBK) {
            strBuf.append(Integer.toHexString(b & 0xff));
        }
        return strBuf.toString();
    }
}