package edu.wustl.cab2b.client.ui.filter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.experiment.ApplyFilterPanel;
import edu.wustl.cab2b.client.ui.experiment.ExperimentStackBox;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;

/**
 * Class to implement the filter pop-up
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public abstract class Cab2bFilterPopup extends Cab2bPanel {

    protected JDialog dialog;

    protected Cab2bButton okButton, cancelButton;

    protected String columnName;

    protected int columnIndex;

    /**
     * Constructor to generate pop-up
     * 
     * @param colName
     * @param colIndex
     */
    public Cab2bFilterPopup(final ApplyFilterPanel applyFilterPanel, String colName, int colIndex) {
        this.columnName = colName;
        this.columnIndex = colIndex;

        // Cancle button listener
        cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        // Generic OK button listener
        okButton = new Cab2bButton("Ok");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CaB2BFilterInterface filter = okActionPerformed(e);
                if (filter != null) {
                    applyFilterPanel.addFilter(columnName, filter);
                    ExperimentStackBox.getDataForFilterPanel(applyFilterPanel);
                    updateUI();
                }
                dialog.dispose();
            }
        });
    }

    /**
     * Abstract method to be implemented by individual pop-ups on "OK" buttom
     * click.
     * 
     * @param e
     * @return
     */
    protected abstract CaB2BFilterInterface okActionPerformed(ActionEvent e);

    public JDialog showInDialog() {
        Dimension dimension = null;
        if (this instanceof PatternPopup) {
            dimension = new Dimension(250, 100);
        } else if (this instanceof EnumeratedFilterPopUp) {
            dimension = new Dimension(250, 175);
        } else if (this instanceof FilterComponent) {
            dimension = new Dimension(435, 190);
        }
        dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this, "Set Filter", dimension, true, false);
        dialog.setVisible(true);
        return dialog;
    }

}
