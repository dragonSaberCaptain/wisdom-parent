package com.wisdom.tools.translate.baidu;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class TransResult {
    private String from;
    private String to;
    private String status;
    private String type;
    private List<TranslateData> data;
    private List<TranslateData> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TranslateData> getData() {
        return data;
    }

    public void setData(List<TranslateData> data) {
        this.data = data;
    }

    public List<TranslateData> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TranslateData> trans_result) {
        this.trans_result = trans_result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("from", from)
                .append("to", to)
                .append("status", status)
                .append("type", type)
                .append("data", data)
                .append("trans_result", trans_result)
                .toString();
    }
}
