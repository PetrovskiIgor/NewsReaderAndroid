package com.teamwe.personalizedreader.tasks;

import com.teamwe.personalizedreader.model.Category;
import com.teamwe.personalizedreader.model.Source;

import java.util.List;

/**
 * Created by igorpetrovski on 6/14/16.
 */
public interface OnCategoriesHere {

    public void onTaskCompleted(List<Category> data);
}
