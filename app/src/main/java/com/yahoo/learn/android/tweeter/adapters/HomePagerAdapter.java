package com.yahoo.learn.android.tweeter.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ankurj on 2/14/2015.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private int mPageCount;
    private String mTabTitles[];
//    private String mTabTitles[] = new String[] { "Timeline", "Mentions", "User" };
    private Fragment[] mFragments;

    public HomePagerAdapter(FragmentManager fm, Fragment[] fragments, String [] tabTitles) {
        super(fm);

        assert (tabTitles.length == fragments.length);
        mTabTitles = tabTitles;
        mFragments = fragments;
        mPageCount = tabTitles.length;
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTabTitles[position];
    }
}