package com.teamwe.personalizedreader.model;

/**
 * Created by Petre on 2/1/2016.
 */
public class NewsPost {
    public String url;
    public String host_page;
    public String title;
    public String source_id;
    public String source_url;
    public String img_url;

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

    public NewsPost(){

    }

    public NewsPost(String url, String host_page, String title) {
        this.url = url;
        this.host_page = host_page;
        this.title = title;
    }
}
