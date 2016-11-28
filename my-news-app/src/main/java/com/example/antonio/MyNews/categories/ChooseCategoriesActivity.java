package com.example.antonio.MyNews.categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.antonio.MyNews.R;
import com.example.antonio.MyNews.items.CheckableImageItem;
import com.example.antonio.MyNews.networking.model.Category;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISelectionListener;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseCategoriesActivity extends AppCompatActivity {
  private static final String LOG_TAG = ChooseCategoriesActivity.class.getSimpleName();

  private View rootView;
  private FastItemAdapter<IItem> adapter;
  private RecyclerView recyclerView;
  private CategoriesPresenter categoriesPresenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    categoriesPresenter = new CategoriesPresenter(this);

    setContentView(R.layout.recyclerview_layout);
    rootView = findViewById(R.id.root_view);

    recyclerView = (RecyclerView) findViewById(R.id.rvContent);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    adapter = new FastItemAdapter<>();
    adapter.withSelectable(true).withMultiSelect(true).withSelectionListener(new ISelectionListener<IItem>() {
      @Override
      public void onSelectionChanged(IItem item, boolean selected) {
        Log.d(LOG_TAG, "onSelectionChanged: " + item.toString());
      }
    });
    recyclerView.setAdapter(adapter);
  }

  @Override
  protected void onResume() {
    super.onResume();
    categoriesPresenter.onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    categoriesPresenter.onDestroy();
  }

  public void showMessage(String message) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
  }

  public void showMessageWithAction(String message, String actionText, final Runnable runnable) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
            .setAction(actionText, new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                runnable.run();
              }
            })
            .show();
  }

  public void updateCategories(@NonNull List<Category> categories) {
    adapter.setNewList(buildItems(categories));
  }

  @NonNull
  private List<IItem> buildItems(@NonNull List<Category> categories) {
    List<IItem> items = new ArrayList<>(categories.size());
    for (Category category : categories) {
      Drawable drawable;
      try {
        drawable = Drawable.createFromStream(getAssets().open("categories/" + category.getId() + ".png"), null);
      } catch (IOException e) {
        drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
      }
      items.add(new CheckableImageItem(drawable, category));
    }
    return items;
  }

  public static void start(Context context) {
    Intent starter = new Intent(context, ChooseCategoriesActivity.class);
    context.startActivity(starter);
  }
}
