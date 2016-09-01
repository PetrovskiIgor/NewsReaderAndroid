package com.teamwe.personalizedreader.activities;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

public class NewsPostActivity extends AppCompatActivity {


    public static String TAG = "NewsPostActivity";

    private String url;
    private String title;


    WebView webview;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_post);





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* swipeView = (SwipeRefreshLayout)findViewById(R.id.swipeViewNewsPost);

        swipeView.setRefreshing(true);
        swipeView.post(new Runnable() {
            @Override
            public void run() {
                swipeView.setRefreshing(true);
            }
        });*/

        Intent intent = getIntent();
        url = intent.getStringExtra(GlobalInfo.NEWS_URL);
        title = intent.getStringExtra(GlobalInfo.NEWS_TITLE);

        String shortTitle = title;
        if(shortTitle.length() > 19) {
            shortTitle= shortTitle.substring(0, 19) + "...";
        }
        setTitle(shortTitle);
        webview = (WebView)findViewById(R.id.webView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        configureWebView();
        configureShareButton();
    }


    private void configureShareButton() {
        /*
       // btnShare = (FloatingActionButton)findViewById(R.id.btnShare);
        //btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClicked();
            }
        });*/
    }

    private void onShareClicked() {
        Intent i=new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
        i.putExtra(android.content.Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(i, "Share via"));

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
        } else if(id == R.id.menu_item_share) {
            //Toast.makeText(this,"should be ok", Toast.LENGTH_SHORT).show();
            onShareClicked();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void configureWebView() {
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...

                progressBar.setProgress(progress); //Make the bar disappear after URL is loaded
                Log.i(TAG, "progress: " + progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                // Return the app name after finish loadin
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public void onResume() {
        super.onResume();
        webview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webview.onPause();
    }

}
