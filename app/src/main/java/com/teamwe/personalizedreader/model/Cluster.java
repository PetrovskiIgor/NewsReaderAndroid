package com.teamwe.personalizedreader.model;

import com.teamwe.personalizedreader.model.NewsPost;

import java.util.List;


public class Cluster {

    public List<NewsPost> listNews;
    public String category;

    public Cluster(){

    }

    public Cluster(List<NewsPost> listNews) {
        this.listNews = listNews;
    }
}
