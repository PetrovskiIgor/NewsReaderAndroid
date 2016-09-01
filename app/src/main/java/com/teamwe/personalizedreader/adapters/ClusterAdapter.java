package com.teamwe.personalizedreader.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamwe.personalizedreader.activities.NewsPostActivity;
import com.teamwe.personalizedreader.activities.SimilarNewsActivity;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;


public class ClusterAdapter extends BaseAdapter {

    private static final String TAG = "ClusterAdapter";
    private Activity activity;
    private List<Cluster> clusters;

    public ClusterAdapter(Activity activity, List<Cluster> clusters){
        super();
        this.activity = activity;
        this.clusters = clusters;
    }


    public Cluster getCluster(int position) {
        return clusters.get(position);
    }

    @Override
    public int getCount() {
        if (clusters!=null)
            return clusters.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (position<getCount())
            return clusters.get(position);
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

        final Cluster cluster = this.clusters.get(position);

        if (cluster!=null){
            holder.position=position;

            NewsPost firstNews = cluster.listNews.get(0);

            String title = firstNews.title;
            String imgUrl = "";

            for(NewsPost np : cluster.listNews) {

                if(np.img_url != null && np.img_url.length() > 0 && np.img_url.startsWith("http")) {
                    imgUrl = np.img_url;
                    break;
                }
            }


            String sourceUrl = cluster.listNews.get(0).source_url;
            long pubDate = cluster.listNews.get(0).pubDate;
            String description = cluster.listNews.get(0).description;


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

            int numTexts = cluster.numNews;
            if (numTexts>1){
                holder.textViewNumPosts.setVisibility(View.VISIBLE);
                String finalText = String.format("%d %s", numTexts,"вести");
                holder.textViewNumPosts.setText(finalText);

                holder.textViewNumPosts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showSimilarNews(cluster.listNews);

                    }
                });

                holder.imgViewPhoto.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        showSimilarNews(cluster.listNews);
                    }
                });
                //holder.textViewNumPosts.setPaintFlags(holder.textViewNumPosts.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else{
                holder.textViewNumPosts.setVisibility(View.GONE);
                holder.imgViewPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NewsPost onlyNewsPost = cluster.listNews.get(0);
                        showNewsPost(onlyNewsPost);
                    }
                });
                //holder.textViewNumPosts.setText("");

            }

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



    class ViewHolder{
        TextView textViewTitle;
        TextView textViewNumPosts;
        TextView txtDescription;
        TextView txtSourceUrl;
        TextView txtPubDate;
        ImageView imgViewPhoto;
        int position;
    }

    private void showSimilarNews(List<NewsPost> newsPosts) {
        Intent intent = new Intent(activity, SimilarNewsActivity.class);
                        /*
                        da se pushti id-to na cluster-ot
                         */
        ArrayList<NewsPost> similarNewsPosts = (ArrayList<NewsPost>) newsPosts;
        intent.putParcelableArrayListExtra(GlobalInfo.LIST_NEWS, similarNewsPosts);
        activity.startActivity(intent);
    }

    private void showNewsPost(NewsPost newsPost) {
        String url = newsPost.getUrl();

        Intent intent = new Intent(activity, NewsPostActivity.class);
        intent.putExtra(GlobalInfo.NEWS_URL, url);
        intent.putExtra(GlobalInfo.NEWS_TITLE, newsPost.getTitle());
        activity.startActivity(intent);
    }
}
