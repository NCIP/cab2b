/**
 * 
 */
package titli.controller;

import java.util.Map;

import org.apache.lucene.document.Document;

import titli.model.TitliConstants;

/**
 * This class represents the identification of a record in databases
 * This identification includes database name, table name and a map of column name => column value pairs
 * for the columns that make up the unique key for the record  
 * @author Juber Patel
 *
 */
public class RecordIdentifier 
{
	private Name dbName;
	private Name tableName;
	private Map<Name, String> uniqueKey;
	
	/**
	 * 
	 * @param dbName the database name
	 * @param tableName the table name
	 * @param uniqueKey the map of unique key column name => column value
	 */
	public RecordIdentifier(Name dbName, Name tableName, Map<Name, String>uniqueKey)
	{
		this.dbName = dbName;
		this.tableName = tableName;
		this.uniqueKey = uniqueKey;
		
	}

	/**
	 * @return the dbName
	 */
	public Name getDbName() 
	{
		return dbName;
	}

	/**
	 * @return the tableName
	 */
	public Name getTableName() 
	{
		return tableName;
	}

	
	/**
	 * @return the uniqueKey
	 */
	public Map<Name, String> getUniqueKey() 
	{
		return uniqueKey;
	}
	
	
	/**
	 * determine whether this identifier represents the specified Document 
	 * @param doc the document
	 * @return true if this identifier represents the specified Document, otherwise false
	 */
	public boolean matches(Document doc)
	{  
		if(!doc.get(TitliConstants.DOCUMENT_DATABASE_FIELD).trim().equalsIgnoreCase(getDbName().toString()))
		{
			return false;
		}
		
		if(!doc.get(TitliConstants.DOCUMENT_TABLE_FIELD).trim().equalsIgnoreCase(getTableName().toString()))
		{
			return false;
		}
		
		for(Name column : getUniqueKey().keySet())
		{
			if(doc.get(column.toString())==null)
			{
				return false;
			}
			
			if(!doc.get(column.toString()).trim().equalsIgnoreCase(getUniqueKey().get(column)))
			{
				return false;
			}
		}
		
		return true;
		
	}
	
}
