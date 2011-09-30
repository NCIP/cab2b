package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

public class TreeTest extends TestCase {
    public void testBasic() {
        Tree<Integer, String> tree;
        try {
            tree = newTree(null);
            fail();
        } catch (NullPointerException e) {

        }
        tree = newTree(1);
        assertTrue(tree.containsNode(1));
        assertFalse(tree.containsNode(2));
        assertEquals(integer(1), tree.getRoot());
        assertTrue(tree.getChildren(1).isEmpty());
        assertNull(tree.getLabel(1));
        assertNull(tree.getParent(1));
        assertTrue(tree.isRoot(1));
        assertTrue(tree.isLeaf(1));
        tree.setLabel(1, "1");
        assertNull(tree.getLabel(1));

        assertNull(tree.putChild(1, 2, "12"));
        assertTrue(tree.containsNode(1));
        assertTrue(tree.containsNode(2));
        assertEquals(integer(1), tree.getRoot());
        assertEquals(asSet(2), tree.getChildren(1));
        assertNull(tree.getLabel(1));
        assertNull(tree.getParent(1));
        assertEquals("12", tree.getLabel(2));
        assertEquals(integer(1), tree.getParent(2));
        assertTrue(tree.getChildren(2).isEmpty());
        assertTrue(tree.isRoot(1));
        assertFalse(tree.isLeaf(1));
        assertFalse(tree.isRoot(2));
        assertTrue(tree.isLeaf(2));

        Tree<Integer, String> temp = tree.clone();

        assertEquals("12", tree.setLabel(2, "21"));
        assertEquals("21", tree.getLabel(2));

        assertEquals("21", tree.putChild(1, 2, "12"));
        assertEquals(temp, tree);
    }

    public void testPivot() {
        Tree<Integer, String> tree = newTree(1);
        tree.pivot(1);
        tree.putChild(1, 2, "12");

        Tree<Integer, String> orig = tree.clone();
        tree.pivot(1);

        Tree<Integer, String> expected = newTree(2);
        expected.putChild(2, 1, "12");
        tree.pivot(2);
        assertEquals(expected, tree);

        tree.pivot(1);
        assertEquals(orig, tree);

        tree.putChild(1, 3, "13"); // 1(2, 3)
        expected.putChild(1, 3, "13");
        tree.pivot(1);
        tree.pivot(2);
        assertEquals(expected, tree); // 2(1(3))

        expected = newTree(3); // 3(1(2))
        expected.putChild(3, 1, "13");
        expected.putChild(1, 2, "12");
        tree.pivot(3);
        assertEquals(expected, tree);

        tree = expected.clone();
        expected = newTree(1);
        expected.putChild(1, 2, "12");
        expected.putChild(1, 3, "13");
        tree.pivot(1);
        assertEquals(expected, tree);
    }

    public void testSetRoot() {
        Tree<Integer, String> tree = newTree(1);
        Tree<Integer, String> expected = newTree(1);
        tree.setRoot(1, "12");
        assertEquals(expected, tree);

        tree.putChild(1, 2, "12");
        expected = newTree(2);
        expected.putChild(2, 1, "21");
        tree.setRoot(2, "21");
        assertEquals(expected, tree);

        expected.putChild(2, 3, "32");
        expected.pivot(3);
        tree.setRoot(3, "32");
        assertEquals(expected, tree);

        expected = newTree(1);
        expected.putChild(1, 3, "13");
        expected.putChild(3, 2, "32");
        tree.setRoot(1, "13");
        assertEquals(expected, tree);

        tree.putChild(1, 4, "14");
        expected = newTree(3);
        expected.putChild(3, 2, "32");
        expected.putChild(3, 1, "31");
        expected.putChild(1, 4, "14");
        tree.setRoot(3, "31");
        assertEquals(expected, tree);

        Tree<Integer, String> temp = tree.clone();
        expected = newTree(2);
        expected.putChild(2, 3, "23");
        expected.putChild(3, 1, "31");
        expected.putChild(1, 4, "14");
        tree.setRoot(2, "23");
        assertEquals(expected, tree);

        tree = temp;
        expected = newTree(4);
        expected.putChild(4, 3, "43");
        expected.putChild(3, 1, "31");
        expected.putChild(3, 2, "32");
        tree.setRoot(4, "43");
        assertEquals(expected, tree);

    }

    public void testSubTree() {
        Tree<Integer, String> tree = newTree(1);
        assertEquals(tree, tree.subTree(1));

        tree.putChild(1, 2, "12");
        assertEquals(tree, tree.subTree(1));
        assertEquals(newTree(2), tree.subTree(2));

        tree.putChild(1, 3, "13");
        assertEquals(tree, tree.subTree(1));
        assertEquals(newTree(2), tree.subTree(2));
        assertEquals(newTree(3), tree.subTree(3));

        tree.putChild(2, 4, "24");
        assertEquals(tree, tree.subTree(1));
        assertEquals(newTree(3), tree.subTree(3));
        assertEquals(newTree(4), tree.subTree(4));
        Tree<Integer, String> subTree2 = newTree(2);
        subTree2.putChild(2, 4, "24");
        assertEquals(subTree2, tree.subTree(2));

        tree.putChild(2, 5, "25");
        subTree2.putChild(2, 5, "25");
        assertEquals(tree, tree.subTree(1));
        assertEquals(subTree2, tree.subTree(2));
    }

