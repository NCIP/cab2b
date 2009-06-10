/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

/**
 * This Class retains the instance of the data for the chart was generated. 
 * @author chetan_patil
 */
public class Cab2bChartRawData {
    /** Stores the reference of the table for which the chart is to be displayed */
    private Cab2bTable cab2bTable;

    /** Stores the selected indices of the rows */
    private int[] selectedRowIndices;

    /** Stores the selected indices of the columns */
    private int[] selectedColumnsIndices;

    /**
     * Parameterized constructor
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