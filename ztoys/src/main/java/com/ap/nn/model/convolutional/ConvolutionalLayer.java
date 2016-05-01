package com.ap.nn.model.convolutional;

import com.ap.common.core.Container;
import com.ap.common.model.D2;
import com.ap.common.model.Gain;
import com.ap.common.model.Kernel;
import com.ap.common.function.Sigmoid;
import com.ap.nn.core.AbstractLayerContainer;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.Sinapse;

public class ConvolutionalLayer extends FeatureMapsLayer {

    public ConvolutionalLayer(Class<? extends Container> containerClass, FeatureMapsLayer layer, Kernel kernel) {
        super(containerClass, kernel);
        D2 d2d = layer.getD2();
        int mapWidth = d2d.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = d2d.getHeight() - (kernel.getHeight() - 1);
        this.setD2(new D2(mapWidth, mapHeight));
        createFeatureMaps(containerClass, getD2(), new Nucleus(new Sigmoid()));
    }

    public ConvolutionalLayer(Class<? extends Container> containerClass, FeatureMapsLayer layer, Kernel kernel, int numberOfMaps) {
        super(containerClass, kernel);
        D2 fromDimension = layer.getD2();
        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
        this.setD2(new D2(mapWidth, mapHeight));
        createFeatureMaps(containerClass, numberOfMaps, this.getD2(), new Nucleus(new Sigmoid()));
    }

    public ConvolutionalLayer(Class<? extends Container> containerClass, FeatureMapsLayer layer, Kernel kernel, int numberOfMaps, Nucleus nuclearGenome) {
        super(containerClass, kernel);
        D2 fromDimension = layer.getD2();
        int mapWidth = fromDimension.getWidth() - (kernel.getWidth() - 1);
        int mapHeight = fromDimension.getHeight() - (kernel.getHeight() - 1);
        this.setD2(new D2(mapWidth, mapHeight));
        createFeatureMaps(containerClass, numberOfMaps, this.getD2(), nuclearGenome);
    }

    public ConvolutionalLayer(Class<? extends Container> containerClass, D2 inputMapSize, int mapCount) {
        super(containerClass, inputMapSize, mapCount);
    }

    @Override
    public FeatureMapsLayer connectMaps(D2Layer fromMap, D2Layer toMap) {
        int numberOfSharedWeights = kernel.getArea();
        Gain[] gains = new Gain[numberOfSharedWeights];
        for (int i = 0; i < numberOfSharedWeights; i++) {
            Gain gain = new Gain();
            gain.randomize(-1, 1);
            gains[i] = gain;
        }
        for (int x = 0; x < toMap.getWidth(); x++) {
            for (int y = 0; y < toMap.getHeight(); y++) {
                Nucleus toNucleus = toMap.getNucleus(x, y);
                for (int dy = 0; dy < kernel.getHeight(); dy++) {
                    for (int dx = 0; dx < kernel.getWidth(); dx++) {
                        int fromX = x + dx;
                        int fromY = y + dy;
                        int gainIndex = dx + dy * kernel.getHeight();
                        Nucleus fromNucleus = fromMap.getNucleus(fromX, fromY);
                        Sinapse.createSinapse(fromNucleus, toNucleus, gains[gainIndex]);
                    }
                }
            }
        }
        return this;
    }

    @Override
    public AbstractLayerContainer connectLayer(AbstractLayerContainer featureMapsLayer) {
        if (!(featureMapsLayer instanceof FeatureMapsLayer)) {
            throw new RuntimeException("Culdn't connect Lyaer " + featureMapsLayer.getClass() + " to FeatureMapsLayer" + this.getClass());
        }
        FeatureMapsLayer mapsLayer = (FeatureMapsLayer) featureMapsLayer;
        for (int i = 0; i < mapsLayer.getNumberOfMaps(); i++) {
            for (int j = 0; j < getNumberOfMaps(); j++) {
                connectMaps(mapsLayer, this, i, j);
            }
        }
        return this;
    }
}
