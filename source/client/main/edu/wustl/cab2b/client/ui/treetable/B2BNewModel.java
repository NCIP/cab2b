package edu.wustl.cab2b.client.ui.treetable;

import java.io.File;




public class B2BNewModel extends AbstractTreeTableModel 
                             
{
	
    // Names of the columns.
    static protected String[]  cNames = {"Attribute", "Value"};

    // Types of the columns.
    static protected Class[]  cTypes = {TreeTableModel.class, String.class};

    // The the returned file length for directories. 
    public static final Integer ZERO = new Integer(0); 

    // This is an instance of B2BTreeNode
    public B2BNewModel(B2BTreeNode b2bNode)
    { 
    	super(b2bNode); 
    }


    /*
     * Implement the method to return children for this instance of B2BTreeNode
     */
    protected Object[] getChildren(Object node) 
    {
    		return ((B2BTreeNode)node).getChildren();
    }


    public int getChildCount(Object node) 
    {     	   
    	return ((B2BTreeNode)node).getChildren().length;
    }

    public Object getChild(Object node, int i) 
    {    
    	return getChildren(node)[i]; 
    }

    // Check if this instance of ICategorialClassRecords has child  
    public boolean isLeaf(Object node)
    { 
    
    	B2BTreeNode b2bNode = (B2BTreeNode)node;
    	if(b2bNode.getChildren()== null)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    //
    //  The TreeTableNode interface. 
    //

    public int getColumnCount() 
    {
    	return cNames.length;
    }

    public String getColumnName(int column) 
    {
    	return cNames[column];
    }

    public Class getColumnClass(int column) 
    {
    	return cTypes[column];
    }
 
    
    public Object getValueAt(Object node, int column) 
    {
    	/*Cast to B2BTreeNode*/
    	B2BTreeNode treeNode = (B2BTreeNode)node;
    	try 
    	{
    		switch(column) 
    		{
    			case 0:
    				return treeNode.toString();
    			case 1:
    				return treeNode.getValue();
    		}
    	}
    	catch  (Exception se) 
    	{ 
    		
    	}
    	return null; 
    }
}






