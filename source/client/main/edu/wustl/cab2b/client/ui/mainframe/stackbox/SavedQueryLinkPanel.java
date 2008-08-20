/**
 * 
 */
package edu.wustl.cab2b.client.ui.mainframe.stackbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collection;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllSavedQueryPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * The singleton class used to display saved Query links on left hand side of
 * mainFrame panel
 * 
 * @author deepak_shingan
 */
public class SavedQueryLinkPanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Only single private static instance of SavedQueryLinkPanel class  
     */
    private static SavedQueryLinkPanel instance = new SavedQueryLinkPanel();

    /**
     * Constructor
     */
    private SavedQueryLinkPanel() {
        updateQueryLinkPanel();
    }

    /**
     * Returns singleton instance of SavedQueryLinkPanel
     * @return SavedQueryLinkPanel
     */
    public static SavedQueryLinkPanel getInstance() {
        return instance;
    }

    /**
     * Method to update search query panel
     */
    public void updateQueryLinkPanel() {
        this.removeAll();
        this.setLayout(new RiverLayout(10, 5));
        QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                    EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                    QueryEngineHome.class,
                                                                                                                                    null);
        Collection<IParameterizedQuery> cab2bQueryList = null;
        try {
            cab2bQueryList = queryEngineBusinessInterface.getAllQueryNameAndDescription();
            int queryCounter = 0;
            for (IParameterizedQuery query : cab2bQueryList) {
                String queryName = query.getName();
                Cab2bHyperlink<Long> queryLink = new Cab2bHyperlink<Long>(true);
                queryLink.setUserObject(query.getId());
                queryLink.setText(queryName);
                if (query.getDescription() == null || query.getDescription().equals(""))
                    queryLink.setToolTipText("* Description not available");
                else
                    queryLink.setToolTipText(query.getDescription());
                queryLink.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        Cab2bHyperlink queryLink = (Cab2bHyperlink) actionEvent.getSource();
                        Long queryID = (Long) queryLink.getUserObject();
                        ShowAllSavedQueryPanel.queryLinkAction(queryID);
                    }
                });
                this.add("br ", queryLink);
                queryCounter++;
                if (queryCounter > 4)
                    break;
            }
            Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
            CommonUtils.setHyperlinkProperties(hyperlink, null, MainFrameStackedBoxPanel.SHOW_ALL_LINK, "",
                                               new ActionListener() {
                                                   public void actionPerformed(ActionEvent e) {
                                                       NewWelcomePanel.getMainFrame().getGlobalNavigationPanel().getGlobalNavigationGlassPane().setShowAllPanel(
                                                                                                                                                                getAllQueryPanel());

                                                   }

                                               });
            this.add("right br", hyperlink);

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
        QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                    EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                    QueryEngineHome.class,
                                                                                                                                    null);
        Collection<IParameterizedQuery> cab2bQueryCollection = null;
        try {
            cab2bQueryCollection = queryEngineBusinessInterface.getAllQueryNameAndDescription();
        } catch (RemoteException exception) {
            CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
        }
        final Object objData[][] = new Object[cab2bQueryCollection.size()][5];
        final String headers[] = { ShowAllSavedQueryPanel.QUERY_NAME_TITLE, ShowAllSavedQueryPanel.QUERY_DESCRIPTION_TITLE, ShowAllSavedQueryPanel.QUERY_DATE_TITLE, "Query ID-Hidden" };
        int i = 0;
        for (IParameterizedQuery paraQuery : cab2bQueryCollection) {
            objData[i][0] = paraQuery.getName();
            objData[i][1] = paraQuery.getDescription();
            objData[i][2] = "Date";
            objData[i][3] = paraQuery.getId();
            i++;
        }
        return new ShowAllSavedQueryPanel(headers, objData);
    }
}
