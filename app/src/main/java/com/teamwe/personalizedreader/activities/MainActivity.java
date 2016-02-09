package com.teamwe.personalizedreader.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.mynews.FragmentNews;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.navigation.ViewPagerAdapter;
import com.teamwe.personalizedreader.navigation.WordsSlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_MACEDONIA = 0;
    private static final int PAGE_ECONOMY = 1;
    private static final int PAGE_WORLD = 2;
    private static final int PAGE_FOOTBALL = 3;
    private static final int PAGE_HEALTHY = 4;
    private static final int PAGE_TECH = 5;


    ViewPagerAdapter viewPagerAdapter;
    ViewPager mViewPager;
    WordsSlidingTabLayout slidingTabLayout;

    List<Fragment> fragments;
    List<String> titles;


    FragmentNews fragmentMacedonia;
    FragmentNews fragmentWorld;
    FragmentNews fragmentEconomy;
    FragmentNews fragmentFootball;
    FragmentNews fragmentHealthy;
    FragmentNews fragmentTech;

    Category[] categories;


    Category categoryMacedonia;
    Category categoryWorld;
    Category categoryEconomy;
    Category categoryFootball;
    Category categoryHealthy;
    Category categoryTech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        categoryMacedonia = new Category("MAKEDONIJA","Македонија");
        categoryEconomy = new Category("EKONOMIJA", "Економија");
        categoryWorld = new Category("SVET", "Свет");
        categoryFootball = new Category("FUDBAL", "Фудбал");
        categoryHealthy = new Category("ZDRAVJE", "Здравје");
        categoryTech = new Category("TEHNOLOGIJA","Технологија");

        this.categories = new Category[]{categoryMacedonia, categoryEconomy, categoryWorld, categoryFootball,
                categoryHealthy, categoryTech};

        // getting the pager
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(5);

        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String> ();

        // read the user specifications for categories from shared preferences

        fragmentMacedonia = (FragmentNews)getSupportFragmentManager()
                .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_MACEDONIA));
        if (fragmentMacedonia == null)
            fragmentMacedonia = new FragmentNews();
        fragmentMacedonia.setCategory(categoryMacedonia);

        fragmentWorld = (FragmentNews)getSupportFragmentManager()
                .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_WORLD));
        if (fragmentWorld == null)
            fragmentWorld = new FragmentNews();
        fragmentWorld.setCategory(categoryWorld);

        fragmentEconomy = (FragmentNews)getSupportFragmentManager()
                .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_ECONOMY));
        if (fragmentEconomy == null)
            fragmentEconomy = new FragmentNews();
        fragmentEconomy.setCategory(categoryEconomy);

        fragmentFootball = (FragmentNews)getSupportFragmentManager()
                .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_FOOTBALL));
        if (fragmentFootball == null)
            fragmentFootball = new FragmentNews();
        fragmentFootball.setCategory(categoryFootball);

        fragmentHealthy = (FragmentNews)getSupportFragmentManager()
                .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_HEALTHY));
        if(fragmentHealthy == null)
            fragmentHealthy = new FragmentNews();
        fragmentHealthy.setCategory(categoryHealthy);

        fragmentTech = (FragmentNews)getSupportFragmentManager()
                .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_TECH));
        if (fragmentTech == null)
            fragmentTech = new FragmentNews();
        fragmentTech.setCategory(categoryTech);


        fragments.add(fragmentMacedonia);
        fragments.add(fragmentEconomy);
        fragments.add(fragmentWorld);
        fragments.add(fragmentFootball);
        fragments.add(fragmentHealthy);
        fragments.add(fragmentTech);

        for (Category cat : categories){
            titles.add(cat.getTitle());
        }

        //setting the adapter for fragments
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
        //setting view pager
        mViewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (WordsSlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(mViewPager);

        mViewPager.setCurrentItem(0, true);
    }
}
