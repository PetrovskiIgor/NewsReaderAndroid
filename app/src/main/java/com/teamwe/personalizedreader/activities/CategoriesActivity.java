package com.teamwe.personalizedreader.activities;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarCategories);

        setSupportActionBar(toolbar);

        listViewCategories = (ListView)findViewById(R.id.listCategories);

        CategoriesAdapter adapter = new CategoriesAdapter(this,0);
        adapter.addAll(GlobalInfo.CATEGORIES);
        listViewCategories.setAdapter(adapter);

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
        startActivity(intent);
        this.finish();
    }
}
