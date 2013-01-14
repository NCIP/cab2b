/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.treetable;

import java.util.Vector;

/**
 * B2BNewModel
 * 
 * @author Deepak Shingan
 */
public class B2BNewModel extends AbstractTreeTableModel {

    /** Names of the columns. */
    private static final String[] columnNames = {"Attribute", "Value"};

    /** Types of the columns. */
    private static final Class[] columnTypes = {TreeTableModel.class, String.class};

    /**
     * This is an instance of B2BTreeNode
     * 
     * @param b2bNode
     */
    public B2BNewModel(B2BTreeNode b2bNode) {
        super(b2bNode);
    }

    /**
     * Implement the method to return children for this instance of B2BTreeNode
     * 
     * @param node
     * @return Vector of B2BTreeNode
     */
    protected Vector<B2BTreeNode> getChildren(Object node) {
        return ((B2BTreeNode) node).getChildren();
    }

    /**
     * @param node parent node
     * @return Number of children
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object node) {
        return ((B2BTreeNode) node).getChildren().size();
    }

    /**
     * @param node Parent node
     * @param i Child number
     * @return "i"TH child
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    public Object getChild(Object node, int i) {
        return getChildren(node).toArray()[i];
    }

    /**
     * Check if this instance of ICategorialClassRecords has child
     * 
     * @param node node to check
     * @return TRUE if given node is leaf
     * @see edu.wustl.cab2b.client.ui.treetable.AbstractTreeTableModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        B2BTreeNode b2bNode = (B2BTreeNode) node;
        if (b2bNode.getChildren() == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return column count
     * @see edu.wustl.cab2b.client.ui.treetable.TreeTableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * @param column column number
     * @return column name
     * @see edu.wustl.cab2b.client.ui.treetable.TreeTableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * @param column column number
     * @return class of object present is given column
     * @see edu.wustl.cab2b.client.ui.treetable.AbstractTreeTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        return columnTypes[column];
    }

    /**
     * @param node
     * @param column
     * @return value
     * @see edu.wustl.cab2b.client.ui.treetable.TreeTableModel#getValueAt(java.lang.Object,
     *      int)
     */
    public Object getValueAt(Object node, int column) {
        /* Cast to B2BTreeNode */
        B2BTreeNode treeNode = (B2BTreeNode) node;
        try {
            switch (column) {
                case 0 :
                    return treeNode.toString();
                case 1 :
                    return treeNode.getValue();
            }
        } catch (Exception se) {// TODO is it really needed
            se.printStackTrace();
        }
        return null;
    }
}
