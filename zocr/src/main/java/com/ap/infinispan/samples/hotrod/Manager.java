package com.ap.infinispan.samples.hotrod;

import com.ap.common.model.TNode;
import com.ap.common.util.ConsoleWrap;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    private static final String msgTNodeMissing = "The specified tnode \"%s\" does not exist, choose next operation\n";
    private static final String msgEnterTNodeName = "Enter tnode name: ";

    private static String initialPrompt = "Choose action:\n"
				    + "===================== \n";

    private static final String tnodesKey = "tnodes";

    private final ConsoleWrap con;

    private static Manager manager;
    
    private final NamedCache namedCache;

    class Action implements Runnable {

        Action() {
        }

        Action(String actionShot, String actionName, Runnable runnable) {
            this.actionShot = actionShot;
            this.actionName = actionName;
            this.runnable = runnable;
        }

        private Runnable runnable;
        private String actionShot;
        private String actionName;

        public void run() {
            runnable.run();
        }

    }

    public static void main(String[] args) {
        ConsoleWrap con = new ConsoleWrap();
        manager = new Manager(con);

        for (Action a : manager.actions) {
            initialPrompt += a.actionName + "\n";
        }

        con.printf(initialPrompt);

        while (true) {
            String action = con.readLine(">");
            for (Action a : manager.actions) {
                if (a.actionShot.equals(action)) {
                    a.run();
                }
            }
        }
    }

    private final Action[] actions = new Action[]{
        new Action("at", "at  -  add a tnode", new Runnable() {
            @Override
            public void run() {
                manager.addTNode();
            }
        }),
        new Action("al", "al  -  add a leaf to a tnode", new Runnable() {
            @Override
            public void run() {
                manager.addLeafs();
            }
        }),
        new Action("rt", "rt  -  remove a tnode", new Runnable() {
            @Override
            public void run() {
                manager.removeTNode();
            }
        }),
        new Action("rp", "rp  -  remove a leaf from a tnode", new Runnable() {
            @Override
            public void run() {
                manager.removeLeaf();
            }
        }),
        new Action("p", "p  -  print all tnodes and leafs", new Runnable() {
            @Override
            public void run() {
                manager.printTNodes();
            }
        }),
        new Action("q", "q  -  quit", new Runnable() {
            @Override
            public void run() {
                namedCache.clearstop();
                System.exit(0);
            }
        })};

    private Manager(ConsoleWrap con) {
        this.con = con;
        namedCache = new NamedCache();
    }

    private void addTNode() {
        String tnodeName = con.readLine(msgEnterTNodeName);
        @SuppressWarnings("unchecked")
        List<String> tnodes = (List<String>) namedCache.cache.get(tnodesKey);
        if (tnodes == null) {
            tnodes = new ArrayList<>();
        }
        TNode t = new TNode(tnodeName);
        namedCache.cache.put(tnodeName, t);
        tnodes.add(tnodeName);

        namedCache.cache.put(tnodesKey, tnodes);
    }

    private void addLeafs() {
        String tnodeName = con.readLine(msgEnterTNodeName);
        String leafName;
        TNode t = (TNode) namedCache.cache.get(tnodeName);
        if (t != null) {
            while (!(leafName = con.readLine("Enter leaf's name (to stop adding, type \"q\"): ")).equals("q")) {
                t.addLeaf(leafName);
            }
            namedCache.cache.put(tnodeName, t);
        } else {
            con.printf(msgTNodeMissing, tnodeName);
        }
    }

    private void removeLeaf() {
        String leafName = con.readLine("Enter leaf's name: ");
        String tnodeName = con.readLine("Enter leaf's tnode: ");
        TNode t = (TNode) namedCache.cache.get(tnodeName);
        if (t != null) {
            t.removeLeaf(leafName);
            namedCache.cache.put(tnodeName, t);
        } else {
            con.printf(msgTNodeMissing, tnodeName);
        }
    }

    private void removeTNode() {
        String tnodeName = con.readLine(msgEnterTNodeName);
        TNode t = (TNode) namedCache.cache.get(tnodeName);
        if (t != null) {
            namedCache.cache.remove(tnodeName);
            @SuppressWarnings("unchecked")
            List<String> tnodes = (List<String>) namedCache.cache.get(tnodesKey);
            if (tnodes != null) {
                tnodes.remove(tnodeName);
            }
            namedCache.cache.put(tnodesKey, tnodes);
        } else {
            con.printf(msgTNodeMissing, tnodeName);
        }
    }

    private void printTNodes() {
        @SuppressWarnings("unchecked")
        List<String> tnodes = (List<String>) namedCache.cache.get(tnodesKey);
        if (tnodes != null) {
            for (String tnodeName : tnodes) {
                con.printf(namedCache.cache.get(tnodeName).toString());
            }
        }
    }
}
