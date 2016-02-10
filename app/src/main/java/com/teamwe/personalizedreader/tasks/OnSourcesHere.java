package com.teamwe.personalizedreader.tasks;

import com.teamwe.personalizedreader.model.Source;

import java.util.List;

/**
 * Created by igorpetrovski on 2/10/16.
 */
public interface OnSourcesHere {


    public void onTaskCompleted(List<Source> data);
}
