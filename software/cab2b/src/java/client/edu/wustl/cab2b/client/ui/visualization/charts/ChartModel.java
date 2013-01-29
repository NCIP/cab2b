/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.client.ui.visualization.charts;

import java.util.List;

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

    public static String UPDATE_CHART_PROPERTY = "UPDATE_CHART_PROPERTY";

    /**
     * Parameterized constructor
     */
    public ChartModel(Object[][] tableData, List<String> selectedRowNames, List<String> selectedColumnNames) {
        this.tableData = tableData;
        this.selectedRowNames = selectedRowNames;
        this.selectedColumnNames = selectedColumnNames;
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
