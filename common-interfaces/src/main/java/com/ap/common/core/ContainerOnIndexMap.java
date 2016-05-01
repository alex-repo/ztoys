package com.ap.common.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ContainerOnIndexMap<I, V extends Identified<Integer>> extends Identified<I> implements Container<V> {

    private ConcurrentHashMap<Integer, V> map;

    public ContainerOnIndexMap() {
        map = new ConcurrentHashMap<>();
    }

    public ContainerOnIndexMap(Collection<V> collection) {
        this();
        addAll(collection);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public ArrayList<V> asList() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Stream<V> asStream() {
        return map.values().stream();
    }

    @Override
    public boolean add(V e) {
        if (e.getId() == null) {
            e.setId(map.size());
        }
        if (!map.containsKey(e.getId())) {
            map.put(e.getId(), e);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        final AtomicInteger ai = new AtomicInteger(map.size());
        c.parallelStream().forEach(e -> {
            e.setId(ai.getAndIncrement());
            map.put(e.getId(), e);
        });
        return c.size() > 0;
    }

    @Override
    public V set(int i, V e) {
        if (i >= map.size()) {
            throw new IndexOutOfBoundsException("Index " + i + " out of map size " + map.size() + " (simulates list's behavior).");
        }
        e.setId(i);
        V v = map.get(i);
        map.put(i, e);
        return v;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public V get(int i) {
        return map.get(i);
    }

    @Override
    public V remove(int i) {
        V v = map.get(i);
        map.remove(i);
        return v;
    }

    @Override
    public boolean remove(V e) {
        if (e.getId() != null) {
            return map.remove(e.getId()) != null;
        }
        return false;
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        // TODO: 12/01/16 todo or remove
    }

    @Override
    public V getAny() {
        Enumeration<V> e = map.elements();
        return e.nextElement();
    }

}
