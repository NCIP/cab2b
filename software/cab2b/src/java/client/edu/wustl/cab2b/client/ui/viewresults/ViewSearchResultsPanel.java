/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTabbedPane;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTabbedPaneUI;
import edu.wustl.cab2b.client.ui.controls.CustomBorder;
import edu.wustl.cab2b.client.ui.searchDataWizard.MainSearchPanel;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;

/**
 * This is the container panel for simple search result panel and advanced
 * search result panel, put in a tabbed pane.
 * 
 * @author chetan_bh
 */
public class ViewSearchResultsPanel extends Cab2bPanel {
	private static final long serialVersionUID = 1L;

	private JTabbedPane simpleAdvTabPane;

	private JXPanel searchResultBreadCrumbsPanel;

	private JXPanel advSearchResultsPanel;

	private IQueryResult queryResult;

	public ViewSearchResultsPanel(IQueryResult queryResult, MainSearchPanel panel) {
		this.queryResult = queryResult;
		initGUI(panel);
	}

	private void initGUI(MainSearchPanel panel) {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(panel.getWidth() - 100, panel.getHeight()));
		this.setMaximumSize(new Dimension(panel.getWidth() - 100, panel.getHeight()));

		simpleAdvTabPane = new JTabbedPane();
		simpleAdvTabPane.setUI(new Cab2bTabbedPaneUI());

		searchResultBreadCrumbsPanel = new SimpleSearchResultBreadCrumbPanel(queryResult, this);
		searchResultBreadCrumbsPanel.setBorder(new CustomBorder());
		simpleAdvTabPane.add(" Simple ", searchResultBreadCrumbsPanel);

		advSearchResultsPanel = new ViewSearchResultsAdvancedPanel(queryResult);

		simpleAdvTabPane.add(" Advanced ", advSearchResultsPanel);
		simpleAdvTabPane.setEnabledAt(1, false);

		this.add(BorderLayout.CENTER, simpleAdvTabPane);
	}

}
