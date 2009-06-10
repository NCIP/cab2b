package edu.wustl.cab2b.client.ui;

/**
 * The main RHS panel from the choose category tab, comprising the
 * {@link ChooseCategorySearchPanel}. Other components would be added in
 * future.
 * 
 * @author mahesh_iyer
 * 
 */

public class ChooseCategoryCategorySearchPanel extends AbstractCategorySearchPanel {
    /**
     * constructor
     * 
     * @param Panel
     *            The reference to the parent content panel to be propogated
     *            through the child heirarchy to cause the parent to be
     *            refreshed for the appropritate event.
     */

    public ChooseCategoryCategorySearchPanel(ContentPanel panel) {
        super(panel);
    }

    /**
     * The abstract method implementation from the base class returns an
     * instance of {@link ChooseCategorySearchPanel} to be added to this panel.
     * Sub-classes might be required to over-ride this method.
     * 
     * @param addLimitPanel
     *            The reference to the parent content panel to be propogated
     *            through the child heirarchy to cause the parent to be
     *            refreshed for the appropritate event.
     */
    public AbstractSearchPanel getSearchPanel(ContentPanel chooseCategoryPanel) {
        return new ChooseCategorySearchPanel(chooseCategoryPanel);
    }

}
