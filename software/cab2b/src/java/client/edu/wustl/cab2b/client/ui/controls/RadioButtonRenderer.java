/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * This class defines the method required by any object that would like to be a renderer for cells in a JTable with radiobutton. 
 * @author Chetan_BH 
 */
public class RadioButtonRenderer implements TableCellRenderer {

    /** Gets CellRendererComponent for Table
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @param hasFocus
     * @return Component
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        if (value == null) {
            return null;
        }
        return (Component) value;
    }
}
