package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTabbedPane;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.CustomBorder;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTabbedPaneUI;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;


/**
 * This is the container panel for simple search result panel and advanced search result 
 * panel, put in a tabbed pane.
 * 
 * @author chetan_bh
 */
public class ViewSearchResultsPanel extends Cab2bPanel
{
	
	JTabbedPane simpleAdvTabPane;
	
	JXPanel searchResultBreadCrumbsPanel;
	
	JXPanel advSearchResultsPanel;
	
	IQueryResult queryResult;
	
	
	public ViewSearchResultsPanel(IQueryResult queryResult, MainSearchPanel panel)
	{
		this.queryResult = queryResult;
		initGUI();
	}
	
	private void initGUI()
	{
		this.setLayout(new BorderLayout());
		simpleAdvTabPane = new JTabbedPane();
	
		simpleAdvTabPane.setUI(new Cab2bTabbedPaneUI());	
		searchResultBreadCrumbsPanel = new SimpleSearchResultBreadCrumbPanel(queryResult, this);
		
		searchResultBreadCrumbsPanel.setBorder(new CustomBorder());
		
		simpleAdvTabPane.add(" Simple ", searchResultBreadCrumbsPanel);
		advSearchResultsPanel = new ViewSearchResultsAdvancedPanel(queryResult);
		simpleAdvTabPane.add(" Advanced ", advSearchResultsPanel);
		
		this.add(BorderLayout.CENTER,simpleAdvTabPane);
		//this.add(simpleAdvTabPane);
	}
	
}
