package com.teamwe.personalizedreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.teamwe.personalizedreader.adapters.ClusterAdapter;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.GetNewsTask;
import com.teamwe.personalizedreader.tasks.OnNewsHere;

import java.util.List;
import java.util.Set;

public class AlternativeMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG = "AlternativeMain";
    // the list where we load the news into
    private ListView listViewNews;

    private RelativeLayout layoutNoInternet;

    // the news of the current category that is shown to the user

    // DEFAULT-noto nema da bide ova!!!
    private Category currentCategory = new Category("MAKEDONIJA", "Македонија");

    // the adapter for the listview
    private ClusterAdapter adapter;

    // the material design swiping layout
    private SwipeRefreshLayout swipeView;

    private boolean noInternet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listViewNews = (ListView) findViewById(R.id.listNews);
        layoutNoInternet = (RelativeLayout)findViewById(R.id.layout_no_internet);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        configureListView();

        configureSwipeView();
        loadNews();
    }


    private void configureListView() {

        this.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cluster currentCluster = adapter.getCluster(position);

                loadWebView(currentCluster.listNews.get(0));
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
                loadNews();
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

    // method for loading the news (from the selected category) into the listview
    private void loadNews() {

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();


        // We need to check the internet connection
        if(null == info || !info.isConnected()) {

            layoutNoInternet.setVisibility(View.VISIBLE);
            listViewNews.setVisibility(View.GONE);

            Log.i(TAG, "No internet connection.");
            Toast.makeText(this, " NO INTERNET CONNECTION! ", Toast.LENGTH_SHORT).show();
            return;
        }

        if(layoutNoInternet.getVisibility() == View.VISIBLE) {
            layoutNoInternet.setVisibility(View.GONE);
        }

        if(listViewNews.getVisibility() == View.GONE) {
            listViewNews.setVisibility(View.VISIBLE);
        }

        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });



        final Activity act = this;

        Set<String> someSources = GlobalInfo.SOME_SOURCES;

        GetNewsTask task = new GetNewsTask(new OnNewsHere() {
            @Override
            public void onTaskCompleted(List<Cluster> list) {
                adapter = new ClusterAdapter(act, list);
                listViewNews.setAdapter(adapter);

                if (swipeView.isRefreshing()) {
                    swipeView.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                        }
                    });
                }
            }
        }, someSources);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            currentCategory = new Category("MAKEDONIJA", "Македонија");
        } else if (id == R.id.nav_gallery) {
            currentCategory = new Category("EKONOMIJA", "Економија");

        } else if (id == R.id.nav_slideshow) {
            currentCategory = new Category("SVET", "Свет");

        } else if (id == R.id.nav_manage) {
            currentCategory = new Category("KULTURA", "Култура");

        } else if (id == R.id.nav_share) {
            currentCategory = new Category("ZABAVA", "Забава");

        } else if (id == R.id.nav_send) {
            currentCategory = new Category("NASTANI", "Настани");

        } else {
            currentCategory = new Category("MAKEDONIJA", "Македонија");
        }

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
