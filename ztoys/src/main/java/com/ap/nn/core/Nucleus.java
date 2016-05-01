package com.ap.nn.core;

import com.ap.common.core.DoubleCollector;
import com.ap.common.core.Identified;
import com.ap.common.core.TreeNode;
import com.ap.common.function.DifferentiableFunction;
import com.ap.common.function.Step;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Nucleus extends Identified<String> implements TreeNode, Serializable {

    private ArrayList<Sinapse> dendrites = new ArrayList<>();
    private ArrayList<Sinapse> axons = new ArrayList<>();

    private AbstractLayerContainer layer;

    public AbstractLayerContainer getParent() {
        return this.layer;
    }

    private transient double input = 0;

    public void setInput(double input) {
        this.input = input;
    }

    public double getInput() {
        return input;
    }

    private transient double output = 0;

    public void setOutput(double output) {
        this.output = output;
    }

    public double getOutput() {
        return output;
    }

    public Double getOutputDouble() {
        return output;
    }

    private DoubleCollector<Sinapse> dendritesCollector = new WSumDoubleCollector();

    protected DoubleCollector<Sinapse> getDendritesCollector() {
        return dendritesCollector;
    }

    protected void setDendritesCollector(DoubleCollector<Sinapse> dendritesCollector) {
        this.dendritesCollector = dendritesCollector;
    }

    private DifferentiableFunction axonFunction = new Step();

    public DifferentiableFunction getAxonFunction() {
        return axonFunction;
    }

    public void setAxonFunction(DifferentiableFunction axonFunction) {
        this.axonFunction = axonFunction;
    }

    protected transient double error = 0;

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public void reset() {
        setInput(0);
        setOutput(0);
    }

    public Nucleus() {
    }

    public Nucleus(DifferentiableFunction axonFunction) {
        this.axonFunction = axonFunction;
    }

    public Nucleus(DoubleCollector<Sinapse> dendritesCollector, DifferentiableFunction axonFunction) {
        this.dendritesCollector = dendritesCollector;
        this.axonFunction = axonFunction;
    }

    public void calculate() {
        if ((dendrites.size() > 0)) {
            input = dendrites.stream().collect(new WSumDoubleCollector());
        }
        output = axonFunction.apply(input).getAsDouble();
    }

    public boolean hasInputs() {
        return !dendrites.isEmpty();
    }

    public boolean hasAxonsTo(TreeNode nucleus) {
        for (Sinapse sinapse : axons) {
            if (sinapse.getParent() == nucleus) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDendritesFrom(Nucleus nucleus) {
        for (Sinapse sinapse : dendrites) {
            if (sinapse.getFrom() == nucleus) {
                return true;
            }
        }
        return false;
    }

    public Sinapse addInput(Sinapse sinapse) {
        if (sinapse == null || sinapse.getParent() != this || hasDendritesFrom(sinapse.getFrom())) {
            return null;
        }
        if (dendrites.add(sinapse)) {
            return sinapse.getFrom().addOutput(sinapse);
        } else {
            return null;
        }
    }

    public Sinapse addInput(Nucleus nucleus, double gain) {
        Sinapse sinapse = new Sinapse(nucleus, this, gain);
        return addInput(sinapse);
    }

    public Sinapse addInput(Nucleus nucleus) {
        Sinapse sinapse = new Sinapse(nucleus, this);
        return addInput(sinapse);
    }

    protected Sinapse addOutput(Sinapse sinapse) {
        if (sinapse == null || sinapse.getFrom() != this || hasAxonsTo(sinapse.getParent())) {
            return null;
        }
        if (axons.add(sinapse)) {
            return sinapse;
        } else {
            return null;
        }
    }

    public ArrayList<Sinapse> getChildrenList() {
        return getDendrites();
    }

    public final ArrayList<Sinapse> getDendrites() {
        return dendrites;
    }

    public final ArrayList<Sinapse> getAxonS() {
        return axons;
    }

    public void initGains(double value) {
        for (Sinapse sinapse : this.dendrites) {
            sinapse.get().accept(value);
        }
    }

    @Override
    public Stream getChildrenStream() {
        return dendrites.stream();
    }

    @Override
    public void setParent(TreeNode tree) {
        this.layer = (AbstractLayerContainer) tree;

    }
}
