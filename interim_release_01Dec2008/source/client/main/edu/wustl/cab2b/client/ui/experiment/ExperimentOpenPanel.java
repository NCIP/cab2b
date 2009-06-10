package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.controls.RiverLayout;
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

    private Container parentPanel;

    private JSplitPane splitPane;

    /**
     * @return the selectedExperiment
     */
    public Experiment getSelectedExperiment() {
        return selectedExperiment;
    }

    /**
     * 
     */
    public ExperimentOpenPanel() {
        initGUI();
    }

    /**
     * @param exp
     */
    public ExperimentOpenPanel(Experiment exp) {
        selectedExperiment = exp;
        initGUI();
    }

    /**
     * @param Obj
     * @param parentPanel
     */
    public ExperimentOpenPanel(ExperimentTreeNode Obj, Container parentPanel) {
        m_ExperimentTreeNodeObj = Obj;
        this.parentPanel = parentPanel;
        initGUI();
    }

    /**
     * Returns ExperimentDataCategoryGridPanel
     * @return ExperimentDataCategoryGridPanel
     */
    public ExperimentDataCategoryGridPanel getExperimentDataCategoryGridPanel() {
        return this.experimentDataCategoryGridPanel;
    }

    /**
     * Method to set experiment panel
     */
    private void setExperimentPanel() {

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
                    tabComponent.setOpaque(false);
                    tabComponent.setTabLayoutPolicy(1);
                    tabComponent.setBackground(Color.WHITE);
                    tabComponent.add("Custom ", filterPanel);
                    tabComponent.add("Applied ", jSheet.getFiltersViewConsole());
                    JLabel noFilterLabel = new Cab2bLabel("No Predefined Filter");
                    noFilterLabel.setOpaque(true);
                    noFilterLabel.setFocusable(false);
                    noFilterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    Font font = new Font(noFilterLabel.getFont().getFontName(), Font.BOLD,
                            noFilterLabel.getFont().getSize());
                    noFilterLabel.setFont(font);
                    tabComponent.add("Predefined ", noFilterLabel);

                    experimentStackBox.setFilterPanel(tabComponent);
                    if (jSheet.getSelectedColumns().length > 0) {
                        experimentStackBox.setChartLinkEnable(true);
                    } else
                        experimentStackBox.setChartLinkEnable(false);
                    experimentStackBox.setHeatMapLinkEnable(false);
                }

                if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.SPREADSHEET_MODEL_UNINSTALLED)) {
                    JLabel noFilterLabel = new Cab2bLabel("No Filter");
                    noFilterLabel.setOpaque(true);
                    noFilterLabel.setFocusable(false);
                    noFilterLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    Font font = new Font(noFilterLabel.getFont().getFontName(), Font.BOLD,
                            noFilterLabel.getFont().getSize());
                    noFilterLabel.setFont(font);
                    Cab2bPanel panel = new Cab2bPanel();
                    panel.setLayout(new BorderLayout());
                    panel.add(noFilterLabel);
                    experimentStackBox.setFilterPanel(panel);
                }

                if (evt.getPropertyName().equals(JSheet.EVENT_DATA_SINGLE_CLICKED)) {
                    experimentStackBox.setChartLinkEnable(true);
                    experimentStackBox.setHeatMapLinkEnable(false);
                    experimentStackBox.setAnalysisLinkEnable(true);
                } else if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.DISABLE_CHART_LINK)) {
                    experimentStackBox.setChartLinkEnable(false);
                    experimentStackBox.setAnalysisLinkEnable(false);
                } else if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.ENABLE_CHART_LINK)) {
                    experimentStackBox.setChartLinkEnable(true);
                    experimentStackBox.setHeatMapLinkEnable(false);
                    experimentStackBox.setAnalysisLinkEnable(true);
                }
                if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.DISABLE_HEATMAP_LINK)) {
                    experimentStackBox.setHeatMapLinkEnable(false);
                } else if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.ENABLE_HEATMAP_LINK)) {
                    experimentStackBox.setHeatMapLinkEnable(true);
                }
                if (evt.getPropertyName().equals(DefaultSpreadSheetViewPanel.DISABLE_ANALYSIS_LINK))
                    experimentStackBox.setAnalysisLinkEnable(false);
            }
        });
        experimentDataCategoryGridPanel.setBorder(null);

        /* Adding Experiment Stack box panel */
        this.add(experimentTitlePanel, BorderLayout.NORTH);
        experimentStackBox = new ExperimentStackBox(selectedExperiment, experimentDataCategoryGridPanel);
        experimentStackBox.setBorder(null);
        experimentStackBox.revalidate();

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, experimentStackBox,
                experimentDataCategoryGridPanel);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(242);
        splitPane.setDividerSize(4);
        this.add(splitPane);

    }

    /**
     * Method for getting experiment details from database 
     * @return
     */
    private Experiment invokeCompleteExperimentFromDatabase() {

        /* ejb code : Getting experiment BusinessInterface */
        ExperimentBusinessInterface expBus = (ExperimentBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                            EjbNamesConstants.EXPERIMENT,
                                                                                                            ExperimentHome.class);
        Long expId;

        if (selectedExperiment != null)
            expId = selectedExperiment.getId();
        else
            expId = m_ExperimentTreeNodeObj.getIdentifier();

        try {
            selectedExperiment = expBus.getExperiment(expId);
        } catch (RemoteException e) {
            CheckedException checkedException = new CheckedException(e.getMessage());
            CommonUtils.handleException(checkedException, parentPanel, true, true, true, false);
            e.printStackTrace();
        } catch (DAOException e) {
            CheckedException checkedException = new CheckedException(e.getMessage());
            CommonUtils.handleException(checkedException, parentPanel, true, true, true, false);
            e.printStackTrace();
        }
        return selectedExperiment;

    }

    /**
     * GUI initilisation method
     */
    public void initGUI() {
        this.setLayout(new BorderLayout());
        invokeCompleteExperimentFromDatabase();
        setExperimentPanel();
    }

    /**
     * Method to add datalist on experiment stack box panel 
     * @param dataListMetadata
     */
    public void addDataList(DataListMetadata dataListMetadata) {
        experimentStackBox.updateStackBox(dataListMetadata);
    }

    /**
     * Returns  ExperimentStackBox panel
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