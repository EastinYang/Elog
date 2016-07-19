package com.eastin.log.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.eastin.log.R;
import com.eastin.log.adapter.LogNameAdapter;
import com.eastin.log.bean.CallbackActionBean;
import com.eastin.log.bean.CallbackModel;
import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.db.LogNameTable;
import com.eastin.log.interfaces.IKeyboardListener;
import com.eastin.log.interfaces.ILogNameItemClick;
import com.eastin.log.interfaces.IMain;
import com.eastin.log.provider.LogNameProvider;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/6.
 */
public class LogNameView extends BaseView implements ILogNameItemClick, View.OnClickListener, IKeyboardListener {
    private ListView lvLog;
    private TextView tvClear, tvTop, tvFilter;
    private KeyboardView kbView;

    private LogNameAdapter logNameAdapter;
    private String logName;
    private LogNameProvider logNameProvider;

    public LogNameView(Context context, IMain parent) {
        super(context, parent);
    }

    @Override
    protected int layoutId() {
        return R.layout.log_name_f;
    }

    @Override
    protected void initView() {
        lvLog = (ListView) view.findViewById(R.id.lvLog);
        tvClear = (TextView) view.findViewById(R.id.tvClear);
        tvTop = (TextView) view.findViewById(R.id.tvTop);
        tvFilter = (TextView) view.findViewById(R.id.tvFilter);
        kbView = (KeyboardView) view.findViewById(R.id.kbView);
    }

    @Override
    protected void initViewListener() {
        tvClear.setOnClickListener(this);
        tvTop.setOnClickListener(this);
        kbView.setKeyboardListener(this);
    }

    @Override
    protected void doOtherThings() {
        kbView.registTextView(tvFilter);
        logNameProvider = new LogNameProvider(context);
        initMenuList();
    }

    private void initMenuList() {
        logNameAdapter = new LogNameAdapter(context, this);
        lvLog.setAdapter(logNameAdapter);
    }

    @Override
    public void onItemClick(LogNameTable logBean) {
        if(logBean != null) {
            Bundle bundle = new Bundle();
            bundle.putString("logName", logBean.getLogName());
            parent.onCallback(new CallbackModel(CallbackActionBean.ADD_LOG_DETAIL, bundle));
        }
    }

    @Override
    public void updateViewDetail(Bundle bundle) {
        if(logNameAdapter != null) {
            if (bundle != null) {
                logName = bundle.getString("logName");
                if (logName != null) {
                    logNameAdapter.setLogName(logName);
                }
            }
            logNameAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClear:
                logNameProvider.clearLogNames();
                break;
            case R.id.tvTop:
                try {
                    if (lvLog != null) {
                        lvLog.setSelection(0);
                    }
                } catch (Exception e) {

                }
                break;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility == View.VISIBLE) {
            Bundle bundle = new Bundle();
            bundle.putString("title", "首页");
            EventBus.getDefault().post(new EventBusUpdateBean("setTitle", bundle));
        } else {
            if (kbView != null) {
                kbView.setVisibility(View.GONE);
            }
        }
        super.setVisibility(visibility);
    }

    @Override
    public void onTextChanged(String oldValue, String newValue) {
        if(logNameAdapter != null) {
            logNameAdapter.setLogName(newValue);
            logNameAdapter.notifyDataSetChanged();
        }
    }
}
