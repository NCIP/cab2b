/**
 * 
 */
package edu.wustl.cab2b.client.ui.controls;

import javax.swing.table.DefaultTableModel;

/**
 * This is an extension of DefaultTableModel.
 * @author chetan_patil
 */
public class RadioButtonTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

    /**
     * Creates RadioButtonTableModel with data as  tableData and column names as tableHeaders
     * @param tableData
     * @param tableHeaders
     */
    public RadioButtonTableModel(Object[][] tableData, Object[] tableHeaders) {
        super(tableData, tableHeaders);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }
}
