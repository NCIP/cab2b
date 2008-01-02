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

public class NumberTypePanel extends AbstractTypePanel {
	private static final long serialVersionUID = 1L;

	public NumberTypePanel(ArrayList<String> conditionList, Dimension maxLabelDimension) {
		super(conditionList, maxLabelDimension);
	}

	public JComponent getFirstComponent() {
		return getProperFormattedTextField();
	}

	public JComponent getSecondComponent() {
		return getProperFormattedTextField();
	}

	public ArrayList<String> getValues() {
		return super.getValues();
	}

	public void setValues(ArrayList<String> values) {
		if (values.size() == 0) {
			return;
		}

		// If selected condition is "IN" or "Not IN"
		if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem()
				.compareToIgnoreCase("Not IN") == 0)) {
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
			((Cab2bFormattedTextField) firstComponent).setCommaAllowed(true);
			((Cab2bFormattedTextField) firstComponent).setText(sb.toString());
		} else if (values.size() == 1) {
			((Cab2bFormattedTextField) firstComponent).setCommaAllowed(false);
			String value = values.get(0);
			int index = -1;
			if ((index = value.indexOf(",")) != -1) {
				value = value.substring(0, index);
			}
			((Cab2bFormattedTextField) firstComponent).setText(value);
		} else if (values.size() == 2) {
			((Cab2bFormattedTextField) firstComponent).setCommaAllowed(false);
			String value = values.get(0);
			int index = -1;
			if ((index = value.indexOf(",")) != -1) {
				value = value.substring(0, index);
			}
			((Cab2bFormattedTextField) firstComponent).setText(value);
			((Cab2bFormattedTextField) secondComponent).setText(values.get(1));
		}

	}

	private Cab2bFormattedTextField getProperFormattedTextField() {
		Cab2bFormattedTextField formattedTextField = null;
		AttributeTypeInformationInterface attributeTypeInformation = attribute
				.getAttributeTypeInformation();

		if (attributeTypeInformation instanceof IntegerAttributeTypeInformation
				|| attributeTypeInformation instanceof LongAttributeTypeInformation
				|| attributeTypeInformation instanceof ShortAttributeTypeInformation) {
			formattedTextField = new Cab2bFormattedTextField(
					CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
					Cab2bFormattedTextField.INTEGER_NUMBER_FIELD);
		} else if (attributeTypeInformation instanceof FloatAttributeTypeInformation
				|| attributeTypeInformation instanceof DoubleAttributeTypeInformation) {
			formattedTextField = new Cab2bFormattedTextField(
					CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
					Cab2bFormattedTextField.FLOAT_NUMBER_FIELD);
		} else {
			formattedTextField = new Cab2bFormattedTextField(
					CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE,
					Cab2bFormattedTextField.WHOLE_NUMBER_FIELD);
		}

		return formattedTextField;
	}

	public void setComponentPreference(String condition) {
		if ((getConditionItem().compareToIgnoreCase("IN") == 0 || getConditionItem()
				.compareToIgnoreCase("Not IN") == 0)) {
			((Cab2bFormattedTextField) firstComponent).setCommaAllowed(true);
		} else {
			((Cab2bFormattedTextField) firstComponent).setCommaAllowed(false);
		}
	}

	public void resetPanel() {
		((Cab2bFormattedTextField) firstComponent).setText("");
		((Cab2bFormattedTextField) secondComponent).setText("");
	}

}
