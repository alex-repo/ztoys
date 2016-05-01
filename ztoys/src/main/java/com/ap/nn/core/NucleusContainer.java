package com.ap.nn.core;

import com.ap.common.core.Container;

import java.util.stream.IntStream;

public class NucleusContainer extends AbstractLayerContainer<Nucleus> {

    public NucleusContainer(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    public NucleusContainer(Class<? extends Container> containerClass, int size) {
        super(containerClass, size);
    }

    public NucleusContainer(Class<? extends Container> containerClass, int nucleusCount, Nucleus nucleargenome) {
        super(containerClass, nucleusCount, nucleargenome);

    }

    public void calculate() {
        getChildrenStream().parallel().forEach(Nucleus::calculate);
    }

    public void reset() {
        getChildrenStream().parallel().forEach(Nucleus::reset);
    }

    public void initGains(double value) {
        getChildrenStream().parallel().forEach(e -> e.initGains(value));
    }

    public NucleusContainer addInput(Nucleus from) {
        getChildrenStream().forEach(n -> n.addInput(from));
        return this;
    }

    private NucleusContainer addInput(AbstractLayerContainer<Nucleus> from) {
        from.getChildrenStream().forEach(this::addInput);
        return this;
    }

    protected AbstractLayerContainer connectLayer(AbstractLayerContainer layer) {
        layer.getChildrenStream().forEach(n -> {
            if (n instanceof Nucleus) {
            /* if (!(fromNucleus instanceof Bias)) */
                addInput((Nucleus) n);
            } else if (n instanceof AbstractLayerContainer) {
                addInput((AbstractLayerContainer) n);
            }
        });
        return this;
    }

    protected AbstractLayerContainer connectLayer(AbstractLayerContainer<Nucleus> to, double gain) {
        to.getChildrenStream().parallel().forEach(tn -> getChildrenStream().parallel().forEach(fn -> tn.addInput(fn, gain)));
        return this;
    }

    public AbstractLayerContainer directConnect(AbstractLayerContainer<Nucleus> to, double gain) {
        int length = Math.min(size(), to.size());
        IntStream.range(0, length).parallel().forEach(i -> to.get(i).addInput(get(i), gain));
        return this;
    }

    private NucleusContainer selfConnect() {
        return selfConnect(Double.NaN);
    }

    private NucleusContainer selfConnect(double gain) {
        getChildrenStream().parallel().forEach(n0 -> getChildrenStream().parallel().filter(n1 -> !n1.equals(n0)).forEach(n -> {
            if (gain != Double.NaN) {
                n0.addInput(n, gain);
            } else {
                n0.addInput(n);
            }
        }));
        return this;
    }
}