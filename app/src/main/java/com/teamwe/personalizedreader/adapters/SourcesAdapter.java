package com.teamwe.personalizedreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.List;


public class SourcesAdapter extends ArrayAdapter<Source> {

    private List<Source> data;
    private Context context;

    private boolean inNavigationDrawer;

    public SourcesAdapter(Context context, int resource, List<Source> data) {
        super(context, resource);
        this.context = context;
        this.data = data;

        this.inNavigationDrawer = false;
    }

    public SourcesAdapter(Context context, int resource, List<Source> data, boolean inNavigationDrawer) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.inNavigationDrawer = inNavigationDrawer;
    }

    @Override
    public int getCount() {
        if(null == data) return 0;


        return data.size();
    }

    public List<Source> getData() {
        return data;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(context).inflate(R.layout.view_source, viewGroup, false);

        Source currSource = data.get(position);

        TextView txtSource = (TextView)convertView.findViewById(R.id.txtSource);
        CheckBox cbSource = (CheckBox)convertView.findViewById(R.id.cbSource);

        ImageView imgCategory = (ImageView)convertView.findViewById(R.id.imgSource);

        if(currSource.getImgUrl() != null && currSource.getImgUrl().length() > 0)
            Picasso.with(context).load(currSource.getImgUrl()).into(imgCategory);
        // maybe we need to load the static image again in an else statement




        txtSource.setText(currSource.getName());


        if(!inNavigationDrawer) {
            if (currSource.isChecked()) {
                cbSource.setChecked(true);
            } else {
                cbSource.setChecked(false);
            }


            cbSource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.get(position).setIsChecked(isChecked);
                }
            });
        } else {
            cbSource.setVisibility(View.GONE);
        }


        return convertView;
    }


    public List<Source> getSources() {
        return data;
    }

}
