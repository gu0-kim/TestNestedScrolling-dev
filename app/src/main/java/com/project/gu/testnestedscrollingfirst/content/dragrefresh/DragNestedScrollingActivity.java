package com.project.gu.testnestedscrollingfirst.content.dragrefresh;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.project.gu.testnestedscrollingfirst.R;
import com.project.gu.testnestedscrollingfirst.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DragNestedScrollingActivity extends AppCompatActivity implements DragRefreshNestedScrollingLayout.IRefresh {

    ViewPager vp;
    ImageView navBackImg, userheaderImg;
    DragRefreshNestedScrollingLayout dragview;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draglayout_activity);
        handler = new Handler();
        dragview = (DragRefreshNestedScrollingLayout) findViewById(R.id.dragview);
        dragview.setRefreshCallBack(this);
        navBackImg = (ImageView) findViewById(R.id.navBackImg);
        navBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        userheaderImg = (ImageView) findViewById(R.id.userheaderImg);
        userheaderImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.log("userheaderImg click!");
            }
        });
        vp = (ViewPager) findViewById(R.id.vp);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(String.valueOf(i));
        }
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), list));
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dragview.refreshComplete();
            }
        }, 3000);
    }
}
