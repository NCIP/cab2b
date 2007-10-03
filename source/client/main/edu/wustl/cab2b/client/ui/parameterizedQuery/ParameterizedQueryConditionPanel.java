/**
 * 
 */
package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlinkUI;
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
import edu.wustl.common.querysuite.queryobject.IQueryEntity;

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

    private Dimension maxLabelDimension;

    private ParameterizedQueryDataModel queryDataModel;

    private boolean isShowAllAttribute;

    private Cab2bPanel headerPanel;

    private Cab2bHyperlink showSelectedConditionHyperlink;

    private Cab2bHyperlink showAllHyperlink;

    public ParameterizedQueryConditionPanel() {
        this(null, true);
    }

    public ParameterizedQueryConditionPanel(ParameterizedQueryDataModel queryDataModel, boolean isShowAllAttribute) {
        this.queryDataModel = queryDataModel;
        this.isShowAllAttribute = isShowAllAttribute;
        initGUI();
    }

    private void showAllAttributePanel() {
        for (IQueryEntity entityQ : queryDataModel.getQueryEntities()) {
            EntityInterface entity = entityQ.getDynamicExtensionsEntity();
            final Collection<AttributeInterface> attributeCollection = entity.getAttributeCollection();

            if (attributeCollection != null) {
                try {
                    List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(attributeCollection);
                    Collections.sort(attributeList, new AttributeInterfaceComparator());
                    ParseXMLFile parseFile = ParseXMLFile.getInstance();
                    Dimension maxDimension = CommonUtils.getMaximumLabelDimension(attributeList);
                    if (maxLabelDimension == null || maxLabelDimension.width < maxDimension.width) {
                        maxLabelDimension = maxDimension;
                    }
                    AbstractTypePanel componentPanel = null;
                    for (AttributeInterface attribute : attributeList) {
                        componentPanel = (AbstractTypePanel) SwingUIManager.generateParameterizedUIPanel(
                                                                                                         parseFile,
                                                                                                         attribute,
                                                                                                         true,
                                                                                                         maxLabelDimension,
                                                                                                         true, "");
                        setConditionValues(componentPanel);
                        contentPanel.add("br ", componentPanel);
                    }
                } catch (CheckedException checkedException) {
                    CommonUtils.handleException(checkedException, this, true, true, false, false);
                }
            }
        }
    }

    /**
     * This Method checks whether the given attribute is associated with any condition 
     * and sets the condition values. 
     * @param attribute
     * @return
     */
    private ICondition setConditionValues(AbstractTypePanel componentPanel) {

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
        return null;
    }

    public Cab2bPanel getConditionPanel() {
        return contentPanel;
    }

    private void showOnlyConditionPanel() {
        Map<IExpressionId, Collection<ICondition>> conditionMap = queryDataModel.getConditions();
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

        try {
            ParseXMLFile parseFile = ParseXMLFile.getInstance();
            AbstractTypePanel componentPanel = null;
            for (IExpressionId key : conditionMap.keySet()) {
                for (ICondition condition : conditionMap.get(key)) {
                    componentPanel = (AbstractTypePanel) SwingUIManager.generateParameterizedUIPanel(
                                                                                                     parseFile,
                                                                                                     condition.getAttribute(),
                                                                                                     true,
                                                                                                     maxLabelDimension,
                                                                                                     true, "");
                    setConditionValues(componentPanel);
                    contentPanel.add("br ", componentPanel);
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, true, false, false);
        }
    }

    public Cab2bPanel creatHeaderPanel() {
        headerPanel = new Cab2bPanel();
        showSelectedConditionHyperlink = new Cab2bHyperlink();
        showSelectedConditionHyperlink.setText("Set Selected Conditions");
        showSelectedConditionHyperlink.setEnabled(false);
        showSelectedConditionHyperlink.addActionListener(new ShowOnlyConditionHyperlinkActionListener());

        showAllHyperlink = new Cab2bHyperlink();
        showAllHyperlink.setText("Show All");
        showAllHyperlink.setEnabled(true);
        showAllHyperlink.addActionListener(new ShowAllHyperlinkActionListener());

        headerPanel.add(showSelectedConditionHyperlink);
        headerPanel.add("tab ", showAllHyperlink);
        return headerPanel;
    }

    /**
     *    
     */
    private void initGUI() {
        contentPanel = new Cab2bPanel();
        contentPanel.add(getHeaderPanel());
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
        this.add(scrollPane);
    }

    /**
     * @return the headerPanel
     */
    private Cab2bPanel getHeaderPanel() {
        if (headerPanel == null)
            headerPanel = creatHeaderPanel();

        return headerPanel;
    }

    public List<AbstractTypePanel> getUnCheckedAttributePanels() {
        List<AbstractTypePanel> panelList = new ArrayList<AbstractTypePanel>();

        for (int index = 0; index < contentPanel.getComponentCount(); index++) {
            if (contentPanel.getComponent(index) instanceof AbstractTypePanel) {
                AbstractTypePanel panel = (AbstractTypePanel) contentPanel.getComponent(index);
                if (!panel.isAttributeCheckBoxSelected()) {
                    panelList.add(panel);
                }
            }
        }
        return panelList;
    }

    public List<AbstractTypePanel> getCheckedAttributePanels() {
        List<AbstractTypePanel> panelList = new ArrayList<AbstractTypePanel>();

        for (int index = 0; index < contentPanel.getComponentCount(); index++) {
            if (contentPanel.getComponent(index) instanceof AbstractTypePanel) {
                AbstractTypePanel panel = (AbstractTypePanel) contentPanel.getComponent(index);
                if (panel.isAttributeCheckBoxSelected()) {
                    panelList.add(panel);
                }
            }
        }
        return panelList;
    }

    private class ShowOnlyConditionHyperlinkActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            isShowAllAttribute = false;
            showSelectedConditionHyperlink.setEnabled(false);
            showAllHyperlink.setEnabled(true);
            contentPanel.removeAll();
            contentPanel.add(getHeaderPanel());
            showOnlyConditionPanel();
            updateUI();
        }
    }

    private class ShowAllHyperlinkActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            isShowAllAttribute = true;
            showAllHyperlink.setEnabled(false);
            showSelectedConditionHyperlink.setEnabled(true);
            contentPanel.removeAll();
            contentPanel.add(getHeaderPanel());
            showAllAttributePanel();
            updateUI();
        }
    }
}
