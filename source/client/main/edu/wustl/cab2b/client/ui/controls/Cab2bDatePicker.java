package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import org.jdesktop.swingx.JXDatePicker;

public class Cab2bDatePicker extends JXDatePicker{

	final Dimension dim = new Dimension(100,20);

	public Cab2bDatePicker() {
		super();
		setPreferredSize(dim);
	}
	public Cab2bDatePicker(long time) {
		super(time);
		setPreferredSize(dim);
	}
}
