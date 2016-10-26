package com.eastin.log.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.eastin.log.interfaces.IMain;

/**
 * Created by Eastin on 16/7/6.
 */
public abstract class BaseView extends RelativeLayout {
    protected View view;
    protected Context context;
    protected IMain parent;

    public BaseView(Context context, IMain parent) {
        super(context);
        this.context = context;
        this.parent = parent;
        init();
    }

    private void init() {
        view = View.inflate(context, layoutId(), this);
        initView();
        initViewListener();
        doOtherThings();
    }

    protected abstract int layoutId();

    protected void initView(){}

    protected void initViewListener(){}

    protected void doOtherThings(){}

    public void updateViewDetail(Bundle bundle) {

    }

    public void toTop() {

    }
}