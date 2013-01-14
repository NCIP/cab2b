/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ShowAllLinkPanels;

import junit.framework.TestCase;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllCategoryPanel;

/**
 * Testclass for ShowAllCategoryPanel class
 * @author deepak_shingan
 *
 */
public class TestShowAllCategoryPanel extends TestCase {

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllCategoryPanel#ShowAllCategoryPanel(java.lang.Object[], java.lang.Object[][])}.
     */
    public final void testShowAllCategoryPanel() {
        ShowAllCategoryPanel panelRef = new ShowAllCategoryPanel(null, null);
        assertNotNull(panelRef);
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllCategoryPanel#categoryLinkAction(edu.wustl.common.querysuite.metadata.category.Category)}.
     */
    public final void testCategoryLinkAction() {
        ShowAllCategoryPanel panelRef = new ShowAllCategoryPanel(null, null);
        try {
            panelRef.categoryLinkAction(null);
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }
}
