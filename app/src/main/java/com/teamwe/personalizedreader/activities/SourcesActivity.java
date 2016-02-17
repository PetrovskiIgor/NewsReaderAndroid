package com.teamwe.personalizedreader.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import com.teamwe.personalizedreader.adapters.SourcesAdapter;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;
import com.teamwe.personalizedreader.tasks.OnSourcesHere;
import com.teamwe.personalizedreader.tasks.SourcesTask;

import java.util.List;

public class SourcesActivity extends AppCompatActivity {

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



        TextView txtNext = (TextView)findViewById(R.id.txtNext);

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(GlobalInfo.PERSONALIZATION_STR, true);
                editor.commit();
                moveToNextActivity();
            }
        });


        SourcesTask task = new SourcesTask(new OnSourcesHere() {
            @Override
            public void onTaskCompleted(List<Source> data) {
                loadSources(data);
            }
        });

        task.execute();

    }

    private void loadSources(List<Source> data) {
        adapterSources = new SourcesAdapter(this, 0, data);
        listSources.setAdapter(adapterSources);
    }


    public void moveToNextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
