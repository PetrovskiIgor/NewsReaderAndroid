package com.example.antonio.MyNews.networking.model;

/**
 * Representing a category/topic for news articles.
 */
public class Category {
  private final String title;
  private final int id;
  private final String imgUrl;
  private final String name;

  public Category(String title, int id, String imgUrl, String name) {
    this.title = title;
    this.id = id;
    this.imgUrl = imgUrl;
    this.name = name;
  }

  public String getTitle() {
    return title;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

}
