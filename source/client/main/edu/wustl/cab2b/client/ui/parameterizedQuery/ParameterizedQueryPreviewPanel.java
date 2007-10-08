/**
 * 
 */
package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.MainSearchPanel;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationGlassPane;
import edu.wustl.cab2b.client.ui.mainframe.GlobalNavigationPanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
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
 * Panel to display conditions in Parameterized query 
 * @author deepak_shingan
 *
 */
public class ParameterizedQueryPreviewPanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Cab2bPanel topConditionPanel;

    private Cab2bPanel bottomConditionPanel;

    private Cab2bTitledPanel topConditionTitlePanel;

    private Cab2bTitledPanel bottomConditionTitlePanel;

    private ParameterizedQueryMainPanel parameterizedQueryMainPanel;

    private ParameterizedQueryDataModel queryDataModel;

    private Cab2bPanel navigationPanel;

    private Cab2bButton okButton;

    private Cab2bButton cancelButton;

    private Cab2bButton showResultButton;

    private Dimension maxLabelDimension;

    private JDialog dialog;

    private boolean isOrderPanel;

    private Cab2bButton upArrowButton;

    private Cab2bButton downArrowButton;

    public ParameterizedQueryPreviewPanel(ParameterizedQueryMainPanel parameterizedQueryMainPanel) {
        this.parameterizedQueryMainPanel = parameterizedQueryMainPanel;
        queryDataModel = parameterizedQueryMainPanel.getParameterizedQueryDataModel();
        isOrderPanel = true;
        initGUI();
    }

    public ParameterizedQueryPreviewPanel(
            ParameterizedQueryMainPanel parameterizedQueryMainPanel,
            boolean isOrderPanel) {
        this.parameterizedQueryMainPanel = parameterizedQueryMainPanel;
        queryDataModel = parameterizedQueryMainPanel.getParameterizedQueryDataModel();
        this.isOrderPanel = isOrderPanel;
        initGUI();
    }

    public ParameterizedQueryPreviewPanel(ICab2bParameterizedQuery query) {
        parameterizedQueryMainPanel = null;
        queryDataModel = new ParameterizedQueryDataModel(query);
        /*as reference of ParameterizedQueryMainPanel is not avialable
         the panel can be used only to show result after main frame stack box 
         hyperlink click */
        isOrderPanel = false;
        initGUI();
    }

    /**
     * Method to creat preview panel
     */
    private void initGUI() {

        bottomConditionPanel = new Cab2bPanel();
        topConditionPanel = new Cab2bPanel();

        if (parameterizedQueryMainPanel != null) {
            okButton = new Cab2bButton("Ok");
            okButton.addActionListener(new OkButtonActionListener());
        } else {
            showResultButton = new Cab2bButton("Show Results");
            showResultButton.setPreferredSize(new Dimension(125, 22));
            showResultButton.addActionListener(new ShowResultsActionListener());
        }

        cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                dialog.dispose();
            }
        });
        createPreviewPanel();

        if (topConditionPanel.getComponentCount() > 0) {
            JScrollPane topScrollPane = new JScrollPane(topConditionPanel);
            topScrollPane.getViewport().setBackground(Color.white);
            topScrollPane.setBorder(BorderFactory.createEmptyBorder());
            topConditionTitlePanel = new Cab2bTitledPanel("Parameterized Conditions");
            Cab2bPanel topPanelContainer = (Cab2bPanel) topConditionTitlePanel.getContentContainer();
            topPanelContainer.add(topScrollPane);
            if (isOrderPanel) {

                ImageIcon icon = createImageIcon("upArrow.gif");
                upArrowButton = new Cab2bButton(icon);
                upArrowButton.addActionListener(new UpArrowActionListener());
                icon = createImageIcon("downArrow.gif");
                downArrowButton = new Cab2bButton(icon);
                downArrowButton.addActionListener(new DownArrowActionListener());
                Cab2bPanel orderLabelPanel = new Cab2bPanel();

                orderLabelPanel.add(" br", upArrowButton);
                orderLabelPanel.add(" br", downArrowButton);
                topPanelContainer.add("tab ", orderLabelPanel);
            }
            topConditionTitlePanel.setMaximumSize(new Dimension(
                    (int) (MainFrame.mainframeScreenDimesion.width * 0.40),
                    (int) (MainFrame.mainframeScreenDimesion.height * 0.35)));
            this.add(topConditionTitlePanel, BorderLayout.NORTH);
        }

        if (bottomConditionPanel.getComponentCount() > 0) {
            JScrollPane bottomScrollPane = new JScrollPane(bottomConditionPanel);
            bottomScrollPane.getViewport().setBackground(Color.white);
            bottomScrollPane.setBorder(BorderFactory.createEmptyBorder());
            bottomConditionTitlePanel = new Cab2bTitledPanel("Defined Conditions");
            bottomConditionTitlePanel.add(bottomScrollPane);
            this.add(bottomConditionTitlePanel, BorderLayout.CENTER);
        }
        this.add(getNavigationPanel(), BorderLayout.SOUTH);
        return;
    }

    /** 
     * Returns an Icon, or null if the path was invalid. 
     */
    private ImageIcon createImageIcon(String name) {
        ImageIcon newLeafIcon = new ImageIcon(getClass().getClassLoader().getResource(name));
        return newLeafIcon;
    }

    private Cab2bPanel getNavigationPanel() {
        if (navigationPanel == null)
            navigationPanel = new Cab2bPanel();

        navigationPanel.removeAll();
        navigationPanel.setLayout(new RiverLayout(5, 10));
        navigationPanel.setBackground(new Color(240, 240, 240));

        if (parameterizedQueryMainPanel != null) {
            navigationPanel.add("right ", okButton);
        } else {
            navigationPanel.add("right ", showResultButton);
        }

        navigationPanel.add("right ", cancelButton);
        navigationPanel.add("br ", new Cab2bLabel());
        return navigationPanel;

    }

    public void showInDialog() {
        Dimension dimension = MainFrame.mainframeScreenDimesion;
        String dialogTitle = "Unsaved Condition";

        if (!isOrderPanel)
            dialogTitle = queryDataModel.getQueryName();

        dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, dialogTitle, new Dimension(
                (int) (dimension.width * 0.80), (int) (dimension.height * 0.75)), true, false);

        dialog.setVisible(true);
    }

    private Dimension getMaxLabelDimension(Map<IExpressionId, Collection<ICondition>> conditionMap) {
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (IExpressionId key : conditionMap.keySet()) {
            for (ICondition condition : conditionMap.get(key)) {
                attributeList.add(condition.getAttribute());
            }
        }

        Dimension maxDimension = CommonUtils.getMaximumLabelDimension(attributeList);
        if (maxLabelDimension == null || maxLabelDimension.width < maxDimension.width) {
            maxLabelDimension = maxDimension;
        }
        //increasing max label width to display expression ID with each label
        maxLabelDimension.width = maxLabelDimension.width + 15;
        return maxLabelDimension;
    }

    /**
     *  Method to create order preview panel
     * @param conditionMap
     */
    private void creatOrderPreviewPanel(Map<IExpressionId, Collection<ICondition>> conditionMap) {
        AbstractTypePanel componentPanel = null;
        List<AbstractTypePanel> panelList;
        ParseXMLFile parseFile = null;
        try {
            parseFile = ParseXMLFile.getInstance();
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
        ParameterizedQueryConditionPanel conditionPanel = parameterizedQueryMainPanel.getParameterConditionPanel();
        panelList = conditionPanel.getCheckedAttributePanels(conditionPanel.getConditionPanel());
        for (int index = 0; index < panelList.size(); index++) {
            AbstractTypePanel panel = panelList.get(index);
            //uncheck all checkboxes 
            panel.setAttributeCheckBox(false);

            //set changed/unchanged label for each attribute field 
            panel.setAttributeDisplayName(panel.getAttributeDisplayNameTextField().getText());

            //hide the attributeDisplayText box                    
            panel.remove(panel.getAttributeDisplayNameTextField());

            //Now add to the condition panel
            topConditionPanel.add("br ", panelList.get(index));
        }

        panelList = parameterizedQueryMainPanel.getParameterConditionPanel().getUnCheckedAttributePanels();
        for (int index = 0; index < panelList.size(); index++) {
            AbstractTypePanel oldComponentPanel = panelList.get(index);
            String conditionString = oldComponentPanel.getCondition();
            if (((conditionString.equals("Is Null")) || conditionString.equals("Is Not Null") || (oldComponentPanel.getValues().size() != 0))) {

                try {
                    componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                        parseFile,
                                                                                        oldComponentPanel.getAttributeEntity(),
                                                                                        true, maxLabelDimension);
                    componentPanel.setAttributeDisplayName(oldComponentPanel.getAttributeDisplayName());
                } catch (CheckedException checkedException) {
                    CommonUtils.handleException(checkedException, this, true, true, false, false);
                }
                setConditionValues(componentPanel);
                CommonUtils.disableAllComponent(componentPanel);
                bottomConditionPanel.add("br ", componentPanel);
            }
        }
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
            for (IExpressionId key : conditionMap.keySet()) {
                for (ICondition condition : conditionMap.get(key)) {
                    if (condition instanceof ParameterizedCondition) {

                        ParameterizedCondition paraCondition = (ParameterizedCondition) condition;
                        componentPanel = (AbstractTypePanel) SwingUIManager.generateParameterizedUIPanel(
                                                                                                         parseFile,
                                                                                                         paraCondition.getAttribute(),
                                                                                                         true,
                                                                                                         maxLabelDimension,
                                                                                                         false,
                                                                                                         paraCondition.getName());
                        setConditionValues(componentPanel);
                        if (topConditionPanel.getComponentCount() < paraCondition.getIndex())
                            topConditionPanel.add("br ", componentPanel);
                        else
                            topConditionPanel.add(componentPanel, "br ",
                                                  ((ParameterizedCondition) condition).getIndex());
                    } else {
                        componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                            parseFile,
                                                                                            condition.getAttribute(),
                                                                                            true,
                                                                                            maxLabelDimension);
                        setConditionValues(componentPanel);
                        componentPanel.setAttributeDisplayName(componentPanel.getAttributeDisplayName() + "_"
                                + key.getInt());
                        CommonUtils.disableAllComponent(componentPanel);
                        bottomConditionPanel.add("br ", componentPanel);
                    }
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
    }

    /**
     * Method to create preview panel
     *  
     */
    private void createPreviewPanel() {
        this.setLayout(new BorderLayout());
        Map<IExpressionId, Collection<ICondition>> conditionMap;
        if (isOrderPanel == false)
            conditionMap = queryDataModel.getConditions();
        else
            conditionMap = parameterizedQueryMainPanel.getParameterizedQueryDataModel().getConditions();

        getMaxLabelDimension(conditionMap);

        if (parameterizedQueryMainPanel != null) {
            creatOrderPreviewPanel(conditionMap);
        } else {
            createShowResultPreviewPanel(conditionMap);
        }
    }

    /**
     * This Method checks whether the given attribute is associated with any condition 
     * and sets the condition values. 
     * @param attribute
     * @return
     */
    private void setConditionValues(AbstractTypePanel componentPanel) {

        AttributeInterface attribute = componentPanel.getAttributeEntity();
        Map<IExpressionId, Collection<ICondition>> conditionMap;
        if (queryDataModel != null)
            conditionMap = queryDataModel.getConditions();
        else
            conditionMap = parameterizedQueryMainPanel.getParameterizedQueryDataModel().getConditions();

        for (IExpressionId key : conditionMap.keySet()) {
            for (ICondition condition : conditionMap.get(key)) {
                if (condition.getAttribute() == attribute) {
                    componentPanel.setValues(new ArrayList<String>(condition.getValues()));
                    componentPanel.setCondition(condition.getRelationalOperator().getStringRepresentation());
                    componentPanel.setExpressionId(key);
                }
            }
        }
    }

    /**
     * Returns all selected attribute panels from the top-condition panels  
     * @return
     */
    Map<Integer, AbstractTypePanel> getAllSelectedPanel() {
        Map<Integer, AbstractTypePanel> panelMap = new HashMap<Integer, AbstractTypePanel>();
        for (int index = 0; index < topConditionPanel.getComponentCount(); index++) {
            if ((topConditionPanel.getComponent(index) instanceof AbstractTypePanel)) {
                AbstractTypePanel panel = (AbstractTypePanel) topConditionPanel.getComponent(index);
                if (panel.isAttributeCheckBoxSelected())
                    panelMap.put(index, (AbstractTypePanel) topConditionPanel.getComponent(index));
            }
        }
        return panelMap;
    }

    private class OkButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            dialog.dispose();

            Cab2bPanel basePanel = parameterizedQueryMainPanel.getParameterConditionPanel().getConditionPanel();
            int totalPanelCount = topConditionPanel.getComponentCount();
            //Keep in mind whenever you will add panel from topConditionPanel to 
            //base panel it will automatically get removed from topConditionPanel 
            for (int index = 0; index < totalPanelCount; index++) {

                if (topConditionPanel.getComponent(0) instanceof AbstractTypePanel) {
                    AbstractTypePanel oldPanel = (AbstractTypePanel) topConditionPanel.getComponent(0);

                    oldPanel.add(oldPanel.getAttributeDisplayNameTextField(), 1);
                    oldPanel.setAttributeCheckBox(true);
                    basePanel.add(oldPanel, "br ", index);
                }
            }
            updateUI();
            parameterizedQueryMainPanel.showInDialog();
        }
    }

    /*
     * DownArrowButton Action listener 
     * @author deepak_shingan
     *
     */
    private class DownArrowActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            Map<Integer, AbstractTypePanel> panelMap = getAllSelectedPanel();
            for (Integer index : panelMap.keySet()) {
                AbstractTypePanel downPanel;
                if (index + 1 != topConditionPanel.getComponentCount()) {
                    downPanel = (AbstractTypePanel) topConditionPanel.getComponent(index);
                    topConditionPanel.remove(downPanel);
                    topConditionPanel.add(downPanel, "br ", index + 1);
                    topConditionPanel.updateUI();
                }
            }
        }
    }

    /**
     * UpArrowButton ActionListener
     * @author deepak_shingan
     *
     */
    private class UpArrowActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            Map<Integer, AbstractTypePanel> panelMap = getAllSelectedPanel();
            for (Integer index : panelMap.keySet()) {
                AbstractTypePanel downPanel;
                if (index != 0) {
                    downPanel = (AbstractTypePanel) topConditionPanel.getComponent(index);
                    topConditionPanel.remove(downPanel);
                    topConditionPanel.add(downPanel, "br ", index - 1);
                    topConditionPanel.updateUI();
                }
            }
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
            if (CommonUtils.updateQueryCondtions(queryDataModel, topConditionPanel,
                                                 ParameterizedQueryPreviewPanel.this)) {
                executeQuery(queryDataModel.getQuery());
                dialog.dispose();
            }
        }
    }
}
