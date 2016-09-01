package com.teamwe.personalizedreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.commonsware.cwac.merge.MergeAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.adapters.AdjustmentAdapter;
import com.teamwe.personalizedreader.adapters.CategoriesAdapter;
import com.teamwe.personalizedreader.adapters.ClusterAdapter;
import com.teamwe.personalizedreader.adapters.NewsPostsAdapter;
import com.teamwe.personalizedreader.adapters.SourcesAdapter;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.GetNewsFromSourceTask;
import com.teamwe.personalizedreader.tasks.GetNewsTask;
import com.teamwe.personalizedreader.tasks.OnNewsFromSourceHere;
import com.teamwe.personalizedreader.tasks.OnNewsHere;

import java.lang.reflect.Type;
import java.util.List;

public class AlternativeMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG = "AlternativeMain";
    // the list where we load the news into
    private ListView listViewNews;

    private RelativeLayout layoutNoInternet;

    // the news of the current category that is shown to the user
    private Category currentCategory;
    private Source currentSource;

    // the adapter for the listview
    private ClusterAdapter adapter;


    private NewsPostsAdapter adapterNewsFromSource;

    // the material design swiping layout
    private SwipeRefreshLayout swipeView;

    private boolean noInternet = false;


    private NavigationView navigationView;


    private ListView listViewNV;

    private static final int REQUEST_CODE_CATEGORIES = 5;
    private static final int REQUEST_CODE_SOURCES=7;
    private static final int REQUEST_CODE_NOTIFICATIONS=9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listViewNews = (ListView) findViewById(R.id.listNews);
        layoutNoInternet = (RelativeLayout) findViewById(R.id.layout_no_internet);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        populateNavigationView();

        configureListView();
        configureSwipeView();
        currentCategory = new Category(-1, "Trending", getResources().getString(R.string.trending_news));
        this.setTitle(currentCategory.getTitle());

        loadNews();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private void populateNavigationView() {

        listViewNV = (ListView)findViewById(R.id.listViewNV);
        listViewNV.setDivider(null);


        // --- load the categories --
        SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);
        String gsonList = preferences.getString(GlobalInfo.SELECTED_CATEGORIES, "");
        Gson gson = new Gson();
        Type typeToken = new TypeToken<List<Category>>() {}.getType();
        List<Category> selectedCategories = gson.fromJson(gsonList, typeToken);

        final CategoriesAdapter adapterCategories = new CategoriesAdapter(this, 0, selectedCategories, true);
        // --- loaded the categories ---

        // --- load the sources --
        preferences = this.getSharedPreferences(GlobalInfo.SOURCES_SPECIFICATION_PREF, Context.MODE_PRIVATE);

        gsonList = preferences.getString(GlobalInfo.SELECTED_SOURCES, "");
        gson = new Gson();
        typeToken = new TypeToken<List<Source>>() {}.getType();
        List<Source> selectedSources = gson.fromJson(gsonList, typeToken);

        final SourcesAdapter sourcesAdapter = new SourcesAdapter(this, 0, selectedSources, true);
        // --- loaded the sources --

        final MergeAdapter mergeAdapter = new MergeAdapter();

        // categories
        View headerCategories = LayoutInflater.from(this).inflate(R.layout.header_navigation_drawer_categories, null);
        mergeAdapter.addView(headerCategories);
        mergeAdapter.addAdapter(adapterCategories);

        // sources
        View headerSources = LayoutInflater.from(this).inflate(R.layout.header_navigation_drawer_sources,null);
        mergeAdapter.addView(headerSources);
        mergeAdapter.addAdapter(sourcesAdapter);




        listViewNV.setAdapter(mergeAdapter);


        final Activity act = this;

        listViewNV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int sz_1 = adapterCategories.getCount() + 1;
                if (position < sz_1) {

                    int relativePosition = position - 1;
                    Category cat = null;
                    if(relativePosition == 0) {
                        cat = new Category(-1, "Trending", getResources().getString(R.string.trending_news));
                    } else {
                        cat = adapterCategories.getData().get(relativePosition-1);
                    }

                    currentCategory = cat;
                    currentSource = null;

                    act.setTitle(currentCategory.getTitle());
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    loadNews();
                    //Toast.makeText(act, cat.getTitle(), Toast.LENGTH_SHORT).show();
                }  else {
                    int relativePosition = position - sz_1 - 1;

                    Source source = sourcesAdapter.getData().get(relativePosition);
                    //Toast.makeText(act, source.getPrettyUrl(), Toast.LENGTH_SHORT).show();

                    currentSource = source;
                    currentCategory = null;

                    act.setTitle(currentSource.getName());
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    loadNewsFromSource();
                }


            }
        });





    }


    private void configureListView() {

        this.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsPost toLoad = null;
                if(adapter != null) {
                    Cluster currentCluster = adapter.getCluster(position);
                    toLoad = currentCluster.listNews.get(0);

                } else {
                    toLoad = adapterNewsFromSource.getNewsPost(position);
                }

                loadWebView(toLoad);
            }
        });
    }

    private void loadWebView(NewsPost newsPost) {
        String url = newsPost.getUrl();

        Intent intent = new Intent(this, NewsPostActivity.class);
        intent.putExtra(GlobalInfo.NEWS_URL, url);
        intent.putExtra(GlobalInfo.NEWS_TITLE, newsPost.getTitle());
        startActivity(intent);

    }


    private void configureSwipeView() {

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipeLayoutNews);

        Log.i(TAG, "Swipe view: " + swipeView);

        swipeView.setColorSchemeResources(R.color.primaryColor);

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);

                if (currentCategory != null)
                    loadNews();
                else
                    loadNewsFromSource();
            }
        });

        listViewNews.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.i(TAG, "onScroll: firstVisibleItem: " + firstVisibleItem + " visibleItemCount: " + visibleItemCount + " totalItemCount: " + totalItemCount);
                if (0 == firstVisibleItem || noInternet) {
                    swipeView.setEnabled(true);
                } else {
                    swipeView.setEnabled(false);
                }
            }
        });
    }


    private void loadNewsFromSource() {
        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });

        final Activity act = this;



        GetNewsFromSourceTask task = new GetNewsFromSourceTask(new OnNewsFromSourceHere(){
            @Override
            public void onTaskCompleted(List<NewsPost> list) {

                adapterNewsFromSource = new NewsPostsAdapter(act, list);
                listViewNews.setAdapter(adapterNewsFromSource);

                adapter = null;

                if (swipeView.isRefreshing()) {
                    swipeView.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                        }
                    });
                }
            }
        });

        task.execute(currentSource);

    }

    // method for loading the news (from the selected category) into the listview
    private void loadNews() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();


        // We need to check the internet connection
        if (null == info || !info.isConnected()) {

            layoutNoInternet.setVisibility(View.VISIBLE);
            listViewNews.setVisibility(View.GONE);

            Log.i(TAG, "No internet connection.");
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            return;
        }

        if (layoutNoInternet.getVisibility() == View.VISIBLE) {
            layoutNoInternet.setVisibility(View.GONE);
        }

        if (listViewNews.getVisibility() == View.GONE) {
            listViewNews.setVisibility(View.VISIBLE);
        }

        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });


        final Activity act = this;

        SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.SOURCES_SPECIFICATION_PREF, Context.MODE_PRIVATE);

        String gsonList =  preferences.getString(GlobalInfo.SELECTED_SOURCES, "");
        Gson gson = new Gson();

        Type typeToken = new TypeToken<List<Source>>() {}.getType();

        List<Source> selectedSources = gson.fromJson(gsonList, typeToken);


        GetNewsTask task = new GetNewsTask(this,new OnNewsHere() {
            @Override
            public void onTaskCompleted(List<Cluster> list) {
                adapter = new ClusterAdapter(act, list);
                listViewNews.setAdapter(adapter);

                adapterNewsFromSource = null;

                if (swipeView.isRefreshing()) {
                    swipeView.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                        }
                    });
                }
            }
        }, selectedSources);

        task.execute(currentCategory);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alternative_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.categories_settings) {
            configureMyCategories();
            return true;
        } else if(id == R.id.sources_settings) {
            configureMySources();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void configureMySources() {
        Intent intent = new Intent(this, SourcesActivity.class);
        intent.putExtra(GlobalInfo.CALL_FROM_MAIN_ACTIVITY, true);
        this.startActivityForResult(intent, REQUEST_CODE_SOURCES);
    }

    private void configureMyCategories() {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra(GlobalInfo.CALL_FROM_MAIN_ACTIVITY, true);
        this.startActivityForResult(intent, REQUEST_CODE_CATEGORIES);
    }

    private void configureNotifications() {
        Intent intent = new Intent(this, NotificationsActivity.class);
        this.startActivityForResult(intent, REQUEST_CODE_NOTIFICATIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_CATEGORIES || requestCode == REQUEST_CODE_SOURCES) {
            // refresh the navigation view
            populateNavigationView();


            if(currentCategory != null) {
                loadNews();
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        this.setTitle(currentCategory.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        loadNews();
        return true;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
