package com.teamwe.personalizedreader.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.ClusterWrapper;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.model.NewsPostWrapper;
import com.teamwe.personalizedreader.model.Source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * Created by igorpetrovski on 6/17/16.
 */


public class GetNewsFromSourceTask extends AsyncTask<Source, Void, List<NewsPost>> {

    private static final String TAG = "GetNewsFromSourceTask";
    OnNewsFromSourceHere listener;


    public GetNewsFromSourceTask(OnNewsFromSourceHere listener) {
        this.listener = listener;
    }
    @Override
    public List<NewsPost> doInBackground(Source ... params) {


        List<NewsPost> newsPosts = null;
        Source source = params[0];
        try {


            String query = String.format("%sget_news_from_page?source_id=%d&format=json", GlobalInfo.SERVER_IP, source.getId());

            Log.i(TAG, "doInBackground: Sostaveno query: " + query);

            URL url = new URL(query);

            URLConnection conn = url.openConnection();
            Log.i(TAG, "doInBackground: instanciran objekt za konekcija");
            InputStream inputStream = conn.getInputStream();
            Log.i(TAG, "doInBackground: zemen stream od server");

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String line;

            StringBuilder sb = new StringBuilder();
            while(null != (line = br.readLine()))
                sb.append(line);

            Log.i(TAG, " doInBackground: probuvam da konveritram so gson");
            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.disableHtmlEscaping().create();
            //Type typeToken = new TypeToken<List<Cluster>>() {}.getType();
            Log.i(TAG, "doInBackround: stream: " + sb.toString());


            NewsPostWrapper wrapper  = gson.fromJson(sb.toString(),NewsPostWrapper.class);
            newsPosts= wrapper.listNewsPosts;

            Log.i(TAG, " Uspeshna konverzija. newsPosts.size() = " + newsPosts.size());

        }catch(UnsupportedEncodingException e) {
            Log.i(TAG, " doInBackground: UnsupportedEncodingException: " + e.getMessage());
            e.printStackTrace();
        }catch(MalformedURLException e) {
            Log.i(TAG, " doInBackground: MalformedURLException: " + e.getMessage());
            e.printStackTrace();
        }catch(IOException e) {
            Log.i(TAG, " doInBackground: IOException: " + e.getMessage());
            e.printStackTrace();
        }catch(NumberFormatException e) {
            Log.i(TAG, " doInBackground: NumberFormatException: " + e.getMessage());
            e.printStackTrace();
        }catch(Exception e) {
            Log.i(TAG, " doInBackground: Exception: " + e.getMessage());
            e.printStackTrace();
        }


        return newsPosts;
    }

    @Override
    public void onPostExecute(List<NewsPost> newsPosts) {
        listener.onTaskCompleted(newsPosts);
    }
}

