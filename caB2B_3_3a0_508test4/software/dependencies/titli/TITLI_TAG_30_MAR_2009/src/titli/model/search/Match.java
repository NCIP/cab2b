/**
 * 
 */
package titli.model.search;


import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import titli.controller.Name;
import titli.controller.interfaces.MatchInterface;
import titli.controller.interfaces.record.RecordInterface;
import titli.model.TitliConstants;
import titli.model.fetch.Fetcher;
import titli.model.fetch.Record;
import titli.model.fetch.TitliFetchException;




/**
 *A class that represents a match in for a given search query
 * @author Juber Patel
 *
 */


public class Match implements MatchInterface
{
	//stores columns and and values alternately
	private Map<Name, String> uniqueKeys ;
	private String self;
	private String queryString;
	
	private Name dbName;
	private Name tableName;
	
	private Fetcher fetcher;
	private Record record;
	
		
	/**
	 * @param doc the document that matched the search query
	 * @param fetcher the Fetcher to be used to fetch the corresponding record 
	 * 
	 */
	Match(Document doc, Fetcher fetcher)
	{
		//initialize the fields
		
		this.fetcher = fetcher;
		this.tableName = new Name(doc.get(TitliConstants.DOCUMENT_TABLE_FIELD));
		this.dbName = new Name(doc.get(TitliConstants.DOCUMENT_DATABASE_FIELD));
		
		initUniqueKeys(doc);
					
	}
	
	
	
	/**
	 * the database name
	 * @return the name of the database
	 */
	public Name getDatabaseName()
	{
		return dbName;
	}
	
	/**
	 * the table name
	 * @return the name of the table
	 */
	public Name getTableName()
	{
		return tableName;
	}
	
	
	/**
	 * get the "column name" => "column value" map of the unique key set of the record
	 * @return the "column name" => "column value" map of the unique key set of the record
	 */
	public Map<Name, String> getUniqueKeys()
	{
		return new LinkedHashMap<Name, String>(uniqueKeys);
	}
	
	
	/**
	 * The SQL query that must be fired to fetch the record corresponding to the match
	 * @return the relevant SQL query
	 */
	public String getQueryString()
	{
		if(queryString==null)
		{
			//buld the corresponding SQL query
			StringBuilder query = new StringBuilder("SELECT * FROM ");
			
			query.append(tableName+" WHERE ");
			
			//add each column and value pair 
			for(Name colName : uniqueKeys.keySet())
			{
				query.append(colName+" = '"+uniqueKeys.get(colName)+"'  and ");
							
			}
			
			//remove the last 'and'
			queryString  = query.substring(0, query.lastIndexOf("and"));
		
		}
		
		return queryString;
	}
	
	/**
	 * String representation of the Match
	 * @return the string representation
	 */
	public String toString()
	{
		if(self==null)
		{
			//build string representation
			StringBuilder string = new StringBuilder("Database : "+dbName+"    Table : "+tableName);
			
			string.append("\nUnique Key Set : ");
			
			for(Name colName : uniqueKeys.keySet())
			{
				string.append("   "+colName+" : "+uniqueKeys.get(colName));
			}
			
			self = new String(string);
			
		}
		
		return self;
	}




	
	/**
	 * 
	 * @return the record corresponding to the match
	 * @throws TitliFetchException if problems occur
	 */
	//the record is fetched only once
	public RecordInterface fetch() throws TitliFetchException
	{
		if(record==null)
		{
			record = fetcher.fetch(this);
		}
		
		return record;
	}
	
	
	/**
	 * initialize the unique key set from the document 
	 * @param doc the document 
	 * 
	 */
	private void initUniqueKeys(Document doc)
	{
		uniqueKeys = new LinkedHashMap<Name, String> ();
		
		Enumeration e = doc.fields();
		
		while (e.hasMoreElements())
		{
			String name = ((Field)(e.nextElement())).name();
			
			//don't include the table  name and database name
			if(name.equals(TitliConstants.DOCUMENT_TABLE_FIELD) || name.equals(TitliConstants.DOCUMENT_DATABASE_FIELD))
			{
				continue;
			}
					
			uniqueKeys.put(new Name(name), doc.get(name));
		}
	
		
		
	}
	
}
