package edu.wustl.cab2b.client.ui.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryShowResultPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IParameterizedCondition;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Condition;
import edu.wustl.common.querysuite.queryobject.impl.ParameterizedCondition;

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
    protected Cab2bLabel nameLabel;

    /**
     * ComboBox for displaying the conditions based on the data-type
     */
    protected Cab2bComboBox conditionComboBox;

    /**
     * TextField for entering the alphanumeric text.
     */
    protected JComponent firstComponent;

    /**
     * Another TextField for entering the alphanumeric text.
     */
    protected JComponent secondComponent;

    /**
     * Parsed xml file's data structure.
     */
    protected ArrayList<String> conditionList;

    /**
     * Entity representing attribute.
     */
    protected AttributeInterface attribute;

    /**
     * Checkbox to select attribute in parameterized query
     */
    protected Cab2bCheckBox attributeCheckBox;

    /**
     * Text box to edit attribute name in parameterized query
     */
    protected Cab2bTextField attributeDisplayNameTextField;

    protected IExpressionId expressionId;

    protected String displayName = null;

    protected Dimension maxLabelDimension;

    protected abstract JComponent getFirstComponent();

    protected abstract JComponent getSecondComponent();

    protected abstract void setComponentPreference(String condition);

    public abstract void resetPanel();

    public AbstractTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        this.setLayout(new RiverLayout(10, 8));
        this.conditionList = conditionList;
        this.maxLabelDimension = maxLabelDimension;
    }

    public void createSimplePanel(AttributeInterface attribute) {
        this.attribute = attribute;
        if (displayName == null) {
            if (!Utility.isCategory(attribute.getEntity()))
                displayName = CommonUtils.getFormattedString(attribute.getName());
            else
                displayName = attribute.getName();
        }

        nameLabel = new Cab2bLabel(displayName + " : ");
        nameLabel.setPreferredSize(maxLabelDimension);
        String toolTipText = edu.wustl.cab2b.client.ui.query.Utility.getAttributeCDEDetails(attribute);
        nameLabel.setToolTipText(toolTipText);
        firstComponent = getFirstComponent();

        secondComponent = getSecondComponent();
        secondComponent.setEnabled(false);
        secondComponent.setVisible(false);
        secondComponent.setOpaque(false);

        final EmptyBorder emptyBorder = new EmptyBorder(2, 2, 2, 2);
        secondComponent.setBorder(emptyBorder);
        add(nameLabel, 0);
        add(firstComponent, 1);
        add(secondComponent, 2);
        setSize(new Dimension(300, 100));
    }

    public void createPanelWithOperator(AttributeInterface attribute) {
        createSimplePanel(attribute);
        setCondtionControl(conditionList, getSecondComponent().getBorder(), new EmptyBorder(2, 2, 2, 2));
        add(conditionComboBox, 1);
    }

    public void createPanelWithOperator(ICondition condition) {
        if (condition instanceof IParameterizedCondition) {
            displayName = ((IParameterizedCondition) condition).getName();
        }
        createPanelWithOperator(condition.getAttribute());
        setValues(new ArrayList<String>(condition.getValues()));
        setCondition(condition.getRelationalOperator().getStringRepresentation());
    }

    public void createParametrizedPanel(AttributeInterface attribute) {
        createPanelWithOperator(attribute);
        attributeCheckBox = new Cab2bCheckBox();
        attributeCheckBox.setPreferredSize(new Dimension(80, maxLabelDimension.height));
        attributeDisplayNameTextField = new Cab2bTextField(displayName, new Dimension(maxLabelDimension.width,
                maxLabelDimension.height + 5));
        attributeDisplayNameTextField.setEnabled(false);
        attributeCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                setAttributeCheckBox(attributeCheckBox.isSelected());
            }
        });
        add(attributeCheckBox, 0);
        add(attributeDisplayNameTextField, 1);
    }

    public void createParametrizedPanel(ICondition condition) {
        if (condition instanceof IParameterizedCondition) {
            displayName = ((IParameterizedCondition) condition).getName();
        }
        createParametrizedPanel(condition.getAttribute());
        setValues(new ArrayList<String>(condition.getValues()));
        setCondition(condition.getRelationalOperator().getStringRepresentation());
    }

    public void setAttributeCheckBox(boolean selectCheckBox) {
        attributeCheckBox.setSelected(selectCheckBox);
        if (attributeDisplayNameTextField != null) {
            attributeDisplayNameTextField.setEnabled(selectCheckBox);
        }
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
        return attribute;
    }

    public String getConditionItem() {
        return (String) conditionComboBox.getSelectedItem();
    }

    public void setCondition(String str) {
        int itemCount = conditionComboBox.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (conditionComboBox.getItemAt(i).toString().compareToIgnoreCase(str) == 0) {
                conditionComboBox.setSelectedIndex(i);
            }
        }
    }

    private void setCondtionControl(ArrayList<String> conditionList, final Border border,
                                    final EmptyBorder emptyBorder) {
        this.conditionList = conditionList;
        /*
         * Initializing conditions can't be abstracted, since it varies from
         * string type to number to date
         */
        conditionComboBox = new Cab2bComboBox();
        conditionComboBox.setPreferredSize(new Dimension(125, 20));
        Collections.sort(conditionList);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < conditionList.size(); i++) {
            model.addElement(conditionList.get(i));
        }

        conditionComboBox.setModel(model);
        conditionComboBox.setMaximumRowCount(conditionList.size());

        conditionComboBox.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                conditionListenerAction(border, emptyBorder);
            }
        });

        conditionComboBox.setSelectedIndex(0);
    }

    private void conditionListenerAction(final Border border, final EmptyBorder emptyBorder) {
        if (conditionComboBox.getSelectedItem().equals("Between")) {
            setNameEdit(true, border);
            setOtherEdit(true, border);
        } else if ((conditionComboBox.getSelectedItem().equals("Is Null"))
                || (conditionComboBox.getSelectedItem().equals("Is Not Null"))) {
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
            if (secondComponent.isEnabled() && oldValues.size() == 2) {
                ArrayList<String> values = new ArrayList<String>();
                values.add(oldValues.get(0));
                values.add("");
                setValues(values);
            } else {
                setValues(oldValues);
            }
            setOtherEdit(false, emptyBorder);
        }
        setComponentPreference(getConditionItem());
    }

    private void setNameEdit(boolean value, Border border) {
        firstComponent.setOpaque(value);
        firstComponent.setEnabled(value);
        firstComponent.setVisible(value);
        firstComponent.setBorder(border);
    }

    private void setOtherEdit(boolean value, Border border) {
        secondComponent.setOpaque(value);
        secondComponent.setEnabled(value);
        secondComponent.setVisible(value);
        secondComponent.setBorder(border);
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
        if (attributeDisplayNameTextField != null)
            displayName = attributeDisplayNameTextField.getText().trim();
        return displayName;
    }

    /**
     * @return the nameLabel
     */
    public void setAttributeDisplayName(String displayName) {
        this.displayName = displayName;
        nameLabel.setText(displayName + " : ");
        if (attributeDisplayNameTextField != null) {
            attributeDisplayNameTextField.setText(displayName);
        }
    }

    /**
     * @return the attributeDisplayNameTextField
     */
    public Cab2bTextField getAttributeDisplayNameTextField() {
        if (attributeDisplayNameTextField == null) {
            attributeDisplayNameTextField = new Cab2bTextField(displayName, new Dimension(maxLabelDimension.width,
                    maxLabelDimension.height + 5));
        }
        return attributeDisplayNameTextField;
    }

    /**
     * returns valid condition from panel otherwise null
     * @param index
     * @return
     */
    public ICondition getCondition(int index, Cab2bPanel parentPanel) {
        String conditionString = getConditionItem();
        ArrayList<String> conditionValues = getValues();
        RelationalOperator operator = RelationalOperator.getOperatorForStringRepresentation(conditionString);

        int conditionStatus = isConditionValid(this);
        if (conditionStatus == 0) {
            if (isAttributeCheckBoxSelected() || (parentPanel instanceof ParameterizedQueryShowResultPanel)) {
                //make a new parameterized condition 
                return new ParameterizedCondition(attribute, operator, conditionValues, index,
                        getAttributeDisplayName());
            } else {
                return new Condition(attribute, operator, conditionValues);
            }
        }
        return null;
    }

    /**
     * Method to check validity of condition 
     * @param parentPanel
     * @return
     */
    public int isConditionValid(Container parentPanel) {
        String conditionString = getConditionItem();
        ArrayList<String> conditionValues = getValues();

        if (conditionString.compareToIgnoreCase("Between") == 0 && (conditionValues.size() == 1)) {
            JOptionPane.showMessageDialog(parentPanel, "Please enter both the values for between operator.",
                                          "Error", JOptionPane.ERROR_MESSAGE);

            return -1;
        }

        if (isAttributeCheckBoxSelected() && conditionValues.size() == 0
                && !(conditionString.equals("Is Null") || conditionString.equals("Is Not Null"))) {
            JOptionPane.showMessageDialog(parentPanel,
                                          "Please enter the values for selected field or remove the selection. \n Field name : "
                                                  + getAttributeDisplayName(), "Error", JOptionPane.ERROR_MESSAGE);

            return -1;
        }

        if (((conditionString.equals("Is Null")) || conditionString.equals("Is Not Null") || (conditionValues.size() != 0))) {
            return 0;
        }
        return 1;
    }
}
