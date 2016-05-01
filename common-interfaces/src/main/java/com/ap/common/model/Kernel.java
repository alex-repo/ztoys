package com.ap.common.model;

public class Kernel extends D2 {

    private double[] factormatrix;
    private double fmsum;

    public Kernel(int width, int height) {
        super(width, height);
    }

    public Kernel(int width, int height, double... factors) {
        this(width, height);
        fill(factors);
    }

    private void fill(double[] factors) {
        factormatrix = new double[width * height];
        int length = factormatrix.length < factors.length ? factormatrix.length : factors.length;
        System.arraycopy(factors, 0, factormatrix, 0, length);
        for (int i = length; i < factormatrix.length; i++) {
            factormatrix[i] = 0;
        }
        for (int i = 0; i < factormatrix.length; i++) {
            fmsum += factormatrix[i];
        }
    }

    public double getFmsum() {
        return fmsum;
    }

    public double convolve(double[] x) {
        int length = factormatrix.length < x.length ? factormatrix.length : x.length;
        double summ = 0;
        for (int i = 0; i < length; i++) {
            summ += x[i] * factormatrix[i];
        }
        return fmsum == 0 ? summ : summ / fmsum;
    }

    public int getArea() {
        return width * height;
    }

}