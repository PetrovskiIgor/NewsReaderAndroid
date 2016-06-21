package com.teamwe.personalizedreader.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.List;


public class CategoriesAdapter extends ArrayAdapter<Category> {

    public static String TAG = "CategoriesAdapter";

    List<Category> data;
    Context context;

    boolean inNavigationDrawer;

    public CategoriesAdapter(Context context, int resource, List<Category> data) {
        super(context, resource);

        this.data = data;
        this.context = context;
        this.inNavigationDrawer = false;
    }

    public CategoriesAdapter(Context context, int resource, List<Category> data, boolean inNavigationDrawer) {
        super(context, resource);

        this.data = data;
        this.context = context;
        this.inNavigationDrawer = inNavigationDrawer;
    }


    public List<Category> getCategories() {
        return this.data;
    }

    public void add(Category element) {
        data.add(element);
        this.notifyDataSetChanged();
    }


    public List<Category> getData() {
        return data;
    }

    @Override
    public int getCount() {
        if(null == data) return 0;

        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {


        Category currCat = data.get(position);

        Log.i(TAG, "position: " + position);
        convertView = LayoutInflater.from(context).inflate(R.layout.view_category, viewGroup, false);
        Log.i(TAG, "convertView: " + convertView);

        TextView txtCategory = (TextView)convertView.findViewById(R.id.txtCategory);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cbCategory);
        ImageView imgCategory = (ImageView)convertView.findViewById(R.id.imgCategory);

        if(currCat.getImgUrl() != null && currCat.getImgUrl().length() > 0)
            Picasso.with(context).load(currCat.getImgUrl()).into(imgCategory);


        if (!inNavigationDrawer) {
            txtCategory.setText(data.get(position).getTitle());
            checkBox.setChecked(data.get(position).getCheckedState());

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean ic) {
                    data.get(position).setCheckedState(ic);
                }
            });
        } else {
            txtCategory.setText(data.get(position).getTitle());
           // txtCategory.setTextAppearance(context,android.R.style.TextAppearance_Small);
            checkBox.setVisibility(View.GONE);
        }

        return convertView;
    }



}
