package com.project.gu.testnestedscrollingfirst.content.sinaweibo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.project.gu.testnestedscrollingfirst.R;

import static com.project.gu.testnestedscrollingfirst.log.LogUtil.log;

public class UserInfoActivity extends AppCompatActivity {

    ViewPager vp;
    RelativeLayout frameTop;
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        frameTop = (RelativeLayout) findViewById(R.id.frameTop);
        vp = (ViewPager) findViewById(R.id.vp);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager(), 4);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(4);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean startScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PageFragment curf = (PageFragment) adapter.getItem(vp.getCurrentItem());
                final int bottom = curf.getTopViewBottom();
                if (bottom > 0) {
                    getTopView().setVisibility(View.VISIBLE);
                    getTopView().offsetTopAndBottom(bottom - getTopView().getBottom());
                }

                if (startScroll) {
                    final int lPos = vp.getCurrentItem() - 1;
                    final int rPos = vp.getCurrentItem() + 1;
                    if (lPos >= 0) {
                        PageFragment lf = (PageFragment) adapter.getItem(lPos);
                        scrollNeighbours(lf, curf);
                    }
                    if (rPos < 4) {
                        PageFragment rf = (PageFragment) adapter.getItem(rPos);
                        scrollNeighbours(rf, curf);
                    }
                    startScroll = false;
                }

                /*
                滚动结束时隐藏顶部
                 */
                if (positionOffset == 0.0f) {
                    getTopView().setVisibility(View.INVISIBLE);
                    startScroll = true;
                }
            }

            @Override
            public void onPageSelected(int position) {
                getTopView().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void scrollNeighbours(PageFragment neighboursFragment, PageFragment selfFragment) {
        int neighboursBottom = neighboursFragment.getTopViewBottom();
        int selfBottom = selfFragment.getTopViewBottom();
        int dy = neighboursBottom - selfBottom;
        log("----needScrollBy dy= " + dy);
        neighboursFragment.needScrollBy(dy);
    }

    public ViewGroup getTopView() {
        return frameTop;
    }

    public ViewPager getViewPager() {
        return vp;
    }

}
