/**
 * 
 */
package edu.wustl.cab2b.client.ui.mainframe.showall;

import javax.swing.table.TableColumnModel;

import edu.wustl.cab2b.client.ui.AddLimitPanel;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * Panel Class for Showing all categories from database  
 * @author deepak_shingan
 *
 */
public class ShowAllCategoryPanel extends ShowAllPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String CATEGORY_NAME_TITLE = "Category Name";

    public static final String CATEGORY_DESCRIPTION_TITLE = "Description";

    public static final String CATEGORY_POPULARITY_TITLE = "Popularity";

    public static final String CATEGORY_DATE_TITLE = "Date Last updated";

    public ShowAllCategoryPanel(Object[] tableHeader, Object[][] data) {
        super(tableHeader, data);
        //hiding last column from table which contains category object
        TableColumnModel colModel = getTable().getColumnModel();
        if (colModel.getColumnCount() > 4)
            getTable().getColumnModel().removeColumn(colModel.getColumn(colModel.getColumnCount() - 1));
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel#linkActionPerformed()
     */
    @Override
    public void linkActionPerformed() {
        Cab2bTable table = getTable();

        //Getting the selected hyperlink row
        int selectionIndex = getTable().getSelectionModel().getLeadSelectionIndex();

        // Getting object associated with hyperlink 
        // column Number will be always last column
        Category category = (Category) table.getModel().getValueAt(selectionIndex,
                                                                   table.getModel().getColumnCount() - 1);
        categoryLinkAction(category);
        updateUI();
    }

    /**
     * Action method for category link  
     * @param hyperLink
     */
    public static final void categoryLinkAction(Category category) {
        MainSearchPanel mainSearchPanel = null;
        try {
            if (GlobalNavigationPanel.getMainSearchPanel() == null)
                mainSearchPanel = new MainSearchPanel();
            GlobalNavigationPanel.setMainSearchPanel(mainSearchPanel);
            mainSearchPanel.getSearchNavigationPanel().setAddLimitPanelInWizard();
            AddLimitPanel addLimitPanel = mainSearchPanel.getCenterPanel().getAddLimitPanel();
            addLimitPanel.getSearchResultPanel().updateAddLimitPage(addLimitPanel, category.getCategoryEntity());
            CommonUtils.launchSearchDataWizard();
        } catch (Exception exception) {
            CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
        }
    }
}
