
/*
 * Created on Nov 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package edu.wustl.common.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import edu.wustl.common.util.global.Constants;



/**
 * @author ramya_nagraj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpecimenTreeRenderer extends DefaultTreeCellRenderer
{
	/**
	 * String containing the type of specimen 
	 */
	String specimenType;
	
	/**
	 * String containing the class of specimen 
	 */
	String specimenClass;
	
	/**
	 * public no-args constructor
	 */
	public SpecimenTreeRenderer()
	{
		super();
	}
	
	
	public SpecimenTreeRenderer(String specimenType,String specimenClass)
	{
		this();
		this.specimenType = specimenType;
		this.specimenClass = specimenClass;
	}
	 /**
     * Configures the renderer based on the passed in components.
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
  
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        SpecimenTreeNode treeNode = (SpecimenTreeNode)node.getUserObject();
    	
        //If the clicked node is root node (i.e, label for specimen tree),then dont display Specimen.gif icon. 
        Icon icon = createImageIcon("Specimen.gif");
        
    	if(!treeNode.toString().equals(Constants.SPECIMEN_TREE_ROOT_NAME))
    	{
    		/*The node is clickable when the specimen  type and specimen class of the node is same as that selected by user.
        	 * In that case,display enabled.gif image to indicate to the use that it is clickable.Otherwise,
        	 * display disabled.gif.
        	 */
    		
        	if(treeNode.getType().equalsIgnoreCase(specimenType) && treeNode.getSpecimenClass().equalsIgnoreCase(specimenClass))
        	{
        		icon = createImageIcon("enabled.gif");       
                setIcon(icon);
        	}
        	else
        	{
        		 icon = createImageIcon("disabled.gif");
        	}
    		
    	}
  
        setIcon(icon);
        return this;
    }
    
    /** 
     * Returns an Icon, or null if the path was invalid. 
     * 
     */
    protected Icon createImageIcon(String name) {
		Icon newLeafIcon = new ImageIcon(getClass().getClassLoader()
				.getResource("images/" + name));
			
		return newLeafIcon;
    }
}
