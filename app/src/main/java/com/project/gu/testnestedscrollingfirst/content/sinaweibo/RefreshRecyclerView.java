package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
    private int mHeaderInitHeight;
    private ViewGroup mRefreshHeader;
    LinearLayoutManager mLayoutManager;
    private Scroller mScroller;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        addOnScrollListener(new Scrolllistener());
    }


    private boolean mIsRecord;
    private int mStartY;
    private int mFirstItemIndex;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mIsRecord && mFirstItemIndex == 0) {
                    mStartY = (int) e.getY();
                    mIsRecord = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsRecord && mFirstItemIndex == 0) {
                    mStartY = (int) e.getY();
                    mIsRecord = true;
                }
                if (!mIsRecord || mState == State.LOADING) {
                    break;
                }
                final int offset = (int) e.getY() - mStartY;
                if (offset < 0) {
                    mIsRecord = false;
                    break;
                }
                if (isPullState(offset)) {
                    updateState(offset);
                    updateHeader(offset);
                    if (listener != null) {
                        //                        listener.onPull(Math.min(offset, mHeaderInitHeight - 1));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState == State.LOADING) {
                    mIsRecord = false;
                    break;
                }
                if (mIsRecord && mState != State.IDLE && mState != State.LOADING) {
                    log("-----headerResetAnim-----");
                    headerResetAnim();
                }
                mIsRecord = false;
                break;
        }
        return super.onTouchEvent(e);
    }

    private void updateHeader(int offset) {
        mRefreshHeader.setPadding(0, Math.min(offset - mHeaderInitHeight + 1, 0), 0, 0);
    }

    private int getHeaderPaddingTop() {
        return mRefreshHeader.getPaddingTop();
    }

    private void headerReset() {
        mRefreshHeader.setPadding(0, -mHeaderInitHeight + 1, 0, 0);
    }

    private void headerResetAnim() {
        if (mScroller.isFinished()) {
            mScroller.startScroll(0, mRefreshHeader.getHeight(), 0, -mRefreshHeader.getHeight() + 1, 1000);
            invalidate();
        }
    }


    private void updateState(int offset) {
        if (offset < REFRESH_MIN_DY) {
            mState = State.PULL;
        } else {
            mState = State.RELEASE_TO_REFRESH;
        }
    }

    private boolean isPullState(int offset) {
        return mFirstItemIndex == 0 && offset >= 0;
    }

    private boolean isLoading() {
        return mState.equals(State.LOADING);
    }


    public interface NotifyScrollListener {
        public void onScrolled(RecyclerView recyclerView, int dx, int dy);

        public void onPull(int offset);
    }

    private NotifyScrollListener listener;

    public void setNotifyListener(NotifyScrollListener listener) {
        this.listener = listener;
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
        mRefreshHeader = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.rv_refresh_header, this, false);
        mearsureHeader(mRefreshHeader);
        mHeaderInitHeight = mRefreshHeader.getMeasuredHeight();
        headerReset();
    }

    private void mearsureHeader(ViewGroup header) {
        ViewGroup.LayoutParams p = header.getLayoutParams();
        int widthSpec = MeasureSpec.makeMeasureSpec(p.width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        header.measure(widthSpec, heightSpec);
    }

    public ViewGroup getRefreshHeader() {
        return mRefreshHeader;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            updateHeader(mScroller.getCurrY());
            if (listener != null) {
                //                listener.onPull(mScroller.getCurrY());
            }
            invalidate();
        }
    }


    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        if (position == 0) {
            return itemHeight;
        } else if (position == 1) {
            return 1 + mHeaderInitHeight;
        } else {
            return 1 + mHeaderInitHeight + (position - 2) * itemHeight - firstVisiableChildView.getTop();
        }
    }
}
