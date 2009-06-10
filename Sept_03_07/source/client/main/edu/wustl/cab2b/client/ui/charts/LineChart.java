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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This class generates the Line Chart
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
            for (int i = 0; i < selectedColumnsIndices.length; i++) {
                String seriesName = cab2bTable.getColumnName(selectedColumnsIndices[i]);
                xySeries = new XYSeries(seriesName);
                for (int j = 0; j < selectedRowIndices.length; j++) {
                    String value;
                    if (chartRawData.isWholeColumnSelected())
                        value = (String) chartRawData.getCab2bTableData()[j][i];
                    else
                        value = (String) cab2bTable.getValueAt(selectedRowIndices[j], selectedColumnsIndices[i]);

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
            }
        } else {
            for (int i = 0; i < selectedRowIndices.length; i++) {
                String seriesName = selectedRowIndices[i] + "";
                xySeries = new XYSeries(seriesName);
                for (int j = 0; j < selectedColumnsIndices.length; j++) {
                    String value;
                    if (chartRawData.isWholeColumnSelected())
                        value = (String) chartRawData.getCab2bTableData()[i][j];
                    else
                        value = (String) cab2bTable.getValueAt(selectedRowIndices[i], selectedColumnsIndices[j]);

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
        JFreeChart jfreechart = ChartFactory.createXYLineChart(ChartType.LINE_CHART.getType(), "X", "Y",
                                                               xyDataset, PlotOrientation.VERTICAL, true, true,
                                                               false);
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

}
