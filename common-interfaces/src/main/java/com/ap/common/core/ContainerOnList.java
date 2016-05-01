package com.ap.common.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class ContainerOnList<K, V> extends Identified<K> implements Container<V> {

    private ArrayList<V> list;

    public ContainerOnList() {
        list = new ArrayList<V>();
    }

    public ContainerOnList(Collection<V> collection) {
        list = new ArrayList<V>(collection);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        list.ensureCapacity(minCapacity);
    }

    @Override
    public ArrayList<V> asList() {
        return list;
    }

    @Override
    public Stream<V> asStream() {
        return list.stream();
    }

    @Override
    public boolean add(V e) {
        return list.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        return list.addAll(c);
    }

    @Override
    public V set(int i, V e) {
        return list.set(i, e);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public V get(int i) {
        return list.get(i);
    }

    @Override
    public V remove(int i) {
        return list.remove(i);
    }

    @Override
    public boolean remove(V e) {
        return list.remove(e);
    }

    @Override
    public V getAny() {
        return list.get(0);
    }

}
