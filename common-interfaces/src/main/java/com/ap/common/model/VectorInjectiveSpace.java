package com.ap.common.model;

import com.ap.common.core.AbstractContainer;
import com.ap.common.core.Container;
import com.ap.common.image.AbstractImageWrapper;
import com.ap.common.image.IImage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VectorInjectiveSpace extends AbstractContainer<Integer, VectorInjective> {

    private int inputSize;

    private int outputSize;

    boolean injective = false;

    public VectorInjectiveSpace(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    public VectorInjectiveSpace(Class<? extends Container> containerClass, int initialCapacity) {
        super(containerClass, initialCapacity);
    }

    public VectorInjectiveSpace(Class<? extends Container> containerClass, Collection<VectorInjective> collection) {
        super(containerClass, collection);
        sizeValidation();

    }

    public VectorInjectiveSpace(Class<? extends Container> containerClass, List<IImage> images) {
        super(containerClass);
        createInjectives(images);
    }

    public VectorInjectiveSpace(Class<? extends Container> containerClass, AbstractImageWrapper imageWrapper) {
        super(containerClass);
        List<IImage> images = new ArrayList<IImage>();
        images.add(imageWrapper);
        createInjectives(images);
    }

    @Override
    public boolean add(VectorInjective vectorInjective) {
        if (size() > 0) {
            if (inputSize == vectorInjective.getInput().size() && outputSize == vectorInjective.getOutput().size()) {
                return super.add(vectorInjective);
            }
        } else {
            inputSize = (vectorInjective.getInput() == null) ? 0 : vectorInjective.getInput().size();
            outputSize = (vectorInjective.getOutput() == null) ? 0 : vectorInjective.getOutput().size();
            return super.add(vectorInjective);
        }
        return false;
    }

    private void sizeValidation() {
        if (size() > 0) {
            ArrayList<VectorInjective> list = asList();
            inputSize = (getAny().getInput() == null) ? 0 : getAny().getInput().size();
            outputSize = (getAny().getOutput() == null) ? 0 : getAny().getOutput().size();

            if (inputSize == 0) {
                throw new RuntimeException();
            }

            for (VectorInjective vectorInjective : asList()) {
                if (inputSize != vectorInjective.getInput().size() || (vectorInjective.getOutput() != null && outputSize != vectorInjective.getOutput().size())) {
                    throw new RuntimeException();
                }
            }
        }
    }

    public boolean isInjective() {
        return size() > 0 && outputSize != 0;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public int getOutputSize() {
        return outputSize;
    }

    public int getInputSize() {
        return inputSize;
    }

    private void createInjectives(List<IImage> images) {
        ArrayList<Integer> labelIds = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            IImage dImage = images.get(i);
            double[] input = dImage.asGrayDoubleArray();
            VectorInjective vectorInjective = new VectorInjective(input);
            Integer label = dImage.getLabel();
            vectorInjective.setId(label);
            labelIds.add(label);
            super.add(vectorInjective);
        }
        int imax = labelIds.stream().mapToInt(Integer::intValue).max().getAsInt();
        for (VectorInjective vectorInjective : asList()) {
            if (vectorInjective.getId() != -1) {
                double[] output = new double[imax + 1];
                output[vectorInjective.getId()] = 1;
                vectorInjective.setOutput(output);
            }
        }
        sizeValidation();
    }
}
