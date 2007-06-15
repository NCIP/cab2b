package edu.wustl.cab2b.client.ui;

import java.util.Set;

import org.jdesktop.swingx.JXPanel;

/**
 * The UI component that represents the search results panel from the AddLimit
 * section of the main search dialog.
 * 
 * 
 * @author mahesh_iyer/chetan_bh
 * 
 */

public class AddLimitSearchResultPanel extends AbstractSearchResultPanel {

    /**
     * Constructor
     * 
     * @param addLimitPanel
     *            Reference to the parent content panel that needs refreshing.
     * 
     * @param result
     *            The collectiond of entities.
     */
    AddLimitSearchResultPanel(ContentPanel addLimitPanel, Set results) {
        super(addLimitPanel, results);
    }

    /**
     * The abstract method implementation from the base class that needs to
     * handle any refresh related activites for the {@link AddLimitPanel}
     * 
     * @param arrComponentPanel
     *            This is the array of panels that forms the dynamically
     *            generated criterion pages. Each panel corresponds to one
     *            attribute from the class/category
     * 
     * @param strClassName
     *            The class/category name.
     */
    public void performAction(JXPanel[] componentPanel, String strClassName) {
        ChooseCategorySearchResultPanel.selectPanel(contentPanel, componentPanel, strClassName);
    }

    /**
     * The abstract method implementation from the base class that returns the
     * number of elements to be displayed/page on the {@link AddLimitPanel}
     * page.
     * 
     * @return int Value represents the number of elements/page.
     */

    public int getPageSize() {
        return 2;
    }
}
