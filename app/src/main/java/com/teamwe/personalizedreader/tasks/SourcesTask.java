package com.teamwe.personalizedreader.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.GlobalInfo;

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

// getting the sources from the server
public class SourcesTask extends AsyncTask<Void, Void, List<Source>> {

    private static final String TAG = "SourcesTask";
    private OnSourcesHere listener;


    public SourcesTask(OnSourcesHere listener) {
        this.listener = listener;
    }

    @Override
    public List<Source> doInBackground(Void ... params) {


        List<Source> sources = null;
        try {
            String query = String.format("http://%s/get_sources", GlobalInfo.SERVER_IP);

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

            Log.i(TAG, "doInBackround: stream: " + sb.toString());

            Type typeToken = new TypeToken<List<Source>>() {}.getType();

            sources = gson.fromJson(sb.toString(), typeToken);


            Log.i(TAG, " Uspeshna konverzija. sources.size() = " + sources.size());

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


        return sources;
    }

    @Override
    public void onPostExecute(List<Source> sources) {
        if (sources !=null) {
            listener.onTaskCompleted(sources);
        } else {
            Log.i(TAG, "onPostExecute: sources are null SOMETHING IS WRONG!!!!");
        }
    }
}
