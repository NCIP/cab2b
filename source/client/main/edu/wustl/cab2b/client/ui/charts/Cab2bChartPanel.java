/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.util.Constants;

/**
 * This class is the panel of the chart and provides a method to set the type of chart in this panel
 * @author chetan_patil
 */
public class Cab2bChartPanel extends Cab2bPanel {
	private static final long serialVersionUID = 1L;
	
	private Cab2bChartRawData chartRawData;
	
	/**
	 * Parameterized contructor
	 * @param cab2bTable
	 */
	public Cab2bChartPanel(Cab2bTable cab2bTable) {
		this.chartRawData = new Cab2bChartRawData(cab2bTable);
		this.setBorder(null);
	}
	
	public void setChartType(final String chartType, final String entityName) {
		JPanel jPanel = null;
		if (chartType.equals(Constants.BAR_CHART)) {
			jPanel = getBarChart(entityName);
		} else if (chartType.equals(Constants.LINE_CHART)) {
			jPanel = getLineChart();
		} else if (chartType.equals(Constants.SCATTER_PLOT)) {
			jPanel = getScatterPlot();
		}
		this.removeAll();
		this.add("hfill vfill ", jPanel);
	}
	
	/**
	 * This method returns the JPanel consisting of generated Bar Chart
	 * @param entityName label to be displayed for x-axis 
	 * @return JPanel consisting of Bar Chart
	 */
	private JPanel getBarChart(String entityName) {
		AbstractChart barChart = new BarChart(chartRawData, entityName);
		JPanel jPanel = barChart.createChartPanel();
		return jPanel;
	}
	
	/**
	 * This method returns the JPanel consisting of generated Line Chart
	 * @return JPanel consisting of Line Chart
	 */
	private JPanel getLineChart() {
		AbstractChart lineChart = new LineChart(chartRawData);
		JPanel jPanel = lineChart.createChartPanel();
		return jPanel;
	}
	
	/**
	 * This method returns the JPanel consisting of generated Scatter Plot
	 * @return JPanel consisting of Scatter Plot
	 */
	private JPanel getScatterPlot() {
		AbstractChart scatterPlot = new ScatterPlot(chartRawData);
		JPanel jPanel = scatterPlot.createChartPanel();
		return jPanel;
	}
	
}
