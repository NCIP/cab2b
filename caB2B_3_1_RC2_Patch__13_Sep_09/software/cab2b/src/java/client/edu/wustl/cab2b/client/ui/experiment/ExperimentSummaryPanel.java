package edu.wustl.cab2b.client.ui.experiment;

import java.awt.Dimension;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;

public class ExperimentSummaryPanel extends Cab2bPanel {
	
	Map experimentSummary;
	
	/**
	 * @param summary
	 */
	public ExperimentSummaryPanel(Map summary)
	{
		experimentSummary = summary;
		initGUI();
	}
	
	private void initGUI()
	{
		this.setLayout(new RiverLayout());
		
		Iterator summaryIter = experimentSummary.keySet().iterator();
		while(summaryIter.hasNext())
		{
			String key = (String) summaryIter.next();
			String value = (String) experimentSummary.get(key);
			
			JLabel keyLabel = new JLabel(key+" : ");
			JLabel valueLabel = new JLabel(value);
			
			this.add("p",keyLabel);
			this.add("tab tab",valueLabel);
		}
		
		//Set keySet = experimentSummary.keySet();
		//int numOfDetails =keySet.size();
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map expSummary = new HashMap();
		expSummary.put("Name ", "Human Microarray - 1");
		expSummary.put("Created on", (new Date()).toString());
		expSummary.put("Data List", "Hu99As, Hu01At, HuSn45");
		expSummary.put("Description", "This a Dummy Experiment ");
		
		ExperimentSummaryPanel expSummaryPanel = new ExperimentSummaryPanel(expSummary);
		
		JFrame frame = new JFrame("New Exp Details -- caB2B");
		frame.setSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expSummaryPanel);
		frame.setVisible(true);
	}

}
