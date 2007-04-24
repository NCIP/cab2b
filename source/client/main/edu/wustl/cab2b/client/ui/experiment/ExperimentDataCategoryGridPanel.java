package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.common.util.logger.Logger;

/*
 * Class used to display selected Category records 
 */

public class ExperimentDataCategoryGridPanel extends Cab2bPanel{

	/**
	 * @param args
	 */


	/*Tab component*/
	JTabbedPane tabComponent;
	
	/*Panel to display experiment data when data category node is selected 
	 * First tab panel on tab Component*/
	Cab2bPanel experimentDataPanel;	
	
	/*Panel to display analysis performed on experiment 
	 * Second tab panel on tab component*/
	Cab2bPanel analysisDataPanel;
	
	/*Button to save data category */
	Cab2bButton saveDataCategoryButton;
	
	Cab2bButton prevButton;
	
	/*Table to display records on Experiment Data panels, when user selects any
	 * data category node */
	Cab2bTable table;	


	Vector m_columnVector = new Vector();
	Vector m_dataRecordVector = new Vector();
	
	public ExperimentDataCategoryGridPanel(){
		 initGUI();		 
	}
	
	
	public ExperimentDataCategoryGridPanel(Vector columnVector, Vector dataRecordVector){
		m_columnVector = columnVector;
		m_dataRecordVector = dataRecordVector;
		 initGUI();		 
	}
	
	public void refreshTable(Object columnVector[], Object[][] dataRecordVector)
	{		
		this.removeAll();
		experimentDataPanel.removeAll();
		table = new Cab2bTable(false, dataRecordVector, columnVector);		
		experimentDataPanel.add("hfill vfill",new JScrollPane(table));
		
		Cab2bPanel northPanel = new Cab2bPanel();
		northPanel.add(saveDataCategoryButton);
		this.add(northPanel, BorderLayout.NORTH);
		
		tabComponent.add("Experiment Data", experimentDataPanel);
		tabComponent.add("Analysis", analysisDataPanel);
		this.add(tabComponent,BorderLayout.CENTER);
		
		
		Cab2bPanel bottomPanel = new Cab2bPanel();
		bottomPanel.add(prevButton);
		this.add(bottomPanel,BorderLayout.SOUTH);		
		this.updateUI();
	}
	
	public void initGUI()
	{
		this.setLayout(new BorderLayout());		
		tabComponent = new JTabbedPane();
		tabComponent.setBorder(null);
		experimentDataPanel = new Cab2bPanel();
		experimentDataPanel.setBorder(null);
		analysisDataPanel = new Cab2bPanel();
		
		table = new Cab2bTable(false, m_dataRecordVector, m_columnVector);
		
		/*Adding scrollpane*/
		JScrollPane scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(null);
		experimentDataPanel.add("hfill vfill",scrollPane);
		
		saveDataCategoryButton = new Cab2bButton("Save Data Category");		
		saveDataCategoryButton.setPreferredSize(new Dimension(160,22));
		
		Cab2bPanel northPanel = new Cab2bPanel();
		northPanel.add(saveDataCategoryButton);
		this.add(northPanel, BorderLayout.NORTH);
		
		tabComponent.add("Experiment Data", experimentDataPanel);
		tabComponent.add("Analysis", analysisDataPanel);
		this.add(tabComponent,BorderLayout.CENTER);
		
		prevButton = new Cab2bButton("Previous");
		Cab2bPanel bottomPanel = new Cab2bPanel();
		bottomPanel.add(prevButton);
		this.add(bottomPanel,BorderLayout.SOUTH);
	}
	
	public static void main(String[] args){
	//TODO Auto-generated method stub
		ExperimentDataCategoryGridPanel expDetPanel = new ExperimentDataCategoryGridPanel();
		JFrame frame = new JFrame("Experiment");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expDetPanel);
		frame.setVisible(true);
	}
}
