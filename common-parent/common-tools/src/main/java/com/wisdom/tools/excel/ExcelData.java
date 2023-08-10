/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.excel.ExcelData
 * 当前文件的名称:ExcelData.java
 * 当前文件的类名:ExcelData
 * 上一次文件修改的日期时间:2023/8/10 下午4:27
 *
 */

package com.wisdom.tools.excel;

import java.util.List;
import java.util.Map;

public class ExcelData {
    /**
     * 文件名 不带后缀
     */
    private String fileName;
    /**
     * 文件类型
     */
    private String fileType = "xlsx";

    /**
     * 文件名全称 待后缀 例如： 123.xlsx
     */
    private String fullFileName;

    /**
     * 设置 excel 第一行 名称与实体类对应
     */
    private Map<String, String> fields;

    /**
     * false-导出 true-模板
     */
    private boolean isTemplate = false;

    /**
     * 设置导出数据
     */
    private List<Object> data;

    /**
     * 第一行对应的名称
     */
    private List<String> firstCell;

    /**
     * 设置起始行所在位置
     */
    private int startRowIndex = 0;
    /**
     * 设置下拉数据
     */
    private List<ExcelTemplateData> dataOptions;

    public List<String> getFirstCell() {
        return firstCell;
    }

    public void setFirstCell(List<String> firstCell) {
        this.firstCell = firstCell;
    }

    public List<ExcelTemplateData> getDataOptions() {
        return dataOptions;
    }

    public void setDataOptions(List<ExcelTemplateData> dataOptions) {
        this.dataOptions = dataOptions;
    }

    public boolean isTemplate() {
        return isTemplate;
    }

    public void setTemplate(boolean template) {
        isTemplate = template;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFullFileName() {
        if (fullFileName != null) {
            return this.fullFileName;
        }
        if (".".startsWith(fileType)) {
            return fileName + fileType;
        }
        return fileName + "." + fileType;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public int getStartRowIndex() {
        return startRowIndex;
    }

    public void setStartRowIndex(int startRowIndex) {
        this.startRowIndex = startRowIndex;
    }
}
