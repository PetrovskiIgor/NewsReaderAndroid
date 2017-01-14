package com.example.antonio.MyNews.networking.model;

import java.util.List;

/**
 * Container for multiple clusters.
 */
public class ClusterWrapper {
  private final List<Cluster> listClusters;

  public ClusterWrapper(List<Cluster> listClusters) {
    this.listClusters = listClusters;
  }

  public List<Cluster> getListClusters() {
    return listClusters;
  }
}
