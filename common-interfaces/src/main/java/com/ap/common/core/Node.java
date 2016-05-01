package com.ap.common.core;

import java.util.ArrayList;
import java.util.stream.Stream;

public interface Node<T> {

    public T getParent();

    public void setParent(T tree);

    public ArrayList<T> getChildrenList();

    public Stream<T> getChildrenStream();

}
