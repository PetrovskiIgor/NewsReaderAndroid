package com.teamwe.personalizedreader.tasks;

import com.teamwe.personalizedreader.model.NewsPost;

import java.util.List;

/**
 * Created by igorpetrovski on 6/17/16.
 */
public interface OnNewsFromSourceHere {

    public void onTaskCompleted(List<NewsPost> list);
}
