package edu.wustl.cab2b.client.ui.mainframe;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchResultPanel;

public class Cab2bContentPanel extends ContentPanel {

	private SearchPanel searchPanel;

	public Cab2bContentPanel() {
		super();
		searchPanel = new SearchPanel(this);
	}

	/**
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#refresh(org.jdesktop.swingx.JXPanel[], java.lang.String)
	 * @param arrPanel
	 * @param strClassName
	 */
	@Override
	public void refresh(JXPanel[] arrPanel, String strClassName) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#setSearchResultPanel(edu.wustl.cab2b.client.ui.searchDataWizard.SearchResultPanel)
	 * @param searchResultPanel
	 */
	@Override
	public void setSearchResultPanel(SearchResultPanel searchResultPanel) {
		searchPanel.setSerachResultPanel(searchResultPanel);
	}

	/**
	 * @see edu.wustl.cab2b.client.ui.searchDataWizard.ContentPanel#setSearchPanel(edu.wustl.cab2b.client.ui.searchDataWizard.SearchPanel)
	 * @param panel
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
