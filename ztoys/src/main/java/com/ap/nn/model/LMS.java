package com.ap.nn.model;

import com.ap.nn.core.AbstractLayerContainer;
import com.ap.nn.core.Nucleus;

import java.util.stream.IntStream;

public class LMS /* Least Mean Square */ extends Supervised {

    @Override
    protected void updateNetwork(double[] outputError) {
        AbstractLayerContainer<Nucleus> layer = getNetwork().getOutputLayer();
        IntStream.range(0, layer.size()).parallel().forEach(i -> {
            layer.get(i).setError(outputError[i]);
            updateNucleusGain(layer.get(i));
        });
    }

    protected void updateNucleusGain(Nucleus nucleus) {
        double error = nucleus.getError();
        nucleus.getDendrites().stream().forEach(sinapse -> {
            double delta = getRate() * error * sinapse.getInput();
            sinapse.get().inc(delta);
        });
    }
}