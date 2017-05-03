package com.project.gu.testnestedscrollingfirst.content.viewpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.project.gu.testnestedscrollingfirst.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerScrollActivity extends AppCompatActivity {

    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_main);
        vp = (ViewPager) findViewById(R.id.vp);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(String.valueOf(i));
        }
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), list));
    }
}
