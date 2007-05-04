package edu.wustl.cab2b.client.ui.filter;

/**
 * <p>Title: Cab2bFilterPopup Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak
 * @version 1.0
 */
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;

public abstract class Cab2bFilterPopup extends Cab2bPanel {

	protected JDialog dialog;

	protected Cab2bButton okButton, cancelButton;

	protected String columnName;

	protected int columnIndex;

	public Cab2bFilterPopup(String colName, int colIndex) {
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

				edu.wustl.cab2b.client.ui.experiment.ExperimentDataCategoryGridPanel
						.addFilter(columnName, filter);
				dialog.dispose();
			}

		});
	}

	protected abstract CaB2BFilterInterface okActionPerformed(ActionEvent e);

	public JDialog showInDialog() {
		dialog = WindowUtilities.setInDialog(NewWelcomePanel.mainFrame, this,
				"Set Filter", new Dimension(250, 150), true, false);
		dialog.setVisible(true);
		return dialog;
	}

}
