package com.example.antonio.MyNews.networking.model;

import java.util.List;

/**
 * Container model for multiple news posts.
 */
public class NewsPostWrapper {
  private final List<NewsPost> listNewsPosts;

  public NewsPostWrapper(List<NewsPost> listNewsPosts) {
    this.listNewsPosts = listNewsPosts;
  }

  public List<NewsPost> getListNewsPosts() {
    return listNewsPosts;
  }
}
