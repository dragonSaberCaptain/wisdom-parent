/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.excel.ExcelTemplateData
 * 当前文件的名称:ExcelTemplateData.java
 * 当前文件的类名:ExcelTemplateData
 * 上一次文件修改的日期时间:2023/8/10 下午4:27
 *
 */

package com.wisdom.tools.excel;

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
