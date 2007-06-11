/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * This abstract class represents the common chart and implements some of the common services.
 * @author chetan_patil
 */
public abstract class AbstractChart {
    /**
     * Holds the cab2bTable which contains the data to be displayed as chart.
     */
    protected Cab2bChartRawData chartRawData;

    /**
     * Parameterized constructor 
     * @param cab2bTable
     */
    public AbstractChart(Cab2bChartRawData chartRawData) {
        this.chartRawData = chartRawData;
    }

    /**
     * This method creates create a chart and places it into a JPanel.
     * @return JPanel containing the generated chart.
     */
    public JPanel createChartPanel() {
        Dataset dataset = createDataset();
        JFreeChart jfreechart = createChart(dataset);
        return new ChartPanel(jfreechart);
    }

    /**
     * This method generates the dataset our of cab2bTabel which is consumed by the chart generator to create chart. 
     * @return dataset required to create chart.
     */
    protected Dataset createDataset() {
        Cab2bTable cab2bTable = chartRawData.getCab2bTable();
        int[] selectedRowIndices = chartRawData.getSelectedRowIndices();
        int[] selectedColumnsIndices = chartRawData.getSelectedColumnsIndices();

        XYSeries xySeries = null;
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        for (int i = 0; i < selectedColumnsIndices.length; i++) {
            String seriesName = cab2bTable.getColumnName(selectedColumnsIndices[i]);
            xySeries = new XYSeries(seriesName);
            for (int j = 0; j < selectedRowIndices.length; j++) {
                String value = (String) cab2bTable.getValueAt(selectedRowIndices[j], selectedColumnsIndices[i]);

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

        return xySeriesCollection;
    }

    /**
     * This method uses the dataset to generate the chart.
     * @param dataset data required to generate chart
     * @return the chart generated
     */
    abstract protected JFreeChart createChart(Dataset dataset);
}
