package com.teamwe.personalizedreader.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.GlobalInfo;

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

// getting the categories from the server
public class CategoriesTask extends AsyncTask<Void, Void, List<Category>> {

    private static final String TAG = "CategoriesTask";
    private OnCategoriesHere listener;


    public CategoriesTask(OnCategoriesHere listener) {
        this.listener = listener;
    }

    @Override
    public List<Category> doInBackground(Void ... params) {
        String func_tag = "doInBackground(): ";

        List<Category> categories = null;
        try {
            String query = String.format("%sget_categories?format=json", GlobalInfo.SERVER_IP);
            Log.i(TAG, func_tag + "query: " + query);
            Log.i(TAG, "doInBackground: Sostaveno query: " + query);

            URL url = new URL(query);
            Log.i(TAG, "Instanciran URL objekt od query");
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

            Log.i(TAG, "doInBackround: stream: " + sb.toString());

            Type typeToken = new TypeToken<List<Category>>() {}.getType();

            categories = gson.fromJson(sb.toString(), typeToken);

            Log.i(TAG, " Uspeshna konverzija. categories.size() = " + categories.size());

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


        return categories;
    }

    @Override
    public void onPostExecute(List<Category> categories) {
        listener.onTaskCompleted(categories);
    }
}
