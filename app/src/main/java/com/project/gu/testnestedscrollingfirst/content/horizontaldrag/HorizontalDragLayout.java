package com.project.gu.testnestedscrollingfirst.content.horizontaldrag;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.project.gu.testnestedscrollingfirst.log.LogUtil;

/**
 * Created by gu on 2017/4/28.
 */

public class HorizontalDragLayout extends HorizontalScrollView {

    private static final String TAG = "TAG";
    private boolean once = true;
    private ViewGroup mWraper;
    private ViewGroup rvFst, rvSec, rvThd;
    private int screenWidth;
    private Scroller mScroller;

    public HorizontalDragLayout(Context context) {
        super(context);
    }

    public HorizontalDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (once) {
            once = false;
            screenWidth = MeasureSpec.getSize(widthMeasureSpec);
            mWraper = (LinearLayout) getChildAt(0);
            rvFst = (ViewGroup) mWraper.getChildAt(0);
            rvSec = (ViewGroup) mWraper.getChildAt(1);
            rvThd = (ViewGroup) mWraper.getChildAt(2);
            rvFst.getLayoutParams().width = screenWidth;
            rvSec.getLayoutParams().width = screenWidth;
            rvThd.getLayoutParams().width = screenWidth;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                final int index = (int) (1f * getScrollX() / screenWidth + 0.5);
                final int scrollX = index * screenWidth;
                animScrollTo(scrollX - getScrollX());
                LogUtil.log("ACTION_UP scrollX= " + scrollX);
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.w(TAG, "onScrollChanged: l= " + l);
    }

    private void animScrollTo(int scrollX) {
        mScroller.startScroll(getScrollX(), 0, scrollX, 0, 800);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
        super.computeScroll();
    }
}
