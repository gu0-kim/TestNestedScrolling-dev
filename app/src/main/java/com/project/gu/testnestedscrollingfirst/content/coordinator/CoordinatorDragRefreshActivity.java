package com.project.gu.testnestedscrollingfirst.content.coordinator;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayoutPullBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.DimenUtils;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;

import com.project.gu.testnestedscrollingfirst.R;


public class CoordinatorDragRefreshActivity extends AppCompatActivity {

    private ToolbarStateHelper mHelper;
    private TabFragmentPagerAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_drag_refresh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.nav_back_level_bg);
        
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHelper = new ToolbarStateHelper(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setScrimVisibleHeightTrigger(DimenUtils.getActionBarHeight(this) + DimenUtils.getStatusBarHeight(this) + 1);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        AppBarLayoutPullBehavior pullBehavior = (AppBarLayoutPullBehavior) ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        pullBehavior.setSpringOffsetCallback(new AppBarLayoutPullBehavior.SpringOffsetCallback() {
            @Override
            public void springCallback(int offset) {
                mHelper.changePbByPullOffset(offset);
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.tabs_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        mPageAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPageAdapter);
        AppBarOffsetChangedListener offsetChangedListener = new AppBarOffsetChangedListener(tabLayout, collapsingToolbarLayout, mHelper);
        appBarLayout.addOnOffsetChangedListener(offsetChangedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPageAdapter.clearFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //        getMenuInflater().inflate(R.menu.coordinator_toolbar_menu, menu);
        return false;
    }

    private boolean isLoading() {
        return mHelper.getState() == ToolbarStateHelper.ToolBarState.REFRESHING;
    }

    private boolean readyForLoading() {
        return mHelper.getState() == ToolbarStateHelper.ToolBarState.RELEASE_TO_REFRESH;
    }

    private void setStateLoading() {
        mHelper.setState(ToolbarStateHelper.ToolBarState.REFRESHING);
    }

    private void refreshRecyclerView() {
        for (int i = 0; i < 4; i++) {
            ((ItemFragment) mPageAdapter.getItem(i)).refresh();
        }
    }

    private int afterUpIndex;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (readyForLoading()) {
                    setStateLoading();
                    mHelper.startAnimation(this);
                    new AsyncLoadingTask().execute();
                    afterUpIndex = 0;
                }
                break;
            default:
                afterUpIndex++;
                break;
        }
        if (isLoading()) {
            /*
             * loading状态下up之后的所有事件，都会被屏蔽（不调用super.dispatchTouchEvent(ev)，子view都不会收到事件）
             */
            return afterUpIndex == 0 && super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }


    private class AsyncLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            mHelper.setState(ToolbarStateHelper.ToolBarState.OPEN);
            mHelper.toggleProgressBar(ToolbarStateHelper.ToolBarState.OPEN);
            mHelper.stopAnimation();
            refreshRecyclerView();
        }
    }
}
