/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

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
     * This method generates the dataset our of cab2bTabel which is used by the chart generator to create chart. 
     * @return dataset required to create chart.
     */
    abstract protected Dataset createDataset();

    /**
     * This method uses the dataset to generate the chart.
     * @param dataset data required to generate chart
     * @return the chart generated
     */
    abstract protected JFreeChart createChart(Dataset dataset);

    /**
     * This method converts the value passed to the chart into a valid number. If value is not a valid number it return 0.
     * @param value
     * @return
     */
    protected Number convertValue(Object value) {
        Number output = 0;
        if (value == null) {
            return output;
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            output = (Number) value;
        }
        if (value instanceof String) {
            try {
                output = Double.valueOf((String) value);
            } catch (Exception exception) {
            }
        }
        return output;
    }
}
