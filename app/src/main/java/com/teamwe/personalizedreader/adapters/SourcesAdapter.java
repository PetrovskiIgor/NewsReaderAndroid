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

import java.util.List;


public class SourcesAdapter extends ArrayAdapter<Source> {

    private LayoutInflater layoutInflater;
    private List<Source> data;
    private Context context;

    private boolean inNavigationDrawer;

    public SourcesAdapter(Context context, int resource, List<Source> data) {
        this(context, resource, data, false);
    }

    public SourcesAdapter(Context context, int resource, List<Source> data, boolean inNavigationDrawer) {
        super(context, resource);
        this.context = context;
        this.data = data;
        this.inNavigationDrawer = inNavigationDrawer;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (null == data) return 0;


        return data.size();
    }

    public List<Source> getData() {
        return data;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_image_text_checkbox, viewGroup, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.clearListeners();
        viewHolder.setPositionToCheckbox(position);

        Source currSource = data.get(position);
        if (currSource.getImgUrl() != null && currSource.getImgUrl().length() > 0)
            Picasso.with(context).load(currSource.getImgUrl()).into(viewHolder.imageView);
        // maybe we need to load the static image again in an else statement

        viewHolder.textView.setText(currSource.getName());

        if (!inNavigationDrawer) {
            viewHolder.checkBox.setChecked(currSource.isChecked());
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setOnCheckedChangeListener(checkBoxListener);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
        }
        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean ic) {
            int pos = (int) buttonView.getTag();
            data.get(pos).setIsChecked(ic);
        }
    };

    public List<Source> getSources() {
        return data;
    }

    public static class ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        private final ImageView imageView;

        private ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.textView);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }

        private void clearListeners() {
            checkBox.setOnCheckedChangeListener(null);
        }

        private void setPositionToCheckbox(int position) {
            checkBox.setTag(position);
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

}
