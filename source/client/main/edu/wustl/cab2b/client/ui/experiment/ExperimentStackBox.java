package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;

/*
 *  Class used to display left hand side stack panel.
 * */

public class ExperimentStackBox extends Cab2bPanel{

	/**
	 * @param args
	 */

	//panel to display data-list (flat structure PPT slide no :44 )
	Cab2bPanel dataCategoryPanel = null;
	
	//panel to display filters on selected data-category 
	Cab2bPanel dataFilterPanel = null;
	
	//panel to display analysed data on selected data-category 
	Cab2bPanel analyseDataPanel = null;
	
	//panel to display analysed data on selected data-category 
	Cab2bPanel visualiseDataPanel = null;
	
	//panel to display analysed data on selected data-category 
	Cab2bPanel dataViewPanel = null;
	
	//stack box to add all this panels
	StackedBox stackedBox;
	
	
	public ExperimentStackBox()
	{
		initGUI();
	}
	
	public void initGUI()
	{
		this.setLayout(new BorderLayout());
		stackedBox = new StackedBox();
		stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
		
		//Select data category pane
		dataCategoryPanel = new Cab2bPanel();
		dataCategoryPanel.setPreferredSize(new Dimension(250, 150));
		dataCategoryPanel.setOpaque(false);
		stackedBox.addBox("Select Data Category", dataCategoryPanel,"resources/images/mysearchqueries_icon.gif");		
		
		dataFilterPanel = new Cab2bPanel();
		dataFilterPanel.setPreferredSize(new Dimension(250, 150));
		dataFilterPanel.setOpaque(false);
		stackedBox.addBox("Filter Data ", dataFilterPanel,"resources/images/mysearchqueries_icon.gif");
				
		analyseDataPanel = new Cab2bPanel();
		analyseDataPanel.setPreferredSize(new Dimension(250, 150));
		analyseDataPanel.setOpaque(false);
		stackedBox.addBox("Analyze Data ", analyseDataPanel,"resources/images/mysearchqueries_icon.gif");
		
		stackedBox.setPreferredSize(new Dimension(250,500));
		stackedBox.setMinimumSize(new Dimension(250,500));
		this.add(stackedBox);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
