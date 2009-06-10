package edu.wustl.cab2b.client.ui.controls;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JButton;

public class Cab2bButton extends JButton{
	
	public Cab2bButton()
	{
		this("");
	}
	
	public Cab2bButton(String strLabel)
	{
		this(strLabel, false);
	}
	
	public Cab2bButton(String strLabel, Font textFont)
	{
		this(strLabel, false, textFont);
	}
	
	public Cab2bButton(String strLabel, boolean isSimpleButtonUI)
	{
		this(strLabel, isSimpleButtonUI, Cab2bStandardFonts.ARIAL_PLAIN_12);
	}
	
	public Cab2bButton(String strLabel, boolean isSimpleButtonUI, Font textFont)
	{
		super(strLabel);
		this.setPreferredSize(new Dimension(85,22));
		/*Arial font in windows is mapped to the SansSerif logical name.*/
		this.setFont(textFont);
		if(isSimpleButtonUI)
			this.setUI(new Cab2bBasicButtonUI());
	}
	
	public Cab2bButton(Icon imageIcon)
	{
		super(imageIcon);
		this.setPreferredSize(new Dimension(85,22));
	}
	
}
