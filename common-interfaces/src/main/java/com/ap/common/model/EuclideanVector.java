package com.ap.common.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EuclideanVector extends ArrayList<Double> {

    private Cluster cluster;

    public Cluster getCluster() {
        return cluster;
    }

    public boolean moveTo(Cluster cluster) {
        if (cluster != null && !cluster.equals(this.cluster)) {
            if (this.cluster != null) this.cluster.remove(this);
            this.cluster = cluster;
            return true;
        } else {
            return false;
        }
    }

    public EuclideanVector() {
        super();
    }

    public EuclideanVector(int size) {
        super(size);
    }

    public EuclideanVector(double[] values) {
        super(Arrays.stream(values).mapToObj(m -> new Double(m)).collect(Collectors.toList()));
    }

    public EuclideanVector(Double[] values) {
        super(Arrays.stream(values).collect(Collectors.toList()));
    }

    public EuclideanVector(Stream<Double> doubleStream) {
        super(doubleStream.collect(Collectors.toList()));
    }

    public void set(Double[] values) {
        clear();
        addAll(Arrays.stream(values).collect(Collectors.toList()));
    }

    public Double[] toDoubleArray() {
        return toArray(new Double[]{});
    }

    public double[] asArray() {
        return stream().mapToDouble(d -> d).toArray();
    }

    public double magnitude() {
        return Math.sqrt(stream().mapToDouble(v -> v * v).sum());
    }

    public double distance(EuclideanVector vector) {
        if (vector.size() != size()) {
            // FIXME handle Exception
            throw new RuntimeException("Vectors dimension failure.");
        }
        return Math.sqrt(IntStream.range(0, size()).mapToDouble(i -> Math.pow(vector.get(i) - get(i), 2)).sum());
    }

    public static EuclideanVector average(EuclideanVector v1, EuclideanVector v2) {
        if (v1.size() != v2.size()) {
            // FIXME handle Exception
            throw new RuntimeException("Vectors dimension failure.");
        }
        return new EuclideanVector(IntStream.range(0, v1.size()).mapToObj(i -> (v1.get(i) + v2.get(i)) / 2));
    }

    public static double calculateSigma(EuclideanVector vector, EuclideanVector[] vectors) {
        double sigma = Arrays.stream(vectors).mapToDouble(v -> Math.pow(vector.distance(v), 2)).sum();
        return Math.sqrt(1 / ((double) vectors.length) * sigma);
    }

}
