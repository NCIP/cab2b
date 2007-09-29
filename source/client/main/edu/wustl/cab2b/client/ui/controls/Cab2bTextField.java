package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import javax.swing.JTextField;

public class Cab2bTextField extends JTextField {

	Dimension dim = new Dimension(100, 20);

	public Cab2bTextField() {
		super();
		setPreferredSize(dim);
	}

	public Cab2bTextField(String initString) {
		super(initString);
		setPreferredSize(dim);
	}

	public Cab2bTextField(String initString, Dimension dim) {
		super(initString);
		this.dim = dim;
		setPreferredSize(this.dim);
	}
}
