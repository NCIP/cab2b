package edu.wustl.cab2b.client.ui;


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JOptionPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.experiment.NewExperimentDetailsPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.client.ui.viewresults.DataListPanel;
import edu.wustl.cab2b.client.ui.viewresults.ViewSearchResultsPanel;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IClassRecords;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.common.util.logger.Logger;
 



/**
 * @author mahesh_iyer
 * 
 * The class represents the navigation panel for the search dialog, and also
 * includes corresponding functionality.
 */

public class SearchNavigationPanel extends Cab2bPanel implements ActionListener
{
	private static final long serialVersionUID = 1234567890L;

	/* The reference to the parent panel.*/
	private MainSearchPanel m_mainSearchPanel = null;

	/* The navigation buttons.*/	
	private Cab2bButton prvButton;
	private Cab2bButton nextButton;
	
	/*Custom buttons for this dialog.*/
	private Cab2bButton srhButton;
	private Cab2bButton saveDataListButton;
	private Cab2bButton addToExperimentButton;
	

	SearchNavigationPanel(MainSearchPanel panel)
	{
		this.m_mainSearchPanel = panel;
		initGUI();
		this.setPreferredSize(new Dimension(976,36));
		this.setBackground(Color.LIGHT_GRAY);
	}	
		
	private void initGUI()
	{
		
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		flowLayout.setHgap(10);
		
		this.setLayout(flowLayout);
		srhButton = new Cab2bButton("Reset");
		prvButton = new Cab2bButton("Previous");
		nextButton = new Cab2bButton("Next");
		
		saveDataListButton = new Cab2bButton("Save Data List");
	  	saveDataListButton.setPreferredSize(new Dimension(160,22));
	  	addToExperimentButton = new Cab2bButton("Add to Experiment");    
	  	addToExperimentButton.setPreferredSize(new Dimension(160,22));	  	
			
		nextButton.addActionListener(this);
		prvButton.addActionListener(this);
		srhButton.addActionListener(this);
		addToExperimentButton.addActionListener(this);
		saveDataListButton.addActionListener(this);
		
		add(srhButton);
		add(prvButton);
		add(nextButton);
		add(saveDataListButton);
	    add(addToExperimentButton);
		
		
		srhButton.setVisible(false);
		prvButton.setVisible(false);
		saveDataListButton.setVisible(false);
		addToExperimentButton.setVisible(false);
	}
	
