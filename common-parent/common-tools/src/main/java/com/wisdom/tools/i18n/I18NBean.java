package com.wisdom.tools.i18n;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright © 2022 dragonSaberCaptain. All rights reserved.
 * <p>
 * 国际化辅助类
 *
 * @author captain
 * @version 1.0
 * @datetime 2022/12/16 17:10 星期五
 */
public class I18NBean {

    /**
     * 记录哪些文件包含有这个中文
     */
    private final Set<String> files = new HashSet<>(5);
    /**
     * 国际化文件中的key
     */
    private String key;
    /**
     * 需要被国际化的中文
     */
    private String chinese;
    /**
     * 中文翻译后的英文
     */
    private String english;
    private String portugal;
    /**
     * 该翻译所属文件
     */
    private String fileName;

    public void setFile(String file) {
        files.add(file);
    }

    public StringBuilder getFiles() {
        StringBuilder sb = new StringBuilder();
        for (String str : files) {
            sb.append(str).append(";");
        }
        return sb;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPortugal() {
        return portugal;
    }

    public void setPortugal(String portugal) {
        this.portugal = portugal;
    }
}