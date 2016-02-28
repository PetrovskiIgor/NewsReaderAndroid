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
import android.widget.Toast;

import com.teamwe.personalizedreader.adapters.CategoriesAdapter;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {


    private ListView listViewCategories;
    private TextView txtNext;
    private CategoriesAdapter adapter;
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

        List<Boolean> specification = getCurrentSpecification();

        adapter = new CategoriesAdapter(this,0);
        adapter.addAll(GlobalInfo.CATEGORIES);
        adapter.addCurrentSpecification(specification);
        listViewCategories.setAdapter(adapter);

        final Activity act = this;
        txtNext = (TextView)toolbar.findViewById(R.id.txtNext);
        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countCategoriesToRead() >= 1) {
                    putSpecificationInSharedPreferences();
                    moveToNextActivity();
                }
                else{
                    showToastForBadSpecification();
                }
            }
        });
    }

    private void showToastForBadSpecification() {
        Toast.makeText(this,"You can't have 0 categories for reading.", Toast.LENGTH_LONG).show();
    }

    private int countCategoriesToRead() {
        int toRet = 0;

        List<Boolean> specification = adapter.getSpecification();
        for (Boolean b : specification){
            if (b){
                toRet++;
            }
        }

        return toRet;
    }

    private List<Boolean> getCurrentSpecification() {
        List<Boolean> toRet = new ArrayList<Boolean>();

        SharedPreferences pref = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF,Context.MODE_PRIVATE);
        List<Category> categories = GlobalInfo.CATEGORIES;

        for (Category cat : categories){
            toRet.add(pref.getBoolean(cat.getName(),true));
        }

        return toRet;
    }

    private void putSpecificationInSharedPreferences() {
        List<Boolean> specification = adapter.getSpecification();
        SharedPreferences pref = this.getSharedPreferences(GlobalInfo.CAT_SPECIFICATION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        List<Category> categories = GlobalInfo.CATEGORIES;
        for (int i=0;i<categories.size();i++){
            editor.putBoolean(categories.get(i).getName(),specification.get(i));
        }

        editor.commit();
    }

    public void moveToNextActivity() {
        Intent intent = new Intent(this, SourcesActivity.class);
        intent.putExtra("callFromMainActivity", callFromMainActivity);
        startActivity(intent);
        this.finish();
    }
}
