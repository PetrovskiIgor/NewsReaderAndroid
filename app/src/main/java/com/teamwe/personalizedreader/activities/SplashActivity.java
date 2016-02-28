package com.teamwe.personalizedreader.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean answer = preferences.getBoolean(GlobalInfo.APP_HAS_BEEN_OPEN, false);
        Log.i(TAG, String.valueOf(answer));


        if (answer==false){

            new WaitingTask().execute();
            putInPreferences();
        }else{
            callMainActivity();
        }

    }

    private void putInPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(GlobalInfo.APP_HAS_BEEN_OPEN,true);
        editor.commit();
    }

    private void callMainActivity(){
        Intent intent = new Intent(this,CategoriesActivity.class);
        startActivity(intent);
        this.finish();
    }

    private class WaitingTask extends AsyncTask<String,String,Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                Thread.sleep(2500);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            callMainActivity();
        }
    }
}
