package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.sql.Date;

import org.jdesktop.swingx.JXDatePicker;

import com.toedter.calendar.JDateChooser;

public class Cab2bDatePicker extends JDateChooser{

	final Dimension dim = new Dimension(100,20);

	public Cab2bDatePicker() {
		super(null, "yyyy/MM/dd");
		setPreferredSize(dim);			
	}
}
