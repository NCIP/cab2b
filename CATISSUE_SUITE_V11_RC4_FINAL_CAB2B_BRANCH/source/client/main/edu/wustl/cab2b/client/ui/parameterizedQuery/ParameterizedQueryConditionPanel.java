/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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

/**
 * Class to generate Parameterized Query Condition GUI
 * 
 * @author deepak_shingan
 * 
 */
public class ParameterizedQueryConditionPanel extends Cab2bTitledPanel {
    private static final long serialVersionUID = 1L;

    private Cab2bPanel contentPanel;

    private Cab2bPanel allAttributePanel;

    private Cab2bPanel definedAttributePanel;

    private ParameterizedQueryDataModel queryDataModel;

    private boolean viewAllAttribute;

    private Cab2bPanel filterPanel;

    private Cab2bComboBox filterComboBox;

    private Dimension maxLabelDimension;

    //Used as buffer to remember the sequence of the conditions on preview panel
    private final List<AbstractTypePanel> panelIndexList = new ArrayList<AbstractTypePanel>();

    public ParameterizedQueryConditionPanel() {
        this(null, true);
    }

    public ParameterizedQueryConditionPanel(ParameterizedQueryDataModel queryDataModel, boolean viewAllAttribute) {
        super();
        this.queryDataModel = queryDataModel;
        this.viewAllAttribute = viewAllAttribute;
        maxLabelDimension = getMaximumDimensionForAttribute();
        initGUI();
    }

    /**
     * Method to get maximum dimension for specified attribute list
     * 
     * @param allAttributes
     * @return Dimension maxLabelDimension
     */
    private Dimension getMaximumDimensionForAttribute() {
        Map<Integer, Collection<AttributeInterface>> allAttributes = queryDataModel.getAllAttributes();
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (Integer exprId : allAttributes.keySet()) {
            Collection<AttributeInterface> attributeCollection = allAttributes.get(exprId);
            attributeList.addAll(attributeCollection);
        }
        Collections.sort(attributeList, new AttributeInterfaceComparator());
        maxLabelDimension = CommonUtils.getMaximumLabelDimension(attributeList);
        // Increasing max label width to display expression ID with each label
        maxLabelDimension.width = maxLabelDimension.width + 15;
        return maxLabelDimension;
    }

