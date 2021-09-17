package com.wisdom.tools.random;

import java.util.UUID;

/**
 * Copyright © 2018 dragonSaberCaptain. All rights reserved.
 * 随机数工具类
 *
 * @author captain
 * @date 2018-07-31 15:18 星期二
 */
public class RandomUtil {
    /**
     * 随机产生一个32位长度的uuid 无符号
     *
     * @param
     * @return java.lang.String
     * @author created by admin on 2021-07-01 17:12:55
     */
    public static String getUuidUn() {
        return String.valueOf(UUID.randomUUID()).replace("-", "");
    }

}
