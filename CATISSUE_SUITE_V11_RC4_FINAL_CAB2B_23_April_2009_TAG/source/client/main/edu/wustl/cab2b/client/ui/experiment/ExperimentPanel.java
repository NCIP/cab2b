package edu.wustl.cab2b.client.ui.experiment;
import static edu.wustl.cab2b.client.ui.util.ApplicationResourceConstants.EXPERIMENT_FRAME_TITLE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * Experiment panel to view experiment and experiment group hierarchies 
 * in left panel and experiment details in the right panel.
 * 
 * @author chetan_bh
 */
public class ExperimentPanel extends Cab2bTitledPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExperimentPanel.class);
    JSplitPane splitPane;

    JButton newExperiment;

    JXPanel displayExperimentPanel;

    ExperimentDetailsPanel expDetailsPanel;

    ExperimentHierarchyPanel expHierarchyPanel;
    final static String title = ApplicationProperties.getValue(EXPERIMENT_FRAME_TITLE);
    public ExperimentPanel() {
        super();
        Cab2bLabel label = new Cab2bLabel(title);
        label.setForeground(Color.blue);
        Font font = label.getFont();
        label.setFont(new Font(font.getName(),Font.BOLD, font.getSize() + 3));
        Cab2bPanel panel = new Cab2bPanel(new RiverLayout(5,5));
        panel.add("hfill", label);
        this.add(panel, BorderLayout.NORTH);
        initGUI();
        setBorder(null);
    }

    private void initGUI() {
        logger.debug("Inside Constructor.");

        displayExperimentPanel = new Cab2bPanel(new RiverLayout(5,5));

        newExperiment = new Cab2bButton("Create New Experiment");
        newExperiment.setPreferredSize(new Dimension(180, 22));
        newExperiment.setEnabled(false);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(242);
        splitPane.setDividerSize(4);
        expDetailsPanel = new ExperimentDetailsPanel();
        splitPane.setRightComponent(expDetailsPanel);
        splitPane.setBorder(null);
        expHierarchyPanel = new ExperimentHierarchyPanel(expDetailsPanel);
        splitPane.setLeftComponent(expHierarchyPanel);
        expHierarchyPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        displayExperimentPanel.add("br", newExperiment);
        displayExperimentPanel.add("br hfill vfill", splitPane);
        this.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        this.add(displayExperimentPanel);
    }

//    public static void main(String[] args) {
//        ApplicationProperties.initBundle("Cab2bApplicationResources");
//        Logger.configure("log4j.properties");
//        ErrorCodeHandler.initBundle("errorcodes");
//
//        ExperimentPanel expPanel = new ExperimentPanel(
//                ApplicationProperties.getValue("cab2b.experiment.frame.title"));
//
//        JFrame frame = new JFrame("Experiment");
//        frame.setSize(600, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(expPanel);
//        frame.setVisible(true);
//    }
}
