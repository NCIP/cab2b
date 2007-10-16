/**
 * 
 */
package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpressionId;

/**
 * Class to generate Parameterized Query Condition GUI 
 * @author deepak_shingan
 *
 */
public class ParameterizedQueryConditionPanel extends Cab2bTitledPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Cab2bPanel contentPanel;

    private Cab2bPanel allAttributePanel;

    private Cab2bPanel onlyConditionAttributePanel;

    private ParameterizedQueryDataModel queryDataModel;

    private boolean isShowAllAttribute;

    private Cab2bPanel headerPanel;

    private Cab2bComboBox filterComboBox;

    private Dimension maxLabelDimension;

    public ParameterizedQueryConditionPanel() {
        this(null, true);
    }

    public ParameterizedQueryConditionPanel(ParameterizedQueryDataModel queryDataModel, boolean isShowAllAttribute) {
        this.queryDataModel = queryDataModel;
        this.isShowAllAttribute = isShowAllAttribute;
        maxLabelDimension = getMaximumDimensionForAttribute();
        initGUI();
    }

    /**
     * Method to get maximum dimension for specified attribute list
     * @param allAttributes
     * @return Dimension maxLabelDimension
     */
    private Dimension getMaximumDimensionForAttribute() {
        Map<IExpressionId, Collection<AttributeInterface>> allAttributes = queryDataModel.getAllAttributes();
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (IExpressionId exprId : allAttributes.keySet()) {
            Collection<AttributeInterface> attributeCollection = allAttributes.get(exprId);
            attributeList.addAll(attributeCollection);
        }
        Collections.sort(attributeList, new AttributeInterfaceComparator());
        maxLabelDimension = CommonUtils.getMaximumLabelDimension(attributeList);
        //Increasing max label width to display expression ID with each label
        maxLabelDimension.width = maxLabelDimension.width + 15;
        return maxLabelDimension;
    }

    private void updatePanelStatus(AbstractTypePanel componentPanel) {
        if (onlyConditionAttributePanel != null) {
            int panelCount = onlyConditionAttributePanel.getComponentCount();
            for (int index = 0; index < panelCount; index++) {
                if (onlyConditionAttributePanel.getComponent(index) instanceof AbstractTypePanel) {
                    AbstractTypePanel conditionPanel = (AbstractTypePanel) onlyConditionAttributePanel.getComponent(index);
                    if (conditionPanel.getAttributeEntity() == componentPanel.getAttributeEntity()
                            && conditionPanel.getExpressionId() == componentPanel.getExpressionId()) {
                        componentPanel.setAttributeCheckBox(conditionPanel.isAttributeCheckBoxSelected());
                        componentPanel.setAttributeDisplayName(conditionPanel.getAttributeDisplayName());
                        componentPanel.setCondition(conditionPanel.getConditionItem());
                        componentPanel.setValues(new ArrayList<String>(conditionPanel.getValues()));
                    }
                }
            }
        }
    }

    /**
     *  Method to generate panels for all attributes avaialble in dag node
     */
    private void showAllAttributePanel() {
        //Logic : Initially create separate panel for each attributes 
        //check/update that attribute values with show  only condition panels   

        if (allAttributePanel == null)
            allAttributePanel = new Cab2bPanel();

        allAttributePanel.removeAll();
        if (onlyConditionAttributePanel != null) {
            //Add checked/unchecked conditions from the show Only condition panel
            TreeMap<Integer, AbstractTypePanel> panelMap = getAllConditionAttributePanels(onlyConditionAttributePanel);
            //first add only checked panels 
            for (Integer index : panelMap.keySet()) {
                if (panelMap.get(index).isAttributeCheckBoxSelected())
                    allAttributePanel.add("br ", panelMap.get(index));
            }
            //now add unchecked condition panels
            for (Integer index : panelMap.keySet()) {
                if (!panelMap.get(index).isAttributeCheckBoxSelected())
                    allAttributePanel.add("br ", panelMap.get(index));
            }
        }

        Map<IExpressionId, Collection<AttributeInterface>> allAttributes = queryDataModel.getAllAttributes();

        try {
            ParseXMLFile parseFile = ParseXMLFile.getInstance();
            AbstractTypePanel componentPanel = null;
            List<AttributeInterface> allConditionAttributeList = getAllConditionAttribute(allAttributePanel);
            
            for (IExpressionId exprId : allAttributes.keySet()) {
                for (AttributeInterface attribute : allAttributes.get(exprId)) {
                    if (!allConditionAttributeList.contains(attribute)) {
                        componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(parseFile, attribute,
                                                                                            maxLabelDimension);
                        componentPanel.createParametrizedPanel(attribute);
                        //setConditionValues(componentPanel);
                        componentPanel.setExpressionId(exprId);
                        componentPanel.setAttributeDisplayName(componentPanel.getAttributeDisplayName() + "_"
                                + exprId.getInt());
                        //check/update that attribute values with show  only condition panels
                        updatePanelStatus(componentPanel);                     
                        allAttributePanel.add("br ", componentPanel);
                    }
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
        contentPanel.add("br ", allAttributePanel);
    }

    /**
     * This Method checks whether the given attribute is associated with any condition 
     * and sets the condition values. 
     * @param attribute
     * @return
     */
    private void setConditionValues(AbstractTypePanel componentPanel) {

        AttributeInterface attribute = componentPanel.getAttributeEntity();
        Map<IExpressionId, Collection<ICondition>> conditionMap = queryDataModel.getConditions();
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
     * Method to return current condition panel  
     * @return
     */
    public Cab2bPanel getConditionPanel() {
        if (isShowAllAttribute)
            return allAttributePanel;
        else
            return onlyConditionAttributePanel;
    }

    /**
     * Method to generate panels for specified conditions defined in dag node  
     */
    private void showOnlyConditionPanel() {
        if (onlyConditionAttributePanel == null)
            onlyConditionAttributePanel = new Cab2bPanel();

        onlyConditionAttributePanel.removeAll();
        if (allAttributePanel != null) {
            //Add checked/unchecked conditions from the show all panel
            TreeMap<Integer, AbstractTypePanel> panelMap = getAllConditionAttributePanels(allAttributePanel);
            for (Integer index : panelMap.keySet()) {
                onlyConditionAttributePanel.add("br ", panelMap.get(index));
            }
        }

        Map<IExpressionId, Collection<ICondition>> conditionMap = queryDataModel.getConditions();
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (IExpressionId key : conditionMap.keySet()) {
            for (ICondition condition : conditionMap.get(key)) {
                attributeList.add(condition.getAttribute());
            }
        }
        try {
            ParseXMLFile parseFile = ParseXMLFile.getInstance();
            AbstractTypePanel componentPanel = null;
            List<AttributeInterface> allConditionAttributeList = getAllConditionAttribute(onlyConditionAttributePanel);
            for (IExpressionId key : conditionMap.keySet()) {
                for (ICondition condition : conditionMap.get(key)) {
                    if (!allConditionAttributeList.contains(condition.getAttribute())) {
                        componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                            parseFile,
                                                                                            condition.getAttribute(),
                                                                                            maxLabelDimension);
                        componentPanel.createParametrizedPanel(condition);
                        setConditionValues(componentPanel);
                        componentPanel.setAttributeDisplayName(componentPanel.getAttributeDisplayName() + "_"
                                + key.getInt());
                        onlyConditionAttributePanel.add("br ", componentPanel);
                    }
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
        contentPanel.add("br ", onlyConditionAttributePanel);
    }

    private List<AttributeInterface> getAllConditionAttribute(Container basePanel) {
        Map<Integer, AbstractTypePanel> allConditionPanel = getAllConditionAttributePanels(basePanel);
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (Integer index : allConditionPanel.keySet()) {
            attributeList.add(allConditionPanel.get(index).getAttributeEntity());
        }
        return attributeList;
    }

    /**
     * Method to create header panel
     * @return Cab2b headerPanel
     */
    public Cab2bPanel creatHeaderPanel() {
        headerPanel = new Cab2bPanel();
        filterComboBox = new Cab2bComboBox();
        filterComboBox.addItem("Only defined");
        filterComboBox.addItem("Show All");
        filterComboBox.addItemListener(new FilterComboBoxItemListener());
        filterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (isShowAllAttribute) {
                    showAllAttributePanel();
                } else {
                    showOnlyConditionPanel();
                }
            }
        });
        headerPanel.add("right ", new Cab2bLabel(" Filter : "));
        headerPanel.add(" right ", filterComboBox);
        return headerPanel;
    }

    /**
     *    method to create UI 
     */
    private void initGUI() {
        contentPanel = new Cab2bPanel();
        if (queryDataModel != null) {
            if (isShowAllAttribute) {
                showAllAttributePanel();
            } else {
                showOnlyConditionPanel();
            }
        }

        this.setTitle("Set Condition Parameters");
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.getContentContainer().add("hfill ", getHeaderPanel());
        this.getContentContainer().add("br ", scrollPane);
    }

    /**
     * @return the headerPanel
     */
    private Cab2bPanel getHeaderPanel() {
        if (headerPanel == null)
            headerPanel = creatHeaderPanel();

        return headerPanel;
    }

    /**
     * Method to which will return all checked condition/non condition panel
     * @return List<AbstractTypePanel> 
     */
    public List<AbstractTypePanel> getUnCheckedAttributePanels() {
        List<AbstractTypePanel> panelList = new ArrayList<AbstractTypePanel>();
        Cab2bPanel basePanel = null;

        if (isShowAllAttribute)
            basePanel = allAttributePanel;
        else
            basePanel = onlyConditionAttributePanel;

        for (int index = 0; index < basePanel.getComponentCount(); index++) {
            if (basePanel.getComponent(index) instanceof AbstractTypePanel) {
                AbstractTypePanel panel = (AbstractTypePanel) basePanel.getComponent(index);
                if (!panel.isAttributeCheckBoxSelected()) {
                    panelList.add(panel);
                }
            }
        }
        return panelList;
    }

    /**
     * Method to which will return all unchecked condition/non condition panel    
     * @return List<AbstractTypePanel> 
     */
    public List<AbstractTypePanel> getCheckedAttributePanels(Container basePanel) {
        List<AbstractTypePanel> panelList = new ArrayList<AbstractTypePanel>();

        for (int index = 0; index < basePanel.getComponentCount(); index++) {
            if (basePanel.getComponent(index) instanceof AbstractTypePanel) {
                AbstractTypePanel panel = (AbstractTypePanel) basePanel.getComponent(index);
                if (panel.isAttributeCheckBoxSelected()) {
                    panelList.add(panel);
                }
            }
        }
        return panelList;
    }

    public TreeMap<Integer, AbstractTypePanel> getAllConditionAttributePanels(Container basePanel) {
        TreeMap<Integer, AbstractTypePanel> panelMap = new TreeMap<Integer, AbstractTypePanel>();

        for (int index = 0; index < basePanel.getComponentCount(); index++) {
            if (basePanel.getComponent(index) instanceof AbstractTypePanel) {
                AbstractTypePanel panel = (AbstractTypePanel) basePanel.getComponent(index);
                String conditionString = panel.getConditionItem();
                if (panel.isAttributeCheckBoxSelected() || conditionString.equals("Is Null")
                        || conditionString.equals("Is Not Null") || panel.getValues().size() > 0) {
                    panelMap.put(index, panel);
                }
            }
        }
        return panelMap;
    }

    /**
     * FilterComboBox ItemListener class  
     * @author deepak_shingan
     *
     */
    private class FilterComboBoxItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent event) {
            contentPanel.removeAll();

            if (filterComboBox.getSelectedIndex() == 0) {
                isShowAllAttribute = false;
            } else {
                isShowAllAttribute = true;
            }
            updateUI();
        }
    }
}
