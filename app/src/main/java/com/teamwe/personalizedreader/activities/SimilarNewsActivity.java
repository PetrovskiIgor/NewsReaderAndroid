package com.teamwe.personalizedreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teamwe.personalizedreader.adapters.NewsPostsAdapter;
import com.teamwe.personalizedreader.model.Cluster;

import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;

public class SimilarNewsActivity extends AppCompatActivity {


    public static String TAG = "SimilarNewsActivity";



    private ListView listViewNews;


    private NewsPostsAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<NewsPost> listNews = bundle.<NewsPost>getParcelableArrayList(GlobalInfo.LIST_NEWS);

        adapter = new NewsPostsAdapter(this, listNews);

        NewsPost np = (NewsPost)intent.getParcelableExtra("mm");
        listViewNews = (ListView)findViewById(R.id.listViewSimilarNews);
        listViewNews.setAdapter(adapter);


        this.listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsPost currentNewsPost= adapter.getNewsPost(position);
                loadWebView(currentNewsPost);
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
