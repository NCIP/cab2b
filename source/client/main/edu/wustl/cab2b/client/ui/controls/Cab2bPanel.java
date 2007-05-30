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
	}

	public Cab2bPanel(LayoutManager lm)
	{
		this(lm, false);
	}

	public Cab2bPanel(LayoutManager lm, boolean isDoubleBuffered)
	{
		this(lm, isDoubleBuffered, defaultBgColor);
	}
	
	public Cab2bPanel(LayoutManager lm, boolean isDoubleBuffered, Color bgColor)
	{
		super(lm, isDoubleBuffered);
		this.setBackground(bgColor);
		//this.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
	}
	
	public static void main(String[] args)
	{
		Cab2bPanel mainpanel = new Cab2bPanel();
		mainpanel.setLayout(new BorderLayout());
		
		Cab2bPanel leftPanel = new Cab2bPanel();
		leftPanel.add(new Cab2bButton("My Button"));
		leftPanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		Cab2bPanel rightPanel = new Cab2bPanel();
		rightPanel.add(new Cab2bLabel("Some Label TExt"));
		rightPanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		Cab2bPanel bottomPanel = new Cab2bPanel();
		bottomPanel.add(new Cab2bLabel("bottom panel label"));
		bottomPanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane.setDividerSize(4);
		splitPane.setOneTouchExpandable(false);
		
		mainpanel.add(splitPane,BorderLayout.CENTER);
		mainpanel.add(bottomPanel, BorderLayout.SOUTH);
		
		
		WindowUtilities.showInFrame(mainpanel, "Testting Cab2bPanel");
	}

}
