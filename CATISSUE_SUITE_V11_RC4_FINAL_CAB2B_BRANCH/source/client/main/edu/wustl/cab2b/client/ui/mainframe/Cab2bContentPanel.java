/*L
 * Copyright Georgetown University.
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
