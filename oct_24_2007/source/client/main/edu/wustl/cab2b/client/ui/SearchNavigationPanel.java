package edu.wustl.cab2b.client.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.dag.MainDagPanel;
import edu.wustl.cab2b.client.ui.experiment.NewExperimentDetailsPanel;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryDataModel;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryMainPanel;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.query.Utility;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.client.ui.viewresults.DataListPanel;
import edu.wustl.cab2b.client.ui.viewresults.ViewSearchResultsPanel;
import edu.wustl.cab2b.common.datalist.IDataRow;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.result.IQueryResult;
import edu.wustl.common.querysuite.queryobject.impl.ExpressionId;

/**
 * @author mahesh_iyer
 * 
 * The class represents the navigation searchPanel for the search dialog, and
 * also includes corresponding functionality.
 */

public class SearchNavigationPanel extends Cab2bPanel implements ActionListener {
    private static final long serialVersionUID = 1234567890L;

    /* The reference to the parent searchPanel. */
    private MainSearchPanel m_mainSearchPanel = null;

    /* The navigation buttons. */
    private Cab2bButton previousButton;

    public Cab2bButton nextButton;

    /* Custom buttons for this dialog. */
    private Cab2bButton resetButton;

    private Cab2bButton saveDataListButton;

    private Cab2bButton saveConditionButton;

    private Cab2bButton addToExperimentButton;

    private Cab2bPanel buttonPanel;

    private Cab2bPanel messagePanel;

    public static Cab2bLabel messageLabel;

    private IQueryResult queryResults;

    public SearchNavigationPanel(MainSearchPanel panel) {
        this.m_mainSearchPanel = panel;
        initGUI();
        this.setPreferredSize(new Dimension(976, 36));
        this.setBackground(new Color(240, 240, 240));
    }

    public Cab2bButton getNextButton() {
        return nextButton;
    }

