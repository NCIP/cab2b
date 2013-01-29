/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchResultPanel;

public class Cab2bContentPanel extends ContentPanel {

	private SearchPanel searchPanel;

	/**
	 *Default Constructor 
	 */
	public Cab2bContentPanel() {
		super();
		searchPanel = new SearchPanel(this);
	}

	/**
	 * @param arrPanel
	 * @param strClassName
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#refresh(org.jdesktop.swingx.JXPanel[], java.lang.String)
	 */
	@Override
	public void refresh(JXPanel[] arrPanel, String strClassName) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param searchResultPanel
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#setSearchResultPanel(edu.wustl.cab2b.client.ui.searchDataWizard.SearchResultPanel)
	 */
	@Override
	public void setSearchResultPanel(SearchResultPanel searchResultPanel) {
		searchPanel.setSerachResultPanel(searchResultPanel);
	}

	/**
	 * @param panel
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#setSearchPanel(edu.wustl.cab2b.client.ui.searchDataWizard.SearchPanel)
	 */
	@Override
	public void setSearchPanel(SearchPanel panel) {
		searchPanel = panel;
	}

	/**
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#getSearchPanel()
	 */
	@Override
	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

}
