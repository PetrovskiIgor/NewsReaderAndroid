package com.teamwe.personalizedreader.adapters;

import android.content.Context;
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

    List<Category> data;
    Context context;
    List<Boolean> isChecked;

    public CategoriesAdapter(Context context, int resource) {
        super(context, resource);

        data = new ArrayList<Category>();
        this.context = context;
        this.isChecked = new ArrayList<Boolean> ();
    }


    public void addAll(List<Category> elems) {
        data = elems;
        this.notifyDataSetChanged();
    }

    public void addCurrentSpecification(List<Boolean> elements){
        this.isChecked = elements;
        this.notifyDataSetChanged();
    }

    public List<Boolean> getSpecification(){
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

        convertView = LayoutInflater.from(context).inflate(R.layout.view_category, viewGroup, false);

        TextView txtCategory = (TextView)convertView.findViewById(R.id.txtCategory);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cbCategory);

        txtCategory.setText(data.get(position).getTitle());
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
