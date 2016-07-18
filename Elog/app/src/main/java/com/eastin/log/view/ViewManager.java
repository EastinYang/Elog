package com.eastin.log.view;

import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Eastin on 16/7/6.
 */
public class ViewManager {
    private ArrayList<BaseView> viewList;
    private RelativeLayout parent;

    public ViewManager(RelativeLayout parent) {
        this.parent = parent;
        viewList = new ArrayList<>();
    }

    public void addView(BaseView view) {
        try {
            if(viewList.size() > 0) {
                viewList.get(viewList.size() - 1).setVisibility(View.GONE);
            }
            RelativeLayout.LayoutParams layoutParams
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            parent.addView(view, layoutParams);
            viewList.add(view);
            view.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }

    public boolean removeTopView() {
        try {
            if (viewList.size() > 1) {
                parent.removeViewAt(viewList.size() - 1);
                viewList.get(viewList.size() - 2).setVisibility(View.VISIBLE);
                viewList.remove(viewList.size() - 1);
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public void removeAllViews() {
        try {
            parent.removeAllViews();
            viewList.clear();
        } catch (Exception e) {

        }
    }

    public boolean isContainView(BaseView view) {
        return viewList.contains(view);
    }
}
