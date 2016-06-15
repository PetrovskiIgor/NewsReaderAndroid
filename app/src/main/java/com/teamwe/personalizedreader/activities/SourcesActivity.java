package com.teamwe.personalizedreader.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.teamwe.personalizedreader.adapters.SourcesAdapter;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.OnSourcesHere;
import com.teamwe.personalizedreader.tasks.SourcesTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SourcesActivity extends AppCompatActivity {

    private static final String TAG = "SourcesActivity";

    private ListView listSources;
    private SourcesAdapter adapterSources;
    private boolean callFromMainActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);


        Intent intent = getIntent();
        callFromMainActivity = intent.getBooleanExtra("callFromMainActivity", false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPersonalized = sharedPreferences.getBoolean(GlobalInfo.PERSONALIZATION_STR, false);


        if(!callFromMainActivity && isPersonalized) {
            moveToNextActivity();
            this.finish();
        }


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarSources);
        setSupportActionBar(toolbar);

        listSources = (ListView)findViewById(R.id.listSources);



        RelativeLayout layoutNext = (RelativeLayout)findViewById(R.id.layoutNext);

        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(GlobalInfo.PERSONALIZATION_STR, true);
                editor.commit();
                if (putSourcesSpecificationToPref()) {
                    moveToNextActivity();
                }
            }
        });


        loadSources();

    }

    private void loadSources(){
        SourcesTask task = new SourcesTask(new OnSourcesHere() {
            @Override
            public void onTaskCompleted(List<Source> data) {
                loadSources(data);
            }
        });

        task.execute();

    }

    private boolean putSourcesSpecificationToPref() {
        if (adapterSources != null){
            List<Source> sources = adapterSources.getSources();
            SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.SOURCES_SPECIFICATION_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            HashSet<String> wanted = new HashSet<String>();
            for (Source s : sources) {
                if (s.isChecked()){
                    wanted.add(s.getId()+"");
                }
            }
            if (wanted.size()==0) {
                editor.commit();
                return false;
            }

            editor.putStringSet(GlobalInfo.WANTED_SOURCES, wanted);
            editor.commit();
        }
        return true;
    }

    private void loadSources(List<Source> data) {

        SharedPreferences preferences = this.getSharedPreferences(GlobalInfo.SOURCES_SPECIFICATION_PREF, Context.MODE_PRIVATE);
        Set<String> wantedSources = preferences.getStringSet(GlobalInfo.WANTED_SOURCES, new HashSet<String> ());


        for (Source s : data){
            if (!callFromMainActivity){
                s.setIsChecked(true);
            }else {
                s.setIsChecked(wantedSources.contains(s.getId() + ""));
            }
        }

        adapterSources = new SourcesAdapter(this, 0, data);
        listSources.setAdapter(adapterSources);
    }


    public void moveToNextActivity() {
        Intent intent = new Intent(this, AlternativeMain.class);
        startActivity(intent);
        this.finish();
    }
}
