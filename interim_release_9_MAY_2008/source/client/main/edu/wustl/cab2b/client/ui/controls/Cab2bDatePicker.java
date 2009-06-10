package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import com.toedter.calendar.JDateChooser;

public class Cab2bDatePicker extends JDateChooser {
    private static final long serialVersionUID = 1L;

    final Dimension dim = new Dimension(100, 20);

    public Cab2bDatePicker() {
        super(null, "yyyy/MM/dd");
        setPreferredSize(dim);
    }

}
