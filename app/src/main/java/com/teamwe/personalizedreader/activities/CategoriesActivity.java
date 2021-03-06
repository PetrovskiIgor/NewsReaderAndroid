package com.teamwe.personalizedreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.adapters.CategoriesAdapter;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.CategoriesTask;
import com.teamwe.personalizedreader.tasks.OnCategoriesHere;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import com.facebook.FacebookSdk;


// !!! WARNING !!!
// NOT OVER:
// We need to update GlobalInfo.WANTED_CATEOGIRES and take into account that this activity may be called from the navigation drawer!

public class CategoriesActivity extends AppCompatActivity {


    public static String TAG = "CategoriesActivity";

    private ListView listViewCategories;
    private RelativeLayout layoutNext;
    private CategoriesAdapter adapter;
    private boolean callFromMainActivity = false;

    private SwipeRefreshLayout swipeView;

    private Toolbar toolbar;


    //private boolean alreadySetHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
        }catch(Exception e){

        }




        setContentView(R.layout.activity_categories);


        // check whether the user has set the preferences
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPersonalized = sharedPreferences.getBoolean(GlobalInfo.PERSONALIZATION_STR, false);

        Intent intent = getIntent();
        callFromMainActivity = intent.getBooleanExtra(GlobalInfo.CALL_FROM_MAIN_ACTIVITY, false);

        // if yes, move to the sources activity, and then to the main activity
        if (!callFromMainActivity && isPersonalized) {
            moveToNextActivity();
            this.finish();
        }

        colorAboveTheToolbar();

        toolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_categories_activity));
        //alreadySetHeaderView = false;


        configureListView();
        configureSwipeView();
        configureLayoutNext();


        loadCategories();


    }

    private void configureLayoutNext() {

        layoutNext = (RelativeLayout) toolbar.findViewById(R.id.layoutNext);
        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(null == adapter) {
                    showToastForGoingTooEarly();
                    return;
                }

                if (countSelectedCategories() >= 1) {
                    putSelectedCategoriesInSharedPreferences();
                    if(callFromMainActivity) {
                        exitActivity();
                    }else {
                        moveToNextActivity();
                    }
                } else {
                    showToastForBadSpecification();
                }
            }
        });
    }
    private void exitActivity() {
        Toast.makeText(this, "Успешно подесување на темите.", Toast.LENGTH_SHORT).show();
        this.finish();
    }
    private void configureListView() {
        listViewCategories = (ListView) findViewById(R.id.listCategories);


        this.listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                Category cat = adapter.getData().get(position);
                cat.setCheckedState(!cat.getCheckedState());
                CheckBox cb = (CheckBox)view.findViewById(R.id.cbCategory);
                cb.setChecked(cat.getCheckedState());

                // for just in case
                adapter.getData().get(position).setCheckedState(cat.getCheckedState());
                */

            }
        });
    }

    private void showToastForBadSpecification() {
        Toast.makeText(this, getResources().getString(R.string.warning_at_least_one_category), Toast.LENGTH_LONG).show();
    }

    private void showToastForGoingTooEarly() {
        Toast.makeText(this, getResources().getString(R.string.going_too_early_categories), Toast.LENGTH_SHORT).show();
    }

    private int countSelectedCategories() {
        int toRet = 0;

        List<Category> categories = adapter.getCategories();
        for (Category cat : categories) {
            if (cat.getCheckedState()) {
                toRet++;
            }
        }

        return toRet;
    }


    private void putSelectedCategoriesInSharedPreferences() {
        String func_tag = "putSelectedCategoriesInSharedPreferences(): ";

        if(null != adapter) {

            List<Category> categories = adapter.getCategories();
            SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            List<Category> selectedCategories = new ArrayList<Category>();
            List<Category> unselectedCategories = new ArrayList<Category>();


            for (Category cat : categories) {
                if (cat.getCheckedState()) {
                    selectedCategories.add(cat);
                } else {
                    unselectedCategories.add(cat);
                }
            }

            for(Category selectedCategory : selectedCategories) {
                Log.i(TAG, String.format("%s%s: true",func_tag,selectedCategory.toString()));
            }

            Gson gson = new Gson();
            Type typeToken = new TypeToken<List<Category>>() {}.getType();

            String gsonFormatList = gson.toJson(selectedCategories, typeToken);

            editor.putString(GlobalInfo.SELECTED_CATEGORIES, gsonFormatList);

            gsonFormatList = gson.toJson(unselectedCategories, typeToken);
            editor.putString(GlobalInfo.UNSELECTED_CATEGORIES, gsonFormatList);

            editor.commit();
        }
    }

    public void moveToNextActivity() {
        Intent intent = new Intent(this, SourcesActivity.class);
        intent.putExtra(GlobalInfo.CALL_FROM_MAIN_ACTIVITY, callFromMainActivity);
        startActivity(intent);
        this.finish();
    }

    private void configureSwipeView() {

        String func_tag = "configureSwipeView(): ";
        Log.i(TAG, func_tag + "beginning");

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeLayoutCategories);

        swipeView.setColorSchemeResources(R.color.primaryColor);


        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.i(TAG, "onRefresh called");
                swipeView.setRefreshing(true);
                loadCategories();
            }
        });

        listViewCategories.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.i(TAG, "onScroll: firstVisibleItem: " + firstVisibleItem + " visibleItemCount: " + visibleItemCount + " totalItemCount: " + totalItemCount);
                if (0 == firstVisibleItem) {
                    swipeView.setEnabled(true);
                } else {
                    swipeView.setEnabled(false);
                }
            }
        });


        Log.i(TAG, func_tag + "exit");
    }

    private void loadCategories() {

        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });


        final Activity act = this;


        CategoriesTask task = new CategoriesTask(new OnCategoriesHere() {
            @Override
            public void onTaskCompleted(List<Category> list) {

                showCategories(list);

            }
        });

        task.execute();

    }


    // show the categories (with a header) in the listview
    private void showCategories(List<Category> data) {
        String func_tag = "showCategories(): ";
        Log.i(TAG, func_tag + "callFromMainActivity? " + callFromMainActivity);

        if (swipeView.isRefreshing()) {
            Log.i(TAG, func_tag + "The swipe view is refreshing, so let's post a runnable to stop refreshing");

            swipeView.post(new Runnable() {
                @Override
                public void run() {
                    swipeView.setRefreshing(false);
                }
            });
        } else {
            Log.i(TAG, func_tag + "Strangely, the swipe view is not refreshing.");
        }

        if(null == data) {
            Toast.makeText(this, "Обидете се повторно.", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);

        String gsonList =  preferences.getString(GlobalInfo.SELECTED_CATEGORIES, "");

        if (gsonList.length() > 0) {

            Gson gson = new Gson();

            Type typeToken = new TypeToken<List<Category>>() {}.getType();

            List<Category> selectedCategories = gson.fromJson(gsonList, typeToken);

            for (Category s : data) {
                if (!callFromMainActivity) {
                    s.setCheckedState(true);
                } else {

                    boolean isSelected = false;

                    for(Category selected : selectedCategories) {
                        if (selected.getId() == s.getId()) {
                            isSelected = true;
                            break;
                        }
                    }

                    s.setCheckedState(isSelected);
                }
            }

        } else {
            for(Category c : data) {
                c.setCheckedState(true);
            }
        }



        adapter = new CategoriesAdapter(this, 0, data);
        listViewCategories.setAdapter(adapter);

        Log.i(TAG, func_tag + "Connected the adapter with the listview.");


    }


    private void colorAboveTheToolbar() {

        if(Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryColorDark));
        }

    }
}
