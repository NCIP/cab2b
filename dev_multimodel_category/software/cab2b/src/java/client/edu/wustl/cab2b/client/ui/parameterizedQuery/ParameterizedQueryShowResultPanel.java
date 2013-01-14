/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationGlassPane;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.searchDataWizard.MainSearchPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.utils.QueryUtility;

/**
 * Panel generated for showing parameterized/non-paramerterized conditions when
 * clicked on saved query link
 * 
 * @author deepak_shingan
 * 
 */
public class ParameterizedQueryShowResultPanel extends ParameterizedQueryPreviewPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Query data model
     */
    private ParameterizedQueryDataModel queryDataModel;

    /**
     * Show result button
     */
    private Cab2bButton showResultButton;

    /**
     * Cancel button
     */
    private Cab2bButton cancelButton;

    /**
     * Constructor
     * 
     * @param query
     */
    public ParameterizedQueryShowResultPanel(ICab2bQuery query) {
        queryDataModel = new ParameterizedQueryDataModel(query);
        initGUI();
    }

    /**
     * returns ParameterizedQueryDataModel
     * 
     * @return
     */
    public ParameterizedQueryDataModel getQueryDataModel() {
        return queryDataModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryPreviewPanel#getNavigationPanel()
     */
    @Override
    protected Cab2bPanel getNavigationPanel() {
        if (navigationPanel == null) {
            navigationPanel = new Cab2bPanel();
        }

        navigationPanel.removeAll();
        navigationPanel.setLayout(new RiverLayout(5, 10));
        navigationPanel.setBackground(new Color(240, 240, 240));
        navigationPanel.add("right ", showResultButton);
        navigationPanel.add("right ", cancelButton);
        navigationPanel.add("br ", new Cab2bLabel());
        return navigationPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryPreviewPanel#initGUI()
     */
    protected void initGUI() {
        this.setLayout(new BorderLayout());
        bottomConditionPanel = new Cab2bPanel();
        topConditionPanel = new Cab2bPanel();

        showResultButton = new Cab2bButton("Show Results");
        showResultButton.setPreferredSize(new Dimension(125, 22));
        showResultButton.addActionListener(new ShowResultsActionListener());
        cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dialog.dispose();
            }
        });
        createShowResultPreviewPanel(queryDataModel.getConditions(), queryDataModel.getQuery());
        initTopConditionPanel();
        initBottomConditionPanel();
        this.add(getNavigationPanel(), BorderLayout.SOUTH);
    }

    /**
     * Method to create show result preview panel
     * 
     * @param conditionMap
     */
    private void createShowResultPreviewPanel(Map<Integer, Collection<ICondition>> conditionMap,
                                              ICab2bQuery cab2bParamQuery) {
        // The following code is executing for StackPanel QueryLink Click
        ParseXMLFile parseFile = null;
        try {
            parseFile = ParseXMLFile.getInstance();
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }

        Collection<ICondition> conditions = QueryUtility.getAllNonParameteriedConditions(cab2bParamQuery);
        Collection<ICondition> paramConditions = QueryUtility.getAllParameterizedConditions(cab2bParamQuery);
        getMaxLabelDimension(conditions, paramConditions);

        addNonParameterizedConditions(conditionMap, conditions, cab2bParamQuery, parseFile);
        addParameterizedConditions(conditionMap, paramConditions, cab2bParamQuery, parseFile);
    }

    /**
     * Method to add non parameterized conditions in show result panel
     * 
     * @param conditionMap
     * @param conditions
     * @param cab2bParamQuery
     * @param parseFile
     */
    private void addNonParameterizedConditions(Map<Integer, Collection<ICondition>> conditionMap,
                                               Collection<ICondition> conditions, ICab2bQuery cab2bParamQuery,
                                               ParseXMLFile parseFile) {
        try {
            for (ICondition condition : conditions) {
                AbstractTypePanel componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                                      parseFile,
                                                                                                      condition.getAttribute(),
                                                                                                      maxLabelDimension);
                componentPanel.createPanelWithOperator(condition);
                componentPanel.setExpressionId(getExpressionIdForCondition(condition, conditionMap));
                CommonUtils.disableAllComponent(componentPanel);
                bottomConditionPanel.add("br ", componentPanel);
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
    }

    /**
     * Method to add parameterized conditions in show result panel
     * 
     * @param conditionMap
     * @param paramConditions
     * @param cab2bParamQuery
     * @param parseFile
     */
    private void addParameterizedConditions(Map<Integer, Collection<ICondition>> conditionMap,
                                            Collection<ICondition> paramConditions, ICab2bQuery cab2bParamQuery,
                                            ParseXMLFile parseFile) {
        try {
            List<IParameter<?>> paramterList = queryDataModel.getQuery().getParameters();
            for (IParameter parameter : paramterList) {
                ICondition condition = (ICondition) parameter.getParameterizedObject();

                AbstractTypePanel componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                                      parseFile,
                                                                                                      condition.getAttribute(),
                                                                                                      maxLabelDimension);
                componentPanel.createPanelWithOperator(condition);
                //Changes made by Deepak 
                //For fixing bug 11056
                componentPanel.setAttributeDisplayName(parameter.getName());
                componentPanel.setExpressionId(getExpressionIdForCondition(condition, conditionMap));

                topConditionPanel.add("br ", componentPanel);
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
    }

    /**
     * Method to experssion ID for conditions
     * 
     * @param condition
     * @param conditionMap
     * @return
     */
    private int getExpressionIdForCondition(ICondition condition, Map<Integer, Collection<ICondition>> conditionMap) {
        int expressionId = 1;
        for (Integer key : conditionMap.keySet()) {
            Collection<ICondition> conditions = conditionMap.get(key);
            if (conditions.contains(condition)) {
                expressionId = key;
                break;
            }
        }
        return expressionId;
    }

    /**
     * ShowResultsButton ActionListener
     * 
     * @author deepak_shingan
     * 
     */
    private class ShowResultsActionListener implements ActionListener {
        private void executeQuery(ICab2bQuery cab2bQuery) {
            // This code is generic and can be used to directly display the
            // executed query results

            // Set Query object into ClientQueryBuilder
            IClientQueryBuilderInterface clientQueryBuilder = new ClientQueryBuilder();
            clientQueryBuilder.setQuery(cab2bQuery);

            // Initialize MainSearchPanel
            GlobalNavigationPanel globalNavigationPanel = NewWelcomePanel.getMainFrame().getGlobalNavigationPanel();
            GlobalNavigationGlassPane globalNavigationGlassPane = globalNavigationPanel.getGlobalNavigationGlassPane();
            globalNavigationGlassPane.initializeMainSearchPanel();

            // Set ClientQueryBuilder object into MainSearchPanel and set it to
            // the 4rd card i.e. View Search Result
            MainSearchPanel mainSearchPanel = GlobalNavigationPanel.getMainSearchPanel();
            mainSearchPanel.setQueryObject(clientQueryBuilder);
            mainSearchPanel.getCenterPanel().getAddLimitPanel().setQueryObject(clientQueryBuilder);
            mainSearchPanel.getCenterPanel().setSelectedCardIndex(2);
            mainSearchPanel.setParaQueryShowResultButtonPressed(true);

            // Fire the query by clicking (simulating) Next button
            SearchNavigationPanel bottomPanel = mainSearchPanel.getSearchNavigationPanel();
            bottomPanel.getNextButton().doClick();

            // Open the Searcg dialog box
            // globalNavigationGlassPane.showSearchDialog();
            CommonUtils.launchSearchDataWizard();
            updateUI();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent arg0) {
            boolean validCondition = false;
            for (int index = 0; index < topConditionPanel.getComponentCount(); index++) {
                if (topConditionPanel.getComponent(index) instanceof AbstractTypePanel) {
                    AbstractTypePanel panel = (AbstractTypePanel) topConditionPanel.getComponent(index);
                    int conditionStatus = panel.isConditionValidBeforeSaving(ParameterizedQueryShowResultPanel.this);
                    if (conditionStatus == 0) {
                        validCondition = true;
                        queryDataModel.addCondition(panel.getExpressionId(),
                                                    panel.getCondition(index,
                                                                       ParameterizedQueryShowResultPanel.this, ""));

                    } else if (conditionStatus == 1) {
                        queryDataModel.removeCondition(panel.getExpressionId(),
                                                       panel.getCondition(index,
                                                                          ParameterizedQueryShowResultPanel.this,
                                                                          ""));
                    }
                }
            }
            if (!validCondition && bottomConditionPanel.getComponentCount() == 0) {
                JOptionPane.showMessageDialog(
                                              ParameterizedQueryShowResultPanel.this,
                                              "Please add values for atleast one condition before executing query.",
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dialog.dispose();
            executeQuery(queryDataModel.getQuery());
        }
    }

}
