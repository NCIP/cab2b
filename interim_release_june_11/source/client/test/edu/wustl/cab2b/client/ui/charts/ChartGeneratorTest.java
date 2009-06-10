/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import junit.framework.TestCase;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * This is a JUnit test class for the Charts
 * @author chetan_patil
 */
public class ChartGeneratorTest extends TestCase {
    private Cab2bTable getDataTable() {
        String[][] data = new String[][] { { "1", "5", "4", "4" }, { "4", "7", "3", "5" }, { "3", "6", "2", "9" }, { "5", "8", "3", "6" }, { "5", "4", "6", "7" }, { "7", "4", "3", "5" }, { "6", "2", "4", "8" }, { "8", "1", "3", "3" } };

        String[] headers = new String[] { "First", "Second", "Third", "Fourth" };

        Cab2bTable cab2bTable = new Cab2bTable(false, data, headers);
        cab2bTable.setRowSelectionInterval(0, data.length - 1);

        return cab2bTable;
    }

    public void testBarChart() {
        Cab2bTable cab2bTable = getDataTable();
        Cab2bChartPanel chartPanel = new Cab2bChartPanel(cab2bTable);
        chartPanel.setChartType(ChartType.BAR_CHART, "");
        assertTrue(true);
    }

    public void testLineChart() {
        Cab2bTable cab2bTable = getDataTable();
        Cab2bChartPanel chartPanel = new Cab2bChartPanel(cab2bTable);
        chartPanel.setChartType(ChartType.LINE_CHART, null);
        assertTrue(true);
    }

    public void testScatterPlot() {
        Cab2bTable cab2bTable = getDataTable();
        Cab2bChartPanel chartPanel = new Cab2bChartPanel(cab2bTable);
        chartPanel.setChartType(ChartType.SCATTER_PLOT, null);
        assertTrue(true);
    }

}
