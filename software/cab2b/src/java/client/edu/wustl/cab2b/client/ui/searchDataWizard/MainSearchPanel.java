/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.searchDataWizard;

import java.awt.BorderLayout;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.domain.DataListMetadata;

/**
 * @author mahesh_iyer
 * 
 * This is the main dialog for building the B2B query. 
 */
public class MainSearchPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**  The top searchPanel consisting of the labels.*/
    private SearchTopPanel topLabelPanel = new SearchTopPanel();

    /** The center searchPanel.*/
    private SearchCenterPanel centerPanel = new SearchCenterPanel();

    /** The bottom navigation searchPanel.*/
    private SearchNavigationPanel bottomPanel = new SearchNavigationPanel(this);

    /** The query object generated when user submits the conditions in the add limit page.*/
    public IClientQueryBuilderInterface queryObject = null;

    /** Data list of the user.*/
    private static DataList dataList = new DataList();

    /** Reference to datalist metadata of the current datalist.  */
    public static DataListMetadata savedDataListMetadata = null;

    private boolean isParaQueryShowResultButtonPressed = false;

    /**
     * @return Returns the queryObject.
     */
    public IClientQueryBuilderInterface getQueryObject() {
        return queryObject;
    }

    public SearchNavigationPanel getSearchNavigationPanel() {
        return bottomPanel;
    }

    /**
     * @param queryObject The queryObject to set.
     */
    public void setQueryObject(IClientQueryBuilderInterface queryObject) {
        this.queryObject = queryObject;
    }

    /**
     * The getter method for the center searchPanel.
     * @return
     */
    public SearchCenterPanel getCenterPanel() {
        return this.centerPanel;
    }

    /**
     * The getter method for the top searchPanel.
     * @return
     */
    public SearchTopPanel getTopPanel() {
        return this.topLabelPanel;

    }

    /**
     * The getter method for the navigation searchPanel.
     * @return
     */
    public SearchNavigationPanel getNavigationPanel() {
        return this.bottomPanel;

    }

    /*
     * The dialog is made up of 3 panels;a top searchPanel consisting of the label
     * searchPanel, the main searchPanel as well as the navigation searchPanel. The center searchPanel
     * has a card layout which is controlled by the navigation searchPanel. The dialog
     * itself has a border layout.
     */
    public MainSearchPanel() {
        initGUI();
    }

    /**
     * The method initializes the tabbed pane.
     */
    private void initGUI() {
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, this.topLabelPanel);
        this.add(BorderLayout.CENTER, this.centerPanel);
        this.add(BorderLayout.SOUTH, this.bottomPanel);
    }

    public static DataList getDataList() {
        return dataList;
    }

    /**
     * @return the isParaQueryShowResultButtonPressed
     */
    public boolean isParaQueryShowResultButtonPressed() {
        return isParaQueryShowResultButtonPressed;
    }

    /**
     * @param isParaQueryShowResultButtonPressed the isParaQueryShowResultButtonPressed to set
     */
    public void setParaQueryShowResultButtonPressed(boolean isParaQueryShowResultButtonPressed) {
        this.isParaQueryShowResultButtonPressed = isParaQueryShowResultButtonPressed;
    }

}