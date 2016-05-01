package com.ap.nn.core;

import com.ap.common.core.Listened;
import com.ap.common.core.TreeNode;
import com.ap.common.model.EuclideanVector;

import java.io.Serializable;

public interface Network<L extends AbstractLayerContainer> extends TreeNode, Listened, Serializable {

    Network addLayer(L layer);

    default Network connect(L from, L to) {
        from.connect(to);
        return this;
    }

    L getOutputLayer();

    L getInputLayer();

    void calculate();

    EuclideanVector getOutputs();

    void setInput(EuclideanVector inputVector);

    L get(int index);

}
