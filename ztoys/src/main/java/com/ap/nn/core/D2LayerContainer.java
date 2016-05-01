package com.ap.nn.core;

import com.ap.common.core.Container;
import com.ap.nn.model.convolutional.D2Layer;

import java.util.stream.IntStream;

public class D2LayerContainer extends AbstractLayerContainer<D2Layer> {

    public D2LayerContainer(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    public D2LayerContainer(Class<? extends Container> containerClass, int size) {
        super(containerClass, size);
    }

    public D2LayerContainer(Class<? extends Container> containerClass, int nucleusCount, D2Layer genome) {// ?
        super(containerClass, nucleusCount);

        IntStream.range(0, nucleusCount)./* parallel(). */forEach(i -> {
            try {
                add(cloneElement(genome));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void calculate() {
        getChildrenStream().parallel().forEach(NucleusContainer::calculate);
    }

    public void reset() {
        getChildrenStream().parallel().forEach(NucleusContainer::reset);
    }

    public void initGains(double value) {
        getChildrenStream().parallel().forEach(e -> e.initGains(value));
    }

    public AbstractLayerContainer addInput(D2Layer from) {
        getChildrenStream().forEach(d2t -> d2t.addInput(from));
        return this;
    }

    protected AbstractLayerContainer connectLayer(AbstractLayerContainer<D2Layer> layer) {
        // FIXME skip Bias: for each if (!(fromNucleus instanceof Bias))
        layer.getChildrenStream().forEach(this::addInput);
        return this;
    }

    protected AbstractLayerContainer connectLayer(AbstractLayerContainer<D2Layer> to, double gain) {
        to.getChildrenStream().parallel().forEach(td2 -> getChildrenStream().parallel().forEach(fd2 -> td2.addInput(fd2, gain)));
        return this;
    }

    public AbstractLayerContainer directConnect(AbstractLayerContainer<D2Layer> to, double gain) {
        int length = Math.min(size(), to.size());
        IntStream.range(0, length).parallel().forEach(i -> to.get(i).addInput(get(i), gain));
        return this;
    }

    private D2LayerContainer selfConnect() {
        return selfConnect(Double.NaN);
    }

    private D2LayerContainer selfConnect(double gain) {
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
