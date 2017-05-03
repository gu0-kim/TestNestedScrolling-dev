package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.gu.testnestedscrollingfirst.R;

import java.util.ArrayList;
import java.util.List;

public class PullRefreshActivity extends AppCompatActivity {

    RefreshRecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh);
        rv = (RefreshRecyclerView) findViewById(R.id.rv);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }
//        rv.setLayoutManager(new LinearLayoutManager(this));
        RefreshAdapter adapter = new RefreshAdapter(this, list, rv);
        rv.setAdapter(adapter);
    }
}
