/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import javax.swing.JPanel;

import junit.framework.TestCase;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * @author chetan_patil
 * 
 */
public class ChartGeneratorTest extends TestCase {
	private Cab2bTable getDataTable() {
		String[][] data = new String[][] { { "1", "5", "4", "4" },
				{ "4", "7", "3", "5" }, { "3", "6", "2", "9" },
				{ "5", "8", "3", "6" }, { "5", "4", "6", "7" },
				{ "7", "4", "3", "5" }, { "6", "2", "4", "8" },
				{ "8", "1", "3", "3" } };
		
		String[] headers = new String[] {
			"First", "Second", "Third", "Fourth"
		};

		Cab2bTable cab2bTable = new Cab2bTable(false, data, headers);
		cab2bTable.setRowSelectionInterval(0, data.length - 1);
		
		return cab2bTable;
	}
	
	public void testBarChart() {
		Cab2bTable cab2bTable = getDataTable();
		ChartGenerator chartGenerator = new ChartGenerator(cab2bTable);
		chartGenerator.getBarChart("Bar Chart");
		assertTrue(true);
	}

	public void testLineChart() {
		Cab2bTable cab2bTable = getDataTable();
		ChartGenerator chartGenerator = new ChartGenerator(cab2bTable);
		chartGenerator.getLineChart();
		assertTrue(true);
	}
	
	public void testScatterPlot() {
		Cab2bTable cab2bTable = getDataTable();
		ChartGenerator chartGenerator = new ChartGenerator(cab2bTable);
		chartGenerator.getScatterPlot();
		assertTrue(true);
	}
}
