package com.project.gu.testnestedscrollingfirst;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by chentian on 23/03/2017.
 */

public class MyChild extends ScrollView implements NestedScrollingChild {
    NestedScrollingChildHelper nscp;
    int lastY;

    //这两个数组用来接收父容器传过来的参数
    int[] consumed = new int[2];
    int[] offsetWindow = new int[2];

    int showHeight;
    int ivHeight;

    public MyChild(Context context) {
        this(context, null);
    }

    public MyChild(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //第一次测量，因为是wrap_content,测量出来的只是父容器除了ImageView和TextView剩余的高度
        //此次测量只是为了求得剩余的高度
        //如果没有第二次测量，那么下面的文字就会显示不出来
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        showHeight = getMeasuredHeight();
        //修改tv的高度，按照tv的内容重新测算尺寸
        TextView tv = (TextView) getChildAt(0);
        tv.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getRawY();
                int dy = lastY - y;
                lastY = y;

                //开启NestedScrolling机制，如果找到了匹配的父容器，那么就与父容器配合消费掉滑动距离
                if (startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)) {
                    //dy是我们传过去的滑动的距离，父容器可以根据逻辑来选择要不要消费，消费多少

                    if (dispatchNestedPreScroll(0, dy, consumed, offsetWindow)) {
                        dy -= consumed[1];
                        //                        scrollBy(0, dy);
                    } else {
                        Log.w("tag", "由孩子自己处理！");
                    }
                    Log.w("tag", "scroll dy = " + dy);
                    scrollBy(0, dy);
                }

                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    //scrollBy内部调用scrollTo,我们不能滑出去，也不能滑的太下面，我们要修正这些情况
    @Override
    public void scrollTo(@Px int x, @Px int y) {
        int maxY = getChildAt(0).getMeasuredHeight() - getMeasuredHeight();
        if (y > maxY) {
            y = maxY;
        } else if (y < 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    //这里使用单例模式提供Helper，我发现如果没有单例模式，机制就会失效
    //原因我大致的猜到了，但是我还不能具体的表达出来，如果有人知道，请在评论区留下言
    private NestedScrollingChildHelper getNscp() {
        if (nscp == null) {
            nscp = new NestedScrollingChildHelper(this);
            nscp.setNestedScrollingEnabled(true);
            
        }
        return nscp;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getNscp().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getNscp().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getNscp().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getNscp().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getNscp().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getNscp().dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getNscp().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getNscp().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getNscp().dispatchNestedPreFling(velocityX, velocityY);
    }
}
