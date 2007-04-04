package edu.wustl.cab2b.client.ui.experiment;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * Experiment panel to view experiment and experiment group hierarchies 
 * in left panel and experiment details in the right panel.
 * 
 * @author chetan_bh
 */
public class ExperimentPanel extends Cab2bTitledPanel
{
	JSplitPane splitPane;
	JButton newExperiment;
	JXPanel parentPanel;
	ExperimentDetailsPanel expDetailsPanel;
	ExperimentHierarchyPanel expHierarchyPanel;
	
	public ExperimentPanel(String title)
	{
		super(title);
		initGUI();
	}
	
	private void initGUI()
	{
		parentPanel = new Cab2bPanel(new RiverLayout());
		
		newExperiment = new Cab2bButton("Create New Experiment");
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(200);
		
		expDetailsPanel = new ExperimentDetailsPanel();
		splitPane.setRightComponent(expDetailsPanel);
		
		expHierarchyPanel = new ExperimentHierarchyPanel(expDetailsPanel);
		splitPane.setLeftComponent(expHierarchyPanel);
		
		parentPanel.add("br",newExperiment);
		parentPanel.add("br hfill vfill",splitPane);
		
		this.add(parentPanel);
	}
	
	public static void main(String[] args)
	{
		ApplicationProperties.initBundle("Cab2bApplicationResources");
		Logger.configure("log4j.properties");
		ErrorCodeHandler.initBundle("errorcodes");
		
		ExperimentPanel expPanel = new ExperimentPanel(ApplicationProperties.getValue("cab2b.experiment.frame.title"));
		
		JFrame frame = new JFrame("Experiment");
		frame.setSize(600,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(expPanel);
		frame.setVisible(true);
	}

}
