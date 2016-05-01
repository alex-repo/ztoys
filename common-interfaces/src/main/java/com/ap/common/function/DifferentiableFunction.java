package com.ap.common.function;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.OptionalDouble;
import java.util.function.Function;

abstract public class DifferentiableFunction implements Function<Double, OptionalDouble>, Serializable {

    transient private OptionalDouble output = OptionalDouble.empty();

    public OptionalDouble getOutput() {
        return output;
    }

    public void setOutput(Double output) {
        this.output = OptionalDouble.of(output);
    }

    public OptionalDouble derivative(Double x) {
        return OptionalDouble.empty();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject((output instanceof OptionalDouble && output.isPresent()) ? output.getAsDouble() : Double.NaN);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        Double value = (Double) ois.readObject();
        if (value.isNaN()) {
            output = OptionalDouble.empty();
        } else {
            output = OptionalDouble.of(value);
        }
        ;
    }

}
