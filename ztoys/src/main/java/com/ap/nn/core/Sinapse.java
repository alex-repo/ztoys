package com.ap.nn.core;

import com.ap.common.core.TreeNode;
import com.ap.common.model.Gain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.DoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Sinapse implements Supplier<Gain>, DoubleConsumer, TreeNode, Serializable {

    private Nucleus from;
    private Nucleus to;
    private Gain gain = new Gain();

    public Sinapse(Nucleus from, Nucleus to) {
        this.from = from;
        this.to = to;
    }

    public Sinapse(Nucleus from, Nucleus to, Gain gain) {
        this.from = from;
        this.to = to;
        this.gain = gain;
    }

    public Sinapse(Nucleus from, Nucleus to, double gain) {
        this.from = from;
        this.to = to;
        this.gain = new Gain(gain);
    }

    public static Sinapse createSinapse(Nucleus from, Nucleus to, Gain gain) {
        Sinapse sinapse = new Sinapse(from, to, gain);
        return to.addInput(sinapse);
    }


    public void setGain(Gain gain) {
        this.gain = gain;
    }

    public double getInput() {
        return from.getOutput();
    }

    public double getWeightedInput() {
        return from.getOutput() * gain.getAsDouble();
    }

    public Nucleus getFrom() {
        return from;
    }

    public void setFrom(Nucleus from) {
        this.from = from;
    }

    public void setToNucleus(Nucleus to) {
        this.to = to;
    }

    @Override
    public void accept(double value) {
        get().accept(value);
    }

    @Override
    public Gain get() {
        return gain;
    }

    @Override
    public ArrayList<TreeNode> getChildrenList() {
        return new ArrayList<>();
    }

    @Override
    public Nucleus getParent() {
        return to;
    }

    @Override
    public Stream getChildrenStream() {
        return new ArrayList<TreeNode>().stream();
    }

    @Override
    public void setParent(TreeNode tree) {
    }
}
