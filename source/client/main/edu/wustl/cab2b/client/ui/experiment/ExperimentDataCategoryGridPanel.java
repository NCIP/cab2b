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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.filter.CaB2BFilterInterface;
import edu.wustl.cab2b.client.ui.filter.CaB2BPatternFilter;
import edu.wustl.cab2b.client.ui.filter.Cab2bFilterPopup;
import edu.wustl.cab2b.client.ui.filter.PatternPopup;
import edu.wustl.cab2b.client.ui.filter.RangeFilter;
import edu.wustl.cab2b.client.ui.filter.RangePopup;

/*
 * Class used to display selected Category records
 */

public class ExperimentDataCategoryGridPanel extends Cab2bPanel {

	/**
	 * @param args
	 */

	/* Tab component */
	JTabbedPane tabComponent;

	/*
	 * Panel to display experiment data when data category node is selected
	 * First tab panel on tab Component
	 */
	Cab2bPanel experimentDataPanel;

	/*
	 * Panel to display analysis performed on experiment Second tab panel on tab
	 * component
	 */
	Cab2bPanel analysisDataPanel;

	/* Button to save data category */
	Cab2bButton saveDataCategoryButton;

	Cab2bButton prevButton;

	/*
	 * Table to display records on Experiment Data panels, when user selects any
	 * data category node
	 */
	ExperimentTableModel table;

	Vector m_columnVector = new Vector();

	Vector m_dataRecordVector = new Vector();

	JScrollPane theScrollPane = new JScrollPane();

	private static Map<String, CaB2BFilterInterface> filterMap = new HashMap<String, CaB2BFilterInterface>();

	public ExperimentDataCategoryGridPanel() {
		initGUI();
	}

	/**
	 * @param columnName
	 * @param filter
	 */
	public static void addFilter(String columnName, CaB2BFilterInterface filter) {
		filterMap.put(columnName, filter);
	}
	
	public static void clearMap(){
	filterMap.clear();		
	}

	public ExperimentDataCategoryGridPanel(Vector columnVector,
			Vector dataRecordVector) {
		m_columnVector = columnVector;
		m_dataRecordVector = dataRecordVector;
		initGUI();
	}

	public void refreshTable(Object columnVector[],
			Object[][] dataRecordVector,
			Map<String, AttributeInterface> attributeMap) {
		this.removeAll();
		experimentDataPanel.removeAll();
		table = new ExperimentTableModel(false, dataRecordVector, columnVector,
				attributeMap);
		MouseListener mouseListener = new myMouseListener();
		// add the listener specifically to the header
		table.addMouseListener(mouseListener);
		table.getTableHeader().addMouseListener(mouseListener);
		theScrollPane.getViewport().add(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		experimentDataPanel.add("hfill vfill", theScrollPane);

		Cab2bPanel northPanel = new Cab2bPanel();
		northPanel.add(saveDataCategoryButton);
		this.add(northPanel, BorderLayout.NORTH);

		tabComponent.add("Experiment Data", experimentDataPanel);
		tabComponent.add("Analysis", analysisDataPanel);
		this.add(tabComponent, BorderLayout.CENTER);

		Cab2bPanel bottomPanel = new Cab2bPanel();
		bottomPanel.add(prevButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.updateUI();
	}

	public void initGUI() {
		this.setLayout(new BorderLayout());
		tabComponent = new JTabbedPane();
		tabComponent.setBorder(null);
		experimentDataPanel = new Cab2bPanel();
		experimentDataPanel.setBorder(null);
		analysisDataPanel = new Cab2bPanel();

		table = new ExperimentTableModel(false, m_dataRecordVector,
				m_columnVector);
		MouseListener mouseListener = new myMouseListener();
		// add the listener specifically to the header
		table.addMouseListener(mouseListener);
		table.getTableHeader().addMouseListener(mouseListener);

		/* Adding scrollpane */
		theScrollPane.getViewport().add(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		experimentDataPanel.add("hfill vfill", theScrollPane);

		saveDataCategoryButton = new Cab2bButton("Save Data Category");
		saveDataCategoryButton.setPreferredSize(new Dimension(160, 22));

		Cab2bPanel northPanel = new Cab2bPanel();
		northPanel.add(saveDataCategoryButton);
		this.add(northPanel, BorderLayout.NORTH);

		tabComponent.add("Experiment Data", experimentDataPanel);
		tabComponent.add("Analysis", analysisDataPanel);
		this.add(tabComponent, BorderLayout.CENTER);

		prevButton = new Cab2bButton("Previous");
		Cab2bPanel bottomPanel = new Cab2bPanel();
		bottomPanel.add(prevButton);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	/*
	 * Action listener for the header click for filteration purpose.
	 */
	class myMouseListener extends MouseAdapter {

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			Object obj = e.getComponent();

			if (obj instanceof JXTableHeader) {

				int columnIndex = ((JXTableHeader) e.getComponent())
						.columnAtPoint(e.getPoint());

				String dataType = table.getColumnAttribute(
						table.getColumnName(columnIndex)).getDataType();

				String columnName = table.getColumnName(columnIndex);
				CaB2BFilterInterface oldFilter = null;
				if (filterMap.containsKey(columnName)) {
					oldFilter = filterMap.get(columnName);
				}

				Cab2bFilterPopup filterPopup = null;

				// If the clicked column is of type String.
				if (dataType.equals("string")) {

					filterPopup = new PatternPopup(
							(CaB2BPatternFilter) oldFilter, columnName,
							columnIndex);
					// If the clicked column is of type int/long
				} else if (dataType.equals("long")) {

					filterPopup = new RangePopup((RangeFilter) oldFilter,
							columnName, columnIndex);
				}
				
				filterPopup.showInDialog();
				applyFilter();
			}

		}

		// Applying filter according to the the values in the map
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

}