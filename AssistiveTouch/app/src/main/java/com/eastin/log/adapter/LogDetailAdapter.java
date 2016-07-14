package com.eastin.log.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eastin.log.R;
import com.eastin.log.db.LogNameTable;
import com.eastin.log.db.LogTable;
import com.eastin.log.provider.LogProvider;
import com.litesuits.android.async.AsyncTask;

/**
 * Created by Eastin on 16/7/5.
 */
public class LogDetailAdapter extends BaseAdapter {
    private Context context;
    private LogProvider provider;
    private String logName;
    private String logDetail;
    private int count;
    private Handler uiHandler = new Handler();

    public LogDetailAdapter(Context context, String logName) {
        this.context = context;
        this.logName = logName;
        this.provider = new LogProvider(context);
        count = provider.getLogDetailCount(logName, logDetail);
    }

    public void setLog(String logName, String logDetail) {
        this.logName = logName;
        this.logDetail = logDetail;
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
        return provider.getLogDetailItem(position, logName, logDetail);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.log_detail_item, null);
            holder.tvDetail = (TextView) convertView.findViewById(R.id.tvDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        display(position, holder);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        count = provider.getLogDetailCount(logName, logDetail);
        super.notifyDataSetChanged();
    }

    private void display(final int position, final ViewHolder holder) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final LogTable bean = (LogTable) getItem(position);
                if(bean != null) {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.tvDetail.setVisibility(View.VISIBLE);
                            holder.tvDetail.setText(bean.getLogDetail());
                        }
                    });
                } else {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.tvDetail.setVisibility(View.GONE);
                        }
                    });
                }
                return null;
            }
        };
        task.execute();
    }

    class ViewHolder {
        private TextView tvDetail;
    }
}
