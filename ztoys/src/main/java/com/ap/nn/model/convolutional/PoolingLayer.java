package com.ap.nn.model.convolutional;

import com.ap.common.core.Container;
import com.ap.common.model.D2;
import com.ap.common.model.Gain;
import com.ap.common.model.Kernel;
import com.ap.common.function.Linear;
import com.ap.nn.core.AbstractLayerContainer;
import com.ap.nn.core.MaxDoubleCollector;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.Sinapse;

public class PoolingLayer extends FeatureMapsLayer {

	public PoolingLayer(Class<? extends Container> containerClass, FeatureMapsLayer layer, Kernel kernel) {
		super(containerClass, kernel);
		int numberOfMaps = layer.getNumberOfMaps();
		D2 fromDimension = layer.getD2();
		int mapWidth = fromDimension.getWidth() / kernel.getWidth();
		int mapHeight = fromDimension.getHeight() / kernel.getHeight();
		this.setD2(new D2(mapWidth, mapHeight));
		createFeatureMaps(containerClass, numberOfMaps, getD2(), new Nucleus(new MaxDoubleCollector(), new Linear()));
	}

	@Override
	public FeatureMapsLayer connectMaps(D2Layer fromMap, D2Layer toMap) {
		int kernelWidth = kernel.getWidth();
		int kernelHeight = kernel.getHeight();

		for (int x = 0; x < fromMap.getWidth() - kernelWidth + 1; x += kernelWidth) {
			for (int y = 0; y < fromMap.getHeight() - kernelHeight + 1; y += kernelHeight) {
				Gain gain = new Gain();
				gain.accept(1);
				Nucleus to = toMap.getNucleus(x / kernelWidth, y / kernelHeight);
				for (int dy = 0; dy < kernelHeight; dy++) {
					for (int dx = 0; dx < kernelWidth; dx++) {
						int fromX = x + dx;
						int fromY = y + dy;
						Nucleus from = fromMap.getNucleus(fromX, fromY);
						Sinapse.createSinapse(from, to, gain);
					}
				}
			}
		}
		return this;
	}

	@Override
	public  AbstractLayerContainer connectLayer(AbstractLayerContainer featureMapsLayer) {
		if (!(featureMapsLayer instanceof FeatureMapsLayer)) {
			throw new RuntimeException("Culdn't connect Lyaer " + featureMapsLayer.getClass() + " to FeatureMapsLayer" + this.getClass());
		}
		FeatureMapsLayer mapsLayer = (FeatureMapsLayer)featureMapsLayer; 

		for (int i = 0; i < getNumberOfMaps(); i++) {
			connectMaps(mapsLayer, this, i, i);
		}
		return this;
	}
}