    public void testAddSubTree() {
        Tree<Integer, String> tree = newTree(1);
        Tree<Integer, String> expected = newTree(1);

        tree.addSubTree(1, newTree(2), "12");
        expected.putChild(1, 2, "12");

        assertEquals(expected, tree);

        Tree<Integer, String> subTree = newTree(3);
        subTree.putChild(3, 4, "34");
        subTree.putChild(3, 5, "35");

        expected.putChild(1, 3, "13");
        expected.putChild(3, 4, "34");
        expected.putChild(3, 5, "35");

        tree.addSubTree(1, subTree, "13");
        assertEquals(expected, tree);

        subTree = newTree(6);
        subTree.putChild(6, 7, "67");
        subTree.putChild(7, 8, "78");
        subTree.putChild(6, 9, "69");

        expected.putChild(2, 6, "26");
        expected.putChild(6, 7, "67");
        expected.putChild(7, 8, "78");
        expected.putChild(6, 9, "69");

        tree.addSubTree(2, subTree, "26");
        assertEquals(expected, tree);
    }

    public void testRemoveSubtree() {
        Tree<Integer, String> tree = newTree(1);
        assertEquals(tree, tree.removeSubTree(1));

        Tree<Integer, String> removed = newTree(2);
        Tree<Integer, String> remaining = newTree(1);

        tree.putChild(1, 2, "12");
        checkRemoval(tree, 2, removed, remaining);

        tree.putChild(1, 2, "12");
        tree.putChild(1, 3, "13");
        remaining.putChild(1, 3, "13");
        checkRemoval(tree, 2, removed, remaining);

        tree.putChild(1, 2, "12");
        tree.putChild(2, 4, "24");
        tree.putChild(2, 5, "25");

        removed.putChild(2, 4, "24");
        removed.putChild(2, 5, "25");
        checkRemoval(tree, 2, removed, remaining);

        tree.putChild(1, 2, "12");
        tree.putChild(2, 4, "24");
        tree.putChild(2, 5, "25");
        tree.putChild(4, 6, "46");

        removed = newTree(4);
        removed.putChild(4, 6, "46");

        remaining.putChild(1, 2, "12");
        remaining.putChild(2, 5, "25");
        checkRemoval(tree, 4, removed, remaining);

        remaining = newTree(1);
        remaining.putChild(1, 2, "12");
        remaining.putChild(2, 5, "25");
        removed = newTree(3);
        checkRemoval(tree, 3, removed, remaining);
    }

    private void checkRemoval(Tree<Integer, String> tree, int nodeToRemove, Tree<Integer, String> removed,
            Tree<Integer, String> remaining) {
        assertEquals(removed, tree.removeSubTree(nodeToRemove));
        assertEquals(remaining, tree);
    }

    public void testContainsAnyNode() {
        Tree<Integer, String> tree1 = newTree(1);
        Tree<Integer, String> tree2 = newTree(1);
        checkContainsAnyNode(tree1, tree2);
        tree2 = newTree(2);
        checkDoesNotContainsAnyNode(tree1, tree2);
        tree2.putChild(2, 1, "21");
        checkContainsAnyNode(tree1, tree2);

        tree1 = newTree(3);
        checkDoesNotContainsAnyNode(tree1, tree2);
        tree1.putChild(3, 4, "34");
        checkDoesNotContainsAnyNode(tree1, tree2);
        Tree<Integer, String> tempTree1 = tree1.clone();

        tree1.putChild(3, 2, "32");
        checkContainsAnyNode(tree1, tree2);
        tree1 = tempTree1;
        tree1.putChild(3, 1, "31");
        checkContainsAnyNode(tree1, tree2);
        tree1 = tempTree1;
        tree1.putChild(4, 1, "41");
        checkContainsAnyNode(tree1, tree2);
        tree1.putChild(3, 2, "32");
        checkContainsAnyNode(tree1, tree2);
    }

    private void checkContainsAnyNode(Tree<Integer, String> tree1, Tree<Integer, String> tree2) {
        assertTrue(tree1.containsAnyNode(tree2));
        assertTrue(tree2.containsAnyNode(tree1));
    }

    private void checkDoesNotContainsAnyNode(Tree<Integer, String> tree1, Tree<Integer, String> tree2) {
        assertFalse(tree1.containsAnyNode(tree2));
        assertFalse(tree2.containsAnyNode(tree1));
    }

    public void testPaths() {
        Tree<Integer, String> tree = newTree(1);
        assertNull(tree.getNodesPath(1));
        tree.putChild(1, 2, "12");
        assertEquals(asList(1, 2), tree.getNodesPath(2));

        tree.putChild(1, 3, "13");
        assertEquals(asList(1, 2), tree.getNodesPath(2));
        assertEquals(asList(1, 3), tree.getNodesPath(3));
        assertNull(tree.getNodesPath(2, 3));
        assertNull(tree.getNodesPath(3, 2));

        tree.putChild(2, 4, "24");
        assertEquals(asList(1, 2, 4), tree.getNodesPath(4));
        assertEquals(asList(2, 4), tree.getNodesPath(2, 4));
        assertNull(tree.getNodesPath(4, 3));
        assertNull(tree.getNodesPath(3, 4));
    }

    private Tree<Integer, String> newTree(Integer root) {
        return new Tree<Integer, String>(root);
    }

    private Integer integer(int i) {
        return new Integer(i);
    }

    private static <T> Set<T> asSet(T... elems) {
        Set<T> res = new HashSet<T>();
        for (T elem : elems) {
            res.add(elem);
        }
        return res;
    }

    private static <T> List<T> asList(T... elems) {
        List<T> res = new ArrayList<T>();
        for (T elem : elems) {
            res.add(elem);
        }
        return res;
    }
}
