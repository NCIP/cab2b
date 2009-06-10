package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.global.ApplicationProperties;

@SuppressWarnings("serial")
public class NewExperimentPanel extends Cab2bPanel implements ActionListener {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(NewExperimentPanel.class);
    private JTabbedPane tabbedPane;

    private JButton nextButton;

    private JButton prevButton;

    private JButton finishButton;

    //JButton cancelButton;

    private JXPanel wizardButtonPanel;

    public NewExperimentPanel() {
        this.setLayout(new BorderLayout());

        //Create the JTabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(790, 550));
        Vector<String> myDataList = new Vector<String>();
        myDataList.add("Tumor Micro Array for Mouse");
        myDataList.add("Microarray Data - HuAv91s");
        myDataList.add("Microarray Data - MuAv01g");
        myDataList.add("Tumor Vs. Diseased Data Set for Human");
        myDataList.add("SNP Data - Chromosome 12 Hu");

        Map<String, String> expSummary = new HashMap<String, String>();
        expSummary.put("Name ", "Human Microarray - 1");
        expSummary.put("Created on", (new Date()).toString());
        expSummary.put("Data List", "Hu99As, Hu01At, HuSn45");
        expSummary.put("Description", "This a Dummy Experiment ");

        //Create the tabs
        tabbedPane.add(ApplicationProperties.getValue("cab2b.experiment.newexp.expdetailstab.text"),
                       new NewExperimentDetailsPanel());
        tabbedPane.add(ApplicationProperties.getValue("cab2b.experiment.newexp.datalisttab.text"),
                       new ExperimentDataSelectionPanel(myDataList));
        tabbedPane.add(ApplicationProperties.getValue("cab2b.experiment.newexp.summarytab.text"),
                       new ExperimentSummaryPanel(expSummary));

        this.add(tabbedPane, BorderLayout.CENTER);
        setSize(795, 550);

        initGUI();

    }

    private void initGUI() {
        wizardButtonPanel = new Cab2bPanel();

        nextButton = new Cab2bButton(ApplicationProperties.getValue("button.next.text"));
        nextButton.addActionListener(this);
        nextButton.setActionCommand("next");

        prevButton = new Cab2bButton(ApplicationProperties.getValue("button.previous.text"));
        prevButton.addActionListener(this);
        prevButton.setActionCommand("previous");

        finishButton = new Cab2bButton(ApplicationProperties.getValue("button.finish.text"));
        finishButton.addActionListener(this);
        finishButton.setActionCommand("finish");
        //cancelButton = new JButton(ApplicationProperties.getValue("button.cancel.text"));

        // For the first screen only "Next" button is added to the wizard button panel.
        wizardButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        wizardButtonPanel.add(nextButton);
        //wizardButtonPanel.add(prevButton);
        //wizardButtonPanel.add(finishButton);
        //wizardButtonPanel.add(cancelButton);

        this.add(wizardButtonPanel, BorderLayout.SOUTH);

    }
    public void actionPerformed(ActionEvent e) {
        JButton wizardButton = (JButton) e.getSource();
        String actionCommnad = wizardButton.getActionCommand();

        int currentTab = tabbedPane.getSelectedIndex();

        if (actionCommnad.equals("next")) {
            tabbedPane.setSelectedIndex(currentTab + 1);

            wizardButtonPanel.removeAll();
            if (currentTab == 0) {
                wizardButtonPanel.add(prevButton);
                wizardButtonPanel.add(nextButton);
            } else // for index == 1
            {
                wizardButtonPanel.add(prevButton);
                wizardButtonPanel.add(finishButton);
            }
        } else if (actionCommnad.equals("previous")) {
            tabbedPane.setSelectedIndex(currentTab - 1);

            wizardButtonPanel.removeAll();
            if (currentTab == 2) {
                wizardButtonPanel.add(prevButton);
                wizardButtonPanel.add(nextButton);
            } else // for index == 2
            {
                wizardButtonPanel.add(nextButton);
            }
        } else if (actionCommnad.equals("finish")) {
            logger.debug("finally do somthing");
        }
        wizardButtonPanel.updateUI();
        wizardButtonPanel.repaint();
    }
}
