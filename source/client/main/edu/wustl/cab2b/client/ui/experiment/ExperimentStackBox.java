package edu.wustl.cab2b.client.ui.experiment;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.ANALYZE_DATA;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.FILTER_DATA;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SELECT_DATA_CATEGORY;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.TREE_CLOSE_FOLDER;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.TREE_LEAF_NODE;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.TREE_OPEN_FOLDER;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.VISUALIZE_DATA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTree;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.client.cache.ClientSideCache;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bFormattedTextField;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.controls.sheet.ColumnFilterVerticalConsole;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.main.IComponent;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.util.ClientConstants;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.client.ui.util.UserObjectWrapper;
import edu.wustl.cab2b.client.ui.viewresults.DataListDetailedPanelInterface;
import edu.wustl.cab2b.client.ui.viewresults.DefaultSpreadSheetViewPanel;
import edu.wustl.cab2b.client.ui.visualization.charts.Cab2bChartPanel;
import edu.wustl.cab2b.client.ui.visualization.charts.ChartModel;
import edu.wustl.cab2b.client.ui.visualization.heatmap.HeatMapModel;
import edu.wustl.cab2b.client.ui.visualization.heatmap.HeatMapPanel;
import edu.wustl.cab2b.common.IdName;
import edu.wustl.cab2b.common.analyticalservice.ServiceDetailsInterface;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.DataListHomeInterface;
import edu.wustl.cab2b.common.domain.DataListMetadata;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.analyticalservice.AnalyticalServiceOperationsBusinessInterface;
import edu.wustl.cab2b.common.ejb.analyticalservice.AnalyticalServiceOperationsHomeInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityBusinessInterface;
import edu.wustl.cab2b.common.ejb.utility.UtilityHomeInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeHandler;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.queryengine.result.QueryResultFactory;
import edu.wustl.cab2b.common.queryengine.result.RecordId;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.Utility;

public class ExperimentStackBox extends Cab2bPanel {
    /** Default Serial version ID */
    private static final long serialVersionUID = 1L;

    /** panel to display filters on selected data-category */
    private Cab2bPanel dataFilterPanel = null;

    /** panel to display analysed data on selected data-category */
    private Cab2bPanel analyseDataPanel = null;

    /** panel to display visual form of data on selected data-category */
    private Cab2bPanel visualiseDataPanel = null;

    /** stack box to add all this panels */
    private StackedBox stackedBox;

    /** ExperimentOpen Panel */
    private ExperimentDataCategoryGridPanel m_experimentDataCategoryGridPanel = null;

    private JXTree datalistTree;

    DefaultMutableTreeNode rootNode;

    private boolean isDatalistContainsCategory = false;

    final static int CATEGORY_NODE_NO = 0;

    final static int CUSTOM_CATEGORY_NODE_NO = 1;

    private JScrollPane treeViewScrollPane;

    private Experiment m_selectedExperiment = null;

    private static int chartIndex = 0;

    private static int heatMapIndex = 0;

    private Cab2bButton customCategoryButton;

    private Cab2bPanel dataCategoryPanel;

    public ExperimentStackBox(Experiment selectedExperiment) {
        m_selectedExperiment = selectedExperiment;
        initGUI();
    }

    public ExperimentStackBox(
            Experiment selectedExperiment,
            ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel) {
        m_selectedExperiment = selectedExperiment;
        m_experimentDataCategoryGridPanel = experimentDataCategoryGridPanel;
        initGUI();
    }

