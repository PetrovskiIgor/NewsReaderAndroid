package com.teamwe.personalizedreader.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.mynews.FragmentNews;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
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

    private static final int SETTINGS_REQUEST_CODE = 7;


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

    List<Category> categories;

    Category categoryMacedonia;
    Category categoryWorld;
    Category categoryEconomy;
    Category categoryFootball;
    Category categoryHealthy;
    Category categoryTech;

    ImageView imgSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgSettings = (ImageView)findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adjustPersonalizationSettings();
            }
        });

        categoryMacedonia = GlobalInfo.CATEGORIES.get(0);
        categoryEconomy = GlobalInfo.CATEGORIES.get(1);
        categoryWorld = GlobalInfo.CATEGORIES.get(2);
        categoryFootball = GlobalInfo.CATEGORIES.get(3);
        categoryHealthy = GlobalInfo.CATEGORIES.get(4);
        categoryTech = GlobalInfo.CATEGORIES.get(5);

        this.categories = GlobalInfo.CATEGORIES;

        // getting the pager
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(5);

        fragments = new ArrayList<Fragment>();
        titles = new ArrayList<String> ();

        // read the user specifications for categories from shared preferences
        SharedPreferences pref = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);

        if (pref.getBoolean(categoryMacedonia.getName(),false)) {
            fragmentMacedonia = (FragmentNews) getSupportFragmentManager()
                    .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_MACEDONIA));
            if (fragmentMacedonia == null)
                fragmentMacedonia = new FragmentNews();
            fragmentMacedonia.setCategory(categoryMacedonia);

            fragments.add(fragmentMacedonia);
            titles.add(categoryMacedonia.getTitle());
        }

        if (pref.getBoolean(categoryWorld.getName(),false)) {
            fragmentWorld = (FragmentNews) getSupportFragmentManager()
                    .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_WORLD));
            if (fragmentWorld == null)
                fragmentWorld = new FragmentNews();
            fragmentWorld.setCategory(categoryWorld);

            fragments.add(fragmentWorld);
            titles.add(categoryWorld.getTitle());
        }

        if (pref.getBoolean(categoryEconomy.getName(),false)) {
            fragmentEconomy = (FragmentNews) getSupportFragmentManager()
                    .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_ECONOMY));
            if (fragmentEconomy == null)
                fragmentEconomy = new FragmentNews();
            fragmentEconomy.setCategory(categoryEconomy);

            fragments.add(fragmentEconomy);
            titles.add(categoryEconomy.getTitle());
        }

        if (pref.getBoolean(categoryFootball.getName(),false)) {
            fragmentFootball = (FragmentNews) getSupportFragmentManager()
                    .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_FOOTBALL));
            if (fragmentFootball == null)
                fragmentFootball = new FragmentNews();
            fragmentFootball.setCategory(categoryFootball);

            fragments.add(fragmentFootball);
            titles.add(categoryFootball.getTitle());
        }

        if (pref.getBoolean(categoryHealthy.getName(),false)) {
            fragmentHealthy = (FragmentNews) getSupportFragmentManager()
                    .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_HEALTHY));
            if (fragmentHealthy == null)
                fragmentHealthy = new FragmentNews();
            fragmentHealthy.setCategory(categoryHealthy);

            fragments.add(fragmentHealthy);
            titles.add(categoryHealthy.getTitle());
        }

        if (pref.getBoolean(categoryTech.getName(),false)) {
            fragmentTech = (FragmentNews) getSupportFragmentManager()
                    .findFragmentByTag(ViewPagerAdapter.getFragmentTag(PAGE_TECH));
            if (fragmentTech == null)
                fragmentTech = new FragmentNews();
            fragmentTech.setCategory(categoryTech);

            fragments.add(fragmentTech);
            titles.add(categoryTech.getTitle());
        }




        //setting the adapter for fragments
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
        //setting view pager
        mViewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout = (WordsSlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(mViewPager);

        mViewPager.setCurrentItem(0, true);
    }


    private void adjustPersonalizationSettings() {

        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("callFromMainActivity", true);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_REQUEST_CODE) {
            Toast.makeText(this, "Settings adjusted!", Toast.LENGTH_SHORT).show();
        }
    }
}
