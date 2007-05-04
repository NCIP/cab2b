/**
 * <p>Title: RangePopup Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.0
 */

package edu.wustl.cab2b.client.ui.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;

/**
 * This class generates a pop-up of Range filter type when clicked on a header
 * of a table that displays selected Category records
 */

public class RangePopup extends Cab2bFilterPopup {

	private Cab2bLabel minLable, maxLable;

	private Cab2bTextField minText, maxText;

	public RangePopup(RangeFilter oldFilter, String colName, int colIndex)

	{
		super(colName, colIndex);
		minLable = new Cab2bLabel("Minimum :");
		maxLable = new Cab2bLabel("Maximum :");

		minText = new Cab2bTextField();
		maxText = new Cab2bTextField();
		String min = "";
		String max = "";

		if (oldFilter != null) {
			min = String.valueOf(oldFilter.minRange);
			minText.setText(min);
			max = String.valueOf(oldFilter.maxRange);
			maxText.setText(max);
		} else {
			min = "0";
			max = "0";
		}
		this.add("hfill ", minLable);
		this.add("tab", minText);

		this.add(" br ", maxLable);
		this.add("tab", maxText);
		this.add("br", okButton);

		this.add("tab ", cancelButton);

		this.setPreferredSize(new Dimension(250, 250));
	}
	/**
	 * This method creates the filter taking its input from the text filed.
	 */
	@Override
	protected CaB2BFilterInterface okActionPerformed(ActionEvent e) {
		int minimum = Integer.parseInt(minText.getText());
		int maximum = Integer.parseInt(maxText.getText());
		return new RangeFilter(minimum, maximum, columnIndex);
	}

}