    private void initGUI() {
        messageLabel = new Cab2bLabel();
        messageLabel.setForeground(new Color(0, 128, 0));
        messagePanel = new Cab2bPanel();
        messagePanel.setBackground(null);
        messagePanel.add(messageLabel);

        resetButton = new Cab2bButton("Reset");
        resetButton.addActionListener(this);

        previousButton = new Cab2bButton("Previous");
        previousButton.addActionListener(new PreviousButtonActionListener());

        nextButton = new Cab2bButton("Next");
        nextButton.addActionListener(new NextButtonActionListener());

        saveDataListButton = new Cab2bButton("Save Data List");
        saveDataListButton.setPreferredSize(new Dimension(160, 22));
        saveDataListButton.addActionListener(this);

        saveConditionButton = new Cab2bButton("Save Condition");
        saveConditionButton.setPreferredSize(new Dimension(160, 22));
        saveConditionButton.addActionListener(new SaveConditionButtonActionListener());

        addToExperimentButton = new Cab2bButton("Add to Experiment");
        addToExperimentButton.setPreferredSize(new Dimension(160, 22));
        addToExperimentButton.addActionListener(this);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
        flowLayout.setHgap(10);
        buttonPanel = new Cab2bPanel();
        buttonPanel.setBackground(null);
        buttonPanel.setLayout(flowLayout);
        buttonPanel.add(saveConditionButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(saveDataListButton);
        buttonPanel.add(addToExperimentButton);
        this.add("hfill", messagePanel);
        this.add("hfill right", buttonPanel);

        setButtons();
    }

    /**
     * Action listener class 
     */
    public void actionPerformed(ActionEvent event) {
        Cab2bButton cab2bButton = (Cab2bButton) event.getSource();
        String strActionCommand = cab2bButton.getActionCommand();
        if (strActionCommand.equals("Save Data List")) {
            SaveDatalistPanel saveDataListPanel = new SaveDatalistPanel(m_mainSearchPanel);
            saveDataListPanel.showInDialog();
        } else if (strActionCommand.equals("Add to Experiment")) {
            if (SaveDatalistPanel.isDataListSaved == false) {
                SaveDatalistPanel saveDataListPanel = new SaveDatalistPanel(m_mainSearchPanel);
                saveDataListPanel.showInDialog();
            }
            if (SaveDatalistPanel.isDataListSaved) {
                NewExperimentDetailsPanel newExperimentDetailsPanel = new NewExperimentDetailsPanel();
                newExperimentDetailsPanel.showInDialog();
            }
        } else if (strActionCommand.equals("Reset")) {
            // 1. Reset the query object
            // 2. Clear result panels
            // 3. Clear breadcrumb on the searchPanel            
            SearchNavigationPanel.messageLabel.setText("");
            m_mainSearchPanel.getCenterPanel().reset();
            m_mainSearchPanel.setQueryObject(null);
        }
        updateUI();
    }

    public void enableButtons() {
        setButtons();
    }

    /*
     * The method sets the focus appropriately for the top searchPanel. Also,
     * update the indexes. corresponding updated can be abstracted into the top
     * searchPanel class itself
     */
    public void showCard(boolean blnNext) {
        /* Get the center Panel through the parent. */
        SearchCenterPanel centerPanel = this.m_mainSearchPanel.getCenterPanel();

        /* Get the layout associated with the center searchPanel */
        CardLayout layout = (CardLayout) centerPanel.getLayout();

        /* Get the currently selected index. */
        int iSelectedCard = centerPanel.getSelectedCardIndex();

        /* Get the top Panel through the parent. */
        SearchTopPanel topPanel = this.m_mainSearchPanel.getTopPanel();

        if (blnNext && ((iSelectedCard + 1) < centerPanel.getIdentifierCount())) {
            /*
             * Validate and increment index to get the corresponding identifier,
             * and use that to show the card. Also increment the index to
             * indicate the currently selected card.
             */
            layout.show(centerPanel, centerPanel.getIdentifier(iSelectedCard + 1));
            centerPanel.setSelectedCardIndex((iSelectedCard + 1));
            topPanel.setFocus(iSelectedCard + 1, true);
        } else if (iSelectedCard > 0) {
            /*
             * Validate and decrement index to get the corresponding identifier,
             * and use that to show the card. Also decrement the index to
             * indicate the currently selected card.
             */
            layout.show(centerPanel, centerPanel.getIdentifier(iSelectedCard - 1));
            centerPanel.setSelectedCardIndex((iSelectedCard - 1));
            topPanel.setFocus(iSelectedCard - 1, false);
        }
        setButtons();
    }

    private void setButtons() {
        int cardindex = m_mainSearchPanel.getCenterPanel().getSelectedCardIndex();

        if (cardindex == 0)
            previousButton.setVisible(false);
        else
            previousButton.setVisible(true);

        if (cardindex == 1)
            resetButton.setVisible(true);
        else
            resetButton.setVisible(false);

        if (cardindex > 1 && cardindex < 4)
            saveConditionButton.setVisible(true);
        else
            saveConditionButton.setVisible(false);

        if (cardindex == 4) {
            saveDataListButton.setVisible(true);
            nextButton.setVisible(false);
            addToExperimentButton.setVisible(true);
        } else {
            saveDataListButton.setVisible(false);
            nextButton.setVisible(true);
            addToExperimentButton.setVisible(false);
            if (cardindex == 3 && CommonUtils.getDataListSize() == 0) {
                nextButton.setEnabled(false);
            } else {
                nextButton.setEnabled(true);
            }
        }
    }

    /**
     * If got error or zero result on query execution call this method if we are
     * calling this method don't call showCard Method
     */
    public void gotoAddLimitPanel() {
        SearchCenterPanel centerPanel = m_mainSearchPanel.getCenterPanel();
        CardLayout layout = (CardLayout) centerPanel.getLayout();

        layout.show(centerPanel, centerPanel.getIdentifier(1));
        centerPanel.setSelectedCardIndex(1);
        SearchTopPanel topPanel = this.m_mainSearchPanel.getTopPanel();
        topPanel.setFocus(1, false);
        setButtons();
    }

    /**
     * Method to switch from view search result searchPanel to searchPanel data
     * list searchPanel ...
     * 
     * @param datarow
     */

    public void gotoDataListPanel(final IDataRow datarow) {
        SearchCenterPanel searchCenterPanel = m_mainSearchPanel.getCenterPanel();
        DataListPanel dataListPanel = (DataListPanel) searchCenterPanel.m_arrCards[searchCenterPanel.getSelectedCardIndex() + 1];
        if (dataListPanel != null) {
            searchCenterPanel.remove(dataListPanel);
        }
        if (datarow == null) {
            dataListPanel = new DataListPanel(MainSearchPanel.getDataList());
        } else {
            dataListPanel = new DataListPanel(MainSearchPanel.getDataList(), datarow);
        }

        searchCenterPanel.m_arrCards[4] = dataListPanel;
        searchCenterPanel.add(dataListPanel, SearchCenterPanel.m_strDataListlbl);
        showCard(true);
    }

    /**
     * Action listener for next button
     * 
     */
    private class NextButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            final SearchCenterPanel searchCenterPanel = m_mainSearchPanel.getCenterPanel();
            final IClientQueryBuilderInterface clientQueryBuilder = m_mainSearchPanel.getQueryObject();
            SearchNavigationPanel.messageLabel.setText("");

            if (searchCenterPanel.getSelectedCardIndex() == 0) {
                AddLimitPanel addLimitPanel = searchCenterPanel.getAddLimitPanel();
                addLimitPanel.addSearchPanel(searchCenterPanel.getChooseCategoryPanel().getSearchPanel());
                searchCenterPanel.setAddLimitPanel(addLimitPanel);
                resetButton.setVisible(true);
                showCard(true);
            } else if (searchCenterPanel.getSelectedCardIndex() == 1) {
                if (clientQueryBuilder != null && clientQueryBuilder.getVisibleExressionIds().size() > 0) {

                    // Also cause for the next card in the dialog to be added
                    // dynamically to the dialog.
                    AdvancedDefineViewPanel defineViewPanel = new AdvancedDefineViewPanel(searchCenterPanel);
                    if (AdvancedDefineViewPanel.isMultipleGraphException == true) {
                        gotoAddLimitPanel();
                    } else {
                        if (null != searchCenterPanel.m_arrCards[2]) {
                            searchCenterPanel.remove(searchCenterPanel.m_arrCards[2]);
                        }
                        searchCenterPanel.m_arrCards[2] = defineViewPanel;
                        searchCenterPanel.add(defineViewPanel, SearchCenterPanel.m_strDefineSearchResultslbl);
                        // Implies the next button was clicked. Call show card
                        // with boolean set to true.                    
                        showCard(true);
                    }
                } else {
                    // Pop-up a dialog asking the user to add alteast a rule.
                    JOptionPane.showMessageDialog(m_mainSearchPanel.getParent(),
                                                  "Please add Limit(s) before proceeding", "Cannot Proceed",
                                                  JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else if (searchCenterPanel.getSelectedCardIndex() == 2) {
                CustomSwingWorker swingWorker = new CustomSwingWorker(SearchNavigationPanel.this) {

                    protected void doNonUILogic() throws Exception {
                        // Get the Functional class for root and update query
                        // object with it.
                        queryResults = CommonUtils.executeQuery((ICab2bQuery) clientQueryBuilder.getQuery(),
                                                                m_mainSearchPanel);
                    }

                    protected void doUIUpdateLogic() {
                        if (queryResults != null) {
                            int recordNo = Utility.getRecordNum(queryResults);
                            if (recordNo == 0) {
                                JOptionPane.showMessageDialog(
                                                              SearchNavigationPanel.this.m_mainSearchPanel.getParent(),
                                                              "No result found.", "",
                                                              JOptionPane.INFORMATION_MESSAGE);
                                gotoAddLimitPanel();
                            } else {
                                ViewSearchResultsPanel viewSearchResultsPanel = new ViewSearchResultsPanel(
                                        queryResults, m_mainSearchPanel);
                                if (searchCenterPanel.m_arrCards[3] != null) {
                                    searchCenterPanel.remove(searchCenterPanel.m_arrCards[3]);
                                }
                                searchCenterPanel.m_arrCards[3] = viewSearchResultsPanel;
                                searchCenterPanel.add(viewSearchResultsPanel,
                                                      SearchCenterPanel.m_strViewSearchResultslbl);
                                showCard(true);
                            }
                        } else {
                            gotoAddLimitPanel();
                        }
                    }
                };
                swingWorker.start();
            } else if (searchCenterPanel.getSelectedCardIndex() == 3) {
                gotoDataListPanel(null);
            } else {
                showCard(true);
            }
        }
    }

    /**
     * Action listener for previous button 
     *
     */
    private class PreviousButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            final SearchCenterPanel searchCenterPanel = m_mainSearchPanel.getCenterPanel();
            // Implies the previous button was clicked. Call show card with
            // boolean set to false.
            SearchNavigationPanel.messageLabel.setText("");
            int cardIndex = searchCenterPanel.getSelectedCardIndex();
            if (cardIndex == 1) {
                // setting the search panel
                ChooseCategoryPanel chooseCategoryPanel = searchCenterPanel.getChooseCategoryPanel();
                chooseCategoryPanel.addSearchPanel(searchCenterPanel.getAddLimitPanel().getSearchPanel());
                searchCenterPanel.setChooseCategoryPanel(chooseCategoryPanel);
            } else if (cardIndex == 2) {
                AddLimitPanel addLimitPanel = (AddLimitPanel) m_mainSearchPanel.getCenterPanel().getAddLimitPanel();
                MainDagPanel mainDagPanel = addLimitPanel.getMainDagPanel();

                if (mainDagPanel.getVisibleNodeCount() == 0 && mainDagPanel.getExpressionCount() > 0) {

                    mainDagPanel.updateGraph();
                    addLimitPanel.editAddLimitUI(mainDagPanel.getFirstExpression());
                    mainDagPanel.selectNode((ExpressionId) mainDagPanel.getFirstExpression().getExpressionId());
                }
            }
            showCard(false);
        }
    }

    /**
     * Save button action listener class
     * @author deepak_shingan
     *
     */
    private class SaveConditionButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            ParameterizedQueryMainPanel parameterizedQueryMainPanel = new ParameterizedQueryMainPanel(
                    new ParameterizedQueryDataModel((ICab2bQuery) m_mainSearchPanel.getQueryObject().getQuery()));
            parameterizedQueryMainPanel.showInDialog();
        }
    }
}