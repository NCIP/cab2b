/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.IDataRow;

public class DataListPanel extends Cab2bPanel {

    /**
     * The split pane to separate the tree and the details panels. 
     */
    private JSplitPane splitPane;

    /**
     * The tree panel.
     */
    private TreePanel treePanel;

    /**
     * Details panel to show the details of the node clicked. 
     */
    private DataListDetailedPanelContainer detailsPanel;

    /**
     * The data list.
     */
    private DataList dataList = new DataList();

    /**
     * @return Returns the treePanel.
     */
    public TreePanel getTreePanel() {
        return treePanel;
    }

    /**
     * @param treePanel The treePanel to set.
     */
    public void setTreePanel(TreePanel treePanel) {
        this.treePanel = treePanel;
    }

    /**
     * @return Returns the detailsPanel.
     */
    public DataListDetailedPanelContainer getDetailsPanel() {
        return detailsPanel;
    }

    /**
     * @param detailsPanel The detailsPanel to set.
     */
    public void setDetailsPanel(DataListDetailedPanelContainer detailsPanel) {
        this.detailsPanel = detailsPanel;
    }

    /**
     * @return Returns the dataList.
     */
    public DataList getDataList() {
        return dataList;
    }

    /**
     * @param dataList The dataList to set.
     */
    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public DataListPanel() {
    }

    public DataListPanel(DataList dataList) {
        this.dataList = dataList;
        initGUI();
        treePanel.selectTreeRoot();
    }

    /**
     * Constructor, getting IDataRow as parameter which should be highlighted
     * on data list result tree. 
     * @param dataList
     * @param row
     */
    public DataListPanel(DataList dataList, final IDataRow row) {
        this.dataList = dataList;
        initGUI();
        treePanel.selectTreeRoot(row);
    }

    private void initGUI() {
        setLayout(new RiverLayout());

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(242);
        splitPane.setDividerSize(4);

        splitPane.setBorder(null);

        detailsPanel = new DataListDetailedPanelContainer();
        splitPane.setRightComponent(detailsPanel);

        treePanel = new TreePanel(dataList.getRootDataRow());
        treePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        splitPane.setLeftComponent(treePanel);
        treePanel.setMinimumSize(new Dimension(242, this.getPreferredSize().height));
        splitPane.setDividerLocation(242);

        this.add("br hfill vfill", splitPane);
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
    }

}
