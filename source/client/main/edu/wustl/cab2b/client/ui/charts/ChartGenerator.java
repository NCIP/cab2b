/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * This class is resonsible of providing the methods to generate the different charts.
 * @author chetan_patil
 */
public class ChartGenerator {
	/**
	 * Holds the cab2bTable
	 */
	private Cab2bTable cab2bTable;
	
	/**
	 * Parameterized contructor
	 * @param cab2bTable
	 */
	public ChartGenerator(Cab2bTable cab2bTable) {
		super();
		this.cab2bTable = cab2bTable;
	}
	
	/**
	 * This method returns the JPanel consisting of generated Bar Chart
	 * @param entityName label to be displayed for x-axis 
	 * @return JPanel consisting of Bar Chart
	 */
	public JPanel getBarChart(String entityName) {
		AbstractChart barChart = new BarChart(cab2bTable, entityName);
		JPanel jPanel = barChart.createChartPanel();
		return jPanel;
	}
	
	/**
	 * This method returns the JPanel consisting of generated Line Chart
	 * @return JPanel consisting of Line Chart
	 */
	public JPanel getLineChart() {
		AbstractChart lineChart = new LineChart(cab2bTable);
		JPanel jPanel = lineChart.createChartPanel();
		return jPanel;
	}
	
	/**
	 * This method returns the JPanel consisting of generated Scatter Plot
	 * @return JPanel consisting of Scatter Plot
	 */
	public JPanel getScatterPlot() {
		AbstractChart scatterPlot = new ScatterPlot(cab2bTable);
		JPanel jPanel = scatterPlot.createChartPanel();
		return jPanel;
	}
}
