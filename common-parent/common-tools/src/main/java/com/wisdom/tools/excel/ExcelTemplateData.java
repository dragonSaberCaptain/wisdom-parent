package com.wisdom.tools.excel;

/**
 * Copyright © 2021 dragonSaberCaptain. All rights reserved.
 *
 * @author captain
 * @version 1.0
 * @apiNote excel模板
 * @dateTime 2021/7/15 17:17 星期四
 */
public class ExcelTemplateData {
    /**
     * 属性名称
     */
    private String key;

    /**
     * 1-普通下拉 2-级联下拉 3-自动填充下拉
     */
    private String optionsType = "1";

    /**
     * 级联下拉子数据所在列
     */
    private String subKeyName;
    /**
     * 级联下拉数据
     */
    private Object optionsData;

    public Object getOptionsData() {
        return optionsData;
    }

    public void setOptionsData(Object optionsData) {
        this.optionsData = optionsData;
    }

    public String getOptionsType() {
        return optionsType;
    }

    public void setOptionsType(String optionsType) {
        this.optionsType = optionsType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubKeyName() {
        return subKeyName;
    }

    public void setSubKeyName(String subKeyName) {
        this.subKeyName = subKeyName;
    }
}
