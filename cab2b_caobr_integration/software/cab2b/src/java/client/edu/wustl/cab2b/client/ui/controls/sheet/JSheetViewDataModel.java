/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wustl.cab2b.client.ui.controls.sheet;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * 
 * @author jasbir_sachdeva
 */
public class JSheetViewDataModel extends AbstractTableModel {

	JTable tblData;

	TableModel compositeDataModel;

	JSheetViewDataModel(JTable tblData, TableModel compositeDataModel) {
		this.tblData = tblData;
		this.compositeDataModel = compositeDataModel;
	}

	/**
	 * @return row count
     * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return tblData.getRowCount();
	}

	/**
	 * @return column Count
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return tblData.getColumnCount();

	}

	/**
	 * @param rowIndex
	 * @param columnIndex
	 * @return Value of Cell
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tblData.getValueAt(rowIndex, columnIndex);
	}

	/**
	 * @param viewRowIndex
	 * @return row index in Model form
	 */
	public int convertRowIndexToModel(int viewRowIndex) {
		return tblData.convertRowIndexToModel(viewRowIndex);
	}

	/**
	 * @param viewColumnIndex
	 * @return column index in Model form
	 */
	public int convertColumnIndexToModel(int viewColumnIndex) {
		return tblData.convertColumnIndexToModel(viewColumnIndex);
	}

	/**
	 * @return JSheet Table Model
	 */
	public TableModel getJSheetDataModel() {
		return compositeDataModel;
	}

	/**
	 * Checks whether Cell is Editable or not
	 * @param viewRowIndex
	 * @param viewColumnIndex
	 * @return Boolean Value
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int viewRowIndex, int viewColumnIndex) {
		return compositeDataModel.isCellEditable(viewRowIndex, viewColumnIndex);
	}

	/**
	 * Gets the Column Name
	 * @param column
	 * @return Column name
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return tblData.getColumnName(column);
	}
}
