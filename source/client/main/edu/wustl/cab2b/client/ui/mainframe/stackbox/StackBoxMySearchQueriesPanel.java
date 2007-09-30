/**
 * 
 */
package edu.wustl.cab2b.client.ui.mainframe.stackbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryPreviewPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;

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
        List<ICab2bParameterizedQuery> cab2bQueryList = null;
        try {
            cab2bQueryList = queryEngineBusinessInterface.retrieveAllQueries();
        } catch (Exception exception) {
            CommonUtils.handleException(exception, MainFrame.newWelcomePanel, true, true, true, false);
        }

        if (cab2bQueryList != null && !cab2bQueryList.isEmpty()) {

            for (ICab2bParameterizedQuery cab2bQuery : cab2bQueryList) {
                String queryName = cab2bQuery.getName();
                UserObjectWrapper<ICab2bParameterizedQuery> userObjectWrapper = new UserObjectWrapper<ICab2bParameterizedQuery>(
                        cab2bQuery, queryName);

                Cab2bHyperlink<UserObjectWrapper<ICab2bParameterizedQuery>> queryLink = new Cab2bHyperlink<UserObjectWrapper<ICab2bParameterizedQuery>>(
                        true);
                queryLink.setUserObject(userObjectWrapper);
                queryLink.setText(queryName);
                queryLink.addActionListener(new MySeachQueiresLinkListener());

                this.add("br ", queryLink);
            }
        }
        updateUI();
    }

    private class MySeachQueiresLinkListener implements ActionListener {

        public void actionPerformed(ActionEvent actionEvent) {
            Cab2bHyperlink<UserObjectWrapper<ICab2bParameterizedQuery>> queryLink = (Cab2bHyperlink<UserObjectWrapper<ICab2bParameterizedQuery>>) actionEvent.getSource();
            UserObjectWrapper<ICab2bParameterizedQuery> userObjectWrapper = queryLink.getUserObject();
            ICab2bParameterizedQuery cab2bQuery = userObjectWrapper.getUserObject();
            ParameterizedQueryPreviewPanel parameterizedQueryPreviewPanel = new ParameterizedQueryPreviewPanel(
                    cab2bQuery);
            parameterizedQueryPreviewPanel.showInDialog();
        }
    }

}
