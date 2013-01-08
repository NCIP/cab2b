/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe.stackbox;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.HYPERLINK_SHOW_ALL;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllSavedQueryPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.authentication.Authenticator;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * The class used to display saved Query links on left hand side of
 * mainFrame panel
 *
 * @author deepak_shingan
 * @author lalit_chand
 *
 *
 */
public class SavedQueryLinkPanel extends Cab2bPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
    * Query comparator class used for descending ordering of queries based on Query.ID
    * @author deepak_shingan
    *
    */
    class QueryComparator implements Comparator<ICab2bQuery> {

        public int compare(ICab2bQuery arg0, ICab2bQuery arg1) {
            int result = 0;
            long value = arg0.getId() - arg1.getId();
            if (value > 0) {
                result = -1;
            } else if (value < 0) {
                result = 1;
            }
            return result;
        }

    }

    /**
     * Method to update search query panel
     */
    public void updateQueryLinkPanel() {
        this.removeAll();
        this.setLayout(new RiverLayout(5, 5));
        try {
            QueryEngineBusinessInterface queryEngine = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                       EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                       QueryEngineHome.class);
            Collection<ICab2bQuery> cab2bQueries = queryEngine.getUsersQueriesDetail(
                                                                                     Authenticator.getSerializedDCR());
            ArrayList<ICab2bQuery> cab2bQueryList = new ArrayList<ICab2bQuery>(cab2bQueries);
            Collections.sort(cab2bQueryList, new QueryComparator());

            int queryCounter = 0;
            for (ICab2bQuery query : cab2bQueryList) {
                String queryName = query.getName();
                Cab2bHyperlink<Long> queryLink = new Cab2bHyperlink<Long>(true);
                queryLink.setUserObject(query.getId());
                queryLink.setText(queryName);
                if (query.getDescription() == null || query.getDescription().equals("")) {
                    queryLink.setToolTipText("* Description not available");
                } else {
                    queryLink.setToolTipText(query.getDescription());
                }
                queryLink.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        Cab2bHyperlink queryLink = (Cab2bHyperlink) actionEvent.getSource();
                        Long queryID = (Long) queryLink.getUserObject();
                        ShowAllSavedQueryPanel.queryLinkAction(queryID);
                    }
                });
                this.add("br ", queryLink);
                if (++queryCounter > 4) {
                    break;
                }
            }

            if (cab2bQueryList.size() > 5) {
                Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
                CommonUtils.setHyperlinkProperties(hyperlink, null,
                                                   ApplicationProperties.getValue(HYPERLINK_SHOW_ALL), "",
                                                   new ActionListener() {
                                                       public void actionPerformed(ActionEvent e) {
                                                           NewWelcomePanel.getMainFrame().getGlobalNavigationPanel().getGlobalNavigationGlassPane().setShowAllPanel(
                                                                                                                                                                    getAllQueryPanel());
                                                       }
                                                   });
                this.add("right br", hyperlink);
            } else if (cab2bQueryList.size() == 0) {
                Cab2bLabel label = new Cab2bLabel("No saved queries.");
                label.setBackground(Color.blue);
                this.add(label);
            }
        } catch (Exception exception) {
            CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
        }
        updateUI();
    }

    /**
     * Method returns panel containing all queries from database
     *
     * @return
     */
    private ShowAllPanel getAllQueryPanel() {
        final Collection<ICab2bQuery> cab2bQueryCollection = CommonUtils.getUserSearchQueries();
        final Object objData[][] = new Object[cab2bQueryCollection.size()][5];
        final String headers[] = { ShowAllSavedQueryPanel.QUERY_NAME_TITLE, ShowAllSavedQueryPanel.QUERY_DATE_TITLE, ShowAllSavedQueryPanel.QUERY_DESCRIPTION_TITLE, "Query ID-Hidden" };
        int i = 0;
        for (ICab2bQuery query : cab2bQueryCollection) {
            objData[i][0] = query.getName();
            objData[i][1] = Utility.getFormattedDate(query.getCreatedDate());
            objData[i][2] = query.getDescription();
            objData[i][3] = query.getId();
            i++;
        }
        return new ShowAllSavedQueryPanel(headers, objData, ShowAllSavedQueryPanel.QUERY_NAME_TITLE);
    }
}
