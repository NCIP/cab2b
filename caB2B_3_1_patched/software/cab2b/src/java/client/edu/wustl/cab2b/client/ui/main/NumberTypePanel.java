package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bFormattedTextField;

/**
 * Condition panel if attribute type is of type NUMBER. Used in AddLimit and ParameterizedQuery pages
 * @author deepak_shingan
 *
 */
public class NumberTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * sets condition and max condition label length
     * @param conditionList
     * @param maxLabelDimension
     */
    public NumberTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    /**
     * @return JComponent the first component in Number Panel
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getFirstComponent()
     */
    public JComponent getFirstComponent() {
        return getProperFormattedTextField();
    }

    /**
     * @return JComponent the second component in Number Panel
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getSecondComponent()
     */
    public JComponent getSecondComponent() {
        return getProperFormattedTextField();
    }

    /**
     * Sets the values for Number Panel
     * @param values
     * @see edu.wustl.cab2b.client.ui.main.IComponent#setValues(java.util.ArrayList)
     */
    public void setValues(ArrayList<String> values) {
        if (values.size() == 0) {
            return;
        }

        // If selected condition is "IN" or "Not IN"
        if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem().compareToIgnoreCase("Not IN") == 0)) {
            super.setMultipleValues(values);
            ((Cab2bFormattedTextField) firstComponent).setCommaAllowed(true);
        } else if (values.size() == 1) {
            ((Cab2bFormattedTextField) firstComponent).setCommaAllowed(false);
            String value = values.get(0);
            int index = -1;
            if ((index = value.indexOf(',')) != -1) {
                value = value.substring(0, index);
            }
            ((Cab2bFormattedTextField) firstComponent).setText(value);
        } else if (values.size() == 2) {
            ((Cab2bFormattedTextField) firstComponent).setCommaAllowed(false);
            String value = values.get(0);
            int index = -1;
            if ((index = value.indexOf(',')) != -1) {
                value = value.substring(0, index);
            }
            ((Cab2bFormattedTextField) firstComponent).setText(value);
            ((Cab2bFormattedTextField) secondComponent).setText(values.get(1));
        }

    }

    /**
     * Returns specific text field according to selected condition 
     * @return
     */
    private Cab2bFormattedTextField getProperFormattedTextField() {
        Cab2bFormattedTextField formattedTextField = null;
        AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();

        if (attributeTypeInformation instanceof IntegerAttributeTypeInformation
                || attributeTypeInformation instanceof LongAttributeTypeInformation
                || attributeTypeInformation instanceof ShortAttributeTypeInformation) {
            formattedTextField = new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                    Cab2bFormattedTextField.INTEGER_NUMBER_FIELD);
        } else if (attributeTypeInformation instanceof FloatAttributeTypeInformation
                || attributeTypeInformation instanceof DoubleAttributeTypeInformation) {
            formattedTextField = new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                    Cab2bFormattedTextField.FLOAT_NUMBER_FIELD);
        } else {
            formattedTextField = new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                    Cab2bFormattedTextField.WHOLE_NUMBER_FIELD);
        }

        return formattedTextField;
    }

    /**
     * @param condition
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#setComponentPreference(java.lang.String)
     */
    public void setComponentPreference(String condition) {
        if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem().compareToIgnoreCase("Not IN") == 0)) {
            ((Cab2bFormattedTextField) firstComponent).setCommaAllowed(true);
        } else {
            ((Cab2bFormattedTextField) firstComponent).setCommaAllowed(false);
        }
    }

    /**
     * Resets all the values in the Number panel
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#resetPanel()
     */
    public void resetPanel() {
        ((Cab2bFormattedTextField) firstComponent).setText("");
        ((Cab2bFormattedTextField) secondComponent).setText("");
    }

}
