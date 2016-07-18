package com.eastin.log.bean;

import android.os.Bundle;

/**
 * Created by Eastin on 16/7/14.
 */
public class EventBusUpdateBean {
    private String action;
    private Bundle bundle;

    public EventBusUpdateBean(String action, Bundle bundle) {
        this.action = action;
        this.bundle = bundle;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
