package com.eastin.log.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eastin on 16/7/10.
 */
public class DatetimeUtil {

    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return simpleDateFormat.format(new Date());
    }

}
