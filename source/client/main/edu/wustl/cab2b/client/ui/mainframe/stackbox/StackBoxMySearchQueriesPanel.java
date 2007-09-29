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
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryOrderPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;

/**
 * 
 * This is a singleton class used to display saved Query links on left hand side of 
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
        
        private void  createQueryConditionDialog(ICab2bParameterizedQuery cab2bQuery)
        {
            
        }
        

        public void actionPerformed(ActionEvent actionEvent) {
            Cab2bHyperlink<UserObjectWrapper<ICab2bParameterizedQuery>> queryLink = (Cab2bHyperlink<UserObjectWrapper<ICab2bParameterizedQuery>>) actionEvent.getSource();
            UserObjectWrapper<ICab2bParameterizedQuery> userObjectWrapper = queryLink.getUserObject();
            ICab2bParameterizedQuery cab2bQuery = userObjectWrapper.getUserObject();
            ParameterizedQueryOrderPanel parameterizedQueryOrderPanel = new ParameterizedQueryOrderPanel(cab2bQuery);
            parameterizedQueryOrderPanel.showInDialog();
            
            //TODO - Update this part

            /*// Set Query object into ClientQueryBuilder
             IClientQueryBuilderInterface clientQueryBuilder = new ClientQueryBuilder();
             clientQueryBuilder.setQuery(cab2bQuery);

             // Initialize MainSearchPanel
             GlobalNavigationPanel globalNavigationPanel = mainFrame.getGlobalNavigationPanel();
             GlobalNavigationGlassPane globalNavigationGlassPane = globalNavigationPanel.getGlobalNavigationGlassPane();
             globalNavigationGlassPane.initializeMainSearchPanel();

             // Set ClientQueryBuilder object into MainSearchPanel and set it to the 4rd card i.e. View Search Result  
             MainSearchPanel mainSearchPanel = GlobalNavigationPanel.mainSearchPanel;
             mainSearchPanel.setQueryObject(clientQueryBuilder);
             mainSearchPanel.getCenterPanel().getAddLimitPanel().setQueryObject(clientQueryBuilder);
             mainSearchPanel.getCenterPanel().setSelectedCardIndex(2);

             // Fire the query by clicking (simulating) Next button
             SearchNavigationPanel bottomPanel = mainSearchPanel.getBottomPanel();
             Cab2bButton nextButton = bottomPanel.getNextButton();
             nextButton.doClick();

             // Open the Searcg dialog box
             globalNavigationGlassPane.showSearchDialog();*/
        }
    }

}
