/**
 * <p>Title: ExperimentDataCategoryGridPanel Class>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Hrishikesh Rajpathak/Deepak Shingan
 * @version 1.4
 */

package edu.wustl.cab2b.client.ui.experiment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.filter.CaB2BFilterInterface;
import edu.wustl.cab2b.client.ui.filter.CaB2BPatternFilter;
import edu.wustl.cab2b.client.ui.filter.Cab2bFilterPopup;
import edu.wustl.cab2b.client.ui.filter.EnumeratedFilterPopUp;
import edu.wustl.cab2b.client.ui.filter.FilterComponent;
import edu.wustl.cab2b.client.ui.filter.PatternPopup;
import edu.wustl.cab2b.client.ui.filter.RangeFilter;
import edu.wustl.cab2b.client.ui.mainframe.MainFrame;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.client.ui.util.CustomSwingWorker;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.experiment.ExperimentBusinessInterface;
import edu.wustl.cab2b.common.experiment.ExperimentHome;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.DataType;

/**
 * This class displays the experiment table. Also provides filtering tool for
 * the class
 * 
 * @author hrishikesh_rajpathak
 * @author deepak_shingan
 * 
 */
public class ExperimentDataCategoryGridPanel extends Cab2bPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ExperimentOpenPanel parent;

	private JTabbedPane tabComponent;

	/**
	 * Panel to display experiment data when data category node is selected
	 * First tab panel on tab Component
	 */
	private Cab2bPanel experimentDataPanel;

	/**
	 * Panel to display analysis performed on experiment Second tab panel on tab
	 * component
	 */
	private Cab2bPanel analysisDataPanel;
	
	/**
	 * Panel to display analysis performed on experiment Second tab panel on tab
	 * component
	 */
	private Cab2bPanel visualizeDataPanel;

	/** Button to save data category */
	private Cab2bButton saveDataCategoryButton;

	private Cab2bButton prevButton;

	/**
	 * Table to display records on Experiment Data panels, when user selects any
	 * data category node
	 */
	private ExperimentTableModel table;

	private Vector tableColumnVector;

	private Vector tableDataRecordVector;

	private JScrollPane theScrollPane = new JScrollPane();

	public static ArrayList<String> values = new ArrayList<String>();

	private static Map<String, CaB2BFilterInterface> filterMap = new HashMap<String, CaB2BFilterInterface>();

	//fields used by Save Data Category functionality
    private EntityInterface dataCategoryEntity;
    private String dataCategoryTitle;
    
    
    public ExperimentDataCategoryGridPanel(ExperimentOpenPanel parent)
	{

 	   this(new Vector(),new Vector());
       this.parent = parent;
	}

	/**
	 * Adding a filter to the filter map
	 */
	public static void addFilter(String columnName, CaB2BFilterInterface filter) {
		filterMap.put(columnName, filter);
	}

	public static void clearMap() {
		filterMap.clear();
	}

	public static Vector getFilterMap() {
		Vector<CaB2BFilterInterface> vector = new Vector<CaB2BFilterInterface>();
		for (CaB2BFilterInterface filter : filterMap.values()) {
			vector.add(filter);
		}
		return vector;
	}

	public ExperimentDataCategoryGridPanel(Vector columnVector, Vector dataRecordVector) {
		tableColumnVector = columnVector;
		tableDataRecordVector = dataRecordVector;
        clearMap();
		initGUI();
	}

	/**
	 * Building/refreshing the table
	 */
	public void refreshTable(Object columnVector[], Object[][] dataRecordVector,
			Map<String, AttributeInterface> attributeMap) {
		this.removeAll();
		experimentDataPanel.removeAll();
		table = new ExperimentTableModel(false, dataRecordVector, columnVector, attributeMap);
		table.setColumnSelectionAllowed(true);
		MouseListener mouseListener = new myMouseListener();
		// add the listener specifically to the header
		table.addMouseListener(mouseListener);
		table.getTableHeader().addMouseListener(mouseListener);

		theScrollPane.getViewport().add(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		experimentDataPanel.add("hfill vfill", theScrollPane);

		Cab2bPanel northPanel = new Cab2bPanel();
		northPanel.add(saveDataCategoryButton);
		this.add(northPanel, BorderLayout.NORTH);

		tabComponent.add("Experiment Data", experimentDataPanel);
		//tabComponent.add("Analysis", analysisDataPanel);
		tabComponent.add("Chart", visualizeDataPanel);
		this.add(tabComponent, BorderLayout.CENTER);

		Cab2bPanel bottomPanel = new Cab2bPanel();
		bottomPanel.add(prevButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.updateUI();
	}

	/**
	 * Initializing the GUI. Building the table initially.
	 */
	public void initGUI() {
		this.setLayout(new BorderLayout());
		tabComponent = new JTabbedPane();
		tabComponent.setBorder(null);
		experimentDataPanel = new Cab2bPanel();
		experimentDataPanel.setBorder(null);
		analysisDataPanel = new Cab2bPanel();
		visualizeDataPanel = new Cab2bPanel();

		
		table = new ExperimentTableModel(false, tableDataRecordVector, tableColumnVector);
		table.setColumnSelectionAllowed(true);
		MouseListener mouseListener = new myMouseListener();
		// add the listener specifically to the header
		table.addMouseListener(mouseListener);
		table.getTableHeader().addMouseListener(mouseListener);
		

		/* Adding scrollpane */
		theScrollPane.getViewport().add(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		experimentDataPanel.add("hfill vfill", theScrollPane);

		saveDataCategoryButton = new Cab2bButton("Save Data Category");
		saveDataCategoryButton.setPreferredSize(new Dimension(160, 22));
		saveDataCategoryButton.addActionListener(new SaveCategoryActionListener(this));

		Cab2bPanel northPanel = new Cab2bPanel();
		northPanel.add(saveDataCategoryButton);
		this.add(northPanel, BorderLayout.NORTH);

		tabComponent.add("Experiment Data", experimentDataPanel);
		//tabComponent.add("Analysis", analysisDataPanel);
		tabComponent.add("Chart", visualizeDataPanel);
		this.add(tabComponent, BorderLayout.CENTER);

		prevButton = new Cab2bButton("Previous");
		Cab2bPanel bottomPanel = new Cab2bPanel();
        prevButton.setEnabled(false);
		bottomPanel.add(prevButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Action listener for the header click for filteration purpose.
	 */
	class myMouseListener extends MouseAdapter {

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			Object obj = e.getComponent();

			if (obj instanceof JXTableHeader) {

				int columnIndex = ((JXTableHeader) e.getComponent()).columnAtPoint(e.getPoint());
				Cab2bFilterPopup filterPopup = null;
				String columnName = table.getColumnName(columnIndex);
				CaB2BFilterInterface oldFilter = null;
				if (filterMap.containsKey(columnName)) {
					oldFilter = filterMap.get(columnName);
				}
				AttributeInterface attribute = table.getColumnAttribute(columnName);
				if (Utility.isEnumerated(attribute)) {
					Collection<PermissibleValueInterface> permissibleValueCollection = Utility
							.getPermissibleValues(attribute);
					filterPopup = new EnumeratedFilterPopUp(columnName, columnIndex,
							permissibleValueCollection, (CaB2BPatternFilter) oldFilter);
				} else {
					DataType dataType = Utility
							.getDataType(attribute.getAttributeTypeInformation());
					// If the clicked column is of type String.
					if (DataType.String == dataType) {

						filterPopup = new PatternPopup((CaB2BPatternFilter) oldFilter, columnName,
								columnIndex);
						// If the clicked column is of type int/long
					} else if (DataType.Long == dataType || DataType.Integer == dataType
							|| DataType.Double == dataType || DataType.Float == dataType) {
						int len = table.getRowCount();
						double columnVal[] = null;
						if (oldFilter == null) {
							columnVal = new double[len];
							for (int i = 0; i < len; i++) {
								String val = (String) table.getValueAt(i, columnIndex);
								if (val != null) {
									double f = Double.parseDouble(val);
									columnVal[i] = f;
								}

							}
						}
						filterPopup = new FilterComponent("Range Filter", columnVal, columnName,
								columnIndex, (RangeFilter) oldFilter);
					}

				}
				filterPopup.showInDialog();
				applyFilter();
			}

		}

		/**
		 * Applying filter according to the the values in the map
		 */
		public void applyFilter() {
			int len = filterMap.size();
			Filter[] filters = new Filter[len];
			int i = 0;
			for (CaB2BFilterInterface filter : filterMap.values()) {
				filters[i] = (Filter) filter.copy();
				i++;
			}
			table.setFilters(new FilterPipeline(filters));
		}
	}

	/**
	 * @return the tabComponent
	 */
	public JTabbedPane getTabComponent() {
		return tabComponent;
	}

	/**
	 * @param tabComponent the tabComponent to set
	 */
	public void setTabComponent(JTabbedPane tabComponent) {
		this.tabComponent = tabComponent;
	}

	/**
	 * @return the table
	 */
	public ExperimentTableModel getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(ExperimentTableModel table) {
		this.table = table;
	}

	/**
	 * @return the visualizeDataPanel
	 */
	public Cab2bPanel getVisualizeDataPanel() {
		return visualizeDataPanel;
	}

	
	/**
	 * @param visualizeDataPanel the visualizeDataPanel to set
	 */
	public void setVisualizeDataPanel(Cab2bPanel visualizeDataPanel) {
		this.visualizeDataPanel = visualizeDataPanel;
        
	}
	
	
	/**
	 * make list of attributes of the parent entity as well as a 2D array of data and pass it to the server to make new entity
	 * this method is called by the {@link SaveDataCategoryPanel} to save a subset of of a datalist as a category 
	 * @param title the title for the category 
	 */
	public void saveDataCategory(String title)
	{
        dataCategoryTitle = title;
        
        MainFrame.setStatus(MainFrame.Status.BUSY);
        MainFrame.setStatusMessage("saving data category '"+title+"'");
                        
        CustomSwingWorker swingWorker = new CustomSwingWorker(MainFrame.openExperimentWelcomePanel)
        {
            
            @Override
            protected void doNonUILogic() throws RuntimeException
            {
                //get the columns, including hidden columns. Not well documented
                List<TableColumn> columnList = table.getColumns(true);
                List<AttributeInterface> attributes = new ArrayList<AttributeInterface>();
                
                //skip filterd-out rows but include hidden columns 
                int rows = table.getRowCount();
                int cols = table.getColumnCount(true);
                Object[][] data =new Object[rows][cols];
                
                //populate attribute list
                for(TableColumn column : columnList)
                {
                    String columnName = column.getIdentifier().toString();  
                    attributes.add(table.getColumnAttribute(columnName));
                }
                
                for(int i=0; i<rows; i++)
                {
                    for(int j=0; j<cols; j++)
                    {
                        //JXTable does not provide API to access hidden data
                        //JXTable.getValueAt() works only on visible columns
                        //using TableModel.getValueAt() requires converting row view index to row model index 
                        data[i][j] = table.getModel().getValueAt(table.convertRowIndexToModel(i), j);
                    }
                    
                    //Logger.out.info(table.getValueAt(i, 4).toString());
                }
                
                //make a call to the server
                ExperimentBusinessInterface bi = (ExperimentBusinessInterface)CommonUtils.getBusinessInterface(EjbNamesConstants.EXPERIMENT, ExperimentHome.class);
                
                try
                {
                    dataCategoryEntity = bi.saveDataCategory(dataCategoryTitle, attributes, data);
                }
                catch(RemoteException e)
                {
                    CommonUtils.handleException(e, MainFrame.newWelcomePanel, true, true, true, false);          
                }
            }

            @Override
            protected void doUIUpdateLogic() throws RuntimeException 
            {
                //update the tree in the stack box
                parent.updateOpenPanel(dataCategoryEntity);
                MainFrame.setStatus(MainFrame.Status.READY);
                MainFrame.setStatusMessage(dataCategoryTitle+" saved");
                
                
                          
            }
        };
        
		swingWorker.start();
		
	}
	
	
	class SaveCategoryActionListener implements ActionListener
	{
		private ExperimentDataCategoryGridPanel gridPanel;
		
		public SaveCategoryActionListener(ExperimentDataCategoryGridPanel gridPanel)
		{
			this.gridPanel = gridPanel;
			
		}
		
		public void actionPerformed(ActionEvent event)
		{
			SaveDataCategoryPanel saveDialogPanel = new SaveDataCategoryPanel(gridPanel);
		}
		
	}

}