package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Cab2bLabel extends JLabel{

	static Color defaultBgColor = Color.WHITE;
	
	public Cab2bLabel()
	{
		this("");
	}
	
	public Cab2bLabel(String title) 
	{
		this(title, Cab2bStandardFonts.ARIAL_PLAIN_12);
	}
	
	public Cab2bLabel(String title, int alignment) 
	{
		this(title, Cab2bStandardFonts.ARIAL_PLAIN_12);
	}
	
	public Cab2bLabel(String title, Font textFont)
	{
		this(title, textFont, SwingConstants.LEFT);
	}
	
	public Cab2bLabel(String title, Font textFont, int alignment)
	{
		super(title, alignment);
		this.setFont(textFont);
		this.setBackground(defaultBgColor);
	}
	
	public Cab2bLabel(Icon icon)
	{
		super(icon);
	}
}
