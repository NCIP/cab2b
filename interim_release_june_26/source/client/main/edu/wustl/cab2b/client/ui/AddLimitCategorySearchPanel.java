package edu.wustl.cab2b.client.ui;

/**
 * The main LHS panel from the Add limit tab, comprising the
 * {@link AddLimitSearchPanel}. Other components would be added in future.
 * 
 * @author mahesh_iyer
 * 
 */
public class AddLimitCategorySearchPanel extends AbstractCategorySearchPanel {

    /**
     * constructor
     * 
     * @param addLimitPanel
     *            The reference to the parent content panel to be propogated
     *            through the child heirarchy to cause the parent to be
     *            refreshed for the appropritate event.
     */
    public AddLimitCategorySearchPanel(ContentPanel addLimitPanel) {
        super(addLimitPanel);
    }

    /**
     * The abstract method implementation from the base class returns an
     * instance of {@link AddLimitSearchPanel} to be added to this panel.
     * Sub-classes might be required to over-ride this method.
     * 
     * @param addLimitPanel
     *            The reference to the parent content panel to be propogated
     *            through the child heirarchy to cause the parent to be
     *            refreshed for the appropritate event.
     */
    public AbstractSearchPanel getSearchPanel(ContentPanel addLimitPanel) {
        return new AddLimitSearchPanel(addLimitPanel);
    }

}
