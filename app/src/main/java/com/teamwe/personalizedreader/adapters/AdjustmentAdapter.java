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

/**
 * Created by igorpetrovski on 6/17/16.
 */
// adapter with only two elements

public class AdjustmentAdapter extends ArrayAdapter {

    public static String TAG = "AdjustmentAdapter";


    Context context;


    boolean inNavigationDrawer;

    public AdjustmentAdapter(Context context, int resource) {
        super(context, resource);

        this.context = context;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        if(0 == position) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adjustments_my_categories, viewGroup, false);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.adjustments_my_sources, viewGroup, false);
        }

        return convertView;

    }



}

