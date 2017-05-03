package com.project.gu.testnestedscrollingfirst.content.sinaweibo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.gu.testnestedscrollingfirst.R;

import java.util.ArrayList;
import java.util.List;

import static com.project.gu.testnestedscrollingfirst.log.LogUtil.log;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int index;
    private RefreshRecyclerView rv;
    private static final int HEADEROUT = 1;
    List<String> list;
    private ViewGroup topView;
    private ViewPager vp;
    private int headerHeight;
    boolean moveTopView = true;

    public void needScrollBy(int dy) {
        rv.scrollBy(0, dy);
    }

    public PageFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        topView = ((UserInfoActivity) context).getTopView();
        vp = ((UserInfoActivity) context).getViewPager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        topView = null;
        vp = null;
    }

    public static PageFragment newInstance(int num) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        headerHeight = getResources().getDimensionPixelSize(R.dimen.drag_header_height);
        rv = (RefreshRecyclerView) inflater.inflate(R.layout.fragment_pager, container, false);
        list = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            list.add(String.valueOf(i));
        }
        rv.setAdapter(new RefreshAdapter(getContext(), list, rv));
        return rv;
    }

    boolean isDragging;

    public int getTopViewBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rv.getLayoutManager();
        View firstVisibItem = rv.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (firstItemPosition == 1) {
            return layoutManager.getDecoratedBottom(firstVisibItem);
        } else if (firstItemPosition == 0) {
            return layoutManager.getDecoratedBottom(firstVisibItem) + headerHeight;
        } else {
            return 0;
        }
    }

    private void notifyScrollBy(int dy) {
        FragmentPagerAdapter adapter = (FragmentPagerAdapter) vp.getAdapter();
        for (int i = 0; i < 4; i++) {
            if (i != index) {
                ((PageFragment) adapter.getItem(i)).needScrollBy(dy);
            }
        }
    }

    class Radapter extends RecyclerView.Adapter {
        private static final int HEADER = 0;
        private static final int ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                return new HeaderHolder(LayoutInflater.from(getContext()).inflate(R.layout.userinfo_header_layout, parent, false));
            } else {
                return new ItemHolder(LayoutInflater.from(getContext()).inflate(R.layout.rv_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position != 0) {
                TextView tv = (TextView) holder.itemView;
                tv.setText(list.get(position - 1));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return HEADER;
            return ITEM;
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        ItemHolder(View itemView) {
            super(itemView);
        }
    }

}
