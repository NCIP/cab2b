/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.visualization.charts;

import java.awt.Color;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * This class generates the Scatter Plot.
 * @author chetan_patil
 */
public class ScatterPlot extends AbstractChart {

    /**
     * Parameterized constructor
     * @param chartRawData
     */
    public ScatterPlot() {
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
     */
    protected JFreeChart createChart(Dataset dataset) {
        XYDataset xyDataset = (XYDataset) dataset;
        JFreeChart jfreechart = ChartFactory.createScatterPlot("Scatter Plot", "X", "Y", xyDataset,
                                                               PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);

        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.white);
        XYDotRenderer xydotrenderer = new XYDotRenderer();
        xydotrenderer.setDotWidth(4);
        xydotrenderer.setDotHeight(4);

        xyplot.setRenderer(xydotrenderer);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);

        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);

        return jfreechart;
    }

    @Override
    protected Dataset createColumnWiseData() {

        List<String> selectedRowNames = chartModel.getSelectedRowNames();
        List<String> selectedColumnsNames = chartModel.getSelectedColumnsNames();

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        if (selectedColumnsNames.size() == 1) {
            String seriesName = chartModel.getColumnName(0);
            XYSeries xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedRowNames.size(); j++) {
                Object value = null;
                value = chartModel.getValueAt(j, 0);
                xySeries.add(convertValue(selectedRowNames.get(j)), convertValue(value));
            }
            xySeriesCollection.addSeries(xySeries);
        } else {
            for (int i = 1; i < selectedColumnsNames.size(); i++) {
                String seriesName = chartModel.getColumnName(i);
                XYSeries xySeries = new XYSeries(seriesName);

                for (int j = 0; j < selectedRowNames.size(); j++) {
                    Object yValueString = null;
                    Object xValueString = null;
                    xValueString = chartModel.getValueAt(j, 0);
                    yValueString = chartModel.getValueAt(j, i);
                    xySeries.add(convertValue(xValueString), convertValue(yValueString));
                }
                xySeriesCollection.addSeries(xySeries);
            }
        }
        return xySeriesCollection;
    }

    @Override
    protected Dataset createRowWiseData() {

        List<String> selectedRowNames = chartModel.getSelectedRowNames();
        List<String> selectedColumnsNames = chartModel.getSelectedColumnsNames();

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        if (selectedRowNames.size() == 1) {
            String seriesName = selectedRowNames.get(0) + "";
            XYSeries xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedColumnsNames.size(); j++) {
                Object value = null;
                if (chartModel.isWholeColumnSelected())
                    value = chartModel.getValueAt(0, j);

                xySeries.add(convertValue(selectedRowNames.get(j)), convertValue(value));
            }
            xySeriesCollection.addSeries(xySeries);
        } else {
            for (int i = 1; i < selectedRowNames.size(); i++) {
                String seriesName = selectedRowNames.get(i) + "";
                XYSeries xySeries = new XYSeries(seriesName);

                for (int j = 0; j < selectedColumnsNames.size(); j++) {
                    Object xValueString = chartModel.getValueAt(0, j);
                    Object yValueString = chartModel.getValueAt(i, j);
                    xySeries.add(convertValue(xValueString), convertValue(yValueString));
                }
                xySeriesCollection.addSeries(xySeries);
            }
        }
        return xySeriesCollection;
    }

}
