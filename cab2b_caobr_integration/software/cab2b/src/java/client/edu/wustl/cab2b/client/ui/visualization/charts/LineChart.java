/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 * This class generates the Line Chart
 * @author chetan_patil
 */
public class LineChart extends AbstractChart {
    /**
     * Parameterized constructor
     * @param chartRawData
     */
    public LineChart() {
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
     */
    protected JFreeChart createChart(Dataset dataset) {
        XYDataset xyDataset = (XYDataset) dataset;
        JFreeChart jfreechart = ChartFactory.createXYLineChart("Line Chart", "X", "Y", xyDataset,
                                                               PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();

        xyplot.setBackgroundPaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setShapesVisible(true);
        xylineandshaperenderer.setShapesFilled(true);

        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return jfreechart;
    }

    @Override
    protected Dataset createColumnWiseData() {

        List<String> selectedColumnNames = chartModel.getSelectedColumnsNames();
        List<String> selectedRowNames = chartModel.getSelectedRowNames();

        XYSeries xySeries = null;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (int i = 0; i < selectedColumnNames.size(); i++) {
            String seriesName = selectedColumnNames.get(i);
            xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedRowNames.size(); j++) {
                Object value = chartModel.getValueAt(j, i);
                xySeries.add(convertValue(selectedRowNames.get(j)), convertValue(value));
            }
            xySeriesCollection.addSeries(xySeries);
        }
        return xySeriesCollection;
    }

    @Override
    protected Dataset createRowWiseData() {

        List<String> selectedColumnNames = chartModel.getSelectedColumnsNames();
        List<String> selectedRowNames = chartModel.getSelectedRowNames();

        XYSeries xySeries = null;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (int i = 0; i < selectedRowNames.size(); i++) {
            String seriesName = selectedRowNames.get(i) + "";
            xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedColumnNames.size(); j++) {
                Object value = chartModel.getValueAt(i, j);
                xySeries.add(convertValue(selectedColumnNames.get(j)), convertValue(value));
            }
            xySeriesCollection.addSeries(xySeries);
        }
        return xySeriesCollection;
    }
}
