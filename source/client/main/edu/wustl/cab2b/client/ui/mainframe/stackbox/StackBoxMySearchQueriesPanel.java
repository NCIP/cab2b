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
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryShowResultPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * 
 * The singleton class used to display saved Query links on left hand side of 
 * mainFrame panel   
 * @author deepak_shingan
 *
 */
public class StackBoxMySearchQueriesPanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static StackBoxMySearchQueriesPanel instance = null;

    private StackBoxMySearchQueriesPanel() {
        updateMySearchQueryPanel();
    }

    public static StackBoxMySearchQueriesPanel getInstance() {
        if (instance == null) {
            instance = new StackBoxMySearchQueriesPanel();
        }
        return instance;
    }

    /**
     * Method to update search query panel 
     */
    public void updateMySearchQueryPanel() {
        this.removeAll();
        this.setLayout(new RiverLayout(10, 5));
        QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                    EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                    QueryEngineHome.class,
                                                                                                                                    null);
        Collection<IParameterizedQuery> cab2bQueryList = null;
        try {
            cab2bQueryList = queryEngineBusinessInterface.getAllQueryNameAndDescription();
            for (IParameterizedQuery query : cab2bQueryList) {
                String queryName = query.getName();
                Cab2bHyperlink<Long> queryLink = new Cab2bHyperlink<Long>(true);
                queryLink.setUserObject(query.getId());
                queryLink.setText(queryName);
                if (query.getDescription() == null || query.getDescription().equals(""))
                    queryLink.setToolTipText("* Description not available");
                else
                    queryLink.setToolTipText(query.getDescription());
                queryLink.addActionListener(new MySeachQueiresLinkListener());
                this.add("br ", queryLink);
            }
        } catch (Exception exception) {
            CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
        }
        updateUI();
    }

    private class MySeachQueiresLinkListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Cab2bHyperlink queryLink = (Cab2bHyperlink) actionEvent.getSource();
            Long queryID = (Long) queryLink.getUserObject();

            QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                        EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                        QueryEngineHome.class,
                                                                                                                                        null);
            ICab2bParameterizedQuery cab2bQuery = null;
            try {
                cab2bQuery = queryEngineBusinessInterface.retrieveQueryById(queryID);
            } catch (RemoteException exception) {
                CommonUtils.handleException(exception, NewWelcomePanel.getMainFrame(), true, true, true, false);
                return;
            }
            ParameterizedQueryShowResultPanel parameterizedQueryPreviewPanel = new ParameterizedQueryShowResultPanel(
                    cab2bQuery);
            parameterizedQueryPreviewPanel.showInDialog();
        }
    }
}
