package com.wisdom.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 * <p>
 * 在需要返回的字段上添加
 *
 * @author captain
 * @version 1.0
 * @datetime 2021/10/26 15:47 星期二
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.FIELD)
public @interface ResultFiled {
}
