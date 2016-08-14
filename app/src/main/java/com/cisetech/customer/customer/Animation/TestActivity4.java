package com.cisetech.customer.customer.Animation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cisetech.customer.customer.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity4 extends AppCompatActivity implements ViewPager.OnPageChangeListener, SimpleViewPagerIndicator.OnIndicatorClickListener {
    private SimpleViewPagerIndicator mIndicator;
    private ViewPager mPager;
    private List<Fragment> mViews=new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        mIndicator= (SimpleViewPagerIndicator) findViewById(R.id.id_stickylayout_nav);
        mPager= (ViewPager) findViewById(R.id.id_stickylayout_viewpager);
        initData();
    }
    private String titles[]={"简介","评价","相关"};
    private void initData() {
        mIndicator.setTitles(titles);
        for (int i = 0; i < 3; i++) {
            TabFragment frag=TabFragment.newInstance(titles[i]);
            Bundle bundle=new Bundle();
            bundle.putString(TabFragment.TITLE,titles[i]);
            frag.setArguments(bundle);
            mViews.add(frag);
        }
        mPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mViews.get(position);
            }

            @Override
            public int getCount() {
                return mViews.size();
            }
        });
        mPager.setOnPageChangeListener(this);
        mIndicator.setOnIndicatorClickListener(this);
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, int positionOffsetPixels) {
        mIndicator.setOffset(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void indicatorClick(View view, final int position) {
        mPager.setCurrentItem(position,false);
    }
}
