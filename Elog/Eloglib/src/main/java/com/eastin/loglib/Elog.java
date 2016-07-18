package com.eastin.loglib;

import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Eastin on 16/7/11.
 */
public class Elog {
    private static Context context;
    private static boolean isOpen;

    public static void init(Context context) {
        Elog.context = context.getApplicationContext();
    }

    public static void close() {
        isOpen = false;
    }

    public static void open() {
        isOpen = true;
    }

    public static void log(String type, String tag, String log) {
        try {
            if (context != null && isOpen
                    && tag != null && log != null
                    && !"".equals(tag) && !"".equals(log)) {
                Intent ln = new Intent();
                ln.setAction("assistivetouch.eastin.com.getlog");
                ln.putExtra("logName", tag);
                ln.putExtra("logDetail", getSystemTime() + " " + log);
                ln.putExtra("type", type);
                context.sendBroadcast(ln);
            }
        } catch (Exception e) {

        }
    }

    private static String getSystemTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
        return simpleDateFormat.format(new Date());
    }

    public static void i(String tag, String log) {
        log("i", tag, log);
    }

    public static void d(String tag, String log) {
        log("d", tag, log);
    }

    public static void v(String tag, String log) {
        log("v", tag, log);
    }

    public static void e(String tag, String log) {
        log("e", tag, log);
    }

    public static void w(String tag, String log) {
        log("w", tag, log);
    }

    public static void clear(String tag) {
        log("clear", tag, "");
    }

}
