package edu.wustl.cab2b.client.ui.controls;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;

/**
 * The default editor for table and tree cells with radio button. 
 * @author deepak_shingan
 *
 */
public class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
    private static final long serialVersionUID = 1L;

    /**
     * Radio button object
     */
    private JRadioButton button;

    /**
     * Creates RadioButtonEditor with specified checkbox property
     * @param checkBox
     */
    public RadioButtonEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    /** Gets CellEditorComponent for Table 
     * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return component
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                                                 int column) {
        if (value == null)
            return null;
        button = (JRadioButton) value;
        button.addItemListener(this);
        return (Component) value;
    }

    /** 
     * @see javax.swing.DefaultCellEditor#getCellEditorValue()
     * @return Object
     */
    public Object getCellEditorValue() {
        button.removeItemListener(this);
        return button;
    }

    /** Handle the Event of StateChanged Property
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     * @param e
     */
    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
    }
}