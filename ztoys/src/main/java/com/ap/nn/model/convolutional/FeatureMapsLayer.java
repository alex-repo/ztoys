package com.ap.nn.model.convolutional;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.ap.common.core.AbstractContainer;
import com.ap.common.core.Container;
import com.ap.common.model.D2;
import com.ap.common.model.Kernel;
import com.ap.nn.core.D2LayerContainer;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.NucleusContainer;
import com.ap.nn.model.Input;

public abstract class FeatureMapsLayer extends D2LayerContainer {

    protected Kernel kernel;
    private D2 d2;

    protected void setD2(D2 d2) {
        this.d2 = d2;
    }

    public D2 getD2() {
        return d2;
    }

    public FeatureMapsLayer(Class<? extends Container> containerClass, Kernel kernel) {
        super(containerClass);
        this.kernel = kernel;
    }

    public FeatureMapsLayer(Class<? extends Container> containerClass, Kernel kernel, D2 d2, int mapCount, Nucleus nucleargenome) {
        this(containerClass, kernel);
        setD2(d2);
        createFeatureMaps(containerClass, mapCount, d2, nucleargenome);
    }

    public FeatureMapsLayer(Class<? extends Container> containerClass, D2 d2, int mapCount) {
        this(containerClass, null, d2, mapCount, new Input());
    }

    protected final void createFeatureMaps(
            Class<? extends Container> containerClass
            , int mapCount
            , D2 d2
            , Nucleus nucleargenome) {
        Stream.generate(() -> new D2Layer(containerClass, d2, nucleargenome)).limit(mapCount).forEachOrdered(
                this::addFeatureMap);
    }

    protected final void createFeatureMaps(Class<? extends Container> containerClass, D2 d2, Nucleus nucleargenome) {
        addFeatureMap(new D2Layer(containerClass, d2, nucleargenome));
    }

    public D2Layer addFeatureMap(D2Layer featureMap) {
        if (((AbstractContainer) this).add(featureMap)) {
            return featureMap;
        } else {
            throw new RuntimeException();
        }
    }

    protected ArrayList<D2Layer> getFeatureMap() {
        return getChildrenList();
    }

    public D2Layer getFeatureMap(int index) {
        return get(index);
    }

    public int getNumberOfMaps() {
        return getChildrenList().size();
    }

    public Nucleus getNucleus(int x, int y, int mapIndex) {
        D2Layer map = get(mapIndex);
        return map.getNucleus(x, y);
    }

    @Override
    public int size() {
        return getChildrenStream().parallel().mapToInt(AbstractContainer::size).sum();
    }

    @Override
    public void calculate() {
        getChildrenStream().parallel().forEach(NucleusContainer::calculate);
    }

    public Kernel getKernel() {
        return kernel;
    }

    public FeatureMapsLayer conect(FeatureMapsLayer toLayer) {
        return (FeatureMapsLayer) super.connect(toLayer);
    }

    static protected FeatureMapsLayer connectMaps(FeatureMapsLayer fromLayer, FeatureMapsLayer toLayer, int fromIndex, int toIndex) {
        return toLayer.connectMaps(fromLayer.getFeatureMap(fromIndex), toLayer.getFeatureMap(toIndex));
    }

    protected abstract FeatureMapsLayer connectMaps(D2Layer from, D2Layer to);
}
