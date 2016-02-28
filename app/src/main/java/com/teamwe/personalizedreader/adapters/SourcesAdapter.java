package com.teamwe.personalizedreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.teamwe.personalizedreader.model.Source;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.List;


public class SourcesAdapter extends ArrayAdapter<Source> {

    private List<Source> data;
    private Context context;
    public SourcesAdapter(Context context, int resource, List<Source> data) {
        super(context, resource);
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(null == data) return 0;


        return data.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(context).inflate(R.layout.view_source, viewGroup, false);

        TextView txtSource = (TextView)convertView.findViewById(R.id.txtSource);
        CheckBox cbSource = (CheckBox)convertView.findViewById(R.id.cbSource);


        Source currSource = data.get(position);

        txtSource.setText(currSource.getUrl());

        if(currSource.isChecked()) {
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


        return convertView;
    }


    public List<Source> getSources() {
        return data;
    }

}
