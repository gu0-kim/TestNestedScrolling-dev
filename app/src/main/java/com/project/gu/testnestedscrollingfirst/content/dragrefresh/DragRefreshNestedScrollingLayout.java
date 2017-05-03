package com.project.gu.testnestedscrollingfirst.content.dragrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.project.gu.testnestedscrollingfirst.R;

import static com.project.gu.testnestedscrollingfirst.log.LogUtil.log;

/**
 * Created by gu on 2017/4/22.
 * <p>
 * draglayout
 */

public class DragRefreshNestedScrollingLayout extends ViewGroup implements NestedScrollingParent, View.OnTouchListener {

    FlingDetector flingDetector = new FlingDetector();
    // Pass the FlingDetector to mGestureDetector to receive the appropriate callbacks
    GestureDetector mGestureDetector = new GestureDetector(getContext(), flingDetector);

    private class FlingDetector extends GestureDetector.SimpleOnGestureListener {
        public FlingDetector() {
            super();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            log("in onFling----:" + velocityY);
            fling(-(int) velocityY);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
    }


    private enum State {
        INIT, PULL, RELEASE_TO_REFRESH, BACK_ANIM, REFRESHING
    }

    private IRefresh mRefreshCallBack;
    private State curState = State.INIT;

    private ImageView bgImg, pbImg, searchImg, navBackImg;
    private LinearLayout contentView;
    private RelativeLayout userBtnLayout;
    private RelativeLayout topBar;
    private LinearLayout middleBar;
    private ViewPager vp;
    private Scroller mScroller;
    private int mTouchSlop;

    private int topBarSize, userBtnLayoutSize, maxScroll, picMaxHeight;
    private int drag2refreshMinDy;
    private Animation pbAnim;

    private NestedScrollingParentHelper nsp;

    public DragRefreshNestedScrollingLayout(Context context) {
        super(context);
    }

    public DragRefreshNestedScrollingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        nsp = new NestedScrollingParentHelper(this);
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        log("mTouchSlop= " + mTouchSlop);
        TypedArray tr = context.obtainStyledAttributes(attrs, R.styleable.refreshheader);
        drag2refreshMinDy = tr.getDimensionPixelSize(R.styleable.refreshheader_refreshMinHeight, 0);
        tr.recycle();
        pbAnim = AnimationUtils.loadAnimation(context, R.anim.pb_rotate_anim);
        setOnTouchListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bgImg = (ImageView) findViewById(R.id.bgImg);
        contentView = (LinearLayout) findViewById(R.id.contentView);
        topBar = (RelativeLayout) findViewById(R.id.topBar);
        middleBar = (LinearLayout) findViewById(R.id.middleBar);
        vp = (ViewPager) findViewById(R.id.vp);
        userBtnLayout = (RelativeLayout) findViewById(R.id.userBtnLayout);
        pbImg = (ImageView) findViewById(R.id.pbImg);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        navBackImg = (ImageView) findViewById(R.id.navBackImg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        log("-----onMeasure-----");
        if (bgImg != null) {
            measureChild(bgImg, widthMeasureSpec, heightMeasureSpec);
            picMaxHeight = bgImg.getDrawable().getIntrinsicWidth();
            log("picMaxHeight=" + picMaxHeight);
        }
        if (contentView != null) {
            int imgHeight = getResources().getDimensionPixelOffset(R.dimen.drag_header_height);
            int screebHeight = MeasureSpec.getSize(heightMeasureSpec);
            int navHeight = getResources().getDimensionPixelOffset(R.dimen.drag_middlebar_height);
            measureChild(contentView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(screebHeight + imgHeight - navHeight, MeasureSpec.EXACTLY));
            userBtnLayoutSize = userBtnLayout.getMeasuredHeight();
            log("navHeight=" + navHeight + ",screebHeight= " + screebHeight + ",imgHeight=" + imgHeight + ",total height is " + (imgHeight + screebHeight - navHeight));
        }
        if (topBar != null) {
            measureChild(topBar, widthMeasureSpec, heightMeasureSpec);
            log("viewpager height is " + vp.getMeasuredHeight());
            topBarSize = topBar.getMeasuredHeight();
            maxScroll = userBtnLayoutSize - topBarSize;
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        log("-----onLayout-----");
        bgImg.layout(0, 0, bgImg.getMeasuredWidth(), -contentView.getScrollY() + userBtnLayoutSize);
        contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        topBar.layout(0, 0, topBar.getMeasuredWidth(), topBar.getMeasuredHeight());
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //参数里target是实现了NestedScrolling机制的子元素，这个子元素可以不是父容器的直接子元素
        //child是包含了target的View，这个View是父容器的直接子元素
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        log("dx=" + dx);
        if (isHiddenTop(dy) || isShowTop(target, dy) || isPullTop(target, dy)) {
            contentViewScrollBy(dy);
            consumed[1] = dy;
            final int scrollY = contentView.getScrollY() + dy;
            updateTopBarByScrollY(scrollY);
            updateStateByScrollY(scrollY);
            //            updateProgressByScrollY(scrollY);
            updateBackgroundImg(scrollY);
        }
    }


    private void updateTopBarByScrollY(int scrollY) {
        if (scrollY >= maxScroll) {
            if (topBar.getBackground().getLevel() != 1) {
                topBar.getBackground().setLevel(1);
                searchImg.setImageLevel(1);
                navBackImg.setImageLevel(1);
                pbImg.setImageLevel(2);
            }
        } else if (scrollY >= 0 && scrollY < maxScroll) {
            if (topBar.getBackground().getLevel() != 0) {
                topBar.getBackground().setLevel(0);
                searchImg.setImageLevel(0);
                navBackImg.setImageLevel(0);
            }
            if (pbImg.getDrawable().getLevel() != 0) {
                pbImg.setImageLevel(0);
                rotationImageView(pbImg, 0);
            }
        } else if (scrollY < 0) {
            if (pbImg.getDrawable().getLevel() != 1) {
                pbImg.setImageLevel(1);
            }
            if (-scrollY > drag2refreshMinDy) {
                rotationImageView(pbImg, calDegree(scrollY));
            }
        }
    }

    /**
     * update progress bar by pull down
     *
     * @param scrollY scrolling distance y
     */
    private void updateProgressByScrollY(int scrollY) {
        if (-scrollY > 0) {
            pbImg.setImageLevel(1);
            if (-scrollY > drag2refreshMinDy) {
                rotationImageView(pbImg, calDegree(scrollY));
            }
        } else if (scrollY >= 0 && scrollY < maxScroll) {
            pbImg.setImageLevel(0);
            rotationImageView(pbImg, 0);
        } else if (scrollY >= maxScroll) {
            pbImg.setImageLevel(2);
        }
    }

    private void updateStateByScrollY(int scrollY) {
        if (-scrollY < drag2refreshMinDy && scrollY < 0) {
            setState(State.PULL);
        } else if (-scrollY > drag2refreshMinDy && scrollY < 0) {
            setState(State.RELEASE_TO_REFRESH);
        } else if (scrollY >= 0) {
            setState(State.INIT);
        }
    }

    private void startLoadingAnim() {
        log("loading");
        pbImg.setImageLevel(1);
        pbImg.startAnimation(pbAnim);
    }

    private void stopLoadingAnim() {
        pbImg.clearAnimation();
        pbImg.setImageLevel(0);
    }


    /**
     * update background imageview by pull down
     *
     * @param scrollY scrolling distance y
     */
    private void updateBackgroundImg(int scrollY) {
        LayoutParams p = bgImg.getLayoutParams();
        p.height = Math.max(userBtnLayoutSize - scrollY, topBarSize);
        bgImg.setLayoutParams(p);

        //        bgImg.layout(0, Math.max(0, -picMaxHeight + userBtnLayoutSize - scrollY), bgImg.getMeasuredWidth(), Math.max(userBtnLayoutSize - scrollY, topBarSize));
    }

    private int calDegree(int scrollY) {
        int y = -scrollY;
        return (int) (((float) y - drag2refreshMinDy) / drag2refreshMinDy * 360);
    }

    private void rotationImageView(ImageView pb, int degree) {
        pb.setPivotX(pb.getWidth() / 2);
        pb.setPivotY(pb.getHeight() / 2);
        pb.setRotation(degree);
    }


    /**
     * actionup后contentview定位
     */
    private void start2PullBack() {
        mScroller.startScroll(0, contentView.getScrollY(), 0, -contentView.getScrollY(), 500);
        postInvalidate();
    }

    private boolean need2SaveScrollY() {
        return contentView.getScrollY() >= 0;
    }

    int mDownY;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mScroller.isFinished() || getState().equals(State.REFRESHING) || getState().equals(State.BACK_ANIM)) {
            log("mScroller.isFinished()=" + mScroller.isFinished() + ",getState()=" + getState());
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                log("dispatchTouchEvent up! ");
                doActionUp();
                if (need2SaveScrollY()) {
                    setActionUpScroll(contentView.getScrollY());
                } else {
                    beforeAnim = getState();
                    setState(State.BACK_ANIM);
                    start2PullBack();
                    setActionUpScroll(0);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 注：
     * （1）onInterceptTouchEvent返回false，说明一定要有子view消耗这个事件（ontouch返回true或者有click）。
     * 返回false会遍历子view,如果都不消耗，则给自己的onTouchEvent处理。如果自己的onTouchEvent消耗了，下次不会再调用onInterceptTouchEvent,直接给onTouchEvent处理。
     * 本例子中，onTouchEvent就返回true销毁了事件
     * （2）onInterceptTouchEvent返回true,直接交给本层的onTouchEvent处理。
     * （3）down返回false，如果子view不消耗事件，随后的move、up都不会再进onInterceptTouchEvent。只有子view消耗事件，move 和 up才会继续进入onInterceptTouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        log("onInterceptTouchEvent !");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //如果下层有VIEW消耗event，此处的ACTION_MOVE会被执行，拖动大于mTouchSlop判定为拖动事件，则屏蔽事件（取消了点击事件）直接发给本层的onTouchEvent
                log("onInterceptTouchEvent move! getNestedScrollAxes()=" + getNestedScrollAxes());
                final int yDiff = Math.abs((int) ev.getY() - mDownY);
                if (yDiff > mTouchSlop && getNestedScrollAxes() == ViewCompat.SCROLL_AXIS_NONE) {
                    log("无滚动轴子view发生拖拽 yDiff= " + yDiff);
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                log("onTouchEvent move!");
                final int scrollY = -(int) event.getY() + mDownY + getActionUpScroll();
                updateTopBarByScrollY(scrollY);
                contentViewScrollTo(scrollY);
                updateStateByScrollY(scrollY);
                //                updateProgressByScrollY(scrollY);
                updateBackgroundImg(scrollY);
                break;
        }
        return true;
    }

    //actionup事件发生时scrollY值
    private int mActionUpScroll;

    private int getActionUpScroll() {
        return mActionUpScroll;
    }

    private void setActionUpScroll(int scroll) {
        this.mActionUpScroll = scroll;
    }


    private void setState(State state) {
        this.curState = state;
    }

    private State getState() {
        return curState;
    }

    //    private boolean dispathTouchEvent


    public void setRefreshCallBack(IRefresh callback) {
        this.mRefreshCallBack = callback;
    }

    public static interface IRefresh {
        public void onRefresh();
    }


    private void doActionUp() {
        if (getState().equals(State.RELEASE_TO_REFRESH)) {
            startLoadingAnim();
        } else if (getState().equals(State.PULL)) {
            updateProgressByScrollY(0);
            //            updateTopBarByScrollY(0);
        }
    }

    private boolean isHiddenTop(int dy) {
        return dy > 0 && contentView.getScrollY() < maxScroll;
    }

    private boolean isShowTop(View target, int dy) {
        return dy < 0 && contentView.getScrollY() >= 0 && !canScrollUp(target);
    }

    private boolean isPullTop(View target, int dy) {
        return dy < 0 && contentView.getScrollY() < 0 && !canScrollUp(target);
    }

    private boolean canScrollUp(View target) {
        return ViewCompat.canScrollVertically(target, -1);
    }

    private boolean canScrollDown(View target) {
        return ViewCompat.canScrollVertically(target, 1);
    }


    private void contentViewScrollTo(int scrollY) {
        log("scrollY---:" + scrollY);
        if (scrollY > maxScroll) {
            scrollY = maxScroll;
        }
        contentView.scrollTo(0, scrollY);
    }

    private void contentViewScrollBy(int dy) {
        contentViewScrollTo(contentView.getScrollY() + dy);
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
    public int getNestedScrollAxes() {
        return nsp.getNestedScrollAxes();
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        log("velocityY = " + velocityY + ",consumed = " + consumed + ",canscroll = " + canScrollUp(target));
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        log("onNestedPreFling velocityX=" + velocityX + ",velocityY=" + velocityY);
        if (contentView.getScrollY() >= 0 && contentView.getScrollY() <= maxScroll && !canScrollUp(target)) {
            fling((int) velocityY);
            return true;
        }
        return false;
    }


    private boolean mFling;

    public void fling(int velocityY) {
        if (mScroller.isFinished()) {
            mFling = true;
            mScroller.fling(0, contentView.getScrollY(), 0, velocityY, 0, 0, 0, maxScroll);
            postInvalidate();
        }
    }

    private State beforeAnim = State.INIT;

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            log("scroller scroll to " + mScroller.getCurrY());
            contentView.scrollTo(0, mScroller.getCurrY());
            updateBackgroundImg(mScroller.getCurrY());
            postInvalidate();
        } else {
            if (getState().equals(State.BACK_ANIM)) {
                if (beforeAnim.equals(State.PULL)) {
                    setState(State.INIT);
                    beforeAnim = State.INIT;
                    log("computeScroll--------------beforeAnim.equals(State.PULL)");
                } else if (beforeAnim.equals(State.RELEASE_TO_REFRESH)) {
                    if (mRefreshCallBack != null) {
                        mRefreshCallBack.onRefresh();
                        log("computeScroll--------------beforeAnim.equals(State.RELEASE_TO_REFRESH)");
                    }
                    setState(State.REFRESHING);
                    beforeAnim = State.INIT;
                }
            } else {
                if (mFling) {
                    //fling finish!
                    log("----fling finish!");
                    mFling = false;
                    updateTopBarByScrollY(Math.min(maxScroll, contentView.getScrollY()));
                    if (contentView.getScrollY() >= 0 && contentView.getScrollY() <= maxScroll) {
                        setActionUpScroll(contentView.getScrollY());
                        log("save scroll y!");
                    }
                }
            }
        }
        super.computeScroll();
    }

    public void refreshComplete() {
        stopLoadingAnim();
        setState(State.INIT);
        updateProgressByScrollY(0);
        log("refreshComplete");
    }
}
