package com.project.gu.testnestedscrollingfirst.content.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author
 * @version 1.0
 * @date 2017/5/9
 */

public class MyScrollView extends ScrollView {

    public void setBgScrollView(ScrollView bgScrollView) {
        this.bgScrollView = bgScrollView;
    }

    private ScrollView bgScrollView;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (bgScrollView != null) {
            bgScrollView.scrollTo(0, t);
        }
    }
}
