package com.teamwe.personalizedreader.mynews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Petre on 1/24/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList;
    List<String> fragmentTitles;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> fragmentTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.fragmentTitles = fragmentTitles;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = fragmentList.get(i);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }


    public static String getFragmentTag(int pos){
        return "android:switcher:"+R.id.pager+":"+pos;
    }
}