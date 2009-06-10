package edu.wustl.cab2b.client.ui.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

public class Cab2bRadioButton extends JRadioButton
{
	
	static Color defaultBgColor = Color.WHITE;
	
	public Cab2bRadioButton(String strLabel)
	{
		super(strLabel);		
		/*Arial font in windows is mapped to the SansSerif logical name.*/
		this.setFont(new Font("SansSerif",Font.PLAIN,12));
		this.setBackground(defaultBgColor);
	}
}
