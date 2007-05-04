package edu.wustl.cab2b.client.ui.experiment;

import java.util.Map;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bDefaultTableModel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;

public class ExperimentTableModel extends Cab2bTable{

	Map<String,AttributeInterface> m_attributeMap;
	public ExperimentTableModel(boolean showCheckBox, Object[][] data, Object[] header) {
		super(showCheckBox, data, header);
		// TODO Auto-generated constructor stub
	}
	
	public ExperimentTableModel(boolean showCheckBox, Object[][] data, Object[] header,Map <String,AttributeInterface> attributeMap ) {
		super(showCheckBox, data, header);
		this.m_attributeMap=attributeMap;
		// TODO Auto-generated constructor stub
	}
	
	public ExperimentTableModel(boolean showCheckBox, Vector data, Vector headers)
	{
		super(showCheckBox, data, headers);
		
		
	}
	
	public AttributeInterface getColumnAttribute(String columnName)
	{
		return m_attributeMap.get(columnName);
	}
	

}
