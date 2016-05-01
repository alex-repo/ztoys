package com.ap.common.core;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TreeNodeTest {

    private static TreeNode tree;
    private static TreeNode childrenLevel00;
    private static TreeNode childrenLevel01;

    public static class SubTree implements TreeNode {

        @Override
        public TreeNode getParent() {
            return childrenLevel00;
        }

        @Override
        public void setParent(TreeNode tree) {
        }

        @Override
        public ArrayList getChildrenList() {
            return null;
        }

        @Override
        public Stream getChildrenStream() {
            return Stream.empty();
        }
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        childrenLevel01 = new SubTree();
        ArrayList ch1 = new ArrayList();
        ch1.add(childrenLevel01);
        ch1.add(childrenLevel01);

        childrenLevel00 = new TreeNode() {

            @Override
            public TreeNode getParent() {
                return tree;
            }

            @Override
            public void setParent(TreeNode tree) {
            }

            @Override
            public ArrayList getChildrenList() {
                return ch1;
            }

            @Override
            public Stream getChildrenStream() {
                return ch1.stream();
            }
        };
        ArrayList ch0 = new ArrayList();
        ch0.add(childrenLevel00);
        ch0.add(childrenLevel00);
        tree = new TreeNode() {

            @Override
            public TreeNode getParent() {
                return null;
            }

            @Override
            public void setParent(TreeNode tree) {
            }

            public ArrayList getChildrenList() {
                return ch0;
            }

            @Override
            public Stream getChildrenStream() {
                return ch0.stream();
            }
        };
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getParent() {
        fail("Not yet implemented");
    }

    @Test
    public void setParent() {
        fail("Not yet implemented");
    }

    @Test
    public void getChildrenList() {
        fail("Not yet implemented");
    }

    @Test
    public void getChildrenStream() {
        Object[] array = tree.flattened(SubTree.class).toArray();
        assertSame(4, array.length);
    }

    @Test
    public void alarm() {
        fail("Not yet implemented");
    }

    @Test
    public void treeClone() {
        fail("Not yet implemented");
    }

    @Test
    public void getStream() {
        fail("Not yet implemented");
    }

    @Test
    public void flattened() {
        Object[] array = tree.flattened().toArray();
        assertSame(7, array.length);
    }
}
