package com.project.gu.testnestedscrollingfirst.content.scrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.project.gu.testnestedscrollingfirst.R;
public class MultiScrollView extends AppCompatActivity {

    MyScrollView datasv;
    ScrollView bgsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_scroll_view);
        datasv = (MyScrollView) findViewById(R.id.data_sv);
        bgsv = (ScrollView) findViewById(R.id.usrlayout_sv);
        datasv.setBgScrollView(bgsv);
    }
}
