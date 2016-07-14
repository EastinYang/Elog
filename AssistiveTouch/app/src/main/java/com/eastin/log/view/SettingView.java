package com.eastin.log.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.eastin.log.R;
import com.eastin.log.bean.CallbackActionBean;
import com.eastin.log.bean.CallbackModel;
import com.eastin.log.interfaces.IMain;



/**
 * Created by Eastin on 16/7/6.
 */
public class SettingView extends BaseView implements View.OnClickListener {
    private Button btnExist;

    public SettingView(Context context, IMain parent) {
        super(context, parent);
    }

    @Override
    protected int layoutId() {
        return R.layout.setting_f;
    }

    @Override
    protected void initView() {
        btnExist = (Button) view.findViewById(R.id.btnExist);
    }

    @Override
    protected void initViewListener() {
        btnExist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExist:
                parent.onCallback(new CallbackModel(CallbackActionBean.STOP_SERVICE, null));
                break;
        }
    }
}
