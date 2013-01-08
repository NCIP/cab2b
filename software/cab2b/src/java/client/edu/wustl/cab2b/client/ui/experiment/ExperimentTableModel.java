/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.experiment;

import java.util.Map;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

public class ExperimentTableModel extends Cab2bTable{

	private static final long serialVersionUID = 1L;
	
	private Map<String,AttributeInterface> m_attributeMap;
	
	/**
	 * @param showCheckBox
	 * @param data
	 * @param header
	 */
	public ExperimentTableModel(boolean showCheckBox, Object[][] data, Object[] header) {
		super(showCheckBox, data, header);
	}
	
	/**
	 * @param showCheckBox
	 * @param data
	 * @param header
	 * @param attributeMap
	 */
	public ExperimentTableModel(boolean showCheckBox, Object[][] data, Object[] header,Map <String,AttributeInterface> attributeMap ) {
		super(showCheckBox, data, header);
		this.m_attributeMap=attributeMap;
	}
	
	/**
	 * @param showCheckBox
	 * @param data
	 * @param headers
	 */
	public ExperimentTableModel(boolean showCheckBox, Vector data, Vector headers) {
		super(showCheckBox, data, headers);
	}
	
	/**
	 * Gets Column Attributes Interface for a given Column
	 * @param columnName
	 * @return Attribute Interface
	 */
	public AttributeInterface getColumnAttribute(String columnName) {
		return m_attributeMap.get(columnName);
	}
	
}
