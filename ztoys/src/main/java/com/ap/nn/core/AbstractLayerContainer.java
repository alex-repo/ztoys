package com.ap.nn.core;

import com.ap.common.core.Container;
import com.ap.common.core.TreeNode;
import com.ap.common.model.TreeContainer;

import java.util.stream.IntStream;

public abstract class AbstractLayerContainer<V extends TreeNode> extends TreeContainer<String, V> {

    private boolean bias = true;

    public AbstractLayerContainer(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    public AbstractLayerContainer(Class<? extends Container> containerClass, int size) {
        super(containerClass, size);
    }

    public AbstractLayerContainer(Class<? extends Container> containerClass, int nucleusCount, V genome) {
        super(containerClass, nucleusCount);

        IntStream.range(0, nucleusCount)./* parallel(). */forEach(i -> {
            try {
                add(cloneElement(genome));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isBias() {
        return bias;
    }

    public AbstractLayerContainer setBias(boolean bias) {
        this.bias = bias;
        return this;
    }

    protected final <T extends AbstractLayerContainer> AbstractLayerContainer connect(T to) {
        return to.connectLayer(this);
    }

    public AbstractLayerContainer<V> directConnect(AbstractLayerContainer<V> toLayer) {
        return directConnect(toLayer, 1);
    }

    public abstract void calculate();

    public abstract void reset();

    public abstract void initGains(double value);

    public abstract AbstractLayerContainer<V> addInput(V from);

    protected abstract AbstractLayerContainer<V> connectLayer(AbstractLayerContainer<V> layer);

    protected abstract AbstractLayerContainer connectLayer(AbstractLayerContainer<V> to, double gain);

    public abstract AbstractLayerContainer directConnect(AbstractLayerContainer<V> to, double gain);

}
