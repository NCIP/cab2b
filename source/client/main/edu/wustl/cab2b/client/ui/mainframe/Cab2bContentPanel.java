package edu.wustl.cab2b.client.ui.mainframe;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.ContentPanel;
import edu.wustl.cab2b.client.ui.SearchPanel;
import edu.wustl.cab2b.client.ui.SearchResultPanel;

public class Cab2bContentPanel extends ContentPanel {

	private SearchPanel searchPanel;

	public Cab2bContentPanel() {
		super();
		searchPanel = new SearchPanel(this);
	}

	@Override
	public void refresh(JXPanel[] arrPanel, String strClassName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSearchResultPanel(SearchResultPanel searchResultPanel) {
		searchPanel.setSerachResultPanel(searchResultPanel);
	}

	@Override
	public void setSearchPanel(SearchPanel panel) {
		searchPanel = panel;
	}

	@Override
	public SearchPanel getSearchPanel() {
		return searchPanel;
	}

}
