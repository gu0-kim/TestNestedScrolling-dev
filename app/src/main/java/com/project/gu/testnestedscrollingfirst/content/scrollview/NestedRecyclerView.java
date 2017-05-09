package com.project.gu.testnestedscrollingfirst.content.scrollview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author
 * @version 1.0
 * @date 2017/5/9
 */

public class NestedRecyclerView extends RecyclerView {

    private MyScrollView parentView;

    public NestedRecyclerView(Context context) {
        super(context);
    }

    public NestedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    
}
