package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.IExpressionId;

/**
 * An abstract class which provides the skeletal implementation of the
 * IComponent interface and defines some more abstract method like
 * getFirstComponent, getSecondComponent that needs to be implemented by the
 * subclasses like NumberTypePanel, StringTypePanel, etc. 
 * 
 * @author chetan_bh 
 * 
 */

/**
 * Added additional features/methods to make it generic for handaling Parameterized Query   
 * UI panels.
 * @author Deepak Shingan 
 * 
 */

public abstract class AbstractTypePanel extends Cab2bPanel implements IComponent {

    public static final int CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE = 9;

    /**
     * Label for displaying the attribute name.
     */
    protected Cab2bLabel m_Name;

    /**
     * ComboBox for displaying the conditions based on the data-type
     */
    protected Cab2bComboBox m_Conditions;

    /**
     * TextField for entering the alphanumeric text.
     */
    protected JComponent m_NameEdit;

    /**
     * Another TextField for entering the alphanumeric text.
     */
    protected JComponent m_OtherEdit;

    /**
     * Parsed xml file's data structure.
     */
    protected ArrayList<String> conditionList;

    /**
     * Entity representing attribute.
     */
    protected AttributeInterface attributeEntity;

    /**
     * Attribute name represented by the panel.
     */
    protected String attributeName;

    /**
     * 
     */
    protected Boolean showCondition;

    /**
     * Checkbox to select attribute in parameterized query
     */
    protected Cab2bCheckBox attributeCheckBox;

    /**
     * Text box to edit attribute name in parameterized query
     */
    protected Cab2bTextField attributeDisplayNameTextField;

    /**
     * flag to identify parameterized query
     */
    protected Boolean isParameterized;

    protected IExpressionId expressionId;

