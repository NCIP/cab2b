package edu.wustl.cab2b.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree node
 * @author Chetan Patil
 */
public class TreeNode<T> implements Serializable, Cloneable {
    private static final long serialVersionUID = -7880358558369168948L;

    private T value;

    private List<TreeNode<T>> children;

    private TreeNode<T> parent;

    /**
     * @param value value
     */
    public TreeNode(T value) {
        setValue(value);
    }

    /**
     * @param value child value to add
     * @return child node
     */
    public TreeNode<T> addChildValue(T value) {
        TreeNode<T> child = new TreeNode<T>(value);
        addChild(child);
        return child;
    }

    /**
     * @return Value
     */
    public T getValue() {
        return value;
    }

    /**
     * @param value value
     */
    private void setValue(T value) {
        this.value = value;
    }

    /**
     * @return All children
     */
    public List<TreeNode<T>> getChildren() {
        if (children == null) {
            children = new ArrayList<TreeNode<T>>();
        }
        return children;
    }

    /**
     * @param child child to add
     */
    public void addChild(TreeNode<T> child) {
        getChildren().add(child);
        child.setParent(this);
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

    /**
     * @return TRUE if this node is leaf
     */
    public boolean isLeaf() {
        return (children == null) || (children.size() == 0);
    }

    /**
     * @return TRUE if this node is root
     */
    public boolean isRoot() {
        return getParent() == null;
    }
    
    /**
     * @return String representation
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return value.toString();
    }

    /**
     * @return TRUE if equals
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean flag = false;
        if (!TreeNode.class.equals(obj.getClass())) {
            TreeNode<T> treeNode = (TreeNode<T>) obj;
            if (this.getValue().equals(treeNode.getValue()) && this.getParent().equals(treeNode.getParent())
                    && this.getChildren().equals(treeNode.getChildren())) {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * @return hash code
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int hashCode = 0;
        if (null != value && null != parent && null != children) {
            hashCode = value.hashCode() + parent.hashCode() + children.hashCode();
        }
        return hashCode;
    }
}
