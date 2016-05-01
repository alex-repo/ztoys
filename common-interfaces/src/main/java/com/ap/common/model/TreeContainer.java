package com.ap.common.model;

import com.ap.common.core.AbstractContainer;
import com.ap.common.core.Container;
import com.ap.common.core.TreeNode;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class TreeContainer<K, V extends TreeNode> extends AbstractContainer<K, V> implements TreeNode {

    private TreeNode parent;

    public TreeContainer(Class<? extends Container> containerClass, int size) {
        super(containerClass, size);
    }

    public TreeContainer(Class<? extends Container> containerClass) {
        super(containerClass);
    }

    @Override
    public final void setParent(TreeNode tree) {
        this.parent = tree;
    }

    @Override
    public final TreeNode getParent() {
        return parent;
    }

    @Override
    public Stream<V> getChildrenStream() {
        return asStream();
    }

    @Override
    public V remove(int index) {
        alarm();
        return super.remove(index);
    }

    @Override
    public boolean remove(V e) {
        alarm();
        return super.remove(e);
    }

    protected V cloneElement(V genome) {
        return (V) genome.treeClone();
    }

    @Override
    public ArrayList<V> getChildrenList() {
        return asList();
    }

    public final boolean add(V e) {
        e.setParent(this);
        alarm();
        return super.add(e);
    }

    public final V set(int index, V e) {
        e.setParent(this);
        alarm();
        return super.set(index, e);
    }

    public final void clear() {
        clear();
        alarm();
    }

}
