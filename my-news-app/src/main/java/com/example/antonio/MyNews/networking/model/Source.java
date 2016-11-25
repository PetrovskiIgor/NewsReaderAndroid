package com.example.antonio.MyNews.networking.model;


/**
 * Model class representing a news source/provider.
 */
public class Source {
  private final String url;
  private final int id;
  private final String imgUrl;
  private final String name;

  public Source(String url, int id, String imgUrl, String name) {
    this.url = url;
    this.id = id;
    this.imgUrl = imgUrl;
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public int getId() {
    return id;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public String getName() {
    return name;
  }
}
