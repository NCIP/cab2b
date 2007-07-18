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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This class generates the Scatter Plot.
 * @author chetan_patil
 */
public class ScatterPlot extends AbstractChart {

    /**
     * Parameterized constructor
     * @param chartRawData
     */
    public ScatterPlot(Cab2bChartRawData chartRawData) {
        super(chartRawData);
    }

    /**
     * This method generates the dataset our of cab2bTabel which is used by the chart generator to create chart. 
     * @return dataset required to create chart.
     */
    protected Dataset createDataset() {
        Cab2bTable cab2bTable = chartRawData.getCab2bTable();
        int[] selectedRowIndices = chartRawData.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartRawData.getSelectedColumnsIndices();
        ChartOrientation chartOrientation = chartRawData.getChartOrientation();

        XYSeries xySeries = null;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        if (chartOrientation == ChartOrientation.COLUMN_AS_CATEGORY) {
            if (selectedColumnsIndices.length == 1) {
                String seriesName = cab2bTable.getColumnName(selectedColumnsIndices[0]);
                xySeries = new XYSeries(seriesName);
                for (int j = 0; j < selectedRowIndices.length; j++) {
                    String value = (String) cab2bTable.getValueAt(selectedRowIndices[j], selectedColumnsIndices[0]);

                    Double xValue = new Double(selectedRowIndices[j]);
                    Double yValue = null;

                    try {
                        yValue = Double.valueOf(value);
                    } catch (Exception exception) {
                        yValue = 0D;
                    }
                    xySeries.add(xValue, yValue);
                }
                xySeriesCollection.addSeries(xySeries);
            } else if (selectedColumnsIndices.length > 1) {
                for (int i = 1; i < selectedColumnsIndices.length; i++) {
                    String seriesName = cab2bTable.getColumnName(selectedColumnsIndices[i]);
                    xySeries = new XYSeries(seriesName);

                    for (int j = 0; j < selectedRowIndices.length; j++) {
                        String value = (String) cab2bTable.getValueAt(selectedRowIndices[j],
                                                                      selectedColumnsIndices[i]);

                        Double xValue = new Double((String) cab2bTable.getValueAt(selectedRowIndices[j],
                                                                                  selectedColumnsIndices[i]));
                        Double yValue = null;

                        try {
                            yValue = Double.valueOf(value);
                        } catch (Exception exception) {
                            yValue = 0D;
                        }
                        xySeries.add(xValue, yValue);
                    }
                    xySeriesCollection.addSeries(xySeries);
                }
            }
        } else {
            if (selectedRowIndices.length == 1) {
                String seriesName = selectedRowIndices[0] + "";
                xySeries = new XYSeries(seriesName);
                for (int j = 0; j < selectedColumnsIndices.length; j++) {
                    String value = (String) cab2bTable.getValueAt(selectedRowIndices[0], selectedColumnsIndices[j]);

                    Double xValue = new Double(selectedColumnsIndices[j]);
                    Double yValue = null;

                    try {
                        yValue = Double.valueOf(value);
                    } catch (Exception exception) {
                        yValue = 0D;
                    }
                    xySeries.add(xValue, yValue);
                }
                xySeriesCollection.addSeries(xySeries);
            } else if (selectedRowIndices.length > 1) {
                for (int i = 0; i < selectedRowIndices.length; i++) {
                    String seriesName = selectedRowIndices[i] + "";
                    xySeries = new XYSeries(seriesName);

                    for (int j = 0; j < selectedColumnsIndices.length; j++) {
                        String value = (String) cab2bTable.getValueAt(selectedRowIndices[i],
                                                                      selectedColumnsIndices[j]);

                        Double xValue = new Double((String) cab2bTable.getValueAt(selectedRowIndices[0],
                                                                                  selectedColumnsIndices[j]));
                        Double yValue = null;

                        try {
                            yValue = Double.valueOf(value);
                        } catch (Exception exception) {
                            yValue = 0D;
                        }
                        xySeries.add(xValue, yValue);
                    }
                    xySeriesCollection.addSeries(xySeries);
                }
            }
        }

        return xySeriesCollection;
    }

    /*
     * (non-Javadoc)
     * @see edu.wustl.cab2b.client.ui.charts.AbstractChart#createChart(org.jfree.data.general.Dataset)
     */
    protected JFreeChart createChart(Dataset dataset) {
        XYDataset xyDataset = (XYDataset) dataset;
        JFreeChart jfreechart = ChartFactory.createScatterPlot(ChartType.SCATTER_PLOT.getType(), "X",
                                                               "Y", xyDataset, PlotOrientation.VERTICAL, true,
                                                               true, false);
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

}
