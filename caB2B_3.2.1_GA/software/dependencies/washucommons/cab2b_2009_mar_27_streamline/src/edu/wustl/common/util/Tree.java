package edu.wustl.common.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A rooted, labeled tree. The tree always contains atleast one node - the root
 * node.
 * <p>
 * Each node in the tree has an associated (possibly <tt>null</tt>)label;
 * this label is to be considered the label on the edge from its parent node.
 * Thus, the label on the root node is always <tt>null</tt>.
 * 
 * Note that <tt>null</tt> nodes are not allowed. Thus, all methods throw a
 * <tt>NullPointerException</tt> if a specified node parameter is
 * <tt>null</tt>. <br>
 * Many methods in this class throw an <tt>IllegalArgumentException</tt> if a
 * specified node is not present in the tree; please refer the methods' docs.
 * <br>
 * Note that modifications to the collections returned by the methods DO NOT
 * affect the tree.<br>
 * 
 * @author srinath_k
 * 
 * @param <N> the type of nodes
 * @param <L> the type of labels
 */
public class Tree<N, L> implements Serializable, Cloneable {
    private static final long serialVersionUID = 9023818990636649627L;

    private Graph<N, L> graph;

    private transient N root;

    /**
     * Creates a new tree that contains the specified root as its only node.
     * 
     * @param root the root of the new tree.
     * @throws NullPointerException if the specified root is <tt>null</tt>.
     */
    public Tree(N root) {
        if (root == null) {
            throw new NullPointerException();
        }
        this.root = root;
        initGraph();
    }

    private void initGraph() {
        graph = new Graph<N, L>();
        graph.addVertex(root);
    }

    /**
     * Constructs a new tree containing the same nodes and labels as that of the
     * specified tree (copy constructor).
     * 
     * @param tree the tree whose contents are to be copied
     * @throws NullPointerException if the specified tree is <tt>null</tt>
     */
    public Tree(Tree<? extends N, ? extends L> tree) {
        assign(tree);
    }

    /**
     * Returns the children of the specified node.
     * 
     * @param node the node whose children are to be returned.
     * @return the children of the specified node.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public Set<N> getChildren(N node) {
        return graph.getOutNeighbours(node);
    }

    /**
     * Returns the children of the specified node along with the labels on the
     * children.
     * 
     * @param node the node whose children are to be returned.
     * @return <tt>map</tt> with key as child node and value as the label on
     *         the child node.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public Map<N, L> getChildrenWithLabels(N node) {
        return graph.getOutgoingEdges(node);
    }

    /**
     * Returns the parent of the specified node.
     * 
     * @param node the node whose parent is to be returned.
     * @return the parent of the specified node, or <tt>null</tt> if the
     *         specified node is the root of this tree.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public N getParent(N node) {
        if (isRoot(node)) {
            return null;
        }
        return first(graph.getInNeighbours(node));
    }

    /**
     * Returns the label on the specified node.
     * 
     * @param child the node whose label is to be returned
     * @return the label on the specified node, or <tt>null</tt> if the
     *         specified node is the root of this tree.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     * 
     */
    public L getLabel(N child) {
        if (isRoot(child)) {
            return null;
        }
        return graph.getEdge(getParent(child), child);
    }

    /**
     * Associates the specified label with the specified node.
     * 
     * @param child the node whose label is to be modified.
     * @param label the new label.
     * @return the previous label on the specified node.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public L setLabel(N child, L label) {
        if (!isRoot(child)) {
            return putChild(getParent(child), child, label);
        }
        return null;
    }

    /**
     * Checks if the specified node is a leaf node. A leaf node is a node that
     * has no children.
     * 
     * @param node the node to be checked
     * @return <tt>true</tt> if the specified node is a leaf.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public boolean isLeaf(N node) {
        return graph.getOutgoingEdges(node).isEmpty();
    }

    /**
     * Checks if the specified node is the root of this tree.
     * 
     * @param node the node to be checked.
     * @return <tt>true</tt> if the specified node is the root of this tree.
     */
    public boolean isRoot(N node) {
        return root.equals(node);
    }

