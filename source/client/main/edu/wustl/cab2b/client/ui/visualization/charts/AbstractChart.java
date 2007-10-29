/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.util.Observable;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Dataset;

import edu.wustl.cab2b.client.ui.AbstractViewer;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This abstract class represents the common chart and implements some of the common services.
 * @author chetan_patil
 */
public abstract class AbstractChart extends AbstractViewer {
    /**
     * Holds the cab2bTable which contains the data to be displayed as chart.
     */
    protected ChartModel chartModel;

    protected ChartPanel rowWiseChart;

    protected ChartPanel columnWiseChart;

    /**
     * This method generates the column wise data set which is used by the chart generator to create chart. 
     * @return data set required to create chart.
     */
    abstract protected Dataset createColumnWiseData();

    /**
     * This method generates the row wise data set which is used by the chart generator to create chart. 
     * @return data set required to create chart.
     */
    abstract protected Dataset createRowWiseData();

    /**
     * This method uses the data set to generate the chart.
     * @param dataset data required to generate chart
     * @return the chart generated
     */
    abstract protected JFreeChart createChart(Dataset dataset);

    /**
     * Parameterized constructor 
     * @param cab2bTable
     */
    public AbstractChart() {
    }

    @Override
    public void update(Observable observable, Object object) {
        chartModel = (ChartModel) observable;

        ChartOrientation chartOrientation = chartModel.getChartOrientation();
        if (chartOrientation == ChartOrientation.COLUMN_AS_CATEGORY && columnWiseChart == null) {
            Dataset dataset = createColumnWiseData();
            columnWiseChart = new ChartPanel(createChart(dataset));
        } else if (chartOrientation == ChartOrientation.ROW_AS_CATEGORY && rowWiseChart == null) {
            Dataset dataset = createRowWiseData();
            rowWiseChart = new ChartPanel(createChart(dataset));
        }
    }

    /**
     * This method creates create a chart and places it into a JPanel.
     * @return JPanel containing the generated chart.
     */
    public JPanel getChartPanel() {
        ChartPanel chartPanel = null;

        ChartOrientation chartOrientation = chartModel.getChartOrientation();
        if (chartOrientation == ChartOrientation.COLUMN_AS_CATEGORY && columnWiseChart != null) {
            chartPanel = columnWiseChart;
        } else if (chartOrientation == ChartOrientation.ROW_AS_CATEGORY && rowWiseChart != null) {
            chartPanel = rowWiseChart;
        }

        return chartPanel;
    }

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
