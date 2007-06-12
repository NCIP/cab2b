package edu.wustl.cab2b.client.ui.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bListBox;
import edu.wustl.cab2b.client.ui.experiment.ApplyFilterPanel;
import edu.wustl.cab2b.client.ui.experiment.ExperimentDataCategoryGridPanel;

/**
 * This class generates a pop-up of Enumerated filter type when clicked on a header
 * of a table that displays selected Category records
 * 
 * @author hrishikesh_rajpathak
 *
 */
public class EnumeratedFilterPopUp extends Cab2bFilterPopup {
	private static final long serialVersionUID = 1L;

	private Cab2bLabel myLable1;

	private Cab2bListBox listBox;

	private String columnName;

	public EnumeratedFilterPopUp(
			ApplyFilterPanel applyFilterpanel,
			Collection<PermissibleValueInterface> permissibleValueCollection,
			CaB2BPatternFilter oldfilter,
			String columnName,
			int columnIndex) {
		super(applyFilterpanel, columnName, columnIndex);
		this.columnName = columnName;

		myLable1 = new Cab2bLabel("Select Values");

		DefaultListModel model = new DefaultListModel();
		for (PermissibleValueInterface pvInterface : permissibleValueCollection) {
			model.addElement(pvInterface.getValueAsObject().toString());

		}
		listBox = new Cab2bListBox(model);

		ArrayList<String> val = new ArrayList<String>();
		if (oldfilter != null) {
			String selectedPatternString = oldfilter.getPattern().pattern();
			StringTokenizer st = new StringTokenizer(selectedPatternString, "|");

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				String selectedPatternValue = token.substring(1, token.length() - 1);
				val.add(selectedPatternValue);

			}
		}
		listBox.setSelectedValues(val);

		this.add("hfill ", myLable1);
		this.add("br", listBox);
		this.add("br", okButton);
		this.add("tab tab", cancelButton);
		this.setPreferredSize(new Dimension(250, 200));
	}

	/**
	 * This method creates the filter taking its input from the list box filed.
	 */
	protected CaB2BFilterInterface okActionPerformed(ActionEvent e) {
		ExperimentDataCategoryGridPanel.values.clear();
		Object[] selectedValues = listBox.getSelectedValues();
		String patternString;
		// int size=selectedValues.length;
		patternString = "(" + selectedValues[0].toString() + ")";
		ExperimentDataCategoryGridPanel.values.add(selectedValues[0].toString());
		for (int i = 1; i < selectedValues.length; i++) {
			patternString = patternString.concat("|" + "(" + selectedValues[i].toString() + ")");
			ExperimentDataCategoryGridPanel.values.add(selectedValues[i].toString());

		}
		System.out.println(patternString);
		return new CaB2BPatternFilter(patternString, 0, columnIndex, columnName);
	}

}
