package com.ap.common.model;

import com.ap.common.core.AbstractContainer;
import com.ap.common.core.Container;

import java.util.ArrayList;
import java.util.Collections;

public class ClusterSpace extends AbstractContainer<String, Cluster> {

    private EuclideanVector[] dataVectors;

    private int numberOfClusters;

    public ClusterSpace(Class<? extends Container> containerClass, VectorInjectiveSpace injectiveSpace) {

        super(containerClass, injectiveSpace.size());
        if (injectiveSpace.size() < 1) {
            throw new RuntimeException();
        }
        this.numberOfClusters = injectiveSpace.size();
        init(injectiveSpace);
        randomizeCentroids();
        doClustering();
    }

    public ClusterSpace(Class<? extends Container> containerClass, VectorInjectiveSpace injectiveSpace, int numberOfClusters) {
        super(containerClass, numberOfClusters);
        if (numberOfClusters < 1) {
            throw new RuntimeException();
        }
        this.numberOfClusters = numberOfClusters;
        init(injectiveSpace);
        randomizeCentroids();
        doClustering();
    }

    void init(VectorInjectiveSpace injectiveSpace) {
        this.dataVectors = new EuclideanVector[injectiveSpace.size()];
        int i = 0;
        for (VectorInjective vectorInjective : injectiveSpace.asList()) {
            EuclideanVector vector = vectorInjective.getInput();
            this.dataVectors[i] = vector;
            i++;
        }
    }

    public void randomizeCentroids() {
        ArrayList<Integer> idxList = new ArrayList<>();
        for (int i = 0; i < dataVectors.length; i++) {
            idxList.add(i);
        }
        Collections.shuffle(idxList);
        for (int i = 0; i < numberOfClusters; i++) {
            Cluster cluster = new Cluster();
            cluster.centroid = dataVectors[idxList.get(i)];
            add(cluster);
        }
    }

    public void doClustering() {
        for (EuclideanVector vector : dataVectors) {
            Cluster nearestCluster = getNearestCluster(vector);
            nearestCluster.add(vector);
        }
        boolean clustersChanged;
        do {
            clustersChanged = false;
            recalculateCentroids();
            for (EuclideanVector vector : dataVectors) {
                Cluster nearestCluster = getNearestCluster(vector);
                if (!vector.getCluster().equals(nearestCluster)) {
                    nearestCluster.add(vector);
                    clustersChanged = true;
                }
            }
        } while (clustersChanged);
    }

    private Cluster getNearestCluster(EuclideanVector vector) {
        Cluster nearestCluster = null;
        double minimumDistanceFromCluster = Double.MAX_VALUE;
        double distanceFromCluster = 0;
        for (Cluster cluster : asList()) {
            distanceFromCluster = vector.distance(cluster.centroid);
            if (distanceFromCluster < minimumDistanceFromCluster) {
                minimumDistanceFromCluster = distanceFromCluster;
                nearestCluster = cluster;
            }
        }
        return nearestCluster;
    }

    private void recalculateCentroids() {
        for (Cluster cluster : asList()) {
            if (cluster.size() > 0) {
                cluster.centroid();
            }
        }
    }
}