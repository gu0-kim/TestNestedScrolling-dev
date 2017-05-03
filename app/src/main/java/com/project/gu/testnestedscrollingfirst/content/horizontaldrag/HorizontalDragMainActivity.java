package com.project.gu.testnestedscrollingfirst.content.horizontaldrag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.gu.testnestedscrollingfirst.R;

import java.util.ArrayList;
import java.util.List;

public class HorizontalDragMainActivity extends AppCompatActivity {
    RecyclerView rv_fst, rv_sec, rv_thd;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_drag_main);

        rv_fst = (RecyclerView) findViewById(R.id.rv_fst);
        rv_sec = (RecyclerView) findViewById(R.id.rv_sec);
        rv_thd = (RecyclerView) findViewById(R.id.rv_thd);
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i));
        }
        rv_fst.setAdapter(new Radapter());
        rv_fst.setLayoutManager(new LinearLayoutManager(this));
        rv_sec.setAdapter(new Radapter());
        rv_sec.setLayoutManager(new LinearLayoutManager(this));
        rv_thd.setAdapter(new Radapter());
        rv_thd.setLayoutManager(new LinearLayoutManager(this));
    }

    class Radapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(HorizontalDragMainActivity.this).inflate(R.layout.rv_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView tv = (TextView) holder.itemView;
            tv.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
