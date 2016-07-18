package com.eastin.log;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.eastin.log.constant.LogConstant;
import com.eastin.log.db.MainConstantShare;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private SeekBar sbTag, sbLog;

    private TextView tvTagNum, tvLogNum, tvCopyTo;

    private Button btnTagNum, btnLogNum, btnStart, btnStop;

    private Switch sCopyTo;

    private MainConstantShare constantShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewListener();
        doOtherThings();
    }

    private void initView() {
        sbTag = (SeekBar) findViewById(R.id.sbTag);
        sbLog = (SeekBar) findViewById(R.id.sbLog);
        tvTagNum = (TextView) findViewById(R.id.tvTagNum);
        tvLogNum = (TextView) findViewById(R.id.tvLogNum);
        tvCopyTo = (TextView) findViewById(R.id.tvCopyTo);
        btnTagNum = (Button) findViewById(R.id.btnTagNum);
        btnLogNum = (Button) findViewById(R.id.btnLogNum);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        sCopyTo = (Switch) findViewById(R.id.sCopyTo);
    }

    private void initViewListener() {
        btnTagNum.setOnClickListener(this);
        btnLogNum.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        sbTag.setOnSeekBarChangeListener(this);
        sbLog.setOnSeekBarChangeListener(this);
        sCopyTo.setOnCheckedChangeListener(this);
    }

    private void doOtherThings() {
        int tag = MainConstantShare.getInstance(this).getInt(LogConstant.TAG_MAX, 0);
        int log = MainConstantShare.getInstance(this).getInt(LogConstant.LOG_MAX, 0);
        String copyTo = MainConstantShare.getInstance(this).getString(LogConstant.LOG_COPY_TO, "CLIP");
        if(tag != 0) {
            sbTag.setProgress(tag);
        }
        if(log != 0) {
            sbLog.setProgress(log);
        }
        if("FILE".equals(copyTo)) {
            sCopyTo.setChecked(true);
        } else {
            sCopyTo.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTagNum:
                Intent lnTag = new Intent(MainBroadcast.UPDATE_TAG_MAX);
                lnTag.putExtra("tagMax", Integer.parseInt(tvTagNum.getText().toString()));
                sendBroadcast(lnTag);
                MainConstantShare.getInstance(this).putInt(LogConstant.TAG_MAX, sbTag.getProgress());
                break;
            case R.id.btnLogNum:
                Intent lnLog = new Intent(MainBroadcast.UPDATE_LOG_MAX);
                lnLog.putExtra("logMax", Integer.parseInt(tvLogNum.getText().toString()));
                sendBroadcast(lnLog);
                MainConstantShare.getInstance(this).putInt(LogConstant.LOG_MAX, sbLog.getProgress());
                break;
            case R.id.btnStart:
                Intent service = new Intent();
                service.setClass(this, MainService.class);
                startService(service);
                break;
            case R.id.btnStop:
                Intent lnStop = new Intent(MainBroadcast.STOP_SERVICE);
                sendBroadcast(lnStop);
                break;
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sbTag:
                float x = 9.9f;
                int y = (int) (x * progress + 10);
                tvTagNum.setText("" + y);
                break;
            case R.id.sbLog:
                tvLogNum.setText("" + (90 * progress + 1000));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            tvCopyTo.setText("复制到文件");
        } else {
            tvCopyTo.setText("复制到粘贴板");
        }
        Intent lnLog = new Intent(MainBroadcast.UPDATE_COPY_TO);
        lnLog.putExtra("copyTo", isChecked ? "FILE" : "CLIP");
        sendBroadcast(lnLog);
        MainConstantShare.getInstance(this).putString(LogConstant.LOG_COPY_TO, isChecked ? "FILE" : "CLIP");
    }
}
