package com.example.antonio.MyNews.networking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for similar news articles.
 */
public class Cluster {
  private final List<NewsPost> listNews;

  public Cluster() {
    this(new ArrayList<NewsPost>());
  }

  public Cluster(List<NewsPost> listNews) {
    this.listNews = listNews;
  }

  public List<NewsPost> getListNews() {
    return listNews;
  }
}
