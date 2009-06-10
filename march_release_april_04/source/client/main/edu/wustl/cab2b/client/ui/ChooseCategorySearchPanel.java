package edu.wustl.cab2b.client.ui;

import java.awt.Dimension;
import java.util.Set;

import org.jdesktop.swingx.JXPanel;


/**
 *
 * The actual RHS panel from the choose category tab from the main search
 * dialog,comprising the advanced/category search panels as well as the
 * subsequent results panel.
 * 
 * @author mahesh_iyer/chetan_bh
 * 
 */

public class ChooseCategorySearchPanel extends AbstractSearchPanel 
{

	/**
	 * constructor
	 * 
	 * @param chooseCategoryPanel
	 *            The reference to the parent content panel that is saved, so
	 *            that it can be made available to child components, which can
	 *            then cause the parent to refresh for appropriate events.
	 */
	ChooseCategorySearchPanel(ContentPanel chooseCategoryPanel)
	{
		super(chooseCategoryPanel);
	}
	
	/**
	 * The abstract method implementation from the base class that returns an instance of 
	 * {@link ChooseCategoryAdvancedSearchPanel}
	 * 
	 */	
	public AbstractAdvancedSearchPanel getAdvancedSearchPanel()
	{			
		return new ChooseCategoryAdvancedSearchPanel();	
	}
	
	

	/**
	 * The abstract method implementation from the base class that adds a text
	 * field as is required by this specific implementation of the
	 * {@link ChooseCategoryAdvancedSearchPanel}
	 * 
	 */	
	public void addTextField()
	{				
		this.getTextField().setPreferredSize(new Dimension(350,25));
		this.add(this.getTextField());
	}
	
	
	/**
	 * The abstract method implementation from the base class returns an
	 * instance of {@link ChooseCategorySearchResultPanel}
	 * 
	 * 
	 * @param addLimitPanel
	 *            The reference to the parent content panel required by the
	 *            {@link ChooseCategorySearchResultPanel} to be refreshed for the
	 *            appropritate events it can generate.
	 * 
	 * @param searchResult
	 *            The collection of {@link Entities}
	 */
	public AbstractSearchResultPanel getSearchResultPanel(ContentPanel addLimitPanel, Set searchResult)
	{
		return  new ChooseCategorySearchResultPanel(addLimitPanel,searchResult);
	}
	
	
}
