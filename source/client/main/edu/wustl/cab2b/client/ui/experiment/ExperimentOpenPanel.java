package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;

public class ExperimentOpenPanel extends Cab2bTitledPanel {
	
	ExperimentStackBox experimentStackBox;
	ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel = null;
	Cab2bPanel experimentTitlePanel;
	Cab2bButton addDataButton;
	
	JSplitPane splitPane;
	
	
	public ExperimentOpenPanel()
	{
		initGUI();
	}
	
	public void initGUI()
	{   
		this.setLayout(new BorderLayout());
		
		experimentTitlePanel = new Cab2bPanel();		
		Cab2bLabel experimentLabel = new Cab2bLabel("Experiment Name");
		experimentTitlePanel.add("br br hfill",experimentLabel);
		
		addDataButton = new Cab2bButton("Add Data");	
		experimentTitlePanel.add(addDataButton);
		
		this.add(experimentTitlePanel, BorderLayout.NORTH );
		
		experimentStackBox = new ExperimentStackBox();
		experimentStackBox.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		experimentDataCategoryGridPanel = new ExperimentDataCategoryGridPanel();		
		experimentDataCategoryGridPanel.setBorder(new CustomizableBorder(new Insets(1,1,1,1), true, true));
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, experimentStackBox, experimentDataCategoryGridPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(0.2D);
		
		this.add(splitPane);
	}
	
	public static void main(String str[])
	{
	
		ExperimentOpenPanel expDetPanel = new ExperimentOpenPanel();
		
		JFrame frame = new JFrame("Experiment");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expDetPanel);
		frame.setVisible(true);
	}	
}
