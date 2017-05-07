package com.project.gu.testnestedscrollingfirst.content.coordinator;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayoutPullBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.DimenUtils;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.project.gu.testnestedscrollingfirst.R;

public class CoordinatorDragRefreshActivity extends AppCompatActivity {

    private ToolbarStateHelper mHelper;

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
        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
        AppBarOffsetChangedListener offsetChangedListener = new AppBarOffsetChangedListener(tabLayout, collapsingToolbarLayout, mHelper);
        appBarLayout.addOnOffsetChangedListener(offsetChangedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.coordinator_toolbar_menu, menu);
        return true;
    }
}