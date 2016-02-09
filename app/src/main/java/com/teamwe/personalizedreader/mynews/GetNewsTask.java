package com.teamwe.personalizedreader.mynews;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by Petre on 1/5/2016.
 */
public class GetNewsTask extends AsyncTask<Category, Void, List<Cluster>>{
    private static final String TAG = "GetNews";
    OnNewsHere listener;


    public GetNewsTask(OnNewsHere listener) {
        this.listener = listener;
    }
    @Override
    public List<Cluster> doInBackground(Category ... params) {


        List<Cluster> clusters = null;
        Category category= params[0];
        try {
            String query = String.format("http://%s/get_my_clusters?category=%s", GlobalInfo.SERVER_IP,category.getName());

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

    @Override
    public void onPostExecute(List<Cluster> locations) {
        if (locations!=null) {
            listener.onTaskCompleted(locations);
        }
    }
}