    /**
     * Adds one node as the child of another. The parent node must already be a
     * part of this tree.
     * <ul>
     * <li>If the child node is also a part of this tree, then the existing
     * parent "loses" the child.</li>
     * <li> If the specified parent already had the specified child, then the
     * only effect of this call is to modify the label on the child.</li>
     * </ul>
     * 
     * @param parent the parent node
     * @param child the child node
     * @param label the label on the child
     * @return the old label if the specified parent-child already existed, or
     *         <tt>null</tt> otherwise.
     * @throws IllegalArgumentException if <tt>parent</tt> is not present in
     *             this tree.
     */
    public L putChild(N parent, N child, L label) {
        if (parent.equals(child)) {
            throw new IllegalArgumentException("parent and child are same.");
        }
        if (!graph.containsVertex(parent)) {
            throw new IllegalArgumentException("specified parent is not present in tree; must add parent before child.");
        }
        L res;
        if (graph.containsVertex(child)) {
            res = dissociateChild(child);
        } else {
            res = null;
        }
        graph.putEdge(parent, child, label);
        return res;
    }

    private L dissociateChild(N child) {
        if (isRoot(child)) {
            return null;
        }
        return graph.removeEdge(getParent(child), child);
    }

    /**
     * Pivots the tree around the specified node. The following modifications
     * occur as a result of this call:
     * <ul>
     * <li>for all nodes on the path from the root to the specified node, the
     * parent-child relationship is reversed; the label on a node is transferred
     * to its parent.</li>
     * <li>the specified node becomes the root of this tree</li>
     * </ul>
     * <br>
     * For example, consider the tree
     * 
     * <pre>
     *             1
     *          /  |  \
     *         2   3   4
     *        / \      |
     *       5  6      7 
     * </pre>
     * 
     * <br>
     * <ul>
     * <li>Pivoting around 1 leaves the tree unchanged.</li>
     * <li>Pivoting around 2 changes the tree to:
     * 
     * <pre>
     *             2
     *          /  |  \
     *         5   6   1
     *                / \     
     *               3   4
     *                   | 
     *                   7   
     * </pre>
     * 
     * The label on 2 is transferred to 1. </li>
     * <li>Pivoting the original tree around 5 changes the tree to:
     * 
     * <pre>
     *             5
     *             |  
     *             2
     *            / \   
     *           6   1
     *              / \     
     *             3   4
     *                 | 
     *                 7   
     * </pre>
     * 
     * The label on 2 is transferred to 1; the label on 5 is transferred to 2.
     * </li>
     * </ul>
     * 
     * @param child the node around which the tree is to be pivoted.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public void pivot(N child) {
        if (isRoot(child)) {
            return;
        }
        // N parent = getParent(child);
        // L label = dissociateChild(child);
        // putChild(child, parent, label);
        N newRoot = child;
        N parent = getParent(child);
        while (!isRoot(child)) {
            N grandParent = getParent(parent);
            L label = graph.removeEdge(parent, child);
            graph.putEdge(child, parent, label);
            child = parent;
            parent = grandParent;
        }
        this.root = newRoot;
    }

    /**
     * Sets the specified node as the root of this tree. The following
     * modifications occur as a result of this call:
     * <ul>
     * <li>the specified node is set as the root of this tree.</li>
     * <li>the old root is added as a child of the new root.</li>
     * <li>the specified label is set on the old root</li>
     * </ul>
     * <br>
     * For example, consider the tree
     * 
     * <pre>
     *             1
     *          /  |  \
     *         2   3   4
     *        / \      |
     *       5  6      7 
     * </pre>
     * 
     * <br>
     * <ul>
     * <li>Setting root as 1 leaves the tree unchanged.</li>
     * <li>Setting root as 2 changes the tree to:
     * 
     * <pre>
     *             2
     *          /  |  \
     *         5   6   1
     *                / \     
     *               3   4
     *                   | 
     *                   7   
     * </pre>
     * 
     * The specified label is set on 1. </li>
     * <li>Setting root as 5 (in the original tree) changes the tree to:
     * 
     * <pre>
     *             5
     *             | 
     *             1
     *          /  |  \
     *         2   3   4
     *         |       |
     *         6       7  
     * </pre>
     * 
     * The specified label is set on 1. </li>
     * <li>Setting root as 8 (in the original tree) changes the tree to:
     * 
     * <pre>
     *             8
     *             | 
     *             1
     *          /  |  \
     *         2   3   4
     *        / \      |
     *       5  6      7 
     * </pre>
     * 
     * The specified label is set on 1. </li>
     * </ul>
     * 
     * @param newRoot the node to be set as the new root of this tree.
     * @param label the label to be set on the current root of this tree.
     */
    public void setRoot(N newRoot, L label) {
        if (newRoot == null) {
            throw new NullPointerException("tree node cannot be null.");
        }
        if (getRoot().equals(newRoot)) {
            return;
        }
        putChild(root, newRoot, label);
        pivot(newRoot);
    }

