package com.ap.nn.model.convolutional;

import com.ap.common.core.Container;
import com.ap.common.model.D2;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.NucleusContainer;

import java.util.stream.IntStream;

public class D2Layer extends NucleusContainer {

    private D2 d2;

    public D2Layer(Class<? extends Container> containerClass, D2 d2, Nucleus nucleargenome) {
        super(containerClass, d2.getHeight() * d2.getWidth(), nucleargenome);
        this.d2 = d2;
    }

    public int getWidth() {
        return getD2().getWidth();
    }

    public int getHeight() {
        return getD2().getHeight();
    }

    public D2 getD2() {
        return d2;
    }

    public Nucleus getNucleus(int x, int y) {
        return get(x + y * (getD2().getWidth()));
    }

    public D2Layer addInput(D2Layer from) {
        from.getChildrenStream().forEach(this::addInput);
        return this;
    }

    public D2Layer addInput(D2Layer from, double gain) {
        int length = Math.min(size(), size());
        IntStream.range(0, length).parallel().forEach(i -> get(i).addInput(from.get(i), gain));
        return this;
    }
}
