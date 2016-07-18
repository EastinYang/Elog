package com.eastin.log.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eastin on 16/7/18.
 * MainService进程的SharePreference
 */
public class LogConstantShare {
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor localEditor;
    private static LogConstantShare logConstantShare;

    private LogConstantShare(Context context) {
        this.context = context;
        settings = context.getSharedPreferences("elog", Context.MODE_PRIVATE);
        localEditor = settings.edit();
    }

    public static LogConstantShare getInstance(Context context) {
        if(logConstantShare == null) {
            logConstantShare = new LogConstantShare(context.getApplicationContext());
        }
        return logConstantShare;
    }

    public void putString(String key, String value) {
        if(localEditor != null) {
            localEditor.putString(key, value);
            localEditor.commit();
        }
    }

    public String getString(String key, String defaultValue) {
        String re = "";
        if(settings != null) {
            re = settings.getString(key, defaultValue);
        }
        return re;
    }

    public void putInt(String key, int value) {
        if(localEditor != null) {
            localEditor.putInt(key, value);
            localEditor.commit();
        }
    }

    public int getInt(String key, int defaultValue) {
        int re = 0;
        if(settings != null) {
            re = settings.getInt(key, defaultValue);
        }
        return re;
    }

}
