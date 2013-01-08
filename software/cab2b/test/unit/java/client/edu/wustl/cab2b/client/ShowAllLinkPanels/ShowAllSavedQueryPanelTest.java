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

import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllSavedQueryPanel;
import junit.framework.TestCase;

/**
 * @author deepak_shingan
 *
 */
public class ShowAllSavedQueryPanelTest extends TestCase {

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllSavedQueryPanel#ShowAllSavedQueryPanel(java.lang.Object[], java.lang.Object[][])}.
     */
    public final void testShowAllSavedQueryPanel() {
        ShowAllSavedQueryPanel showAllSavedQueryPanel = new ShowAllSavedQueryPanel(null, null,null);
        assertNotNull(showAllSavedQueryPanel);

    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllSavedQueryPanel#queryLinkAction(java.lang.Long)}.
     */
    public final void testQueryLinkAction() {
        ShowAllSavedQueryPanel panelRef = new ShowAllSavedQueryPanel(null, null,null);
        try {
            panelRef.queryLinkAction(null);
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }

    /**
     * Test method for {@link edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel#getTable()}.
     */
    public final void testGetTable() {
        ShowAllSavedQueryPanel panelRef = new ShowAllSavedQueryPanel(null, null,null);
        assertNotNull(panelRef.getTable());
    }

}
