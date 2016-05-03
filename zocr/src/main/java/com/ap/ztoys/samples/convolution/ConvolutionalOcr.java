package com.ap.ztoys.samples.convolution;

import com.ap.common.core.AbstractEventListener;
import com.ap.common.core.ComparableId;
import com.ap.common.core.Container;
import com.ap.common.core.ContainerOnIndexMap;
import com.ap.common.function.Sigmoid;
import com.ap.common.model.D2;
import com.ap.common.model.Kernel;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.common.util.IOUtils;
import com.ap.nn.core.AbstractLayerContainer;
import com.ap.nn.core.Network;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.NucleusContainer;
import com.ap.nn.core.WSumDoubleCollector;
import com.ap.nn.model.ConvolutionalBackpropagation;
import com.ap.nn.model.Iterative;
import com.ap.nn.model.Supervised;
import com.ap.nn.model.convolutional.ConvolutionalLayer;
import com.ap.nn.model.convolutional.ConvolutionalNetwork;
import com.ap.nn.model.convolutional.FeatureMapsLayer;
import com.ap.nn.model.convolutional.PoolingLayer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EventObject;
import java.util.PriorityQueue;

public class ConvolutionalOcr {

    private static final Class<? extends Container> containerType = ContainerOnIndexMap.class;
    private static final String nfn = "SignNet.nn";

    public static void main(String[] args) {
        VectorInjectiveSpace trainSet = getSignTrainSet();
        Network convolutionalNet = getNetwork();
        trainAndSaveNetwork(convolutionalNet, trainSet);
        try {
            String testSign = "sign#6.png";
            VectorInjectiveSpace signs = new VectorInjectiveSpace(containerType, IOUtils.createImages(testSign));
            PriorityQueue<ComparableId> actualOutput = ModelMetric.calculateResult(signs.getAny(), convolutionalNet);
            Integer s = trainSet.get(actualOutput.peek().getId()).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Network createNetwork() {
        long ctime = System.currentTimeMillis();
        Network network = new ConvolutionalNetwork(containerType);
        D2 mapSize = new D2(28, 28);
        Kernel convolutionKernel = new Kernel(5, 5);
        Kernel poolingKernel = new Kernel(2, 2);
        FeatureMapsLayer inputLayer = new ConvolutionalLayer(containerType, mapSize, 1);
        ConvolutionalLayer cLayer1 = new ConvolutionalLayer(containerType, inputLayer, convolutionKernel, 6);
        PoolingLayer pLayer2 = new PoolingLayer(containerType, cLayer1, poolingKernel);
        ConvolutionalLayer cLayer3 = new ConvolutionalLayer(containerType, pLayer2, convolutionKernel, 16);
        PoolingLayer pLayer4 = new PoolingLayer(containerType, cLayer3, poolingKernel);
        ConvolutionalLayer cLayer5 = new ConvolutionalLayer(containerType, pLayer4, new Kernel(4, 4), 120);
        AbstractLayerContainer outputLayer = new NucleusContainer(containerType, 10, new Nucleus(new WSumDoubleCollector(), new Sigmoid())).setBias(true);
        network.addLayer(inputLayer).addLayer(cLayer1).addLayer(pLayer2).addLayer(cLayer3).addLayer(pLayer4).addLayer(cLayer5).addLayer(outputLayer);
        ctime = System.currentTimeMillis() - ctime;
        return network;
    }

    private static void trainAndSaveNetwork(Network network, VectorInjectiveSpace injectiveSpace) {
        Listener listener = new Listener();
        Iterative learning = new ConvolutionalBackpropagation().rate(0.05).maxIterations(200);
        learning.addListener(listener);
        learning.setNetwork(network);
        learning.learn(injectiveSpace);
        ModelMetric.calculateModelMetric(network, injectiveSpace);
        IOUtils.save(ConvolutionalOcr.nfn, network);
    }

    public static VectorInjectiveSpace getSignTrainSet() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);
        String tscn = "10-28";
        return new VectorInjectiveSpace(containerType, IOUtils.createImages(tscn));
    }

    public static Network getNetwork() {
        Network network;
        try {
            network = (ConvolutionalNetwork) IOUtils.createFromFile(nfn);
        } catch (Exception e) {
            e.printStackTrace();
            network = null;
        }
        if (!(network instanceof ConvolutionalNetwork)) {
            network = createNetwork();
        }
        return network;
    }

    private static class Listener implements AbstractEventListener {
        @Override
        public void handleEvent(EventObject event) {
            Supervised bp = (Supervised) event.getSource();
        }
    }
}
