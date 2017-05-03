package com.project.gu.testnestedscrollingfirst.content.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import static com.project.gu.testnestedscrollingfirst.log.LogUtil.log;

/**
 * Created by gu on 2017/4/21.
 */

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        log("vp height= " + height);
        //        int btnlayoutHeight = getResources().getDimensionPixelSize(R.dimen.header_pic_height);
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        log("in vp measure!");
    }
}
