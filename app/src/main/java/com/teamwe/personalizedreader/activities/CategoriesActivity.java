package com.teamwe.personalizedreader.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.teamwe.personalizedreader.adapters.CategoriesAdapter;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

public class CategoriesActivity extends AppCompatActivity {


    private ListView listViewCategories;
    private TextView txtNext;
    private boolean callFromMainActivity = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Intent intent = getIntent();
        callFromMainActivity = intent.getBooleanExtra("callFromMainActivity", false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPersonalized = sharedPreferences.getBoolean(GlobalInfo.PERSONALIZATION_STR, false);



        if(!callFromMainActivity && isPersonalized) {
            moveToNextActivity();
            this.finish();
        }


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarCategories);

        setSupportActionBar(toolbar);

        listViewCategories = (ListView)findViewById(R.id.listCategories);

        CategoriesAdapter adapter = new CategoriesAdapter(this,0);
        adapter.addAll(GlobalInfo.CATEGORIES);
        listViewCategories.setAdapter(adapter);

        final Activity act = this;
        txtNext = (TextView)toolbar.findViewById(R.id.txtNext);
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
                moveToNextActivity();
            }
        });
    }

    public void moveToNextActivity() {
        Intent intent = new Intent(this, SourcesActivity.class);
        intent.putExtra("callFromMainActivity", callFromMainActivity);
        startActivity(intent);
        this.finish();
    }
}
