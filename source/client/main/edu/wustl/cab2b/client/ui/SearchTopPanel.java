package edu.wustl.cab2b.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mahesh_iyer
 * 
 * This is the main dialog for building the B2B query. 
 */

public class SearchTopPanel extends Cab2bPanel
{	
	
	/*The searchPanel of labels*/
	private JXPanel[] m_arrSearchPanels = new Cab2bPanel[5];
	
	
	/*Constructor to initialize the top searchPanel for the search dialog.*/
	SearchTopPanel()
	{		
		initGUI();
		Logger.out.debug("SearchTopPanel ::: initGUI :: size "+getSize()+" preferredSize "+getPreferredSize());
	}
	
	private void initGUI()
	{
		this.setLayout(new GridLayout(1,5));
		/*Construct the labels and set the preferred sice and border*/
		JLabel lblCategorySearch = new Cab2bLabel("  1. Choose Search Category");
		/*
		 * The center searchPanel corresponding to this one would be selected, and
		 * hence there are no borders or background colors
		 */	
		lblCategorySearch.setPreferredSize(new Dimension(197,39));		
		JXPanel subCategPanel = new Cab2bPanel();		
		subCategPanel.setLayout(new GridLayout(1,1));		
		subCategPanel.add(lblCategorySearch);		
		subCategPanel.setBackground(Color.WHITE);
		this.m_arrSearchPanels[0] = subCategPanel;
		
		
		
		/*Construct the labels and set the preferred sice and border*/
		JLabel lblAddLimits = new Cab2bLabel("  2. Add Limits");
		lblAddLimits.setPreferredSize(new Dimension(197,39));		
		/*Add the label to the corresponding sub-searchPanel*/
		JXPanel subAddLimitPanel = new Cab2bPanel();
		subAddLimitPanel.setBorder(new LineBorder(Color.BLACK));
		//subAddLimitPanel.setBackground(new Color(13034239));
		subAddLimitPanel.setBackground(new Color(185, 211, 238));
		
		subAddLimitPanel.setLayout(new GridLayout(1,1));
		subAddLimitPanel.add(lblAddLimits);				
		/*Assign as the second element in array.*/
		this.m_arrSearchPanels[1]=subAddLimitPanel;
		
		
		/*Construct the labels and set the preferred sice and border*/
		JLabel lblDefineView = new Cab2bLabel("  3. Define Search Results View");
		lblDefineView.setPreferredSize(new Dimension(197,39));
		/*Add the label to the corresponding sub-searchPanel*/
		JXPanel subDefineViewPanel = new Cab2bPanel();
		subDefineViewPanel.setBorder(new LineBorder(Color.BLACK));
		subDefineViewPanel.setBackground( new Color(185, 211, 238));
		subDefineViewPanel.setLayout(new GridLayout(1,1));
		subDefineViewPanel.add(lblDefineView);				
		/*Assign as the second element in array.*/
		this.m_arrSearchPanels[2]=subDefineViewPanel;
		
		
		/*Construct the labels and set the preferred sice and border*/
		JLabel lblViewResults = new Cab2bLabel("  4. View Search Results");
		lblViewResults.setPreferredSize(new Dimension(197,39));
		
		/*Add the label to the corresponding sub-searchPanel*/
		JXPanel subViewResultsPanel = new Cab2bPanel();
		subViewResultsPanel.setBorder(new LineBorder(Color.BLACK));
		//subViewResultsPanel.setBackground( new Color(8900346));
		subViewResultsPanel.setBackground( new Color(185, 211, 238));
		subViewResultsPanel.setLayout(new GridLayout(1,1));
		subViewResultsPanel.add(lblViewResults);				
		/*Assign as the second element in array.*/
		this.m_arrSearchPanels[3]=subViewResultsPanel;
		
		
		/*Construct the labels and set the preferred sice and border*/
		JLabel lblDataList = new Cab2bLabel("  5. Data List");
		lblDataList.setPreferredSize(new Dimension(197,39));
		
		/*Add the label to the corresponding sub-searchPanel*/
		JXPanel subDataListPanel = new Cab2bPanel();
		subDataListPanel.setBorder(new LineBorder(Color.BLACK));
		subDataListPanel.setBackground( new Color(185, 211, 238));
		subDataListPanel.setLayout(new GridLayout(1,1));
		subDataListPanel.add(lblDataList);				
		/*Assign as the second element in array.*/
		this.m_arrSearchPanels[4]=subDataListPanel;
		
		int iPanelCount = this.m_arrSearchPanels.length;
		
		/*Add all the elements of the searchPanel array to the main searchPanel.*/
		for(int i =0;i<iPanelCount;i++){
			this.add(this.m_arrSearchPanels[i]);
		}		
	}

	/*
	 * The method treats the searchPanel/label indicated by index to be the one that
	 * is selected. The boolean indicates the direction of the traversal, so
	 * that the adjacent searchPanel/label can also be accordingly rendered to
	 * indicate that it is no more selected.
	 */
	public void setFocus(int index, boolean blnForward)
	{
		
		JXPanel selectedPanel = this.m_arrSearchPanels[index];
		JXPanel adjacantPanel = blnForward?this.m_arrSearchPanels[index-1]:this.m_arrSearchPanels[index+1];
		
		selectedPanel.setBackground(Color.WHITE);
		selectedPanel.setBorder(null);
		
		adjacantPanel.setBackground(new Color(185, 211, 238));
		adjacantPanel.setBorder(new LineBorder(Color.BLACK));
		
		this.updateUI();
		
		
	}
	
	
}
