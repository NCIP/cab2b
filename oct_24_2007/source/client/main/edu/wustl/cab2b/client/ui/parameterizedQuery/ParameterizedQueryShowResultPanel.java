/**
 * 
 */
package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Map;

import javax.swing.JOptionPane;

import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationGlassPane;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;

/**
 * @author deepak_shingan
 *
 */
public class ParameterizedQueryShowResultPanel extends ParameterizedQueryPreviewPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ParameterizedQueryDataModel queryDataModel;

    private Cab2bButton showResultButton;

    private Cab2bButton cancelButton;

    public ParameterizedQueryShowResultPanel(ICab2bParameterizedQuery query) {
        queryDataModel = new ParameterizedQueryDataModel(query);
        initGUI();
    }

    @Override
    protected Cab2bPanel getNavigationPanel() {
        if (navigationPanel == null)
            navigationPanel = new Cab2bPanel();

        navigationPanel.removeAll();
        navigationPanel.setLayout(new RiverLayout(5, 10));
        navigationPanel.setBackground(new Color(240, 240, 240));
        navigationPanel.add("right ", showResultButton);
        navigationPanel.add("right ", cancelButton);
        navigationPanel.add("br ", new Cab2bLabel());
        return navigationPanel;
    }

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
        createShowResultPreviewPanel(queryDataModel.getConditions());
        initTopConditionPanel();
        initBottomConditionPanel();
        this.add(getNavigationPanel(), BorderLayout.SOUTH);
    }

    /**
     * Method to create show result preview panel
     * @param conditionMap
     */
    private void createShowResultPreviewPanel(Map<IExpressionId, Collection<ICondition>> conditionMap) {
        //The following code is executing for StackPanel QueryLink Click
        AbstractTypePanel componentPanel = null;
        ParseXMLFile parseFile = null;

        try {
            parseFile = ParseXMLFile.getInstance();
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }

        try {
            getMaxLabelDimension(conditionMap);
            for (IExpressionId key : conditionMap.keySet()) {
                for (ICondition condition : conditionMap.get(key)) {
                    componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(parseFile,
                                                                                        condition.getAttribute(),
                                                                                        maxLabelDimension);
                    componentPanel.createPanelWithOperator(condition);

                    if (condition instanceof ParameterizedCondition) {
                        ParameterizedCondition paraCondition = (ParameterizedCondition) condition;

                        if (topConditionPanel.getComponentCount() < paraCondition.getIndex())
                            topConditionPanel.add("br ", componentPanel);
                        else
                            topConditionPanel.add(componentPanel, "br ",
                                                  ((ParameterizedCondition) condition).getIndex());
                    } else {
                        CommonUtils.disableAllComponent(componentPanel);
                        bottomConditionPanel.add("br ", componentPanel);
                    }
                    componentPanel.setExpressionId(key);
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
    }

    /**
     * ShowResultsButton ActionListener 
     * @author deepak_shingan
     *
     */
    private class ShowResultsActionListener implements ActionListener {
        private void executeQuery(ICab2bParameterizedQuery cab2bQuery) {
            // This code is generic and can be used to directly display the 
            // executed query results

            // Set Query object into ClientQueryBuilder
            IClientQueryBuilderInterface clientQueryBuilder = new ClientQueryBuilder();
            clientQueryBuilder.setQuery(cab2bQuery);

            // Initialize MainSearchPanel
            GlobalNavigationPanel globalNavigationPanel = NewWelcomePanel.mainFrame.getGlobalNavigationPanel();
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
            updateUI();
        }

        public void actionPerformed(ActionEvent arg0) {
            for (int index = 0; index < topConditionPanel.getComponentCount(); index++) {
                if (topConditionPanel.getComponent(index) instanceof AbstractTypePanel) {
                    AbstractTypePanel panel = (AbstractTypePanel) topConditionPanel.getComponent(index);
                    int conditionStatus = panel.isConditionValid(ParameterizedQueryShowResultPanel.this);
                    if (conditionStatus == 0) {
                        queryDataModel.addCondition(panel.getExpressionId(), panel.getCondition(index));
                    } else {
                        if (conditionStatus == 1) {
                            JOptionPane.showMessageDialog(ParameterizedQueryShowResultPanel.this,
                                                          "Please enter the values for selected field or remove the selection. \n Field name : "
                                                                  + panel.getAttributeDisplayName(), "Error",
                                                          JOptionPane.ERROR_MESSAGE);

                        }
                        return;
                    }
                }
            }
            dialog.dispose();
            executeQuery(queryDataModel.getQuery());
        }
    }

}