	/**
	 * Action listener class for "Reset", "Previous" and "Next" Buttons.
	 */
	public  void actionPerformed(ActionEvent event)
	{
		String strActionCommand = ((Cab2bButton)event.getSource()).getActionCommand();		
		if(strActionCommand.equals("Save Data List"))
		{
			Logger.out.info("Clicked on save Data List button");
			
			SaveDatalistPanel saveDataListPanel = new SaveDatalistPanel(m_mainSearchPanel);
			saveDataListPanel.showInDialog();
		}else
		if(strActionCommand.equals("Add to Experiment"))
		{
			Logger.out.info("Clicked on Add to Experiment button");
			//check if Data List is saved
			if(SaveDatalistPanel.isDataListSaved == false)
			{
				SaveDatalistPanel saveDataListPanel = new SaveDatalistPanel(m_mainSearchPanel);
				saveDataListPanel.showInDialog();	
			}	
			if(SaveDatalistPanel.isDataListSaved)
			{
				NewExperimentDetailsPanel newExperimentDetailsPanel = new NewExperimentDetailsPanel();
				newExperimentDetailsPanel.showInDialog();
			}
		}else
		if(strActionCommand.equals("Next"))
		{
			srhButton.setVisible(false);
			prvButton.setVisible(true);
            if (m_mainSearchPanel.getCenterPanel().getSelectedCardIndex() == 1)
            {
               	//Gautam:
                //If the central panel is AddLimitPanel, update the query object with the 
                //logical operators between the rules.
                //m_mainSearchPanel.updateQueryObject();
                if(m_mainSearchPanel.getQueryObject()!= null)
                {
                	/*
    				 * Also cause for the next card in the dialog to be added
    				 * dynamically to the dialog.
    				 */                	
                	
                	AdvancedDefineViewPanel defineViewPanel = new AdvancedDefineViewPanel(this.m_mainSearchPanel.getCenterPanel());
                	
                	if(AdvancedDefineViewPanel.isMultipleGraphException == true)
                	{
                		gotoAddLimitPanel();
                	}else
                	{                		
                		if(null != m_mainSearchPanel.getCenterPanel().m_arrCards[2])
                		{
                			m_mainSearchPanel.getCenterPanel().remove(m_mainSearchPanel.getCenterPanel().m_arrCards[2]);
                		}	
                		m_mainSearchPanel.getCenterPanel().m_arrCards[2] = defineViewPanel;
                		m_mainSearchPanel.getCenterPanel().add(defineViewPanel, SearchCenterPanel.m_strDefineSearchResultslbl);
                		/*
                		 * 	Implies the next button was clicked. Call show card with boolean set to true.
                		 */
                		showCard(true);
                	}
                }
                else
                {
                	srhButton.setVisible(true);
                	saveDataListButton.setVisible(false);
                	addToExperimentButton.setVisible(false);
                	/*Pop-up a dialog asking the user to add alteast a rule.*/
                	JOptionPane.showInternalMessageDialog(this.m_mainSearchPanel.getParent(),"Please add a rule,before proceeding.","Information", JOptionPane.INFORMATION_MESSAGE);
                	return;                	
                }
              
            }
            else if(m_mainSearchPanel.getCenterPanel().getSelectedCardIndex() == 2)
            {
            	CustomSwingWorker swingWorker = new CustomSwingWorker(this.m_mainSearchPanel)
            	{
            		IQueryResult queryResults;
            		@Override
            		protected void doNonUILogic()
            		{
            			/* Get the Functional class for root and update query object
            			 * with it.
            			 */            			
            			queryResults = CommonUtils.executeQuery((ICab2bQuery)m_mainSearchPanel.getQueryObject().getQuery(), m_mainSearchPanel);     				
    				         				            				     
            		}            		
            	            		
            		@Override
            		protected void doUIUpdateLogic()
            		{
            			prvButton.setVisible(true);            	  	
                	  	nextButton.setVisible(true);    
                	  	
                	  	saveDataListButton.setVisible(false);
                		addToExperimentButton.setVisible(false);    
            			
            			if(queryResults != null)
            			{
            			
            				Logger.out.info("Inside doUIUpdateLogic ");
            			
            				/*
        					 * Based on the output selected i.e. class or category, cast
        					 * the results class accordingly. Basic assumptions is
        					 * object returned will not be null.
        					 */
                    		
            				
            				if(!queryResults.isCategoryResult())
            				{
            				
            					IClassRecords classRecords = (IClassRecords)queryResults;	
            					
            					Map<String, String[][]> allRecords = classRecords.getAllRecords();
            					Iterator ittr = allRecords.keySet().iterator();
            					if (ittr.hasNext())
            					{
            						/* Set the url for each data row. */
            						String urlKey = (String) ittr.next();
            						Object[][] results = allRecords.get(urlKey);															
            						if( results.length == 0 || classRecords.getAllRecords().size() == 0 )
            						{
            							JOptionPane.showMessageDialog(null, "No result found.", "", JOptionPane.INFORMATION_MESSAGE);
            							srhButton.setVisible(true);   
            							gotoAddLimitPanel();
            			      	
            						}else
            						{
									    ViewSearchResultsPanel viewSearchResultsPanel = new ViewSearchResultsPanel(queryResults,m_mainSearchPanel);		                	
										if(null != m_mainSearchPanel.getCenterPanel().m_arrCards[3])
										{
											m_mainSearchPanel.getCenterPanel().remove(m_mainSearchPanel.getCenterPanel().m_arrCards[3]);
										}
										m_mainSearchPanel.getCenterPanel().m_arrCards[3] = viewSearchResultsPanel;
										m_mainSearchPanel.getCenterPanel().add(viewSearchResultsPanel, SearchCenterPanel.m_strViewSearchResultslbl);
										/*
										 * 	Implies the next button was clicked. Call show card with boolean set to true.
										 */
										showCard(true);								
            						}
            					}
							}
						}else
						{
							srhButton.setVisible(true);      
							gotoAddLimitPanel();
						}
            		}
            	};
            	swingWorker.start();          	
            }
            else if(m_mainSearchPanel.getCenterPanel().getSelectedCardIndex() == 3)
            {

            	  	gotoDataListPanel(null);
            	  	
            	  	srhButton.setVisible(false);            	  	
            	  	nextButton.setVisible(false);    
            	  	
            	  	saveDataListButton.setVisible(true);
            		addToExperimentButton.setVisible(true);         	  	

            	
            }
//            	else if(m_mainSearchPanel.getCenterPanel().getSelectedCardIndex() == 4)
//            {
//            	System.out.println("user clicked on next button in the data list tab");
//            	System.out.println("saving Data List ... ");
//            	DataListManager.saveDataList(MainSearchPanel.dataList);
//            	System.out.println("data list saved.");
//
//            }
            else
            {
            	srhButton.setVisible(true);
            	prvButton.setVisible(true);            	  	
        	  	nextButton.setVisible(true);    
        	  		
            	saveDataListButton.setVisible(false);
        		addToExperimentButton.setVisible(false);        
            	showCard(true);
            }
        }
		else if(strActionCommand.equals("Previous"))
		{
			/*
			 * Implies the previous button was clicked. Call show card with boolean set to false.
			 * 
			 */
			int cardIndex = m_mainSearchPanel.getCenterPanel().getSelectedCardIndex();
			if( cardIndex == 1)
			{
				nextButton.setVisible(true);
				prvButton.setVisible(false);
				srhButton.setVisible(false);
				saveDataListButton.setVisible(false);
        		addToExperimentButton.setVisible(false);    
			}
			else if (cardIndex == 2)
			{
				nextButton.setVisible(true);
				prvButton.setVisible(true);
				srhButton.setVisible(true);
				saveDataListButton.setVisible(false);
        		addToExperimentButton.setVisible(false);				  
			}else if (cardIndex == 3 || cardIndex == 4 )
			{
				nextButton.setVisible(true);
				prvButton.setVisible(true);
				srhButton.setVisible(false);
				saveDataListButton.setVisible(false);
        		addToExperimentButton.setVisible(false);				  
			}			
			showCard(false);			
		}
		else if(strActionCommand.equals("Reset"))
		{
			// -------- PERFORM FOLLOWING CLEAN-UP --------------------
			// 1. Reset the query object
			// 2. Clear result panels
			// 3. Clear breadcrumb on the panel
			m_mainSearchPanel.getCenterPanel().reset();
			m_mainSearchPanel.setQueryObject(null);
			int componentCount = m_mainSearchPanel.getCenterPanel().getComponentCount();
			if (componentCount > 2)
			{
				int cnt=componentCount-1;
				while(cnt>1)
				{
					m_mainSearchPanel.getCenterPanel().remove(cnt);
					m_mainSearchPanel.getCenterPanel().m_arrCards[cnt] =null;
					cnt--;
				}
			}
		}		
	}
	
