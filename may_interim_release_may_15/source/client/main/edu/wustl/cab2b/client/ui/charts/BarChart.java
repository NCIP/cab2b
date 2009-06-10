package edu.wustl.cab2b.client.ui.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * This class represents and generates the Bar Chart
 * @author chetan_patil
 */
public class BarChart extends AbstractChart {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the entity for which the chart is being generated
	 */
	private String entityName;

	/**
	 * Parameterized constructor
	 * @param cab2bTable
	 * @param entityName
	 */
	public BarChart(Cab2bTable cab2bTable, String entityName) {
		super(cab2bTable);
		this.entityName = entityName;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createDataset()
	 */
	protected CategoryDataset createDataset() {
		int[] selectedRowIndices = cab2bTable.getSelectedRows();
		int[] selectedColumnsIndices = cab2bTable.getSelectedColumns();

		String[] series = new String[selectedColumnsIndices.length];
		String[] categories = new String[selectedRowIndices.length];
				
		int i, j;
		for (i = 0; i < selectedRowIndices.length; i++) {
			categories[i] = "Row" + (selectedRowIndices[i] + 1);
		}

		for (j = 0; j < selectedColumnsIndices.length; j++) {
			series[j] = cab2bTable.getColumnName(selectedColumnsIndices[j]);
		}

		String value = null;
		Double doubleValue;
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		
		for (i = 0; i < selectedRowIndices.length; i++) {
			for (j = 0; j < selectedColumnsIndices.length; j++) {
				value = (String) cab2bTable.getValueAt(selectedRowIndices[i],
						selectedColumnsIndices[j]);

				try {
					doubleValue = Double.valueOf(value);
				} catch (Exception exception) {
					doubleValue = 0D;
				}
				
				defaultcategorydataset.addValue(doubleValue, series[j],
						categories[i]);
			}
		}

		return defaultcategorydataset;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
	 */
	protected JFreeChart createChart(Dataset dataset) {
		CategoryDataset categoryDataSet = (CategoryDataset)dataset;
		
		JFreeChart jFreeChart = ChartFactory.createBarChart("Bar Chart",
				entityName, "Value", categoryDataSet, PlotOrientation.VERTICAL,
				true, true, false);
		jFreeChart.setBackgroundPaint(Color.white);

		CategoryPlot categoryPlot = (CategoryPlot) jFreeChart.getPlot();
		categoryPlot.setBackgroundPaint(Color.lightGray);
		categoryPlot.setDomainGridlinePaint(Color.white);
		categoryPlot.setRangeGridlinePaint(Color.white);

		NumberAxis numberAxis = (NumberAxis) categoryPlot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
		barRenderer.setDrawBarOutline(false);
		barRenderer.setItemMargin(0.1D);

		CategoryAxis categoryaxis = categoryPlot.getDomainAxis();
		categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		return jFreeChart;
	}

	/**
	 * This class is used for the generation of the labels to be displayed against each group on x-axis. 
	 * @author chetan_patil
	 */
	static class LabelGenerator extends StandardCategoryItemLabelGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		LabelGenerator() {
		}

		public String generateLabel(CategoryDataset categoryDataSet, int i,
				int j) {
			return categoryDataSet.getRowKey(i).toString();
		}
	}

}