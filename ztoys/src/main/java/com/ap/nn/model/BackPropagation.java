package com.ap.nn.model;

import com.ap.common.core.TreeNode;
import com.ap.common.function.DifferentiableFunction;
import com.ap.nn.core.AbstractLayerContainer;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.NucleusContainer;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class BackPropagation extends LMS {

    int i;

    @Override
    protected void updateNetwork(double[] outputError) {
        updateOutputLayer(outputError);
        updateHiddenLayers();
    }

    protected void updateOutputLayer(double[] outputError) {
        i = 0;
        AbstractLayerContainer<Nucleus> container = getNetwork().getOutputLayer();
        container.asStream().forEach(n -> {
            if (outputError[i] == 0) {
                n.setError(0);
            } else {

                DifferentiableFunction differentiableFunction = n.getAxonFunction();
                double input = n.getInput();

                double delta = outputError[i]
                        * (differentiableFunction.derivative(input).isPresent() ? differentiableFunction.derivative(input).getAsDouble() : 1.0);
                n.setError(delta);
                updateNucleusGain(n);
            }
            i++;
        });
    }

    protected void updateHiddenLayers() {
        ArrayList<? extends TreeNode> layers = getNetwork().getChildrenList();
        IntStream.iterate(layers.size() - 2, f -> f - 1).limit(layers.size() - 2).forEachOrdered(
                i -> ((NucleusContainer) layers.get(i)).getChildrenList().stream().forEach(n -> {
            double delta = calculateError(n);
            n.setError(delta);
            updateNucleusGain(n);
        }));
    }

    protected double calculateError(Nucleus nucleus) {
        double sum = nucleus.getAxonS().stream().parallel().mapToDouble(s -> s.getParent().getError() * s.get().getAsDouble()).sum();
        DifferentiableFunction differentiableFunction = nucleus.getAxonFunction();
        double input = nucleus.getInput();
        double f = differentiableFunction.derivative(input).isPresent() ? differentiableFunction.derivative(input).getAsDouble() : 1.0;
        return f * sum;
    }
}
