package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.querysuite.queryobject.ICondition;

/**
 * Panel to preview conditions in Parameterized query 
 * @author deepak_shingan
 *
 */
public abstract class ParameterizedQueryPreviewPanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected Cab2bPanel topConditionPanel;

    protected Cab2bPanel bottomConditionPanel;

    protected Cab2bTitledPanel topConditionTitlePanel;

    protected Cab2bTitledPanel bottomConditionTitlePanel;

    protected Cab2bPanel navigationPanel;

    protected Dimension maxLabelDimension;

    protected JDialog dialog;

    /**
     * Method to creat preview panel
     */
    protected abstract void initGUI();

    protected abstract Cab2bPanel getNavigationPanel();

    public void showInDialog() {
        Dimension dimension = MainFrame.mainframeScreenDimesion;
        String dialogTitle = "Unsaved Condition";

        dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, dialogTitle, new Dimension(
                (int) (dimension.width * 0.80), (int) (dimension.height * 0.75)), true, false);
        dialog.setVisible(true);
    }

    protected void initTopConditionPanel() {
        if (topConditionPanel.getComponentCount() > 0) {
            JScrollPane topScrollPane = new JScrollPane(topConditionPanel);
            topScrollPane.getViewport().setBackground(Color.white);
            topScrollPane.setBorder(BorderFactory.createEmptyBorder());
            topConditionTitlePanel = new Cab2bTitledPanel("Parameterized Conditions");
            Cab2bPanel topPanelContainer = (Cab2bPanel) topConditionTitlePanel.getContentContainer();
            topPanelContainer.add(topScrollPane);
            topConditionTitlePanel.setMaximumSize(new Dimension(
                    (int) (MainFrame.mainframeScreenDimesion.width * 0.40),
                    (int) (MainFrame.mainframeScreenDimesion.height * 0.35)));
            this.add(topConditionTitlePanel, BorderLayout.NORTH);
        }
    }

    protected void initBottomConditionPanel() {
        if (bottomConditionPanel.getComponentCount() > 0) {
            JScrollPane bottomScrollPane = new JScrollPane(bottomConditionPanel);
            bottomScrollPane.getViewport().setBackground(Color.white);
            bottomScrollPane.setBorder(BorderFactory.createEmptyBorder());
            bottomConditionTitlePanel = new Cab2bTitledPanel("Defined Conditions");
            bottomConditionTitlePanel.add(bottomScrollPane);
            this.add(bottomConditionTitlePanel, BorderLayout.CENTER);
        }
    }

    protected Dimension getMaxLabelDimension(Map<Integer, Collection<ICondition>> conditionMap) {
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (Integer key : conditionMap.keySet()) {
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
     * This Method checks whether the given attribute is associated with any condition 
     * and sets the condition values. 
     * @param attribute
     * @return
     */
    protected void setConditionValues(AbstractTypePanel componentPanel,
                                      Map<Integer, Collection<ICondition>> conditionMap) {
        AttributeInterface attribute = componentPanel.getAttributeEntity();
        for (Integer key : conditionMap.keySet()) {
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
}
