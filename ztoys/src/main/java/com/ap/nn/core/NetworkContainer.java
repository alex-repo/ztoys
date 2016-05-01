package com.ap.nn.core;

import com.ap.common.core.AbstractContainer;
import com.ap.common.core.Container;
import com.ap.common.core.TreeNode;
import com.ap.common.model.EuclideanVector;
import com.ap.nn.model.Bias;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.stream.Stream;

public class NetworkContainer extends AbstractContainer<String, AbstractLayerContainer> implements Network<AbstractLayerContainer> {

    public NetworkContainer(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    private AbstractLayerContainer<Nucleus> inputLayer;
    private AbstractLayerContainer<Nucleus> outputLayer;

    @Override
    public Network addLayer(AbstractLayerContainer layer) {
        // FIXME Could Container belong another Network? So check parent.
        if (layer != null) {
            layer.setParent(this);
            if (add(layer) && size() == 1) {
                setInputLayer(layer);
            }
            if (size() > 0) {
                setOutputLayer(get(size() - 1));
            }
            if (size() > 1) {
                get(size() - 2).connect(get(size() - 1));
            }
        }
        // FIXME RuntimeException could be here
        fireEvent(new EventObject(layer));
        return this;
    }

    @Override
    public final ArrayList<AbstractLayerContainer> getChildrenList() {
        return asList();
    }

    @Override
    public EuclideanVector getOutputs() {
        return new EuclideanVector(outputLayer.asStream().map(Nucleus::getOutput));
    }

    @Override
    public AbstractLayerContainer getOutputLayer() {
        return outputLayer;

    }

    protected Network setOutputLayer(AbstractLayerContainer outputLayer) {
        this.outputLayer = outputLayer;
        return this;
    }

    protected Network setInOutLayers() {
        setInputLayer(get(0));
        setOutputLayer(get(size() - 1));
        return this;
    }

    @Override
    public AbstractLayerContainer getInputLayer() {
        return inputLayer;
    }

    protected Network setInputLayer(AbstractLayerContainer inputLayer) {
        this.inputLayer = inputLayer;
        return this;
    }

    @Override
    public void calculate() {
        asStream().forEach(AbstractLayerContainer::calculate);
    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    private int current;

    @Override
    public void setInput(EuclideanVector inputVector) {
        current = 0;
        inputLayer.asStream().filter(p -> !(p instanceof Bias)).forEachOrdered(n -> n.setInput(inputVector.get(current++)));
    }

    @Override
    public Stream getChildrenStream() {
        return asStream();
    }

    @Override
    public void setParent(TreeNode tree) {
    }
}
