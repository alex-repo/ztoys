package com.ap.nn.model;

import com.ap.common.core.Listened;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.nn.core.Network;

abstract public class Learning extends LearningSpace implements Listened {

    private Network network;

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    abstract public void learn(VectorInjectiveSpace injectiveSpace);

    abstract protected void neuralyzerPlus(VectorInjectiveSpace injectiveSpace);
}