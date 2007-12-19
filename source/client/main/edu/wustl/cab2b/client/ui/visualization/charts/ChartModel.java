/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.util.List;

import edu.wustl.cab2b.client.ui.AbstractModel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.viewresults.DefaultSpreadSheetViewPanel;
import edu.wustl.cab2b.client.ui.viewresults.ThreeDResultObjectDetailsPanel;
import edu.wustl.cab2b.common.util.Constants.ChartOrientation;

/**
 * This Class retains the instance of the data for the chart was generated. 
 * @author chetan_patil
 */
public class ChartModel extends AbstractModel {
    /** Stores the reference of the table for which the chart is to be displayed */

    /** Stores the selected indices of the rows */
    private List<String> selectedRowNames;

    /** Stores the selected indices of the columns */
    private List<String> selectedColumnNames;

    /** Stores the orientation of the chart */
    private ChartOrientation chartOrientation = ChartOrientation.COLUMN_AS_CATEGORY;

    private boolean isWholeColumnSelected = false;

    /** Stores the table Data  */
    private Object[][] tableData;

    private String chartType;

    /**
     * Parameterized constructor
     */
    public ChartModel(Object[][] tableData, List<String> selectedRowNames, List<String> selectedColumnNames) {
        this.tableData = tableData;
        this.selectedRowNames = selectedRowNames;
        this.selectedColumnNames = selectedColumnNames;

        /*  if (dataListDetailedPanel instanceof DefaultSpreadSheetViewPanel) {

         DefaultSpreadSheetViewPanel spreadSheetViewPanel;
         spreadSheetViewPanel = (DefaultSpreadSheetViewPanel) dataListDetailedPanel;

         selectedColumnsIndices = spreadSheetViewPanel.getSelectedColumns();
         selectedRowIndices = spreadSheetViewPanel.getSelectedRows();
         } else {
         selectedColumnsIndices = tableData.getSelectedColumns();
         if (dataListDetailedPanel instanceof ThreeDResultObjectDetailsPanel) {
         ThreeDResultObjectDetailsPanel threeDResultObjectDetailsPanel = (ThreeDResultObjectDetailsPanel) dataListDetailedPanel;
         isWholeColumnSelected = threeDResultObjectDetailsPanel.getIsWholeColumnSelected();
         tableData = threeDResultObjectDetailsPanel.getDataTable();

         if (isWholeColumnSelected) {
         selectedRowIndices = new int[selectedColumnsIndices.length];
         for (int i = 0; i < selectedColumnsIndices.length; i++) {
         selectedRowIndices[i] = i;
         }
         }
         } else {
         selectedRowIndices = this.tableData.getSelectedRows();
         }
         }*/
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableData[rowIndex][columnIndex];

    }

    public String getColumnName(int column) {
        return selectedColumnNames.get(column);
    }

    public boolean isWholeColumnSelected() {
        return isWholeColumnSelected;
    }

    /*    public Cab2bTable getCab2bTableData() {
     return tableData;
     }*/

    /*public Object getCab2bTableValue(int row, int column) {
     return tableData.getValueAt(row, column);
     }*/

    /* *//**
     * @return the selectedColumnsIndices
     */
    /*
     public Cab2bTable getCab2bTable() {
     return cab2bTable;
     }
     */

    public List<String> getSelectedColumnsNames() {
        return selectedColumnNames;
    }

    public List<String> getSelectedRowNames() {
        return selectedRowNames;
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

        setChanged();
        notifyObservers();
    }

    /**
     * @return the chartType
     */
    public String getChartType() {
        return chartType;
    }

    /**
     * @param chartType the chartType to set
     */
    public void setChartType(String chartType) {
        this.chartType = chartType;

        setChanged();
        notifyObservers();
    }

}