    /**
     * Returns the root of this tree.
     * 
     * @return the root of this tree.
     */
    public N getRoot() {
        return root;
    }

    /**
     * Adds a disjoint subtree to this tree. As a result of this call, the root
     * of the specified subtree becomes a child of the specified parent; the
     * specified label is set on this new child. <br>
     * Note that the subtree is not modified in any way. An
     * <tt>IllegalArgumentException</tt> is thrown if any node in the subtree
     * is already present in this tree.
     * 
     * @param parent the node under which the specified subtree is to be added.
     * @param subTree the subtree to be added.
     * @param label the label to be set on the node that is the root of the
     *            subtree.
     * @throws IllegalArgumentException if a node of the subtree is already
     *             present in this tree.
     */
    public void addSubTree(N parent, Tree<? extends N, ? extends L> subTree, L label) {
        if (overlaps(subTree)) {
            throw new IllegalArgumentException();
        }
        addSubTree2(parent, subTree, label);
    }

    private <N1 extends N, L1 extends L> void addSubTree2(N parent, Tree<N1, L1> subTree, L label) {
        N1 subTreeRoot = subTree.getRoot();
        putChild(parent, subTreeRoot, label);
        for (Map.Entry<N1, L1> entry : subTree.getChildrenWithLabels(subTreeRoot).entrySet()) {
            addSubTree2(subTreeRoot, subTree.subTree(entry.getKey()), entry.getValue());
        }
    }

    private boolean overlaps(Tree<?, ?> other) {
        return allNodes().removeAll(other.allNodes());
    }

    /**
     * Returns all the nodes in this tree (in no particular order).
     * 
     * @return all the nodes in this tree.
     */
    public Set<N> allNodes() {
        return graph.getVertices();
    }

    /**
     * Returns the subtree with specified node as root. <br>
     * <b>Subtree definition</b> (wikipedia) : Any node in a tree T, together
     * with all the nodes below it, comprise a subtree of T. Note that the
     * subtree corresponding to the root node is the entire tree.
     * 
     * @param node the root of the subtree to be returned.
     * @return a subtree whose root is the specified node.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public Tree<N, L> subTree(N node) {
        Tree<N, L> res = new Tree<N, L>(node);
        for (Map.Entry<N, L> entry : getChildrenWithLabels(node).entrySet()) {
            // no need for overwrite
            res.addSubTree(node, subTree(entry.getKey()), entry.getValue());
        }
        return res;
    }

    /**
     * Removes the specified node and all the nodes below it. Note that if the
     * specified node is the root, then all nodes except the root are removed.
     * (The root can be modified using <tt>setRoot</tt>).
     * 
     * @param node the root of the subtree to be removed.
     * @return the removed subtree.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public Tree<N, L> removeSubTree(N node) {
        Tree<N, L> subTree = subTree(node);
        for (N child : getChildren(node)) {
            removeSubTree(child);
            graph.removeVertex(child);
        }
        if (!isRoot(node))
            graph.removeVertex(node);
        return subTree;
    }

    /**
     * Checks if the specified node is present in this tree.
     * 
     * @param node the node whose presence is to be checked.
     * @return <tt>true</tt> if the specified node is present in this tree.
     */
    public boolean containsNode(N node) {
        return graph.containsVertex(node);
    }

