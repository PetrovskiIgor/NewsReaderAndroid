package com.teamwe.personalizedreader.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.activities.SimilarNewsActivity;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.mynews.R;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class NewsPostsAdapter extends BaseAdapter {

    private static final String TAG = "ClusterAdapter";
    private Activity activity;
    private List<NewsPost> newsPosts;

    public NewsPostsAdapter(Activity activity, List<NewsPost> clusters){
        super();
        this.activity = activity;
        this.newsPosts = clusters;
    }


    public NewsPost getNewsPost(int position) {

        return newsPosts.get(position);
    }

    @Override
    public int getCount() {
        if (newsPosts!=null)
            return newsPosts.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (position<getCount())
            return newsPosts.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView ==null){

            convertView = activity.getLayoutInflater().inflate(R.layout.row_cluster,parent,false);
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.textViewNumPosts = (TextView) convertView.findViewById(R.id.textViewNumPosts);
            holder.txtSourceUrl = (TextView)convertView.findViewById(R.id.textViewSourceUrl);
            holder.imgViewPhoto = (ImageView) convertView.findViewById(R.id.imgCluster);
            holder.txtPubDate = (TextView)convertView.findViewById(R.id.txtPubDate);
            holder.txtDescription = (TextView)convertView.findViewById(R.id.txtDescription);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        if (position>=getCount())
            return null;

        final NewsPost newsPost = this.newsPosts.get(position);

        if (newsPost!=null){
            holder.position=position;
            String title = newsPost.getTitle();
            String imgUrl = newsPost.getImg_url();
            String sourceUrl = newsPost.source_url;
            long pubDate = newsPost.pubDate;
            String description = newsPost.description;

            holder.txtPubDate.setText(GlobalInfo.configureDate(pubDate));

            if(null == description)
                description = "";
            description = description.trim();
            if(description.length() > 80) {
                description = description.substring(0, 80);
            }
            description += "...";
            holder.txtDescription.setText(description);


            String bStr = "http://";
            if(sourceUrl.startsWith(bStr)) {
                sourceUrl = sourceUrl.substring(bStr.length());
            }

            bStr = "www.";
            if(sourceUrl.startsWith(bStr)) {
                sourceUrl = sourceUrl.substring(bStr.length());
            }

            if(sourceUrl.endsWith("/")) {
                sourceUrl = sourceUrl.substring(0, sourceUrl.length()-1);
            }
            holder.txtSourceUrl.setText(sourceUrl);

            holder.textViewTitle.setText(title);
            holder.textViewNumPosts.setVisibility(View.GONE);
            if (null != imgUrl && imgUrl.length()!=0 && imgUrl.startsWith("http")) {
                Picasso.with(activity).load(imgUrl).into(holder.imgViewPhoto,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //putRandomPhotoTo(holder.imgViewPhoto);
                    }
                });
            }
            else{
                //putRandomPhotoTo(holder.imgViewPhoto);
            }
        }

        return convertView;
    }
/*
    private void putRandomPhotoTo(ImageView imgViewPhoto) {
        Random r = new Random();
        int k = r.nextInt(8)+1;
        if (k==1) Picasso.with(activity).load(R.drawable.first).into(imgViewPhoto);
        if (k==2) Picasso.with(activity).load(R.drawable.second).into(imgViewPhoto);
        if (k==3) Picasso.with(activity).load(R.drawable.third).into(imgViewPhoto);
        if (k==4) Picasso.with(activity).load(R.drawable.fourth).into(imgViewPhoto);
        if (k==5) Picasso.with(activity).load(R.drawable.fifth).into(imgViewPhoto);
        if (k==6) Picasso.with(activity).load(R.drawable.sixth).into(imgViewPhoto);
        if (k==7) Picasso.with(activity).load(R.drawable.seventh).into(imgViewPhoto);
        if (k==8) Picasso.with(activity).load(R.drawable.eight).into(imgViewPhoto);

    }*/




    private final void startIntent(NewsPost post){
        String url = post.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    class ViewHolder{
        TextView textViewTitle;
        TextView textViewNumPosts;
        TextView txtSourceUrl;
        TextView txtDescription;
        ImageView imgViewPhoto;
        TextView txtPubDate;
        int position;
    }
}
