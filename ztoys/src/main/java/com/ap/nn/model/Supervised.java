package com.ap.nn.model;

import com.ap.common.model.EuclideanVector;
import com.ap.common.model.VectorInjective;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.common.util.CommonUtils;
import com.ap.common.function.DoubleSupplierConsumer;
import com.ap.common.function.MeanSquaredError;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

abstract public class Supervised extends Iterative {

    static Function<Iterative, Boolean> breakOnError = (i) -> {
        if (i.getTotalNetworkError().isPresent()) {
            if (i.getTotalNetworkError().getAsDouble() < i.getMaxError()) {
                return true;
            }
        }
        return false;
    };

    static Function<Iterative, Boolean> breakOnMinError =
            (i) -> i.getDeltaErrorCount() >= i.getDeltaErrorLimit();

    private DoubleSupplierConsumer error;

    static {
        breaks.add(breakOnError);
        breaks.add(breakOnMinError);
    }

    protected OptionalDouble totalNetworkError = OptionalDouble.empty();
    protected OptionalDouble previousEpochError = OptionalDouble.empty();

    private double maxError = 0.01d;
    private double deltaError = Double.POSITIVE_INFINITY;
    private int deltaErrorLimit = Integer.MAX_VALUE;
    private int deltaErrorCount = 0;

    public void learn(VectorInjectiveSpace injectiveSpace, double maxError) {
        this.maxError = maxError;
        learn(injectiveSpace);
    }

    public void learn(VectorInjectiveSpace injectiveSpace, double maxError, int maxIterations) {
        this.maxError = maxError;
        maxIterations(maxIterations);
        learn(injectiveSpace);
    }

    @Override
    protected void before() {
        error.reset();
    }

    @Override
    public void resetError(VectorInjectiveSpace injectiveSpace) {
        error = new MeanSquaredError(injectiveSpace.size());
    }

    @Override
    protected void after() {
        double absErrorChange = Math.abs((previousEpochError.isPresent() ? previousEpochError.getAsDouble() : 0)
                - (totalNetworkError.isPresent() ? totalNetworkError.getAsDouble() : 0));
        if (absErrorChange <= this.deltaError) {
            deltaErrorCount++;
        } else {
            deltaErrorCount = 0;
        }
    }

    @Override
    public void epoch(VectorInjectiveSpace injectiveSpace) {
        long ctime = System.currentTimeMillis();
        injectiveSpace.asStream().filter(p -> !isBreaking()).forEachOrdered(this::learnPattern);

        totalNetworkError = error.get();
        ctime = System.currentTimeMillis() - ctime;
        CommonUtils.getLogger(getClass()).debug("LearningEpoch Takes: " + ctime + "ms");
    }

    protected void learnPattern(VectorInjective injective) {

        getNetwork().setInput(injective.getInput());
        getNetwork().calculate();
        DoubleStream errorStream = calculateOutputError(injective.getOutput(), getNetwork().getOutputs());
        double[] err = errorStream.toArray();
        updateNetwork(err);
        error.accept(Arrays.stream(err));
    }

    protected DoubleStream calculateOutputError(EuclideanVector desiredOutput, EuclideanVector output) {
        return IntStream.range(0, output.size()).parallel().mapToDouble(
                i -> desiredOutput.get(i) - output.get(i));
    }

    @Override
    public void setMaxError(double maxError) {
        this.maxError = maxError;
    }

    @Override
    public double getMaxError() {
        return maxError;
    }

    @Override
    public OptionalDouble getTotalNetworkError() {
        return totalNetworkError;
    }

    @Override
    public double getDeltaError() {
        return deltaError;
    }

    @Override
    public int getDeltaErrorCount() {
        return deltaErrorCount;
    }

    @Override
    public int getDeltaErrorLimit() {
        return deltaErrorLimit;
    }

    abstract protected void updateNetwork(double[] ds);

}
