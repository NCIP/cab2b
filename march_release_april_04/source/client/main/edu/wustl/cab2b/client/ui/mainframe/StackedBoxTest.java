package edu.wustl.cab2b.client.ui.mainframe;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;


public class StackedBoxTest
{
	public static void main(String[] args)
	{
		StackedBox stackedBox = new StackedBox();
		//B2BStackedBox stackedBox = new B2BStackedBox();
		
		stackedBox.addBox("Status", getStausPanel(),"");
		stackedBox.addBox("Info", getInfoPanel(),"");
		
		WindowUtilities.showInFrame(stackedBox, "Stacked BOx Dem,o");
	}
	
	public static JXPanel getStausPanel()
	{
		JXPanel panel = new Cab2bPanel();
		
		panel.add(new Cab2bButton("Button - 1"));
		panel.add(new Cab2bButton("Button - 2"));
		
		return panel;
	}
	
	public static JXPanel getInfoPanel()
	{
		JXPanel panel = new Cab2bPanel();
		
		panel.add(new Cab2bLabel("Info 1"));
		panel.add(new Cab2bLabel("Info 2"));
		
		return panel;
	}
	
}
