/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> implements Serializable, Cloneable {
    private T value;

    private List<TreeNode<T>> children;

    private TreeNode<T> parent;

    public TreeNode(T value) {
        setValue(value);
    }

    public TreeNode<T> addChildValue(T value) {
        TreeNode<T> child = new TreeNode<T>(value);
        addChild(child);
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

    public boolean isLeaf() {
        return (children == null) || (children.size() == 0);
    }

    public boolean isRoot() {
        return getParent() == null;
    }
    
    public String toString() {
        return value.toString();
    }

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

    /* (non-Javadoc)
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
