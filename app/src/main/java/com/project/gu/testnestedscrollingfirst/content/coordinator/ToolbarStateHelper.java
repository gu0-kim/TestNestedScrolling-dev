package com.project.gu.testnestedscrollingfirst.content.coordinator;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.project.gu.testnestedscrollingfirst.R;

/**
 * Created by gu on 2017/5/7.
 */

public class ToolbarStateHelper {
    private Toolbar mToolbar;
    private ImageView searchImg, moreImg;
    private Animation mRotateAnimation;

    public void setState(ToolBarState state) {
        mState = state;
    }

    public ToolBarState getState() {
        return mState;
    }

    private ToolBarState mState;

    public enum ToolBarState {
        OPEN, CLOSED, PULL, RELEASE_TO_REFRESH, REFRESHING
    }

    private static final int NEED_ROTATE_DY = 60;

    public ToolbarStateHelper(Toolbar toolbar) {
        mToolbar = toolbar;
        mState = ToolBarState.OPEN;
        searchImg = (ImageView) toolbar.findViewById(R.id.searchImg);
        moreImg = (ImageView) toolbar.findViewById(R.id.moreImg);
    }

    public void changeNavCollapsedState(ToolBarState state) {
        mToolbar.getNavigationIcon().setLevel(state == ToolBarState.CLOSED ? 1 : 0);
    }

    public void toggleSearch(ToolBarState state) {
        if (mState == ToolBarState.OPEN || mState == ToolBarState.CLOSED) {
            searchImg.getDrawable().setLevel(state == ToolBarState.CLOSED ? 1 : 0);
            mState = state;
        }
    }

    public void toggleProgressBar(ToolBarState state) {
        if (mState == ToolBarState.OPEN || mState == ToolBarState.CLOSED) {
            moreImg.getDrawable().setLevel(state == ToolBarState.CLOSED ? 2 : 0);
            rotationImageView(moreImg, 0);
            mState = state;
        }
    }

    public void changePbByPullOffset(int offset) {
        if (mState == ToolBarState.REFRESHING)
            return;
        if (offset <= 0) {
            moreImg.getDrawable().setLevel(0);
            mState = ToolBarState.OPEN;
        } else if (offset > 0 && offset < NEED_ROTATE_DY) {
            moreImg.getDrawable().setLevel(1);
            //            rotationImageView(moreImg, 0);
            mState = ToolBarState.PULL;
        } else if (offset >= NEED_ROTATE_DY) {
            moreImg.getDrawable().setLevel(1);
            rotateProgressBarByOffset(offset);
            mState = ToolBarState.RELEASE_TO_REFRESH;
        }
    }

    private void rotateProgressBarByOffset(int offset) {
        rotationImageView(moreImg, calDegree(offset));
    }


    private int calDegree(int offset) {
        return (int) ((NEED_ROTATE_DY - (float) offset) / NEED_ROTATE_DY * 360) * 2;
    }

    private void rotationImageView(ImageView pb, int degree) {
        pb.setPivotX(pb.getWidth() / 2);
        pb.setPivotY(pb.getHeight() / 2);
        pb.setRotation(degree);
    }

    public void startAnimation(Context context) {
        if (mRotateAnimation == null) {
            mRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.pb_rotate_anim);
        }
        moreImg.startAnimation(mRotateAnimation);
    }

    public void stopAnimation() {
        moreImg.clearAnimation();
    }
}
