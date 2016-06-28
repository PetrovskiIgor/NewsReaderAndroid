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
import com.teamwe.personalizedreader.activities.SimilarNewsActivity;
import com.teamwe.personalizedreader.model.Cluster;
import com.teamwe.personalizedreader.model.NewsPost;
import com.teamwe.personalizedreader.GlobalInfo;
import com.teamwe.personalizedreader.mynews.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


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
            String title = cluster.listNews.get(0).title;
            String imgUrl = cluster.listNews.get(0).img_url;
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

            int numTexts = cluster.listNews.size();
            if (numTexts>1){
                String finalText = String.format("%d %s", numTexts-1, ((numTexts-1) > 1 )? "СЛИЧНИ ВЕСТИ":"СЛИЧНА ВЕСТ");
                holder.textViewNumPosts.setText(finalText);

                holder.textViewNumPosts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(activity, SimilarNewsActivity.class);
                        /*
                        da se pushti id-to na cluster-ot
                         */
                        ArrayList<NewsPost> similarNewsPosts = (ArrayList<NewsPost>)cluster.listNews;
                        intent.putParcelableArrayListExtra(GlobalInfo.LIST_NEWS, similarNewsPosts);
                        activity.startActivity(intent);
                    }
                });
            }
            else{
                holder.textViewNumPosts.setVisibility(View.GONE);
                //holder.textViewNumPosts.setText("");

            }

            if (null != imgUrl && imgUrl.length()!=0 && imgUrl.startsWith("http")) {
                Picasso.with(activity).load(imgUrl).into(holder.imgViewPhoto,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        putRandomPhotoTo(holder.imgViewPhoto);
                    }
                });
            }
            else{
                putRandomPhotoTo(holder.imgViewPhoto);
            }
        }

        return convertView;
    }

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
        
    }

    public void startDialog(Cluster cluster){

        if (cluster.listNews.size()==1){
            startIntent(cluster.listNews.get(0));
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String [] arr = new String[cluster.listNews.size()];
        final HashMap<String, NewsPost> hostPageVsPost = new HashMap<String, NewsPost> ();
        for (int i=0;i<arr.length;i++){
            arr[i]=cluster.listNews.get(i).getSource_url();
            hostPageVsPost.put(arr[i],cluster.listNews.get(i));
        }
        builder.setTitle(R.string.pick_news).setItems(arr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String hostPage = arr[which];
                NewsPost post = hostPageVsPost.get(hostPage);
                startIntent(post);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private final void startIntent(NewsPost post){
        String url = post.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
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
}
