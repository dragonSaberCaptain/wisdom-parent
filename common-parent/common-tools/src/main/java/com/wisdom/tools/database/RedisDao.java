package com.wisdom.tools.database;

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

    public String save(String key, String value, long num, TimeUnit timeUnit) {
        String keyTmp = stringRedisTemplate.opsForValue().get(key);
        if (StringUtil.isBlank(keyTmp)) {
            set(key, value, num, timeUnit);
        }
        return keyTmp;
    }

    public void set(String key, String value, long num, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, num, timeUnit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
