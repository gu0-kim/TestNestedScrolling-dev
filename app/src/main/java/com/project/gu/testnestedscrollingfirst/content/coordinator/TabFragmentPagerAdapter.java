package com.project.gu.testnestedscrollingfirst.content.coordinator;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    Map<Integer, ItemFragment> map;
    String mStrings[] = {"    主页    ", "    微博    ", "    相册    "};

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        map = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        if (map.containsKey(position)) {
            return map.get(position);
        } else {
            ItemFragment fragment = ItemFragment.newInstance();
            map.put(position, fragment);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStrings[position];
    }

    public void clearFragment() {
        if (!map.isEmpty()) {
            map.clear();
        }
    }
}
