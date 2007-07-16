package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.dag.MainDagPanel;
import edu.wustl.cab2b.client.ui.main.B2BStackedBox;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryobject.IExpressionId;

/**
 * This is the searchPanel for the choose category tab from the main search dialog. The
 * class is also an instance of the {@link ContentPanel},so that child
 * component can cause this searchPanel to refresh in a way required by this searchPanel.
 * 
 * @author mahesh_iyer
 */

public class ChooseCategoryPanel extends ContentPanel {
    private static final long serialVersionUID = 1L;

    public SearchPanel searchPanel = null;

    private B2BStackedBox box;

    private JSplitPane pane;

    /** The advanced search searchPanel along with the results searchPanel. */

    /**
     * Constructor
     */
    ChooseCategoryPanel() {
        initGUI();
    }

    /**
     * Method initializes the searchPanel by appropriately laying out child components.
     */
    public void initGUI() {
        /* The searchPanel consists of 2 parts
         * 1. StackedCollapsiblePanels.
         * 2. Panel for the Advanced search*/

        this.setLayout(new BorderLayout());
        box = new B2BStackedBox();
        /*
         * Setting prefered size is required if there is an empty searchPanel for
         * WEST or for CENTER, else the other component takes the whole place
         */
        box.setMinimumSize(new Dimension(263, 122));
        box.setBorder(null);
        addSearchPanel(searchPanel);
    }

    /**
     * The method is a custom implementation from {@link ContentPanel} interface. 
     * @param panelToBeRefreshed The searchPanel to be refreshed.
     */
    public void refresh(JXPanel[] arrPanel, String strClassName) {
        //TODO implementation needs to be completed
    }    
  
    public void setSearchResultPanel(SearchResultPanel searchResultPanel) {
        searchPanel.setSerachResultPanel(searchResultPanel);
    }

/*    
     * Given the flow of events, the method will be called when the
     * dynamically generated UI is loaded into an instance of the
     * AddLimitPanel. Thus we need to be able to get a reference to that
     * instance and be able to set the query instance in the main DAG Panel.
     

    public void setQueryObject(IClientQueryBuilderInterface query) {
         For this instance get a handle to the SerchCenterPanel.
        SearchCenterPanel searchCenterPanel = (SearchCenterPanel) this.getParent();
        AddLimitPanel panel = (AddLimitPanel) searchCenterPanel.getComponent(1);
        panel.setQueryObject(query);
    }*/

    /* 
     * Gets Search panel
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.ContentPanel#getSearchPanel()
     */
    @Override
    public SearchPanel getSearchPanel() {
        if (searchPanel == null)
            searchPanel = new SearchPanel(this);
        return searchPanel;
    }

    /**
     * Returns Search panel 
     * @return SearchResultPanel
     */
    public SearchResultPanel getSearchResultPanel() {
        return searchPanel.getSerachResultPanel();
    }

    /**
     * Adds search panel in choose category panel
     * @param panel
     */
    public void addSearchPanel(SearchPanel panel) {
        if (panel == null) {
            panel = new SearchPanel(this);
        }
        if (pane != null) {
            this.remove(pane);
        }
        setSearchPanel(panel);
        searchPanel = panel;
        searchPanel.setUIForChooseCategorySearchPanel();
        pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, box, searchPanel);
        pane.setDividerSize(4);
        pane.setDividerLocation(242);
        pane.setOneTouchExpandable(false);
        pane.setBorder(null);
        this.setBorder(null);
        this.add(BorderLayout.CENTER, pane);
    }

    /* 
     * Sets SearchPanel
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.ContentPanel#setSearchPanel(edu.wustl.cab2b.client.ui.SearchPanel)
     */
    public void setSearchPanel(SearchPanel panel) {
        this.searchPanel = panel;
    }

    /**
     * Adds result panel
     * @param resultPanel
     */
    public void addResultPanel(SearchResultPanel resultPanel) {
        searchPanel.addResultsPanel(resultPanel);
    }

    /**
     * Set's search text
     * @param searchText
     */
    public void setSearchText(String searchText) {
        searchPanel.setSearchtext(searchText);
    }

    /**
     * get search text
     * @return String
     */
    public String getSearchText() {
        return searchPanel.getSearchtext();
    }

}
