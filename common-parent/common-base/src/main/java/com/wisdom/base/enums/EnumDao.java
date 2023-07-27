package com.wisdom.base.enums;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @description 枚举类接口
 * @dateTime 2021/7/5 15:07 星期一
 */
public interface EnumDao {
    String getCode();

    int getCodeToInt();

    Long getCodeToLong();

    String getCodeToUpperCase();

    String getCodeToLowerCase();

    String getMsg();

    String getMsgToUpperCase();

    String getMsgToLowerCase();

    String getSubMsg();

}
