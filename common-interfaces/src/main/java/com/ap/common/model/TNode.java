package com.ap.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TNode implements Serializable {

    private static final long serialVersionUID = -181403229462007401L;

    private String nodeName;
    private List<String> leafs;

    public TNode(String tnodeName) {
        this.nodeName = tnodeName;
        leafs = new ArrayList<>();
    }

    public void addLeaf(String name) {
        leafs.add(name);
    }

    public void removeLeaf(String name) {
        leafs.remove(name);
    }

    public List<String> getLeafs() {
        return leafs;
    }

    public String getName() {
        return nodeName;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("=== TNode: " + nodeName + " ===\n");
        b.append("Leafs:\n");
        for (String leaf : leafs) {
            b.append("- " + leaf + "\n");
        }
        return b.toString();
    }
}