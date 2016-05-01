package com.ap.ztoys.samples.convolution;

import com.ap.common.core.ComparableId;
import com.ap.common.model.EuclideanVector;
import com.ap.common.model.VectorInjective;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.nn.core.Network;

import java.util.PriorityQueue;

public class ModelMetric {

    public static void calculateModelMetric(Network network, VectorInjectiveSpace testSet) {
        int totalWrong = 0;
        int totalRight = 0;
        for (VectorInjective vectorInjective : testSet.asList()) {
            network.setInput(vectorInjective.getInput());
            network.calculate();
            PriorityQueue<ComparableId> desiredOutput = new PriorityQueue<>();
            PriorityQueue<ComparableId> actualOutput = new PriorityQueue<>();
            EuclideanVector outputResult = vectorInjective.getOutput();
            EuclideanVector actualResult = network.getOutputs();
            for (int i = 0; i < outputResult.size(); i++) {
                desiredOutput.add(new ComparableId(i, outputResult.get(i)));
                actualOutput.add(new ComparableId(i, actualResult.get(i)));
            }
            if (desiredOutput.peek().getId() == actualOutput.peek().getId()) {
                totalRight++;
            } else {
                totalWrong++;
            }
        }
        System.out.println("RIGHT: " + totalRight);
        System.out.println("WRONG: " + totalWrong);
    }

    public static PriorityQueue<ComparableId> calculateResult(VectorInjective dataSetRow, Network convolutionalNet) {
        EuclideanVector inputVector = dataSetRow.getInput();
        convolutionalNet.setInput(inputVector);
        convolutionalNet.calculate();
        EuclideanVector output = convolutionalNet.getOutputs();
        PriorityQueue<ComparableId> actualOutput = new PriorityQueue<>();
        for (int i = 0; i < output.size(); i++) {
            actualOutput.add(new ComparableId(i, output.get(i)));
        }
        return actualOutput;
    }
}
