package com.teamwe.personalizedreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.List;


public class CategoriesAdapter extends ArrayAdapter<Category> {

    List<Category> data;
    Context context;

    public CategoriesAdapter(Context context, int resource) {
        super(context, resource);

        data = new ArrayList<Category>();
        this.context = context;
    }


    public void addAll(List<Category> elems) {
        data = elems;
        this.notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        convertView = LayoutInflater.from(context).inflate(R.layout.view_category, viewGroup, false);

        TextView txtCategory = (TextView)convertView.findViewById(R.id.txtCategory);

        txtCategory.setText(data.get(position).getTitle());
        return convertView;
    }


}
