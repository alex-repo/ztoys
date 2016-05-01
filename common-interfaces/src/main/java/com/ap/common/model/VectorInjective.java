package com.ap.common.model;

import com.ap.common.core.Identified;

import java.io.Serializable;

public class VectorInjective extends Identified<Integer> implements Serializable {

    private EuclideanVector input;
    private EuclideanVector output;

    public VectorInjective(double[] input, double[] output) {
        this.input = new EuclideanVector(input);
        this.output = new EuclideanVector(output);
    }

    public VectorInjective(EuclideanVector input, EuclideanVector output) {
        this.input = input;
        this.output = output;
    }

    public VectorInjective(EuclideanVector input) {
        this.input = input;
    }

    public VectorInjective(double[] input) {
        this.input = new EuclideanVector(input);
    }

    public EuclideanVector getInput() {
        return this.input;
    }

    public void setInput(double[] input) {
        this.input = new EuclideanVector(input);
    }

    public void setInput(EuclideanVector input) {
        this.input = input;
    }

    public EuclideanVector getOutput() {
        return output;
    }

    public void setOutput(EuclideanVector desired) {
        this.output = desired;
    }

    public void setOutput(double[] desired) {
        this.output = new EuclideanVector(desired);
    }

    public boolean isInjective() {
        return (output == null ? false : true);
    }

}