package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.CustomizableBorder;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.common.tree.ExperimentTreeNode;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * A panel to display details of the selected experiment
 * 
 * @author Deepak_Shingan
 */

public class ExperimentOpenPanel extends Cab2bTitledPanel {
    private static final long serialVersionUID = 1L;

    /*
     * Left hand side stack box used to display data category, Filter data,
     * Analysed data
     */
    private ExperimentStackBox experimentStackBox;

    /* Panel to display experiment details for selected data category node */
    private ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel = null;

    /*
     * Panel to display experiment details like Experiment Name, Created Date,
     * Last Modified Date
     */
    private Cab2bPanel experimentTitlePanel;

    private Cab2bButton addDataButton;

    /* user selected experiment object */
    private Experiment selectedExperiment = null;

    private ExperimentTreeNode m_ExperimentTreeNodeObj;

    private ExperimentDetailsPanel m_parentPanel;

    /* experiment BusinessInterface */
    private ExperimentBusinessInterface expBus;

    private JSplitPane splitPane;

    /**
     * @return the selectedExperiment
     */
    public Experiment getSelectedExperiment() {
        return selectedExperiment;
    }

    public ExperimentOpenPanel() {
        initGUI();
    }

    public ExperimentOpenPanel(ExperimentTreeNode Obj, ExperimentDetailsPanel parentPanel) {
        m_ExperimentTreeNodeObj = Obj;
        m_parentPanel = parentPanel;
        m_parentPanel.setBorder(null);
        initGUI();
    }

    public ExperimentDataCategoryGridPanel getExperimentDataCategoryGridPanel() {
        return this.experimentDataCategoryGridPanel;
    }

    public void initGUI() {
        this.setLayout(new BorderLayout());

        /* ejb code : Getting experiment BusinessInterface */
        expBus = (ExperimentBusinessInterface) CommonUtils.getBusinessInterface(EjbNamesConstants.EXPERIMENT,
                                                                                ExperimentHome.class);

        try {
            selectedExperiment = expBus.getExperiment(m_ExperimentTreeNodeObj.getIdentifier());
        } catch (RemoteException e) {
            CheckedException checkedException = new CheckedException(e.getMessage());
            CommonUtils.handleException(checkedException, m_parentPanel, true, true, true, false);
            e.printStackTrace();
        } catch (DAOException e) {
            CheckedException checkedException = new CheckedException(e.getMessage());
            CommonUtils.handleException(checkedException, m_parentPanel, true, true, true, false);
            e.printStackTrace();
        }
        // ejb code end

        experimentTitlePanel = new Cab2bPanel(new RiverLayout(5,5));

        /* Adding Experiment name */
        Cab2bLabel experimentLabel = new Cab2bLabel(selectedExperiment.getName());
        experimentLabel.setForeground(Color.blue);
        Font font = experimentLabel.getFont();
        Font textFont = new Font(font.getName(), Font.BOLD, font.getSize() + 3);
        experimentLabel.setFont(textFont);
        experimentTitlePanel.add("hfill", experimentLabel);

        /* Adding Experiment Creation Date */
        Cab2bLabel experimentCreatedOn = new Cab2bLabel("Created On :"
                + selectedExperiment.getCreatedOn().toString());
        experimentTitlePanel.add("tab tab tab tab hfill ", experimentCreatedOn);

        /* Adding Experiment Last modification Date */
        Cab2bLabel experimentModifiedOn = new Cab2bLabel("Last Updated :"
                + selectedExperiment.getLastUpdatedOn().toString());
        experimentTitlePanel.add("tab tab tab tab hfill", experimentModifiedOn);

        addDataButton = new Cab2bButton("Add Data");
        addDataButton.setEnabled(false);
        experimentTitlePanel.add(addDataButton);
        experimentTitlePanel.setBorder(null);

        /* Adding Experiment grid panel */
        experimentDataCategoryGridPanel = new ExperimentDataCategoryGridPanel(this);
        /*	experimentDataCategoryGridPanel.setBorder(new CustomizableBorder(
         new Insets(1, 1, 1, 1), true, true));*/
        experimentDataCategoryGridPanel.setBorder(null);

        /* Adding Experiment Stack box panel */
        this.add(experimentTitlePanel, BorderLayout.NORTH);
        experimentStackBox = new ExperimentStackBox(selectedExperiment, experimentDataCategoryGridPanel);
        /*experimentStackBox.setBorder(new CustomizableBorder(new Insets(1, 1, 1,
         1), true, true));*/
        experimentStackBox.setBorder(null);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, experimentStackBox,
                experimentDataCategoryGridPanel);

        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(242);
        splitPane.setDividerSize(4);
        this.add(splitPane);
    }

    public void addDataList(DataListMetadata dataListMetadata) {
        experimentStackBox.updateStackBox(dataListMetadata);
    }

    public static void main(String str[]) {
        ExperimentOpenPanel expDetPanel = null;
        expDetPanel = new ExperimentOpenPanel();
        JFrame frame = new JFrame("Experiment");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(expDetPanel);
        frame.setVisible(true);
    }

    /**
     * @return the experimentStackBox
     */
    public ExperimentStackBox getExperimentStackBox() {
        return experimentStackBox;
    }

    /**
     * @param experimentStackBox
     *            the experimentStackBox to set
     */
    public void setExperimentStackBox(ExperimentStackBox experimentStackBox) {
        this.experimentStackBox = experimentStackBox;
    }

}