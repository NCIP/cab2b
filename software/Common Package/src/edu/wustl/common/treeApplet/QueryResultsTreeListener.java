/**
 * <p>Title: QueryResultsTreeListener Class>
 * <p>Description: QueryResultsTreeListener handles the node selction event of the tree 
 * in the applet.</p>
 * Copyright: Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.common.treeApplet;
import java.applet.AppletContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import edu.wustl.common.tree.TreeNodeImpl;
import edu.wustl.common.util.global.Constants;

/**
 * QueryResultsTreeListener handles the node selction event of the tree in the applet.
 * @author gautam_shetty
 */
public class QueryResultsTreeListener implements TreeSelectionListener, ActionListener
{
    /**
     * The URL of the host from which the applet is loaded.
     */
    private URL codeBase = null;
    
    /**
     * Corresponds to an applet environment.
     */
    private AppletContext appletContext = null;
    
    /**
     * Defines the type of view to be showed on tree node selection.
     */
    private String viewType = new String(Constants.SPREADSHEET_VIEW);
    
    /**
     * Status whether a node is selected or not.
     */
    private boolean nodeSelectionStatus = false;
    
    /**
     * Name of selected node.
     */
    private String nodeName = null;
    
    //List of Ids to be disabled
    private List disableSpecimenIds = null;
    
    private boolean isDisabled=false;
    
    /**
     * Initializes an empty QueryResultsTreeListener.
     */
    public QueryResultsTreeListener()
    {
    }
    
    /**
     * Creates and initializes a QueryResultsTreeListener with the codeBase and appletContext.
     * @param codeBase2
     * @param appletContext2
     */
    public QueryResultsTreeListener(URL codeBase, AppletContext appletContext)
    {
        this.codeBase = codeBase;
        this.appletContext = appletContext;
    }
    
    /**
     * Returns true if a node is selected, else returns false.
     * @return true if a node is selected, else returns false.
     */
    private boolean isNodeSelected()
    {
        return nodeSelectionStatus;
    }
    
    /**
     * Implements and overrides the valueChanged method in TreeSelectionListener.
     */
    public void valueChanged(TreeSelectionEvent e)
    {
        
    	Object object = e.getSource();
        JTree t = null;
        
        if (object instanceof JTree)
        {
            t = (JTree) object;
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
            							   t.getLastSelectedPathComponent();
            TreeNodeImpl treeNodeData = (TreeNodeImpl) node.getUserObject();
            this.isDisabled=isdisabledSpecimenId(this.disableSpecimenIds,treeNodeData);
            
            this.nodeName = treeNodeData.getValue();
            if (!Constants.ROOT.equals(this.nodeName))
            	this.nodeName += ":"+ treeNodeData.getIdentifier();
            /* if node selected is collection protocol, send the parentId and objectName as Collection Protocol & Participant have 
             * many to many relation
             */
           	if(treeNodeData.getValue().equals(Constants.COLLECTION_PROTOCOL))
           	{
           	    TreeNodeImpl parentNode = (TreeNodeImpl)treeNodeData.getParentNode();
           		this.nodeName = treeNodeData.getValue()+":"+ treeNodeData.getIdentifier()+":"+parentNode.getValue()+":"+parentNode.getIdentifier();
           	}
            
            try
            {
                String urlSuffix = null;
                //If the node selected is Root and view is individual view, don't show anything.
                if (!this.isDisabled && !(nodeName.equals(Constants.ROOT) && viewType.equals(Constants.OBJECT_VIEW)))
                {
                	this.nodeSelectionStatus = true;
                    showDataView();
                }
            }
            catch (MalformedURLException malExp)
            {
            }
        }
    }
    
    /**
     * Shows the data view according to the view type and node selected.
     * @throws MalformedURLException
     */
    private void showDataView() throws MalformedURLException
    {
    	String protocol = codeBase.getProtocol();
    	String host = codeBase.getHost();
        int port = codeBase.getPort();
        
        String applicationPath = codeBase.getPath();
        // modify applicationPath String ...
		
        if(applicationPath.indexOf('/',1)!=-1){ //indexOf returns -1 if no match found
			String newApplicationPath=null;
			newApplicationPath = applicationPath.substring(0,applicationPath.indexOf('/',1)+1);
			applicationPath=newApplicationPath;
			
        }
        Random random = new Random();
        int dummyParameter = random.nextInt();
        String urlSuffix = applicationPath + Constants.DATA_VIEW_ACTION + nodeName + 
        			   "&"+ Constants.VIEW_TYPE + "=" + viewType+"&dummyParameter="+dummyParameter;
        
        URL dataURL = new URL(protocol,host,port,urlSuffix);
        appletContext.showDocument(dataURL,Constants.DATA_VIEW_FRAME);
    }
    
    /**
     * Action performed on selecting the radio buttons.
     */
    public void actionPerformed(ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand.equals(Constants.SPREADSHEET_VIEW))
        {
            this.viewType = new String(Constants.SPREADSHEET_VIEW);
        }
        else
        {
            this.viewType = new String(Constants.OBJECT_VIEW);
        }
        
        if (isNodeSelected() && !(nodeName.equals(Constants.ROOT)) && !this.isDisabled)
        {
        	try
            {
                showDataView();
            }
            catch (MalformedURLException malExp)
            {
                
            }
        }
    }
	
    /**
	 * @return Returns the disableSpecimenIds.
	 */
	public List getDisableSpecimenIds() {
		return disableSpecimenIds;
	}
	
	/**
	 * @param disableSpecimenIds The disableSpecimenIds to set.
	 */
	public void setDisableSpecimenIds(List disableSpecimenIds) {
		this.disableSpecimenIds = disableSpecimenIds;
	}
	
	private boolean isdisabledSpecimenId(List disableSpecimenIds,TreeNodeImpl treeNodeData)
	{
		if(disableSpecimenIds.contains(treeNodeData.getIdentifier()))
				return true;
		else 
			return false;
				
	}
}