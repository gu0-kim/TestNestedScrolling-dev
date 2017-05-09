package com.project.gu.testnestedscrollingfirst.content.coordinator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author gu
 * @version 1.0
 */

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new ScrollAutoTop());
    }

    class ScrollAutoTop extends OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                modifyScroll();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    public void modifyScroll() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager != null && linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    /*
                    自动滚动补齐
                     */
            linearLayoutManager.scrollToPositionWithOffset(0, 0);
            Log.e("tag", "modifyScroll: auto scorll to top!");
        }
    }
}
