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
import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.mynews.R;

import java.util.List;


public class CategoriesAdapter extends ArrayAdapter<Category> {

    public static String TAG = "CategoriesAdapter";
    private LayoutInflater layoutInflater;
    private List<Category> data;
    private Context context;

    private boolean inNavigationDrawer;

    public CategoriesAdapter(Context context, int resource, List<Category> data) {
        this(context, resource, data, false);
    }

    public CategoriesAdapter(Context context, int resource, List<Category> data, boolean inNavigationDrawer) {
        super(context, resource);
        this.data = data;
        this.context = context;
        this.inNavigationDrawer = inNavigationDrawer;
        this.layoutInflater = LayoutInflater.from(context);
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
        if (null == data) return 0;

        if (inNavigationDrawer) {
            return 1 + data.size(); // Најнови
        }
        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_image_text_checkbox, viewGroup, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.clearListeners();

        final int pos = inNavigationDrawer ? (position - 1) : position;
        viewHolder.setPositionToCheckbox(pos);
        if (inNavigationDrawer && position == 0) {
            viewHolder.txtCategory.setText(context.getResources().getString(R.string.trending_news));
            Picasso.with(context).load(R.drawable.trending_moj_vesnik).into(viewHolder.imgCategory);
        } else {
            Category currCat = data.get(pos);
            if (currCat.getImgUrl() != null && currCat.getImgUrl().length() > 0) {
                Picasso.with(context).load(currCat.getImgUrl()).into(viewHolder.imgCategory);
            }
            viewHolder.txtCategory.setText(data.get(pos).getTitle());
        }
        if (!inNavigationDrawer) {
            viewHolder.checkBox.setChecked(data.get(pos).getCheckedState());
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
            data.get(pos).setCheckedState(ic);
        }
    };

    public static class ViewHolder {
        private final TextView txtCategory;
        private final CheckBox checkBox;
        private final ImageView imgCategory;

        private ViewHolder(View view) {
            txtCategory = (TextView) view.findViewById(R.id.textView);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            imgCategory = (ImageView) view.findViewById(R.id.imageView);
        }

        private void clearListeners(){
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
