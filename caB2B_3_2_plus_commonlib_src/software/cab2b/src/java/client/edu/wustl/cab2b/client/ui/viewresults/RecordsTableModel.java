package edu.wustl.cab2b.client.ui.viewresults;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.common.querysuite.queryobject.DataType;

/**
 *  Adapter for JSheet Data Model.
  * @author rahul_ner
*/

class RecordsTableModel extends AbstractTableModel {

	private List<IRecord> inputRecords = new ArrayList<IRecord>();

	private List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>();

	public RecordsTableModel(List<IRecord> inputRecords) {

		if (inputRecords != null && !inputRecords.isEmpty()) {

			this.inputRecords = inputRecords;

			attributeList = Utility.getAttributeList(inputRecords.get(0)
					.getAttributes());

		}

		fireTableDataChanged();

	}

	public int getColumnCount() {

		return getListSize(attributeList);

	}

	public String getColumnName(int columnNo) {

		return Utility.getFormattedString(attributeList.get(columnNo)
				.getName());

	}

	public Class<?> getColumnClass(int columnNo) {

		AttributeInterface attribute = attributeList.get(columnNo);

		DataType dataType = Utility.getDataType(attribute
				.getAttributeTypeInformation());

		if (dataType.equals(DataType.Date)) {

			return DataType.String.getJavaType();

		}

		return dataType.getJavaType();

	}

	/**
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 * 
	 */

	public Object getValueAt(int rowNo, int columnNo) {

		IRecord record = inputRecords.get(rowNo);

		Object value = record.getValueForAttribute(attributeList.get(columnNo));

		// if(value.equals("")) {

		// value = null;

		// }

		return value;

	}

	public int getRowCount() {

		return getListSize(inputRecords);

	}

	private int getListSize(List list) {

		return (list == null) ? 0 : list.size();

	}

}
