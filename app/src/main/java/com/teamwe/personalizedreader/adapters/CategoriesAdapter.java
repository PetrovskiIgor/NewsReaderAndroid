package com.teamwe.personalizedreader.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.List;


public class CategoriesAdapter extends ArrayAdapter<Category> {

    public static String TAG = "CategoriesAdapter";

    List<Category> data;
    Context context;
    List<Boolean> isChecked;

    public CategoriesAdapter(Context context, int resource, List<Category> data) {
        super(context, resource);

        this.data = data;
        this.context = context;
        this.isChecked = new ArrayList<Boolean> ();


        // all categories are checked in the beginning
        for(Category cat : data) {
            isChecked.add(true);
        }
    }


    public void addAll(List<Category> elems) {
        data = elems;
        this.notifyDataSetChanged();
    }

    public void addCurrentSpecification(List<Boolean> elements){
        this.isChecked = elements;
        this.notifyDataSetChanged();
    }

    public List<Boolean> getSpecification() {
        return this.isChecked;
    }

    public void add(Category element) {
        data.add(element);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(null == data) return 0;

        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        Log.i(TAG, "position: " + position);
        convertView = LayoutInflater.from(context).inflate(R.layout.view_category, viewGroup, false);
        Log.i(TAG, "convertView: " + convertView);

        TextView txtCategory = (TextView)convertView.findViewById(R.id.txtCategory);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cbCategory);

        txtCategory.setText(data.get(position).getName());
        checkBox.setChecked(isChecked.get(position));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean ic) {
                isChecked.set(position,ic);
            }
        });

        return convertView;
    }



}
