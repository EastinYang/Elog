package com.eastin.log.provider;

import android.content.Context;
import android.util.Log;

import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.constant.LogConstant;
import com.eastin.log.db.LogConstantShare;
import com.eastin.log.db.LogNameTable;
import com.eastin.log.db.LogTable;
import com.eastin.log.db.PALogDB;
import com.litesuits.android.async.AsyncTask;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/10.
 */
public class LogNameProvider extends PALogDB {
    private Object object = new Object();
    private Context context;

    public LogNameProvider(Context context) {
        super(context);
        this.context = context;
    }

    private QueryBuilder getLogNameBuilder(String logName) {
        QueryBuilder qb = new QueryBuilder(LogNameTable.class)
                .columns(new String[]{"logName"});
        if(logName != null && !"".equals(logName)) {
            qb.where("logName LIKE ?", new Object[]{"%" + logName + "%"});
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
                        int maxLength = LogConstantShare.getInstance(context).getInt(LogConstant.TAG_MAX, LogConstant.TAG_DEFAULT_MAX_LENGTH);
                        Log.i("LogProvider", "maxLength：" + maxLength);
                        if(logCount >= maxLength + LogConstant.TAG_WHEN_DELETE) {
                            liteOrm.delete(LogNameTable.class, 0, LogConstant.TAG_WHEN_DELETE, "datetime");
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

    public void clearLogName(final String logName) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (object) {
                    if (logName != null && !"".equals(logName)) {
                        liteOrm.delete(new WhereBuilder(LogNameTable.class).where("logName=?", new Object[]{logName}));
                        EventBus.getDefault().post(new EventBusUpdateBean("logName", null));
                    }
                    return null;
                }
            }
        };
        task.execute();
    }


}
