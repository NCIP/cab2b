package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.sheet.JSheet;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.viewresults.DefaultSpreadSheetViewPanel;
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

        experimentTitlePanel = new Cab2bPanel(new RiverLayout(5, 5));

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

        experimentTitlePanel.add("br", new JLabel(""));
        experimentTitlePanel.setBorder(null);

        /* Adding Experiment grid panel */
        experimentDataCategoryGridPanel = new ExperimentDataCategoryGridPanel(this);
        experimentDataCategoryGridPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.SPREADSHEET_MODEL_INSTALLED)) {
                    Cab2bPanel filterPanel = new Cab2bPanel();
                    JSheet jSheet = (JSheet) evt.getNewValue();
                    filterPanel.add("hfill vfill", jSheet.getContextFilterConsole());

                    JTabbedPane tabComponent = new JTabbedPane();
                    tabComponent.setTabLayoutPolicy(1);
                    tabComponent.setBackground(Color.WHITE);
                    tabComponent.add("Custom ", filterPanel);
                    tabComponent.add("Applied ", jSheet.getFiltersViewConsole());
                    tabComponent.add("Predefined ", new Cab2bLabel("No Predefined Filter"));

                    experimentStackBox.setFilterPanel(tabComponent);
                    experimentStackBox.setChartLinkEnable(false);
                }
                if (evt.getPropertyName().equals(JSheet.EVENT_DATA_SINGLE_CLICKED)) {
                    experimentStackBox.setChartLinkEnable(true);  
                    experimentStackBox.setHeatMapLinkEnable(false);
                } else if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.DISABLE_CHART_LINK)) {
                    experimentStackBox.setChartLinkEnable(false);
                } else if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.ENABLE_CHART_LINK)) {
                    experimentStackBox.setChartLinkEnable(true);
                    experimentStackBox.setHeatMapLinkEnable(false);
                }
                if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.DISABLE_HEATMAP_LINK)) {
                    experimentStackBox.setHeatMapLinkEnable(false);
                } else if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.ENABLE_HEATMAP_LINK)) {
                    experimentStackBox.setHeatMapLinkEnable(true);
                }
            }
        });
        experimentDataCategoryGridPanel.setBorder(null);

        /* Adding Experiment Stack box panel */
        this.add(experimentTitlePanel, BorderLayout.NORTH);
        experimentStackBox = new ExperimentStackBox(selectedExperiment, experimentDataCategoryGridPanel);
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