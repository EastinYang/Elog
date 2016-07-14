package com.eastin.log;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastin.log.bean.CallbackActionBean;
import com.eastin.log.bean.CallbackModel;
import com.eastin.log.bean.EventBusUpdateBean;
import com.eastin.log.db.LogNameTable;
import com.eastin.log.db.LogTable;
import com.eastin.log.interfaces.IMain;
import com.eastin.log.provider.LogNameProvider;
import com.eastin.log.provider.LogProvider;
import com.eastin.log.util.DatetimeUtil;
import com.eastin.log.view.BaseView;
import com.eastin.log.view.LogDetailView;
import com.eastin.log.view.LogNameView;
import com.eastin.log.view.SettingView;
import com.eastin.log.view.ViewManager;
import com.litesuits.android.async.AsyncTask;

import de.greenrobot.event.EventBus;

/**
 * Created by Eastin on 16/7/5.
 */
public class MainService extends Service implements View.OnTouchListener, View.OnClickListener, IMain {
    private final static String GETLOG = "assistivetouch.eastin.com.getlog";

    // 控制球View
    private View touchBallView;
    private View touchBall;

    // 弹出菜单
    private View menuView;

    // WindowManager
    WindowManager wm, wmMenu;
    // WindowManagerParams
    WindowManager.LayoutParams params;
    // WindowManagerParams
    WindowManager.LayoutParams menuParams;
    private PopupWindow popup;

    // 点击坐标
    private float x, y;
    private float touchX, touchY;
    // 移动Flag
    private boolean isMoving;

    private final int BALL_VISIBLE_TIME = 5000;

    // 头部
    private TextView tvBack, tvHide, tvSetting;

    // 内容
    private RelativeLayout lytFragment;

    private ViewManager viewManager;

    private LogProvider logProvider;
    private LogNameProvider logNameProvider;

    private LogNameView logNameView;
    private LogDetailView logDetailView;
    private SettingView settingView;

