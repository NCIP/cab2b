package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;

public class Cab2bCheckBox extends JCheckBox
{
	
	static Color defaultBgColor = Color.WHITE;
	
	public Cab2bCheckBox()
	{
		this("");
	}
	
	public Cab2bCheckBox(String title) 
	{
		this(title, Cab2bStandardFonts.DEFAULT_FONT);
	}
	
	public Cab2bCheckBox(String title, Font textFont) 
	{
		this(title, textFont, defaultBgColor);
	}
	
	public Cab2bCheckBox(String title, Font textFont, Color bgColor) 
	{
		super(title);
		this.setFont(textFont);
		this.setBackground(defaultBgColor);
	}
	
}
