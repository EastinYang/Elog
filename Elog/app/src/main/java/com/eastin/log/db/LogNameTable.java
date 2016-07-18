package com.eastin.log.db;

/**
 * Created by Eastin on 16/7/10.
 */

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

@Table("lognametable")
public class LogNameTable {
    @PrimaryKey(AssignType.BY_MYSELF)
    private String logName;
    private String datetime;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