    public void initGUI() {
        this.setLayout(new BorderLayout());
        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            DefaultMutableTreeNode rootNode = null;

            protected void doNonUILogic() throws RuntimeException {
                rootNode = generateRootNode(m_selectedExperiment);
            }

            protected void doUIUpdateLogic() throws RuntimeException {
                stackedBox = new StackedBox();
                stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
                initializeDataListTree(rootNode);
                initializePanels();
                if (datalistTree.getRowCount() >= 1) {
                    datalistTree.setSelectionRow(1);
                }
            }
        };
        swingWorker.start();
        updateUI();
    }

    /**
     * This method generates a root node given the set of entities
     * 
     * @param experiment
     *            set of entities
     * @return root node
     */
    private DefaultMutableTreeNode generateRootNode(Experiment experiment) {
        rootNode = new DefaultMutableTreeNode("Make it invisible");
        DefaultMutableTreeNode CategoriesRoot = new DefaultMutableTreeNode("Data Categories");
        DefaultMutableTreeNode customCategoriesRoot = null;

        // for each datalist
        for (DataListMetadata dataListMetadata : experiment.getDataListMetadataCollection()) {
            if (dataListMetadata.isCustomDataCategory()) {
                if (customCategoriesRoot == null) {
                    customCategoriesRoot = new DefaultMutableTreeNode("Custom Data Categories");
                }
                DefaultMutableTreeNode customCategoryNode = getNodeForCustomCategory(dataListMetadata);
                customCategoriesRoot.add(customCategoryNode);
                continue;
            }

            for (IdName idName : dataListMetadata.getEntitiesNames()) {
                EntityInterface originalEntity = ClientSideCache.getInstance().getEntityById(
                                                                                             idName.getOriginalEntityId());

                if (Utility.isCategory(originalEntity)) {
                    isDatalistContainsCategory = true;
                }

                UserObjectWrapper<IdName> categoryUserObject = new UserObjectWrapper<IdName>(idName,
                        Utility.getDisplayName(originalEntity));
                DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(categoryUserObject);
                CategoriesRoot.add(categoryNode);
            }
        }
        rootNode.add(CategoriesRoot);
        if (customCategoriesRoot != null) {
            rootNode.add(customCategoriesRoot);
        }
        return rootNode;
    }

    /**
     * This method initalize the datalist tree given the root node
     * 
     * @param rootNode
     *            the root node to be set.
     */
    private void initializeDataListTree(DefaultMutableTreeNode rootNode) {
        datalistTree = new JXTree(rootNode);
        // creating datalist tree
        datalistTree.setRootVisible(false);
        datalistTree.expandAll();

        // action listener for selected data category
        datalistTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                treeSelectionListenerAction();
            }
        });
        ClassLoader loader = this.getClass().getClassLoader();
        datalistTree.setOpenIcon(new ImageIcon(loader.getResource(TREE_OPEN_FOLDER)));
        datalistTree.setClosedIcon(new ImageIcon(loader.getResource(TREE_CLOSE_FOLDER)));
        datalistTree.setLeafIcon(new ImageIcon(loader.getResource(TREE_LEAF_NODE)));
        datalistTree.setBorder(null);

        customCategoryButton = new Cab2bButton("Custom Data Category");
        Cab2bPanel cab2bPanel = new Cab2bPanel();
        customCategoryButton.setPreferredSize(new Dimension(160, 22));
        customCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                CustomSwingWorker swingWorker = new CustomSwingWorker(
                        ExperimentStackBox.this.m_experimentDataCategoryGridPanel) {

                    @Override
                    protected void doNonUILogic() throws Exception {
                        if (isDatalistContainsCategory) {
                            JOptionPane.showMessageDialog(
                                                          ExperimentStackBox.this.m_experimentDataCategoryGridPanel,
                                                          ErrorCodeHandler.getErrorMessage(edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants.CUSTOM_CATEGORY_ERROR),
                                                          "Custom data category", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        CustomCategoryPanel categoryPanel = new CustomCategoryPanel(m_selectedExperiment);
                        if (categoryPanel.getDataListMetadata() != null)
                            updateStackBox(categoryPanel.getDataListMetadata());

                    }

                    @Override
                    protected void doUIUpdateLogic() throws Exception {
                        dataCategoryPanel.revalidate();
                    }
                };
                swingWorker.start();
            }
        });
        cab2bPanel.add(customCategoryButton);
        dataCategoryPanel = new Cab2bPanel(new RiverLayout(5, 5));
        dataCategoryPanel.add(cab2bPanel);
        dataCategoryPanel.add("br", datalistTree);
    }

    /**
     * This method initialzse the different UI panels and panes.
     */
    private void initializePanels() {
        // Adding Select data category pane
        treeViewScrollPane = new JScrollPane(dataCategoryPanel);
        treeViewScrollPane.getViewport().setBackground(Color.WHITE);
        treeViewScrollPane.setPreferredSize(new Dimension(250, 200));
        treeViewScrollPane.setBorder(null);
        stackedBox.addBox("Experiment Data", treeViewScrollPane, SELECT_DATA_CATEGORY, false);

        // Adding Filter data category panel
        dataFilterPanel = new Cab2bPanel(new RiverLayout(0, 5));
        dataFilterPanel.setPreferredSize(new Dimension(250, 200));
        dataFilterPanel.setOpaque(false);
        dataFilterPanel.setBorder(null);
        dataFilterPanel.add(new ColumnFilterVerticalConsole());
        stackedBox.addBox("Filters", dataFilterPanel, FILTER_DATA, true);

        // Adding Visualize data panel
        visualiseDataPanel = new Cab2bPanel(new RiverLayout(0, 5));
        visualiseDataPanel.setPreferredSize(new Dimension(250, 100));
        visualiseDataPanel.setOpaque(false);
        visualiseDataPanel.setBorder(null);
        visualiseDataPanel.removeAll();
        // Set the type of charts to be displayed.
        setBarChartLink();
        setLineChartLink();
        setScatterPlotLink();
        setHeatmapLink();
        stackedBox.addBox("Visualize Data ", visualiseDataPanel, VISUALIZE_DATA, true);

        // Adding Analyse data panel
        analyseDataPanel = new Cab2bPanel(new RiverLayout(0, 5));
        analyseDataPanel.setPreferredSize(new Dimension(250, 150));
        analyseDataPanel.setOpaque(false);
        analyseDataPanel.setBorder(null);
        stackedBox.addBox("Analyze Data ", analyseDataPanel, ANALYZE_DATA, true);

        stackedBox.setPreferredSize(new Dimension(250, 500));
        stackedBox.setMinimumSize(new Dimension(250, 500));
        stackedBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        stackedBox.revalidate();
        this.add(stackedBox);
        updateUI();
    }

    /**
     * Method to perform tree node selection action for currently selected node
     */
    public void treeSelectionListenerAction() {
        Cab2bPanel selectedPanel = (Cab2bPanel) m_experimentDataCategoryGridPanel.getTabComponent().getSelectedComponent();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) datalistTree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }

        Object nodeInfo = node.getUserObject();
        if (nodeInfo instanceof UserObjectWrapper) {
            updateSpreadSheet((UserObjectWrapper<IdName>) nodeInfo);
        }
        updateUI();
    }

    private void updateSpreadSheet(final UserObjectWrapper<IdName> idName) {
        CustomSwingWorker swingWorker = new CustomSwingWorker(this) {
            List<IRecord> recordList = null;

            List<TreeSet<Comparable<?>>> recordValues = null;

            protected void doNonUILogic() throws RuntimeException {
                try {
                    // getting datalist entity interface
                    DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                        EjbNamesConstants.DATALIST_BEAN,
                                                                                                                        DataListHomeInterface.class);

                    UtilityBusinessInterface utilityBusinessInterface = (UtilityBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                    EjbNamesConstants.UTILITY_BEAN,
                                                                                                                                    UtilityHomeInterface.class);
                    recordList = dataListBI.getEntityRecord(idName.getUserObject().getId());
                    recordValues = utilityBusinessInterface.getUniqueRecordValues(idName.getUserObject().getId());
                } catch (RemoteException remoteException) {
                    CommonUtils.handleException(remoteException, MainFrame.newWelcomePanel, true, true, true,
                                                false);
                }
                addAvailableAnalysisServices(idName.getUserObject());
            }

            protected void doUIUpdateLogic() throws RuntimeException {
                m_experimentDataCategoryGridPanel.refreshTable(recordList);
            }
        };
        swingWorker.start();
        updateUI();
    }

    public void setFilterPanel(Component filterConsole) {
        if (dataFilterPanel != null) {
            dataFilterPanel.removeAll();
            dataFilterPanel.add(" hfill vfill", filterConsole);
            dataFilterPanel.revalidate();
            dataFilterPanel.updateUI();
        }
    }

    private void setBarChartLink() {
        URL url = this.getClass().getClassLoader().getResource(ClientConstants.BAR_GRAPH);
        setVisulaizationToolLink(ClientConstants.BAR_CHART, url, new LinkActionListener(ClientConstants.BAR_CHART));
    }

    private void setLineChartLink() {
        URL url = this.getClass().getClassLoader().getResource(ClientConstants.LINE_GRAPH);
        setVisulaizationToolLink(ClientConstants.LINE_CHART, url, new LinkActionListener(
                ClientConstants.LINE_CHART));
    }

    private void setScatterPlotLink() {
        URL url = this.getClass().getClassLoader().getResource(ClientConstants.SCATTER_GRAPH);
        setVisulaizationToolLink(ClientConstants.SCATTER_PLOT, url, new LinkActionListener(
                ClientConstants.SCATTER_PLOT));
    }

    private void setHeatmapLink() {
        URL url = this.getClass().getClassLoader().getResource(ClientConstants.HEAT_MAP);
        setVisulaizationToolLink(ClientConstants.HEATMAP, url, new LinkActionListener(ClientConstants.HEATMAP));
    }

    /**
     * Method to set chart links enable/disable 
     * @param active
     */
    public void setChartLinkEnable(boolean active) {
        if (visualiseDataPanel == null)
            return;

        for (int i = 0; i < visualiseDataPanel.getComponentCount(); i++) {
            if (visualiseDataPanel.getComponent(i) instanceof Cab2bHyperlink) {
                ((Cab2bHyperlink) visualiseDataPanel.getComponent(i)).setEnabled(active);
            }
        }
    }

    /**
     * Method to set Heat map links enable/disable 
     * @param active
     */
    public void setHeatMapLinkEnable(boolean active) {
        if (visualiseDataPanel == null)
            return;
        if (visualiseDataPanel.getComponent(visualiseDataPanel.getComponentCount() - 1) instanceof Cab2bHyperlink) {
            ((Cab2bHyperlink) visualiseDataPanel.getComponent(visualiseDataPanel.getComponentCount() - 1)).setEnabled(active);
        }

    }

    /**
     * Method to set Heat map links enable/disable 
     * @param active
     */
    public void setAnalysisLinkEnable(boolean active) {
        if (analyseDataPanel == null)
            return;

        if (analyseDataPanel.getComponent(analyseDataPanel.getComponentCount() - 1) instanceof Cab2bHyperlink) {
            ((Cab2bHyperlink) analyseDataPanel.getComponent(analyseDataPanel.getComponentCount() - 1)).setEnabled(active);
        }
    }

    /**
     * This method displays the type of chart in the Visualize Data panel which
     * is at the left bottom of the screen.
     * 
     * @param chartTypes
     *            name of charts to be displayed.
     */
    private void setVisulaizationToolLink(String toolType, URL iconUrl, ActionListener actionListener) {
        Cab2bHyperlink hyperlink = new Cab2bHyperlink();
        hyperlink.setText(toolType);
        hyperlink.setActionCommand(toolType);

        if (iconUrl != null) {
            hyperlink.setIcon(new ImageIcon(iconUrl));
        }
        hyperlink.addActionListener(actionListener);
        hyperlink.setEnabled(false);
        visualiseDataPanel.add("br", hyperlink);
    }

    private class LinkActionListener implements ActionListener {
        private ActionEvent actionEvent = null;

        private String toolType;

        LinkActionListener(String toolType) {
            this.toolType = toolType;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            this.actionEvent = actionEvent;

            CustomSwingWorker customSwingWorker = new CustomSwingWorker(ExperimentStackBox.this) {
                @Override
                protected void doNonUILogic() {
                    String actionCommand = LinkActionListener.this.actionEvent.getActionCommand();
                    if (toolType.equals(ClientConstants.BAR_CHART) || toolType.equals(ClientConstants.LINE_CHART)
                            || toolType.equals(ClientConstants.SCATTER_PLOT)) {
                        showChartAction(actionCommand);
                    } else if (toolType.equals(ClientConstants.HEATMAP)) {
                        showHeatmapAction();
                    }
                }

                @Override
                protected void doUIUpdateLogic() {
                }
            };
            customSwingWorker.start();
            updateUI();
        };
    }

    /**
     * This method displays the chart selected in the Chart tab.
     * 
     * @param linkClicked
     *            the name of the chart to be displayed
     */
    private void showChartAction(String chartType) {
        final JTabbedPane tabComponent = m_experimentDataCategoryGridPanel.getTabComponent();
        Cab2bPanel currentChartPanel = m_experimentDataCategoryGridPanel.getCurrentChartPanel();
        if (currentChartPanel == null) {
            final Cab2bPanel visualizationTabPanel = createVisualizationTabPanel(tabComponent);

            DataListDetailedPanelInterface dataListDetailedPanel = m_experimentDataCategoryGridPanel.getCurrentSpreadSheetViewPanel();
            Cab2bChartPanel cab2bChartPanel = new Cab2bChartPanel();

            cab2bChartPanel.setModel(new ChartModel(dataListDetailedPanel.getSelectedData(),
                    dataListDetailedPanel.getSelectedRowNames(), dataListDetailedPanel.getSelectedColumnNames()));
            cab2bChartPanel.drawChart(chartType);

            visualizationTabPanel.add("br hfill vfill ", cab2bChartPanel);
            m_experimentDataCategoryGridPanel.setCurrentChartPanel(visualizationTabPanel);
            tabComponent.add("Chart" + ++chartIndex, visualizationTabPanel);
            tabComponent.setSelectedComponent(visualizationTabPanel);
        } else {
            Cab2bChartPanel cab2bChartPanel = (Cab2bChartPanel) CommonUtils.getComponentByName(currentChartPanel,
                                                                                               "cab2bChartPanel");
            cab2bChartPanel.drawChart(chartType);
            tabComponent.setSelectedComponent(currentChartPanel);
        }
    }

    private Cab2bPanel createVisualizationTabPanel(final JTabbedPane tabComponent) {
        final Cab2bPanel visualizationTabPanel = new Cab2bPanel();
        visualizationTabPanel.setBorder(null);

        Cab2bButton closeButton = new Cab2bButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                tabComponent.remove(visualizationTabPanel);
                m_experimentDataCategoryGridPanel.setCurrentChartPanel(null);
                chartIndex--;
            }
        });
        visualizationTabPanel.add("right ", closeButton);

        return visualizationTabPanel;
    }

    /**
     * This method displays the chart selected in the Chart tab.
     * 
     * @param linkClicked
     *            the name of the chart to be displayed
     */
    private void showHeatmapAction() {
        final JTabbedPane tabComponent = m_experimentDataCategoryGridPanel.getTabComponent();
        Cab2bPanel currentHeatMapPanel = m_experimentDataCategoryGridPanel.getCurrentHeatMapPanel();
        if (currentHeatMapPanel == null) {
            final Cab2bPanel visualizationTabPanel = createVisualizationTabPanel(tabComponent);

            HeatMapModel heatMapModel = new HeatMapModel();
            HeatMapPanel heatMapPanel = new HeatMapPanel();
            heatMapModel.addObserver(heatMapPanel);

            DataListDetailedPanelInterface dataListDetailedPanel = m_experimentDataCategoryGridPanel.getCurrentSpreadSheetViewPanel();
            heatMapModel.setData(dataListDetailedPanel);

            visualizationTabPanel.add("br hfill vfill ", heatMapPanel);
            m_experimentDataCategoryGridPanel.setCurrentChartPanel(visualizationTabPanel);
            tabComponent.add("HeatMap" + ++heatMapIndex, visualizationTabPanel);
            tabComponent.setSelectedComponent(visualizationTabPanel);
        } else {
            tabComponent.setSelectedComponent(currentHeatMapPanel);
        }
    }

    /**
     * update the tree, add the newly created entity to the current selection
     * 
     * @param dataListMetadata
     */
    public void updateStackBox(DataListMetadata dataListMetadata) {
        DefaultMutableTreeNode customCategoriesRoot = null;
        if (rootNode.getChildCount() == 1) {
            customCategoriesRoot = new DefaultMutableTreeNode("Custom Data Categories");
            rootNode.add(customCategoriesRoot);
        } else {
            customCategoriesRoot = (DefaultMutableTreeNode) rootNode.getChildAt(CUSTOM_CATEGORY_NODE_NO);
        }

        DefaultMutableTreeNode customCategoryNode = getNodeForCustomCategory(dataListMetadata);
        customCategoriesRoot.add(customCategoryNode);
        datalistTree.updateUI();
    }

    DefaultMutableTreeNode getNodeForCustomCategory(DataListMetadata dataListMetadata) {
        // Currently since custom data catrgories will have one and only one
        // entity, so hard coding
        IdName idName = dataListMetadata.getEntitiesNames().iterator().next();

        UserObjectWrapper<IdName> customCategoryUserObject = new UserObjectWrapper<IdName>(idName,
                dataListMetadata.getName());
        return new DefaultMutableTreeNode(customCategoryUserObject);

    }

    /**
     * This method adds all the available services as link in analysis panel
     * 
     * @param experimentEntity
     */
    private void addAvailableAnalysisServices(IdName idName) {
        // final EntityInterface dataEntity = idName.getEntityInterface();
        ClientSideCache clientSideCache = ClientSideCache.getInstance();
        EntityInterface dataEntity = clientSideCache.getEntityById(idName.getOriginalEntityId());
        AnalyticalServiceOperationsBusinessInterface analyticalServiceOperationsBusinessInterface = (AnalyticalServiceOperationsBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                                                                    EjbNamesConstants.ANALYTICAL_SERVICE_BEAN,
                                                                                                                                                                                    AnalyticalServiceOperationsHomeInterface.class,
                                                                                                                                                                                    null);
        List<ServiceDetailsInterface> serviceDetailInterfaceList = null;
        try {
            serviceDetailInterfaceList = analyticalServiceOperationsBusinessInterface.getApplicableAnalyticalServices(idName.getOriginalEntityId());
            if (analyseDataPanel != null) {
                analyseDataPanel.removeAll();
                for (ServiceDetailsInterface serviceDetails : serviceDetailInterfaceList) {
                    addHyperLinkToAnalyticalPane(serviceDetails, dataEntity);
                }
            }
        } catch (RemoteException remoteException) {
            CommonUtils.handleException(remoteException, MainFrame.newWelcomePanel, true, true, true, false);
        }
    }

    /**
     * This method adds a hyperlink to the Analytical Panel for the given
     * service
     * 
     * @param serviceDetails
     * @param dataEntity
     */
    private void addHyperLinkToAnalyticalPane(ServiceDetailsInterface serviceDetails,
                                              final EntityInterface dataEntity) {
        String serviceName = serviceDetails.getDisplayName();

        Cab2bHyperlink<ServiceDetailsInterface> hyperlink = new Cab2bHyperlink<ServiceDetailsInterface>();
        hyperlink.setBounds(new Rectangle(5, 5, 5, 5));
        hyperlink.setText(serviceName);
        hyperlink.setActionCommand(serviceName);
        hyperlink.setUserObject(serviceDetails);

        hyperlink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Cab2bHyperlink<ServiceDetailsInterface> hyperlink = (Cab2bHyperlink<ServiceDetailsInterface>) (actionEvent.getSource());

                ServiceDetailsInterface serviceDetails = hyperlink.getUserObject();
                List<EntityInterface> requiredEntityList = serviceDetails.getRequiredEntities();
                for (EntityInterface requiredEntity : requiredEntityList) {
                    // TODO compare with entity id instead of name
                    // if(entity.getId() != requiredEntity.getId() &&
                    // !requiredEntity.getAbstractAttributeCollection().isEmpty())
                    // {
                    if (!dataEntity.getName().equals(requiredEntity.getName())
                            && !requiredEntity.getAbstractAttributeCollection().isEmpty()) {
                        serviceSelectAction(serviceDetails, requiredEntity, dataEntity);
                    }
                }
            }
        });
        analyseDataPanel.add("br", hyperlink);
    }

    /**
     * The action listener for the individual service elements.
     * 
     * @param dataEntity
     * @param entityId
     * @param serviceDetailsInterface
     * @param actionEvent
     *            The event that contains details of the click on the individual
     *            service.
     */
    private void serviceSelectAction(ServiceDetailsInterface serviceDetails, final EntityInterface requiredEntity,
                                     final EntityInterface dataEntity) {
        // Create panel for Analysis Title
        JComponent tilteLabel = new Cab2bLabel("Analysis Title : ");
        JComponent titleField = new Cab2bFormattedTextField(20, Cab2bFormattedTextField.PLAIN_FIELD);
        Cab2bPanel titlePanel = new Cab2bPanel();
        titlePanel.setName("titlePanel");
        titlePanel.add("br", tilteLabel);
        titlePanel.add("tab", titleField);

        // Create panels for service parameters
        Cab2bPanel parameterPanel = getParameterPanels(requiredEntity);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setName("jScrollPane");
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jScrollPane.getViewport().add(parameterPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.getViewport().setBackground(Color.WHITE);

        // Create finish button
        Cab2bButton finishButton = new Cab2bButton("Finish");
        finishButton.addActionListener(new FinishButtonActionListner(serviceDetails, dataEntity, requiredEntity,
                m_experimentDataCategoryGridPanel));

        Cab2bPanel servicePanel = new Cab2bPanel(new RiverLayout(5, 5));
        servicePanel.add("br left ", titlePanel);
        servicePanel.add("br center hfill vfill ", jScrollPane);
        servicePanel.add("br right ", finishButton);

        String displayName = CommonUtils.getFormattedString(requiredEntity.getName());
        WindowUtilities.showInDialog(NewWelcomePanel.mainFrame, servicePanel, displayName,
                                     Constants.WIZARD_SIZE2_DIMENSION, true, false);
    }

    /**
     * This method returns the parameter panel that has the controls to accept
     * the parameter values.
     * 
     * @param entity
     *            the entity for which the control panels are to be displayed
     * @return Array of control panel
     */
    private Cab2bPanel getParameterPanels(final EntityInterface entity) {
        final Collection<AttributeInterface> attributeCollection = entity.getAttributeCollection();
        Cab2bPanel parameterPanel = new Cab2bPanel(new RiverLayout(10, 8));
        parameterPanel.setName("parameterPanel");

        try {
            ParseXMLFile parseFile = ParseXMLFile.getInstance();
            if (attributeCollection != null) {
                List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(attributeCollection);
                Collections.sort(attributeList, new AttributeInterfaceComparator());

                Dimension maxLabelDimension = CommonUtils.getMaximumLabelDimension(attributeList);
                for (AttributeInterface attribute : attributeList) {
                    AbstractTypePanel jxPanel = (AbstractTypePanel) SwingUIManager.generateUIPanel(parseFile,
                                                                                                   attribute,
                                                                                                   maxLabelDimension);
                    jxPanel.createPanelWithOperator(attribute);
                    parameterPanel.add("br", jxPanel);
                }
            }
        } catch (CheckedException checkedException) {
            CommonUtils.handleException(checkedException, this, true, false, false, false);
        }
        return parameterPanel;
    }

    /**
     * @return the visualiseDataPanel
     */
    public Cab2bPanel getVisualiseDataPanel() {
        return visualiseDataPanel;
    }

    /**
     * @param visualiseDataPanel
     *            the visualiseDataPanel to set
     */
    public void setVisualiseDataPanel(Cab2bPanel visualiseDataPanel) {
        this.visualiseDataPanel = visualiseDataPanel;
    }
}

