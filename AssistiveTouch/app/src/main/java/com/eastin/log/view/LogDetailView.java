package com.eastin.log.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.eastin.log.R;
import com.eastin.log.adapter.LogDetailAdapter;
import com.eastin.log.interfaces.IMain;
import com.eastin.log.provider.LogProvider;

/**
 * Created by Eastin on 16/7/6.
 */
public class LogDetailView extends BaseView implements AdapterView.OnItemLongClickListener, View.OnClickListener {
    private LogDetailAdapter detailAdapter;
    private String logName;
    private String logDetail;
    private TextView tvClear, tvTop;
    private EditText etFilter;

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
        etFilter = (EditText) view.findViewById(R.id.etFilter);
    }

    @Override
    protected void initViewListener() {
        lvLogDetail.setOnItemLongClickListener(this);
        tvClear.setOnClickListener(this);
        tvTop.setOnClickListener(this);
    }

    @Override
    protected void doOtherThings() {
        logProvider = new LogProvider(context);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }


    @Override
    public void updateViewDetail(Bundle bundle) {
        if (bundle != null) {
            logName = bundle.getString("logName");
            if (logName != null && !"".equals(logName)) {
                if(detailAdapter == null) {
                    detailAdapter = new LogDetailAdapter(context, logName);
                    lvLogDetail.setAdapter(detailAdapter);
                } else {
                    detailAdapter.setLog(logName, logDetail);
                    detailAdapter.notifyDataSetChanged();
                }
            }
        } else if(detailAdapter != null) {
            detailAdapter.notifyDataSetChanged();
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
}
