package edu.wustl.cab2b.client.ui.dag.ambiguityresolver;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jdesktop.swingx.JXTaskPane;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;

/**
 * A Panel to display the controls for filtering paths 
 * for the current ambiguity.
 * 
 * @author chetan_bh
 */
public class FilterPathsPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /**
     * A task pane to hold all filter related controls. 
     */
    private JXTaskPane filterPathsTaskPane;

    private Cab2bComboBox nodeSelectionCombo;

    private Cab2bComboBox categoryCombo;

    private Cab2bButton addToFilterPathButton;

    private Cab2bButton clearButton;

    public FilterPathsPanel() {
        initGUI();
    }

    private void initGUI() {
        filterPathsTaskPane = new JXTaskPane();
        filterPathsTaskPane.setExpanded(false);
        filterPathsTaskPane.setLayout(new RiverLayout());
        filterPathsTaskPane.getContentPane().setBackground(Color.WHITE);
        filterPathsTaskPane.setTitle("Filter Paths");
        filterPathsTaskPane.add("br", new Cab2bLabel(
                "You can filter the list of paths below by selecting and adding coordinates to the Filter Path."));
        filterPathsTaskPane.add("br", new Cab2bLabel("Node Selection"));

        nodeSelectionCombo = new Cab2bComboBox();
        nodeSelectionCombo.addItem("1"); // TODO remove this hard coding.
        nodeSelectionCombo.addItem("2");
        nodeSelectionCombo.addItem("3");
        filterPathsTaskPane.add("tab", nodeSelectionCombo);
        filterPathsTaskPane.add("tab", new Cab2bLabel("Category"));
        categoryCombo = new Cab2bComboBox();
        categoryCombo.addItem("Participant"); // TODO remove this hard coding.
        categoryCombo.addItem("Microarray");
        filterPathsTaskPane.add("tab", categoryCombo);
        addToFilterPathButton = new Cab2bButton("Add To Filter Path");
        addToFilterPathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Logger.out.debug("add to filter path");
            }
        });
        filterPathsTaskPane.add(addToFilterPathButton);

        filterPathsTaskPane.add("br", new Cab2bLabel("Filter Path"));

        clearButton = new Cab2bButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Logger.out.debug("clear button action listener");
            }
        });
        filterPathsTaskPane.add("tab", clearButton);
        this.add(filterPathsTaskPane);
    }

}