/**
 * This Listener class adds a row in the table on the Analysis tab.
 * 
 * @author chetan_patil
 */
class FinishButtonActionListner implements ActionListener {
    private ServiceDetailsInterface serviceDetails;

    private EntityInterface dataEntity;

    private EntityInterface requiredEntity;

    private ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel;

    /**
     * Parameterized constructor
     * 
     * @param serviceDetails
     * @param dataEntity
     * @param experimentDataCategoryGridPanel
     */
    public FinishButtonActionListner(
            ServiceDetailsInterface serviceDetails,
            EntityInterface dataEntity,
            EntityInterface requiredEntity,
            ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel) {
        super();
        this.serviceDetails = serviceDetails;
        this.dataEntity = dataEntity;
        this.requiredEntity = requiredEntity;
        this.experimentDataCategoryGridPanel = experimentDataCategoryGridPanel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent actionEvent) {
        MainFrame.setStatus(MainFrame.Status.BUSY);
        MainFrame.setStatusMessage("Analysing the data...");

        Cab2bButton finishButton = (Cab2bButton) actionEvent.getSource();
        Cab2bPanel servicePanel = (Cab2bPanel) finishButton.getParent();
        closeDialogWindow(servicePanel);

        // Obtain the analysis title
        Cab2bPanel titlePanel = (Cab2bPanel) CommonUtils.getComponentByName(servicePanel, "titlePanel");
        Cab2bFormattedTextField titleField = (Cab2bFormattedTextField) titlePanel.getComponents()[1];
        String analysisTitle = titleField.getText();

        // Obtain values of service parameters
        JScrollPane jScrollPane = (JScrollPane) CommonUtils.getComponentByName(servicePanel, "jScrollPane");
        Cab2bPanel parameterPanel = (Cab2bPanel) CommonUtils.getComponentByName(jScrollPane.getViewport(),
                                                                                "parameterPanel");
        List<IRecord> serviceParameterList = collectServiceParameterValues(parameterPanel.getComponents());

        // Obtain table data
        DataListDetailedPanelInterface dataListDetailedPanelInterface = experimentDataCategoryGridPanel.getCurrentSpreadSheetViewPanel();
        if (dataListDetailedPanelInterface instanceof DefaultSpreadSheetViewPanel) {
            DefaultSpreadSheetViewPanel defaultSpreadSheetViewPanel = (DefaultSpreadSheetViewPanel) dataListDetailedPanelInterface;
            List<IRecord> recordList = defaultSpreadSheetViewPanel.getSelectedRecords();

            List<IRecord> entityRecordList = null;
            AnalyticalServiceOperationsBusinessInterface analyticalServiceOperationsBusinessInterface = (AnalyticalServiceOperationsBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                                                                        EjbNamesConstants.ANALYTICAL_SERVICE_BEAN,
                                                                                                                                                                                        AnalyticalServiceOperationsHomeInterface.class,
                                                                                                                                                                                        null);
            try {
                entityRecordList = analyticalServiceOperationsBusinessInterface.invokeService(serviceDetails,
                                                                                              recordList,
                                                                                              serviceParameterList);
                updateAnalysisTable(analysisTitle, entityRecordList);
            } catch (RemoteException remoteException) {
                CommonUtils.handleException(remoteException, MainFrame.newWelcomePanel, true, true, true, false);
            }

            MainFrame.setStatus(MainFrame.Status.READY);
            MainFrame.setStatusMessage("Analysis finished");
        }
    }

