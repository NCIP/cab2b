/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 * This class represents and generates the Line Chart
 * @author chetan_patil
 */
public class LineChart extends AbstractChart {
	/**
	 * Parameterized constructor
	 * @param chartRawData
	 */
	public LineChart(Cab2bChartRawData chartRawData) {
		super(chartRawData);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
	 */
	protected JFreeChart createChart(Dataset dataset) {
		XYDataset xyDataset = (XYDataset) dataset;
		JFreeChart jfreechart = ChartFactory.createXYLineChart("Line Chart",
				"X", "Y", xyDataset, PlotOrientation.VERTICAL, true, true,
				false);
		jfreechart.setBackgroundPaint(Color.white);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(Color.white);
		//xyplot.setBackgroundPaint(new Color(120, 120, 120));
		xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);

		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot
				.getRenderer();
		xylineandshaperenderer.setShapesVisible(true);
		xylineandshaperenderer.setShapesFilled(true);

		NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return jfreechart;
	}

}
