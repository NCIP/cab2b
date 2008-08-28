package edu.wustl.cab2b.client.ui.controls;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * This class defines the method required by any object that would like to be a renderer for cells in a JTable with radiobutton. 
 * @author Chetan_BH 
 */
public class RadioButtonRenderer implements TableCellRenderer {
    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        if (value == null)
            return null;
        return (Component) value;
    }
}
