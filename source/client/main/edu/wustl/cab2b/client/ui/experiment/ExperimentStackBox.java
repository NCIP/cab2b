package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTree;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordInterface;
import edu.common.dynamicextensions.entitymanager.EntityRecordResultInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.charts.ChartGenerator;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.charts.ChartGenerator;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.cab2b.client.ui.filter.CaB2BFilterInterface;
import edu.wustl.cab2b.client.ui.filter.CaB2BPatternFilter;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.datalist.DataListBusinessInterface;
import edu.wustl.cab2b.common.datalist.DataListHomeInterface;
import edu.wustl.cab2b.common.domain.Experiment;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.common.util.logger.Logger;

/*
 *  Class used to display left hand side stack panel.
 * */

public class ExperimentStackBox extends Cab2bPanel {

    /**
     * @param args
     */

    /*panel to display data-list (flat structure PPT slide no :44 )*/
    Cab2bPanel dataCategoryPanel = null;

    /*panel to display filters on selected data-category*/
    static Cab2bPanel dataFilterPanel = null;

    /*panel to display analysed data on selected data-category*/
    Cab2bPanel analyseDataPanel = null;

    Cab2bPanel visualiseDataPanel = null;

    Cab2bPanel dataViewPanel = null;

    /*stack box to add all this panels*/
    StackedBox stackedBox;

    /*ExperimentOpen Panel*/
    ExperimentDataCategoryGridPanel m_experimentDataCategoryGridPanel = null;

    JXTree datalistTree;

    JScrollPane treeViewScrollPane;

    ExperimentBusinessInterface m_experimentBusinessInterface;

    Experiment m_selectedExperiment = null;

    String columnName[] = null;
    
    static CaB2BFilterInterface obj=null;

    Object recordObject[][] = null;
    Map<String, AttributeInterface> attributeMap = new HashMap<String, AttributeInterface>();

    public ExperimentStackBox(ExperimentBusinessInterface expBus, Experiment selectedExperiment) {
        m_experimentBusinessInterface = expBus;
        m_selectedExperiment = selectedExperiment;
        initGUI();
    }

    public ExperimentStackBox(
            ExperimentBusinessInterface expBus,
            Experiment selectedExperiment,
            ExperimentDataCategoryGridPanel experimentDataCategoryGridPanel) {
        m_experimentBusinessInterface = expBus;
        m_selectedExperiment = selectedExperiment;
        m_experimentDataCategoryGridPanel = experimentDataCategoryGridPanel;
        initGUI();
    }
    