    private void updatePanelStatus(AbstractTypePanel componentPanel) {
        if (definedAttributePanel != null) {
            int panelCount = definedAttributePanel.getComponentCount();
            for (int index = 0; index < panelCount; index++) {
                if (definedAttributePanel.getComponent(index) instanceof AbstractTypePanel) {
                    AbstractTypePanel conditionPanel = (AbstractTypePanel) definedAttributePanel.getComponent(index);

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
     * Method to generate panels for all attributes avaialble in dag node
     */
    private void showAllAttributePanel() {
        // Initially create separate panel for each attributes
        // check/update that attribute values with show only condition panels
        if (allAttributePanel == null) {
            allAttributePanel = new Cab2bPanel();
        }
        allAttributePanel.removeAll();

        if (definedAttributePanel != null) {
            // Add checked/unchecked conditions from the show Only condition panel
            TreeMap<Integer, AbstractTypePanel> panelMap = getAllConditionAttributePanels(definedAttributePanel);

            // first add only checked panels
            for (Integer index : panelMap.keySet()) {
                if (panelMap.get(index).isAttributeCheckBoxSelected()) {
                    allAttributePanel.add("br ", panelMap.get(index));
                }
            }

            // now add unchecked condition panels
            for (Integer index : panelMap.keySet()) {
                if (!panelMap.get(index).isAttributeCheckBoxSelected()) {
                    allAttributePanel.add("br ", panelMap.get(index));
                }
            }
        }

        Map<Integer, Collection<AttributeInterface>> allAttributes = queryDataModel.getAllAttributes();
        try {
            ParseXMLFile parseFile = ParseXMLFile.getInstance();
            List<AttributeInterface> allConditionAttributeList = getAllConditionAttribute(allAttributePanel);
            for (Integer exprId : allAttributes.keySet()) {
                for (AttributeInterface attribute : allAttributes.get(exprId)) {
                    if (!allConditionAttributeList.contains(attribute)) {
                        AbstractTypePanel componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                                              parseFile,
                                                                                                              attribute,
                                                                                                              maxLabelDimension);
                        componentPanel.createParametrizedPanel(attribute);
                        // setConditionValues(componentPanel);
                        componentPanel.setExpressionId(exprId);
                        componentPanel.setAttributeDisplayName(exprId.intValue() + "."
                                + componentPanel.getAttributeDisplayName());
                        // check/update that attribute values with show only condition panels
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
     * This Method checks whether the given attribute is associated with any
     * condition and sets the condition values.
     * 
     * @param attribute
     * @return
     */
    private void setConditionValues(AbstractTypePanel componentPanel) {
        AttributeInterface attribute = componentPanel.getAttributeEntity();
        Map<Integer, Collection<ICondition>> conditionMap = queryDataModel.getConditions();
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
     * Method to return current condition panel
     * 
     * @return
     */
    public Cab2bPanel getConditionPanel() {
        if (viewAllAttribute)
            return allAttributePanel;
        else
            return definedAttributePanel;
    }

    /**
     * Method to generate panels for specified conditions defined in dag node
     */
    private void showOnlyConditionPanel() {
        if (definedAttributePanel == null)
            definedAttributePanel = new Cab2bPanel();

        definedAttributePanel.removeAll();
        // onlyConditionAttributePanel.add("br hfill", getLabelHeaderPanel());
        if (allAttributePanel != null) {
            // Add checked/unchecked conditions from the show all panel
            TreeMap<Integer, AbstractTypePanel> panelMap = getAllConditionAttributePanels(allAttributePanel);
            for (Integer index : panelMap.keySet()) {
                definedAttributePanel.add("br ", panelMap.get(index));
            }
        }

        Map<Integer, Collection<ICondition>> conditionMap = queryDataModel.getConditions();
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (Integer key : conditionMap.keySet()) {
            for (ICondition condition : conditionMap.get(key)) {
                attributeList.add(condition.getAttribute());
            }
        }
        try {
            ParseXMLFile parseFile = ParseXMLFile.getInstance();
            AbstractTypePanel componentPanel = null;
            List<AttributeInterface> allConditionAttributeList = getAllConditionAttribute(definedAttributePanel);
            for (Integer key : conditionMap.keySet()) {
                for (ICondition condition : conditionMap.get(key)) {
                    if (!allConditionAttributeList.contains(condition.getAttribute())) {
                        componentPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(
                                                                                            parseFile,
                                                                                            condition.getAttribute(),
                                                                                            maxLabelDimension);
                        componentPanel.createParametrizedPanel(condition);
                        setConditionValues(componentPanel);
                        componentPanel.setAttributeDisplayName(key.intValue() + "."
                                + componentPanel.getAttributeDisplayName());
                        definedAttributePanel.add("br ", componentPanel);
                    }
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
        contentPanel.add("br ", definedAttributePanel);
    }

    private List<AttributeInterface> getAllConditionAttribute(Container basePanel) {
        Map<Integer, AbstractTypePanel> allConditionPanel = getAllConditionAttributePanels(basePanel);
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();
        for (Integer index : allConditionPanel.keySet()) {
            attributeList.add(allConditionPanel.get(index).getAttributeEntity());
        }
        return attributeList;
    }

    private Cab2bPanel getLabelHeaderPanel() {
        Cab2bPanel labelHeaderPanel = new Cab2bPanel();
        labelHeaderPanel.setBackground(new Color(240, 240, 240));
        Cab2bLabel userDefinedLabel = new Cab2bLabel("User defined");
        Cab2bLabel displayLabel = new Cab2bLabel("Display label");
        Cab2bLabel attributesLabel = new Cab2bLabel("Attributes");
        Cab2bLabel conditionsLabel = new Cab2bLabel("Conditions");

        Font newBoldFont = new Font(userDefinedLabel.getFont().getName(), Font.BOLD,
                userDefinedLabel.getFont().getSize());
        userDefinedLabel.setFont(newBoldFont);
        displayLabel.setFont(newBoldFont);
        attributesLabel.setFont(newBoldFont);
        conditionsLabel.setFont(newBoldFont);

        Dimension labelDimension = new Dimension(maxLabelDimension.width + 10, maxLabelDimension.height + 10);
        userDefinedLabel.setPreferredSize(new Dimension(80, labelDimension.height));
        displayLabel.setPreferredSize(labelDimension);
        attributesLabel.setPreferredSize(labelDimension);
        conditionsLabel.setPreferredSize(labelDimension);

        labelHeaderPanel.add("br ", new Cab2bLabel());
        labelHeaderPanel.add("tab ", new Cab2bLabel());
        labelHeaderPanel.add("tab ", userDefinedLabel);
        labelHeaderPanel.add("tab ", displayLabel);
        labelHeaderPanel.add("tab ", attributesLabel);
        labelHeaderPanel.add("tab ", conditionsLabel);
        labelHeaderPanel.add("br ", new Cab2bLabel());
        return labelHeaderPanel;
    }

    private void populatePanelIndexList(final Cab2bPanel cab2bPanel) {
        panelIndexList.clear();
        for (int index = 0; index < cab2bPanel.getComponentCount(); index++) {
            panelIndexList.add((AbstractTypePanel) cab2bPanel.getComponent(index));
        }
    }

    /**
     * Method to create header panel
     * 
     * @return Cab2b filterPanel
     */
    public Cab2bPanel creatFilterPanel() {
        filterComboBox = new Cab2bComboBox();
        filterComboBox.addItem("Only defined");
        filterComboBox.addItem("Show All");
        filterComboBox.addItemListener(new FilterComboBoxItemListener());
        filterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (viewAllAttribute) {
                    showAllAttributePanel();
                    populatePanelIndexList(allAttributePanel);
                } else {
                    showOnlyConditionPanel();
                    populatePanelIndexList(definedAttributePanel);
                }
            }
        });

        filterPanel = new Cab2bPanel();
        filterPanel.add("right ", new Cab2bLabel(" Filter : "));
        filterPanel.add("right ", filterComboBox);
        filterPanel.add("tab", new Cab2bLabel());
        filterPanel.add("br ", new Cab2bLabel());
        return filterPanel;
    }

    /**
     * method to create UI
     */
    private void initGUI() {
        contentPanel = new Cab2bPanel();
        if (queryDataModel != null) {
            if (viewAllAttribute) {
                showAllAttributePanel();
            } else {
                showOnlyConditionPanel();
            }
        }

        this.setTitle("Set Condition Parameters");
        // contentPanel
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(contentPanel);
        scrollPane.getViewport().setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.getContentContainer().add("hfill ", getFilterPanel());
        this.getContentContainer().add("br hfill", getLabelHeaderPanel());
        this.getContentContainer().add("br hfill vfill", scrollPane);
    }

    /**
     * @return the filterPanel
     */
    private Cab2bPanel getFilterPanel() {
        if (filterPanel == null)
            filterPanel = creatFilterPanel();

        return filterPanel;
    }

    /**
     * Method to which will return all checked condition/non condition panel
     * 
     * @return List<AbstractTypePanel>
     */
    public List<AbstractTypePanel> getUnCheckedAttributePanels() {
        List<AbstractTypePanel> panelList = new ArrayList<AbstractTypePanel>();
        Cab2bPanel basePanel = null;

        if (viewAllAttribute)
            basePanel = allAttributePanel;
        else
            basePanel = definedAttributePanel;

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
     * 
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
     * 
     * @author deepak_shingan
     * 
     */
    private class FilterComboBoxItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent event) {
            contentPanel.removeAll();

            if (filterComboBox.getSelectedIndex() == 0) {
                viewAllAttribute = false;
            } else {
                viewAllAttribute = true;
            }
            updateUI();
        }
    }

    public void resetConditionIndices() {
        Cab2bPanel cab2bPanel = null;
        if (viewAllAttribute) {
            cab2bPanel = allAttributePanel;
        } else {
            cab2bPanel = definedAttributePanel;
        }
        cab2bPanel.removeAll();
        for (AbstractTypePanel absPanel : panelIndexList) {
            absPanel.add(absPanel.getAttributeDisplayNameTextField(), 1);
            absPanel.setAttributeCheckBox(false);
            cab2bPanel.add("br ", absPanel);
        }
    }
}
