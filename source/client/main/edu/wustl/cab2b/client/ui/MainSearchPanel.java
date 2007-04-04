
package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mahesh_iyer
 * 
 * This is the main dialog for building the B2B query. 
 */

public class MainSearchPanel extends Cab2bPanel
{

	/**  The top panel consisting of the labels.*/
	private SearchTopPanel m_TopLabelPanel = new SearchTopPanel();

	/** The center panel.*/
	private SearchCenterPanel m_CenterPanel = new SearchCenterPanel();

	/** The bottom navigation panel.*/
	private SearchNavigationPanel m_BottomPanel = new SearchNavigationPanel(this);

	/** The query object generated when user submits the conditions in the add limit page.*/	
	public IClientQueryBuilderInterface queryObject = null;

	/** Data list of the user.*/
	public static DataList dataList = new DataList();

	/**
	 * @return Returns the queryObject.
	 */
	public IClientQueryBuilderInterface getQueryObject()
	{
		return queryObject;
	}

	/**
	 * @param queryObject The queryObject to set.
	 */
	public void setQueryObject(IClientQueryBuilderInterface queryObject)
	{
		this.queryObject = queryObject;
	}

	/**
	 * The getter method for the center panel.
	 * @return
	 */
	public SearchCenterPanel getCenterPanel()
	{
		return this.m_CenterPanel;
	}

	/**
	 * The getter method for the top panel.
	 * @return
	 */
	public SearchTopPanel getTopPanel()
	{
		return this.m_TopLabelPanel;

	}

	/**
	 * The getter method for the navigation panel.
	 * @return
	 */
	public SearchNavigationPanel getNavigationPanel()
	{
		return this.m_BottomPanel;

	}

	/*
	 * The dialog is made up of 3 panels;a top panel consisting of the label
	 * panel, the main panel as well as the navigation panel. The center panel
	 * has a card layout which is controlled by the navigation panel. The dialog
	 * itself has a border layout.
	 */
	public MainSearchPanel()
	{
		initGUI();
	}

	/**
	 * The method initializes the tabbed pane.
	 */
	private void initGUI()
	{
		this.setLayout(new BorderLayout());
		this.add(BorderLayout.NORTH, this.m_TopLabelPanel);
		this.add(BorderLayout.CENTER, this.m_CenterPanel);
		this.add(BorderLayout.SOUTH, this.m_BottomPanel);
		
		//Logger.configure();
	}

	public static void main(String[] args)
	{
		Logger.configure();
		JFrame dialog = new JFrame();
		dialog.setSize(976, 580);
		dialog.setTitle("Search Data");
	
		MainSearchPanel searchPanel = new MainSearchPanel();

		dialog.getContentPane().add(searchPanel);
		dialog.setVisible(true);
	}

}