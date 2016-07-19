package com.eastin.log.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eastin.log.R;
import com.eastin.log.interfaces.IKeyboardListener;

import java.util.ArrayList;

/**
 * Created by Eastin on 16/7/19.
 */
public class KeyboardView extends RelativeLayout implements View.OnClickListener {
    private View view;
    private Context context;
    private LinearLayout lytMain;
    private ArrayList<String[]> keyList;
    private TextView textView;
    private int itemHeight;
    private float tSize;
    private int itemPadding;
    private IKeyboardListener keyboardListener;

    public void setKeyboardListener(IKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public void registTextView(TextView textView) {
        this.textView = textView;
        if(textView != null) {
            textView.setTag("mainText");
            textView.setOnClickListener(this);
        }
    }

    public KeyboardView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        view = View.inflate(context, R.layout.keyborad, this);
        itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        tSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 5, context.getResources().getDisplayMetrics());
        itemPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
        initView();
        doOtherThing();
    }

    private void doOtherThing() {
        initDefaultKey();
        generateKey();
    }


    private void initView() {
        lytMain = (LinearLayout) view.findViewById(R.id.lytMain);
    }

    private void initDefaultKey() {
        getKeyList().add(new String[]{"1","2","3","4","5","6","7","8","9","0"});
        getKeyList().add(new String[]{"Q","W","E","R","T","Y","U","I","O","P"});
        getKeyList().add(new String[]{"A","S","D","F","G","H","J","K","L", " "});
        getKeyList().add(new String[]{"Z","X","C","V","B","N","M","<","<<", "x"});
    }

    private void generateKey() {
        if(getKeyList().size() > 0) {
            lytMain.removeAllViews();
            View vline = new View(context);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            vline.setBackgroundColor(getResources().getColor(R.color.white));
            vline.setLayoutParams(lineParams);
            lytMain.addView(vline);
            int size = getKeyList().size();
            for(int i = 0; i < size; i++) {
                initLine(getKeyList().get(i));
            }
        }
    }

    private ArrayList<String[]> getKeyList() {
        if(keyList == null) {
            keyList = new ArrayList<>();
        }
        return keyList;
    }

    private void initLine(String[] line) {
        LinearLayout lytLine = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight);
        lytLine.setPadding(itemPadding, itemPadding, itemPadding, 0);
        lytLine.setLayoutParams(layoutParams);
        lytLine.setOrientation(LinearLayout.HORIZONTAL);
        for(int i = 0; i < line.length; i ++) {
            TextView item = new TextView(context);
            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            itemParams.setMargins(itemPadding, itemPadding, itemPadding, itemPadding);
            item.setTextColor(getResources().getColor(R.color.white));
            item.setBackgroundColor(getResources().getColor(R.color.gray));
            item.setLayoutParams(itemParams);
            item.setTextSize(tSize);
            item.setText(line[i]);
            item.setTag(line[i]);
            item.setGravity(Gravity.CENTER);
            item.setOnClickListener(this);
            lytLine.addView(item);
        }
        lytMain.addView(lytLine);
    }

    @Override
    public void onClick(View v) {
        if("mainText".equals(v.getTag())) {
            this.setVisibility(View.VISIBLE);
        } else if("<".equals(v.getTag())) {
            if(textView != null) {
                String before = textView.getText().toString();
                if(before != null && !"".equals(before)) {
                    if (before.length() > 1) {
                        String after = before.substring(0, before.length() - 1);
                        textView.setText(after);
                        addChange(before, after);
                    } else {
                        textView.setText("");
                        addChange(before, "");
                    }
                }
            }
        } else if("<<".equals(v.getTag())) {
            if(textView != null) {
                String before = textView.getText().toString();
                textView.setText("");
                addChange(before, "");
            }
        } else if("x".equals(v.getTag())) {
            this.setVisibility(View.GONE);
        } else {
            if(textView != null) {
                String before = textView.getText().toString();
                String after = before + v.getTag();
                textView.setText(after);
                addChange(before, after);
            }
        }
    }

    private void addChange(String before, String after) {
        if(keyboardListener != null) {
            keyboardListener.onTextChanged(before, after);
        }
    }
}
