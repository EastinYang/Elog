package com.eastin.log.provider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.constant.LogConstant;
import com.litesuits.android.async.AsyncTask;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.List;
import com.eastin.log.db.LogTable;
import com.eastin.log.db.PALogDB;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/10.
 */
public class LogProvider extends PALogDB {
    private long ms = 0;
    private long addms = 0;

    private Object object = new Object();

    public LogProvider(Context context) {
        super(context);
    }

    private QueryBuilder getLogDetailBuilder(String logName, String logDetail) {
        QueryBuilder<LogTable> qb = new QueryBuilder(LogTable.class)
                .columns(new String[]{"id", "logName", "logDetail"});
        if(logName != null && !"".equals(logName)) {
            qb.whereEquals("logName", logName);
            if (logDetail != null && !"".equals(logDetail)) {
                qb.whereAnd("logDetail LIKE ?", new String[]{"%" + logDetail + "%"});
            }
        }
        qb.appendOrderDescBy("datetime");
        return qb;
    }

    public int getLogDetailCount(String logName, String logDetail) {
        synchronized (object) {
            ms = System.currentTimeMillis();
            int count = (int) liteOrm.queryCount(getLogDetailBuilder(logName, logDetail));
            Log.i("LogProvider", "getLogDetailCount：" + (System.currentTimeMillis() - ms));
            return count;
        }
    }

    public LogTable getLogDetailItem(int position, String logName, String logDetail) {
        synchronized (object) {
            ms = System.currentTimeMillis();
            QueryBuilder qb = getLogDetailBuilder(logName, logDetail);
            qb.limit(position, 1);
            List<LogTable> logs = liteOrm.query(qb);
            if (logs != null && logs.size() > 0) {
                return logs.get(0);
            }
            Log.i("LogProvider", "getLogDetailItem：" + (System.currentTimeMillis() - ms));
            return null;
        }
    }

    public void clearLogDetails(final String logName) {
        final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (object) {
                    if (logName != null && !"".equals(logName)) {
                        liteOrm.delete(new WhereBuilder(LogTable.class).where("logName=?", new String[]{logName}));
                        EventBus.getDefault().post(new EventBusUpdateBean("log", null));
                    }
                    return null;
                }
            }
        };
        task.execute();
    }

    public void addLog(final LogTable logTable) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (object) {
                    try {
                        addms = System.currentTimeMillis();
                        liteOrm.insert(logTable);
                        int logCount = getLogDetailCount("", "");
                        if (logCount >= LogConstant.LOG_MAX_LENGTH + LogConstant.LOG_WHEN_DELETE) {
                            liteOrm.delete(LogTable.class, 0, LogConstant.LOG_WHEN_DELETE, "datetime");
                        }
                        Log.i("LogProvider", "addLog：" + (System.currentTimeMillis() - addms));
                        EventBus.getDefault().post(new EventBusUpdateBean("log", null));
                    } catch (Exception e) {
                        Log.i("LogProvider", "delete fail");
                    }
                }
                return null;
            }
        };
        task.execute();
    }

}
