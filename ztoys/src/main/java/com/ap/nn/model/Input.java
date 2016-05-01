package com.ap.nn.model;

import com.ap.common.function.Linear;
import com.ap.nn.core.Nucleus;

public class Input extends Nucleus {

    public Input() {
        super(new Linear());
    }

    @Override
    public void calculate() {
        setOutput(getInput());
    }
}
