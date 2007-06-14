package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bFormattedTextField;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

public class NumberTypePanel extends AbstractTypePanel {
    private static final long serialVersionUID = 1L;

    public NumberTypePanel(
            ArrayList<String> conditionList,
            AttributeInterface attributeEntity,
            Boolean showCondition,
            Dimension maxLabelDimension) {
        super(conditionList, attributeEntity, new RiverLayout(), showCondition, maxLabelDimension);
    }

    public JComponent getFirstComponent() {
        return getProperFormattedTextField();
    }

    public JComponent getSecondComponent() {
        return getProperFormattedTextField();
    }

    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<String>();
        String nameString = ((Cab2bFormattedTextField) m_NameEdit).getText();
        if (nameString.length() != 0) {
            if (showCondition
                    && (getCondition().compareToIgnoreCase("IN") == 0 || getCondition().compareToIgnoreCase(
                                                                                                            "Not IN") == 0)) {
                ArrayList<String> strings = CommonUtils.splitStringWithTextQualifier(nameString, '"', ',');
                for (int i = 0; i < strings.size(); i++) {
                    values.add(strings.get(i).trim());
                }
            } else {
                values.add(((Cab2bFormattedTextField) m_NameEdit).getText());
            }

            if (((Cab2bFormattedTextField) m_OtherEdit).getText().length() != 0) {
                values.add(((Cab2bFormattedTextField) m_OtherEdit).getText());
            }
        } else if (((Cab2bFormattedTextField) m_OtherEdit).getText().length() != 0) {
            values.add(((Cab2bFormattedTextField) m_OtherEdit).getText());
        }
        return values;
    }

    public void setValues(ArrayList<String> values) {
        if (values.size() == 0) {
            return;
        }

        // If selected condition is "IN" or "Not IN"
        if (showCondition
                && (getCondition().compareToIgnoreCase("IN") == 0 || getCondition().compareToIgnoreCase("Not IN") == 0)) {
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
            ((Cab2bFormattedTextField) m_NameEdit).setCommaAllowed(true);
            ((Cab2bFormattedTextField) m_NameEdit).setText(sb.toString());
        } else if (values.size() == 1) {
            ((Cab2bFormattedTextField) m_NameEdit).setCommaAllowed(false);
            String value = values.get(0);
            int index = -1;
            if ((index = value.indexOf(",")) != -1) {
                value = value.substring(0, index);
            }
            ((Cab2bFormattedTextField) m_NameEdit).setText(value);
        } else if (values.size() == 2) {
            ((Cab2bFormattedTextField) m_NameEdit).setCommaAllowed(false);
            String value = values.get(0);
            int index = -1;
            if ((index = value.indexOf(",")) != -1) {
                value = value.substring(0, index);
            }
            ((Cab2bFormattedTextField) m_NameEdit).setText(value);
            ((Cab2bFormattedTextField) m_OtherEdit).setText(values.get(1));
        }

    }

    private Cab2bFormattedTextField getProperFormattedTextField() {
        Cab2bFormattedTextField formattedTextField = null;
        AttributeTypeInformationInterface attributeTypeInformation = attributeEntity.getAttributeTypeInformation();

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

    public void setComponentPreference(String condition) {
        if (showCondition
                && (getCondition().compareToIgnoreCase("IN") == 0 || getCondition().compareToIgnoreCase("Not IN") == 0)) {
            ((Cab2bFormattedTextField) m_NameEdit).setCommaAllowed(true);
        } else {
            ((Cab2bFormattedTextField) m_NameEdit).setCommaAllowed(false);
        }
    }

    public void resetPanel() {
        ((Cab2bFormattedTextField) m_NameEdit).setText("");
        ((Cab2bFormattedTextField) m_OtherEdit).setText("");
    }

}
