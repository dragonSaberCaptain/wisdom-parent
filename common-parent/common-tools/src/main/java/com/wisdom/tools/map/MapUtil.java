package com.wisdom.tools.map;

import com.google.common.base.CaseFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description TODO
 * @date 2021/8/9 11:29 星期一
 */
public class MapUtil {
    /**
     * 将源map中的key格式化
     *
     * @param sourceMap 源
     * @param sourceFormat 源格式
     * @param targetFormat 目标格式
     * @author captain
     * @datetime 2022-03-08 10:16:09
     */
    public static Map<String, Object> keyFormat(Map<String, Object> sourceMap, CaseFormat sourceFormat, CaseFormat targetFormat) {
        if (sourceMap == null || sourceMap.size() == 0) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        sourceMap.forEach((k, v) -> resultMap.put(sourceFormat.to(targetFormat, k), v));
        return resultMap;
    }

    /**
     * 将源map中的key全部下划线转换成驼峰,返回新的map 例如:test_data -> testData
     *
     * @param sourceMap 源
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author captain  2021-08-09 13:56:48
     */
    public static Map<String, Object> lowerUnderscoreToLowerCamel(Map<String, Object> sourceMap) {
        return keyFormat(sourceMap, CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_CAMEL);
    }

    /**
     * 将源map中的key全部驼峰转换成下划线,返回新的map 例如:testData -> test_data
     *
     * @param sourceMap 源
     * @author captain  2021-08-09 13:55:20
     */
    public static Map<String, Object> lowerCamelToLowerUnderscore(Map<String, Object> sourceMap) {
        return keyFormat(sourceMap, CaseFormat.LOWER_CAMEL, CaseFormat.LOWER_UNDERSCORE);
    }

    /**
     * 将源map中的key全部转换成小写,返回新的map
     *
     * @param sourceMap 源
     * @author captain  2021-08-09 13:54:45
     */
    public static Map<String, Object> keyToLowerCase(Map<String, Object> sourceMap) {
        if (sourceMap == null || sourceMap.size() == 0) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        sourceMap.forEach((k, v) -> resultMap.put(k.toLowerCase(), v));
        return resultMap;
    }

    /**
     * 将源map中的key全部转换成大写,返回新的map
     *
     * @param sourceMap 源
     * @author captain  2021-08-09 13:50:18
     */
    public static Map<String, Object> keyToUpperCase(Map<String, Object> sourceMap) {
        if (sourceMap == null || sourceMap.size() == 0) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        sourceMap.forEach((k, v) -> resultMap.put(k.toUpperCase(), v));
        return resultMap;
    }
}
