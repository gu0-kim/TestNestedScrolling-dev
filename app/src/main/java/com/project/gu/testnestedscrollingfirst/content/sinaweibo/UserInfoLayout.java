package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.gu.testnestedscrollingfirst.R;
import com.project.gu.testnestedscrollingfirst.log.LogUtil;

/**
 * Created by gu on 2017/4/28.
 */

public class UserInfoLayout extends ViewGroup {
    private RelativeLayout frameTop;
    private ViewPager vp;
    private boolean onloaded, onMeasured;

    public UserInfoLayout(Context context) {
        super(context);
    }

    public UserInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        frameTop = (RelativeLayout) findViewById(R.id.frameTop);
        vp = (ViewPager) findViewById(R.id.vp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (frameTop != null) {
            measureChild(frameTop, widthMeasureSpec, heightMeasureSpec);
        }
        if (vp != null) {
            measureChild(vp, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //顶部漏出1像素刷新头顶部
        frameTop.layout(0, 1, frameTop.getMeasuredWidth(), frameTop.getMeasuredHeight() + 1);
        vp.layout(0, 0, vp.getMeasuredWidth(), vp.getMeasuredHeight());
    }

}
