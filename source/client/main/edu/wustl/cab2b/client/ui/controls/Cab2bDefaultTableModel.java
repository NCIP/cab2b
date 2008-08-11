package edu.wustl.cab2b.client.ui.controls;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class Cab2bDefaultTableModel extends DefaultTableModel {
    boolean m_showCheckBox;

    /**
     * Constructor of table model
     * @param data contents of table
     * @param header header details of the table
     */
    public Cab2bDefaultTableModel(boolean showCheckBox, Object[][] data, Object[] header) {
        super(data, header);
        m_showCheckBox = showCheckBox;
        setDataVector(getDataVector(), this.columnIdentifiers); // This method makes sure that the proper data is set in the table
    }

    /**
     * Constructor of table model
     * @param data contents of table
     * @param header header details of the table
     */
    public Cab2bDefaultTableModel(boolean showCheckBox, Vector data, Vector header) {
        super(data, header);
        m_showCheckBox = showCheckBox;
        setDataVector(data, header); // This method makes sure that the proper data is set in the table
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        // If first column is check-box then only 
        // make first column editable
        if (true == m_showCheckBox) {
            if (col == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Method to return selected rows in the table
     * @return
     */
    public Vector<Integer> getCheckedRowIndexes() {
        Vector<Integer> selectIndex = new Vector<Integer>();
        Vector tableData = getDataVector();
        for (int i = 0; i < tableData.size(); i++) {
            Boolean value = (Boolean) ((Vector) tableData.get(i)).get(0);
            if (value == true) {
                selectIndex.add(new Integer(i));
            }
        }
        return selectIndex;
    }

    /**
     * Method to select all the rows of the table
     */
    public void selectAllRows() {
        Vector tableData = getDataVector();
        for (int i = 0; i < tableData.size(); i++) {
            ((Vector) tableData.get(i)).set(0, new Boolean(true));
            fireTableCellUpdated(i, 0);
        }
    }

    /**
     * Method to select all the rows of the table
     */
    public void unSelectAllRows() {
        Vector tableData = getDataVector();
        for (int i = 0; i < tableData.size(); i++) {
            ((Vector) tableData.get(i)).set(0, new Boolean(false));
            fireTableCellUpdated(i, 0);
        }
    }

    /**
     * Set the data actual contents of the table 
     * depending upon whether to show check-box
     * as first row or not
     */
    public void setDataVector(Vector data, Vector header) {
        // If check-box has to be shown
        if (true == m_showCheckBox) {
            Vector tableData = new Vector();
            for (int i = 0; i < data.size(); i++) {
                Vector rowData = (Vector) data.get(i);
                Vector tableRowData = new Vector(rowData.size() + 1);
                tableRowData.add(0, new Boolean(false));
                for (int j = 1; j <= rowData.size(); j++) {
                    tableRowData.add(j, rowData.get(j - 1));
                }
                tableData.add(tableRowData);
            }
            Vector tableHeader = new Vector(header.size() + 1);
            tableHeader.add(0, "");
            for (int i = 1; i <= header.size(); i++) {
                tableHeader.add(i, header.get(i - 1));
            }
            super.setDataVector(tableData, tableHeader);
        } else {
            super.setDataVector(data, header);
        }
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c) {
        if (getValueAt(0, c) == null) {
            return String.class;
        } else {
            return getValueAt(0, c).getClass();
        }
    }
}
