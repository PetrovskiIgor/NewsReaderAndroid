package com.example.antonio.MyNews.categories;

import android.util.Log;

import com.example.antonio.MyNews.MyNewsApp;
import com.example.antonio.MyNews.R;
import com.example.antonio.MyNews.networking.model.Category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesPresenter {
  private static final String LOG_TAG = CategoriesPresenter.class.getSimpleName();
  private final ChooseCategoriesActivity categoriesActivity;
  private final List<Category> categories;

  public CategoriesPresenter(ChooseCategoriesActivity activity) {
    this.categoriesActivity = activity;
    this.categories = new ArrayList<>(10);
  }

  protected void loadCategories() {
    MyNewsApp.getInstance().getMyNewsClient().getCategories().enqueue(categoriesCallback);
  }

  private final Callback<List<Category>> categoriesCallback = new Callback<List<Category>>() {
    private static final int ALLOWED_RETRIES = 5;
    private int retriesCount = 0;

    @Override
    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
      retriesCount = 0;
      if (response.isSuccessful()) {
        categoriesActivity.updateCategories(response.body());
      } else {
        categoriesActivity.showMessage(categoriesActivity.getString(R.string.connection_failed));
        try {
          Log.e(LOG_TAG, response.errorBody().string());
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    @Override
    public void onFailure(Call<List<Category>> call, Throwable t) {
      if (retriesCount++ < ALLOWED_RETRIES) {
        // retry
        call.enqueue(categoriesCallback);
      } else {
        categoriesActivity.showMessageWithAction(
                categoriesActivity.getString(R.string.connection_failed),
                categoriesActivity.getString(R.string.retry),
                retryRunnable);
      }
    }

    /** Runnable that will be run when the user click "Retry" on the message with action.*/
    private final Runnable retryRunnable = new Runnable() {
      @Override
      public void run() {
        retriesCount = 0;
        loadCategories();
      }
    };
  };

  public void onResume() {
    loadCategories();
  }

  public void onDestroy() {

  }
}
