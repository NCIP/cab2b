package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.common.util.logger.Logger;

/**
 * @author mahesh_iyer
 * 
 * This is the main dialog for building the B2B query. 
 */
public class MainSearchPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**  The top searchPanel consisting of the labels.*/
    private SearchTopPanel m_TopLabelPanel = new SearchTopPanel();

    /** The center searchPanel.*/
    private SearchCenterPanel m_CenterPanel = new SearchCenterPanel();

    /** The bottom navigation searchPanel.*/
    private SearchNavigationPanel m_BottomPanel = new SearchNavigationPanel(this);

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

    public SearchNavigationPanel getBottomPanel() {
        return m_BottomPanel;
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
        return this.m_CenterPanel;
    }

    /**
     * The getter method for the top searchPanel.
     * @return
     */
    public SearchTopPanel getTopPanel() {
        return this.m_TopLabelPanel;

    }

    /**
     * The getter method for the navigation searchPanel.
     * @return
     */
    public SearchNavigationPanel getNavigationPanel() {
        return this.m_BottomPanel;

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
        this.add(BorderLayout.NORTH, this.m_TopLabelPanel);
        this.add(BorderLayout.CENTER, this.m_CenterPanel);
        this.add(BorderLayout.SOUTH, this.m_BottomPanel);
    }

    public static DataList getDataList() {
        return dataList;
    }

    public static void main(String[] args) {
        Logger.configure();

        MainSearchPanel searchPanel = new MainSearchPanel();

        JFrame dialog = new JFrame();
        dialog.setSize(976, 580);
        dialog.setTitle("Search Data");
        dialog.getContentPane().add(searchPanel);
        dialog.setVisible(true);
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