    public void initGUI() {
        this.setLayout(new BorderLayout());
        stackedBox = new StackedBox();
        stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));

        Set<EntityInterface> entitySet = null;
        try {
            entitySet = m_experimentBusinessInterface.getDataListEntityNames(m_selectedExperiment);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Iterator iter = entitySet.iterator();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Experiment Data Categories");
        DefaultMutableTreeNode node = null;

        while (iter.hasNext()) {
            EntityInterface entity = (EntityInterface) iter.next();
            ExperimentEntity expEnity = new ExperimentEntity();
            expEnity.setEntityInterface(entity);
            node = new DefaultMutableTreeNode(expEnity);
            rootNode.add(node);
        }

        //creating datalist tree
        datalistTree = new JXTree(rootNode);

        //setting the first node as selected and displaying the corresponding records 
        //in the table  
        if (datalistTree.getRowCount() >= 2) {
            datalistTree.setSelectionRow(1);
            datalistTree.expandRow(1);
            CustomSwingWorker swingWorker = new CustomSwingWorker(datalistTree) {
                @Override
                protected void doNonUILogic() throws RuntimeException {

                    Object nodeInfo = ((DefaultMutableTreeNode) datalistTree.getLastSelectedPathComponent()).getUserObject();
                    if (nodeInfo instanceof ExperimentEntity) {
                        getDataCategoyRecords((ExperimentEntity) nodeInfo);
                    }
                }

                @Override
                protected void doUIUpdateLogic() throws RuntimeException {
                    m_experimentDataCategoryGridPanel.refreshTable(columnName, recordObject,attributeMap);
                }
            };
            swingWorker.start();
        }

        //action listener for selected data category 
        datalistTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                treeSelectionListenerAction();
            }
        });

        //Adding Select data category pane		
        treeViewScrollPane = new JScrollPane(datalistTree);
        stackedBox.addBox("Select Data Category", treeViewScrollPane, "resources/images/mysearchqueries_icon.gif");

        //Adding Filter data category panel
        dataFilterPanel = new Cab2bPanel();
        dataFilterPanel.setPreferredSize(new Dimension(250, 150));
        dataFilterPanel.setOpaque(false);
        
       
       
        stackedBox.addBox("Filter Data ", dataFilterPanel, "resources/images/mysearchqueries_icon.gif");

        /**
    	 * TODO Uncomment the code lines of adding analysisDataPanel when implementing this feaature.
    	 */
       /* //Adding Analyse data  panel
        analyseDataPanel = new Cab2bPanel();
        analyseDataPanel.setPreferredSize(new Dimension(250, 150));
        analyseDataPanel.setOpaque(false);
        stackedBox.addBox("Analyze Data ", analyseDataPanel, "resources/images/mysearchqueries_icon.gif");*/
        
        //Adding Visualize data panel
        visualiseDataPanel = new Cab2bPanel();
        visualiseDataPanel.setPreferredSize(new Dimension(250, 150));
        visualiseDataPanel.setOpaque(false);
        stackedBox.addBox("Visualize Data ", visualiseDataPanel, "resources/images/mysearchqueries_icon.gif");
        
        //Set the type of charts to be displayed.
        Vector<String> chartTypes = new Vector<String>();
        chartTypes.add(Constants.BAR_CHART);
        chartTypes.add(Constants.LINE_CHART);
        chartTypes.add(Constants.SCATTER_PLOT);
        setChartTypesForVisualiseDataPanel(chartTypes);

        stackedBox.setPreferredSize(new Dimension(250, 500));
        stackedBox.setMinimumSize(new Dimension(250, 500));
        this.add(stackedBox);
    }

    /**
     * Method to perform tree node selection action
     * for currently selected node 
     */

    public void treeSelectionListenerAction() {

        CustomSwingWorker swingWorker = new CustomSwingWorker(datalistTree) {
            @Override
            protected void doNonUILogic() throws RuntimeException {
                Logger.out.info("Clicked on datalist");
                ExperimentDataCategoryGridPanel.clearMap();
                getDataForFilterPanel();
                updateUI();
               
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) datalistTree.getLastSelectedPathComponent();
                if (node == null)
                    return;

                //if rot node is selected, clear all table content 
                if (node.isRoot() == true) {
                    columnName = null;
                    recordObject = null;
                }

                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof ExperimentEntity) {
                    getDataCategoyRecords((ExperimentEntity) nodeInfo);
                }

            }

            protected void doUIUpdateLogic() throws RuntimeException {
                m_experimentDataCategoryGridPanel.refreshTable(columnName, recordObject,attributeMap);
            }
        };
        swingWorker.start();

      
    }
    /*Method used to get dataCategory records from database
     * and covert it into table format
     * */

    private void getDataCategoyRecords(ExperimentEntity nodeInfo) {

        EntityInterface entityNode = nodeInfo.getEntityInterface();
        entityNode.getAttributeCollection();
        Logger.out.info("ID :: " + entityNode.getId());

        //getting datalist entity interface
        DataListBusinessInterface dataListBI = (DataListBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                            EjbNamesConstants.DATALIST_BEAN,
                                                                                                            DataListHomeInterface.class);
        try {
            //getting list of attributes
            EntityRecordResultInterface recordResultInterface = dataListBI.getEntityRecord(entityNode.getId());
            List<AbstractAttributeInterface> headerList = recordResultInterface.getEntityRecordMetadata().getAttributeList();
            Iterator it = headerList.iterator();
            columnName = new String[headerList.size()];
            int i = 0;
            while (it.hasNext()) {
                AttributeInterface attribute = (AttributeInterface) it.next();
                
                columnName[i++] = CommonUtils.getFormattedString(attribute.getName());
                attributeMap.put(columnName[i-1],attribute);
                Logger.out.info("Table Header :" + attribute.getName());
            }

            //getting actual records
            List<EntityRecordInterface> recordList = recordResultInterface.getEntityRecordList();
            it = recordList.iterator();
            recordObject = new Object[recordList.size()][headerList.size()];
            Logger.out.info("Record Size :: " + recordList.size());
            i = 0;
            while (it.hasNext()) {
                EntityRecordInterface record = (EntityRecordInterface) it.next();
                List recordValueList = record.getRecordValueList();
                int j = 0;
                Iterator iterList = recordValueList.iterator();
                while (iterList.hasNext()) {
                    recordObject[i][j] = new Object();
                    recordObject[i][j] = iterList.next();
                    Logger.out.info("Data [" + i + "]" + "[" + j + "]" + recordObject[i][j]);
                    j++;
                }
                i++;
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

    }
    
    public static void setDataForFilterPanel(Vector data) {

        Logger.out.info("setDataForMyExperimentsPanel :: data " + data);
        dataFilterPanel.removeAll();
        dataFilterPanel.add(new Cab2bLabel());
        Iterator iter = data.iterator();
        while (iter.hasNext()) {
        	obj = (CaB2BFilterInterface)iter.next();
            String hyperlinkName = obj.toString();
            Cab2bHyperlink hyperlink = new Cab2bHyperlink();
            hyperlink.setBounds(new Rectangle(5, 5, 5, 5));
            hyperlink.setText(hyperlinkName);
            hyperlink.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Logger.out.info("Clicked on expt link");
                    System.out.println(obj.getClass());
                    if(obj instanceof CaB2BPatternFilter){
                    	CaB2BPatternFilter filter=(CaB2BPatternFilter)obj;
                    	System.out.println(filter.getPattern().pattern());
                    }
                    	
                }
            });
            dataFilterPanel.add("br", hyperlink);
        }
        dataFilterPanel.revalidate();
    }
    
    public static void getDataForFilterPanel(){
    	 Vector<CaB2BFilterInterface> vector=new Vector<CaB2BFilterInterface>();
         vector=ExperimentDataCategoryGridPanel.getFilterMap();
         if(vector!=null){
         setDataForFilterPanel(vector);
         }
    }

    
    /**
     * This method displays the type of chart in the Visualize Data panel which is at the left bottom of the screen.
     * @param chartTypes name of charts to be displayed.
     */
    private void setChartTypesForVisualiseDataPanel(Vector<String> chartTypes) {
        Logger.out.info("setChartTypesForVisualiseDataPanel :: chartTypes " + chartTypes);
        visualiseDataPanel.removeAll();
        
        for(String hyperlinkName : chartTypes) {
            Cab2bHyperlink hyperlink = new Cab2bHyperlink();
            hyperlink.setBounds(new Rectangle(5, 5, 5, 5));
            hyperlink.setText(hyperlinkName);
            hyperlink.setActionCommand(hyperlinkName);
            
            hyperlink.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                	String linkClicked = actionEvent.getActionCommand();
                	Logger.out.info("Clicked on " + linkClicked + " link");
                    showChartAction(linkClicked);
                    updateUI();
                }
            });
            visualiseDataPanel.add("br", hyperlink);
        }
        visualiseDataPanel.revalidate();
    }
    
    /**
     * This method displays the chart selected in the Chart tab.
     * @param linkClicked the name of the chart to be displayed
     */
	private void showChartAction(String linkClicked) {
		Cab2bTable cab2bTable = m_experimentDataCategoryGridPanel.getTable();
    	
		String entityName = null;
		Object nodeInfo = ((DefaultMutableTreeNode) datalistTree.getLastSelectedPathComponent()).getUserObject();
		if (nodeInfo instanceof ExperimentEntity) {
			entityName = ((ExperimentEntity)nodeInfo).toString();
		}
    	
		JPanel jPanel = null;
        ChartGenerator chartGenerator = new ChartGenerator(cab2bTable);
        if(linkClicked.equals(Constants.BAR_CHART)) {
        	jPanel = chartGenerator.getBarChart(entityName);
        } else if(linkClicked.equals(Constants.LINE_CHART)) {
        	jPanel = chartGenerator.getLineChart();
        } else if(linkClicked.equals(Constants.SCATTER_PLOT)) {
        	jPanel = chartGenerator.getScatterPlot();
        }
        
    	Cab2bPanel visualizeDataPanel = m_experimentDataCategoryGridPanel.getVisualizeDataPanel();
    	visualizeDataPanel.removeAll();
    	visualizeDataPanel.add(jPanel);
    	
    	JTabbedPane tabComponent = m_experimentDataCategoryGridPanel.getTabComponent();
    	tabComponent.setSelectedComponent(visualizeDataPanel);
    }
    
}