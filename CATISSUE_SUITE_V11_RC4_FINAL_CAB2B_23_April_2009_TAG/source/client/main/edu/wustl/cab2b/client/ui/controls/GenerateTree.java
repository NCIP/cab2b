/**
 * <p>Title: GenerateTree Class>
 * <p>Description:	GenerateTree generates tree for the storage structure.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.cab2b.client.ui.controls;

import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTree;

import edu.wustl.cab2b.common.util.TreeNode;

/**
 * GenerateTree generates tree for the storage structure.
 * @author gautam_shetty
 * @author chetan_patil
 */
public class GenerateTree {

    /**
     * Default constructor
     */
    public GenerateTree() {

    }

    /**
     * Creates and returns the JTree from the vector of data nodes passed.
     * @param dataVector the data vector.
     * @param treeType the type of tree.
     * @return the JTree from the vector of data nodes passed.
     */
    public JXTree createTree(TreeNode<?> rootNode) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);

        //Create the hierarchy under the root node.
        createHierarchy(root, rootNode.getChildren());

        return new JXTree(root);
    }

    /**
     * Creates the hierarchy of nodes under the parent node with the child nodes passed.
     * @param parentNode the parent node.
     * @param dNodchildNodeses the child nodes.
     */
    private void createHierarchy(DefaultMutableTreeNode parentNode, List<?> children) {
        Iterator<?> iterator = children.iterator();
        while (iterator.hasNext()) {
            TreeNode<?> childNode = (TreeNode<?>) iterator.next();
            DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(childNode);
            parentNode.add(childTreeNode);

            createHierarchy(childTreeNode, childNode.getChildren());
        }
    }

}
