package com.example.antonio.MyNews.networking.model;


/**
 * Simple model representing one news article.
 */
public class NewsPost {
  private final String description;
  private final long pubDate;
  private final String title;
  private final String url;
  private final String source_url;
  private final String source_id;
  private final String img_url;

  public NewsPost(String description, long pubDate, String title, String url, String source_url,
                  String source_id, String img_url) {
    this.description = description;
    this.pubDate = pubDate;
    this.title = title;
    this.url = url;
    this.source_url = source_url;
    this.source_id = source_id;
    this.img_url = img_url;
  }

  public String getDescription() {
    return description;
  }

  public long getPubDate() {
    return pubDate;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public String getSource_url() {
    return source_url;
  }

  public String getSource_id() {
    return source_id;
  }

  public String getImg_url() {
    return img_url;
  }
}