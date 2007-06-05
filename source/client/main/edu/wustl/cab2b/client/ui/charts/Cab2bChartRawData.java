/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * @author chetan_patil
 *
 */
public class Cab2bChartRawData {
	private Cab2bTable cab2bTable;
	
	private int[] selectedRowIndices;
	
	private int[] selectedColumnsIndices;

	/**
	 * 
	 */
	public Cab2bChartRawData(Cab2bTable cab2bTable) {
		this.cab2bTable = cab2bTable;
		selectedRowIndices = this.cab2bTable.getSelectedRows();
		selectedColumnsIndices = this.cab2bTable.getSelectedColumns();
	}

	/**
	 * @return the selectedColumnsIndices
	 */
	public Cab2bTable getCab2bTable() {
		return cab2bTable;
	}
	
	/**
	 * @return the selectedColumnsIndices
	 */
	public int[] getSelectedColumnsIndices() {
		return selectedColumnsIndices;
	}

	/**
	 * @return the selectedRowIndices
	 */
	public int[] getSelectedRowIndices() {
		return selectedRowIndices;
	}

}
