package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.project.gu.testnestedscrollingfirst.R;

import static com.project.gu.testnestedscrollingfirst.log.LogUtil.log;

/**
 * Created by gu on 2017/5/1.
 */

public class RefreshRecyclerView extends RecyclerView {
    private enum State {IDLE, PULL, RELEASE_TO_REFRESH, LOADING}

    private State mState = State.PULL;
    private static final int REFRESH_MIN_DY = 160;
    //    private static final int HEADER_IN_SCREEN = 1;
    private int mHeaderInitHeight;
    private ViewGroup mRefreshHeader;
    LinearLayoutManager mLayoutManager;
    private Scroller mScroller;
    private int mTouchslop;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        addOnScrollListener(new Scrolllistener());
        mTouchslop = ViewConfiguration.get(context).getScaledTouchSlop();
        log("mtouchslop= " + mTouchslop);
    }


    private boolean mIsRecord;
    private int mStartY, mStartX;
    private int mFirstItemIndex;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "log----(down)----");
                if (needRecord()) {
                    recordStart(e);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (needRecord()) {
                    recordStart(e);
                }
                if (!mIsRecord || mState == State.LOADING) {
                    break;
                }
                final int offsetY = (int) e.getY() - mStartY;
                final int offsetX = (int) e.getX() - mStartX;
                if (offsetY < 0) {
                    mIsRecord = false;
                    break;
                }
                
                if (Math.abs(offsetY) > Math.abs(offsetX)) {
                    if (Math.abs(offsetY) > mTouchslop) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (isPull(offsetY) && !isAnim()) {
                    //                        log("----mStartY= (" + mStartY + "),offsetY= (" + offsetY + ")");
                    updateState(offsetY);
                    updateHeader(offsetY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState == State.LOADING) {
                    mIsRecord = false;
                    break;
                }
                if (mIsRecord && mState != State.IDLE) {
                    log("-----headerResetAnim-----");
                    headerResetAnim();
                }
                mIsRecord = false;
                break;
        }
        return super.onTouchEvent(e);
    }

    private boolean needRecord() {
        return !mIsRecord && mFirstItemIndex == 0 && mRefreshHeader.getHeight() >= mHeaderInitHeight && mRefreshHeader.getTop() >= 0;
    }

    private void recordStart(MotionEvent e) {
        mStartY = (int) e.getY();
        mStartX = (int) e.getX();
        mIsRecord = true;
    }

    private void updateHeader(int offset) {
        ViewGroup.LayoutParams params = mRefreshHeader.getLayoutParams();
        params.height = mHeaderInitHeight + offset;
        mRefreshHeader.setLayoutParams(params);
    }

    private void setHeaderHeight(int height) {
        ViewGroup.LayoutParams params = mRefreshHeader.getLayoutParams();
        params.height = height;
        mRefreshHeader.setLayoutParams(params);
    }

    private void headerReset() {
        ViewGroup.LayoutParams params = mRefreshHeader.getLayoutParams();
        params.height = mHeaderInitHeight;
        mRefreshHeader.setLayoutParams(params);
        //        mRefreshHeader.setPadding(0, -mHeaderInitHeight + HEADER_IN_SCREEN, 0, 0);
    }

    private void headerResetAnim() {
        if (mScroller.isFinished()) {
            mScroller.startScroll(0, mRefreshHeader.getHeight(), 0, -mRefreshHeader.getHeight() + mHeaderInitHeight, 500);
            invalidate();
        }
    }

    private boolean isAnim() {
        return !mScroller.isFinished();
    }


    private void updateState(int offset) {
        if (offset < REFRESH_MIN_DY) {
            mState = State.PULL;
        } else {
            mState = State.RELEASE_TO_REFRESH;
        }
    }

    private boolean isPull(int offset) {
        log("mRefreshHeader.getHeight()= " + mRefreshHeader.getHeight());
        return mFirstItemIndex == 0 && offset >= 0;
    }

    private class Scrolllistener extends OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mFirstItemIndex = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        }
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        setLayoutManager(new LinearLayoutManager(context));
        mRefreshHeader = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.userinfo_header_layout, this, false);
        mearsureHeader(mRefreshHeader);
        mHeaderInitHeight = mRefreshHeader.getMeasuredHeight();
        //        log("header height =" + mHeaderInitHeight);
        //        headerReset();
    }

    private void mearsureHeader(ViewGroup header) {
        ViewGroup.LayoutParams p = header.getLayoutParams();
        int widthSpec = MeasureSpec.makeMeasureSpec(p.width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY);
        header.measure(widthSpec, heightSpec);
    }

    public ViewGroup getRefreshHeader() {
        return mRefreshHeader;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            setHeaderHeight(mScroller.getCurrY());
            invalidate();
        }
    }

}
