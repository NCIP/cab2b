
package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.List;

import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.common.datalist.DataList;
import edu.wustl.cab2b.common.datalist.DataRow;
import edu.wustl.cab2b.common.datalist.IDataRow;

public class DataListPanel extends Cab2bPanel
{

    /**
     * The split pane to separate the tree and the details panels. 
     */
    private JSplitPane splitPane;
    
    /**
     * The parent panel.
     */
    private JXPanel parentPanel;
    
    /**
     * The tree panel.
     */
    private TreePanel treePanel;
    
    /**
     * Details panel to show the details of the node clicked. 
     */
    private DataListDetailsPanel detailsPanel = new DataListDetailsPanel();
    
    /**
     * The data list.
     */
    private DataList dataList = new DataList();
    
    /**
     * @return Returns the treePanel.
     */
    public TreePanel getTreePanel()
    {
        return treePanel;
    }
    
    /**
     * @param treePanel The treePanel to set.
     */
    public void setTreePanel(TreePanel treePanel)
    {
        this.treePanel = treePanel;
    }

    /**
     * @return Returns the detailsPanel.
     */
    public DataListDetailsPanel getDetailsPanel()
    {
        return detailsPanel;
    }

    /**
     * @param detailsPanel The detailsPanel to set.
     */
    public void setDetailsPanel(DataListDetailsPanel detailsPanel)
    {
        this.detailsPanel = detailsPanel;
    }

    /**
     * @return Returns the dataList.
     */
    public DataList getDataList()
    {
        return dataList;
    }
    
    /**
     * @param dataList The dataList to set.
     */
    public void setDataList(DataList dataList)
    {
        this.dataList = dataList;
    }
    
    public DataListPanel()
    {
    }
    
    public DataListPanel(DataList dataList)
    {
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
    public DataListPanel(DataList dataList, final IDataRow row)
    {
    	this.dataList = dataList;
        initGUI();
        treePanel.selectTreeRoot(row);
    }
    
    private void initGUI()
    {
        setLayout(new RiverLayout());
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        //splitPane.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
        splitPane.setDividerLocation(350);
        
        
        detailsPanel = new DataListDetailsPanel();
        splitPane.setRightComponent(detailsPanel);
        
        treePanel = new TreePanel(dataList.getDataList());
        splitPane.setLeftComponent(treePanel);
        treePanel.setMinimumSize(new Dimension(242,this.getPreferredSize().height));
        
        this.add("br hfill vfill", splitPane);
        this.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
    }
    
    public static void main(String[] args)
    {
        JPagination pagination = new JPagination();
        List selectedUserObjects = pagination.getSelectedPageElementsUserObjects();
        
        IDataRow dataRow2 = (IDataRow)selectedUserObjects.get(1);
        
        IDataRow dataRow3 = new DataRow();
        dataRow3.setId(new Long(3));
        dataRow3.setClassName("Biospecimen");
        dataRow3.setParent(dataRow2);
        selectedUserObjects.add(dataRow3);
        
        IDataRow dataRow4 = new DataRow();
        dataRow4.setId(new Long(4));
        dataRow4.setClassName("Biospecimen");
        dataRow4.setParent(dataRow3);
        selectedUserObjects.add(dataRow4);
        
        DataList dataList = new DataList();
        dataList.addDataRows(selectedUserObjects);
        
        //TODO - Temp remove this.
        DataListPanel dataListPanel = new DataListPanel(dataList);
        WindowUtilities.showInFrame(dataListPanel, "Data List");
    }
}
