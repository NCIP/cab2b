/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.treetable;

import javax.swing.tree.*;
import javax.swing.event.*;

/**
 * AbstractTreeTableModel
 * 
 * @author Mahesh Iyer
 */
public abstract class AbstractTreeTableModel implements TreeTableModel {

    /** The root */
    protected Object root;

    /** The list of EventListener */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Constructor
     * 
     * @param root Root
     */
    public AbstractTreeTableModel(Object root) {
        this.root = root;
    }

    /**
     * @return root
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    public Object getRoot() {
        return root;
    }

    /**
     * Check if this instance of ICategorialClassRecords has child
     * 
     * @param node node to check
     * @return TRUE if given node is leaf
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    /**
     * @param path Tree path
     * @param newValue new value
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
     *      java.lang.Object)
     */
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    /**
     * @param parent parent
     * @param child child
     * @return index of given child
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object,
     *      java.lang.Object)
     */
    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            if (getChild(parent, i).equals(child)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param l TreeModelListener to add
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * @param l TreeModelListener to remove
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source source
     * @param path path
     * @param childIndices indexes of child
     * @param children children
     */
    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source source
     * @param path path
     * @param childIndices indexes of child
     * @param children children
     */
    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source source
     * @param path path
     * @param childIndices indexes of child
     * @param children children
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notify all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     * 
     * @param source source
     * @param path path
     * @param childIndices indexes of child
     * @param children children
     */
    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Default implementation for methods in the TreeTableModel interface.
     * 
     * @param column column
     * @return Class
     * @see edu.wustl.cab2b.client.ui.treetable.TreeTableModel#getColumnClass(int)
     */
    public Class getColumnClass(int column) {
        return Object.class;
    }

    /**
     * By default, make the column with the Tree in it the only edit-able one.
     * Making this column edit-able causes the JTable to forward mouse and
     * keyboard events in the Tree column to the underlying JTree.
     * 
     * @param node
     * @param column
     * @return TRUE if the cell is edit-able
     */
    public boolean isCellEditable(Object node, int column) {
        return getColumnClass(column) == TreeTableModel.class;
    }

    /**
     * @param value
     * @param node
     * @param column
     * @see edu.wustl.cab2b.client.ui.treetable.TreeTableModel#setValueAt(java.lang.Object,
     *      java.lang.Object, int)
     */
    public void setValueAt(Object value, Object node, int column) {}

}
