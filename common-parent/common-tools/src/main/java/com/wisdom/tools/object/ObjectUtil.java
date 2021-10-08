package com.wisdom.tools.object;

import com.wisdom.config.enums.EnumDao;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote 与Object相关的工具类
 * @date 2021/7/6 16:05 星期二
 */
public class ObjectUtil {
    /**
     * 使用该方法枚举类要实现 EnumDao s接口
     */
    public static EnumDao isEnum(Object object) {
        if (object instanceof Enum && object.getClass().isEnum()) {
            return (EnumDao) object;
        }
        return null;
    }

    public static Map<String, Object> getDeclaredField(Object object) {
        Map<String, Object> fieldsMap = new LinkedHashMap<>();

        Class<?> clazz = object.getClass();
        while (clazz != null && clazz != Object.class) {
            Field[] declaredFields = clazz.getDeclaredFields();
            try {
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    String key = field.getName();
                    if (fieldsMap.containsKey(key)) {
                        continue;
                    }
                    fieldsMap.put(key, field.get(object));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }
        return fieldsMap;
    }
}
