package com.teamwe.personalizedreader.model;


import android.os.Parcel;
import android.os.Parcelable;

public class NewsPost implements Parcelable {
    public String url;
    public String host_page;
    public String title;
    public String source_id;
    public String source_url;
    public String img_url;
    public String description;
    public long pubDate;

    public String getHost_page() {
        return host_page;
    }

    public String getSource_id() {
        return source_id;
    }

    public String getSource_url() {
        return source_url;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getUrl() {return url;}
    public String getTitle() {return title;}

    public NewsPost(Parcel input){

        url = input.readString();
        host_page = input.readString();
        title = input.readString();
        source_id = input.readString();
        source_url = input.readString();
        img_url = input.readString();
        description = input.readString();


    }

    public NewsPost(String url, String host_page, String title) {
        this.url = url;
        this.host_page = host_page;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(host_page);
        dest.writeString(title);
        dest.writeString(source_id);
        dest.writeString(source_url);
        dest.writeString(img_url);
        dest.writeString(description);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NewsPost> CREATOR = new Parcelable.Creator<NewsPost>() {
        @Override
        public NewsPost createFromParcel(Parcel in) {
            return new NewsPost(in);
        }

        @Override
        public NewsPost[] newArray(int size) {
            return new NewsPost[size];
        }
    };
}