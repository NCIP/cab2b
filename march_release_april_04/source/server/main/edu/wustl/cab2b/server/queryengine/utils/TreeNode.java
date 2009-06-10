package edu.wustl.cab2b.server.queryengine.utils;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private T value;

    private List<TreeNode<T>> children;

    private TreeNode<T> parent;

    public TreeNode(T value) {
        setValue(value);
    }

    public TreeNode<T> addChildValue(T value) {
        TreeNode<T> child = new TreeNode<T>(value);
        getChildren().add(child);
        child.setParent(this);
        return child;
    }

    public T getValue() {
        return value;
    }

    /**
     * @param group
     */
    private void setValue(T value) {
        this.value = value;
    }

    public List<TreeNode<T>> getChildren() {
        if (children == null) {
            children = new ArrayList<TreeNode<T>>();
        }
        return children;
    }

    /**
     * @return Returns the parent.
     */
    public TreeNode<T> getParent() {
        return parent;
    }

    private void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return (children == null) || (children.size() == 0);
    }

    public boolean isRoot() {
        return getParent() == null;
    }
}
