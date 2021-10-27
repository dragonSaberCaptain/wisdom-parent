package com.wisdom.tools.object;

import com.wisdom.config.annotation.ResultFiled;
import com.wisdom.tools.string.StringUtil;

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

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getResultMap(Object obj) {
        Map<String, Object> resultMap = new LinkedHashMap<>();
        if (obj instanceof Enum) {
            Enum curEnum = (Enum) obj;
            Class clazz = curEnum.getDeclaringClass();

            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ResultFiled annotation = declaredField.getAnnotation(ResultFiled.class);
                if (null != annotation) {
                    String fieldName = declaredField.getName();
                    String methodName = "get" + StringUtil.upperFirstCase(fieldName);
                    Object fieldValue = null;
                    try {
                        fieldValue = clazz.getMethod(methodName).invoke(curEnum);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultMap.put(fieldName, fieldValue);
                }
            }
        }
        return resultMap;
    }
}
