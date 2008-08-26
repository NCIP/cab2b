package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;

import edu.wustl.cab2b.client.ui.controls.Cab2bFormattedTextField;

/**
 * Condition panel if attribute type is of type String. Used in AddLimit and ParameterizedQuery pages.
 * @author deepak_shingan 
 */
public class StringTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param conditionList
     * @param maxLabelDimension
     */
    public StringTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getFirstComponent()
     */
    public JComponent getFirstComponent() {
        return new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                Cab2bFormattedTextField.PLAIN_FIELD);
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#getSecondComponent()
     */
    public JComponent getSecondComponent() {
        return new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                Cab2bFormattedTextField.PLAIN_FIELD);
    }

    /* Method to set values in the component
     *  (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.main.IComponent#setValues(java.util.ArrayList)
     */
    public void setValues(ArrayList<String> values) {
        if (values.size() == 0) {
            return;
        }

        // If selected condition is "IN" or "Not IN"
        if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem().compareToIgnoreCase("Not IN") == 0)) {
            super.setMultipleValues(values);
        } else if (values.size() == 1) {
            ((Cab2bFormattedTextField) firstComponent).setText(values.get(0));
        } else if (values.size() == 2) {
            ((Cab2bFormattedTextField) firstComponent).setText(values.get(0));
            ((Cab2bFormattedTextField) secondComponent).setText(values.get(1));
        }
    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#setComponentPreference(java.lang.String)
     */
    public void setComponentPreference(String condition) {

    }

    /* (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.main.AbstractTypePanel#resetPanel()
     */
    public void resetPanel() {
        ((Cab2bFormattedTextField) firstComponent).setText("");
        ((Cab2bFormattedTextField) secondComponent).setText("");
    }
}
