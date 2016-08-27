package com.teamwe.personalizedreader.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.ClusterWrapper;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.model.NewsPost;
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
import java.util.Set;


public class GetNewsTask extends AsyncTask<Category, Void, List<Cluster>>{
    private static final String TAG = "GetNews";
    private final List<Source> selectedSources;
    private OnNewsHere listener;
    private Activity activity;

    public GetNewsTask(Activity activity,OnNewsHere listener, List<Source> selectedSources) {
        this.listener = listener;
        this.selectedSources = selectedSources;
        this.activity = activity;
    }
    @Override
    public List<Cluster> doInBackground(Category ... params) {


        List<Cluster> clusters = null;
        Category category = params[0];
        try {
            StringBuilder sb = new StringBuilder();
            for (Source s : selectedSources){
                sb.append(s.getId()+",");
            }

            String wantedSources = "";
            if (sb.toString().length()>0) {
                wantedSources = sb.toString().substring(0, sb.length() - 1);
            }


            // needs working about the categories
            String query = String.format("%sread_clusters?format=json&how_much=%d&selected_sources=%s", GlobalInfo.SERVER_IP, GlobalInfo.HOW_MUCH_TOP_NEWS,wantedSources);

            // if we have picked a specific category (not top news)
            if(category.getId() >= 0) {
                query += String.format("&category_id=%d", category.getId());
            } else {
                StringBuilder sbUnwantedCategories = new StringBuilder();

                List<Category> unwantedCategories = this.getUnwantedCategories();
                if(unwantedCategories != null) {
                    for (Category cat : unwantedCategories) {
                        sbUnwantedCategories.append(cat.getId() + ",");
                    }

                    if(sbUnwantedCategories.length() > 0) {
                        String strFinal = sbUnwantedCategories.toString().substring(0, sbUnwantedCategories.length() - 1);
                        query += String.format("&unwanted_categories=%s", strFinal);
                    }
                }
            }


            Log.i(TAG, "doInBackground: Sostaveno query: " + query);

            URL url = new URL(query);

            URLConnection conn = url.openConnection();
            Log.i(TAG, "doInBackground: instanciran objekt za konekcija");
            InputStream inputStream = conn.getInputStream();
            Log.i(TAG, "doInBackground: zemen stream od server");

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String line;

            sb = new StringBuilder();
            while(null != (line = br.readLine()))
                sb.append(line);

            Log.i(TAG, " doInBackground: probuvam da konveritram so gson");
            GsonBuilder builder = new GsonBuilder();

            Gson gson = builder.disableHtmlEscaping().create();
            //Type typeToken = new TypeToken<List<Cluster>>() {}.getType();
            Log.i(TAG, "doInBackround: stream: " + sb.toString());

            ClusterWrapper wrapper  = gson.fromJson(sb.toString(),ClusterWrapper.class);
            clusters = wrapper.listClusters;
            Log.i(TAG, " Uspeshna konverzija. clusters.size() = " + clusters.size());

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


        return clusters;
    }


    private List<Category> getUnwantedCategories() {

        SharedPreferences preferences = activity.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);
        String gsonList = preferences.getString(GlobalInfo.UNSELECTED_CATEGORIES, "");
        Gson gson = new Gson();
        Type typeToken = new TypeToken<List<Category>>() {}.getType();
        List<Category> unselectedCategories = gson.fromJson(gsonList, typeToken);

        return unselectedCategories;
    }

    @Override
    public void onPostExecute(List<Cluster> clusters) {
        listener.onTaskCompleted(clusters);
    }

}
