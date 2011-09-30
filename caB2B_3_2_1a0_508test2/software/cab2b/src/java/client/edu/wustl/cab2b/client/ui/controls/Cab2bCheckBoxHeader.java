package edu.wustl.cab2b.client.ui.controls;

import java.awt.Component;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Add a JCheckBox to the Renderer
 * @author Chetan_BH
 *
 */
public class Cab2bCheckBoxHeader extends JCheckBox implements TableCellRenderer, MouseListener {

    /**
     * the Renderer Component.
     * @see #getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
     */
    protected Cab2bCheckBoxHeader rendererComponent;

    /** To which (JTable-) columns does this Checkbox belong ? */
    protected int column;

    /**
     * remembers, if mousePressed() was called before.
     * Workaround, because dozens of mouseevents occurs after one
     * mouseclick.
     */
    protected boolean mousePressed = false;

    /**
     * @param itemListener will be notified when Checkbox will be checked/unchecked
     */
    public Cab2bCheckBoxHeader(ItemListener itemListener) {
        rendererComponent = this;
        setHorizontalAlignment(JLabel.CENTER);
        rendererComponent.addItemListener(itemListener);
    }

    /** Gets the CellRenderer Component for given Table 
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return Component for given parameters
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {

        if (table != null) {
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                rendererComponent.setForeground(header.getForeground());
                rendererComponent.setBackground(header.getBackground());
                rendererComponent.setFont(header.getFont());
                header.addMouseListener(rendererComponent);
            }
        }
        setColumn(column);
        rendererComponent.setText((value == null) ? "" : value.toString());
        setBorder(BorderFactory.createRaisedBevelBorder());
        return rendererComponent;
    }

    /** @param column to which the CheckBox belongs to */
    protected void setColumn(int column) {
        this.column = column;
    }

    /** @return the column to which the CheckBox belongs to */
    public int getColumn() {
        return column;
    }

    /**
     * Calls doClick(), because the CheckBox doesn't receive any
     * mouseevents itself. (because it is in a CellRendererPane).
     * @param e MouseEvent is passed
     */
    
    protected void handleClickEvent(MouseEvent e) {
        // Workaround: dozens of mouseevents occur for only one mouse click.
        // First MousePressedEvents, then MouseReleasedEvents, (then
        // MouseClickedEvents).
        // The boolean flag 'mousePressed' is set to make sure
        // that the action is performed only once.
        if (mousePressed) {
            mousePressed = false;
            JTableHeader header = (JTableHeader) (e.getSource());
            JTable tableView = header.getTable();
            TableColumnModel columnModel = tableView.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = tableView.convertColumnIndexToModel(viewColumn);

            if (viewColumn == this.column && e.getClickCount() == 1 && column != -1) {
                doClick();
            }
        }
    }

    /** Function to handle MouseClicked event
     * @param e
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        handleClickEvent(e);
        //Header doesn't repaint itself properly
        ((JTableHeader) e.getSource()).repaint();
    }

    /** Function to handle MousePressed event
     * @param e
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
    }

    /**Function to handle MouseReleased event
     * @param e
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**Function to handle MouseEntered event
     * @param e
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**Function to handle MouseExited event
     * @param e
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
    }
}
