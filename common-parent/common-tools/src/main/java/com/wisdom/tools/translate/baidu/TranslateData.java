package com.wisdom.tools.translate.baidu;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TranslateData {
    private String src;
    private String dst;
    private String prefixWrap;
    private String result;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getPrefixWrap() {
        return prefixWrap;
    }

    public void setPrefixWrap(String prefixWrap) {
        this.prefixWrap = prefixWrap;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("src", src)
                .append("dst", dst)
                .append("prefixWrap", prefixWrap)
                .append("result", result)
                .toString();
    }
}
