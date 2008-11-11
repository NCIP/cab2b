/**
 * 
 */
package edu.wustl.cab2b.client.ui.mainframe.showall;

import javax.swing.table.TableColumnModel;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.MainSearchPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.addLimit.AddLimitPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

/**
 * Panel Class for Showing all categories from database  
 * @author deepak_shingan
 *
 */
public class ShowAllCategoryPanel extends ShowAllPanel {
    
    private static final long serialVersionUID = 1L;

    /** Category Name of Category*/
    public static final String CATEGORY_NAME_TITLE = "Category Name";

    /** Description of Category*/
    public static final String CATEGORY_DESCRIPTION_TITLE = "Description";

    /** Popularity Category*/
    public static final String CATEGORY_POPULARITY_TITLE = "Popularity";

    /** Date Last updated of Category*/
    public static final String CATEGORY_DATE_TITLE = "Date Last updated";

    /**
     * @param tableHeader
     * @param data
     */
    public ShowAllCategoryPanel(Object[] tableHeader, Object[][] data,String columnName) {
        super(tableHeader, data,columnName);
        //hiding last column from table which contains category object
        TableColumnModel colModel = getTable().getColumnModel();
        if (colModel.getColumnCount() > 4)
            getTable().getColumnModel().removeColumn(colModel.getColumn(colModel.getColumnCount() - 1));
    }

    /** Action Performed Event
     * @see edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel#linkActionPerformed()
     */
    @Override
    public void linkActionPerformed() {
        Cab2bTable table = getTable();

        //Getting the selected hyperlink row
        int selectionIndex = getTable().getSelectionModel().getLeadSelectionIndex();

        // Getting object associated with hyperlink 
        // column Number will be always last column
        EntityInterface category = (EntityInterface) table.getModel().getValueAt(
                                                                                 selectionIndex,
                                                                                 table.getModel().getColumnCount() - 1);
        categoryLinkAction(category);
        updateUI();
    }

    /**
     * Action method for category link  
     * @param category
     */
    public static final void categoryLinkAction(EntityInterface category) {
        MainSearchPanel mainSearchPanel = null;
        try {
            if (GlobalNavigationPanel.getMainSearchPanel() == null)
                mainSearchPanel = new MainSearchPanel();
            GlobalNavigationPanel.setMainSearchPanel(mainSearchPanel);
            mainSearchPanel.getSearchNavigationPanel().setAddLimitPanelInWizard();
            AddLimitPanel addLimitPanel = mainSearchPanel.getCenterPanel().getAddLimitPanel();
            addLimitPanel.getSearchResultPanel().updateAddLimitPage(addLimitPanel, category);
            CommonUtils.launchSearchDataWizard();
        } catch (Exception exception) {
            CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
        }
    }
}
