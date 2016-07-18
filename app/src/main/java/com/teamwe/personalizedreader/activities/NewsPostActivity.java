package com.teamwe.personalizedreader.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

public class NewsPostActivity extends AppCompatActivity {


    private String url;
    private String title;

    boolean loadingFinished = true;
    boolean redirect = false;
    WebView webview;

    private SwipeRefreshLayout swipeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_news_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeView = (SwipeRefreshLayout)findViewById(R.id.swipeViewNewsPost);

        swipeView.setRefreshing(true);
        /*swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });*/

        Intent intent = getIntent();
        url = intent.getStringExtra(GlobalInfo.NEWS_URL);
        title = intent.getStringExtra(GlobalInfo.NEWS_TITLE);
        setTitle(title);
        webview = new WebView(this);
        setContentView(webview);


        final Activity act = this;

        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                /*swipeView.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                    }
                });*/

            }

        });

        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);


        // so that the user can watch videos
        webview.getSettings().setJavaScriptEnabled(true);

        // CELA SLIKA NA EKRAN..

        // MOZHEBI E LOSHO ?!
       /* webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);*/

        webview.loadUrl(url);
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
