package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.EXPERIMENT_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.POPULAR_CATEGORY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.QUERY_BOX_TEXT;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_EXPERIMENT_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.MY_SEARCH_QUERIES_IMAGE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.POPULAR_CATEGORIES_IMAGE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Chandrakant Talele
 */
public class MainFrameStackedBoxPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    private JPanel mySearchQueriesPanel;

    private JPanel popularSearchCategoryPanel;

    private JPanel myExperimentsPanel;

    private static Color CLICKED_COLOR = new Color(76, 41, 157);

    private static Color UNCLICKED_COLOR = new Color(0x034E74);

    private MainFrame mainFrame;

    /**
     * @param frame 
     * @param mainFrame
     */
    public MainFrameStackedBoxPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BorderLayout());
        
        StackedBox stackedBox = new StackedBox();
        stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
        JScrollPane scrollPane = new JScrollPane(stackedBox);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);

        mySearchQueriesPanel = getPanel();
        JScrollPane jScrollPane = new JScrollPane(mySearchQueriesPanel);
        jScrollPane.setPreferredSize(new Dimension(250, 150));
        jScrollPane.setBorder(null);
        jScrollPane.getViewport().setBackground(Color.WHITE);
        
        final String titleQuery = ApplicationProperties.getValue(QUERY_BOX_TEXT);
        stackedBox.addBox(titleQuery, jScrollPane, MY_SEARCH_QUERIES_IMAGE, false);

        popularSearchCategoryPanel = getPanel();
        final String titlePopularcategories = ApplicationProperties.getValue(POPULAR_CATEGORY_BOX_TEXT);
        stackedBox.addBox(titlePopularcategories, popularSearchCategoryPanel, POPULAR_CATEGORIES_IMAGE, false);

        myExperimentsPanel = getPanel();
        final String titleExpr = ApplicationProperties.getValue(EXPERIMENT_BOX_TEXT);
        stackedBox.addBox(titleExpr, myExperimentsPanel, MY_EXPERIMENT_IMAGE, false);

        stackedBox.setPreferredSize(new Dimension(250, 500));
        stackedBox.setMinimumSize(new Dimension(250, 500));

        this.setMinimumSize(new Dimension(242, this.getMinimumSize().height)); //fix for bug#3745
    }

    /**
     * @return
     */
    private JPanel getPanel() {
        JPanel panel = new Cab2bPanel();
        panel.setLayout(new RiverLayout(10, 5));
        //panel.setPreferredSize(new Dimension(250, 150));
        panel.setOpaque(false);
        return panel;
    }

    /**
     * @param data
     */
    public void setDataForMySearchQueriesPanel() {
        QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                    EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                    QueryEngineHome.class,
                                                                                                                                    MainFrameStackedBoxPanel.this);
        try {
            List<ICab2bParameterizedQuery> cab2bParameterizedQueryList = queryEngineBusinessInterface.retrieveAllQueries();
            if (cab2bParameterizedQueryList != null && !cab2bParameterizedQueryList.isEmpty()) {
                updateMySearchQueryPanel(cab2bParameterizedQueryList);
            }
        } catch (Exception exception) {
            CommonUtils.handleException(exception, MainFrame.newWelcomePanel, true, true, true, false);
        }
    }

    /**
     * @param data
     */
    public void setDataForPopularSearchCategoriesPanel(Vector data) {
        setDataForPanel(popularSearchCategoryPanel, data,
                        "This link will open selected popular category in add limit page.\nThis feature is not yet implemented.");
    }

    /**
     * @param data
     */
    public void setDataForMyExperimentsPanel(Vector data) {
        setDataForPanel(myExperimentsPanel, data,
                        "This link will open selected user experiment.\nThis feature is not yet implemented.");
    }

    /**
     * @param panel
     * @param data
     */
    private void setDataForPanel(JPanel panel, Vector data, final String msg) {
        panel.removeAll();
        panel.add(new Cab2bLabel());
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //TODO this will be removed in later releases.
                Container comp = (Container) ae.getSource();
                while (comp.getParent() != null) {
                    comp = comp.getParent();
                }
                JOptionPane.showMessageDialog(comp, msg, "caB2B Information", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            String hyperlinkName = obj.toString();
            Cab2bHyperlink hyperlink = new Cab2bHyperlink(true);
            hyperlink.setClickedColor(CLICKED_COLOR);
            hyperlink.setUnclickedColor(UNCLICKED_COLOR);
            hyperlink.setText(hyperlinkName);
            hyperlink.addActionListener(actionListener);
            panel.add("br", hyperlink);
        }
        panel.revalidate();
    }

    public void updateMySearchQueryPanel(List<ICab2bParameterizedQuery> cab2bParameterizedQueryList) {
        mySearchQueriesPanel.removeAll();
        for (ICab2bParameterizedQuery cab2bQuery : cab2bParameterizedQueryList) {
            String queryName = cab2bQuery.getName();
            UserObjectWrapper<ICab2bQuery> userObjectWrapper = new UserObjectWrapper<ICab2bQuery>(cab2bQuery,
                    queryName);

            Cab2bHyperlink<UserObjectWrapper<ICab2bQuery>> queryLink = new Cab2bHyperlink<UserObjectWrapper<ICab2bQuery>>(
                    true);
            queryLink.setUserObject(userObjectWrapper);
            queryLink.setClickedColor(CLICKED_COLOR);
            queryLink.setUnclickedColor(UNCLICKED_COLOR);
            queryLink.setText(queryName);
            queryLink.addActionListener(new MySeachQueiresLinkListener());

            mySearchQueriesPanel.add("br ", queryLink);
        }
        updateUI();
    }

    private class MySeachQueiresLinkListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            Cab2bHyperlink<UserObjectWrapper<ICab2bQuery>> queryLink = (Cab2bHyperlink<UserObjectWrapper<ICab2bQuery>>) actionEvent.getSource();
            UserObjectWrapper<ICab2bQuery> userObjectWrapper = queryLink.getUserObject();
            ICab2bQuery cab2bQuery = userObjectWrapper.getUserObject();

            // Set Query object into ClientQueryBuilder
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
            globalNavigationGlassPane.showSearchDialog();
        }
    }
}