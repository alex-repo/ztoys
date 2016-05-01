package com.ap.nn.model;

import com.ap.common.model.D2;
import com.ap.common.model.TreeContainer;
import com.ap.nn.core.AbstractLayerContainer;
import com.ap.nn.core.Nucleus;
import com.ap.nn.model.convolutional.ConvolutionalLayer;
import com.ap.nn.model.convolutional.D2Layer;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConvolutionalBackpropagation extends BackPropagation {

    @Override
    protected void updateHiddenLayers() {
        ArrayList layers = getNetwork().getChildrenList();
        IntStream.iterate(layers.size() - 2, f -> f - 1).limit(layers.size() - 2).forEachOrdered(i -> {
            AbstractLayerContainer al = (AbstractLayerContainer) layers.get(layers.size() - 2);
            ((Stream<Nucleus>) al.getChildrenStream().flatMap(
                    mapper -> ((TreeContainer<String, Nucleus>) mapper).getChildrenStream()))
                    .parallel().forEach(
                    n -> {
                        double neuronError = calculateError(n);
                        n.setError(neuronError);
                        if (layers.get(i) instanceof ConvolutionalLayer) {
                            updateNucleusGain(n);
                        }
                    });
        });
    }

    @Override
    protected double calculateError(Nucleus nucleus) {
        D2 d2 = ((D2Layer) nucleus.getParent()).getD2();
        return super.calculateError(nucleus) / d2.area();
    }
}
