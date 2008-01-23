package edu.wustl.cab2b.client.ui.controls;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class CheckBoxTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    private String[] m_columnNames;

    private Object[][] m_data;

    public CheckBoxTableModel(String[] columnNames, Object[][] data) {
        m_columnNames = columnNames;
        m_data = data;
    }

    public int getColumnCount() {
        return m_columnNames.length;
    }

    public int getRowCount() {
        return m_data.length;
    }

    public String getColumnName(int col) {
        return m_columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return m_data[row][col];
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

    public int[] getCheckedRowIndexes() {
        Vector<Integer> selectIndex = new Vector<Integer>();
        for (int i = 0; i < m_data.length; i++) {
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
        m_data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    private void printDebugData() {
        int numRows = getRowCount();
        int numCols = getColumnCount();

        for (int i = 0; i < numRows; i++) {
             
            for (int j = 0; j < numCols; j++) {
                 
            }
             
        }
         
    }
}