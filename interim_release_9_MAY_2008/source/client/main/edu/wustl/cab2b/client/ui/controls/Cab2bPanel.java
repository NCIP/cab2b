package edu.wustl.cab2b.client.ui.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;


public class Cab2bPanel extends JXPanel
{

	static Color defaultBgColor = Color.WHITE;
	
	public Cab2bPanel()
	{
		this(new RiverLayout());
		this.setBorder(null);
	}

	public Cab2bPanel(LayoutManager lm)
	{
		this(lm, false);
		this.setBorder(null);
	}

	public Cab2bPanel(LayoutManager lm, boolean isDoubleBuffered)
	{
		this(lm, isDoubleBuffered, defaultBgColor);
		this.setBorder(null);
	}
	
	public Cab2bPanel(LayoutManager lm, boolean isDoubleBuffered, Color bgColor)
	{
		super(lm, isDoubleBuffered);
		this.setBackground(bgColor);
		//this.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		this.setBorder(null);
	}
    
    public void doInitialization() {
        
    }
}
