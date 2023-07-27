package com.wisdom.base.redis;

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
public interface RedisService {

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
    String save(String key, String value, long num, TimeUnit timeUnit);

    void set(String key, String value, long num, TimeUnit timeUnit);

    String get(String key);
}
