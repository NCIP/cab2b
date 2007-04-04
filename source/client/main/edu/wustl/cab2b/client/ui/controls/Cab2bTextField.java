
package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;

import javax.swing.JTextField;

public class Cab2bTextField extends JTextField
{

	final Dimension dim = new Dimension(100, 20);

	public Cab2bTextField()
	{
		super();
		setPreferredSize(dim);
	}
}
