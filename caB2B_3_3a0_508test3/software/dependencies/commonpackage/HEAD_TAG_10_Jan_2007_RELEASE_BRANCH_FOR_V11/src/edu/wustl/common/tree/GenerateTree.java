/**
 * <p>Title: GenerateTree Class>
 * <p>Description:	GenerateTree generates tree for the storage structure.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.common.tree;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTree;

/**
 * GenerateTree generates tree for the storage structure.
 * @author gautam_shetty
 */
public class GenerateTree
{

	ArrayList list;
	String containerName;

	/**
	 * Default consructor
	 */
	public GenerateTree()
	{

	}

	/**
	 * parameterised consructor
	 */
	public GenerateTree(String containerName)
	{
		this.containerName = containerName;
	}
	
//	public JTree createTree(Vector dataVector, int treeType)
//    {
//		JTree tree = createTree(dataVector, treeType, false);
//		return tree;
//    }
	
    /**
     * Creates and returns the JTree from the vector of data nodes passed.
     * @param dataVector the data vector.
     * @param treeType the type of tree.
     * @return the JTree from the vector of data nodes passed.
     */
    public JTree createTree(Vector dataVector, int treeType,boolean isJXTree)
    {
        TreeNode rootName = null;
        if (dataVector != null && (dataVector.isEmpty()==false))
        {
            rootName = (TreeNode)dataVector.get(0);
        }
        
        //Get the root node.
        TreeNode root1 = TreeNodeFactory.getTreeNode(treeType, rootName);
        TreeNodeImpl rootNode = (TreeNodeImpl) root1;
        rootNode.setChildNodes(dataVector);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
        
        //Create the hierarchy under the root node.
        createHierarchy(root, dataVector);
        JTree tree;
        if(isJXTree)
        {
        	tree = new JXTree(root);
        }else
        {
        	tree = new JTree(root){
			public String getToolTipText(MouseEvent e) {
					String tip = "";
					TreePath path = getPathForLocation(e.getX(), e.getY());
					if (path != null) 
					{
						Object treeNode = path.getLastPathComponent();
						if (treeNode instanceof DefaultMutableTreeNode)
						{
							TreeNodeImpl userObject = (TreeNodeImpl)((DefaultMutableTreeNode)treeNode).getUserObject();
							tip = userObject.getToolTip();
						}
					}
					return tip;
				}
        	};
        	ToolTipManager.sharedInstance().registerComponent(tree);
        }
        return tree;
    }
    
    /**
	 * Creates and returns the JTree from the vector of data nodes passed.
	 * @param dataVector the data vector.
	 * @param treeType the type of tree.
	 * @param tempList List in which id is set
	 * @return the JTree from the vector of data nodes passed.
	 */

	public JTree createTree(Vector dataVector, int treeType, ArrayList tempList)
	{
		TreeNode rootName = null;
		if (dataVector != null && (dataVector.isEmpty() == false))
		{
			rootName = (TreeNode) dataVector.get(0);
		}

		//Get the root node.
		TreeNode root1 = TreeNodeFactory.getTreeNode(treeType, rootName);
		TreeNodeImpl rootNode = (TreeNodeImpl) root1;
		rootNode.setChildNodes(dataVector);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
		createHierarchy(root, dataVector);

		JTree tree = new JTree(root)
		{

			public String getToolTipText(MouseEvent e)
			{
				String tip = "";
				TreePath path = getPathForLocation(e.getX(), e.getY());
				if (path != null)
				{
					Object treeNode = path.getLastPathComponent();
					if (treeNode instanceof DefaultMutableTreeNode)
					{
						TreeNodeImpl userObject = (TreeNodeImpl) ((DefaultMutableTreeNode) treeNode).getUserObject();
						tip = userObject.getToolTip();
					}
				}
				return tip;
			}
		};
		if (list != null)
		{
			tree.setSelectionPath((TreePath) list.get(0));
			if(tempList!=null)
				tempList.add(list.get(1));
		}
		ToolTipManager.sharedInstance().registerComponent(tree);
		return tree;
		
	}
	
	/**
	 * Creates and returns the JTree from the vector of data nodes passed.
	 * @param dataVector the data vector.
	 * @param treeType the type of tree.
	 * @return the JTree from the vector of data nodes passed.
	 */
	public JTree createTree(Vector dataVector, int treeType)
	{
		return createTree(dataVector,treeType,null);
	}

    /**
     * Creates and returns the JTree from the vector of data nodes passed.
     * @param dataVector the data vector.
     * @param treeType the type of tree.
     * @return the JTree from the vector of data nodes passed.
     */
    public JTree createTree(TreeNodeImpl rootNode, boolean isJXTree)
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
        
        //Create the hierarchy under the root node.
        createHierarchy(root, rootNode.getChildNodes());
        JTree tree;
        if(isJXTree)
        {
            tree = new JXTree(root);
        }else
        {
            tree = new JTree(root){
            public String getToolTipText(MouseEvent e) {
                    String tip = "";
                    TreePath path = getPathForLocation(e.getX(), e.getY());
                    if (path != null) 
                    {
                        Object treeNode = path.getLastPathComponent();
                        if (treeNode instanceof DefaultMutableTreeNode)
                        {
                            TreeNodeImpl userObject = (TreeNodeImpl)((DefaultMutableTreeNode)treeNode).getUserObject();
                            tip = userObject.getToolTip();
                        }
                    }
                    return tip;
                }
            };
            ToolTipManager.sharedInstance().registerComponent(tree);
        }
        return tree;
    }
	/**
	 * Creates the hierarchy of nodes under the parent node with the child nodes passed.
	 * @param parentNode the parent node.
	 * @param childNodes the child nodes.
	 */
	private void createHierarchy(DefaultMutableTreeNode parentNode, Vector childNodes)
	{
		Iterator iterator = childNodes.iterator();
		while (iterator.hasNext())
		{
			TreeNodeImpl childNode = (TreeNodeImpl) iterator.next();
			DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(childNode);
			parentNode.add(childTreeNode);
			if (childNode.getValue() != null && containerName != null && childNode.getValue().equalsIgnoreCase(containerName.trim()))
			{
			    TreePath treePath = new TreePath(childTreeNode.getPath());
				list = new ArrayList();
				list.add(treePath);
				list.add(childNode.getIdentifier());
				
			}

			createHierarchy(childTreeNode, childNode.getChildNodes());
		}
	}

}

