/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.controls.LazyTable;

import javax.swing.event.TableModelListener;

/**
 * defautl implememtation of {@link LazyTableModelInterface}.
 * 
 * @author Rahul Ner
 */
public class DefaultLazyTableModel<D extends LazyDataSourceInterface> implements LazyTableModelInterface {

    /**
     * 
     */
    protected D dataSource;

    /**
     * @param dataSource
     */
    public DefaultLazyTableModel(D dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return dataSource.getRowCount();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return dataSource.getColumnCount();
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int arg0) {
        return dataSource.getColumnName(arg0);
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowNo, int columnNo) {
        return dataSource.getData(rowNo, columnNo);
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object arg0, int arg1, int arg2) {

    }

    /**
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    public void addTableModelListener(TableModelListener arg0) {

    }

    /**
     * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    public void removeTableModelListener(TableModelListener arg0) {

    }
}
