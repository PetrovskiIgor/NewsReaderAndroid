package com.example.antonio.MyNews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.antonio.MyNews.networking.MyNewsClient;
import com.example.antonio.MyNews.networking.model.Category;
import com.example.antonio.MyNews.networking.model.ClusterWrapper;
import com.example.antonio.MyNews.networking.model.NewsPostWrapper;
import com.example.antonio.MyNews.networking.model.Source;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
  private static final String LOG_TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.btnCategories).setOnClickListener(onClickListener);
    findViewById(R.id.btnSources).setOnClickListener(onClickListener);
    findViewById(R.id.btnNews).setOnClickListener(onClickListener);
    findViewById(R.id.btnNewsFromPage).setOnClickListener(onClickListener);
  }

  private final View.OnClickListener onClickListener = new View.OnClickListener() {
    @Override
    public void onClick(final View view) {
      final MyNewsClient client = MyNewsApp.getInstance().getMyNewsClient();
      switch (view.getId()) {
        case R.id.btnCategories:
          client.getCategories().enqueue(categoriesCallback);
          break;
        case R.id.btnSources:
          client.getSources().enqueue(sourcesCallback);
          break;
        case R.id.btnNews:
          client.getNews("10", "0", "21", null).enqueue(newsCallback);
          break;
        case R.id.btnNewsFromPage:
          client.getNewsFromPage("21").enqueue(newsFromPageCallback);
          break;
      }
    }
  };

  private final Callback<List<Category>> categoriesCallback = new Callback<List<Category>>() {
    @Override
    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
      Log.d(LOG_TAG, "onResponse: " + response.message());
    }

    @Override
    public void onFailure(Call<List<Category>> call, Throwable t) {
      Log.e(LOG_TAG, "onFailure: " + call.toString(), t);
    }
  };

  private final Callback<List<Source>> sourcesCallback = new Callback<List<Source>>() {
    @Override
    public void onResponse(Call<List<Source>> call, Response<List<Source>> response) {
      Log.d(LOG_TAG, "onResponse: " + response.message());
    }


    @Override
    public void onFailure(Call<List<Source>> call, Throwable t) {
      Log.e(LOG_TAG, "onFailure: " + call.toString(), t);
    }
  };

  private final Callback<ClusterWrapper> newsCallback = new Callback<ClusterWrapper>() {
    @Override
    public void onResponse(Call<ClusterWrapper> call, Response<ClusterWrapper> response) {
      Log.d(LOG_TAG, "onResponse: " + response.message());
    }

    @Override
    public void onFailure(Call<ClusterWrapper> call, Throwable t) {
      Log.e(LOG_TAG, "onFailure: " + call.toString(), t);
    }
  };

  private final Callback<NewsPostWrapper> newsFromPageCallback = new Callback<NewsPostWrapper>() {
    @Override
    public void onResponse(Call<NewsPostWrapper> call, Response<NewsPostWrapper> response) {
      Log.d(LOG_TAG, "onResponse: " + response.message());
    }

    @Override
    public void onFailure(Call<NewsPostWrapper> call, Throwable t) {
      Log.e(LOG_TAG, "onFailure: " + call.toString(), t);
    }
  };
}