    protected String displayName;

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Dimension maxLabelDimension) {
        this(conditionList, attributeEntity, true, maxLabelDimension, false, "");
    }

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Dimension maxLabelDimension,
            Boolean isParameterized) {
        this(conditionList, attributeEntity, true, maxLabelDimension, isParameterized, "");
    }

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension) {
        this(conditionList, attributeEntity, true, maxLabelDimension, false, "");
    }

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension,
            String displayName) {
        this(conditionList, attributeEntity, true, maxLabelDimension, false, displayName);
    }

    public AbstractTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension,
            Boolean isParameterized,
            String displayName) {
        this.setLayout(new RiverLayout(10, 8));
        this.attributeEntity = attributeEntity;
        this.attributeName = attributeEntity.getName();
        this.showCondition = showCondition;
        this.isParameterized = isParameterized;
        this.conditionList = conditionList;
        this.displayName = displayName;
        initGUI(maxLabelDimension);
    }

    private void initGUI(Dimension maxLabelDimension) {
        this.setLayout(new RiverLayout(10, 8));

        if (displayName == null || displayName.equals("")) {
            if (!Utility.isCategory(attributeEntity.getEntity()))
                displayName = CommonUtils.getFormattedString(attributeEntity.getName());
            else
                displayName = attributeName;
        }
        m_Name = new Cab2bLabel(displayName + " : ");

        m_Name.setPreferredSize(maxLabelDimension);// new Dimension(235,20)
        String toolTipText = edu.wustl.cab2b.client.ui.query.Utility.getAttributeCDEDetails(attributeEntity);
        m_Name.setToolTipText(toolTipText);
        m_NameEdit = getFirstComponent();

        m_OtherEdit = getSecondComponent();
        m_OtherEdit.setEnabled(false);
        m_OtherEdit.setVisible(false);
        m_OtherEdit.setOpaque(false);

        final Border border = m_OtherEdit.getBorder();
        final EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);
        m_OtherEdit.setBorder(emptyBorder);

        if (isParameterized) {

            attributeCheckBox = new Cab2bCheckBox();

            attributeDisplayNameTextField = new Cab2bTextField(displayName, new Dimension(maxLabelDimension.width,
                    maxLabelDimension.height + 5));
            attributeDisplayNameTextField.setEnabled(false);
            attributeCheckBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    if (attributeCheckBox.isSelected())
                        attributeDisplayNameTextField.setEnabled(true);
                    else
                        attributeDisplayNameTextField.setEnabled(false);
                }
            });

            add(attributeCheckBox);
            add(attributeDisplayNameTextField);
        }

        add(m_Name);
        if (showCondition) {
            setCondtionControl(conditionList, border, emptyBorder);
        }
        add(m_NameEdit);
        add(m_OtherEdit);
        setSize(new Dimension(300, 100));
    }

    public void setAttributeCheckBox(boolean selectCheckBox) {
        attributeCheckBox.setSelected(selectCheckBox);
    }

    public boolean isAttributeCheckBoxSelected() {
        if (attributeCheckBox != null)
            return attributeCheckBox.isSelected();
        return false;
    }

    /**
     * @return AttributeInterface
     */
    public AttributeInterface getAttributeEntity() {
        return attributeEntity;
    }

    public String getCondition() {
        return (String) m_Conditions.getSelectedItem();
    }

    public abstract JComponent getFirstComponent();

    public abstract JComponent getSecondComponent();

    public void setCondition(String str) {
        int itemCount = m_Conditions.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (m_Conditions.getItemAt(i).toString().compareToIgnoreCase(str) == 0) {
                m_Conditions.setSelectedIndex(i);
            }
        }
    }

    public abstract void setComponentPreference(String condition);

    public abstract void resetPanel();

    public String getAttributeName() {
        return attributeName;
    }

    private void setCondtionControl(ArrayList<String> conditionList, final Border border,
                                    final EmptyBorder emptyBorder) {
        this.conditionList = conditionList;

        /*
         * Initializing conditions can't be abstracted, since it varies from
         * string type to number to date
         */
        m_Conditions = new Cab2bComboBox();
        // m_Conditions.setBorder(ClientConstants.border);
        m_Conditions.setPreferredSize(new Dimension(125, 20));
        Collections.sort(conditionList);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < conditionList.size(); i++) {
            model.addElement(conditionList.get(i));
        }

        m_Conditions.setModel(model);
        m_Conditions.setMaximumRowCount(conditionList.size());

        m_Conditions.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                conditionListenerAction(border, emptyBorder);
            }
        });

        m_Conditions.setSelectedIndex(0);
        add(m_Conditions);
    }

    private void conditionListenerAction(final Border border, final EmptyBorder emptyBorder) {
        if (m_Conditions.getSelectedItem().equals("Between")) {
            setNameEdit(true, border);
            setOtherEdit(true, border);
        } else if ((m_Conditions.getSelectedItem().equals("Is Null"))
                || (m_Conditions.getSelectedItem().equals("Is Not Null"))) {
            setNameEdit(false, emptyBorder);
            setOtherEdit(false, emptyBorder);

            ArrayList<String> values = new ArrayList<String>();
            values.add("");
            values.add("");
            setValues(values);
        } else {
            setNameEdit(true, border);

            // If previously selected condition was 'Between' then clear the
            // second text box
            ArrayList<String> oldValues = getValues();
            if (m_OtherEdit.isEnabled() && oldValues.size() == 2) {
                ArrayList<String> values = new ArrayList<String>();
                values.add(oldValues.get(0));
                values.add("");
                setValues(values);
            } else {
                setValues(oldValues);
            }
            setOtherEdit(false, emptyBorder);
        }
        setComponentPreference(getCondition());
    }

    private void setNameEdit(boolean value, Border border) {
        m_NameEdit.setOpaque(value);
        m_NameEdit.setEnabled(value);
        m_NameEdit.setVisible(value);
        m_NameEdit.setBorder(border);
    }

    private void setOtherEdit(boolean value, Border border) {
        m_OtherEdit.setOpaque(value);
        m_OtherEdit.setEnabled(value);
        m_OtherEdit.setVisible(value);
        m_OtherEdit.setBorder(border);
    }

    /**
     * @return the expressionId
     */
    public IExpressionId getExpressionId() {
        return expressionId;
    }

    /**
     * @param expressionId the expressionId to set
     */
    public void setExpressionId(IExpressionId expressionId) {
        this.expressionId = expressionId;
    }

    /**
     * @return 
     */
    public String getAttributeDisplayName() {
        return displayName;
    }

    /**
     * @return the m_Name
     */
    public void setAttributeDisplayName(String displayName) {
        this.displayName = displayName;
        m_Name.setText(displayName + " : ");
    }

    /**
     * @return the attributeDisplayNameTextField
     */
    public Cab2bTextField getAttributeDisplayNameTextField() {
        return attributeDisplayNameTextField;
    }
}
