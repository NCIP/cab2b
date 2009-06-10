package edu.wustl.cab2b.client.ui;

import java.awt.Container;
import java.util.Set;

import org.jdesktop.swingx.JXPanel;

/**
 * The UI component that represents the search results panel from the
 * ChooseCategory section of the main search dialog.
 * 
 * @author mahesh_iyer/chetan_bh
 * 
 */

public class ChooseCategorySearchResultPanel extends AbstractSearchResultPanel {

    /**
     * Constructor
     * 
     * @param addLimitPanel
     *            Reference to the parent content panel that needs refreshing.
     * 
     * @param result
     *            The collectiond of entities.
     */

    public ChooseCategorySearchResultPanel(ContentPanel contentPanel, Set result) {
        super(contentPanel, result);

    }

    /**
     * The abstract method implementation from the base class that returns the
     * number of elements to be displayed/page on the {@link ChooseCategoryPanel}
     * page.
     * 
     * @return int Value represents the number of elements/page.
     */
    public int getPageSize() {
        return 3;
    }
    
    /** 
      Method to select appropriate panel and refresh 
	  the addLimit page 
	*/


    public static void selectPanel(final ContentPanel contentPanel, JXPanel[] componentPanel,  String strClassName)
    {
        Container container = ((JXPanel) (contentPanel)).getParent();
        
        if (container instanceof SearchCenterPanel) {
            container = (SearchCenterPanel) container;
            /*
             * Use the parent reference to in turn get a reference to the
             * navigation panel, and cause it to move to the next card.
             */
            MainSearchPanel mainSearchPanel = (MainSearchPanel) (container.getParent());
            (mainSearchPanel.getNavigationPanel()).enableButtons();

            /* Get the panel corresponding to the currently selcted card and refresh it.*/
            AddLimitPanel addLimitPanel = (AddLimitPanel) (mainSearchPanel.getCenterPanel().getAddLimitPanel());

            //set search-result panel in AddLimit panel and move to next tab 
            if (mainSearchPanel.getCenterPanel().getSelectedCardIndex() == 0) {
                AbstractSearchResultPanel searchResultPanel = mainSearchPanel.getCenterPanel().getChooseCategoryPanel().getSearchResultPanel();
                if (searchResultPanel != null) {
                    addLimitPanel.addResultsPanel(searchResultPanel);
                    mainSearchPanel.getCenterPanel().setAddLimitPanel(addLimitPanel);
                }
                (mainSearchPanel.getNavigationPanel()).showCard(true);
            }              
            addLimitPanel.refresh(componentPanel, strClassName);        
        }        
    }
    
    

    /**
     * The abstract method implementation from the base class that needs to
     * handle any refresh related activites for the {@link ChooseCategoryPanel}
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
        selectPanel(this.contentPanel, componentPanel, strClassName);
    }

}
