package edu.wustl.cab2b.client.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.common.util.logger.Logger;


/**
 * The center panel from the main search dialog. The panel uses a card layout to
 * manage all the cards. Each card is in turn a panel corresponding to each tab
 * in th main search dialog.
 * 
 * @author mahesh_iyer
 * 
 */

public class SearchCenterPanel extends Cab2bPanel 
{
	/** An array of all the cards to be added to this panel.*/
	public JXPanel[] m_arrCards = new Cab2bPanel[5]; 
	
	/** Identifier to identify the card corresponding to the advanced search panel.*/
	public static final String m_strIdentifierChooseCateglbl = "Choose Search Category";
	
	/** Identifier to identify the card corresponding to the Add Limit panel.*/
	public static final String m_strIdentifierAddLimitlbl = "Add Limit";
	
	/** Identifier to identify the card corresponding to the Define Search Results panel.*/
	public static final String m_strDefineSearchResultslbl = "Define Search Results View";
	
	/** Identifier to identify the card corresponding to the View Search Results panel.*/
	public static final String m_strViewSearchResultslbl = "View Search Results";

	/** Identifier to identify the card corresponding to the Data List panel.*/
	public static final String m_strDataListlbl = "Data List";

	
	
	/** Index to indicate the currently selected index. Initialized to default value of 0.*/
	private int m_iCurrentlySelectedCard = 0;
	
	private AddLimitPanel addLimitPanel;
    
    private ChooseCategoryPanel chooseCategPanel; 
	
	/**
	 * HashMap of identifiers. This would be used by the navigation panel to
	 * bring up the appropriate card.
	 */
	private Vector m_vIdentifiers = new Vector(); 
	
	
	/**
	 * The method returns the number of cards currently selected
	 * 
	 * @return int The current card count.
	 * 
	 */		
	public int getIdentifierCount()
	{
		return this.m_vIdentifiers.size();
	}
	
	/**
	 * The method returns the index for the currently selected card.
	 * 
	 * @return int The index of the currently selected card.
	 * 
	 */		
	public int getSelectedCardIndex()
	{
		return this.m_iCurrentlySelectedCard;
	}
	
	/**
	 * The method returns the currently selected card.
	 * 
	 * @return JXPanel The currently selected card.
	 * 
	 */		
	
	public JXPanel getSelectedCard(){
		
		return this.m_arrCards[this.getSelectedCardIndex()];
	}
	
	/**
	 * The method sets the index of the currently selected card.
	 * 
	 * @param  iSelectedCard index of the card to be selected.
	 * 
	 */		
	
	public void setSelectedCardIndex(int iSelectedCard)
	{				
		this.m_iCurrentlySelectedCard = iSelectedCard;
	}
	
	/**
	 * Constructor.
	 */
	
	SearchCenterPanel()
	{
		initGUI();
		this.setBorder( new SearchDialogBorder());
	}
	
	/**
	 * The method initializes the tabbed pane.
	 */
	private void initGUI()
	{
		/*Set the card layout for the center panel.*/
		this.setLayout(new CardLayout());
		
		/* First card initialization.*/
		chooseCategPanel = new ChooseCategoryPanel();
		this.add(chooseCategPanel,this.m_strIdentifierChooseCateglbl);
		chooseCategPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
		
		/*
		 * Add the identifier to the vector, where from it would be refrenced by
		 * the navigation panel.
		 */
		this.m_vIdentifiers.add(this.m_strIdentifierChooseCateglbl);
		this.m_arrCards[0] = chooseCategPanel;

		/* Second card initialization.*/
		addLimitPanel = new AddLimitPanel();
		this.add(addLimitPanel,this.m_strIdentifierAddLimitlbl);
		this.m_vIdentifiers.add(this.m_strIdentifierAddLimitlbl);
		this.m_arrCards[1] = addLimitPanel;
		
		this.m_vIdentifiers.add(this.m_strDefineSearchResultslbl);
		this.m_vIdentifiers.add(this.m_strViewSearchResultslbl);
		this.m_vIdentifiers.add(this.m_strDataListlbl);
	}
	
	/**
	 * The method returns the identifier associated with the given index.
	 * 
	 * @param index
	 * @return
	 */
	public String getIdentifier(int index)
	{
		return (String)this.m_vIdentifiers.elementAt(index);
	}
	
	public void reset()
	{
		addLimitPanel.resetPanel();
	}
    
    
    /**
     * get the choose category panel instance 
     * @return the choose category panel instance
     */
    public ChooseCategoryPanel getChooseCategoryPanel()
    {
        return chooseCategPanel;
    }

    
    /**
     * get the add limit panel instance 
     * @return the add limit panel instance
     */
    public AddLimitPanel getAddLimitPanel()
    {
        return addLimitPanel;
    }
	
}
	
	
