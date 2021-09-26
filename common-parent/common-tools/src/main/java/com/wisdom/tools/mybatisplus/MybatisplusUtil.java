package com.wisdom.tools.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.wisdom.tools.string.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * TODO (用一句话描述该类的作用)
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/9/13 15:49 星期一
 */
@Slf4j
public class MybatisplusUtil {
    public static <T> QueryWrapper<T> createWrapper(T entity) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        Field[] declaredFields = entity.getClass().getDeclaredFields();
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                //驼峰转大写下划线, userName -> USER_NAME
                String dbKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, field.getName());
                String value = String.valueOf(field.get(entity));
                if (StringUtil.isNotBlank(value)) {
                    if ("CREATE_DATE_TIME_START".equals(dbKey)) {
                        wrapper.ge("CREATE_TIME", value);
                    } else if ("CREATE_DATE_TIME_END".equals(dbKey)) {
                        wrapper.le("CREATE_TIME", value);
                    } else if ("UP_DATE_TIME_START".equals(dbKey)) {
                        wrapper.ge("UPDATE_TIME", value);
                    } else if ("UP_DATE_TIME_END".equals(dbKey)) {
                        wrapper.le("UPDATE_TIME", value);
                    } else {
                        wrapper.eq(dbKey, value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return wrapper;
    }
}
