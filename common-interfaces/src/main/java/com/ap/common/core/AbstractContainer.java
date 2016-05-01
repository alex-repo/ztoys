package com.ap.common.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class AbstractContainer<K, V> extends Identified<K> implements Container<V> {

    private Container<V> container;

    public AbstractContainer(Class<? extends Container> containerClass) {
        container = new ProxyContainer(containerClass).getContainer();
    }

    public AbstractContainer(Class<? extends Container> containerClass, int initialCapacity) {
        container = new ProxyContainer(containerClass).getContainer();
        container.ensureCapacity(initialCapacity);
    }

    public AbstractContainer(Class<? extends Container> containerClass, Collection<V> collection) {
        container = new ProxyContainer(containerClass).getContainer();
        container.addAll(collection);
    }

    @Override
    public ArrayList<V> asList() {
        return container.asList();
    }

    @Override
    public Stream<V> asStream() {
        return container.asStream();
    }

    @Override
    public boolean add(V e) {
        return container.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        return container.addAll(c);
    }

    @Override
    public V set(int i, V e) {
        return container.set(i, e);
    }

    @Override
    public int size() {
        return container.size();
    }

    @Override
    public V get(int i) {
        return container.get(i);
    }

    @Override
    public V remove(int i) {
        return container.remove(i);
    }

    @Override
    public boolean remove(V e) {
        return container.remove(e);
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        container.ensureCapacity(minCapacity);
    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public V getAny() {
        return container.getAny();
    }

}
