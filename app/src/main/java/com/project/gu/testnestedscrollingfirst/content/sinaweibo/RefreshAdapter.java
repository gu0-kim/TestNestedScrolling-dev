package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.gu.testnestedscrollingfirst.R;

import java.util.List;

/**
 * Created by gu on 2017/5/1.
 */

public class RefreshAdapter extends RecyclerView.Adapter {
    private List<String> list;
    private Context mContext;
    private RefreshRecyclerView rv;
    private static final int HEADERCOUNT = 2;
    private static final int HEADERTYPE = 0;
    private static final int HEADERIMGTYPE = 2;
    private static final int ITEMTYPE = 1;

    public RefreshAdapter(Context context, List<String> list, RefreshRecyclerView rv) {
        this.list = list;
        this.mContext = context;
        this.rv = rv;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADERTYPE) {
            return new Holder(rv.getRefreshHeader());
        } else if (viewType == ITEMTYPE) {
            return new Holder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false));
        } else if (viewType == HEADERIMGTYPE) {
            return new Holder(LayoutInflater.from(mContext).inflate(R.layout.userinfo_header_layout, parent, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADERTYPE : position == 1 ? HEADERIMGTYPE : ITEMTYPE;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEMTYPE) {
            ((TextView) holder.itemView).setText(list.get(position - HEADERCOUNT));
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return HEADERCOUNT;
        return list.size() + HEADERCOUNT;
    }

    private class Holder extends ViewHolder {

        private Holder(View itemView) {
            super(itemView);
        }
    }
}
