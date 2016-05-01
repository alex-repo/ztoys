package com.ap.common.model;

import java.util.ArrayList;
import java.util.Optional;

public class Cluster extends ArrayList<EuclideanVector> {

    protected EuclideanVector centroid = null;

    public EuclideanVector centroid() {
        Optional<EuclideanVector> opt = stream().reduce(EuclideanVector::average);
        if (opt.isPresent()) {
            return centroid = opt.get();
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Cluster))
            return false;
        if (((Cluster) obj).centroid() == null || centroid() == null) {
            return false;
        }
        return centroid().equals(((Cluster) obj).centroid());
    }

    @Override
    public boolean add(EuclideanVector vector) {
        if (vector.getCluster() != this) {
            vector.moveTo(this);
            if (super.add(vector)) {
                return true;
            }
        }
        return false;
    }

    public EuclideanVector[] kNearestNeighbours(EuclideanVector vector, int k) {
        return stream().map(v -> Distance.createDistance(v, vector)).sorted().limit(k).map(d -> d.getVector()).toArray(EuclideanVector[]::new);
    }

}
