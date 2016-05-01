package com.ap.nn.model;

import com.ap.common.core.DoubleCollector;
import com.ap.common.function.DifferentiableFunction;
import com.ap.common.function.Step;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.Sinapse;
import com.ap.nn.core.WSumDoubleCollector;

public class Bias extends Nucleus {

    public Bias(DoubleCollector<Sinapse> collector, DifferentiableFunction differentiableFunction) {
        super(new WSumDoubleCollector(), new Step());
    }

    @Override
    public double getOutput() {
        return 1;
    }

    @Override
    public Sinapse addInput(Sinapse sinapse) {
        return null;
    }

    @Override
    public Sinapse addInput(Nucleus nucleus, double gain) {
        return null;
    }

    @Override
    public Sinapse addInput(Nucleus nucleus) {
        return null;
    }

}
