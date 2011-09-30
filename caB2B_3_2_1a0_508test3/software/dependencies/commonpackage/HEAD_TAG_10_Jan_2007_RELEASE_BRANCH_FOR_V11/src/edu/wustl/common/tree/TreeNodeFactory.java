/*
 * Created on Aug 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.tree;

import edu.wustl.common.util.global.Constants;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TreeNodeFactory
{
    public static TreeNode getTreeNode(int treeType,TreeNode root)
    {
        TreeNode treeNode = null;
        switch(treeType)
        {
            case Constants.TISSUE_SITE_TREE_ID:
                CDETreeNode rootNode = (CDETreeNode) root;
            	String cdeName = null;
            	if (rootNode != null)
            	{
            	    cdeName = rootNode.getCdeName();
            	}
            	
                treeNode = new CDETreeNode(null, cdeName);
            	break;
            case Constants.STORAGE_CONTAINER_TREE_ID:
                treeNode = new StorageContainerTreeNode(new Long(0),null,Constants.CATISSUE_CORE);
            	break;
            case Constants.QUERY_RESULTS_TREE_ID:
            	treeNode = new AdvanceQueryTreeNode(new Long(0),Constants.ROOT,Constants.ALL);
                break;
            //Added By Ramya.
            //To display specimen tree hierarchy.
            case Constants.SPECIMEN_TREE_ID:
            	treeNode = new SpecimenTreeNode(null,Constants.SPECIMEN_TREE_ROOT_NAME);
            	break;
            case Constants.EXPERIMETN_TREE_ID:
                treeNode = new ExperimentTreeNode(new Long(0),"My Experiments");
                ((ExperimentTreeNode)treeNode).setExperimentGroup(true);
                break;
                
        }
        
        return treeNode;
    }
}
