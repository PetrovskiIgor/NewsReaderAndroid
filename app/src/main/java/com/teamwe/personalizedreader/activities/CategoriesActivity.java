package com.teamwe.personalizedreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.teamwe.personalizedreader.adapters.CategoriesAdapter;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.CategoriesTask;
import com.teamwe.personalizedreader.tasks.OnCategoriesHere;


import java.util.ArrayList;
import java.util.List;


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
        setContentView(R.layout.activity_categories);


        // check whether the user has set the preferences
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPersonalized = sharedPreferences.getBoolean(GlobalInfo.PERSONALIZATION_STR, false);

        Intent intent = getIntent();
        callFromMainActivity = intent.getBooleanExtra("callFromMainActivity", false);

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

        Log.i(TAG, "Preparing to configure the list view..");
        configureListView();
        Log.i(TAG, "Configured the list view.");


        Log.i(TAG, "Preparing to configure the swipe view..");
        configureSwipeView();

        Log.i(TAG, "Preparing to configure the layout next..");
        configureLayoutNext();


        Log.i(TAG, "Preparing to load the categories.");
        loadCategories();


    }

    private void configureLayoutNext() {

        layoutNext = (RelativeLayout) toolbar.findViewById(R.id.layoutNext);
        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countCategoriesToRead() >= 1) {
                    putSpecificationInSharedPreferences();
                    moveToNextActivity();
                } else {
                    showToastForBadSpecification();
                }
            }
        });
    }

    private void configureListView() {
        listViewCategories = (ListView) findViewById(R.id.listCategories);


        this.listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // set checked
            }
        });
    }

    private void showToastForBadSpecification() {
        Toast.makeText(this, getResources().getString(R.string.warning_at_least_one_category), Toast.LENGTH_LONG).show();
    }

    private int countCategoriesToRead() {
        int toRet = 0;

        List<Boolean> specification = adapter.getSpecification();
        for (Boolean b : specification) {
            if (b) {
                toRet++;
            }
        }

        return toRet;
    }

    private List<Boolean> getCurrentSpecification() {
        List<Boolean> toRet = new ArrayList<Boolean>();

        SharedPreferences pref = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);
        List<Category> categories = GlobalInfo.CATEGORIES;

        for (Category cat : categories) {
            toRet.add(pref.getBoolean(cat.getName(), true));
        }

        return toRet;
    }

    private void putSpecificationInSharedPreferences() {
        String func_tag = "putSpecificationInSharedPreferences(): ";

        List<Boolean> specification = adapter.getSpecification();

        SharedPreferences pref = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        List<Category> categories = adapter.getData();

        int i=0;
        for (Category category : categories) {

            // maybe we should put the ID here, instead of the name
            editor.putBoolean(category.getName(), specification.get(i));

            Log.i(TAG, String.format("%s%s: %b", func_tag, category.getName(), specification.get(i)));
            i++;
        }

        editor.commit();
    }

    public void moveToNextActivity() {
        Intent intent = new Intent(this, SourcesActivity.class);
        intent.putExtra("callFromMainActivity", callFromMainActivity);
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

                if(null != list && list.size() > 0) {
                    storeCategories(list);
                }

            }
        });

        task.execute();

    }

    // after we've got the categories from the server, store them in SharedPreferences for just in case
    private void storeCategories(List<Category> categories) {

    }

    // show the categories (with a header) in the listview
    private void showCategories(List<Category> list) {
        String func_tag = "showCategories(): ";

        // we should consider whether we need to add a header as a description
        // maybe a snicker ?
        /*if(!alreadySetHeaderView) {
            View listHeader = LayoutInflater.from(this).inflate(R.layout.categories_list_header, null);
            listViewCategories.addHeaderView(listHeader);
            alreadySetHeaderView = true;
        }*/

        adapter = new CategoriesAdapter(this, 0, list);
        listViewCategories.setAdapter(adapter);

        Log.i(TAG, func_tag + "Connected the adapter with the listview.");
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

    }


    private void colorAboveTheToolbar() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.primaryColorDark));
    }
}
