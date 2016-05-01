package com.ap.nn.model;

import com.ap.common.model.VectorInjectiveSpace;

import java.util.EventObject;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

abstract public class Iterative extends Learning {

    private double rate = 0.1d;
    private int currentIteration = 0;
    private int maxIterations = Integer.MAX_VALUE;
    private boolean iterationsLimited = false;

    static Function<Iterative, Boolean> breakOnMaxIterations = (i) -> {
        if (i.isIterationsLimited()) {
            if (i.getCurrentIteration() >= i.getMaxIterations()) {
                return true;
            }
        } else if (i.getCurrentIteration() == Integer.MAX_VALUE) {
            return true;
        }
        return false;
    };

    final static List<Function<Iterative, Boolean>> breaks = new CopyOnWriteArrayList<>();

    static {
        breaks.add(breakOnMaxIterations);
    }

    boolean isIterationsLimited() {
        return iterationsLimited;
    }

    Iterative iterationsLimited(boolean iterationsLimited) {
        this.iterationsLimited = iterationsLimited;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public Iterative rate(double rate) {
        this.rate = rate;
        return this;
    }

    public Iterative maxIterations(int iterations) {
        if (iterations > 0) {
            maxIterations = iterations;
            iterationsLimited(true);
        }
        return this;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    @Override
    final public void learn(VectorInjectiveSpace injectiveSpace) {
        neuralyzerPlus(injectiveSpace);
        while (!isBreaking()) {
            before();
            epoch(injectiveSpace);
            currentIteration++;
            after();
            fireEvent(new EventObject(this));
        }
        fireEvent(new EventObject(this));
    }

    @Override
    protected void neuralyzerPlus(VectorInjectiveSpace injectiveSpace) {
        setInjectiveSpace(injectiveSpace);
        resetError(injectiveSpace);
    }

    protected Boolean isBreaking() {
        Optional<Boolean> optional = breaks.stream().map(func -> func.apply(this)).reduce(Boolean::logicalOr);
        return optional.isPresent() ? optional.get() : false;
    }

    public void learn(VectorInjectiveSpace injectiveSpace, int maxIterations) {
        maxIterations(maxIterations);
        learn(injectiveSpace);
    }

    abstract public double getMaxError();

    abstract public OptionalDouble getTotalNetworkError();

    abstract public void resetError(VectorInjectiveSpace injectiveSpace);

    abstract public int getDeltaErrorCount();

    abstract public int getDeltaErrorLimit();

    abstract protected void after();

    abstract protected void before();

    abstract protected void epoch(VectorInjectiveSpace injectiveSpace);

    abstract protected double getDeltaError();

    abstract protected void setMaxError(double maxError);
}