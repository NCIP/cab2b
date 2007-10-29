/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

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
        Cab2bTable cab2bTable = chartModel.getCab2bTable();
        int[] selectedRowIndices = chartModel.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartModel.getSelectedColumnsIndices();

        XYSeries xySeries = null;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (int i = 0; i < selectedColumnsIndices.length; i++) {
            String seriesName = cab2bTable.getColumnName(selectedColumnsIndices[i]);
            xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedRowIndices.length; j++) {
                Object value;
                if (chartModel.isWholeColumnSelected())
                    value = chartModel.getCab2bTableData()[j][i];
                else
                    value = cab2bTable.getValueAt(selectedRowIndices[j], selectedColumnsIndices[i]);

                xySeries.add(selectedRowIndices[j], convertValue(value));
            }
            xySeriesCollection.addSeries(xySeries);
        }

        return xySeriesCollection;
    }

    @Override
    protected Dataset createRowWiseData() {
        Cab2bTable cab2bTable = chartModel.getCab2bTable();
        int[] selectedRowIndices = chartModel.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartModel.getSelectedColumnsIndices();

        XYSeries xySeries = null;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (int i = 0; i < selectedRowIndices.length; i++) {
            String seriesName = selectedRowIndices[i] + "";
            xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedColumnsIndices.length; j++) {
                Object value;
                if (chartModel.isWholeColumnSelected())
                    value = chartModel.getCab2bTableData()[i][j];
                else
                    value = cab2bTable.getValueAt(selectedRowIndices[i], selectedColumnsIndices[j]);

                xySeries.add(selectedColumnsIndices[j], convertValue(value));
            }
            xySeriesCollection.addSeries(xySeries);
        }

        return xySeriesCollection;
    }

}
