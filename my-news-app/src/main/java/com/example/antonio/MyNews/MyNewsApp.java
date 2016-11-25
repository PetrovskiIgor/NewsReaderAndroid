package com.example.antonio.MyNews;

import android.app.Application;

import com.example.antonio.MyNews.networking.MyNewsClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyNewsApp extends Application {
  public static final String API_BASE_URL = "http://mojpersonalizirancitac.appspot.com";
  private static MyNewsApp instance;

  private MyNewsClient myNewsClient;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
  }

  public MyNewsClient getMyNewsClient() {
    if (myNewsClient == null) {
      myNewsClient = new Retrofit.Builder()
              .baseUrl(API_BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build().create(MyNewsClient.class);
    }
    return myNewsClient;
  }

  public static MyNewsApp getInstance() {
    return instance;
  }
}
