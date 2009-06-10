package edu.wustl.cab2b.client.ui.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import edu.wustl.cab2b.client.ui.experiment.ApplyFilterPanel;
import edu.wustl.cab2b.client.ui.filter.CaB2BPatternFilter;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTextField;

/**
 * This class generates a pop-up of Pattern filter type when clicked on a header
 * of a table that displays selected Category records
 * 
 * @author hrishikesh_rajpathak
 *
 */
public class PatternPopup extends Cab2bFilterPopup {
    private static final long serialVersionUID = 1L;

    private Cab2bLabel myLable1;

    private Cab2bTextField patternText;

    private String columnName;

    public PatternPopup(
            ApplyFilterPanel applyFilterpanel,
            CaB2BPatternFilter inputFilter,
            String colName,
            int colIndex) {
        super(applyFilterpanel, colName, colIndex);
        this.columnName = colName;
        myLable1 = new Cab2bLabel("Enter Pattern");

        patternText = new Cab2bTextField();
        if (inputFilter != null) {
            patternText.setText(inputFilter.getPattern().pattern());
        }

        this.add("hfill ", myLable1);
        this.add("tab", patternText);
        this.add("br", okButton);
        this.add("tab tab", cancelButton);
        this.setPreferredSize(new Dimension(250, 100));
    }

    /**
     * This method creates the filter taking its input from the text filed.
     */
    protected CaB2BFilterInterface okActionPerformed(ActionEvent e) {
        return new CaB2BPatternFilter(patternText.getText(), 0, columnIndex, columnName);
    }
}
