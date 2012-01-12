/**
 * 
 */

package edu.wustl.common.querysuite.queryobject.impl.metadata;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;

/**
 * @author prafull_kadam
 * Class to store metadata of the output tree attributes. 
 * This will store the attribute of an output entity & its corresponding column name in the temporary table created while executing Suite Advance query. 
 */
public class QueryOutputTreeAttributeMetadata
{

	/**
	 * Reference to the attribute for which the metadata is to be stored.
	 */
	private AttributeInterface attribute;
	/**
	 * The name of the column in the temporary table for the given attribute.
	 */
	private String columnName;

	private String displayName;
	private OutputTreeDataNode treeDataNode;
	
	/**
	 * Constructor to instanciate this object.
	 * @param attribute The reference to the DE attribute.
	 * @param columnName The name of the column for the attribute passed in the temporary table.
	 * @param treeDataNode TODO
	 * @param displayName TODO
	 */
	public QueryOutputTreeAttributeMetadata(AttributeInterface attribute, String columnName, OutputTreeDataNode treeDataNode, String displayName)
	{
		this.attribute = attribute;
		this.columnName = columnName;
		this.treeDataNode = treeDataNode;
		this.displayName = displayName;
	}

	/**
	 * To get the attribute whose metadata is stored in this class.
	 * @return the attribute reference
	 */
	public AttributeInterface getAttribute()
	{
		return attribute;
	}

	/**
	 * TO get the table column name of the attribute.
	 * @return the columnName
	 */
	public String getColumnName()
	{
		return columnName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * @return the treeDataNode
	 */
	public OutputTreeDataNode getTreeDataNode()
	{
		return treeDataNode;
	}
	
	public String getUniqueId()
	{   
		String id = this.getTreeDataNode().getExpressionId()+ Constants.EXPRESSION_ID_SEPARATOR + this.attribute.getId(); //TODO
		return id;
	}
}
