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
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

/**
 * @author chetan_patil
 *
 */
public class ScatterPlot extends AbstractChart {

	/**
	 * Parameterized constructor
	 * @param chartRawData
	 */
	public ScatterPlot(Cab2bChartRawData chartRawData) {
		super(chartRawData);
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
	 */
	protected JFreeChart createChart(Dataset dataset) {
		XYDataset xyDataset = (XYDataset)createDataset();
        JFreeChart jfreechart = ChartFactory.createScatterPlot("Scatter Plot", "X", "Y", xyDataset, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.white);
        //xyplot.setBackgroundPaint(new Color(120, 120, 120));
        XYDotRenderer xydotrenderer = new XYDotRenderer();
        xydotrenderer.setDotWidth(4);
        xydotrenderer.setDotHeight(4);
        
        xyplot.setRenderer(xydotrenderer);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);
        
        NumberAxis numberaxis = (NumberAxis)xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        
        return jfreechart;
	}

}
