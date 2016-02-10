package com.teamwe.personalizedreader.tasks;

import com.teamwe.personalizedreader.model.Cluster;

import java.util.List;

/**
 * Created by Petre on 2/3/2016.
 */
public interface OnNewsHere {
    public void onTaskCompleted(List<Cluster> list);
}
