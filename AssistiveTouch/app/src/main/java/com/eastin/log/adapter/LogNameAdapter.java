package com.eastin.log.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.eastin.log.R;
import com.eastin.log.db.LogNameTable;
import com.eastin.log.interfaces.ILogNameItemClick;
import com.eastin.log.provider.LogNameProvider;
import com.litesuits.android.async.AsyncTask;

import javax.net.ssl.HandshakeCompletedEvent;

/**
 * Created by Eastin on 16/7/5.
 */
public class LogNameAdapter extends BaseAdapter {
    private LogNameProvider provider;
    private Context context;
    private ILogNameItemClick logNameItemClick;
    private String logName = "";
    private int count = 0;

    private Handler uiHandler = new Handler();

    public LogNameAdapter(Context context, ILogNameItemClick logNameItemClick) {
        this.context = context;
        this.logNameItemClick = logNameItemClick;
        provider = new LogNameProvider(context);
        count = provider.getLogNameCount(logName);
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return provider.getLogNameItem(position, logName);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.log_name_item, null);
            holder.btn = (Button) convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        display(position, holder);
        return convertView;
    }

    private void display(final int position, final ViewHolder holder) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final LogNameTable table = (LogNameTable) getItem(position);
                if(table != null) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.btn.setVisibility(View.VISIBLE);
                            holder.btn.setText(table.getLogName());
                            holder.btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(logNameItemClick != null) {
                                        logNameItemClick.onItemClick(table);
                                    }
                                }
                            });
                        }
                    });
                } else {
                    Log.i("LogProvider", "LogNameTable null,count:" + count + ";position:" + position);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.btn.setVisibility(View.GONE);
                        }
                    });
                }
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void notifyDataSetChanged() {
        count = provider.getLogNameCount(logName);
        Log.i("LogProvider", "count:" + count);
        super.notifyDataSetChanged();
    }

    class ViewHolder {
        private Button btn;
    }
}
