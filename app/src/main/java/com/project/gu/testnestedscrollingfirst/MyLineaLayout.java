package com.project.gu.testnestedscrollingfirst;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by gu on 2017/4/18.
 */

public class MyLineaLayout extends LinearLayout {
    public MyLineaLayout(Context context) {
        super(context);
    }

    public MyLineaLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    int downY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = downY - (int) ev.getY();
                scrollBy(0, dy);
                Log.w("tag", "move scrollBy");
                break;
            default:
                scrollTo(0, 0);
                break;
        }
        return true;
    }
}
