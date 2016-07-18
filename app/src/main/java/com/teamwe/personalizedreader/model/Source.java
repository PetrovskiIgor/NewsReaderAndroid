package com.teamwe.personalizedreader.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Source implements Parcelable {

    private int id;
    private String url;
    private String name;
    private boolean isChecked;

    // usually the favicon of the web page
    private String imgUrl;

    public Source(Parcel input){

        id = input.readInt();
        url = input.readString();
        name = input.readString();
        isChecked = (input.readInt() == 1);
        imgUrl = input.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeString(name);
        dest.writeInt(isChecked?1:0);
        dest.writeString(imgUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Source> CREATOR = new Parcelable.Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public Source(int id,String url, boolean isChecked) {
        this.id = id;
        this.url = url;
        this.isChecked = isChecked;
    }

    public Source(int id, String url) {
        this.id = id;
        this.url = url;
        this.isChecked = true;
    }

    public Source() {
        this.isChecked = true;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getPrettyUrl() {
        String prettyUrl = url;

        String check = "http://";

        if(prettyUrl.startsWith(check)) {
            prettyUrl = prettyUrl.substring(check.length());
        }

        check = "www.";

        if(prettyUrl.startsWith(check)) {
            prettyUrl = prettyUrl.substring(check.length());
        }

        if(prettyUrl.endsWith("/")) {
            prettyUrl = prettyUrl.substring(0, prettyUrl.length()-1);
        }


        return prettyUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        if(name == null || name.length() == 0) {
            return this.getPrettyUrl();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
