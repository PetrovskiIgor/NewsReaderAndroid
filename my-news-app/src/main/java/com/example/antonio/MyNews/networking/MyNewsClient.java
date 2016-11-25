package com.example.antonio.MyNews.networking;

import android.support.annotation.NonNull;

import com.example.antonio.MyNews.networking.model.Category;
import com.example.antonio.MyNews.networking.model.ClusterWrapper;
import com.example.antonio.MyNews.networking.model.NewsPostWrapper;
import com.example.antonio.MyNews.networking.model.Source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * {@link retrofit2.Retrofit} client interface.
 */
public interface MyNewsClient {

  /**
   * Gets the categories that are available.
   */
  @GET("/get_categories?format=json")
  Call<List<Category>> getCategories();

  /**
   * Gets the sources/providers that are available.
   */
  @GET("/get_sources?format=json")
  Call<List<Source>> getSources();


  /**
   * Gets the clusters based on the provided query parameters.
   *
   * @param howMuch            the number of news that will be returned
   * @param category           the category to fetch the news from
   * @param selectedSources    the sources that should be queried for news
   * @param unwantedCategories the sources that will be explicitly excluded
   */
  @GET("/read_clusters?format=json")
  Call<ClusterWrapper> getNews(
          @Query("how_much") String howMuch,
          @Query("category") String category,
          @Query("selected_sources") String selectedSources,
          @Query("unwanted_categories") String unwantedCategories);

  /**
   * Gets the news articles for the provided source. Note: This returns all categories.
   *
   * @param sourceId the source that will provide the news.
   */
  @GET("/get_news_from_page?format=json")
  Call<NewsPostWrapper> getNewsFromPage(@Query("source_id") @NonNull String sourceId);
}
