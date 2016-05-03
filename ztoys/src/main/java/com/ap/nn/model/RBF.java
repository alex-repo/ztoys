package com.ap.nn.model;

import com.ap.common.core.ContainerOnList;
import com.ap.common.function.Gaussian;
import com.ap.common.model.Cluster;
import com.ap.common.model.ClusterSpace;
import com.ap.common.model.EuclideanVector;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.NucleusContainer;
import com.ap.nn.core.Sinapse;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class RBF /* Radial Basis Function */ extends LMS {

    @Override
    protected void neuralyzerPlus(VectorInjectiveSpace injectiveSpace) {
        super.neuralyzerPlus(injectiveSpace);
        NucleusContainer layer = (NucleusContainer) getNetwork().getChildrenList().get(1);
        ClusterSpace clusterSpace = new ClusterSpace(ContainerOnList.class, getInjectiveSpace(), layer.size());
        IntStream.range(0, layer.size()).forEach(i -> {
            Double[] gains = clusterSpace.get(i).centroid().toDoubleArray();
            ArrayList<Sinapse> sinapses = layer.get(i).getDendrites();
            IntStream.range(0, sinapses.size()).forEach(j -> sinapses.get(j).get().accept(gains[j]));
        });
        Cluster centroidsClaster = new Cluster();
        clusterSpace.asStream().forEach(c -> centroidsClaster.add(c.centroid()));

        int k = 2;
        int n = 0;
        for (EuclideanVector centroid : centroidsClaster) {
            EuclideanVector[] nearestNeighbours = centroidsClaster.kNearestNeighbours(centroid, k);
            Nucleus nucleus = layer.get(n);
            double sigma = EuclideanVector.calculateSigma(centroid, nearestNeighbours);
            ((Gaussian) nucleus.getAxonFunction()).setSigma(sigma);
            n++;
        }
    }
}
