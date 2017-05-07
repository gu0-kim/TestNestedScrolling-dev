package com.project.gu.testnestedscrollingfirst.content.coordinator;


import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

import com.project.gu.testnestedscrollingfirst.content.coordinator.ToolbarStateHelper.ToolBarState;

public class AppBarOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;

    private TabLayout mTabLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private long mScrimAnimationDuration;
    private int mScrimAlpha;
    private boolean mScrimsAreShown;
    //    private ValueAnimatorCompat mScrimAnimator;
    private ValueAnimator mScrimAnimator;
    private final int mNormalColor;
    private final int mSelectedColor;
    private int mCollapseTabSelectTextColor;
    private int mCollapseTabNormalTextColor;
    private int mCollapseTabBackgroundColor;
    private int mCollapsedHeight;
    private ToolbarStateHelper mHelper;


    public AppBarOffsetChangedListener(TabLayout tabLayout, CollapsingToolbarLayout toolbarLayout, ToolbarStateHelper toolbarHelper) {
        initDefault();
        mTabLayout = tabLayout;
        mHelper = toolbarHelper;
        mToolbarLayout = toolbarLayout;
        ColorStateList colorStateList = mTabLayout.getTabTextColors();
        mSelectedColor = colorStateList.getColorForState(new int[]{android.R.attr.state_selected}, Color.parseColor("#ffffff"));//#ffffff
        mNormalColor = colorStateList.getDefaultColor();
    }

    @SuppressWarnings("unused")
    public void setCollapseTabBackgroundColor(int collapseTabBackgroundColor) {
        mCollapseTabBackgroundColor = collapseTabBackgroundColor;
    }

    @SuppressWarnings("unused")
    public void setCollapseTabSelectTextColor(int collapseTabSelectTextColor) {
        mCollapseTabSelectTextColor = collapseTabSelectTextColor;
    }

    private void initDefault() {
        mScrimAnimationDuration = DEFAULT_SCRIM_ANIMATION_DURATION;
        mCollapseTabBackgroundColor = Color.parseColor("#ffffff");//#3F51B5
        mCollapseTabNormalTextColor = Color.parseColor("#555555");
        mCollapseTabSelectTextColor = Color.parseColor("#FF4081");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mCollapsedHeight == 0) {
            mCollapsedHeight = appBarLayout.getTotalScrollRange();
        }
        setScrimsShown(isCollapsed(verticalOffset));
        mHelper.changeNavCollapsedState(getCollapsedState(verticalOffset));
        mHelper.toggleSearch(getCollapsedState(verticalOffset));
        mHelper.toggleProgressBar(getCollapsedState(verticalOffset));
    }

    private ToolBarState getCollapsedState(int offset) {
        return isCollapsed(offset) ? ToolBarState.CLOSED : ToolBarState.OPEN;
    }

    private boolean isCollapsed(int offset) {
        return -offset == mCollapsedHeight;
    }

    public void setScrimsShown(boolean shown) {
        setScrimsShown(shown, ViewCompat.isLaidOut(mToolbarLayout) && !mToolbarLayout.isInEditMode());
    }

    public void setScrimsShown(boolean shown, boolean animate) {
        if (mScrimsAreShown != shown) {
            if (animate) {
                animateScrim(shown ? 0xFF : 0x0);
            } else {
                setScrimAlpha(shown ? 0xFF : 0x0);
            }
            mScrimsAreShown = shown;
        }
    }

    private void animateScrim(int targetAlpha) {
        if (mScrimAnimator == null) {
            mScrimAnimator = new ValueAnimator();
            mScrimAnimator.setDuration(mScrimAnimationDuration);
            mScrimAnimator.setInterpolator(targetAlpha > mScrimAlpha ? new FastOutLinearInInterpolator() : new LinearOutSlowInInterpolator());
            mScrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    setScrimAlpha((int) animator.getAnimatedValue());
                }
            });
        } else if (mScrimAnimator.isRunning()) {
            mScrimAnimator.cancel();
        }
        mScrimAnimator.setIntValues(mScrimAlpha, targetAlpha);
        mScrimAnimator.start();
    }

    void setScrimAlpha(int alpha) {
        if (alpha != mScrimAlpha) {
            mScrimAlpha = alpha;
            updateLayout();
        }
    }

    private void updateLayout() {
        int color = ColorUtils.setAlphaComponent(mCollapseTabBackgroundColor, mScrimAlpha);
        mTabLayout.setBackgroundColor(color);
        float i = 1.f * mScrimAlpha / 255;
        mTabLayout.setTabTextColors(ColorUtils.blendARGB(mNormalColor, mCollapseTabNormalTextColor, i), ColorUtils.blendARGB(mSelectedColor, mCollapseTabSelectTextColor, i));
    }
}
