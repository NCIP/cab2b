package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;

import edu.wustl.cab2b.client.ui.controls.Cab2bFormattedTextField;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

public class StringTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    public StringTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
        super(conditionList, maxLabelDimension);
    }

    public JComponent getFirstComponent() {
        return new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                Cab2bFormattedTextField.PLAIN_FIELD);
    }

    public JComponent getSecondComponent() {
        return new Cab2bFormattedTextField(CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
                Cab2bFormattedTextField.PLAIN_FIELD);
    }

    /**
     * Method to return set values
     */
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<String>();
        String nameString = ((Cab2bFormattedTextField) firstComponent).getText();
        if (nameString.length() != 0) {
            if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem().compareToIgnoreCase("Not IN") == 0)) {
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
        }
        return values;
    }

    /**
     * Method to set values in the component
     */
    public void setValues(ArrayList<String> values) {
        if (values.size() == 0) {
            return;
        }

        // If selected condition is "IN" or "Not IN"
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
        } else if (values.size() == 1) {
            ((Cab2bFormattedTextField) firstComponent).setText(values.get(0));
        } else if (values.size() == 2) {
            ((Cab2bFormattedTextField) firstComponent).setText(values.get(0));
            ((Cab2bFormattedTextField) secondComponent).setText(values.get(1));
        }
    }

    public void setComponentPreference(String condition) {

    }

    public void resetPanel() {
        ((Cab2bFormattedTextField) firstComponent).setText("");
        ((Cab2bFormattedTextField) secondComponent).setText("");
    }
}
