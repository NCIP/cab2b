package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.dag.MainDagPanel;
import edu.wustl.cab2b.client.ui.main.B2BStackedBox;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.IExpressionId;

/**
 * This is the panel for the choose category tab from the main search dialog. The
 * class is also an instance of the {@link ContentPanel},so that child
 * component can cause this panel to refresh in a way required by this panel.
 * 
 * @author mahesh_iyer
 */

public class ChooseCategoryPanel extends ContentPanel
{
	
	/**
	 * Constructor
	 *
	 */

	ChooseCategoryPanel()
	{		
		initGUI();			
	}
	
	/**
	 * Method initializes the panel by appropriately laying out child components.
	 */

	public void initGUI()
	{
		/* The panel consists of 2 parts
		 * 1. StackedCollapsiblePanels.
		 * 2. Panel for the Advanced search*/
		
		this.setLayout(new  BorderLayout());				
		B2BStackedBox box = new B2BStackedBox();			
		/*
		 * Setting prefered size is required if there is an empty panel for
		 * WEST or for CENTER, else the other component takes the whole
		 * place
		 * 
		 */		
		box.setMinimumSize(new Dimension(263,122));
		ChooseCategoryCategorySearchPanel panel = new ChooseCategoryCategorySearchPanel(this);

		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,box,panel);
		pane.setOneTouchExpandable(true);
		box.setBorder(new EmptyBorder(1,1,1,1));
		panel.setBorder(new EmptyBorder(1,1,1,1));		
		this.add(BorderLayout.CENTER,pane);
	}

	/**
	 * The method is a custom implementation from {@link ContentPanel}
	 * interface. 
	 * @param panelToBeRefreshed
	 *            The panel to be refreshed.
	 */
	
	public void refresh(JXPanel[] arrPanel,String strClassName)
	{
		//TODO implementation needs to be completed
	}
	
	/**
	 * The method is a custom implementation for the refresh method from the
	 * {@link ContentPanel} interface.
	 * 
	 * @param panel the panel to be added
	 */

	public void refreshBottomCenterPanel(JXPanel panel)
    {
		//TODO implementation needs to be completed
    }    
	
	
	/**
	 * This method takes the newly added expression and renders the node accordingly
	 * @param expressionId
	 */
	public void refreshBottomCenterPanel(IExpressionId expressionId)
	{
		SearchCenterPanel searchCenterPanel = (SearchCenterPanel)this.getParent();	   
		AddLimitPanel panel = (AddLimitPanel)searchCenterPanel.getComponent(1);
		try 
    	{
			((MainDagPanel)panel.getBottomCenterPanel()).updateGraph(expressionId);
		}
		catch (MultipleRootsException e) 
		{
			CommonUtils.handleException(e, this, true, true, false, false);
			//e.printStackTrace();
		}
        this.updateUI();
	}
	
	
	public void setSearchResultPanel(AbstractSearchResultPanel searchResultPanel)
	{
		SearchCenterPanel searchCenterPanel = (SearchCenterPanel)this.getParent();	   
		AddLimitPanel panel = (AddLimitPanel)searchCenterPanel.getComponent(1);
	
		panel.setSearchResultPanel(searchResultPanel);
	}
	
	/*
	 * Given the flow of events, the method will be called when the
	 * dynamically generated UI is loaded into an instance of the
	 * AddLimitPanel. Thus we need to be able to get a reference to that
	 * instance and be able to set the query instance in the main DAG Panel.
	 */
	
   public void setQueryObject(IClientQueryBuilderInterface query)
   {	   
	   /* For this instance get a handle to the SerchCenterPanel.*/
	   
	   //System.out.println(" Inside set query object : ");
	   SearchCenterPanel searchCenterPanel = (SearchCenterPanel)this.getParent();	   
	   AddLimitPanel panel = (AddLimitPanel)searchCenterPanel.getComponent(1);
	   panel.setQueryObject(query);
	   
   }
}

