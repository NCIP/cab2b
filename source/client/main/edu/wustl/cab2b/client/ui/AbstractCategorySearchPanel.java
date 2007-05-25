package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;


/**
 * The abstract class that encapsulates a {@link AbstractSearchPanel}.
 * Sub-classes must over-ride method that will determine the exact type of
 * {@link AbstractSearchPanel} to be added to this panel
 * 
 * @author mahesh_iyer
 * 
 */

public abstract class AbstractCategorySearchPanel extends Cab2bPanel
{
	
	/** The generic reference to a particular instance of the search panel. */
	private AbstractSearchPanel m_searchPanel = null;
	
	/**
	 * constructor
	 * 
	 * @param addLimitPanel
	 *            The reference to the parent content panel to be propogated
	 *            through the child heirarchy to cause the parent to be
	 *            refreshed for the appropritate event.
	 */
	public AbstractCategorySearchPanel(ContentPanel addLimitPanel)
	{
		initGUI(addLimitPanel);
	}

	/**
	 * Method initializes the panel by appropriately laying out child components.
	 * 
	 * @param addLimitPanel
	 *            The reference to the parent content panel to be propogated
	 *            through the child heirarchy to cause the parent to be
	 *            refreshed for the appropritate event.
	 */
	private void initGUI(ContentPanel addLimitPanel)
	{

		this.setLayout(new BorderLayout());
		/*
		 * Add the actual content panel to the center. This will allow the
		 * pagination component that is part of this, to resize depending on the
		 * space available
		 */
		this.m_searchPanel = getSearchPanel(addLimitPanel); 
		this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
		this.add((JXPanel)this.m_searchPanel, BorderLayout.CENTER);
	}
	
	/**
	 * The method returns the appropriate type of {@link AbstractSearchPanel} to
	 * be added to this panel. Sub-classes are required to over-ride this
	 * method.
	 * 
	 * @param addLimitPanel
	 *            The reference to the parent content panel to be propogated
	 *            through the child heirarchy to cause the parent to be
	 *            refreshed for the appropritate event.
	 */
	public abstract AbstractSearchPanel getSearchPanel(ContentPanel addLimitPanel);
	

}
