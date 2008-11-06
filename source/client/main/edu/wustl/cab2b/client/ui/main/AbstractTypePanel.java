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
import edu.wustl.cab2b.client.ui.controls.Cab2bCheckBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bFormattedTextField;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryNavigationPanel;
import edu.wustl.cab2b.client.ui.parameterizedQuery.ParameterizedQueryShowResultPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IParameter;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Condition;

/**
 * An abstract class which provides the skeletal implementation of the
 * IComponent interface and defines some more abstract method like
 * getFirstComponent, getSecondComponent that needs to be implemented by the
 * subclasses like NumberTypePanel, StringTypePanel, etc. It also has methods to
 * make it generic for handling parameterized Query UI panels.
 * 
 * @author Deepak Shingan
 */

public abstract class AbstractTypePanel extends Cab2bPanel implements IComponent {

    /** Text Field Column Size */
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

    /**
     * ExpressionID
     */
    protected int expressionId;

    /**
     * New edited attribute name for parameterized condition 
     */
    protected String displayName = null;

    /**
     * Returns max label dimension 
     */
    protected Dimension maxLabelDimension;

    /**
     * Returns component used to set first value for the condition
     * @return JComponent
     */
    protected abstract JComponent getFirstComponent();

    /**
     * Returns component used to set second value for the condition  
     * @return JComponent 
     */
    protected abstract JComponent getSecondComponent();

    /**
     * @param condition String
     */
    protected abstract void setComponentPreference(String condition);

    /**
     * Method to reset values and conditions 
     */
    public abstract void resetPanel();

