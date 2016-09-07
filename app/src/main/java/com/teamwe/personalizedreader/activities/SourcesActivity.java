package com.teamwe.personalizedreader.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.adapters.SourcesAdapter;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.OnSourcesHere;
import com.teamwe.personalizedreader.tasks.SourcesTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SourcesActivity extends AppCompatActivity {

    private static final String TAG = "SourcesActivity";

    private ListView listViewSources;
    private SourcesAdapter adapter;
    private boolean callFromMainActivity = false;

    private SwipeRefreshLayout swipeView;

    private Toolbar toolbar;


    // the layout, when clicked, brings us to the main activity
    private RelativeLayout layoutNext;
    private boolean isPersonalized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);


        Intent intent = getIntent();
        callFromMainActivity = intent.getBooleanExtra(GlobalInfo.CALL_FROM_MAIN_ACTIVITY, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isPersonalized = sharedPreferences.getBoolean(GlobalInfo.PERSONALIZATION_STR, false);


        if (!callFromMainActivity && isPersonalized) {
            moveToNextActivity();
            this.finish();
        }

        // color the notification bar with a color slightly darker than the primary color
        colorAboveTheToolbar();

        toolbar = (Toolbar) findViewById(R.id.toolbarSources);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_sources_activity));

        configureListView();

        configureSwipeView();

        configureLayoutNext();

        loadSources();

    }

    private void configureListView() {
        listViewSources = (ListView) findViewById(R.id.listSources);
        listViewSources.setOnItemClickListener(onItemClickListener);
        listViewSources.setDivider(null);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SourcesAdapter.ViewHolder viewHolder = (SourcesAdapter.ViewHolder) view.getTag();
            viewHolder.getCheckBox().setChecked(!viewHolder.getCheckBox().isChecked());
        }
    };

    private void configureSwipeView() {

        String func_tag = "configureSwipeView(): ";
        Log.i(TAG, func_tag + "beginning");

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeLayoutSources);

        swipeView.setColorSchemeResources(R.color.primaryColor);


        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Log.i(TAG, "onRefresh called");
                swipeView.setRefreshing(true);
                loadSources();
            }
        });

        listViewSources.setOnScrollListener(new ListView.OnScrollListener() {

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



    private void configureLayoutNext() {

        layoutNext = (RelativeLayout) toolbar.findViewById(R.id.layoutNext);
        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null == adapter) {
                    showToastForGoingTooEarly();
                    return;
                }
                if (countSelectedSources() >= 1) {
                    putSelectedSourcesInSharedPreferences();

                    if (callFromMainActivity) {
                        exitActivity();
                    } else {
                        moveToNextActivity();
                    }
                } else {
                    showToastForBadSpecification();
                }
            }
        });
    }

    private void exitActivity() {
        Toast.makeText(this, "Успешно подесување на изворите.", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    // we should change it with a snickerbar
    private void showToastForBadSpecification() {
        Toast.makeText(this, getResources().getString(R.string.warning_at_least_one_source), Toast.LENGTH_LONG).show();
    }

    private void showToastForGoingTooEarly() {
        Toast.makeText(this, getResources().getString(R.string.going_too_early_sources), Toast.LENGTH_SHORT).show();
    }

    /*
    function for putting the selected sources in shared preferences,
    so that we can later know which sources the user wants to read from
     */
    private void putSelectedSourcesInSharedPreferences() {

        String func_tag = "putSelectedSourcesInSharedPreferences(): ";
        if (adapter != null) {

            List<Source> sources = adapter.getSources();
            SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.SOURCES_SPECIFICATION_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            List<Source> selectedSources = new ArrayList<Source>();


            for (Source s : sources) {
                if (s.isChecked()) {
                    selectedSources.add(s);
                }
            }

            for (Source wantedSource : selectedSources) {
                Log.i(TAG, String.format("%s%s: true", func_tag, wantedSource.toString()));
            }

            Gson gson = new Gson();
            Type typeToken = new TypeToken<List<Source>>() {
            }.getType();

            String gsonFormatList = gson.toJson(selectedSources, typeToken);

            editor.putString(GlobalInfo.SELECTED_SOURCES, gsonFormatList);

            editor.commit();
        }
    }

    private int countSelectedSources() {
        int toRet = 0;
        // da se sredi bugfix ako ne e izload-irano sve
        List<Source> sources = adapter.getSources();
        for (Source source : sources) {
            if (source.isChecked()) {
                toRet++;
            }
        }

        return toRet;
    }

    private void loadSources() {
        final String func_tag = "loadSources(): ";

        Log.i(TAG, func_tag + "Posting the animation for loading..");

        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });


        SourcesTask task = new SourcesTask(new OnSourcesHere() {
            @Override
            public void onTaskCompleted(List<Source> data) {

                Log.i(TAG, func_tag + "The task has been completed. Showing the data in the listview..");
                showSources(data);
            }
        });

        task.execute();

    }


    /*
    after we have loaded the sources from the server, we need to show them into the listview
    this function adds the loaded data into the listview
     */
    private void showSources(List<Source> data) {

        String func_tag = "showSources(): ";

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

        if (null == data) {
            Toast.makeText(this, "Обидете се повторно.", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.SOURCES_SPECIFICATION_PREF, Context.MODE_PRIVATE);

        String gsonList = preferences.getString(GlobalInfo.SELECTED_SOURCES, "");

        if (gsonList.length() > 0) {

            Gson gson = new Gson();

            Type typeToken = new TypeToken<List<Source>>() {
            }.getType();

            List<Source> selectedSources = gson.fromJson(gsonList, typeToken);

            for (Source s : data) {
                if (!callFromMainActivity) {
                    s.setIsChecked(true);
                } else {

                    boolean isSelected = false;

                    for (Source selected : selectedSources) {
                        if (selected.getId() == s.getId()) {
                            isSelected = true;
                            break;
                        }
                    }

                    s.setIsChecked(isSelected);
                }
            }
        } else {
            for (Source s : data) {
                s.setIsChecked(true);
            }
        }
        adapter = new SourcesAdapter(this, 0, data);
        listViewSources.setAdapter(adapter);
    }


    public void moveToNextActivity() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(GlobalInfo.PERSONALIZATION_STR, true);
        editor.commit();

        Intent intent = new Intent(this, AlternativeMain.class);
        startActivity(intent);
        this.finish();
    }

    private void colorAboveTheToolbar() {

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryColorDark));
        }

    }
}
