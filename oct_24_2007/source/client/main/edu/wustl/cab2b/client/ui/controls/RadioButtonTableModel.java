/**
 * 
 */
package edu.wustl.cab2b.client.ui.controls;

import javax.swing.table.DefaultTableModel;

/**
 * @author chetan_patil
 *
 */
public class RadioButtonTableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

    public RadioButtonTableModel(Object[][] tableData, Object[] tableHeaders) {
        super(tableData, tableHeaders);
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 0) {
            return true;
        } else {
            return false;
        }
    }
    
}
