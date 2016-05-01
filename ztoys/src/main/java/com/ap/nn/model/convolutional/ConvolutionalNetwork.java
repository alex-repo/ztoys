package com.ap.nn.model.convolutional;

import com.ap.common.core.AbstractContainer;
import com.ap.common.core.Container;
import com.ap.common.model.EuclideanVector;
import com.ap.nn.core.NetworkContainer;
import com.ap.nn.model.Bias;

public class ConvolutionalNetwork extends NetworkContainer {

    private int current;

    public ConvolutionalNetwork(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    @Override
    public void setInput(EuclideanVector inputVector) {
        FeatureMapsLayer inputLayer = (FeatureMapsLayer) get(0);
        current = 0;
        inputLayer.getFeatureMap().stream().flatMap(AbstractContainer::asStream).filter(p -> !(p instanceof Bias))
                .forEachOrdered(n -> n.setInput(inputVector.get(current++)));
    }
}