package com.teamwe.personalizedreader.model;

import com.teamwe.personalizedreader.model.NewsPost;

import java.util.ArrayList;
import java.util.List;


public class Cluster {

    public List<NewsPost> listNews;
    public int numNews;


    public Cluster(){
        this.listNews = new ArrayList<NewsPost>();

    }

    public Cluster(List<NewsPost> listNews) {
        this.listNews = listNews;
    }
}
