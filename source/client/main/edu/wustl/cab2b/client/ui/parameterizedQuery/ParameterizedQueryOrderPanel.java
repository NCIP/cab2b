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
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.ICab2bParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;

/**
 * @author deepak_shingan
 *
 */
public class ParameterizedQueryOrderPanel extends Cab2bPanel {

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

    public ParameterizedQueryOrderPanel(ParameterizedQueryMainPanel parameterizedQueryMainPanel) {
        this.parameterizedQueryMainPanel = parameterizedQueryMainPanel;
        queryDataModel = null;
        initGUI();
    }

    public ParameterizedQueryOrderPanel(ICab2bParameterizedQuery query) {
        parameterizedQueryMainPanel = null;
        queryDataModel = new ParameterizedQueryDataModel(query);
        initGUI();
    }

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
        createOrderPanel();
        if (topConditionPanel.getComponentCount() > 0) {
            JScrollPane topScrollPane = new JScrollPane(topConditionPanel);
            topScrollPane.getViewport().setBackground(Color.white);
            topScrollPane.setBorder(BorderFactory.createEmptyBorder());
            topConditionTitlePanel = new Cab2bTitledPanel("Parameterized Conditions");
            topConditionTitlePanel.add(topScrollPane);
            topConditionTitlePanel.setMaximumSize(new Dimension(
                    (int) (MainFrame.mainframeScreenDimesion.width * 0.40),
                    (int) (MainFrame.mainframeScreenDimesion.height * 0.35)));
            this.add(topConditionTitlePanel, BorderLayout.NORTH);
        }

        if (bottomConditionPanel.getComponentCount() > 0) {
            bottomConditionPanel.setEnabled(false);
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
        dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, "Unsaved Condition", new Dimension(
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
        return maxLabelDimension;
    }

    private void createOrderPanel() {
        this.setLayout(new BorderLayout());
        AbstractTypePanel componentPanel = null;
        ParseXMLFile parseFile;
        Map<IExpressionId, Collection<ICondition>> conditionMap;

        if (queryDataModel != null)
            conditionMap = queryDataModel.getConditions();
        else
            conditionMap = parameterizedQueryMainPanel.getParameterizedQueryDataModel().getConditions();

        getMaxLabelDimension(conditionMap);
        try {
            parseFile = ParseXMLFile.getInstance();
            if (parameterizedQueryMainPanel != null) {
                List<AbstractTypePanel> panelList;
                panelList = parameterizedQueryMainPanel.getParameterConditionPanel().getCheckedAttributePanels();
                for (int index = 0; index < panelList.size(); index++)
                    topConditionPanel.add("br ", panelList.get(index));

                panelList = parameterizedQueryMainPanel.getParameterConditionPanel().getUnCheckedAttributePanels();
                for (int index = 0; index < panelList.size(); index++) {
                    AbstractTypePanel oldComponentPanel = panelList.get(index);

                    componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                        parseFile,
                                                                                        oldComponentPanel.getAttributeEntity(),
                                                                                        true, maxLabelDimension);
                    setConditionValues(componentPanel);
                    bottomConditionPanel.add("br ", componentPanel);
                }
            } else {
                //The following code is executing for StackPanel QueryLink Click           
                try {
                    for (IExpressionId key : conditionMap.keySet()) {
                        for (ICondition condition : conditionMap.get(key)) {
                            if (condition instanceof ParameterizedCondition) {
                                componentPanel = (AbstractTypePanel) SwingUIManager.generateParameterizedUIPanel(
                                                                                                                 parseFile,
                                                                                                                 condition.getAttribute(),
                                                                                                                 true,
                                                                                                                 maxLabelDimension);
                                setConditionValues(componentPanel);
                                topConditionPanel.add("br ", componentPanel);
                            } else {
                                componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                                    parseFile,
                                                                                                    condition.getAttribute(),
                                                                                                    true,
                                                                                                    maxLabelDimension);
                                setConditionValues(componentPanel);
                                bottomConditionPanel.add("br ", componentPanel);
                            }
                        }
                    }
                } catch (CheckedException checkedException) {
                    CommonUtils.handleException(checkedException, this, true, true, false, false);
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
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

    private class OkButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            dialog.dispose();
        }

    }

    private class ShowResultsActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            CommonUtils.updateQueryCondtions(queryDataModel, topConditionPanel, ParameterizedQueryOrderPanel.this);
        }
    }
}
