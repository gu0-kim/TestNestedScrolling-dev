package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gu on 2017/4/18.
 */

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    int num;
    Map<Integer, Fragment> maps;

    public FragmentPagerAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
        maps = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = maps.get(position);
        if (f != null)
            return f;
        f = PageFragment.newInstance(position);
        maps.put(position, f);
        return f;
    }

    @Override
    public int getCount() {
        return num;
    }

    public void clear() {
        if (maps != null) {
            maps.clear();
        }
    }

}
