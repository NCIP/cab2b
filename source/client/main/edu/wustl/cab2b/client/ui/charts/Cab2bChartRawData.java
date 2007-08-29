/**
 * 
 */
package edu.wustl.cab2b.client.ui.charts;

import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.LazyTable.DefaultLazyTableModel;
import edu.wustl.cab2b.client.ui.viewresults.BDQTableModel;
import edu.wustl.cab2b.client.ui.viewresults.ThreeDResultObjectDetailsPanel;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

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

    /** Stores the orientation of the chart */
    private ChartOrientation chartOrientation;

    /** Stores the table Data  */
    private Object[][] tableData = null;

    /**
     * Parameterized constructor
     */
    public Cab2bChartRawData(Cab2bTable cab2bTable, ChartOrientation chartOrientation) {
        this.cab2bTable = cab2bTable;
        if (chartOrientation != null) {
            this.chartOrientation = chartOrientation;
        } else {
            this.chartOrientation = ChartOrientation.COLUMN_AS_CATEGORY;
        }
        selectedRowIndices = this.cab2bTable.getSelectedRows();
        selectedColumnsIndices = this.cab2bTable.getSelectedColumns();

        if (ThreeDResultObjectDetailsPanel.isWholeColumnSelected) {
            if (cab2bTable.getModel() instanceof DefaultLazyTableModel) {
                BDQTableModel datCubeTableModel = (BDQTableModel) cab2bTable.getModel();
                tableData = datCubeTableModel.getColumnValues(cab2bTable.getSelectedColumns());
                selectedRowIndices = new int[tableData.length];                
                for (int i = 0; i < tableData.length; i++)
                selectedRowIndices[i] = i;                
                selectedColumnsIndices = this.cab2bTable.getSelectedColumns();
            }
        }
    }

    
    public Object[][] getCab2bTableData() {
        return tableData;
    }
    
    
    public Object getCab2bTableValue(int row, int column) {
        return tableData[row][column];
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

    /**
     * @return the chartOrientation
     */
    public ChartOrientation getChartOrientation() {
        return chartOrientation;
    }

    /**
     * @param chartOrientation the chartOrientation to set
     */
    public void setChartOrientation(ChartOrientation chartOrientation) {
        this.chartOrientation = chartOrientation;
    }

}
