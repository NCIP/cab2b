/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

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

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return modelColumnNames.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return tableData.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return modelColumnNames[col];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
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
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
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
    public void setValueAt(Object value, int row, int col) {
        tableData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}