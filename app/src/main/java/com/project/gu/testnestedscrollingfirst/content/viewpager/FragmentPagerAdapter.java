package com.project.gu.testnestedscrollingfirst.content.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by gu on 2017/4/18.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    List<String> data;

    public FragmentPagerAdapter(FragmentManager fm, List<String> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance("", data.get(position));
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