    /**
     * Constructor
     * @param conditionList
     * @param maxLabelDimension
     */
    public AbstractTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        this.setLayout(new RiverLayout(10, 8));
        this.conditionList = conditionList;
        this.maxLabelDimension = maxLabelDimension;
    }

    /**
     * Create panel with only Attribute name and component for values   
     * @param attribute
     */
    public void createSimplePanel(AttributeInterface attribute) {
        this.attribute = attribute;
        if (displayName == null) {
            if (!Utility.isCategory(attribute.getEntity()))
                displayName = Utility.getFormattedString(attribute.getName());
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

    /** 
     * This method creates Abstarct type panel with operator using AttributeInterface
     * @param attribute
     */
    public void createPanelWithOperator(AttributeInterface attribute) {
        createSimplePanel(attribute);
        setCondtionControl(conditionList, getSecondComponent().getBorder(), new EmptyBorder(2, 2, 2, 2));
        add(conditionComboBox, 1);
    }

    /**
     * This method creates Abstarct type panel with operator using ICondition
     * @param condition
     */
    public void createPanelWithOperator(ICondition condition) {
        createPanelWithOperator(condition.getAttribute());
        setValues(new ArrayList<String>(condition.getValues()));
        setCondition(condition.getRelationalOperator().getStringRepresentation());
    }

    /**
     * Method to create parameterized panel having checkbox and other components
     * @param attribute
     */
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

    /**
     * Method to create parameterized condition panel using ICondition
     * @param condition
     */
    public void createParametrizedPanel(ICondition condition) {

        createParametrizedPanel(condition.getAttribute());
        setValues(new ArrayList<String>(condition.getValues()));
        setCondition(condition.getRelationalOperator().getStringRepresentation());
    }

    /**
     * Sets checkbox in panel 
     * @param selectCheckBox
     */
    public void setAttributeCheckBox(boolean selectCheckBox) {
        attributeCheckBox.setSelected(selectCheckBox);
        if (attributeDisplayNameTextField != null) {
            attributeDisplayNameTextField.setEnabled(selectCheckBox);
        }
    }

    /**
     * Returns true if checkbox is selected else false 
     * @return
     */
    public boolean isAttributeCheckBoxSelected() {
        if (attributeCheckBox != null)
            return attributeCheckBox.isSelected();
        return false;
    }

    /**
     * Returns attribute
     * @return AttributeInterface
     */
    public AttributeInterface getAttributeEntity() {
        return attribute;
    }

    /**
     * @see edu.wustl.cab2b.client.ui.main.IComponent#getConditionItem()
     * @return String 
     */
    public String getConditionItem() {
        return (String) conditionComboBox.getSelectedItem();
    }

    /**
     * @see edu.wustl.cab2b.client.ui.main.IComponent#setCondition(java.lang.String)
     * @param str
     */
    public void setCondition(String str) {
        int itemCount = conditionComboBox.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (conditionComboBox.getItemAt(i).toString().compareToIgnoreCase(str) == 0) {
                conditionComboBox.setSelectedIndex(i);
            }
        }
    }

    /**
     * Set values only if selected condition is "IN" or "Not IN"
     * @param values
     */
    protected void setMultipleValues(ArrayList<String> values) {
        //If selected condition is "IN" or "Not IN"
        if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem().compareToIgnoreCase("Not IN") == 0)) {
            StringBuffer sb = new StringBuffer();

            // For first element append text qualifier if required
            if (values.get(0).indexOf(",") != -1) {
                sb.append("\"");
                sb.append(values.get(0));
                sb.append("\"");
            } else {
                sb.append(values.get(0));
            }

            for (int i = 1; i < values.size(); i++) {
                sb.append(",");
                if (values.get(i).indexOf(",") != -1) {
                    sb.append("\"");
                    sb.append(values.get(i));
                    sb.append("\"");
                } else {
                    sb.append(values.get(i));
                }
            }
            ((Cab2bFormattedTextField) firstComponent).setText(sb.toString());
        }
    }

    /**
     * @param conditionList
     * @param border
     * @param emptyBorder
     */
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

    /**
     * @param border
     * @param emptyBorder
     */
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

    /**
     * @param value
     * @param border
     */
    private void setNameEdit(boolean value, Border border) {
        firstComponent.setOpaque(value);
        firstComponent.setEnabled(value);
        firstComponent.setVisible(value);
        firstComponent.setBorder(border);
    }

    /**
     * @param value
     * @param border
     */
    private void setOtherEdit(boolean value, Border border) {
        secondComponent.setOpaque(value);
        secondComponent.setEnabled(value);
        secondComponent.setVisible(value);
        secondComponent.setBorder(border);
    }

    /**
     * @return the expressionId
     */
    public int getExpressionId() {
        return expressionId;
    }

    /**
     * @param expressionId
     *            the expressionId to set
     */
    public void setExpressionId(int expressionId) {
        this.expressionId = expressionId;
    }

    /**
     * Returns attribute edited name
     * @return Attribute edited name
     */
    public String getAttributeDisplayName() {
        if (attributeDisplayNameTextField != null)
            displayName = attributeDisplayNameTextField.getText().trim();
        return displayName;
    }

    /**
     * Sets the Display Name for corresponding Attribute
     * @param displayName
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
     * Returns valid condition from panel otherwise null
     * @param parentPanel
     * @param index
     * @return
     */
    public ICondition getCondition(int index, Cab2bPanel parentPanel) {
        String conditionString = getConditionItem();
        ArrayList<String> conditionValues = getValues();
        RelationalOperator operator = RelationalOperator.getOperatorForStringRepresentation(conditionString);

        ICondition condition = new Condition(attribute, operator, conditionValues);

        IParameterizedQuery paramQuery = null;

        boolean isValid = false;
        if (parentPanel instanceof ParameterizedQueryShowResultPanel) {
            ParameterizedQueryShowResultPanel paramQueryShowResultPanel = (ParameterizedQueryShowResultPanel) parentPanel;
            paramQuery = paramQueryShowResultPanel.getQueryDataModel().getQuery();
            isValid = true;
        } else if (parentPanel instanceof ParameterizedQueryNavigationPanel) {
            ParameterizedQueryNavigationPanel paramQueryNavigationPanel = (ParameterizedQueryNavigationPanel) parentPanel;
            paramQuery = paramQueryNavigationPanel.getQuery();
            if (isAttributeCheckBoxSelected()) {
                isValid = true;
            }
        }

        if (isValid) {
            // make a new parameterized condition
            IParameter<?> parameter = QueryObjectFactory.createParameter(condition,
                                                                         condition.getAttribute().getName());
            paramQuery.getParameters().add(parameter);
        }
        return condition;
    }

    /**
     * Method to check validity of condition before saving. -1: Error, 0: Valid,
     * 1: Remove
     * 
     * @param parentPanel
     * @return
     */
    public int isConditionValidBeforeSaving(Container parentPanel) {
        String conditionString = getConditionItem();
        ArrayList<String> conditionValues = getValues();

        if (!isAttributeCheckBoxSelected() && conditionString.compareToIgnoreCase("Between") == 0
                && (conditionValues.size() == 1)) {
            JOptionPane.showMessageDialog(parentPanel, "Please enter both the values for between operator.",
                                          "Error", JOptionPane.ERROR_MESSAGE);

            return -1;
        }
        if (isAttributeCheckBoxSelected()) {
            return 0;
        } else if ((conditionValues.size() != 0)
                || ((conditionString.equals("Is Null")) || conditionString.equals("Is Not Null"))) {
            return 0;
        } else {
            /*
             * If the attribute check box is not selected and there are also no
             * values specified for that condition, then that condition is
             * eligible for removal from the query. To identify such conditions,
             * the code 1 is returned.
             */
            return 1;
        }
    }

    /**
     * Method to check validity of conditions
     * 
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

    /**
     * @see edu.wustl.cab2b.client.ui.main.IComponent#getValues()
     * @return ArrayList
     */
    public ArrayList<String> getValues() {

        ArrayList<String> values = new ArrayList<String>();
        String nameString = ((Cab2bFormattedTextField) firstComponent).getText();
        if (nameString.length() != 0) {
            if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem().compareToIgnoreCase(
                                                                                                             "Not IN") == 0)) {
                ArrayList<String> strings = CommonUtils.splitStringWithTextQualifier(nameString, '"', ',');
                for (int i = 0; i < strings.size(); i++) {
                    values.add(strings.get(i));
                }
            } else {
                values.add(((Cab2bFormattedTextField) firstComponent).getText());
            }

            if (((Cab2bFormattedTextField) secondComponent).getText().length() != 0) {
                values.add(((Cab2bFormattedTextField) secondComponent).getText());
            }

        } else if (((Cab2bFormattedTextField) secondComponent).getText().length() != 0) {
            values.add(((Cab2bFormattedTextField) secondComponent).getText());
        }
        return values;

    }
}
