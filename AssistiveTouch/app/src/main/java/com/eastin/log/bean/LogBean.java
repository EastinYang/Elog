package com.eastin.log.bean;

/**
 * Created by Eastin on 16/7/5.
 */
public class LogBean {
    String name;
    String logId;

    public LogBean(String name, String logId) {
        this.name = name;
        this.logId = logId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
