package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

public class Cab2bComboBox extends JComboBox{
	
	final Dimension dim = new Dimension(100,20);
	
	static Color defaultBgColor = Color.WHITE;
	
	public Cab2bComboBox(ComboBoxModel aModel)
	{
		super(aModel);
		setCommonPreferences();
	}
	
	public Cab2bComboBox(Object[] items)
	{
		super(items);
		setCommonPreferences();
	}
	
	public Cab2bComboBox(Vector<?> items)
	{
		super(items);
		setCommonPreferences();
	}
	
	public Cab2bComboBox() {
		super();
		setCommonPreferences();
	}
	
	/**
	 * This will set common preferences which should be
	 * common to all Cab2bComboBoxes.
	 */
	private void setCommonPreferences()
	{
		this.setPreferredSize(dim);
		this.setFont(new Font("SansSerif", Font.PLAIN, 10));
		this.setBackground(defaultBgColor);
	}
	
}
