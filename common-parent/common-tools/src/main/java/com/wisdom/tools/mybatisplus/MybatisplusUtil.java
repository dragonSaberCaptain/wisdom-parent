package com.wisdom.tools.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.CaseFormat;
import com.wisdom.tools.object.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

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
        Map<String, Object> fieldMap = ObjectUtil.getDeclaredField(entity);
        for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ("serialVersionUID".equalsIgnoreCase(key) || "SERIAL_VERSION_U_I_D".equalsIgnoreCase(key)) {
                continue;
            }
            if (value == null) {
                continue;
            }
            //驼峰转大写下划线, userName -> USER_NAME
            String dbKey = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, key);

            if ("CREATE_DATE_TIME_START".equalsIgnoreCase(dbKey)) {
                wrapper.ge("CREATE_TIME", value);
            } else if ("CREATE_DATE_TIME_END".equalsIgnoreCase(dbKey)) {
                wrapper.le("CREATE_TIME", value);
            } else if ("UP_DATE_TIME_START".equalsIgnoreCase(dbKey)) {
                wrapper.ge("UPDATE_TIME", value);
            } else if ("UP_DATE_TIME_END".equalsIgnoreCase(dbKey)) {
                wrapper.le("UPDATE_TIME", value);
            } else {
                wrapper.eq(dbKey, value);
            }
        }
        return wrapper;
    }
}