    private BroadcastReceiver mainBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    if(GETLOG.equals(intent.getAction())) {
                        final String logName = intent.getStringExtra("logName");
                        final String logDetail = intent.getStringExtra("logDetail");
                        if(logName != null && logDetail != null
                                && !"".equals(logName) && !"".equals(logDetail)) {
                            String strDatetime = DatetimeUtil.getDateTime();
                            LogNameTable logNameTable = new LogNameTable();
                            logNameTable.setLogName(logName);
                            logNameTable.setDatetime(strDatetime);
                            logNameProvider.addLogName(logNameTable);
                            LogTable logTable = new LogTable();
                            logTable.setLogName(logName);
                            logTable.setLogDetail(logDetail);
                            logTable.setDatetime(strDatetime);
                            logProvider.addLog(logTable);
                        }
                    }
                    return null;
                }
            };
            task.execute();
        }
    };

    private final int SHOW_BALL = 100;
    private final int HIDE_BALL = 101;
    private boolean isAddingLog = false;
    private Handler uiHandler = new Handler();
    private Handler ballHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_BALL:
                    ballHandler.removeMessages(HIDE_BALL);
                    if(touchBall != null) {
                        touchBall.setAlpha(0.8f);
                    }
                    break;
                case HIDE_BALL:
                    ballHandler.removeMessages(SHOW_BALL);
                    if(touchBall != null) {
                        touchBall.setAlpha(0.3f);
                    }
                    break;
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initView();
        initViewListener();
        doOtherThings();
    }

    private void initView() {
        touchBallView = LayoutInflater.from(this).inflate(R.layout.ball, null);
        touchBall = touchBallView.findViewById(R.id.touchBall);
        menuView = LayoutInflater.from(this).inflate(R.layout.menu, null);
        tvBack = (TextView) menuView.findViewById(R.id.tvBack);
        tvHide = (TextView) menuView.findViewById(R.id.tvHide);
        tvSetting = (TextView) menuView.findViewById(R.id.tvSetting);
        lytFragment = (RelativeLayout) menuView.findViewById(R.id.lytFragment);
    }

    private void initViewListener() {
        touchBall.setOnTouchListener(this);
        touchBall.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        tvHide.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
    }

    private void doOtherThings() {
        EventBus.getDefault().register(this);
        logProvider = new LogProvider(this);
        logNameProvider = new LogNameProvider(this);
        ballHandler.sendEmptyMessage(HIDE_BALL);
        viewManager = new ViewManager(lytFragment);
        addBaseView(getLogNameView());
        createTouchBallView();
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GETLOG);
        registerReceiver(mainBroadcast, filter);
    }


    /**
     * 显示TouchBall
     */
    private void createTouchBallView() {
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.RGBA_8888;
        wm.addView(touchBallView, params);

        wmMenu = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        menuParams = new WindowManager.LayoutParams();
        menuParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        menuParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        menuParams.gravity = Gravity.CENTER;
        menuParams.x = 0;
        menuParams.y = 0;
        menuParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        menuParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        menuParams.format = PixelFormat.RGBA_8888;
        menuParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        wmMenu.addView(menuView, menuParams);
        menuView.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        x = event.getRawX();
        y = event.getRawY();
        // 触摸事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(ballHandler != null) {
                    ballHandler.sendEmptyMessage(SHOW_BALL);
                }
                isMoving = false;
                // 触摸坐标赋值
                touchX = event.getX();
                touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                isMoving = true;
                // 触摸坐标赋值
                params.x = (int) (x - touchX);
                params.y = (int) (y - touchY);
                wm.updateViewLayout(touchBallView, params);
                break;
            case MotionEvent.ACTION_UP:
                // 触摸坐标赋值
                touchX = touchY = 0;
                if(ballHandler != null) {
                    ballHandler.sendEmptyMessageDelayed(HIDE_BALL, BALL_VISIBLE_TIME);
                }
                break;
            default:
                break;
        }
        if (isMoving) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touchBall:
                showMenuWindow();
                break;
            case R.id.tvBack:
                removeBaseView();
                break;
            case R.id.tvHide:
                closeMenuWindow();
                break;
            case R.id.tvSetting:
                addBaseView(getSettingView());
                break;
        }
    }

    /**
     * 显示Popup菜单
     */
    private void showMenuWindow() {
        touchBallView.setVisibility(View.GONE);
        menuView.setVisibility(View.VISIBLE);
    }

    private void closeMenuWindow() {
        touchBallView.setVisibility(View.VISIBLE);
        menuView.setVisibility(View.GONE);
    }

    private void addBaseView(BaseView view) {
        viewManager.addView(view);
    }

    private void removeBaseView() {
        if(!viewManager.removeTopView()) {
            closeMenuWindow();
        }
    }


    @Override
    public void onCallback(final CallbackModel model) {
        if(model != null) {
            if(CallbackActionBean.ADD_LOG_DETAIL.equals(model.getAction())) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        addBaseView(getLogDetailView());
                        getLogDetailView().updateViewDetail(model.getBundle());
                    }
                });
            } else if(CallbackActionBean.STOP_SERVICE.equals(model.getAction())) {
                wm.removeViewImmediate(touchBallView);
                wmMenu.removeViewImmediate(menuView);
                viewManager.removeAllViews();
                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        ballHandler.removeMessages(SHOW_BALL);
        ballHandler.removeMessages(HIDE_BALL);
        ballHandler = null;
        uiHandler = null;
        unregisterReceiver(mainBroadcast);
        super.onDestroy();
    }

    private LogNameView getLogNameView() {
        if(logNameView == null) {
            logNameView = new LogNameView(this, this);
        }
        return logNameView;
    }

    private LogDetailView getLogDetailView() {
        if(logDetailView == null) {
            logDetailView = new LogDetailView(this, this);
        }
        return logDetailView;
    }

    private SettingView getSettingView() {
        if(settingView == null) {
            settingView = new SettingView(this, this);
        }
        return settingView;
    }

    public void onEventMainThread(EventBusUpdateBean updateBean) {
        if(updateBean != null) {
            if("log".equals(updateBean.getAction())) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (viewManager != null) {
                            if (logDetailView != null && viewManager.isContainView(logDetailView)) {
                                logDetailView.updateViewDetail(null);
                            }
                        }
                    }
                });
            } else if("logName".equals(updateBean.getAction())) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (viewManager != null) {
                            if (logNameView != null && viewManager.isContainView(logNameView)) {
                                logNameView.updateViewDetail(null);
                            }
                        }
                    }
                });
            }
        }
    }





}
