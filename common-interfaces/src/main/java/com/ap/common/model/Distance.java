package com.ap.common.model;

public class Distance implements Comparable<Distance> {

    private EuclideanVector euclideanVector;
    private Double distance;

    Distance() {
    }

    public static Distance createDistance(EuclideanVector v, EuclideanVector to) {
        Distance d = new Distance();
        d.euclideanVector = v;
        d.distance = v.distance(to);
        return d;
    }

    @Override
    public int compareTo(Distance o) {
        if (o == null) {
            //TODO handle Exception
            throw new RuntimeException();
        }
        return distance.compareTo(o.distance);
    }

    public EuclideanVector getVector() {
        return euclideanVector;
    }

}
