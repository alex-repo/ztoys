package com.ap.common.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public interface Container<V> extends Serializable {

    ArrayList<V> asList();

    Stream<V> asStream();

    boolean add(V e);

    boolean addAll(Collection<? extends V> c);

    V set(int i, V e);

    int size();

    V get(int i);

    V getAny();

    V remove(int i);

    boolean remove(V e);

    void ensureCapacity(int minCapacity);

    void clear();

}
