/**
 * <p>Title: AdvanceQueryTreeRenderer Class </p>
 * <p>Description:	Tree Renderer class for Advance Query result tree. It will specify the images to be displayed for each node in the tree.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Prafull Kadam
 * @version 1.00
 */

package edu.wustl.common.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.wustl.common.util.global.Constants;

public class AdvanceQueryTreeRenderer extends DefaultTreeCellRenderer
{

	/**
	 * Configures the renderer based on the passed in components.
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		AdvanceQueryTreeNode treeNode = (AdvanceQueryTreeNode) node.getUserObject();
		Icon icon = null;
		if (Constants.SPECIMEN.equals(treeNode.getValue()))
		{
			icon = createImageIcon("Specimen.gif");
			setIcon(icon);
		}
		else if (Constants.COLLECTION_PROTOCOL.equals(treeNode.getValue()))
		{
			icon = createImageIcon("CollectionProtocol.gif");
			setIcon(icon);
		}
		else if (Constants.SPECIMEN_COLLECTION_GROUP.equals(treeNode.getValue()))
		{
			icon = createImageIcon("SpecimenCollectionGroup.gif");
			setIcon(icon);
		}
		else if (Constants.PARTICIPANT.equals(treeNode.getValue()))
		{
			icon = createImageIcon("Participant.gif");
			setIcon(icon);
		}

		return this;
	}

	/** 
	 * Returns an Icon, or null if the path was invalid. 
	 */
	protected Icon createImageIcon(String name)
	{
		Icon newLeafIcon = new ImageIcon(getClass().getClassLoader().getResource("images/" + name));
		return newLeafIcon;
	}
}