	public void enableButtons()
	{
		/* The navigation buttons.*/	
		prvButton.setVisible(true);
		srhButton.setVisible(true);;
	}
	
	
	/*
	 * The method sets the focus appropriately for the top panel. Also, update
	 * the indexes. TODO: Please note that the setting of the focus, and the
	 * corresponding updated can be abstracted into the top panel class itself
	 */
	public void showCard(boolean blnNext)
	{
		/* Get the center Panel through the parent.*/
		SearchCenterPanel centerPanel  = this.m_mainSearchPanel.getCenterPanel();
		/* Get the layout associated with the center panel*/
		CardLayout layout = (CardLayout)centerPanel.getLayout();		
		/*Get the currently selected index.*/
		int iSelectedCard = this.m_mainSearchPanel.getCenterPanel().getSelectedCardIndex();
		/* Get the top Panel through the parent.*/
		SearchTopPanel topPanel  = this.m_mainSearchPanel.getTopPanel();
		 		
		if(blnNext)
		{
			/*
			 * validate and increment index to get the corresponding identifier, and use that
			 * to show the card. Also increment the index to indicate the currently selected card.
			 */
			if((iSelectedCard+1) < this.m_mainSearchPanel.getCenterPanel().getIdentifierCount())
			{
				Logger.out.info("inside if");
				layout.show(centerPanel,centerPanel.getIdentifier(iSelectedCard+1));
				centerPanel.setSelectedCardIndex((iSelectedCard+1));		
				topPanel.setFocus(iSelectedCard+1,true);
			}
		}
		else
		{
			/*
			 * validate and increment index to get the corresponding identifier, and use that
			 * to show the card. Also increment the index to indicate the currently selected card.
			 */		
			if(iSelectedCard > 0)
			{				
				layout.show(centerPanel,centerPanel.getIdentifier(iSelectedCard-1));
				centerPanel.setSelectedCardIndex((iSelectedCard-1));		
				topPanel.setFocus(iSelectedCard-1,false);
			}
		}
	}
	
	/**
	 *  If got error or zero result on query execution call this method 
	 * if we are calling this method don't call showCard Method	
	 */	
		
	public void gotoAddLimitPanel()
	{
		Logger.out.info("gotoAddLimitPanel");
		srhButton.setVisible(true);
		SearchCenterPanel centerPanel  = this.m_mainSearchPanel.getCenterPanel();
		CardLayout layout = (CardLayout)centerPanel.getLayout();	
		
		layout.show(centerPanel,centerPanel.getIdentifier(1));
		centerPanel.setSelectedCardIndex(1);
		SearchTopPanel topPanel  = this.m_mainSearchPanel.getTopPanel();
		topPanel.setFocus(1,false);
    }
	
	/**
	 * Method to switch from view search result panel to panel data list panel ... 
	 * @param datarow
	 */
	
	public void gotoDataListPanel(final IDataRow datarow)
	{
		DataListPanel dataListPanel = (DataListPanel) m_mainSearchPanel.getCenterPanel().m_arrCards[m_mainSearchPanel.getCenterPanel().getSelectedCardIndex()+1];
    	if(dataListPanel != null)
    	{
    		  m_mainSearchPanel.getCenterPanel().remove(dataListPanel);
    	}
    	if(datarow == null)
    	{
    		dataListPanel = new DataListPanel(MainSearchPanel.getDataList());
    	}
    	else
    	{
    		dataListPanel = new DataListPanel(MainSearchPanel.getDataList(), datarow);
    	}
    	
    	m_mainSearchPanel.getCenterPanel().m_arrCards[4] = dataListPanel;
        m_mainSearchPanel.getCenterPanel().add(dataListPanel, SearchCenterPanel.m_strDataListlbl );           		
        /*
		 * Implies the next button was clicked. Call show card with boolean set to true.
		 */
        srhButton.setVisible(false);            	  	
	  	nextButton.setVisible(false);
	  	
	  	prvButton.setVisible(true);
	  	saveDataListButton.setVisible(true);
		addToExperimentButton.setVisible(true);    
		showCard(true);    
	}
}
