package com.eastin.log;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewListener();
        doOtherThings();
    }

    private void initView() {

    }

    private void initViewListener() {

    }

    private void doOtherThings() {
        Intent service = new Intent();
        service.setClass(this, MainService.class);
        startService(service);
    }
}
