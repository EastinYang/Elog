package com.eastin.log;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.constant.LogConstant;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/15.
 */
public class MainBroadcast extends BroadcastReceiver {
    public final static String GETLOG = "assistivetouch.eastin.com.getlog";
    public final static String STOP_SERVICE = "com.eastin.stop.service";
    public final static String UPDATE_TAG_MAX = "com.eastin.tag.max";
    public final static String UPDATE_LOG_MAX = "com.eastin.log.max";
    public final static String UPDATE_COPY_TO = "com.eastin.copy.to";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(GETLOG.equals(intent.getAction())) {
            String logName = intent.getStringExtra("logName");
            String logDetail = intent.getStringExtra("logDetail");
            String type = intent.getStringExtra("type");
            Bundle bundle = new Bundle();
            bundle.putString("type", type == null ? "" : type);
            bundle.putString("logName", logName == null ? "" : logName);
            bundle.putString("logDetail", logDetail == null ? "" : logDetail);
            EventBus.getDefault().post(new EventBusUpdateBean("broadcast", bundle));
        } else if(STOP_SERVICE.equals(intent.getAction())) {
            EventBus.getDefault().post(new EventBusUpdateBean("stopService", null));
        } else if(UPDATE_LOG_MAX.equals(intent.getAction())) {
            int num = intent.getIntExtra("logMax", LogConstant.LOG_DEFAULT_MAX_LENGTH);
            Bundle bundle = new Bundle();
            bundle.putInt("logMax", num);
            EventBus.getDefault().post(new EventBusUpdateBean("updateLogMax", bundle));
        } else if(UPDATE_TAG_MAX.equals(intent.getAction())) {
            int num = intent.getIntExtra("tagMax", LogConstant.TAG_DEFAULT_MAX_LENGTH);
            Bundle bundle = new Bundle();
            bundle.putInt("tagMax", num);
            EventBus.getDefault().post(new EventBusUpdateBean("updateTagMax", bundle));
        } else if(UPDATE_COPY_TO.equals(intent.getAction())) {
            String copyTo = intent.getStringExtra("copyTo");
            Bundle bundle = new Bundle();
            bundle.putString("copyTo", copyTo);
            EventBus.getDefault().post(new EventBusUpdateBean("updateCopyTo", bundle));
        }
    }
}
