package com.eastin.log.provider;

import android.content.Context;
import android.util.Log;

import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.constant.LogConstant;
import com.eastin.log.db.LogNameTable;
import com.eastin.log.db.LogTable;
import com.eastin.log.db.PALogDB;
import com.litesuits.android.async.AsyncTask;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/10.
 */
public class LogNameProvider extends PALogDB {
    private Object object = new Object();

    public LogNameProvider(Context context) {
        super(context);
    }

    private QueryBuilder getLogNameBuilder(String logName) {
        QueryBuilder qb = new QueryBuilder(LogNameTable.class)
                .columns(new String[]{"logName"});
        if(logName != null && !"".equals(logName)) {
            qb.where("logName LIKE ?", new String[]{"%" + logName + "%"});
        }
        qb.appendOrderDescBy("datetime");
        return qb;
    }

    public int getLogNameCount(String logName) {
        synchronized (object) {
            return (int) liteOrm.queryCount(getLogNameBuilder(logName));
        }
    }

    public LogNameTable getLogNameItem(int position, String logName) {
        synchronized (object) {
            QueryBuilder qb = getLogNameBuilder(logName);
            qb.limit(position, 1);
            List<LogNameTable> logs = liteOrm.query(qb);
            if (logs != null && logs.size() > 0) {
                return logs.get(0);
            }
            return null;
        }
    }

    public void clearLogNames() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (object) {
                    liteOrm.delete(LogNameTable.class);
                    EventBus.getDefault().post(new EventBusUpdateBean("logName", null));
                    liteOrm.delete(LogTable.class);
                    EventBus.getDefault().post(new EventBusUpdateBean("log", null));
                }
                return null;
            }
        };
        task.execute();
    }

    public void addLogName(final LogNameTable logNameTable) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (object) {
                    try {
                        liteOrm.save(logNameTable);
                        int logCount = getLogNameCount("");
                        if(logCount >= LogConstant.LOGNAME_MAX_LENGTH + LogConstant.LOGNAME_WHEN_DELETE) {
                            liteOrm.delete(LogNameTable.class, 0, LogConstant.LOGNAME_WHEN_DELETE, "datetime");
                        }
                        EventBus.getDefault().post(new EventBusUpdateBean("logName", null));
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
