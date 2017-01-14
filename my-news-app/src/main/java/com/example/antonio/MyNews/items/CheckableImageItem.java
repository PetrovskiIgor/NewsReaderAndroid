package com.example.antonio.MyNews.items;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.antonio.MyNews.R;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class CheckableImageItem extends AbstractItem<CheckableImageItem, CheckableImageItem.VH> {
  private final Drawable drawable;
  private Object model;

  public CheckableImageItem(Drawable drawable) {
    this.drawable = drawable;
  }

  public CheckableImageItem(Drawable drawable, Object model) {
    this(drawable);
    this.model = model;
  }

  @Override
  public int getType() {
    return R.id.checkable_image_list_item;

  }

  @Override
  public int getLayoutRes() {
    return R.layout.checkable_image_list_item;
  }

  @Override
  public void bindView(VH holder, List<Object> payloads) {
    super.bindView(holder, payloads);
    holder.imageView.setImageDrawable(drawable);
  }

  public Object getModel() {
    return model;
  }

  @Override
  public void unbindView(VH holder) {
    super.unbindView(holder);
  }

  public static class VH extends RecyclerView.ViewHolder {
    ImageView imageView;

    public VH(View itemView) {
      super(itemView);
      imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
  }
}
