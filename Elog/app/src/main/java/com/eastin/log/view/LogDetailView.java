package com.eastin.log.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.eastin.log.R;
import com.eastin.log.adapter.LogDetailAdapter;
import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.interfaces.IKeyboardListener;
import com.eastin.log.interfaces.IMain;
import com.eastin.log.provider.LogProvider;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/6.
 */
public class LogDetailView extends BaseView implements View.OnClickListener, IKeyboardListener {
    private LogDetailAdapter detailAdapter;
    private String logName;
    private String logDetail;
    private TextView tvClear, tvTop, tvFilter;
    private KeyboardView kbView;

    private LogProvider logProvider;

    private ListView lvLogDetail;

    public LogDetailView(Context context, IMain parent) {
        super(context, parent);
    }

    @Override
    protected int layoutId() {
        return R.layout.log_detail_f;
    }

    @Override
    protected void initView() {
        lvLogDetail = (ListView) view.findViewById(R.id.lvLogDetail);
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
        logProvider = new LogProvider(context);
    }


    @Override
    public void updateViewDetail(Bundle bundle) {
        if (bundle != null) {
            logName = bundle.getString("logName") == null ? logName : bundle.getString("logName");
            logDetail = bundle.getString("logDetail");
            if (logName != null && !"".equals(logName)) {
                setTitle();
                if(detailAdapter == null) {
                    detailAdapter = new LogDetailAdapter(context, logName);
                    detailAdapter.setLog(logName, logDetail);
                    lvLogDetail.setAdapter(detailAdapter);
                } else {
                    detailAdapter.setCount(0);
                    detailAdapter.setLog(logName, logDetail);
                    detailAdapter.myNotifyDataSetChanged();
                }
            }
        } else if(detailAdapter != null) {
            detailAdapter.myNotifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClear:
                if(logName != null && !"".equals(logName)) {
                    logProvider.clearLogDetails(logName);
                }
                break;
            case R.id.tvTop:
                try {
                    if (lvLogDetail != null) {
                        lvLogDetail.setSelection(0);
                    }
                } catch (Exception e) {

                }
                break;
        }
    }

    private void setTitle() {
        Bundle bundle = new Bundle();
        bundle.putString("title", logName);
        EventBus.getDefault().post(new EventBusUpdateBean("setTitle", bundle));
    }

    @Override
    public void setVisibility(int visibility) {
        if(visibility == View.VISIBLE) {
            setTitle();
        } else {
            if (kbView != null) {
                tvFilter.setText("");
                kbView.setVisibility(View.GONE);
            }
        }
        super.setVisibility(visibility);
    }

    @Override
    public void onTextChanged(String oldValue, String newValue) {
        if(detailAdapter != null) {
            detailAdapter.setLog(logName, newValue);
            detailAdapter.notifyDataSetChanged();
        }
    }
}
