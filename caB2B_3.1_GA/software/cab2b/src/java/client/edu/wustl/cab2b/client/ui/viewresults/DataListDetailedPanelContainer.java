package edu.wustl.cab2b.client.ui.viewresults;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bFileFilter;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

/**
 * Contains of the components in data list detailed section
 * @author rahul_ner
 */
public class DataListDetailedPanelContainer extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    Cab2bPanel dataListDetailedPanel;

    public DataListDetailedPanelContainer() {

    }

    public DataListDetailedPanelContainer(Cab2bPanel dataListDetailedPanel) {
        this.dataListDetailedPanel = dataListDetailedPanel;
        initGUI();
    }

    private void initGUI() {
        this.add("br hfill vfill", dataListDetailedPanel);
    }
}