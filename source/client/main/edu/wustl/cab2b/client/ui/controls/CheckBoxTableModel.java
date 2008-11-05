package edu.wustl.cab2b.client.ui.controls;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * TableModel defined for AvailablePathPanel extended from AbstractTableModel
 * @author Chetan_BH
 *
 */
public class CheckBoxTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    /**
     * Column names array 
     */
    private String[] modelColumnNames;

    /**
     * Table data double array
     */
    private Object[][] tableData;

    /**
     * @param columnNames
     * @param data
     */
    public CheckBoxTableModel(String[] columnNames, Object[][] data) {
        modelColumnNames = columnNames;
        tableData = data;
    }

    /** Retunrs the column count of the given table
     * @see javax.swing.table.TableModel#getColumnCount()
     * @return modelColumnNames.length
     */
    public int getColumnCount() {
        return modelColumnNames.length;
    }

    /** Returns the row count of the given Table
     * @see javax.swing.table.TableModel#getRowCount()
     * @return tableData.length
     */
    public int getRowCount() {
        return tableData.length;
    }

    /** Returns the ColumnName for given ColumnIndex
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     * @param col
     * @return ColumnName
     */
    public String getColumnName(int col) {
        return modelColumnNames[col];
    }

    /** Returns the value for a particular cell in the table
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     * @param row
     * @param col
     */
    public Object getValueAt(int row, int col) {
        return tableData[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    /** Returns the Class for given Column
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     * @param c
     * @return Class
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    /** 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     * @param row
     * @param col
     * @return boolean value for CellEditable
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets checked row indexes
     * @return int[]
     */
    public int[] getCheckedRowIndexes() {
        Vector<Integer> selectIndex = new Vector<Integer>();
        for (int i = 0; i < tableData.length; i++) {
            Boolean value = (Boolean) getValueAt(i, 0);
            if (value == true) {
                selectIndex.add(new Integer(i));
            }
        }
        int values[] = new int[selectIndex.size()];
        for (int i = 0; i < selectIndex.size(); i++) {
            values[i] = selectIndex.get(i).intValue();
        }
        return values;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    /**
     * Sets the value for corresponding Cell
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     * @param value
     * @param row
     * @param col
     */
    public void setValueAt(Object value, int row, int col) {
        tableData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}