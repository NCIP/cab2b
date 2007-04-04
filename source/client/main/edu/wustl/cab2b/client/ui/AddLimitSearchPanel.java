package edu.wustl.cab2b.client.ui;

import java.util.Set;

/**
 * The actual LHS panel from the Add limit tab,comprising the advanced/category search
 * panels as well as the subsequent results panel.
 * 
 * @author mahesh_iyer/chetan_bh
 * 
 */
public class AddLimitSearchPanel extends AbstractSearchPanel
{
	
	/**
	 * constructor
	 * 
	 * @param addLimitPanel
	 *            The reference to the parent content panel that is saved, so
	 *            that it can be made available to child components, which can
	 *            then cause the parent to refresh for appropriate events.
	 */
	
	AddLimitSearchPanel(ContentPanel addLimitPanel)
	{
		super(addLimitPanel);
	}
	
	
	/**
	 * The abstract method implementation from the base class that returns an instance of 
	 * {@link AddLimitAdvancedSearchPanel}
	 * 
	 */	
	
	public AddLimitAdvancedSearchPanel getAdvancedSearchPanel()
	{	
		return new AddLimitAdvancedSearchPanel();	
	}
	
	/**
	 * The abstract method implementation from the base class that adds a text
	 * field as is required by this specific implementation of the
	 * {@link AbstractSearchPanel}
	 * 
	 */	
	
	public void addTextField()
	{		
		this.add("p hfill",this.getTextField());	
	}
	
	
	/**
	 * The abstract method implementation from the base class returns an
	 * instance of {@link AddLimitSearchResultPanel}
	 * 
	 * 
	 * @param addLimitPanel
	 *            The reference to the parent content panel required by the
	 *            {@link AddLimitSearchResultPanel} to be refreshed for the
	 *            appropritate events it can generate.
	 * 
	 * @param searchResult
	 *            The collection of {@link Entities}
	 */
	public AbstractSearchResultPanel getSearchResultPanel(ContentPanel addLimitPanel, Set dummySrhResult)
	{
		return  new AddLimitSearchResultPanel(addLimitPanel,dummySrhResult);
	}
	
}
