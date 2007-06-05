/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import java.awt.Component;

import javax.swing.JPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.util.Constants;

/**
 * This class is responsible of providing the methods to generate the different charts.
 * @author chetan_patil
 */
public class Cab2bChartPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Cab2bChartRawData chartRawData;
	
	/**
	 * Parameterized contructor
	 * @param cab2bTable
	 */
	public Cab2bChartPanel(Cab2bTable cab2bTable) {
		this.chartRawData = new Cab2bChartRawData(cab2bTable);
	}
	
	public void setChartType(final String chartType, final String entityName) {
		Component component = null;
		if (chartType.equals(Constants.BAR_CHART)) {
			component = getBarChart(entityName);
		} else if (chartType.equals(Constants.LINE_CHART)) {
			component = getLineChart();
		} else if (chartType.equals(Constants.SCATTER_PLOT)) {
			component = getScatterPlot();
		}
		this.removeAll();
		this.add(component);
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
