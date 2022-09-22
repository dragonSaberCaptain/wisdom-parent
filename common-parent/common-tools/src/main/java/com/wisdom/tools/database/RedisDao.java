package com.wisdom.tools.database;

import com.wisdom.config.enums.DateTimeEnum;
import com.wisdom.tools.datetime.DateUtilByZoned;
import com.wisdom.tools.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * redis 操作工具类
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/8/4 11:13 星期四
 */
@Service
public class RedisDao {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取redis中对应的值,没有就插入传入的值
     *
     * @param key      key键
     * @param value    值
     * @param num      数字
     * @param timeUnit 单位
     * @author captain
     * @datetime 2022-09-22 14:26:26
     */
    public String save(String key, String value, long num, TimeUnit timeUnit) {
        String valueTmp = stringRedisTemplate.opsForValue().get(key);
        if (StringUtil.isBlank(valueTmp)) {
            valueTmp = value;
            set(key, value, num, timeUnit);
        }
        return valueTmp;
    }

    /**
     * 获取redis中对应的值,没有就插入默认值
     *
     * @param key      key键
     * @param num      数字
     * @param timeUnit 单位
     * @author captain
     * @datetime 2022-09-22 14:24:52
     */
    public String saveDefaultValue(String key, long num, TimeUnit timeUnit) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (StringUtil.isBlank(value)) {
            value = DateUtilByZoned.getDateTime(DateTimeEnum.DATETIME_PATTERN_MILLI_UN); // value设置默认值
            set(key, value, num, timeUnit);
        }
        return value;
    }

    public void set(String key, String value, long num, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, num, timeUnit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