    /**
     * Checks if atleast one node of the specified tree is present in this tree.
     * 
     * @param other the tree whose nodes' presence is to be checked.
     * @return <tt>true</tt> if atleast one node of the specified tree is
     *         present in this tree also.
     */
    public boolean containsAnyNode(Tree<? extends N, ? extends L> other) {
        return allNodes().removeAll(other.allNodes());
    }

    /**
     * Returns the path, from the root, to the specified node. The returned path
     * contains the root as the first node and the specified node as the last
     * node.
     * 
     * @param node the node to which the path is to be found.
     * @return list of nodes on the path from the root to specified node, or
     *         <tt>null</tt> if the specified node is the root.
     * @throws IllegalArgumentException if the specified node is not present in
     *             this tree.
     */
    public List<N> getNodesPath(N node) {
        return getNodesPath(getRoot(), node);
    }

    /**
     * Returns the path from the specified source node to specified destination
     * node. The returned path contains the specified source as the first node
     * and the specified destination as the last node.
     * 
     * @param source the source node
     * @param destination the destination node
     * @return list of nodes on the path from the root to specified node, or
     *         <tt>null</tt> if neither node is a descendant of another.
     * @throws IllegalArgumentException if either of the specified nodes is not
     *             present in this tree.
     */
    public List<N> getNodesPath(N source, N destination) {
        return first(graph.getVertexPaths(source, destination));
    }

    /**
     * Returns the number of nodes in this tree.
     * 
     * @return number of nodes in this tree.
     */
    public int size() {
        return graph.numVertices();
    }

    private static <T> T first(Set<T> set) {
        if (set.size() > 1) {
            throw new IllegalStateException();
        }
        if (set.isEmpty()) {
            return null;
        } else {
            return set.iterator().next();
        }
    }

    /**
     * Compares the specified object with this tree for equality. Returns
     * <tt>true</tt> if the given object is also a tree and the two trees
     * represent the same tree structure. More formally, two tree instances
     * <tt>t1</tt> and <tt>t2</tt> represent the same tree if
     * <tt>t1.allNodes().equals(t2.allNodes())</tt> and for every node
     * <tt>n</tt> in <tt>t1.allNodes()</tt>,
     * <tt> (t1.getChildrenWithLabels(v).equals(t2.getChildrenWithLabels(v))) </tt>.
     * 
     * @param o object to be compared for equality with this tree.
     * @return <tt>true</tt> if the specified object is equal to this tree.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tree)) {
            return false;
        }
        Tree other = (Tree) obj;
        return graph.equals(other.graph);
    }

    @Override
    public int hashCode() {
        return graph.hashCode();
    }

    /**
     * Returns a string representation of this tree. The string representation
     * consists of the set of children of each node.
     * 
     * @return a string representation of this tree.
     */
    @Override
    public String toString() {
        return graph.toString();
    }

    /**
     * Returns a shallow copy of this <tt>Tree</tt> instance: the nodes and
     * labels themselves are not cloned.
     * 
     * @return a shallow copy of this tree.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Tree<N, L> clone() {
        Tree<N, L> res = null;
        try {
            res = (Tree<N, L>) super.clone();
        } catch (CloneNotSupportedException e) {
            // can't occur
        }
        res.assign(this);
        return res;
    }

    /**
     * Performs a reverse clone i.e. initializes this tree instance to be the
     * same as the specified tree.
     * 
     * @param tree the tree instance to be reverse-cloned.
     * @throws NullPointerException if the specified tree is <tt>null</tt>
     */
    public void assign(Tree<? extends N, ? extends L> tree) {
        if (tree == null) {
            throw new NullPointerException();
        }
        initGraph();
        graph.assign(tree.graph);
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        if (!graph.isTree()) {
            throw new IllegalArgumentException();
        }
        Set<N> roots = graph.getUnreachableVertices();
        if (roots.size() != 1) {
            throw new IllegalArgumentException();
        }
        root = first(roots);
    }
}
