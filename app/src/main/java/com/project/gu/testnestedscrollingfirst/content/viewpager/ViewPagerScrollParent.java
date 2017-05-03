package com.project.gu.testnestedscrollingfirst.content.viewpager;

/**
 * Created by gu on 2017/4/18.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class ViewPagerScrollParent extends LinearLayout implements NestedScrollingParent {
    NestedScrollingParentHelper nsp;
    ImageView iv;
    ViewPager nsc;
    int ivHeight;

    public ViewPagerScrollParent(Context context) {
        this(context, null);
    }

    public ViewPagerScrollParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //创建一个Helper类
        nsp = new NestedScrollingParentHelper(this);
    }

    //拿到父容器里面的三个子View
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        iv = (ImageView) getChildAt(0);
        nsc = (ViewPager) getChildAt(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ivHeight = iv.getMeasuredHeight();
        Log.w("tag", "iv height= " + ivHeight);
        nsc.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(nsc.getMeasuredHeight() + ivHeight, MeasureSpec.EXACTLY));
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(getMeasuredHeight() + ivHeight, MeasureSpec.EXACTLY));
        Log.w("tag", "recyclerview height is " + nsc.getMeasuredHeight());
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //参数里target是实现了NestedScrolling机制的子元素，这个子元素可以不是父容器的直接子元素
        //child是包含了target的View，这个View是父容器的直接子元素
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //dy是子View传过来的，来询问父容器是不是要消费他，要的话，就把dy放进consumed数组,表示我消费了
        //其中consumed数组，consumed[0]表示x方向的距离，consumed[1]表示y方向的距离
        Log.w("tag", "onNestedPreScroll");
        boolean hiddenTop = dy > 0 && getScrollY() < ivHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !canScrollUp(target);
        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
            //            Log.w("tag", "showtop is " + showTop + ",consumed is " + dy);
        }
    }

    private boolean canScrollUp(View target) {
        return ViewCompat.canScrollVertically(target, -1);
    }

    private boolean canScrollDown(View target) {
        return ViewCompat.canScrollVertically(target, 1);
    }


    //scrollBy内部调用scrollTo,我们父容器不能滑出去，也不能滑的太下面，我们要修正这些情况
    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (y > ivHeight) {
            y = ivHeight;
        } else if (y < 0) {
            y = 0;
        }

        super.scrollTo(x, y);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        nsp.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        nsp.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return nsp.getNestedScrollAxes();
    }
}

