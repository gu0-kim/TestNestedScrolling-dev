package com.project.gu.testnestedscrollingfirst.content.scrollview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.gu.testnestedscrollingfirst.R;
import com.project.gu.testnestedscrollingfirst.content.sinaweibo.RefreshRecyclerView;

import java.util.List;

import static com.project.gu.testnestedscrollingfirst.R.id.rv;

/**
 * Created by gu on 2017/5/1.
 */

public class RefreshAdapter extends RecyclerView.Adapter {
    private List<String> list;
    private Context mContext;

    public RefreshAdapter(Context context, List<String> list) {
        this.list = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    private class Holder extends ViewHolder {

        private Holder(View itemView) {
            super(itemView);
        }
    }
}