    /**
     * This method refreshes the table of analysis in the Analysis tab. It
     * displays the values of entityRecordResult in the tablular form.
     * 
     * @param analysisTitle
     *            the analysis title to display as link value in the table
     * @param entityRecordResult
     *            the EntityRecordResult whose values are to be displayed in a
     *            single row of table.
     */
    private void updateAnalysisTable(final String analysisTitle, final List<IRecord> entityRecordList) {
        UserObjectWrapper<List<IRecord>> userObjectWrapper = new UserObjectWrapper<List<IRecord>>(
                entityRecordList, analysisTitle);
        String entityName = Utility.getDisplayName(dataEntity);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = simpleDateFormat.format(new Date());
        String progress = "Completed";

        Object[][] analysisTableData = new Object[][] { { entityName, userObjectWrapper, currentDate, progress } };
        experimentDataCategoryGridPanel.refreshAnalysisTable(analysisTableData);
    }

    /**
     * This method closes the currnet dialog window
     * 
     * @param servicePanel
     */
    private void closeDialogWindow(final Cab2bPanel servicePanel) {
        Container container = servicePanel.getParent();
        container = container.getParent();
        container = container.getParent();
        container = container.getParent();
        if (container instanceof JDialog) {
            JDialog jDialog = (JDialog) container;
            jDialog.dispose();
        }
    }

    /**
     * This method collects the values from the service popup window
     * 
     * @param controlPanels
     *            parameter paneles of the service pop window
     * @return Record which has the parameter values
     */
    private List<IRecord> collectServiceParameterValues(final Component[] controlPanels) {
        List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(
                requiredEntity.getAttributeCollection());
        Collections.sort(attributeList, new AttributeInterfaceComparator());

        IRecord record = QueryResultFactory.createRecord(new HashSet<AttributeInterface>(attributeList),
                                                         new RecordId("-1", ""));

        int index = 0;
        for (AttributeInterface attribute : attributeList) {
            String value = null;
            ArrayList<String> parameterValues = ((IComponent) controlPanels[index++]).getValues();
            if (!parameterValues.isEmpty()) {
                value = parameterValues.get(0);
            }
            record.putValueForAttribute(attribute, value);
        }

        List<IRecord> parameterList = new Vector<IRecord>();
        parameterList.add(record);

        return parameterList;
    }

}
